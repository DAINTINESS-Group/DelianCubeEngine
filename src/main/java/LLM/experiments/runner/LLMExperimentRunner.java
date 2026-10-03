package LLM.experiments.runner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import LLM.client.LLMCallResult;
import LLM.client.LLMModelConfig;
import LLM.client.OllamaLLMClient;
import LLM.experiments.CubeQueryValidator;
import LLM.experiments.ExpectedCubeQuery;
import LLM.experiments.LLMExperimentCase;
import LLM.experiments.ValidationResult;

import LLM.normalization.CubeQueryNormalizer;
import LLM.normalization.CubeQueryNormalizer.NormalizationResult;

import LLM.experiments.similarity.HierarchyMetadataIndex;
import LLM.experiments.similarity.QuerySimilarityEvaluator;
import LLM.experiments.similarity.QuerySimilarityResult;

import LLM.experiments.results.ExperimentResult;
import LLM.experiments.results.ExperimentResultsWriter;
import LLM.experiments.results.ValidationLogWriter;

import LLM.prompts.PromptBuilder;
import LLM.prompts.PromptTechnique;
import LLM.prompts.PromptTechniqueCombinationGenerator;

import LLM.schema.CubeSchema;
import LLM.validation.CubeFieldValidator;

public class LLMExperimentRunner {

    private static final long EXPERIMENT_ORDER_SEED = 42L;

    private final OllamaLLMClient llmClient;
    private final PromptBuilder promptBuilder;

    private int runCounter;

    public LLMExperimentRunner(
            OllamaLLMClient llmClient
    ) {
        this(
                llmClient,
                new PromptBuilder()
        );
    }

    public LLMExperimentRunner(
            OllamaLLMClient llmClient,
            PromptBuilder promptBuilder
    ) {
        if (llmClient == null) {
            throw new IllegalArgumentException(
                    "llmClient cannot be null."
            );
        }

        if (promptBuilder == null) {
            throw new IllegalArgumentException(
                    "promptBuilder cannot be null."
            );
        }

        this.llmClient = llmClient;
        this.promptBuilder = promptBuilder;
        this.runCounter = 0;
    }

    public void runExperiments(
            CubeSchema cubeSchema,
            List<LLMModelConfig> modelConfigs,
            List<List<PromptTechnique>> promptCombinations,
            List<LLMExperimentCase> experimentCases,
            ExperimentResultsWriter resultsWriter,
            ValidationLogWriter validationLogWriter
    ) throws Exception {

        validateInputs(
                cubeSchema,
                modelConfigs,
                promptCombinations,
                experimentCases,
                resultsWriter,
                validationLogWriter
        );

        CubeFieldValidator cubeFieldValidator =
                new CubeFieldValidator(cubeSchema);

        List<LLMExperimentCase> orderedExperimentCases =
                ExperimentCaseOrderer.createPairedOrder(
                        experimentCases,
                        EXPERIMENT_ORDER_SEED
                );

        HierarchyMetadataIndex hierarchyMetadataIndex =
                new HierarchyMetadataIndex(cubeSchema);

        QuerySimilarityEvaluator querySimilarityEvaluator =
                new QuerySimilarityEvaluator(
                        hierarchyMetadataIndex
                );

        int totalRuns =
                modelConfigs.size()
                        * promptCombinations.size()
                        * orderedExperimentCases.size();

        printExperimentOverview(
                modelConfigs,
                promptCombinations,
                orderedExperimentCases,
                totalRuns
        );

        printExperimentOrder(
                orderedExperimentCases
        );

        for (LLMModelConfig modelConfig : modelConfigs) {

            System.out.println();
            System.out.println(
                    "========================================"
            );
            System.out.println(
                    "MODEL: " + modelConfig.getModelName()
            );
            System.out.println(
                    "========================================"
            );
            System.out.println();

            for (List<PromptTechnique> promptCombination
                    : promptCombinations) {

                String combinationName =
                        PromptTechniqueCombinationGenerator
                                .getCombinationName(
                                        promptCombination
                                );

                System.out.println(
                        "Running combination: "
                                + combinationName
                );

                System.out.println(
                        "No separate warm-up request."
                );

                System.out.println(
                        "The first real query is the measured "
                                + "cache-seeding request."
                );

                System.out.println();

                int requestIndexInBlock = 0;

                for (LLMExperimentCase experimentCase
                        : orderedExperimentCases) {

                    requestIndexInBlock++;

                    ExperimentResult result =
                            runSingleExperiment(
                                    cubeSchema,
                                    cubeFieldValidator,
                                    modelConfig,
                                    promptCombination,
                                    experimentCase,
                                    querySimilarityEvaluator
                            );

                    resultsWriter.writeResult(
                            result
                    );

                    validationLogWriter.writeResult(
                            result
                    );

                    printRunSummary(
                            result,
                            totalRuns,
                            requestIndexInBlock
                    );
                }

                System.out.println();
            }
        }

        System.out.println();

        System.out.println(
                "LLM experiments completed."
        );

        System.out.println(
                "Completed measured runs: "
                        + runCounter
                        + "/"
                        + totalRuns
        );
    }

    private ExperimentResult runSingleExperiment(
            CubeSchema cubeSchema,
            CubeFieldValidator cubeFieldValidator,
            LLMModelConfig modelConfig,
            List<PromptTechnique> promptCombination,
            LLMExperimentCase experimentCase,
            QuerySimilarityEvaluator querySimilarityEvaluator
    ) {

        String runId =
                nextRunId();

        String timestamp =
                currentTimestamp();

        String combinationName =
                PromptTechniqueCombinationGenerator
                        .getCombinationName(
                                promptCombination
                        );

        String prompt = "";

        PromptSizeStats promptSizeStats =
                PromptSizeStats.fromPrompt("");

        try {

            prompt =
                    promptBuilder.buildPrompt(
                            cubeSchema,
                            experimentCase.getQuestion(),
                            promptCombination
                    );

            promptSizeStats =
                    PromptSizeStats.fromPrompt(
                            prompt
                    );

            LLMCallResult llmCallResult =
                    llmClient.generate(
                            modelConfig,
                            prompt
                    );

            String actualAnswer =
                    llmCallResult.getAnswer();

            NormalizationResult normalizationResult =
                    CubeQueryNormalizer.normalize(
                            actualAnswer
                    );

            ValidationResult strictValidationResult =
                    CubeQueryValidator.validate(
                            normalizationResult,
                            experimentCase.getExpectedQuery(),
                            cubeFieldValidator
                    );

            ValidationResult structuralValidationResult =
                    CubeQueryValidator
                            .validateIgnoringSigmaValues(
                                    normalizationResult,
                                    experimentCase.getExpectedQuery(),
                                    cubeFieldValidator
                            );

            QuerySimilarityResult querySimilarityResult;

            if (llmCallResult.isSuccess()
                    && normalizationResult.isSuccess()
                    && normalizationResult.getParsedQuery() != null) {

                querySimilarityResult =
                        querySimilarityEvaluator.evaluate(
                                experimentCase
                                        .getExpectedQuery(),
                                normalizationResult
                                        .getParsedQuery()
                        );

            } else {

                querySimilarityResult =
                        QuerySimilarityResult
                                .unavailable();
            }

            return ExperimentResult.from(
                    runId,
                    timestamp,
                    modelConfig,
                    combinationName,
                    experimentCase,
                    actualAnswer,
                    strictValidationResult,
                    structuralValidationResult,
                    querySimilarityResult,
                    promptSizeStats,
                    llmCallResult
            );

        } catch (Exception e) {

            return createFailedExperimentResult(
                    runId,
                    timestamp,
                    cubeFieldValidator,
                    modelConfig,
                    combinationName,
                    experimentCase,
                    promptSizeStats,
                    e
            );
        }
    }

    private ExperimentResult createFailedExperimentResult(
            String runId,
            String timestamp,
            CubeFieldValidator cubeFieldValidator,
            LLMModelConfig modelConfig,
            String combinationName,
            LLMExperimentCase experimentCase,
            PromptSizeStats promptSizeStats,
            Exception exception
    ) {

        LLMCallResult failedCallResult =
                LLMCallResult.failure(
                        0L,
                        "RUNNER_EXCEPTION",
                        exception
                                .getClass()
                                .getSimpleName()
                                + ": "
                                + safe(
                                exception.getMessage()
                        ),
                        ""
                );

        ExpectedCubeQuery expectedQuery =
                experimentCase == null
                        ? null
                        : experimentCase
                        .getExpectedQuery();

        ValidationResult strictValidationResult;
        ValidationResult structuralValidationResult;

        if (expectedQuery == null) {

            strictValidationResult =
                    new ValidationResult(
                            false,
                            null
                    );

            structuralValidationResult =
                    new ValidationResult(
                            false,
                            null
                    );

        } else {

            NormalizationResult failedNormalizationResult =
                    CubeQueryNormalizer.normalize("");

            strictValidationResult =
                    CubeQueryValidator.validate(
                            failedNormalizationResult,
                            expectedQuery,
                            cubeFieldValidator
                    );

            structuralValidationResult =
                    CubeQueryValidator
                            .validateIgnoringSigmaValues(
                                    failedNormalizationResult,
                                    expectedQuery,
                                    cubeFieldValidator
                            );
        }

        QuerySimilarityResult querySimilarityResult =
                QuerySimilarityResult
                        .unavailable();

        return ExperimentResult.from(
                runId,
                timestamp,
                modelConfig,
                combinationName,
                experimentCase,
                "",
                strictValidationResult,
                structuralValidationResult,
                querySimilarityResult,
                promptSizeStats,
                failedCallResult
        );
    }

    private void printExperimentOverview(
            List<LLMModelConfig> modelConfigs,
            List<List<PromptTechnique>> promptCombinations,
            List<LLMExperimentCase> experimentCases,
            int totalRuns
    ) {

        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "STARTING LLM EXPERIMENTS"
        );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Models: "
                        + modelConfigs.size()
        );

        System.out.println(
                "Prompt combinations: "
                        + promptCombinations.size()
        );

        System.out.println(
                "Paired test cases: "
                        + experimentCases.size()
        );

        System.out.println(
                "Semantic pairs: "
                        + (
                        experimentCases.size()
                                / 2
                )
        );

        System.out.println(
                "Order seed: "
                        + EXPERIMENT_ORDER_SEED
        );

        System.out.println(
                "Total measured runs: "
                        + totalRuns
        );

        System.out.println(
                "Separate warm-up calls: 0"
        );

        System.out.println(
                "First real request in each block is measured."
        );

        System.out.println();
    }

    private void printExperimentOrder(
            List<LLMExperimentCase> experimentCases
    ) {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "DETERMINISTIC EXPERIMENT ORDER"
        );

        System.out.println(
                "Seed: "
                        + EXPERIMENT_ORDER_SEED
        );

        System.out.println(
                "========================================"
        );

        for (int i = 0;
             i < experimentCases.size();
             i++) {

            LLMExperimentCase experimentCase =
                    experimentCases.get(i);

            String phase =
                    i == 0
                            ? "FIRST_REQUEST"
                            : "SUBSEQUENT_REQUEST";

            System.out.println(
                    (i + 1)
                            + ". "
                            + experimentCase.getTestId()
                            + " | pair="
                            + experimentCase.getPairId()
                            + " | wording="
                            + experimentCase.getWordingType()
                            + " | phase="
                            + phase
            );
        }

        System.out.println();
    }

    private void printRunSummary(
            ExperimentResult result,
            int totalRuns,
            int requestIndexInBlock
    ) {

        String similarityText =
                result.isQuerySimilarityAvailable()
                        ? formatSimilarity(
                        result.getOverallSimilarity()
                )
                        : "N/A";

        String phase =
                requestIndexInBlock == 1
                        ? "FIRST_REQUEST"
                        : "SUBSEQUENT_REQUEST";

        String cachedTokensText =
                result.getPromptEvalCachedCount() >= 0L
                        ? String.valueOf(
                        result.getPromptEvalCachedCount()
                )
                        : "N/A";

        String uncachedTokensText =
                result.getPromptEvalUncachedCount() >= 0L
                        ? String.valueOf(
                        result.getPromptEvalUncachedCount()
                )
                        : "N/A";

        String cacheHitRatioText =
                result.getPromptCacheHitRatio() >= 0.0
                        ? formatPercentage(
                        result.getPromptCacheHitRatio()
                )
                        : "N/A";

        System.out.println(
                "["
                        + runCounter
                        + "/"
                        + totalRuns
                        + "] "
                        + "llm="
                        + result.getLlmName()
                        + " | combination="
                        + result.getPromptCombinationName()
                        + " | requestInBlock="
                        + requestIndexInBlock
                        + " | phase="
                        + phase
                        + " | test="
                        + result.getTestId()
                        + " | pair="
                        + result.getPairId()
                        + " | wording="
                        + result.getWordingType()
                        + " | structuralAccuracy="
                        + result.getStructuralAccuracy()
                        + " | structuralWeighted="
                        + result.getStructuralWeightedScore()
                        + " | overallSimilarity="
                        + similarityText
                        + " | promptTokens="
                        + result.getPromptEvalCount()
                        + " | cachedTokens="
                        + cachedTokensText
                        + " | uncachedTokens="
                        + uncachedTokensText
                        + " | cacheHitRatio="
                        + cacheHitRatioText
                        + " | promptEvalMs="
                        + result.getPromptEvalDurationMs()
                        + " | evalMs="
                        + result.getEvalDurationMs()
                        + " | totalMs="
                        + result.getTotalDurationMs()
                        + " | loadMs="
                        + result.getLoadDurationMs()
                        + " | responseTimeMs="
                        + result.getResponseTimeMs()
                        + " | error="
                        + result.getErrorType()
        );
    }

    private void validateInputs(
            CubeSchema cubeSchema,
            List<LLMModelConfig> modelConfigs,
            List<List<PromptTechnique>> promptCombinations,
            List<LLMExperimentCase> experimentCases,
            ExperimentResultsWriter resultsWriter,
            ValidationLogWriter validationLogWriter
    ) {

        if (cubeSchema == null) {
            throw new IllegalArgumentException(
                    "cubeSchema cannot be null."
            );
        }

        if (modelConfigs == null
                || modelConfigs.isEmpty()) {

            throw new IllegalArgumentException(
                    "modelConfigs cannot be null or empty."
            );
        }

        for (LLMModelConfig modelConfig
                : modelConfigs) {

            if (modelConfig == null) {
                throw new IllegalArgumentException(
                        "modelConfigs cannot contain null."
                );
            }
        }

        if (promptCombinations == null
                || promptCombinations.isEmpty()) {

            throw new IllegalArgumentException(
                    "promptCombinations cannot be null or empty."
            );
        }

        for (List<PromptTechnique> combination
                : promptCombinations) {

            if (combination == null) {
                throw new IllegalArgumentException(
                        "promptCombinations cannot contain null combinations."
                );
            }

            for (PromptTechnique technique
                    : combination) {

                if (technique == null) {
                    throw new IllegalArgumentException(
                            "A prompt combination cannot contain null techniques."
                    );
                }
            }
        }

        if (experimentCases == null
                || experimentCases.isEmpty()) {

            throw new IllegalArgumentException(
                    "experimentCases cannot be null or empty."
            );
        }

        for (LLMExperimentCase experimentCase
                : experimentCases) {

            if (experimentCase == null) {
                throw new IllegalArgumentException(
                        "experimentCases cannot contain null."
                );
            }

            if (experimentCase.getExpectedQuery() == null) {
                throw new IllegalArgumentException(
                        "Experiment case "
                                + experimentCase.getTestId()
                                + " has null expectedQuery."
                );
            }

            if (experimentCase.getQuestion() == null
                    || experimentCase
                    .getQuestion()
                    .trim()
                    .isEmpty()) {

                throw new IllegalArgumentException(
                        "Experiment case "
                                + experimentCase.getTestId()
                                + " has an empty question."
                );
            }

            if (experimentCase.getPairId() == null
                    || experimentCase
                    .getPairId()
                    .trim()
                    .isEmpty()) {

                throw new IllegalArgumentException(
                        "Experiment case "
                                + experimentCase.getTestId()
                                + " has an empty pairId."
                );
            }

            if (experimentCase.getWordingType() == null
                    || experimentCase
                    .getWordingType()
                    .trim()
                    .isEmpty()) {

                throw new IllegalArgumentException(
                        "Experiment case "
                                + experimentCase.getTestId()
                                + " has an empty wordingType."
                );
            }

            String wordingType =
                    experimentCase.getWordingType();

            if (!LLMExperimentCase
                    .WORDING_TYPE_STRUCTURED
                    .equals(wordingType)
                    && !LLMExperimentCase
                    .WORDING_TYPE_NATURAL
                    .equals(wordingType)) {

                throw new IllegalArgumentException(
                        "Experiment case "
                                + experimentCase.getTestId()
                                + " has invalid wordingType: "
                                + wordingType
                );
            }
        }

        if (resultsWriter == null) {
            throw new IllegalArgumentException(
                    "resultsWriter cannot be null."
            );
        }

        if (validationLogWriter == null) {
            throw new IllegalArgumentException(
                    "validationLogWriter cannot be null."
            );
        }
    }

    private String nextRunId() {

        runCounter++;

        if (runCounter < 10) {
            return "run_000" + runCounter;
        }

        if (runCounter < 100) {
            return "run_00" + runCounter;
        }

        if (runCounter < 1000) {
            return "run_0" + runCounter;
        }

        return "run_" + runCounter;
    }

    private String currentTimestamp() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm:ss"
                );

        return LocalDateTime
                .now()
                .format(formatter);
    }

    private String safe(
            String value
    ) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private String formatSimilarity(
            double value
    ) {
        return String.format(
                java.util.Locale.US,
                "%.2f",
                value
        );
    }

    private String formatPercentage(
            double ratio
    ) {
        return String.format(
                java.util.Locale.US,
                "%.2f%%",
                ratio * 100.0
        );
    }
}