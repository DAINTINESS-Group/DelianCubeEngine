package chartRequestManagement;

public class ChartRequestFactory {
	
	
	public IChartRequestBuilder createRequest()
	{
		return new ChartRequestBuilderImpl();
	}

}
