package LLM.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import LLM.normalization.CubeQueryNormalizer.NormalizationResult;
import LLM.query.ParsedCubeQuery;
import LLM.validation.CubeFieldValidator;

public class CubeQueryValidator {

    private CubeQueryValidator() {
    }


    public static ValidationResult validate(
            NormalizationResult normalizationResult,
            ExpectedCubeQuery expectedQuery,
            CubeFieldValidator cubeFieldValidator
    ) {

        return validateInternal(
                normalizationResult,
                expectedQuery,
                cubeFieldValidator,
                false
        );
    }


    public static ValidationResult validateIgnoringSigmaValues(
            NormalizationResult normalizationResult,
            ExpectedCubeQuery expectedQuery,
            CubeFieldValidator cubeFieldValidator
    ) {

        return validateInternal(
                normalizationResult,
                expectedQuery,
                cubeFieldValidator,
                true
        );
    }


    private static ValidationResult validateInternal(
            NormalizationResult normalizationResult,
            ExpectedCubeQuery expectedQuery,
            CubeFieldValidator cubeFieldValidator,
            boolean ignoreSigmaValueErrors
    ) {

        List<String> errors =
                new ArrayList<String>();


        if (expectedQuery == null) {

            errors.add(
                    "Expected query is null."
            );

            return createFailedValidationResult(
                    errors
            );
        }


        if (normalizationResult == null) {

            errors.add(
                    "Normalization result is null."
            );

            return createFailedValidationResult(
                    errors
            );
        }


        if (!normalizationResult.isSuccess()) {

            errors.add(
                    "Query normalization failed."
            );

            errors.addAll(
                    normalizationResult.getErrors()
            );

            return createFailedValidationResult(
                    errors
            );
        }


        ParsedCubeQuery actualQuery =
                normalizationResult.getParsedQuery();


        if (actualQuery == null) {

            errors.add(
                    "Normalized query is null."
            );

            return createFailedValidationResult(
                    errors
            );
        }


        boolean cubeCorrect =
                compareExact(
                        "cubeName",
                        expectedQuery.getCubeName(),
                        actualQuery.getCubeName(),
                        errors
                );


        boolean aggregateCorrect =
                compareIgnoreCase(
                        "aggregateFunction",
                        expectedQuery.getAggregateFunction(),
                        actualQuery.getAggregateFunction(),
                        errors
                );


        boolean measureCorrect =
                compareExact(
                        "measure",
                        expectedQuery.getMeasure(),
                        actualQuery.getMeasure(),
                        errors
                );


        FieldSetComparison gammaComparison =
                compareGammaFields(
                        expectedQuery.getGammaFields(),
                        actualQuery.getGammaFields(),
                        cubeFieldValidator,
                        errors
                );


        SigmaComparison sigmaComparison =
                compareSigmaConditions(
                        expectedQuery.getSigmaConditions(),
                        actualQuery.getSigmaConditions(),
                        cubeFieldValidator,
                        errors,
                        ignoreSigmaValueErrors
                );


        int unknownFieldsCount =
                gammaComparison.getUnknownFieldsCount()
                        + sigmaComparison.getUnknownFieldsCount();


        boolean gammaCorrect =
                gammaComparison.isCorrect();


        boolean sigmaCorrect =
                sigmaComparison.isCorrect();


        double weightedScore =
                calculateWeightedScore(
                        cubeCorrect,
                        aggregateCorrect,
                        measureCorrect,
                        gammaComparison,
                        sigmaComparison
                );


        boolean valid =
                cubeCorrect
                        && aggregateCorrect
                        && measureCorrect
                        && gammaCorrect
                        && sigmaCorrect
                        && unknownFieldsCount == 0;


        return new ValidationResult(
                valid,
                true,
                cubeCorrect,
                aggregateCorrect,
                measureCorrect,
                gammaCorrect,
                sigmaCorrect,
                unknownFieldsCount,
                gammaComparison.getMissingCount(),
                gammaComparison.getExtraCount(),
                sigmaComparison.getMissingCount(),
                sigmaComparison.getExtraCount(),
                sigmaComparison.getWrongValueCount(),
                weightedScore,
                errors
        );
    }


    private static boolean compareExact(
            String fieldName,
            String expected,
            String actual,
            List<String> errors
    ) {

        String expectedValue =
                safe(
                        expected
                );


        String actualValue =
                safe(
                        actual
                );


        boolean correct =
                expectedValue.equals(
                        actualValue
                );


        if (!correct) {

            errors.add(
                    fieldName
                            + " validation failed."
            );

            errors.add(
                    "Expected "
                            + fieldName
                            + ": "
                            + expectedValue
            );

            errors.add(
                    "Actual "
                            + fieldName
                            + ": "
                            + actualValue
            );
        }


        return correct;
    }


    private static boolean compareIgnoreCase(
            String fieldName,
            String expected,
            String actual,
            List<String> errors
    ) {

        String expectedValue =
                safe(
                        expected
                );


        String actualValue =
                safe(
                        actual
                );


        boolean correct =
                expectedValue.equalsIgnoreCase(
                        actualValue
                );


        if (!correct) {

            errors.add(
                    fieldName
                            + " validation failed."
            );

            errors.add(
                    "Expected "
                            + fieldName
                            + ": "
                            + expectedValue
            );

            errors.add(
                    "Actual "
                            + fieldName
                            + ": "
                            + actualValue
            );
        }


        return correct;
    }


    private static FieldSetComparison compareGammaFields(
            List<String> expectedGammaFields,
            List<String> actualGammaFields,
            CubeFieldValidator cubeFieldValidator,
            List<String> errors
    ) {

        Set<String> expectedSet =
                toSet(
                        expectedGammaFields
                );


        Set<String> actualSet =
                toSet(
                        actualGammaFields
                );


        Set<String> missing =
                new HashSet<String>(
                        expectedSet
                );


        missing.removeAll(
                actualSet
        );


        Set<String> extra =
                new HashSet<String>(
                        actualSet
                );


        extra.removeAll(
                expectedSet
        );


        Set<String> unknown =
                findUnknownFields(
                        actualSet,
                        cubeFieldValidator
                );


        boolean correct =
                missing.isEmpty()
                        && extra.isEmpty()
                        && unknown.isEmpty();


        if (!correct) {

            errors.add(
                    "Gamma validation failed."
            );


            if (!missing.isEmpty()) {

                errors.add(
                        "Missing gamma fields: "
                                + missing
                );
            }


            if (!extra.isEmpty()) {

                errors.add(
                        "Extra gamma fields: "
                                + extra
                );
            }


            if (!unknown.isEmpty()) {

                errors.add(
                        "Unknown gamma fields: "
                                + unknown
                );
            }
        }


        double partialScoreRatio =
                calculateSetScoreRatio(
                        expectedSet,
                        actualSet
                );


        return new FieldSetComparison(
                correct,
                missing.size(),
                extra.size(),
                unknown.size(),
                partialScoreRatio
        );
    }


    private static SigmaComparison compareSigmaConditions(
            List<String> expectedSigmaConditions,
            List<String> actualSigmaConditions,
            CubeFieldValidator cubeFieldValidator,
            List<String> errors,
            boolean ignoreSigmaValueErrors
    ) {

        Map<String, String> expectedMap =
                toSigmaMap(
                        expectedSigmaConditions
                );


        Map<String, String> actualMap =
                toSigmaMap(
                        actualSigmaConditions
                );


        Set<String> expectedFields =
                expectedMap.keySet();


        Set<String> actualFields =
                actualMap.keySet();


        Set<String> missingFields =
                new HashSet<String>();


        Set<String> extraFields =
                new HashSet<String>();


        Set<String> wrongValueFields =
                new HashSet<String>();


        for (String expectedField
                : expectedFields) {

            if (!actualMap.containsKey(
                    expectedField
            )) {

                missingFields.add(
                        expectedField
                );

                continue;
            }


            String expectedValue =
                    expectedMap.get(
                            expectedField
                    );


            String actualValue =
                    actualMap.get(
                            expectedField
                    );


            if (!expectedValue.equals(
                    actualValue
            )) {

                wrongValueFields.add(
                        expectedField
                );
            }
        }


        for (String actualField
                : actualFields) {

            if (!expectedMap.containsKey(
                    actualField
            )) {

                extraFields.add(
                        actualField
                );
            }
        }


        Set<String> unknownFields =
                findUnknownFields(
                        actualFields,
                        cubeFieldValidator
                );


        boolean correct =
                missingFields.isEmpty()
                        && extraFields.isEmpty()
                        && unknownFields.isEmpty()
                        && (
                        ignoreSigmaValueErrors
                                || wrongValueFields.isEmpty()
                );


        if (!correct) {

            errors.add(
                    "Sigma validation failed."
            );


            if (!missingFields.isEmpty()) {

                errors.add(
                        "Missing sigma fields: "
                                + missingFields
                );
            }


            if (!extraFields.isEmpty()) {

                errors.add(
                        "Extra sigma fields: "
                                + extraFields
                );
            }


            if (!wrongValueFields.isEmpty()
                    && !ignoreSigmaValueErrors) {

                errors.add(
                        "Wrong sigma values: "
                                + wrongValueFields
                );
            }


            if (!unknownFields.isEmpty()) {

                errors.add(
                        "Unknown sigma fields: "
                                + unknownFields
                );
            }
        }


        double partialScoreRatio;


        if (ignoreSigmaValueErrors) {

            partialScoreRatio =
                    calculateSetScoreRatio(
                            expectedFields,
                            actualFields
                    );

        } else {

            partialScoreRatio =
                    calculateSigmaScoreRatio(
                            expectedMap,
                            actualMap
                    );
        }


        return new SigmaComparison(
                correct,
                missingFields.size(),
                extraFields.size(),
                wrongValueFields.size(),
                unknownFields.size(),
                partialScoreRatio
        );
    }


    private static Map<String, String> toSigmaMap(
            List<String> sigmaConditions
    ) {

        Map<String, String> result =
                new HashMap<String, String>();


        if (sigmaConditions == null) {

            return result;
        }


        for (String condition
                : sigmaConditions) {

            if (condition == null
                    || condition.trim().isEmpty()) {

                continue;
            }


            int equalsIndex =
                    condition.indexOf('=');


            if (equalsIndex == -1) {

                continue;
            }


            String field =
                    condition.substring(
                            0,
                            equalsIndex
                    );


            String value =
                    condition.substring(
                            equalsIndex + 1
                    );


            if (value.startsWith("'")
                    && value.endsWith("'")
                    && value.length() >= 2) {

                value =
                        value.substring(
                                1,
                                value.length() - 1
                        );
            }


            result.put(
                    field,
                    value
            );
        }


        return result;
    }


    private static Set<String> toSet(
            List<String> values
    ) {

        Set<String> result =
                new HashSet<String>();


        if (values == null) {

            return result;
        }


        for (String value : values) {

            if (value == null
                    || value.isEmpty()) {

                continue;
            }


            result.add(
                    value
            );
        }


        return result;
    }


    private static Set<String> findUnknownFields(
            Set<String> actualFields,
            CubeFieldValidator cubeFieldValidator
    ) {

        if (cubeFieldValidator == null) {

            return new HashSet<String>();
        }


        return cubeFieldValidator.findUnknownFields(
                actualFields
        );
    }


    private static double calculateWeightedScore(
            boolean cubeCorrect,
            boolean aggregateCorrect,
            boolean measureCorrect,
            FieldSetComparison gammaComparison,
            SigmaComparison sigmaComparison
    ) {

        double score =
                0.0;


        if (cubeCorrect) {

            score +=
                    QueryComponentWeights.CUBE;
        }


        if (aggregateCorrect) {

            score +=
                    QueryComponentWeights.AGGREGATE;
        }


        if (measureCorrect) {

            score +=
                    QueryComponentWeights.MEASURE;
        }


        score +=
                QueryComponentWeights.GAMMA
                        * gammaComparison
                        .getPartialScoreRatio();


        score +=
                QueryComponentWeights.SIGMA
                        * sigmaComparison
                        .getPartialScoreRatio();


        return roundTwoDecimals(
                score
        );
    }


    private static double calculateSetScoreRatio(
            Set<String> expectedSet,
            Set<String> actualSet
    ) {

        if (expectedSet.isEmpty()
                && actualSet.isEmpty()) {

            return 1.0;
        }


        if (expectedSet.isEmpty()
                || actualSet.isEmpty()) {

            return 0.0;
        }


        Set<String> intersection =
                new HashSet<String>(
                        expectedSet
                );


        intersection.retainAll(
                actualSet
        );


        int denominator =
                Math.max(
                        expectedSet.size(),
                        actualSet.size()
                );


        return intersection.size()
                / (double) denominator;
    }


    private static double calculateSigmaScoreRatio(
            Map<String, String> expectedMap,
            Map<String, String> actualMap
    ) {

        if (expectedMap.isEmpty()
                && actualMap.isEmpty()) {

            return 1.0;
        }


        if (expectedMap.isEmpty()
                || actualMap.isEmpty()) {

            return 0.0;
        }


        int correctConditions =
                0;


        for (String expectedField
                : expectedMap.keySet()) {

            if (!actualMap.containsKey(
                    expectedField
            )) {

                continue;
            }


            if (expectedMap.get(
                    expectedField
            ).equals(
                    actualMap.get(
                            expectedField
                    )
            )) {

                correctConditions++;
            }
        }


        int denominator =
                Math.max(
                        expectedMap.size(),
                        actualMap.size()
                );


        return correctConditions
                / (double) denominator;
    }


    private static ValidationResult createFailedValidationResult(
            List<String> errors
    ) {

        return new ValidationResult(
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                0,
                0,
                0,
                0,
                0,
                0,
                0.0,
                errors
        );
    }


    private static String safe(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }


    private static double roundTwoDecimals(
            double value
    ) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }


    private static class FieldSetComparison {

        private final boolean correct;
        private final int missingCount;
        private final int extraCount;
        private final int unknownFieldsCount;
        private final double partialScoreRatio;


        public FieldSetComparison(
                boolean correct,
                int missingCount,
                int extraCount,
                int unknownFieldsCount,
                double partialScoreRatio
        ) {

            this.correct =
                    correct;

            this.missingCount =
                    missingCount;

            this.extraCount =
                    extraCount;

            this.unknownFieldsCount =
                    unknownFieldsCount;

            this.partialScoreRatio =
                    partialScoreRatio;
        }


        public boolean isCorrect() {

            return correct;
        }


        public int getMissingCount() {

            return missingCount;
        }


        public int getExtraCount() {

            return extraCount;
        }


        public int getUnknownFieldsCount() {

            return unknownFieldsCount;
        }


        public double getPartialScoreRatio() {

            return partialScoreRatio;
        }
    }


    private static class SigmaComparison {

        private final boolean correct;
        private final int missingCount;
        private final int extraCount;
        private final int wrongValueCount;
        private final int unknownFieldsCount;
        private final double partialScoreRatio;


        public SigmaComparison(
                boolean correct,
                int missingCount,
                int extraCount,
                int wrongValueCount,
                int unknownFieldsCount,
                double partialScoreRatio
        ) {

            this.correct =
                    correct;

            this.missingCount =
                    missingCount;

            this.extraCount =
                    extraCount;

            this.wrongValueCount =
                    wrongValueCount;

            this.unknownFieldsCount =
                    unknownFieldsCount;

            this.partialScoreRatio =
                    partialScoreRatio;
        }


        public boolean isCorrect() {

            return correct;
        }


        public int getMissingCount() {

            return missingCount;
        }


        public int getExtraCount() {

            return extraCount;
        }


        public int getWrongValueCount() {

            return wrongValueCount;
        }


        public int getUnknownFieldsCount() {

            return unknownFieldsCount;
        }


        public double getPartialScoreRatio() {

            return partialScoreRatio;
        }
    }
}