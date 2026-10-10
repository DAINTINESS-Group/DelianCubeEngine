package client;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Properties;
import java.util.Scanner;

import LLM.client.OllamaLLMClient;

import LLM.extractor.CubeIniSchemaExtractor;

import LLM.schema.CubeSchema;

import LLM.service.LLMQueryGenerationService;


public class LLMQueryMainClient {

    private static final String CONFIG_RESOURCE =
            "/LLM/llm.properties";

    /*
     * Change these values when using another cube.
     */
    private static final String INPUT_FOLDER =
            "foodmart_reduced";

    private static final String CUBE_FILE_NAME =
            "sales";


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


        Path iniPath =
                Paths.get(
                        "InputFiles",
                        INPUT_FOLDER,
                        CUBE_FILE_NAME + ".ini"
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


        OllamaLLMClient llmClient =
                new OllamaLLMClient(
                        llmHost,
                        apiKey
                );


        LLMQueryGenerationService queryService =
                new LLMQueryGenerationService(
                        llmClient
                );


        String userQuestion =
                readUserQuestion(
                        args
                );


        String generatedQuery =
                queryService.generateQuery(
                        cubeSchema,
                        userQuestion
                );


        System.out.println();
        System.out.println(
                "Generated CineCubes query:"
        );
        System.out.println();

        System.out.println(
                generatedQuery
        );
    }


    private static String readUserQuestion(
            String[] args
    ) {

        if (args != null
                && args.length > 0) {

            StringBuilder builder =
                    new StringBuilder();

            for (String argument : args) {

                if (builder.length() > 0) {
                    builder.append(" ");
                }

                builder.append(
                        argument
                );
            }

            return builder
                    .toString()
                    .trim();
        }


        Scanner scanner =
                new Scanner(
                        System.in
                );


        System.out.println(
                "Enter a natural-language analytical question:"
        );

        String question =
                scanner.nextLine();


        if (question == null
                || question.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "User question cannot be empty."
            );
        }


        return question.trim();
    }


    private static Properties loadProperties()
            throws Exception {

        Properties properties =
                new Properties();


        try (
                InputStream inputStream =
                        LLMQueryMainClient.class
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


        if (value.startsWith(
                "WRITE_YOUR_"
        )) {

            throw new IllegalStateException(
                    "Configuration property has not been configured: "
                            + propertyName
            );
        }


        return value;
    }
}