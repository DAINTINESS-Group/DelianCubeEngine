package assess;

import assess.utils.DatesHandler;
import cubemanager.CubeManager;
import cubemanager.cubebase.Cube;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Level;
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
                .filter(dimension -> dimension.getName().equals("date_dim")) // This name is database dependent
                .findFirst()
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

    public void updateSelectionPredicate(String key, String value) {
        selectionPredicates.put(key, value);
    }

    public void setAggregationFunction(String aggregationFunction) {
        this.aggregationFunction = aggregationFunction;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public void setGroupBySet(Set<String> groupBySet) {
        this.groupBySet = groupBySet;
    }

    public CubeQuery translateToCubeQuery() {
        try {
            return cubeManager.createCubeQueryFromString(generateQuery(), new HashMap<>());
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
    private String generateQuery() {
        return new StringJoiner("\n")
                .add("CubeName:" + targetCubeName)
                .add("Name:" + targetCubeName + "_" + measurement)
                .add("AggrFunc:" + aggregationFunction)
                .add("Measure:" + measurement)
                .add("Gamma:" + collectGammaLevels(targetCubeName + "_cube"))
                .add("Sigma:" + collectSigmaLevels(targetCubeName + "_cube"))
                .toString();
    }

    private String collectSigmaLevels(String targetCubeName) {
        // Early return when there are no selection predicates
        if (selectionPredicates == null) {
            return "";
        }

        Cube targetCube = cubeManager.getCubeByName(targetCubeName);
        StringJoiner stringJoiner = new StringJoiner(",");
        selectionPredicates.forEach((key, value) -> {
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

    public String getDateSelectionPredicate() {
        return selectionPredicates
                .keySet()
                .stream()
                .filter(datesHandler::keyIsDate)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "A date was not defined in the selection predicates"));
    }

    public List<Result> collectPastRecords(int pastRecordsNumber) {
        if (pastRecordsNumber <= 0) {
            throw new RuntimeException("Did not provide a valid number of past Records");
        }
        String dateLevel = getDateSelectionPredicate();
        String currentDate = selectionPredicates.get(dateLevel);
        List<String> pastDates = DatesHandler.decrementDate(currentDate, dateLevel, pastRecordsNumber);
        List<Result> pastRecords = new ArrayList<>();

        for (String date : pastDates) {
            updateSelectionPredicate(dateLevel, date);
            Result queryResult = executeCubeQuery(translateToCubeQuery());
            if (!queryResult.getCells().isEmpty()) // If the result is not empty
                pastRecords.add(queryResult);
        }
        return pastRecords;
    }
}
