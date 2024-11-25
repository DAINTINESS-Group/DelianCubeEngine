package analyze.mqoaggregateadapt;

public class AggregateAdapterFactory {
	
	public AggregateAdapter createAdapter(String aggrFunc) {
		
		if(aggrFunc.equals("sum") || aggrFunc.equals("Sum") || aggrFunc.equals("SUM")) {
			return new SumAdapter();
		}else if(aggrFunc.equals("count") || aggrFunc.equals("Count") || aggrFunc.equals("COUNT")) {
			return new CountAdapter();
		}else if(aggrFunc.equals("max") || aggrFunc.equals("Max") || aggrFunc.equals("MAX")) {
			return new MaxAdapter();
		}else if(aggrFunc.equals("min") || aggrFunc.equals("Min") || aggrFunc.equals("MIN")) {
			return new MinAdapter();
		}else {
			return null;
		}
	}
	
}
