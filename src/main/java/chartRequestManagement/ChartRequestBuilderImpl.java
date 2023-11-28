package chartRequestManagement;

public class ChartRequestBuilderImpl implements IChartRequestBuilder{

	@Override
	public ChartRequest build(String chart, String query) {
		// TODO Auto-generated method stub
		return new ChartRequest(chart, query);
	}

}
