package LLM.experiments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpectedCubeQuery {

    private final String cubeName;
    private final String aggregateFunction;
    private final String measure;

    private final List<String> gammaFields;
    private final List<String> sigmaConditions;

    public ExpectedCubeQuery(
            String cubeName,
            String aggregateFunction,
            String measure,
            List<String> gammaFields,
            List<String> sigmaConditions
    ) {
        validateRequiredField(
                "cubeName",
                cubeName
        );

        validateRequiredField(
                "aggregateFunction",
                aggregateFunction
        );

        validateRequiredField(
                "measure",
                measure
        );

        this.cubeName =
                safeString(cubeName);

        this.aggregateFunction =
                safeString(aggregateFunction);

        this.measure =
                safeString(measure);

        this.gammaFields =
                copyList(gammaFields);

        this.sigmaConditions =
                copyList(sigmaConditions);
    }

    public String getCubeName() {
        return cubeName;
    }

    public String getAggregateFunction() {
        return aggregateFunction;
    }

    public String getMeasure() {
        return measure;
    }

    public List<String> getGammaFields() {
        return Collections.unmodifiableList(
                gammaFields
        );
    }

    public List<String> getSigmaConditions() {
        return Collections.unmodifiableList(
                sigmaConditions
        );
    }

    private static void validateRequiredField(
            String fieldName,
            String value
    ) {
        if (value == null
                || value.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    fieldName
                            + " cannot be null or empty."
            );
        }
    }

    private static String safeString(
            String value
    ) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private static List<String> copyList(
            List<String> values
    ) {
        List<String> copiedValues =
                new ArrayList<String>();

        if (values == null) {
            return copiedValues;
        }

        for (String value : values) {

            if (value == null) {
                continue;
            }

            String trimmedValue =
                    value.trim();

            if (!trimmedValue.isEmpty()) {
                copiedValues.add(
                        trimmedValue
                );
            }
        }

        return copiedValues;
    }

    @Override
    public String toString() {

        return "ExpectedCubeQuery{" +

                "cubeName='"
                + cubeName
                + '\'' +

                ", aggregateFunction='"
                + aggregateFunction
                + '\'' +

                ", measure='"
                + measure
                + '\'' +

                ", gammaFields="
                + gammaFields +

                ", sigmaConditions="
                + sigmaConditions +

                '}';
    }
}