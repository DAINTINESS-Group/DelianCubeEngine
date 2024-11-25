package analyze.mqoaggregateadapt;

public class SumAdapter implements AggregateAdapter {

	
	public Double adapt(Double oldMeasure, Double newMeasure) {
		return oldMeasure+newMeasure;
	}

}
