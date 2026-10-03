package LLM.experiments.results;

import LLM.client.LLMCallResult;
import LLM.client.LLMModelConfig;
import LLM.experiments.ExpectedCubeQuery;
import LLM.experiments.LLMExperimentCase;
import LLM.experiments.ValidationResult;
import LLM.experiments.runner.PromptSizeStats;
import LLM.experiments.similarity.QuerySimilarityResult;

public class ExperimentResult {

    private final String runId;
    private final String timestamp;
    private final String llmName;
    private final String promptCombinationName;

    private final String testId;
    private final String pairId;
    private final String wordingType;
    private final String difficulty;
    private final String category;
    private final String question;

    private final ExpectedCubeQuery expectedQuery;
    private final String actualAnswer;

    private final ValidationResult strictValidationResult;
    private final ValidationResult structuralValidationResult;
    private final QuerySimilarityResult querySimilarityResult;

    private final PromptSizeStats promptSizeStats;
    private final LLMCallResult llmCallResult;

    public ExperimentResult(
            String runId,
            String timestamp,
            String llmName,
            String promptCombinationName,
            String testId,
            String pairId,
            String wordingType,
            String difficulty,
            String category,
            String question,
            ExpectedCubeQuery expectedQuery,
            String actualAnswer,
            ValidationResult strictValidationResult,
            ValidationResult structuralValidationResult,
            QuerySimilarityResult querySimilarityResult,
            PromptSizeStats promptSizeStats,
            LLMCallResult llmCallResult
    ) {
        this.runId = safeString(runId);
        this.timestamp = safeString(timestamp);
        this.llmName = safeString(llmName);
        this.promptCombinationName = safeString(promptCombinationName);

        this.testId = safeString(testId);
        this.pairId = safeString(pairId);
        this.wordingType = safeString(wordingType);
        this.difficulty = safeString(difficulty);
        this.category = safeString(category);
        this.question = safeString(question);

        this.expectedQuery = expectedQuery;
        this.actualAnswer = safeString(actualAnswer);

        this.strictValidationResult = strictValidationResult;
        this.structuralValidationResult = structuralValidationResult;
        this.querySimilarityResult = querySimilarityResult == null
                ? QuerySimilarityResult.unavailable()
                : querySimilarityResult;

        this.promptSizeStats = promptSizeStats;
        this.llmCallResult = llmCallResult;
    }

    public ExperimentResult(
            String runId,
            String timestamp,
            String llmName,
            String promptCombinationName,
            String testId,
            String difficulty,
            String category,
            String question,
            ExpectedCubeQuery expectedQuery,
            String actualAnswer,
            ValidationResult strictValidationResult,
            ValidationResult structuralValidationResult,
            QuerySimilarityResult querySimilarityResult,
            PromptSizeStats promptSizeStats,
            LLMCallResult llmCallResult
    ) {
        this(
                runId,
                timestamp,
                llmName,
                promptCombinationName,
                testId,
                "",
                "",
                difficulty,
                category,
                question,
                expectedQuery,
                actualAnswer,
                strictValidationResult,
                structuralValidationResult,
                querySimilarityResult,
                promptSizeStats,
                llmCallResult
        );
    }

    public ExperimentResult(
            String runId,
            String timestamp,
            String llmName,
            String promptCombinationName,
            String testId,
            String difficulty,
            String category,
            String question,
            ExpectedCubeQuery expectedQuery,
            String actualAnswer,
            ValidationResult strictValidationResult,
            ValidationResult structuralValidationResult,
            PromptSizeStats promptSizeStats,
            LLMCallResult llmCallResult
    ) {
        this(
                runId,
                timestamp,
                llmName,
                promptCombinationName,
                testId,
                difficulty,
                category,
                question,
                expectedQuery,
                actualAnswer,
                strictValidationResult,
                structuralValidationResult,
                QuerySimilarityResult.unavailable(),
                promptSizeStats,
                llmCallResult
        );
    }

    public ExperimentResult(
            String runId,
            String timestamp,
            String llmName,
            String promptCombinationName,
            String testId,
            String pairId,
            String wordingType,
            String difficulty,
            String category,
            String question,
            ExpectedCubeQuery expectedQuery,
            String actualAnswer,
            ValidationResult strictValidationResult,
            ValidationResult structuralValidationResult,
            PromptSizeStats promptSizeStats,
            LLMCallResult llmCallResult
    ) {
        this(
                runId,
                timestamp,
                llmName,
                promptCombinationName,
                testId,
                pairId,
                wordingType,
                difficulty,
                category,
                question,
                expectedQuery,
                actualAnswer,
                strictValidationResult,
                structuralValidationResult,
                QuerySimilarityResult.unavailable(),
                promptSizeStats,
                llmCallResult
        );
    }

    public static ExperimentResult from(
            String runId,
            String timestamp,
            LLMModelConfig modelConfig,
            String promptCombinationName,
            LLMExperimentCase experimentCase,
            String actualAnswer,
            ValidationResult strictValidationResult,
            ValidationResult structuralValidationResult,
            QuerySimilarityResult querySimilarityResult,
            PromptSizeStats promptSizeStats,
            LLMCallResult llmCallResult
    ) {
        return new ExperimentResult(
                runId,
                timestamp,
                modelConfig == null ? "" : modelConfig.getModelName(),
                promptCombinationName,
                experimentCase == null ? "" : experimentCase.getTestId(),
                experimentCase == null ? "" : experimentCase.getPairId(),
                experimentCase == null ? "" : experimentCase.getWordingType(),
                experimentCase == null ? "" : experimentCase.getDifficulty(),
                experimentCase == null ? "" : experimentCase.getCategory(),
                experimentCase == null ? "" : experimentCase.getQuestion(),
                experimentCase == null ? null : experimentCase.getExpectedQuery(),
                actualAnswer,
                strictValidationResult,
                structuralValidationResult,
                querySimilarityResult,
                promptSizeStats,
                llmCallResult
        );
    }

    public static ExperimentResult from(
            String runId,
            String timestamp,
            LLMModelConfig modelConfig,
            String promptCombinationName,
            LLMExperimentCase experimentCase,
            String actualAnswer,
            ValidationResult strictValidationResult,
            ValidationResult structuralValidationResult,
            PromptSizeStats promptSizeStats,
            LLMCallResult llmCallResult
    ) {
        return from(
                runId,
                timestamp,
                modelConfig,
                promptCombinationName,
                experimentCase,
                actualAnswer,
                strictValidationResult,
                structuralValidationResult,
                QuerySimilarityResult.unavailable(),
                promptSizeStats,
                llmCallResult
        );
    }

    public String getRunId() {
        return runId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLlmName() {
        return llmName;
    }

    public String getPromptCombinationName() {
        return promptCombinationName;
    }

    public String getPromptTechniqueName() {
        return promptCombinationName;
    }

    public String getTestId() {
        return testId;
    }

    public String getPairId() {
        return pairId;
    }

    public String getWordingType() {
        return wordingType;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public ExpectedCubeQuery getExpectedQuery() {
        return expectedQuery;
    }

    public String getActualAnswer() {
        return actualAnswer;
    }

    public PromptSizeStats getPromptSizeStats() {
        return promptSizeStats;
    }

    public LLMCallResult getLlmCallResult() {
        return llmCallResult;
    }

    public QuerySimilarityResult getQuerySimilarityResult() {
        return querySimilarityResult;
    }

    public ValidationResult getStrictValidationResult() {
        return strictValidationResult;
    }

    public ValidationResult getStructuralValidationResult() {
        return structuralValidationResult;
    }

    public boolean isStrictValid() {
        return strictValidationResult != null
                && strictValidationResult.isValid();
    }

    public double getStrictAccuracy() {
        return strictValidationResult == null
                ? 0.0
                : strictValidationResult.getStrictAccuracy();
    }

    public double getStrictWeightedScore() {
        return strictValidationResult == null
                ? 0.0
                : strictValidationResult.getWeightedScore();
    }

    public boolean isQuerySimilarityAvailable() {
        return querySimilarityResult != null
                && querySimilarityResult.isAvailable();
    }

    public double getOverallSimilarity() {
        return isQuerySimilarityAvailable()
                ? querySimilarityResult.getOverallSimilarity()
                : 0.0;
    }

    public double getGammaSimilarity() {
        return isQuerySimilarityAvailable()
                ? querySimilarityResult.getGammaSimilarity()
                : 0.0;
    }

    public double getSigmaSimilarity() {
        return isQuerySimilarityAvailable()
                ? querySimilarityResult.getSigmaSimilarity()
                : 0.0;
    }

    public double getMeasureSimilarity() {
        return isQuerySimilarityAvailable()
                ? querySimilarityResult.getMeasureSimilarity()
                : 0.0;
    }

    public double getAggregateSimilarity() {
        return isQuerySimilarityAvailable()
                ? querySimilarityResult.getAggregateSimilarity()
                : 0.0;
    }

    public double getCubeSimilarity() {
        return isQuerySimilarityAvailable()
                ? querySimilarityResult.getCubeSimilarity()
                : 0.0;
    }

    public boolean isStructuralValid() {
        return structuralValidationResult != null
                && structuralValidationResult.isValid();
    }

    public double getStructuralAccuracy() {
        return structuralValidationResult == null
                ? 0.0
                : structuralValidationResult.getStrictAccuracy();
    }

    public double getStructuralWeightedScore() {
        return structuralValidationResult == null
                ? 0.0
                : structuralValidationResult.getWeightedScore();
    }

    public boolean isValid() {
        return isStrictValid();
    }

    public double getWeightedScore() {
        return getStrictWeightedScore();
    }

    public ValidationResult getValidationResult() {
        return strictValidationResult;
    }

    public boolean isFormatValid() {
        return strictValidationResult != null
                && strictValidationResult.isFormatValid();
    }

    public boolean isCubeCorrect() {
        return strictValidationResult != null
                && strictValidationResult.isCubeCorrect();
    }

    public boolean isAggregateFunctionCorrect() {
        return strictValidationResult != null
                && strictValidationResult.isAggregateFunctionCorrect();
    }

    public boolean isMeasureCorrect() {
        return strictValidationResult != null
                && strictValidationResult.isMeasureCorrect();
    }

    public boolean isGammaCorrect() {
        return strictValidationResult != null
                && strictValidationResult.isGammaCorrect();
    }

    public boolean isSigmaCorrect() {
        return strictValidationResult != null
                && strictValidationResult.isSigmaCorrect();
    }

    public boolean isStructuralSigmaCorrect() {
        return structuralValidationResult != null
                && structuralValidationResult.isSigmaCorrect();
    }

    public int getUnknownFieldsCount() {
        return strictValidationResult == null
                ? 0
                : strictValidationResult.getUnknownFieldsCount();
    }

    public int getMissingGammaCount() {
        return strictValidationResult == null
                ? 0
                : strictValidationResult.getMissingGammaCount();
    }

    public int getExtraGammaCount() {
        return strictValidationResult == null
                ? 0
                : strictValidationResult.getExtraGammaCount();
    }

    public int getMissingSigmaCount() {
        return strictValidationResult == null
                ? 0
                : strictValidationResult.getMissingSigmaCount();
    }

    public int getExtraSigmaCount() {
        return strictValidationResult == null
                ? 0
                : strictValidationResult.getExtraSigmaCount();
    }

    public int getWrongSigmaValueCount() {
        return strictValidationResult == null
                ? 0
                : strictValidationResult.getWrongSigmaValueCount();
    }

    public boolean isLlmCallSuccessful() {
        return llmCallResult != null
                && llmCallResult.isSuccess();
    }

    public long getResponseTimeMs() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getResponseTimeMs();
    }

    public String getErrorType() {
        return llmCallResult == null
                ? ""
                : llmCallResult.getErrorType();
    }

    public String getErrorMessage() {
        return llmCallResult == null
                ? ""
                : llmCallResult.getErrorMessage();
    }

    public int getPromptCharacterCount() {
        return promptSizeStats == null
                ? 0
                : promptSizeStats.getCharacterCount();
    }

    public int getPromptUtf8ByteCount() {
        return promptSizeStats == null
                ? 0
                : promptSizeStats.getUtf8ByteCount();
    }

    public long getPromptEstimatedTokensByCharacters() {
        return promptSizeStats == null
                ? 0L
                : promptSizeStats.getEstimatedTokensByCharacters();
    }

    public long getPromptEstimatedTokensByBytes() {
        return promptSizeStats == null
                ? 0L
                : promptSizeStats.getEstimatedTokensByBytes();
    }

    public long getPromptEstimatedTokens() {
        return getPromptEstimatedTokensByCharacters();
    }

    public long getPromptEvalCount() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getPromptEvalCount();
    }

    public long getPromptEvalCachedCount() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getPromptEvalCachedCount();
    }

    public long getPromptEvalUncachedCount() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getPromptEvalUncachedCount();
    }

    public double getPromptCacheHitRatio() {
        return llmCallResult == null
                ? -1.0
                : llmCallResult.getPromptCacheHitRatio();
    }

    public long getEvalCount() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getEvalCount();
    }

    public long getTotalTokenCount() {
        return llmCallResult == null
                ? 0L
                : llmCallResult.getTotalTokenCount();
    }

    public long getTotalDurationMs() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getTotalDurationMs();
    }

    public long getLoadDurationMs() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getLoadDurationMs();
    }

    public long getPromptEvalDurationMs() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getPromptEvalDurationMs();
    }

    public long getEvalDurationMs() {
        return llmCallResult == null
                ? -1L
                : llmCallResult.getEvalDurationMs();
    }

    public double getPromptTokensPerSecond() {
        return llmCallResult == null
                ? 0.0
                : llmCallResult.getPromptTokensPerSecond();
    }

    public double getOutputTokensPerSecond() {
        return llmCallResult == null
                ? 0.0
                : llmCallResult.getOutputTokensPerSecond();
    }

    private static String safeString(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public String toString() {
        return "ExperimentResult{" +
                "runId='" + runId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", llmName='" + llmName + '\'' +
                ", promptCombinationName='" + promptCombinationName + '\'' +
                ", testId='" + testId + '\'' +
                ", pairId='" + pairId + '\'' +
                ", wordingType='" + wordingType + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", strictValid=" + isStrictValid() +
                ", overallSimilarity=" + getOverallSimilarity() +
                ", structuralValid=" + isStructuralValid() +
                ", responseTimeMs=" + getResponseTimeMs() +
                ", promptEvalCount=" + getPromptEvalCount() +
                ", promptEvalCachedCount=" + getPromptEvalCachedCount() +
                ", promptEvalUncachedCount=" + getPromptEvalUncachedCount() +
                ", promptCacheHitRatio=" + getPromptCacheHitRatio() +
                ", errorType='" + getErrorType() + '\'' +
                '}';
    }
}