package chartRequestManagement;

import java.io.Serializable;

public class ChartRequest implements Serializable
{
	
	private String chart;
	private String query;
	
	
	public ChartRequest(String chart, String query)
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
