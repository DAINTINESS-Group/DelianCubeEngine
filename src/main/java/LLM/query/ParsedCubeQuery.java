package LLM.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParsedCubeQuery {

    private final String cubeName;
    private final String queryName;
    private final String aggregateFunction;
    private final String measure;
    private final List<String> gammaFields;
    private final List<String> sigmaConditions;

    public ParsedCubeQuery(
            String cubeName,
            String queryName,
            String aggregateFunction,
            String measure,
            List<String> gammaFields,
            List<String> sigmaConditions
    ) {
        this.cubeName = safeString(cubeName);
        this.queryName = safeString(queryName);
        this.aggregateFunction = safeString(aggregateFunction);
        this.measure = safeString(measure);
        this.gammaFields = copyList(gammaFields);
        this.sigmaConditions = copyList(sigmaConditions);
    }

    public String getCubeName() {
        return cubeName;
    }

    public String getQueryName() {
        return queryName;
    }

    public String getAggregateFunction() {
        return aggregateFunction;
    }

    public String getMeasure() {
        return measure;
    }

    public List<String> getGammaFields() {
        return Collections.unmodifiableList(gammaFields);
    }

    public List<String> getSigmaConditions() {
        return Collections.unmodifiableList(sigmaConditions);
    }

    public boolean hasCubeName() {
        return !cubeName.isEmpty();
    }

    public boolean hasAggregateFunction() {
        return !aggregateFunction.isEmpty();
    }

    public boolean hasMeasure() {
        return !measure.isEmpty();
    }

    public boolean hasRequiredCoreFields() {
        return hasCubeName()
                && hasAggregateFunction()
                && hasMeasure();
    }

    private static String safeString(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private static List<String> copyList(List<String> values) {
        List<String> copiedValues = new ArrayList<String>();

        if (values == null) {
            return copiedValues;
        }

        for (String value : values) {
            if (value == null) {
                continue;
            }

            String trimmedValue = value.trim();

            if (!trimmedValue.isEmpty()) {
                copiedValues.add(trimmedValue);
            }
        }

        return copiedValues;
    }

    @Override
    public String toString() {
        return "ParsedCubeQuery{" +
                "cubeName='" + cubeName + '\'' +
                ", queryName='" + queryName + '\'' +
                ", aggregateFunction='" + aggregateFunction + '\'' +
                ", measure='" + measure + '\'' +
                ", gammaFields=" + gammaFields +
                ", sigmaConditions=" + sigmaConditions +
                '}';
    }
}