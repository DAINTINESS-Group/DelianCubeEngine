package client;

public class ChartQueryBuilderImpl implements IChartQueryBuilder{

	@Override
	public ChartQueryObject build(String chart, String query) {
		// TODO Auto-generated method stub
		return new ChartQueryObject(chart, query);
	}

}
