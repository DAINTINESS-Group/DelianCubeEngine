package LLM.experiments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private final boolean valid;

    private final boolean formatValid;
    private final boolean cubeCorrect;
    private final boolean aggregateFunctionCorrect;
    private final boolean measureCorrect;
    private final boolean gammaCorrect;
    private final boolean sigmaCorrect;

    private final int unknownFieldsCount;
    private final int missingGammaCount;
    private final int extraGammaCount;
    private final int missingSigmaCount;
    private final int extraSigmaCount;
    private final int wrongSigmaValueCount;

    private final double weightedScore;

    private final List<String> errors;

    /*
     * Legacy constructor, για να μη σπάσει παλιός κώδικας αν κάπου το χρησιμοποιείς ακόμα.
     */
    public ValidationResult(boolean valid, List<String> errors) {
        this(
                valid,
                valid,
                valid,
                valid,
                valid,
                valid,
                valid,
                0,
                0,
                0,
                0,
                0,
                0,
                valid ? 100.0 : 0.0,
                errors
        );
    }

    public ValidationResult(
            boolean valid,
            boolean formatValid,
            boolean cubeCorrect,
            boolean aggregateFunctionCorrect,
            boolean measureCorrect,
            boolean gammaCorrect,
            boolean sigmaCorrect,
            int unknownFieldsCount,
            int missingGammaCount,
            int extraGammaCount,
            int missingSigmaCount,
            int extraSigmaCount,
            int wrongSigmaValueCount,
            double weightedScore,
            List<String> errors
    ) {
        this.valid = valid;
        this.formatValid = formatValid;
        this.cubeCorrect = cubeCorrect;
        this.aggregateFunctionCorrect = aggregateFunctionCorrect;
        this.measureCorrect = measureCorrect;
        this.gammaCorrect = gammaCorrect;
        this.sigmaCorrect = sigmaCorrect;

        this.unknownFieldsCount = Math.max(0, unknownFieldsCount);
        this.missingGammaCount = Math.max(0, missingGammaCount);
        this.extraGammaCount = Math.max(0, extraGammaCount);
        this.missingSigmaCount = Math.max(0, missingSigmaCount);
        this.extraSigmaCount = Math.max(0, extraSigmaCount);
        this.wrongSigmaValueCount = Math.max(0, wrongSigmaValueCount);

        this.weightedScore = weightedScore;
        this.errors = copyList(errors);
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isFormatValid() {
        return formatValid;
    }

    public boolean isCubeCorrect() {
        return cubeCorrect;
    }

    public boolean isAggregateFunctionCorrect() {
        return aggregateFunctionCorrect;
    }

    public boolean isMeasureCorrect() {
        return measureCorrect;
    }

    public boolean isGammaCorrect() {
        return gammaCorrect;
    }

    public boolean isSigmaCorrect() {
        return sigmaCorrect;
    }

    public int getUnknownFieldsCount() {
        return unknownFieldsCount;
    }

    public int getMissingGammaCount() {
        return missingGammaCount;
    }

    public int getExtraGammaCount() {
        return extraGammaCount;
    }

    public int getMissingSigmaCount() {
        return missingSigmaCount;
    }

    public int getExtraSigmaCount() {
        return extraSigmaCount;
    }

    public int getWrongSigmaValueCount() {
        return wrongSigmaValueCount;
    }

    public double getWeightedScore() {
        return weightedScore;
    }

    public double getStrictAccuracy() {
        return valid ? 1.0 : 0.0;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    private static List<String> copyList(List<String> values) {
        List<String> copiedValues = new ArrayList<String>();

        if (values == null) {
            return copiedValues;
        }

        for (String value : values) {
            if (value == null) {
                continue;
            }

            String trimmedValue = value.trim();

            if (!trimmedValue.isEmpty()) {
                copiedValues.add(trimmedValue);
            }
        }

        return copiedValues;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + valid +
                ", formatValid=" + formatValid +
                ", cubeCorrect=" + cubeCorrect +
                ", aggregateFunctionCorrect=" + aggregateFunctionCorrect +
                ", measureCorrect=" + measureCorrect +
                ", gammaCorrect=" + gammaCorrect +
                ", sigmaCorrect=" + sigmaCorrect +
                ", unknownFieldsCount=" + unknownFieldsCount +
                ", missingGammaCount=" + missingGammaCount +
                ", extraGammaCount=" + extraGammaCount +
                ", missingSigmaCount=" + missingSigmaCount +
                ", extraSigmaCount=" + extraSigmaCount +
                ", wrongSigmaValueCount=" + wrongSigmaValueCount +
                ", weightedScore=" + weightedScore +
                ", errors=" + errors +
                '}';
    }
}