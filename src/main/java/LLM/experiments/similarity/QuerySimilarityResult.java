package LLM.experiments.similarity;

public class QuerySimilarityResult {

    private final boolean available;
    private final double overallSimilarity;
    private final double cubeSimilarity;
    private final double aggregateSimilarity;
    private final double measureSimilarity;
    private final double gammaSimilarity;
    private final double sigmaSimilarity;

    public QuerySimilarityResult(
            boolean available,
            double overallSimilarity,
            double cubeSimilarity,
            double aggregateSimilarity,
            double measureSimilarity,
            double gammaSimilarity,
            double sigmaSimilarity) {

        this.available = available;
        this.overallSimilarity = normalizeScore(overallSimilarity);
        this.cubeSimilarity = normalizeScore(cubeSimilarity);
        this.aggregateSimilarity = normalizeScore(aggregateSimilarity);
        this.measureSimilarity = normalizeScore(measureSimilarity);
        this.gammaSimilarity = normalizeScore(gammaSimilarity);
        this.sigmaSimilarity = normalizeScore(sigmaSimilarity);
    }

    public static QuerySimilarityResult unavailable() {
        return new QuerySimilarityResult(
                false,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
        );
    }

    public boolean isAvailable() {
        return available;
    }

    public double getOverallSimilarity() {
        return overallSimilarity;
    }

    public double getCubeSimilarity() {
        return cubeSimilarity;
    }

    public double getAggregateSimilarity() {
        return aggregateSimilarity;
    }

    public double getMeasureSimilarity() {
        return measureSimilarity;
    }

    public double getGammaSimilarity() {
        return gammaSimilarity;
    }

    public double getSigmaSimilarity() {
        return sigmaSimilarity;
    }

    private static double normalizeScore(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return 0.0;
        }

        if (value < 0.0) {
            return 0.0;
        }

        if (value > 1.0) {
            return 1.0;
        }

        return value;
    }

    @Override
    public String toString() {
        return "QuerySimilarityResult{" +
                "available=" + available +
                ", overallSimilarity=" + overallSimilarity +
                ", cubeSimilarity=" + cubeSimilarity +
                ", aggregateSimilarity=" + aggregateSimilarity +
                ", measureSimilarity=" + measureSimilarity +
                ", gammaSimilarity=" + gammaSimilarity +
                ", sigmaSimilarity=" + sigmaSimilarity +
                '}';
    }
}