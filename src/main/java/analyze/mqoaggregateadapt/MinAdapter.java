package analyze.mqoaggregateadapt;

public class MinAdapter implements AggregateAdapter {

	public Double adapt(Double oldMeasure, Double newMeasure) {
		return Double.min(oldMeasure, newMeasure);
	}
	
}
