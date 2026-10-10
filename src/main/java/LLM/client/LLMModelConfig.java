package LLM.client;

public class LLMModelConfig {

    public static final int DEFAULT_SEED = 42;
    public static final double DEFAULT_TEMPERATURE = 0.0;
    public static final int DEFAULT_TOP_K = 1;
    public static final double DEFAULT_TOP_P = 1.0;

    // Kept for compatibility with existing experiment code.
    public static final int EXPERIMENT_SEED = DEFAULT_SEED;
    public static final double EXPERIMENT_TEMPERATURE = DEFAULT_TEMPERATURE;
    public static final int EXPERIMENT_TOP_K = DEFAULT_TOP_K;
    public static final double EXPERIMENT_TOP_P = DEFAULT_TOP_P;

    // Common configuration used by the final experiments.
    private static final int EXPERIMENT_NUM_CTX = 4096;
    private static final int EXPERIMENT_NUM_PREDICT = 256;
    private static final String EXPERIMENT_KEEP_ALIVE = "30m";
    private static final boolean EXPERIMENT_STREAM_ENABLED = true;

    private final String modelName;

    private final int numCtx;
    private final int numPredict;

    private final double temperature;
    private final int seed;
    private final int topK;
    private final double topP;

    private final String keepAlive;
    private final boolean streamEnabled;


    public LLMModelConfig(
            String modelName,
            int numCtx,
            int numPredict,
            double temperature,
            int seed,
            int topK,
            double topP,
            String keepAlive,
            boolean streamEnabled
    ) {

        validate(
                modelName,
                numCtx,
                numPredict,
                temperature,
                topK,
                topP
        );

        this.modelName = safeString(modelName);

        this.numCtx = numCtx;
        this.numPredict = numPredict;

        this.temperature = temperature;
        this.seed = seed;

        this.topK = topK;
        this.topP = topP;

        this.keepAlive = safeString(keepAlive);
        this.streamEnabled = streamEnabled;
    }


    public LLMModelConfig(
            String modelName,
            int numCtx,
            int numPredict,
            double temperature,
            int topK,
            double topP,
            String keepAlive,
            boolean streamEnabled
    ) {

        this(
                modelName,
                numCtx,
                numPredict,
                temperature,
                DEFAULT_SEED,
                topK,
                topP,
                keepAlive,
                streamEnabled
        );
    }


    /*
     * Final experiment configuration.
     * Normally only the model name needs to change.
     */
    public static LLMModelConfig createExperimentConfig(
            String modelName
    ) {

        return new LLMModelConfig(
                modelName,
                EXPERIMENT_NUM_CTX,
                EXPERIMENT_NUM_PREDICT,
                EXPERIMENT_TEMPERATURE,
                EXPERIMENT_SEED,
                EXPERIMENT_TOP_K,
                EXPERIMENT_TOP_P,
                EXPERIMENT_KEEP_ALIVE,
                EXPERIMENT_STREAM_ENABLED
        );
    }


    /*
     * Custom experiment configuration, kept for experiments
     * that need different context/output/streaming settings.
     */
    public static LLMModelConfig createExperimentConfig(
            String modelName,
            int numCtx,
            int numPredict,
            boolean streamEnabled
    ) {

        return new LLMModelConfig(
                modelName,
                numCtx,
                numPredict,
                EXPERIMENT_TEMPERATURE,
                EXPERIMENT_SEED,
                EXPERIMENT_TOP_K,
                EXPERIMENT_TOP_P,
                EXPERIMENT_KEEP_ALIVE,
                streamEnabled
        );
    }


    public static LLMModelConfig createDefault(
            String modelName,
            int numCtx,
            int numPredict,
            boolean streamEnabled
    ) {

        return new LLMModelConfig(
                modelName,
                numCtx,
                numPredict,
                DEFAULT_TEMPERATURE,
                DEFAULT_SEED,
                DEFAULT_TOP_K,
                DEFAULT_TOP_P,
                "30m",
                streamEnabled
        );
    }


    public static LLMModelConfig createDefault(
            String modelName
    ) {

        return createDefault(
                modelName,
                4096,
                128,
                true
        );
    }


    public static LLMModelConfig createFast(
            String modelName
    ) {

        return createDefault(
                modelName,
                4096,
                80,
                true
        );
    }


    public static LLMModelConfig createNonStreamed(
            String modelName
    ) {

        return createDefault(
                modelName,
                4096,
                128,
                false
        );
    }


    public static LLMModelConfig createStreamed(
            String modelName
    ) {

        return createDefault(
                modelName,
                4096,
                128,
                true
        );
    }


    public static LLMModelConfig createCustom(
            String modelName,
            int numCtx,
            int numPredict,
            boolean streamEnabled
    ) {

        return createDefault(
                modelName,
                numCtx,
                numPredict,
                streamEnabled
        );
    }


    public String getModelName() {
        return modelName;
    }


    public int getNumCtx() {
        return numCtx;
    }


    public int getNumPredict() {
        return numPredict;
    }


    public double getTemperature() {
        return temperature;
    }


    public int getSeed() {
        return seed;
    }


    public int getTopK() {
        return topK;
    }


    public double getTopP() {
        return topP;
    }


    public String getKeepAlive() {
        return keepAlive;
    }


    public boolean isStreamEnabled() {
        return streamEnabled;
    }


    public String getSafeFileName() {

        return modelName
                .replace(":", "_")
                .replace("/", "_")
                .replace("\\", "_")
                .replace(" ", "_");
    }


    private static void validate(
            String modelName,
            int numCtx,
            int numPredict,
            double temperature,
            int topK,
            double topP
    ) {

        if (modelName == null
                || modelName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "modelName cannot be null or empty."
            );
        }

        if (numCtx <= 0) {

            throw new IllegalArgumentException(
                    "numCtx must be greater than 0."
            );
        }

        if (numPredict <= 0) {

            throw new IllegalArgumentException(
                    "numPredict must be greater than 0."
            );
        }

        if (temperature < 0.0) {

            throw new IllegalArgumentException(
                    "temperature cannot be negative."
            );
        }

        if (topK < 1) {

            throw new IllegalArgumentException(
                    "topK must be at least 1."
            );
        }

        if (topP <= 0.0
                || topP > 1.0) {

            throw new IllegalArgumentException(
                    "topP must be greater than 0 and less than or equal to 1."
            );
        }
    }


    private static String safeString(
            String value
    ) {

        return value == null
                ? ""
                : value.trim();
    }


    @Override
    public String toString() {

        return "LLMModelConfig{" +
                "modelName='" + modelName + '\'' +
                ", numCtx=" + numCtx +
                ", numPredict=" + numPredict +
                ", temperature=" + temperature +
                ", seed=" + seed +
                ", topK=" + topK +
                ", topP=" + topP +
                ", keepAlive='" + keepAlive + '\'' +
                ", streamEnabled=" + streamEnabled +
                '}';
    }
}