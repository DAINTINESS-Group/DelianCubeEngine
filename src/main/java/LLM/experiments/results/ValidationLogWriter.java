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
import LLM.experiments.ValidationResult;
import LLM.client.LLMCallResult;
import LLM.experiments.runner.PromptSizeStats;
import LLM.experiments.similarity.QuerySimilarityResult;

public class ValidationLogWriter implements Closeable {

    private final BufferedWriter writer;

    public ValidationLogWriter(Path outputPath) throws IOException {
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
    }

    public void writeResult(ExperimentResult result) throws IOException {
        if (result == null) {
            throw new IllegalArgumentException("ExperimentResult cannot be null.");
        }

        writeSeparator();

        writer.write("Run ID: " + safe(result.getRunId()));
        writer.newLine();

        writer.write("Timestamp: " + safe(result.getTimestamp()));
        writer.newLine();

        writer.write("LLM: " + safe(result.getLlmName()));
        writer.newLine();

        writer.write("Prompt combination: " +
                safe(result.getPromptCombinationName()));
        writer.newLine();

        writer.newLine();

        writer.write("Test ID: " + safe(result.getTestId()));
        writer.newLine();

        writer.write("Pair ID: " + safe(result.getPairId()));
        writer.newLine();

        writer.write("Wording Type: " + safe(result.getWordingType()));
        writer.newLine();

        writer.write("Difficulty: " + safe(result.getDifficulty()));
        writer.newLine();

        writer.write("Category: " + safe(result.getCategory()));
        writer.newLine();

        writer.newLine();

        writer.write("Question:");
        writer.newLine();
        writeMultilineText(result.getQuestion());
        writer.newLine();

        writer.write("Expected query:");
        writer.newLine();
        writeExpectedQuery(result.getExpectedQuery());
        writer.newLine();

        writer.write("Actual LLM answer:");
        writer.newLine();
        writeMultilineText(result.getActualAnswer());
        writer.newLine();

        writer.write("STRICT VALIDATION:");
        writer.newLine();
        writeValidationSummary(result.getStrictValidationResult());
        writer.newLine();

        writer.write("Strict validation errors:");
        writer.newLine();
        writeValidationErrors(result.getStrictValidationResult());
        writer.newLine();

        writer.write("STRUCTURAL VALIDATION:");
        writer.newLine();
        writeValidationSummary(result.getStructuralValidationResult());
        writer.newLine();

        writer.write("Structural validation errors:");
        writer.newLine();
        writeValidationErrors(result.getStructuralValidationResult());
        writer.newLine();

        writer.write("QUERY SIMILARITY:");
        writer.newLine();
        writeQuerySimilarity(result.getQuerySimilarityResult());
        writer.newLine();

        writer.write("Prompt size:");
        writer.newLine();
        writePromptSizeStats(result.getPromptSizeStats());
        writer.newLine();

        writer.write("Timing and LLM stats:");
        writer.newLine();
        writeTimingAndStats(result.getLlmCallResult());

        writeSeparator();
        writer.newLine();
        writer.flush();
    }

    private void writeExpectedQuery(ExpectedCubeQuery expectedQuery)
            throws IOException {

        if (expectedQuery == null) {
            writer.write("No expected query available.");
            writer.newLine();
            return;
        }

        writer.write("cubeName: " + safe(expectedQuery.getCubeName()));
        writer.newLine();

        writer.write("aggregateFunction: " +
                safe(expectedQuery.getAggregateFunction()));
        writer.newLine();

        writer.write("measure: " + safe(expectedQuery.getMeasure()));
        writer.newLine();

        writer.write("gamma: " + joinList(expectedQuery.getGammaFields()));
        writer.newLine();

        writer.write("sigma: " + joinList(expectedQuery.getSigmaConditions()));
        writer.newLine();
    }

    private void writeValidationSummary(
            ValidationResult validationResult
    ) throws IOException {

        if (validationResult == null) {
            writer.write("No validation result available.");
            writer.newLine();
            return;
        }

        writer.write("valid: " + validationResult.isValid());
        writer.newLine();

        writer.write("accuracy: " +
                formatDouble(validationResult.getStrictAccuracy()));
        writer.newLine();

        writer.write("weighted_score: " +
                formatDouble(validationResult.getWeightedScore()));
        writer.newLine();

        writer.write("format_valid: " +
                validationResult.isFormatValid());
        writer.newLine();

        writer.write("cube_correct: " +
                validationResult.isCubeCorrect());
        writer.newLine();

        writer.write("aggregate_correct: " +
                validationResult.isAggregateFunctionCorrect());
        writer.newLine();

        writer.write("measure_correct: " +
                validationResult.isMeasureCorrect());
        writer.newLine();

        writer.write("gamma_correct: " +
                validationResult.isGammaCorrect());
        writer.newLine();

        writer.write("sigma_correct: " +
                validationResult.isSigmaCorrect());
        writer.newLine();

        writer.write("unknown_fields_count: " +
                validationResult.getUnknownFieldsCount());
        writer.newLine();

        writer.write("missing_gamma_count: " +
                validationResult.getMissingGammaCount());
        writer.newLine();

        writer.write("extra_gamma_count: " +
                validationResult.getExtraGammaCount());
        writer.newLine();

        writer.write("missing_sigma_count: " +
                validationResult.getMissingSigmaCount());
        writer.newLine();

        writer.write("extra_sigma_count: " +
                validationResult.getExtraSigmaCount());
        writer.newLine();

        writer.write("wrong_sigma_value_count: " +
                validationResult.getWrongSigmaValueCount());
        writer.newLine();
    }

    private void writeValidationErrors(
            ValidationResult validationResult
    ) throws IOException {

        if (validationResult == null) {
            writer.write("- No validation result available.");
            writer.newLine();
            return;
        }

        List<String> errors = validationResult.getErrors();

        if (errors == null || errors.isEmpty()) {
            writer.write("- No validation errors.");
            writer.newLine();
            return;
        }

        for (String error : errors) {
            writer.write("- " + safe(error));
            writer.newLine();
        }
    }

    private void writeQuerySimilarity(
            QuerySimilarityResult similarityResult
    ) throws IOException {

        if (similarityResult == null || !similarityResult.isAvailable()) {
            writer.write("available: false");
            writer.newLine();

            writer.write("No query similarity result available.");
            writer.newLine();
            return;
        }

        writer.write("available: true");
        writer.newLine();

        writer.write("overall_similarity: " +
                formatDouble(similarityResult.getOverallSimilarity()));
        writer.newLine();

        writer.write("gamma_similarity: " +
                formatDouble(similarityResult.getGammaSimilarity()));
        writer.newLine();

        writer.write("sigma_similarity: " +
                formatDouble(similarityResult.getSigmaSimilarity()));
        writer.newLine();

        writer.write("measure_similarity: " +
                formatDouble(similarityResult.getMeasureSimilarity()));
        writer.newLine();

        writer.write("aggregate_similarity: " +
                formatDouble(similarityResult.getAggregateSimilarity()));
        writer.newLine();

        writer.write("cube_similarity: " +
                formatDouble(similarityResult.getCubeSimilarity()));
        writer.newLine();
    }

    private void writePromptSizeStats(
            PromptSizeStats promptSizeStats
    ) throws IOException {

        if (promptSizeStats == null) {
            writer.write("No prompt size stats available.");
            writer.newLine();
            return;
        }

        writer.write("prompt_chars: " +
                promptSizeStats.getCharacterCount());
        writer.newLine();

        writer.write("prompt_utf8_bytes: " +
                promptSizeStats.getUtf8ByteCount());
        writer.newLine();

        writer.write("prompt_estimated_tokens_chars: " +
                promptSizeStats.getEstimatedTokensByCharacters());
        writer.newLine();

        writer.write("prompt_estimated_tokens_bytes: " +
                promptSizeStats.getEstimatedTokensByBytes());
        writer.newLine();
    }

    private void writeTimingAndStats(LLMCallResult callResult)
            throws IOException {

        if (callResult == null) {
            writer.write("No LLM call result available.");
            writer.newLine();
            return;
        }

        writer.write("llm_call_success: " + callResult.isSuccess());
        writer.newLine();

        writer.write("response_time_ms: " +
                callResult.getResponseTimeMs());
        writer.newLine();

        writer.write("prompt_eval_count: " +
                callResult.getPromptEvalCount());
        writer.newLine();

        writer.write("eval_count: " +
                callResult.getEvalCount());
        writer.newLine();

        writer.write("total_tokens: " +
                callResult.getTotalTokenCount());
        writer.newLine();

        writer.write("total_duration_ms: " +
                callResult.getTotalDurationMs());
        writer.newLine();

        writer.write("load_duration_ms: " +
                callResult.getLoadDurationMs());
        writer.newLine();

        long adjustedDurationMs = calculateAdjustedDurationMs(callResult);

        writer.write("adjusted_duration_ms: " +
                (adjustedDurationMs < 0L
                        ? ""
                        : String.valueOf(adjustedDurationMs)));
        writer.newLine();

        writer.write("adjusted_duration_s: " +
                (adjustedDurationMs < 0L
                        ? ""
                        : formatSeconds(adjustedDurationMs)));
        writer.newLine();

        writer.write("prompt_eval_duration_ms: " +
                callResult.getPromptEvalDurationMs());
        writer.newLine();

        writer.write("eval_duration_ms: " +
                callResult.getEvalDurationMs());
        writer.newLine();

        writer.write("prompt_tokens_per_second: " +
                formatDouble(callResult.getPromptTokensPerSecond()));
        writer.newLine();

        writer.write("output_tokens_per_second: " +
                formatDouble(callResult.getOutputTokensPerSecond()));
        writer.newLine();

        if (callResult.isFailed()) {
            writer.write("error_type: " +
                    safe(callResult.getErrorType()));
            writer.newLine();

            writer.write("error_message: " +
                    safe(callResult.getErrorMessage()));
            writer.newLine();
        }
    }

    private long calculateAdjustedDurationMs(LLMCallResult callResult) {
        if (callResult == null) {
            return -1L;
        }

        long totalDurationMs = callResult.getTotalDurationMs();
        long loadDurationMs = callResult.getLoadDurationMs();

        if (totalDurationMs < 0L || loadDurationMs < 0L) {
            return -1L;
        }

        long adjustedDurationMs = totalDurationMs - loadDurationMs;
        return adjustedDurationMs < 0L ? -1L : adjustedDurationMs;
    }

    private String formatSeconds(long durationMs) {
        return String.format(
                Locale.US,
                "%.3f",
                durationMs / 1000.0
        );
    }

    private void writeMultilineText(String text) throws IOException {
        if (text == null || text.trim().isEmpty()) {
            writer.write("");
            writer.newLine();
            return;
        }

        String normalizedText = text
                .replace("\r\n", "\n")
                .replace("\r", "\n");

        String[] lines = normalizedText.split("\n");

        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
    }

    private String joinList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(safe(values.get(i)));
        }

        builder.append("]");
        return builder.toString();
    }

    private void writeSeparator() throws IOException {
        writer.write(
                "============================================================"
        );
        writer.newLine();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String formatDouble(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    @Override
    public void close() throws IOException {
        writer.flush();
        writer.close();
    }
}