package LLM.validation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import LLM.schema.CubeSchema;
import LLM.schema.DimensionSchema;
import LLM.schema.LevelAttributeSchema;
import LLM.schema.LevelSchema;

public class CubeFieldValidator {

    private final Set<String> allowedFields;


    public CubeFieldValidator(
            CubeSchema cubeSchema
    ) {

        if (cubeSchema == null) {

            throw new IllegalArgumentException(
                    "CubeSchema cannot be null."
            );
        }

        this.allowedFields =
                collectAllowedFields(
                        cubeSchema
                );
    }


    public boolean fieldExists(
            String field
    ) {

        if (field == null
                || field.trim().isEmpty()) {

            return false;
        }

        return allowedFields.contains(
                field
        );
    }


    public Set<String> findUnknownFields(
            Set<String> fields
    ) {

        Set<String> unknownFields =
                new HashSet<String>();

        if (fields == null
                || fields.isEmpty()) {

            return unknownFields;
        }

        for (String field : fields) {

            if (!fieldExists(field)) {

                unknownFields.add(
                        field
                );
            }
        }

        return unknownFields;
    }


    public Set<String> getAllowedFields() {

        return Collections.unmodifiableSet(
                allowedFields
        );
    }


    private Set<String> collectAllowedFields(
            CubeSchema cubeSchema
    ) {

        Set<String> fields =
                new HashSet<String>();

        if (cubeSchema.getDimensions() == null) {

            return fields;
        }

        for (DimensionSchema dimension
                : cubeSchema.getDimensions()) {

            if (dimension == null
                    || dimension.getName() == null
                    || dimension.getName().trim().isEmpty()
                    || dimension.getLevels() == null) {

                continue;
            }

            for (LevelSchema level
                    : dimension.getLevels()) {

                if (level == null
                        || level.getAttributes() == null) {

                    continue;
                }

                for (LevelAttributeSchema attribute
                        : level.getAttributes()) {

                    if (attribute == null
                            || attribute.getName() == null
                            || attribute.getName().trim().isEmpty()) {

                        continue;
                    }

                    String fullFieldName =
                            dimension.getName()
                                    + "."
                                    + attribute.getName();

                    fields.add(
                            fullFieldName
                    );
                }
            }
        }

        return fields;
    }
}