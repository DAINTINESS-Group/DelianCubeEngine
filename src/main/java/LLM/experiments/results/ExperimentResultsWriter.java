package LLM.experiments.results;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import LLM.experiments.ExpectedCubeQuery;

public class ExperimentResultsWriter implements Closeable {

    private final BufferedWriter writer;
    private boolean headerWritten;

    public ExperimentResultsWriter(Path outputPath) throws IOException {
        if (outputPath == null) {
            throw new IllegalArgumentException("outputPath cannot be null.");
        }

        Path parent = outputPath.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        writer = Files.newBufferedWriter(
                outputPath,
                StandardCharsets.UTF_8
        );
        headerWritten = false;
    }

    public void writeHeaderIfNeeded() throws IOException {
        if (headerWritten) {
            return;
        }

        writer.write(joinTsv(new String[]{
                "run_id",
                "llm",
                "prompt_combination",
                "test_id",
                "pair_id",
                "wording_type",
                "difficulty",
                "category",

                "strict_accuracy",
                "overall_similarity",
                "structural_accuracy",
                "response_time_ms",
                "similarity_available",

                "gamma_similarity",
                "sigma_similarity",
                "measure_similarity",
                "aggregate_similarity",
                "cube_similarity",

                "strict_valid",
                "structural_valid",
                "strict_weighted_score",
                "structural_weighted_score",

                "format_valid",
                "cube_correct",
                "aggregate_correct",
                "measure_correct",
                "gamma_correct",
                "sigma_correct",
                "structural_sigma_correct",

                "unknown_fields_count",
                "missing_gamma_count",
                "extra_gamma_count",
                "missing_sigma_count",
                "extra_sigma_count",
                "wrong_sigma_value_count",

                "question",
                "expected_cube",
                "expected_aggregate",
                "expected_measure",
                "expected_gamma",
                "expected_sigma",
                "actual_answer",

                "prompt_chars",
                "prompt_utf8_bytes",
                "prompt_estimated_tokens_chars",
                "prompt_estimated_tokens_bytes",

                "prompt_eval_count",
                "prompt_eval_cached_count",
                "prompt_eval_uncached_count",
                "prompt_cache_hit_ratio",
                "eval_count",
                "total_tokens",

                "total_duration_ms",
                "load_duration_ms",
                "adjusted_duration_ms",
                "adjusted_duration_s",
                "prompt_eval_duration_ms",
                "eval_duration_ms",

                "prompt_tokens_per_second",
                "output_tokens_per_second",

                "llm_call_success",
                "error_type",
                "error_message",
                "timestamp"
        }));

        writer.newLine();
        writer.flush();
        headerWritten = true;
    }

    public void writeResult(ExperimentResult result) throws IOException {
        if (result == null) {
            throw new IllegalArgumentException("ExperimentResult cannot be null.");
        }

        writeHeaderIfNeeded();

        ExpectedCubeQuery expectedQuery = result.getExpectedQuery();

        writer.write(joinTsv(new String[]{
                result.getRunId(),
                result.getLlmName(),
                result.getPromptCombinationName(),
                result.getTestId(),
                result.getPairId(),
                result.getWordingType(),
                result.getDifficulty(),
                result.getCategory(),

                formatDouble(result.getStrictAccuracy()),
                formatSimilarity(result, result.getOverallSimilarity()),
                formatDouble(result.getStructuralAccuracy()),
                String.valueOf(result.getResponseTimeMs()),
                booleanToString(result.isQuerySimilarityAvailable()),

                formatSimilarity(result, result.getGammaSimilarity()),
                formatSimilarity(result, result.getSigmaSimilarity()),
                formatSimilarity(result, result.getMeasureSimilarity()),
                formatSimilarity(result, result.getAggregateSimilarity()),
                formatSimilarity(result, result.getCubeSimilarity()),

                booleanToString(result.isStrictValid()),
                booleanToString(result.isStructuralValid()),
                formatDouble(result.getStrictWeightedScore()),
                formatDouble(result.getStructuralWeightedScore()),

                booleanToString(result.isFormatValid()),
                booleanToString(result.isCubeCorrect()),
                booleanToString(result.isAggregateFunctionCorrect()),
                booleanToString(result.isMeasureCorrect()),
                booleanToString(result.isGammaCorrect()),
                booleanToString(result.isSigmaCorrect()),
                booleanToString(result.isStructuralSigmaCorrect()),

                String.valueOf(result.getUnknownFieldsCount()),
                String.valueOf(result.getMissingGammaCount()),
                String.valueOf(result.getExtraGammaCount()),
                String.valueOf(result.getMissingSigmaCount()),
                String.valueOf(result.getExtraSigmaCount()),
                String.valueOf(result.getWrongSigmaValueCount()),

                result.getQuestion(),
                getExpectedCube(expectedQuery),
                getExpectedAggregate(expectedQuery),
                getExpectedMeasure(expectedQuery),
                getExpectedGamma(expectedQuery),
                getExpectedSigma(expectedQuery),
                result.getActualAnswer(),

                String.valueOf(result.getPromptCharacterCount()),
                String.valueOf(result.getPromptUtf8ByteCount()),
                String.valueOf(result.getPromptEstimatedTokensByCharacters()),
                String.valueOf(result.getPromptEstimatedTokensByBytes()),

                String.valueOf(result.getPromptEvalCount()),
                formatOptionalLong(result.getPromptEvalCachedCount()),
                formatOptionalLong(result.getPromptEvalUncachedCount()),
                formatOptionalRatio(result.getPromptCacheHitRatio()),
                String.valueOf(result.getEvalCount()),
                String.valueOf(result.getTotalTokenCount()),

                String.valueOf(result.getTotalDurationMs()),
                String.valueOf(result.getLoadDurationMs()),
                formatAdjustedDurationMs(result),
                formatAdjustedDurationSeconds(result),
                String.valueOf(result.getPromptEvalDurationMs()),
                String.valueOf(result.getEvalDurationMs()),

                formatDouble(result.getPromptTokensPerSecond()),
                formatDouble(result.getOutputTokensPerSecond()),

                booleanToString(result.isLlmCallSuccessful()),
                result.getErrorType(),
                result.getErrorMessage(),
                result.getTimestamp()
        }));

        writer.newLine();
        writer.flush();
    }

    private static String getExpectedCube(ExpectedCubeQuery expectedQuery) {
        if (expectedQuery == null) {
            return "";
        }
        return safeString(expectedQuery.getCubeName());
    }

    private static String getExpectedAggregate(ExpectedCubeQuery expectedQuery) {
        if (expectedQuery == null) {
            return "";
        }
        return safeString(expectedQuery.getAggregateFunction());
    }

    private static String getExpectedMeasure(ExpectedCubeQuery expectedQuery) {
        if (expectedQuery == null) {
            return "";
        }
        return safeString(expectedQuery.getMeasure());
    }

    private static String getExpectedGamma(ExpectedCubeQuery expectedQuery) {
        if (expectedQuery == null) {
            return "";
        }
        return joinList(expectedQuery.getGammaFields());
    }

    private static String getExpectedSigma(ExpectedCubeQuery expectedQuery) {
        if (expectedQuery == null) {
            return "";
        }
        return joinList(expectedQuery.getSigmaConditions());
    }

    private static String joinList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(safeString(values.get(i)));
        }
        builder.append("]");

        return builder.toString();
    }

    private static String formatOptionalLong(long value) {
        return value < 0L ? "" : String.valueOf(value);
    }

    private static String formatOptionalRatio(double value) {
        if (value < 0.0 || Double.isNaN(value) || Double.isInfinite(value)) {
            return "";
        }

        return String.format(Locale.US, "%.4f", value);
    }

    private static long calculateAdjustedDurationMs(ExperimentResult result) {
        if (result == null) {
            return -1L;
        }

        long totalDurationMs = result.getTotalDurationMs();
        long loadDurationMs = result.getLoadDurationMs();

        if (totalDurationMs < 0L || loadDurationMs < 0L) {
            return -1L;
        }

        long adjustedDurationMs = totalDurationMs - loadDurationMs;
        return adjustedDurationMs < 0L ? -1L : adjustedDurationMs;
    }

    private static String formatAdjustedDurationMs(ExperimentResult result) {
        long adjustedDurationMs = calculateAdjustedDurationMs(result);
        return adjustedDurationMs < 0L
                ? ""
                : String.valueOf(adjustedDurationMs);
    }

    private static String formatAdjustedDurationSeconds(ExperimentResult result) {
        long adjustedDurationMs = calculateAdjustedDurationMs(result);

        if (adjustedDurationMs < 0L) {
            return "";
        }

        return String.format(
                Locale.US,
                "%.3f",
                adjustedDurationMs / 1000.0
        );
    }

    private static String formatSimilarity(
            ExperimentResult result,
            double value
    ) {
        if (result == null || !result.isQuerySimilarityAvailable()) {
            return "";
        }
        return formatDouble(value);
    }

    private static String joinTsv(String[] values) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append('\t');
            }
            builder.append(escapeTsv(values[i]));
        }

        return builder.toString();
    }

    private static String escapeTsv(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\t", " ")
                .replace("\r\n", "\\n")
                .replace("\r", "\\n")
                .replace("\n", "\\n")
                .trim();
    }

    private static String booleanToString(boolean value) {
        return value ? "true" : "false";
    }

    private static String formatDouble(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    private static String safeString(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public void close() throws IOException {
        writer.flush();
        writer.close();
    }
}