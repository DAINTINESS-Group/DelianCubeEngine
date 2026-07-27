package intentional.analyze.mqoaggregateadapt;

public interface AggregateAdapter {
	
	public abstract Double adapt(Double oldMeasure, Double newMeasure);

}
