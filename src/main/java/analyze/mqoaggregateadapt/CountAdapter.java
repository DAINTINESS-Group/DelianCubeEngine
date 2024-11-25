package analyze.mqoaggregateadapt;

public class CountAdapter implements AggregateAdapter{


	public Double adapt(Double oldMeasure, Double newMeasure) {
		return oldMeasure+1;
	}

}
