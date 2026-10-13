package client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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


    /*
     * Change this list when running a different set of LLMs.
     */
    private static final String[] BENCHMARK_MODELS = {
            "gemma4:26b",
            "gemma4:31b",
            "qwen3.8:27b",
            "qwen3-coder:30b",
            "qwen3.5:35b",
            "devstral-small-2:24b",
            "gpt-oss:20b"
    };


    /*
     * Change this when running a different cube.
     */
    private static final String CUBE_NAME =
            "sales";


    public static void main(
            String[] args
    ) throws Exception {

        String inputFolder =
                "foodmart_reduced";

        Path iniPath =
                Paths.get(
                        "InputFiles",
                        inputFolder,
                        CUBE_NAME + ".ini"
                );

        if (!Files.exists(iniPath)) {
            throw new RuntimeException(
                    "INI file not found: "
                            + iniPath.toAbsolutePath()
            );
        }

        System.out.println(
                "===== LLM EXPERIMENTS ====="
        );

        System.out.println(
                "Cube: "
                        + CUBE_NAME
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
                PromptTechniqueCombinationGenerator
                        .generateAllCombinations(
                                selectedTechniques
                        );

        List<LLMExperimentCase> experimentCases =
                LLMBasicExperiments
                        .getBasicFoodmartExperiments();


        int totalRuns =
                modelConfigs.size()
                        * promptCombinations.size()
                        * experimentCases.size();

        System.out.println(
                "Models: "
                        + modelConfigs.size()
        );

        System.out.println(
                "Prompt combinations: "
                        + promptCombinations.size()
        );

        System.out.println(
                "Experiment cases: "
                        + experimentCases.size()
        );

        System.out.println(
                "Total runs: "
                        + totalRuns
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
                        new ExperimentResultsWriter();

                ValidationLogWriter validationLogWriter =
                        new ValidationLogWriter()
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
                "Experiments completed."
        );

        System.out.println(
                "Total runs: "
                        + totalRuns
        );

        System.out.println(
                "Validation log written successfully."
        );
    }


    private static List<LLMModelConfig> buildModelConfigs() {

        List<LLMModelConfig> modelConfigs =
                new ArrayList<LLMModelConfig>();

        for (String modelName : BENCHMARK_MODELS) {

            modelConfigs.add(
                    LLMModelConfig.createExperimentConfig(
                            modelName,
                            4096,
                            256,
                            true
                    )
            );
        }

        return modelConfigs;
    }


    private static List<PromptTechnique>
    buildSelectedPromptTechniques() {

        return PromptTechniqueFactory
                .getAllPromptTechniques();
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

        System.out.println(
                "===== EXPERIMENT PLAN ====="
        );

        System.out.println(
                "Models:"
        );

        for (LLMModelConfig modelConfig
                : modelConfigs) {

            System.out.println(
                    "- "
                            + modelConfig.getModelName()
            );
        }

        System.out.println();

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
                "Prompt combinations:"
        );

        for (List<PromptTechnique> combination
                : promptCombinations) {

            System.out.println(
                    "- "
                            + PromptTechniqueCombinationGenerator
                            .getCombinationName(
                                    combination
                            )
            );
        }

        System.out.println();

        System.out.println(
                "Experiment cases: "
                        + experimentCases.size()
        );

        System.out.println();
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