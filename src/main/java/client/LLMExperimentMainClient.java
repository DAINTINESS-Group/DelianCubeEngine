package client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import LLM.extractor.CubeIniSchemaExtractor;
import LLM.schema.CubeSchema;

import LLM.experiments.LLMBasicExperiments;
import LLM.experiments.LLMExperimentCase;

import LLM.prompts.PromptTechnique;
import LLM.prompts.PromptTechniqueFactory;
import LLM.prompts.PromptTechniqueCombinationGenerator;

import LLM.experiments.runner.LLMExperimentRunner;
import LLM.client.LLMModelConfig;
import LLM.client.OllamaLLMClient;

import LLM.experiments.results.ExperimentResultsWriter;
import LLM.experiments.results.ValidationLogWriter;

public class LLMExperimentMainClient {

    private static final String LLM_ENDPOINT =
            getRequiredEnv("HTTP_GATE");

    private static final String API_KEY =
            getRequiredEnv("API_KEY");

    private static final String BENCHMARK_MODEL =
            "gemma4:26b";

    private static final String INSTRUCTIONS_TECHNIQUE =
            "instructions_for_query_generation";

    private static final String EXAMPLES_TECHNIQUE =
            "examples";


    public static void main(
            String[] args
    ) throws Exception {

        String inputFolder =
                "foodmart_reduced";

        String cubeFileName =
                "sales";


        Path iniPath =
                Paths.get(
                        "InputFiles",
                        inputFolder,
                        cubeFileName + ".ini"
                );


        if (!Files.exists(iniPath)) {

            throw new RuntimeException(
                    "INI file not found: "
                            + iniPath.toAbsolutePath()
            );
        }


        System.out.println(
                "===== GEMMA 26B CACHE BENCHMARK ====="
        );

        System.out.println(
                "LLM endpoint: "
                        + LLM_ENDPOINT
        );

        System.out.println(
                "INI file: "
                        + iniPath.toAbsolutePath()
        );

        System.out.println();


        CubeIniSchemaExtractor extractor =
                new CubeIniSchemaExtractor();

        CubeSchema cubeSchema =
                extractor.extractFromFile(
                        iniPath.toString()
                );


        printCubeSchemaSummary(
                cubeSchema
        );


        List<LLMModelConfig> modelConfigs =
                buildModelConfigs();


        List<PromptTechnique> selectedTechniques =
                buildSelectedPromptTechniques();


        List<List<PromptTechnique>> promptCombinations =
                buildSelectedPromptCombinations(
                        selectedTechniques
                );


        List<LLMExperimentCase> experimentCases =
                LLMBasicExperiments
                        .getBasicFoodmartExperiments();


        int totalRuns =
                modelConfigs.size()
                        * promptCombinations.size()
                        * experimentCases.size();


        if (totalRuns != 30) {

            throw new IllegalStateException(
                    "Expected 30 runs but calculated "
                            + totalRuns
            );
        }


        Path outputDirectory =
                Paths.get(
                        "experiment_outputs"
                );

        Files.createDirectories(
                outputDirectory
        );


        String timestampForFile =
                createTimestampForFileName();


        Path resultsPath =
                outputDirectory.resolve(
                        "gemma26_instructions_examples_cache_"
                                + timestampForFile
                                + ".tsv"
                );


        Path validationLogPath =
                outputDirectory.resolve(
                        "gemma26_instructions_examples_validation_"
                                + timestampForFile
                                + ".txt"
                );


        System.out.println(
                "Output TSV:"
        );

        System.out.println(
                resultsPath.toAbsolutePath()
        );

        System.out.println();


        System.out.println(
                "Validation log:"
        );

        System.out.println(
                validationLogPath.toAbsolutePath()
        );

        System.out.println();


        printExperimentPlan(
                modelConfigs,
                selectedTechniques,
                promptCombinations,
                experimentCases
        );


        OllamaLLMClient llmClient =
                new OllamaLLMClient(
                        LLM_ENDPOINT,
                        API_KEY
                );


        LLMExperimentRunner runner =
                new LLMExperimentRunner(
                        llmClient
                );


        try (
                ExperimentResultsWriter resultsWriter =
                        new ExperimentResultsWriter(
                                resultsPath
                        );

                ValidationLogWriter validationLogWriter =
                        new ValidationLogWriter(
                                validationLogPath
                        )
        ) {

            runner.runExperiments(
                    cubeSchema,
                    modelConfigs,
                    promptCombinations,
                    experimentCases,
                    resultsWriter,
                    validationLogWriter
            );
        }


        System.out.println();

        System.out.println(
                "Benchmark completed."
        );

        System.out.println(
                "Results file: "
                        + resultsPath.toAbsolutePath()
        );

        System.out.println(
                "Validation log: "
                        + validationLogPath.toAbsolutePath()
        );
    }


    private static List<LLMModelConfig> buildModelConfigs() {

        List<LLMModelConfig> modelConfigs =
                new ArrayList<LLMModelConfig>();


        modelConfigs.add(
                LLMModelConfig.createExperimentConfig(
                        BENCHMARK_MODEL,
                        4096,
                        256,
                        true
                )
        );


        return modelConfigs;
    }


    private static List<PromptTechnique>
    buildSelectedPromptTechniques() {

        List<PromptTechnique> allTechniques =
                PromptTechniqueFactory
                        .getAllPromptTechniques();


        List<PromptTechnique> selectedTechniques =
                new ArrayList<PromptTechnique>();


        for (PromptTechnique technique
                : allTechniques) {

            if (technique == null) {
                continue;
            }


            String name =
                    technique.getName();


            if (INSTRUCTIONS_TECHNIQUE.equals(name)
                    || EXAMPLES_TECHNIQUE.equals(name)) {

                selectedTechniques.add(
                        technique
                );
            }
        }


        if (selectedTechniques.size() != 2) {

            throw new IllegalStateException(
                    "Expected exactly 2 prompt techniques "
                            + "(instructions + examples), but found "
                            + selectedTechniques.size()
            );
        }


        return selectedTechniques;
    }


    private static List<List<PromptTechnique>>
    buildSelectedPromptCombinations(
            List<PromptTechnique> selectedTechniques
    ) {

        List<PromptTechnique> combination =
                new ArrayList<PromptTechnique>();

        combination.addAll(
                selectedTechniques
        );


        List<List<PromptTechnique>> combinations =
                new ArrayList<List<PromptTechnique>>();

        combinations.add(
                combination
        );


        return combinations;
    }


    private static void printCubeSchemaSummary(
            CubeSchema cubeSchema
    ) {

        System.out.println(
                "===== CUBE SCHEMA SUMMARY ====="
        );

        System.out.println(
                "Cube Name: "
                        + cubeSchema.getCubeName()
        );

        System.out.println(
                "Dimensions Count: "
                        + cubeSchema
                        .getDimensions()
                        .size()
        );

        System.out.println(
                "Measures Count: "
                        + cubeSchema
                        .getMeasures()
                        .size()
        );

        System.out.println();
    }


    private static void printExperimentPlan(
            List<LLMModelConfig> modelConfigs,
            List<PromptTechnique> selectedTechniques,
            List<List<PromptTechnique>> promptCombinations,
            List<LLMExperimentCase> experimentCases
    ) {

        int totalRuns =
                modelConfigs.size()
                        * promptCombinations.size()
                        * experimentCases.size();


        System.out.println(
                "===== CACHE BENCHMARK PLAN ====="
        );

        System.out.println(
                "Model: "
                        + modelConfigs.get(0).getModelName()
        );


        System.out.println(
                "Prompt techniques:"
        );

        for (PromptTechnique technique
                : selectedTechniques) {

            System.out.println(
                    "- "
                            + technique.getName()
            );
        }


        System.out.println();


        System.out.println(
                "Prompt combination:"
        );

        System.out.println(
                PromptTechniqueCombinationGenerator
                        .getCombinationName(
                                promptCombinations.get(0)
                        )
        );


        System.out.println();

        System.out.println(
                "Experiment cases: "
                        + experimentCases.size()
        );

        System.out.println(
                "Measured requests: "
                        + totalRuns
        );

        System.out.println(
                "Separate warm-up calls: 0"
        );

        System.out.println();


        System.out.println(
                "Request 1:"
        );

        System.out.println(
                "First real measured request / cache seed"
        );


        System.out.println();

        System.out.println(
                "Requests 2-30:"
        );

        System.out.println(
                "Real measured requests / cached-prefix behaviour"
        );

        System.out.println();
    }


    private static String createTimestampForFileName() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd_HH-mm-ss"
                );

        return LocalDateTime
                .now()
                .format(
                        formatter
                );
    }


    private static String getRequiredEnv(
            String name
    ) {

        String value =
                System.getenv(
                        name
                );


        if (value == null
                || value.trim().isEmpty()) {

            throw new IllegalStateException(
                    "Missing required environment variable: "
                            + name
            );
        }


        return value.trim();
    }
}