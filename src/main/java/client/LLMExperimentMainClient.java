package client;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import LLM.client.LLMModelConfig;
import LLM.client.OllamaLLMClient;

import LLM.extractor.CubeIniSchemaExtractor;

import LLM.schema.CubeSchema;

import LLM.experiments.LLMBasicExperiments;
import LLM.experiments.LLMExperimentCase;

import LLM.experiments.runner.LLMExperimentRunner;

import LLM.experiments.results.ExperimentResultsWriter;
import LLM.experiments.results.ValidationLogWriter;

import LLM.prompts.PromptTechnique;
import LLM.prompts.PromptTechniqueCombinationGenerator;
import LLM.prompts.PromptTechniqueFactory;


public class LLMExperimentMainClient {

    private static final String CONFIG_RESOURCE =
            "/LLM/llm.properties";


    public static void main(
            String[] args
    ) throws Exception {

        Properties properties =
                loadProperties();


        String llmHost =
                getRequiredProperty(
                        properties,
                        "llm.host"
                );


        String apiKey =
                getRequiredProperty(
                        properties,
                        "llm.apiKey"
                );


        // Change these values when using another cube.
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

            throw new IllegalStateException(
                    "INI file not found: "
                            + iniPath.toAbsolutePath()
            );
        }


        CubeIniSchemaExtractor extractor =
                new CubeIniSchemaExtractor();


        CubeSchema cubeSchema =
                extractor.extractFromFile(
                        iniPath.toString()
                );


        List<LLMModelConfig> modelConfigs =
                buildModelConfigs();


        /*
         * All prompt techniques registered in PromptTechniqueFactory
         * are included in the experiment.
         */
        List<PromptTechnique> promptTechniques =
                PromptTechniqueFactory
                        .getAllPromptTechniques();


        List<List<PromptTechnique>> promptCombinations =
                PromptTechniqueCombinationGenerator
                        .generateAllCombinations(
                                promptTechniques
                        );


        List<LLMExperimentCase> experimentCases =
                LLMBasicExperiments
                        .getBasicFoodmartExperiments();


        int totalRuns =
                modelConfigs.size()
                        * promptCombinations.size()
                        * experimentCases.size();


        printExperimentPlan(
                cubeSchema,
                modelConfigs,
                promptTechniques,
                promptCombinations,
                experimentCases,
                totalRuns
        );


        Path outputDirectory =
                Paths.get(
                        "experiment_outputs"
                );


        Files.createDirectories(
                outputDirectory
        );


        String timestamp =
                createTimestampForFileName();


        Path resultsPath =
                outputDirectory.resolve(
                        "experiment_results_"
                                + timestamp
                                + ".tsv"
                );


        Path validationLogPath =
                outputDirectory.resolve(
                        "validation_log_"
                                + timestamp
                                + ".txt"
                );


        OllamaLLMClient llmClient =
                new OllamaLLMClient(
                        llmHost,
                        apiKey
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
                "Experiments completed."
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


        /*
         * Add or remove models here.
         * Generation parameters are defined in LLMModelConfig.
         */
        addModel(
                modelConfigs,
                "gemma4:26b"
        );

        addModel(
                modelConfigs,
                "qwen3.5:35b"
        );

        addModel(
                modelConfigs,
                "gpt-oss:20b"
        );

        addModel(
                modelConfigs,
                "devstral-small-2:24b"
        );

        addModel(
                modelConfigs,
                "qwen3-coder:30b"
        );

        addModel(
                modelConfigs,
                "gemma4:31b"
        );

        addModel(
                modelConfigs,
                "qwen3.8:27b"
        );


        return modelConfigs;
    }


    private static void addModel(
            List<LLMModelConfig> modelConfigs,
            String modelName
    ) {

        modelConfigs.add(
                LLMModelConfig.createExperimentConfig(
                        modelName
                )
        );
    }


    private static Properties loadProperties()
            throws Exception {

        Properties properties =
                new Properties();


        try (
                InputStream inputStream =
                        LLMExperimentMainClient.class
                                .getResourceAsStream(
                                        CONFIG_RESOURCE
                                )
        ) {

            if (inputStream == null) {

                throw new IllegalStateException(
                        "LLM configuration file not found: "
                                + CONFIG_RESOURCE
                );
            }


            properties.load(
                    inputStream
            );
        }


        return properties;
    }


    private static String getRequiredProperty(
            Properties properties,
            String propertyName
    ) {

        String value =
                properties.getProperty(
                        propertyName
                );


        if (value == null
                || value.trim().isEmpty()) {

            throw new IllegalStateException(
                    "Missing configuration property: "
                            + propertyName
            );
        }


        value =
                value.trim();


        if (value.startsWith("WRITE_YOUR_")) {

            throw new IllegalStateException(
                    "Configuration property has not been configured: "
                            + propertyName
            );
        }


        return value;
    }


    private static void printExperimentPlan(
            CubeSchema cubeSchema,
            List<LLMModelConfig> modelConfigs,
            List<PromptTechnique> promptTechniques,
            List<List<PromptTechnique>> promptCombinations,
            List<LLMExperimentCase> experimentCases,
            int totalRuns
    ) {

        System.out.println(
                "===== LLM EXPERIMENT PLAN ====="
        );


        System.out.println(
                "Cube: "
                        + cubeSchema.getCubeName()
        );


        System.out.println();
        System.out.println("Models:");

        for (LLMModelConfig modelConfig
                : modelConfigs) {

            System.out.println(
                    "- "
                            + modelConfig.getModelName()
            );
        }


        System.out.println();
        System.out.println("Prompt techniques:");

        for (PromptTechnique technique
                : promptTechniques) {

            System.out.println(
                    "- "
                            + technique.getName()
            );
        }


        System.out.println();

        System.out.println(
                "Prompt configurations: "
                        + promptCombinations.size()
        );


        System.out.println(
                "Experiment cases: "
                        + experimentCases.size()
        );


        System.out.println(
                "Total measured runs: "
                        + totalRuns
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
}