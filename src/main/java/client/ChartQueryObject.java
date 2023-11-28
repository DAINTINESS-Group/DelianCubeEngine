package client;

public class ChartQueryObject
{
	
	private String chart;
	private String query;
	
	
	public ChartQueryObject(String chart, String query)
	{
		this.chart = chart;
		this.query = query;
	}
	
	
	
	public String getChart()
	{
		return chart;
	}
	
	public String getQuery()
	{
		return query;
	}

}
