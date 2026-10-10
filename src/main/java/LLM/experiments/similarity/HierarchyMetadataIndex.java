package LLM.experiments.similarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LLM.schema.CubeSchema;
import LLM.schema.DimensionSchema;
import LLM.schema.LevelAttributeSchema;
import LLM.schema.LevelSchema;

public class HierarchyMetadataIndex {

    private final Map<String, FieldHierarchyInfo> fields;

    public HierarchyMetadataIndex(CubeSchema cubeSchema) {
        this.fields = new HashMap<String, FieldHierarchyInfo>();
        buildIndex(cubeSchema);
    }

    public FieldHierarchyInfo getFieldInfo(String fieldName) {
        String normalized = normalizeFieldName(fieldName);

        if (normalized.isEmpty()) {
            return null;
        }

        return fields.get(normalized);
    }

    public boolean containsField(String fieldName) {
        return getFieldInfo(fieldName) != null;
    }

    public int size() {
        return fields.size();
    }

    private void buildIndex(CubeSchema cubeSchema) {
        if (cubeSchema == null || cubeSchema.getDimensions() == null) {
            return;
        }

        for (DimensionSchema dimension : cubeSchema.getDimensions()) {
            if (dimension == null) {
                continue;
            }

            indexDimension(dimension);
        }
    }

    private void indexDimension(DimensionSchema dimension) {
        String dimensionName = safeString(dimension.getName());

        if (dimensionName.isEmpty()) {
            return;
        }

        List<LevelSchema> levels = dimension.getLevels();

        if (levels == null || levels.isEmpty()) {
            return;
        }

        String hierarchyName = dimensionName;

        List<String> hierarchy = dimension.getHierarchy();

        int hierarchySize = hierarchy == null
                ? 0
                : hierarchy.size();

        for (LevelSchema level : levels) {
            if (level == null) {
                continue;
            }

            String levelName = safeString(level.getLevelName());

            if (levelName.isEmpty()) {
                continue;
            }

            int levelPosition = findLevelPosition(
                    hierarchy,
                    levelName
            );

            List<LevelAttributeSchema> attributes =
                    level.getAttributes();

            if (attributes == null) {
                continue;
            }

            for (LevelAttributeSchema attribute : attributes) {
                if (attribute == null) {
                    continue;
                }

                String attributeName =
                        safeString(attribute.getName());

                if (attributeName.isEmpty()) {
                    continue;
                }

                String fullFieldName =
                        dimensionName + "." + attributeName;

                FieldHierarchyInfo info =
                        new FieldHierarchyInfo(
                                fullFieldName,
                                dimensionName,
                                hierarchyName,
                                levelName,
                                levelPosition,
                                hierarchySize
                        );

                fields.put(
                        normalizeFieldName(fullFieldName),
                        info
                );
            }
        }
    }

    private static int findLevelPosition(
            List<String> hierarchy,
            String levelName) {

        if (hierarchy == null || hierarchy.isEmpty()) {
            return -1;
        }

        String normalizedLevelName = normalizeName(levelName);

        for (int i = 0; i < hierarchy.size(); i++) {
            String hierarchyLevel = hierarchy.get(i);

            if (normalizeName(hierarchyLevel)
                    .equals(normalizedLevelName)) {
                return i;
            }
        }

        return -1;
    }

    private static String normalizeFieldName(String fieldName) {
        if (fieldName == null) {
            return "";
        }

        String normalized = fieldName.trim();

        while (normalized.endsWith(";")) {
            normalized = normalized.substring(
                    0,
                    normalized.length() - 1
            ).trim();
        }

        return normalized.replaceAll("\\s+", "");
    }

    private static String normalizeName(String value) {
        return safeString(value).toLowerCase();
    }

    private static String safeString(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    @Override
    public String toString() {
        return "HierarchyMetadataIndex{" +
                "fieldCount=" + fields.size() +
                '}';
    }
}