package client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import LLM.extractor.CubeIniSchemaExtractor;
import LLM.prompts.CubeSchemaPromptFormatterCompact;
import LLM.schema.CubeSchema;

public class CubeSchemaFormatterInspectionClient {

    public static void main(String[] args) throws Exception {

        Path iniPath =
                Paths.get(
                        "InputFiles",
                        "foodmart_reduced",
                        "sales.ini"
                );

        if (!Files.exists(iniPath)) {
            throw new RuntimeException(
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

        CubeSchemaPromptFormatterCompact formatter =
                new CubeSchemaPromptFormatterCompact();

        String formattedSchema =
                formatter.format(
                        cubeSchema
                );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "CUBE SCHEMA PROMPT FORMATTER OUTPUT"
        );

        System.out.println(
                "========================================"
        );

        System.out.println();

        System.out.println(
                formattedSchema
        );

        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "FIELD CHECKS"
        );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "product_dim.product_subcategory = "
                        + formattedSchema.contains(
                        "product_dim.product_subcategory"
                )
        );

        System.out.println(
                "product_dim.product_category = "
                        + formattedSchema.contains(
                        "product_dim.product_category"
                )
        );

        System.out.println(
                "promotion_dim.media_type = "
                        + formattedSchema.contains(
                        "promotion_dim.media_type"
                )
        );

        System.out.println(
                "date_dim.year_quarter = "
                        + formattedSchema.contains(
                        "date_dim.year_quarter"
                )
        );

        System.out.println();

        System.out.println(
                "Characters: "
                        + formattedSchema.length()
        );
    }
}