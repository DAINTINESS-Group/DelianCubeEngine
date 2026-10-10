package LLM.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import LLM.client.LLMCallResult;
import LLM.client.LLMModelConfig;
import LLM.client.OllamaLLMClient;

import LLM.normalization.CubeQueryNormalizer;
import LLM.normalization.CubeQueryNormalizer.NormalizationResult;

import LLM.prompts.PromptBuilder;
import LLM.prompts.PromptTechnique;
import LLM.prompts.PromptTechniqueFactory;

import LLM.query.ParsedCubeQuery;

import LLM.schema.CubeSchema;
import LLM.schema.MeasureSchema;

import LLM.validation.CubeFieldValidator;

public class LLMQueryGenerationService {

    private static final String DEFAULT_MODEL =
            "qwen3.8:27b";

    private final OllamaLLMClient llmClient;
    private final PromptBuilder promptBuilder;
    private final LLMModelConfig modelConfig;
    private final List<PromptTechnique> promptTechniques;

    public LLMQueryGenerationService(
            OllamaLLMClient llmClient
    ) {

        if (llmClient == null) {
            throw new IllegalArgumentException(
                    "OllamaLLMClient cannot be null."
            );
        }

        this.llmClient = llmClient;

        this.promptBuilder =
                new PromptBuilder();

        this.modelConfig =
                LLMModelConfig.createExperimentConfig(
                        DEFAULT_MODEL
                );

        this.promptTechniques =
                Collections.<PromptTechnique>singletonList(
                        PromptTechniqueFactory.createByName(
                                PromptTechniqueFactory
                                        .PRESCRIPTION_OF_RESULT
                        )
                );
    }

    public String generateQuery(
            CubeSchema cubeSchema,
            String userQuestion
    ) {

        validateInput(
                cubeSchema,
                userQuestion
        );

        String prompt =
                promptBuilder.buildPrompt(
                        cubeSchema,
                        userQuestion,
                        promptTechniques
                );

        LLMCallResult callResult =
                llmClient.generate(
                        modelConfig,
                        prompt
                );

        if (callResult == null
                || callResult.isFailed()) {

            throw new IllegalStateException(
                    buildLLMErrorMessage(
                            callResult
                    )
            );
        }

        NormalizationResult normalizationResult =
                CubeQueryNormalizer.normalize(
                        callResult.getAnswer()
                );

        if (!normalizationResult.isSuccess()) {

            throw new IllegalStateException(
                    "Generated query could not be normalized: "
                            + normalizationResult.getErrors()
            );
        }

        ParsedCubeQuery parsedQuery =
                normalizationResult.getParsedQuery();

        validateGeneratedQuery(
                cubeSchema,
                parsedQuery
        );

        return normalizationResult
                .getCanonicalQuery();
    }

    private void validateInput(
            CubeSchema cubeSchema,
            String userQuestion
    ) {

        if (cubeSchema == null) {

            throw new IllegalArgumentException(
                    "CubeSchema cannot be null."
            );
        }

        if (userQuestion == null
                || userQuestion.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "User question cannot be null or empty."
            );
        }
    }

    private void validateGeneratedQuery(
            CubeSchema cubeSchema,
            ParsedCubeQuery query
    ) {

        if (query == null) {

            throw new IllegalStateException(
                    "Generated query is null."
            );
        }

        if (!query.hasRequiredCoreFields()) {

            throw new IllegalStateException(
                    "Generated query is missing required fields."
            );
        }

        validateCubeName(
                cubeSchema,
                query
        );

        validateMeasure(
                cubeSchema,
                query
        );

        validateDimensionFields(
                cubeSchema,
                query
        );
    }

    private void validateCubeName(
            CubeSchema cubeSchema,
            ParsedCubeQuery query
    ) {

        String expectedCubeName =
                safeString(
                        cubeSchema.getCubeName()
                );

        String generatedCubeName =
                safeString(
                        query.getCubeName()
                );

        if (!expectedCubeName.equalsIgnoreCase(
                generatedCubeName
        )) {

            throw new IllegalStateException(
                    "Unknown cube: "
                            + generatedCubeName
            );
        }
    }

    private void validateMeasure(
            CubeSchema cubeSchema,
            ParsedCubeQuery query
    ) {

        String generatedMeasure =
                safeString(
                        query.getMeasure()
                );

        boolean measureExists = false;

        if (cubeSchema.getMeasures() != null) {

            for (MeasureSchema measure
                    : cubeSchema.getMeasures()) {

                if (measure == null) {
                    continue;
                }

                if (generatedMeasure.equals(
                        safeString(
                                measure.getName()
                        )
                )) {

                    measureExists = true;
                    break;
                }
            }
        }

        if (!measureExists) {

            throw new IllegalStateException(
                    "Unknown measure: "
                            + generatedMeasure
            );
        }
    }

    private void validateDimensionFields(
            CubeSchema cubeSchema,
            ParsedCubeQuery query
    ) {

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(
                        cubeSchema
                );

        Set<String> fieldsToValidate =
                new HashSet<String>();

        fieldsToValidate.addAll(
                query.getGammaFields()
        );

        for (String sigmaCondition
                : query.getSigmaConditions()) {

            String sigmaField =
                    extractSigmaField(
                            sigmaCondition
                    );

            if (!sigmaField.isEmpty()) {

                fieldsToValidate.add(
                        sigmaField
                );
            }
        }

        Set<String> unknownFields =
                fieldValidator.findUnknownFields(
                        fieldsToValidate
                );

        if (!unknownFields.isEmpty()) {

            throw new IllegalStateException(
                    "Generated query contains unknown cube fields: "
                            + unknownFields
            );
        }
    }

    private String extractSigmaField(
            String sigmaCondition
    ) {

        if (sigmaCondition == null) {
            return "";
        }

        String condition =
                sigmaCondition.trim();

        if (condition.isEmpty()) {
            return "";
        }

        int operatorIndex =
                findFirstOperatorIndex(
                        condition
                );

        if (operatorIndex <= 0) {
            return "";
        }

        return condition
                .substring(
                        0,
                        operatorIndex
                )
                .trim();
    }

    private int findFirstOperatorIndex(
            String condition
    ) {

        int result = -1;

        String[] operators = {
                " NOT IN ",
                " BETWEEN ",
                " IN ",
                "=",
                "<",
                ">"
        };

        for (String operator : operators) {

            int index =
                    condition.indexOf(
                            operator
                    );

            if (index >= 0
                    && (result == -1
                    || index < result)) {

                result = index;
            }
        }

        return result;
    }

    private String buildLLMErrorMessage(
            LLMCallResult callResult
    ) {

        if (callResult == null) {

            return "LLM request returned no result.";
        }

        return "LLM request failed. Type: "
                + safeString(
                callResult.getErrorType()
        )
                + ", message: "
                + safeString(
                callResult.getErrorMessage()
        );
    }

    private String safeString(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }
}