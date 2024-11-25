package analyze.mqoaggregateadapt;

public class MaxAdapter implements AggregateAdapter {

	public Double adapt(Double oldMeasure, Double newMeasure) {
		return Double.max(oldMeasure, newMeasure);
	}

}
