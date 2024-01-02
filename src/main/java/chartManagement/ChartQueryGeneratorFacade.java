package chartManagement;

public class ChartQueryGeneratorFacade 
{
	
	IChartQueryNModelGenerator createChartQueryGenerator(String type)
	{
		if(type.equals("Barchart"))
		{
			return new BarchartQueryGenerator();
		}else if(type.equals("Scatterplot")) {
			return new ScatterplotQueryGenerator();
			
		}else if(type.equals("Linechart")) {
			return new LinechartQueryGenerator();
		}else {
			return null;
		}
	}

}
