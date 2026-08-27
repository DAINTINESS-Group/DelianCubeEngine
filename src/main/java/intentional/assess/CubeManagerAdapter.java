package intentional.assess;

import intentional.assess.utils.DatesHandler;
import cubemanager.CubeManager;
import cubemanager.cubebase.Cube;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.QueryMeasure;
import result.Result;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class collects the various data needed to define a QueryString for the
 * CubeManager and issues it.
 */
public class CubeManagerAdapter {
    private final CubeManager cubeManager;
    private String targetCubeName;
    private Map<String, String> selectionPredicates;

    private String aggregationFunction;
    private String measurement;
    private String measureExpression;
    private String measureAlias;
    private Set<String> groupBySet;
    private final DatesHandler datesHandler;

    public CubeManagerAdapter(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
        datesHandler = new DatesHandler(getDateLevels());
    }

    private List<String> getDateLevels() {
        return cubeManager
                .getDimensions()
                .stream()
                .filter(dimension -> dimension.getDimensionType().equals("date")) 
                .findFirst() //TODO To be fixed if more than 1 date dimensions
                .orElseThrow(() -> new RuntimeException("No date dimension in cube"))
                .getHierarchy()
                .get(0) // Because there's always only one hierarchy (At least, last time I checked)
                .getLevels()
                .stream()
                .map(Level::getName)
                .collect(Collectors.toList());

    }

    public void setTargetCubeName(String targetCubeName) {
        this.targetCubeName = targetCubeName;
    }

    public void setSelectionPredicates(Map<String, String> selectionPredicates) {
        this.selectionPredicates = selectionPredicates;
    }

    public Map<String, String> getSelectionPredicates() {
        return selectionPredicates;
    }

    public void setAggregationFunction(String aggregationFunction) {
        this.aggregationFunction = aggregationFunction;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    /**
     * A derived target measure: the base aggregate and column feed the query template, and the full
     * expression replaces the query's measure after translation — so every re-translation (each
     * benchmark date included) carries the expression.
     */
    public void setMeasureExpression(String expression, String alias,
                                     String baseAggregation, String baseColumn) {
        this.measureExpression = expression;
        this.measureAlias = alias;
        this.aggregationFunction = baseAggregation.toLowerCase();
        this.measurement = baseColumn;
    }

    public void setGroupBySet(Set<String> groupBySet) {
        this.groupBySet = groupBySet;
    }

    /** The name the target measure answers to: its alias when derived, its column otherwise. */
    public String getTargetMeasureReference() {
        return measureAlias != null ? measureAlias : measurement;
    }

    public CubeQuery translateToCubeQuery() {
        return translateFor(selectionPredicates);
    }

    /** Translates the query template under the given predicates, leaving this adapter's own untouched. */
    public CubeQuery translateFor(Map<String, String> predicates) {
        try {
            CubeQuery query = cubeManager.createCubeQueryFromString(generateQuery(predicates), new HashMap<>());
            if (measureExpression != null) {
                QueryMeasure derived = new QueryMeasure(measureAlias == null
                        ? measureExpression
                        : measureExpression + " AS " + measureAlias);
                query.getQueryMeasures().clear();
                query.addQueryMeasure(derived.getFunction(), derived.getName(), derived.getAlias());
            }
            return query;
        } catch (RemoteException re) {
            throw new RuntimeException(
                    "Failed to issue AssessQuery to the CubeManager\n" + re);
        }
    }

    public Result executeCubeQuery(CubeQuery cb) {
        return cubeManager.executeQuery(cb);
    }

    /**
     * Creates the query for the CubeManager
     *
     * @return the formatted query
     */
    private String generateQuery(Map<String, String> predicates) {
        return new StringJoiner("\n")
                .add("CubeName:" + targetCubeName)
                .add("Name:" + targetCubeName + "_" + measurement)
                .add("AggrFunc:" + aggregationFunction)
                .add("Measure:" + measurement)
                .add("Gamma:" + collectGammaLevels(targetCubeName + "_cube"))
                .add("Sigma:" + collectSigmaLevels(targetCubeName + "_cube", predicates))
                .toString();
    }

    private String collectSigmaLevels(String targetCubeName, Map<String, String> predicates) {
        // Early return when there are no selection predicates
        if (predicates == null) {
            return "";
        }

        Cube targetCube = cubeManager.getCubeByName(targetCubeName);
        StringJoiner stringJoiner = new StringJoiner(",");
        predicates.forEach((key, value) -> {
            String result = targetCube.findLevelByName(key);
            if (datesHandler.keyIsDate(key)) {
                value = DatesHandler.formatDates(value);
            }
            result += "='" + value + "'";
            stringJoiner.add(result);
        });
        return stringJoiner.toString();
    }

    private String collectGammaLevels(String targetCubeName) {
        Cube targetCube = cubeManager.getCubeByName(targetCubeName);
        StringJoiner stringJoiner = new StringJoiner(",");
        groupBySet.forEach(attribute ->
                stringJoiner.add(targetCube.findLevelByName(attribute)));
        return stringJoiner.toString();
    }

    /** Whether the key is a level of the date dimension. */
    public boolean isDateLevel(String key) {
        return datesHandler.keyIsDate(key);
    }

    public String getDateSelectionPredicate() {
        return selectionPredicates
                .keySet()
                .stream()
                .filter(datesHandler::keyIsDate)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "A date was not defined in the selection predicates"));
    }

}
