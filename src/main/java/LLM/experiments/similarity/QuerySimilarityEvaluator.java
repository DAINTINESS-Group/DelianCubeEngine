package LLM.experiments.similarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import LLM.experiments.ExpectedCubeQuery;
import LLM.query.ParsedCubeQuery;
import LLM.experiments.QueryComponentWeights;

public class QuerySimilarityEvaluator {

    private final HierarchyMetadataIndex hierarchyIndex;

    public QuerySimilarityEvaluator(HierarchyMetadataIndex hierarchyIndex) {
        if (hierarchyIndex == null) {
            throw new IllegalArgumentException("hierarchyIndex cannot be null.");
        }

        this.hierarchyIndex = hierarchyIndex;
    }

    public QuerySimilarityResult evaluate(
            ExpectedCubeQuery expected,
            ParsedCubeQuery actual) {

        if (expected == null || actual == null) {
            return QuerySimilarityResult.unavailable();
        }

        double cubeSimilarity = exactSimilarity(
                expected.getCubeName(),
                actual.getCubeName()
        );

        double aggregateSimilarity = aggregateSimilarity(
                expected.getAggregateFunction(),
                actual.getAggregateFunction()
        );

        double measureSimilarity = exactSimilarity(
                expected.getMeasure(),
                actual.getMeasure()
        );

        double gammaSimilarity = calculateGammaSimilarity(
                expected.getGammaFields(),
                actual.getGammaFields()
        );

        double sigmaSimilarity = calculateSigmaSimilarity(
                expected.getSigmaConditions(),
                actual.getSigmaConditions()
        );

        double overallSimilarity = (
                QueryComponentWeights.CUBE * cubeSimilarity
                        + QueryComponentWeights.AGGREGATE * aggregateSimilarity
                        + QueryComponentWeights.MEASURE * measureSimilarity
                        + QueryComponentWeights.GAMMA * gammaSimilarity
                        + QueryComponentWeights.SIGMA * sigmaSimilarity
        ) / QueryComponentWeights.TOTAL;

        return new QuerySimilarityResult(
                true,
                overallSimilarity,
                cubeSimilarity,
                aggregateSimilarity,
                measureSimilarity,
                gammaSimilarity,
                sigmaSimilarity
        );
    }

    private double calculateGammaSimilarity(
            List<String> expectedGamma,
            List<String> actualGamma) {

        List<String> expected = normalizeUniqueFields(expectedGamma);
        List<String> actual = normalizeUniqueFields(actualGamma);

        if (expected.isEmpty() && actual.isEmpty()) {
            return 1.0;
        }

        if (expected.isEmpty() || actual.isEmpty()) {
            return 0.0;
        }

        return calculateBestFieldMatchingSimilarity(expected, actual);
    }

    private double calculateBestFieldMatchingSimilarity(
            List<String> expected,
            List<String> actual) {

        List<FieldPairScore> scores = new ArrayList<FieldPairScore>();

        for (int i = 0; i < expected.size(); i++) {
            for (int j = 0; j < actual.size(); j++) {

                double similarity = calculateGammaFieldSimilarity(
                        expected.get(i),
                        actual.get(j)
                );

                if (similarity > 0.0) {
                    scores.add(new FieldPairScore(i, j, similarity));
                }
            }
        }

        sortScoresDescending(scores);

        Set<Integer> usedExpected = new HashSet<Integer>();
        Set<Integer> usedActual = new HashSet<Integer>();
        double totalSimilarity = 0.0;

        for (FieldPairScore score : scores) {
            if (usedExpected.contains(score.getExpectedIndex())) {
                continue;
            }

            if (usedActual.contains(score.getActualIndex())) {
                continue;
            }

            usedExpected.add(score.getExpectedIndex());
            usedActual.add(score.getActualIndex());
            totalSimilarity += score.getSimilarity();
        }

        int denominator = Math.max(expected.size(), actual.size());

        if (denominator == 0) {
            return 1.0;
        }

        return clamp(totalSimilarity / denominator);
    }

    private double calculateGammaFieldSimilarity(
            String expectedField,
            String actualField) {

        String normalizedExpected = normalizeFieldName(expectedField);
        String normalizedActual = normalizeFieldName(actualField);

        if (normalizedExpected.equals(normalizedActual)) {
            return 1.0;
        }

        FieldHierarchyInfo expectedInfo =
                hierarchyIndex.getFieldInfo(normalizedExpected);

        FieldHierarchyInfo actualInfo =
                hierarchyIndex.getFieldInfo(normalizedActual);

        if (expectedInfo == null || actualInfo == null) {
            return 0.0;
        }

        if (!expectedInfo.belongsToSameHierarchy(actualInfo)) {
            return 0.0;
        }

        if (expectedInfo.belongsToSameLevel(actualInfo)) {
            return 0.0;
        }

        int levelDistance =
                expectedInfo.getLevelDistance(actualInfo);

        if (levelDistance < 0) {
            return 0.0;
        }

        int hierarchySize = Math.max(
                expectedInfo.getHierarchySize(),
                actualInfo.getHierarchySize()
        );

        if (hierarchySize <= 1) {
            return 0.0;
        }

        double similarity = 1.0
                - (levelDistance / (double) (hierarchySize - 1));

        return clamp(similarity);
    }

    private double calculateSigmaSimilarity(
            List<String> expectedSigma,
            List<String> actualSigma) {

        List<SigmaCondition> expected =
                parseSigmaConditions(expectedSigma);

        List<SigmaCondition> actual =
                parseSigmaConditions(actualSigma);

        if (expected.isEmpty() && actual.isEmpty()) {
            return 1.0;
        }

        if (expected.isEmpty() || actual.isEmpty()) {
            return 0.0;
        }

        List<SigmaPairScore> scores =
                new ArrayList<SigmaPairScore>();

        for (int i = 0; i < expected.size(); i++) {
            for (int j = 0; j < actual.size(); j++) {

                double similarity =
                        calculateSigmaConditionSimilarity(
                                expected.get(i),
                                actual.get(j)
                        );

                if (similarity > 0.0) {
                    scores.add(
                            new SigmaPairScore(
                                    i,
                                    j,
                                    similarity
                            )
                    );
                }
            }
        }

        sortSigmaScoresDescending(scores);

        Set<Integer> usedExpected = new HashSet<Integer>();
        Set<Integer> usedActual = new HashSet<Integer>();
        double totalSimilarity = 0.0;

        for (SigmaPairScore score : scores) {
            if (usedExpected.contains(score.getExpectedIndex())) {
                continue;
            }

            if (usedActual.contains(score.getActualIndex())) {
                continue;
            }

            usedExpected.add(score.getExpectedIndex());
            usedActual.add(score.getActualIndex());
            totalSimilarity += score.getSimilarity();
        }

        int denominator = Math.max(expected.size(), actual.size());

        if (denominator == 0) {
            return 1.0;
        }

        return clamp(totalSimilarity / denominator);
    }

    private double calculateSigmaConditionSimilarity(
            SigmaCondition expected,
            SigmaCondition actual) {

        if (expected == null || actual == null) {
            return 0.0;
        }

        String expectedField = normalizeFieldName(
                expected.getField()
        );

        String actualField = normalizeFieldName(
                actual.getField()
        );

        if (expectedField.equals(actualField)) {

            if (normalizeSigmaValue(expected.getValue())
                    .equals(normalizeSigmaValue(actual.getValue()))) {
                return 1.0;
            }

            FieldHierarchyInfo info =
                    hierarchyIndex.getFieldInfo(expectedField);

            if (info == null || info.getHierarchySize() <= 0) {
                return 0.5;
            }

            double similarity =
                    1.0 - (1.0 / info.getHierarchySize());

            return clamp(similarity);
        }

        FieldHierarchyInfo expectedInfo =
                hierarchyIndex.getFieldInfo(expectedField);

        FieldHierarchyInfo actualInfo =
                hierarchyIndex.getFieldInfo(actualField);

        if (expectedInfo == null || actualInfo == null) {
            return 0.0;
        }

        if (!expectedInfo.belongsToSameHierarchy(actualInfo)) {
            return 0.0;
        }

        if (expectedInfo.belongsToSameLevel(actualInfo)) {
            return 0.0;
        }

        int levelDistance =
                expectedInfo.getLevelDistance(actualInfo);

        if (levelDistance < 0) {
            return 0.0;
        }

        int hierarchySize = Math.max(
                expectedInfo.getHierarchySize(),
                actualInfo.getHierarchySize()
        );

        if (hierarchySize <= 0) {
            return 0.0;
        }

        double distance = levelDistance + 1.0;

        double similarity =
                1.0 - (distance / hierarchySize);

        return clamp(similarity);
    }

    private static double exactSimilarity(
            String expected,
            String actual) {

        String normalizedExpected =
                normalizeSimpleValue(expected);

        String normalizedActual =
                normalizeSimpleValue(actual);

        return normalizedExpected.equals(normalizedActual)
                ? 1.0
                : 0.0;
    }

    private static double aggregateSimilarity(
            String expected,
            String actual) {

        String normalizedExpected =
                normalizeSimpleValue(expected);

        String normalizedActual =
                normalizeSimpleValue(actual);

        return normalizedExpected.equalsIgnoreCase(normalizedActual)
                ? 1.0
                : 0.0;
    }

    private static List<SigmaCondition> parseSigmaConditions(
            List<String> conditions) {

        List<SigmaCondition> result =
                new ArrayList<SigmaCondition>();

        if (conditions == null) {
            return result;
        }

        Set<String> seenConditions = new HashSet<String>();

        for (String condition : conditions) {

            if (condition == null) {
                continue;
            }

            String clean =
                    removeTrailingSemicolon(condition.trim());

            if (clean.isEmpty()) {
                continue;
            }

            int equalsIndex = clean.indexOf("=");

            String field;
            String value;

            if (equalsIndex < 0) {
                field = normalizeFieldName(clean);
                value = "";
            } else {
                field = normalizeFieldName(
                        clean.substring(0, equalsIndex)
                );

                value = normalizeSigmaValue(
                        clean.substring(equalsIndex + 1)
                );
            }

            if (field.isEmpty()) {
                continue;
            }

            String key = field + "=" + value;

            if (seenConditions.contains(key)) {
                continue;
            }

            seenConditions.add(key);

            result.add(new SigmaCondition(field, value));
        }

        return result;
    }

    private static List<String> normalizeUniqueFields(
            List<String> fields) {

        List<String> result = new ArrayList<String>();
        Set<String> seen = new HashSet<String>();

        if (fields == null) {
            return result;
        }

        for (String field : fields) {

            String normalized = normalizeFieldName(field);

            if (normalized.isEmpty()) {
                continue;
            }

            if (seen.contains(normalized)) {
                continue;
            }

            seen.add(normalized);
            result.add(normalized);
        }

        return result;
    }

    private static String normalizeSimpleValue(String value) {
        if (value == null) {
            return "";
        }

        return removeTrailingSemicolon(value.trim());
    }

    private static String normalizeFieldName(String fieldName) {
        if (fieldName == null) {
            return "";
        }

        return removeTrailingSemicolon(fieldName)
                .trim()
                .replaceAll("\\s+", "");
    }

    private static String normalizeSigmaValue(String value) {
        if (value == null) {
            return "";
        }

        String normalized =
                removeTrailingSemicolon(value.trim());

        normalized = normalized.replace("\"", "'");

        if (normalized.startsWith("'")
                && normalized.endsWith("'")
                && normalized.length() >= 2) {

            normalized = normalized.substring(
                    1,
                    normalized.length() - 1
            );
        }

        return normalized.trim();
    }

    private static String removeTrailingSemicolon(String value) {
        if (value == null) {
            return "";
        }

        String result = value.trim();

        while (result.endsWith(";")) {
            result = result.substring(
                    0,
                    result.length() - 1
            ).trim();
        }

        return result;
    }

    private static void sortScoresDescending(
            List<FieldPairScore> scores) {

        java.util.Collections.sort(
                scores,
                new java.util.Comparator<FieldPairScore>() {
                    @Override
                    public int compare(
                            FieldPairScore left,
                            FieldPairScore right) {

                        return Double.compare(
                                right.getSimilarity(),
                                left.getSimilarity()
                        );
                    }
                }
        );
    }

    private static void sortSigmaScoresDescending(
            List<SigmaPairScore> scores) {

        java.util.Collections.sort(
                scores,
                new java.util.Comparator<SigmaPairScore>() {
                    @Override
                    public int compare(
                            SigmaPairScore left,
                            SigmaPairScore right) {

                        return Double.compare(
                                right.getSimilarity(),
                                left.getSimilarity()
                        );
                    }
                }
        );
    }

    private static double clamp(double value) {
        if (Double.isNaN(value)
                || Double.isInfinite(value)) {
            return 0.0;
        }

        if (value < 0.0) {
            return 0.0;
        }

        if (value > 1.0) {
            return 1.0;
        }

        return value;
    }

    private static class FieldPairScore {

        private final int expectedIndex;
        private final int actualIndex;
        private final double similarity;

        public FieldPairScore(
                int expectedIndex,
                int actualIndex,
                double similarity) {

            this.expectedIndex = expectedIndex;
            this.actualIndex = actualIndex;
            this.similarity = similarity;
        }

        public int getExpectedIndex() {
            return expectedIndex;
        }

        public int getActualIndex() {
            return actualIndex;
        }

        public double getSimilarity() {
            return similarity;
        }
    }

    private static class SigmaPairScore {

        private final int expectedIndex;
        private final int actualIndex;
        private final double similarity;

        public SigmaPairScore(
                int expectedIndex,
                int actualIndex,
                double similarity) {

            this.expectedIndex = expectedIndex;
            this.actualIndex = actualIndex;
            this.similarity = similarity;
        }

        public int getExpectedIndex() {
            return expectedIndex;
        }

        public int getActualIndex() {
            return actualIndex;
        }

        public double getSimilarity() {
            return similarity;
        }
    }

    private static class SigmaCondition {

        private final String field;
        private final String value;

        public SigmaCondition(
                String field,
                String value) {

            this.field = field == null ? "" : field.trim();
            this.value = value == null ? "" : value.trim();
        }

        public String getField() {
            return field;
        }

        public String getValue() {
            return value;
        }
    }
}