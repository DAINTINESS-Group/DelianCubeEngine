package chartRequestManagement;

public class ChartRequestBuilderImpl implements IChartRequestBuilder{

	@Override
	public ChartRequest build(String chart, String query) {
		return new ChartRequest(chart, query);
	}

}
