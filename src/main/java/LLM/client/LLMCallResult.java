package LLM.client;

public class LLMCallResult {

    private final boolean success;

    private final String answer;
    private final String rawResponse;


    private final long responseTimeMs;
    private final long promptEvalCount;
    private final long promptEvalCachedCount;

    private final long evalCount;

    private final long totalDurationNs;
    private final long loadDurationNs;
    private final long promptEvalDurationNs;
    private final long evalDurationNs;

    private final String errorType;
    private final String errorMessage;

    public LLMCallResult(
            boolean success,
            String answer,
            String rawResponse,
            long responseTimeMs,
            long promptEvalCount,
            long promptEvalCachedCount,
            long evalCount,
            long totalDurationNs,
            long loadDurationNs,
            long promptEvalDurationNs,
            long evalDurationNs,
            String errorType,
            String errorMessage
    ) {
        this.success = success;

        this.answer = safeString(answer);
        this.rawResponse = safeString(rawResponse);

        this.responseTimeMs =
                Math.max(0L, responseTimeMs);

        this.promptEvalCount =
                promptEvalCount;

        this.promptEvalCachedCount =
                promptEvalCachedCount;

        this.evalCount =
                evalCount;

        this.totalDurationNs =
                totalDurationNs;

        this.loadDurationNs =
                loadDurationNs;

        this.promptEvalDurationNs =
                promptEvalDurationNs;

        this.evalDurationNs =
                evalDurationNs;

        this.errorType =
                safeString(errorType);

        this.errorMessage =
                safeString(errorMessage);
    }

    public static LLMCallResult success(
            String answer,
            String rawResponse,
            long responseTimeMs,
            long promptEvalCount,
            long promptEvalCachedCount,
            long evalCount,
            long totalDurationNs,
            long loadDurationNs,
            long promptEvalDurationNs,
            long evalDurationNs
    ) {
        return new LLMCallResult(
                true,
                answer,
                rawResponse,
                responseTimeMs,
                promptEvalCount,
                promptEvalCachedCount,
                evalCount,
                totalDurationNs,
                loadDurationNs,
                promptEvalDurationNs,
                evalDurationNs,
                "",
                ""
        );
    }

    public static LLMCallResult failure(
            long responseTimeMs,
            String errorType,
            String errorMessage,
            String rawResponse
    ) {
        return new LLMCallResult(
                false,
                "",
                rawResponse,
                responseTimeMs,

                -1L,
                -1L,
                -1L,

                -1L,
                -1L,
                -1L,
                -1L,

                errorType,
                errorMessage
        );
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailed() {
        return !success;
    }

    public String getAnswer() {
        return answer;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long getPromptEvalCount() {
        return promptEvalCount;
    }

    public long getPromptEvalCachedCount() {
        return promptEvalCachedCount;
    }

    public long getEvalCount() {
        return evalCount;
    }

    public boolean hasPromptEvalCount() {
        return promptEvalCount >= 0;
    }

    public boolean hasPromptEvalCachedCount() {
        return promptEvalCachedCount >= 0;
    }

    public boolean hasEvalCount() {
        return evalCount >= 0;
    }

    public long getPromptEvalUncachedCount() {

        if (!hasPromptEvalCount()
                || !hasPromptEvalCachedCount()) {

            return -1L;
        }

        return Math.max(
                0L,
                promptEvalCount
                        - promptEvalCachedCount
        );
    }

    public double getPromptCacheHitRatio() {

        if (!hasPromptEvalCount()
                || !hasPromptEvalCachedCount()
                || promptEvalCount <= 0) {

            return -1.0;
        }

        return promptEvalCachedCount
                / (double) promptEvalCount;
    }

    public long getTotalTokenCount() {

        long safePromptEvalCount =
                Math.max(
                        0L,
                        promptEvalCount
                );

        long safeEvalCount =
                Math.max(
                        0L,
                        evalCount
                );

        return safePromptEvalCount
                + safeEvalCount;
    }

    public long getTotalDurationNs() {
        return totalDurationNs;
    }

    public long getLoadDurationNs() {
        return loadDurationNs;
    }

    public long getPromptEvalDurationNs() {
        return promptEvalDurationNs;
    }

    public long getEvalDurationNs() {
        return evalDurationNs;
    }

    public long getTotalDurationMs() {

        return nanosToMillis(
                totalDurationNs
        );
    }

    public long getLoadDurationMs() {

        return nanosToMillis(
                loadDurationNs
        );
    }

    public long getPromptEvalDurationMs() {

        return nanosToMillis(
                promptEvalDurationNs
        );
    }

    public long getEvalDurationMs() {

        return nanosToMillis(
                evalDurationNs
        );
    }

    public double getPromptTokensPerSecond() {

        if (promptEvalCount <= 0
                || promptEvalDurationNs <= 0) {

            return 0.0;
        }

        return promptEvalCount
                / (
                promptEvalDurationNs
                        / 1000000000.0
        );
    }

    public double getOutputTokensPerSecond() {

        if (evalCount <= 0
                || evalDurationNs <= 0) {

            return 0.0;
        }

        return evalCount
                / (
                evalDurationNs
                        / 1000000000.0
        );
    }

    private static long nanosToMillis(
            long nanos
    ) {

        if (nanos < 0) {
            return -1L;
        }

        return nanos / 1000000L;
    }

    private static String safeString(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    @Override
    public String toString() {

        return "LLMCallResult{" +

                "success=" + success +

                ", responseTimeMs="
                + responseTimeMs +

                ", promptEvalCount="
                + promptEvalCount +

                ", promptEvalCachedCount="
                + promptEvalCachedCount +

                ", promptEvalUncachedCount="
                + getPromptEvalUncachedCount() +

                ", promptCacheHitRatio="
                + getPromptCacheHitRatio() +

                ", evalCount="
                + evalCount +

                ", totalDurationMs="
                + getTotalDurationMs() +

                ", loadDurationMs="
                + getLoadDurationMs() +

                ", promptEvalDurationMs="
                + getPromptEvalDurationMs() +

                ", evalDurationMs="
                + getEvalDurationMs() +

                ", errorType='"
                + errorType
                + '\'' +

                ", errorMessage='"
                + errorMessage
                + '\'' +

                '}';
    }
}