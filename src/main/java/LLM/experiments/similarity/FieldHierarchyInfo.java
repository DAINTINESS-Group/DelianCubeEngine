package LLM.experiments.similarity;

public class FieldHierarchyInfo {

    private final String fieldName;
    private final String dimensionName;
    private final String hierarchyName;
    private final String levelName;
    private final int levelPosition;
    private final int hierarchySize;

    public FieldHierarchyInfo(
            String fieldName,
            String dimensionName,
            String hierarchyName,
            String levelName,
            int levelPosition,
            int hierarchySize) {

        this.fieldName = safeString(fieldName);
        this.dimensionName = safeString(dimensionName);
        this.hierarchyName = safeString(hierarchyName);
        this.levelName = safeString(levelName);
        this.levelPosition = levelPosition;
        this.hierarchySize = Math.max(0, hierarchySize);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public String getHierarchyName() {
        return hierarchyName;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getLevelPosition() {
        return levelPosition;
    }

    public int getHierarchySize() {
        return hierarchySize;
    }

    public boolean hasKnownHierarchyPosition() {
        return levelPosition >= 0 && hierarchySize > 0;
    }

    public boolean belongsToSameDimension(FieldHierarchyInfo other) {
        if (other == null) {
            return false;
        }

        return normalize(dimensionName)
                .equals(normalize(other.dimensionName));
    }

    public boolean belongsToSameHierarchy(FieldHierarchyInfo other) {
        if (other == null) {
            return false;
        }

        if (!belongsToSameDimension(other)) {
            return false;
        }

        return normalize(hierarchyName)
                .equals(normalize(other.hierarchyName));
    }

    public boolean belongsToSameLevel(FieldHierarchyInfo other) {
        if (other == null) {
            return false;
        }

        if (!belongsToSameHierarchy(other)) {
            return false;
        }

        return normalize(levelName)
                .equals(normalize(other.levelName));
    }

    public int getLevelDistance(FieldHierarchyInfo other) {
        if (other == null) {
            return -1;
        }

        if (!belongsToSameHierarchy(other)) {
            return -1;
        }

        if (!hasKnownHierarchyPosition()
                || !other.hasKnownHierarchyPosition()) {
            return -1;
        }

        return Math.abs(levelPosition - other.levelPosition);
    }

    private static String safeString(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private static String normalize(String value) {
        return safeString(value).toLowerCase();
    }

    @Override
    public String toString() {
        return "FieldHierarchyInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", dimensionName='" + dimensionName + '\'' +
                ", hierarchyName='" + hierarchyName + '\'' +
                ", levelName='" + levelName + '\'' +
                ", levelPosition=" + levelPosition +
                ", hierarchySize=" + hierarchySize +
                '}';
    }
}