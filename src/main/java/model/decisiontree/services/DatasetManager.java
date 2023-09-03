package model.decisiontree.services;

import static org.apache.spark.sql.functions.expr;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;

import model.decisiontree.config.SparkConfig;
import model.decisiontree.services.DecisionTreeManager;
import model.decisiontree.labeling.RuleSet;
import model.decisiontree.Column;
import model.decisiontree.DatasetProfile;
import model.decisiontree.LabeledColumn;


public class DatasetManager implements IDatasetManager {

	private SparkSession spark;
	private DatasetProfile datasetProfile;
	private Dataset<Row> dataset;
	HashMap<String, Dataset<Row>> datasets;
	
	public DatasetManager() {
		SparkConfig sparkConfig = new SparkConfig();
		this.spark = SparkSession.builder().appName(sparkConfig.getAppName()).master(sparkConfig.getMaster())
				.config("spark.sql.warehouse.dir", sparkConfig.getSparkWarehouse()).getOrCreate();
		this.datasets = new HashMap<>();
		//this.dataFrameReaderFactory = new IDatasetReaderFactory(
		//		SparkSession.builder().appName(sparkConfig.getAppName()).master(sparkConfig.getMaster())
		//				.config("spark.sql.warehouse.dir", sparkConfig.getSparkWarehouse()).getOrCreate());
	}

	@Override
	public void registerDataset(String datasetName, String path) throws AnalysisException {
		Instant start = Instant.now();
		
		try {
			dataset = spark.read()
					.format("csv")
					.option("sep", "\t")
					.option("inferSchema", "true")
					.option("header", "true")
					.load(path);
			
		} 
		catch (Exception e) {
				System.err.println("Error : Couldn't load the query result file into Dataset.\n"
						+ "- Check if all the result files are in the correct format inside the OutputFiles folder.\n"
						+ "- Check if all the result files have header.\n"
						+ "- Check if seperator is set correct.");
		}
		
		datasets.put(datasetName, dataset);
		System.out.println("////////////////////// DECISION TREES ATTEMPT /////////////////////");
		datasets.get(datasetName).printSchema();
		//datasets.get(datasetName).schema()
				
		//TOBECONTINUED FROM HERE
		/*
		List<Column> columns = new ArrayList<>();
		StructField[] fields = dataset.schema().fields();
		for (int i = 0; i < fields.length; ++i) {
			columns.add(new Column(i, fields[i].name(), fields[i].dataType().toString()));
		}
		datasetProfile = new DatasetProfile(datasetName, path, columns);
		System.out.println(String.format("Registered Dataset file with name '%s' at %s", datasetName, path));
		*/
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);
		System.out.println(String.format("Duration of registerDataset: %s / %sms", duration, duration.toMillis()));
		
	}

	@Override
	public void computeLabeledColumn(RuleSet ruleSet) {
		// Add new column to the dataset
		String columnName = ruleSet.getNewColumnName();
		String labelingRulesAsExpression = ruleSet.generateSparkSqlExpression();
		dataset = dataset.withColumn(columnName, expr(labelingRulesAsExpression));

		// Create new LabeledColumn
		int index = (int) dataset.schema().getFieldIndex(columnName).get();
		DataType dataType = dataset.schema().fields()[index].dataType();
		datasetProfile.getColumns()
				.add(new LabeledColumn(datasetProfile.getColumns().size(), columnName, dataType.toString(), ruleSet));
		System.out.println(String.format("Added labeled column: %s", columnName));
	}

	
	
	@Override
	public DatasetProfile computeProfileOfDataset() 
			throws IOException {
				
		extractAllDecisionTrees();		
		return datasetProfile;
	}
	

	private void extractAllDecisionTrees() throws IOException {
		Instant start = Instant.now();
		
		DecisionTreeManager decisionTreeManager = new DecisionTreeManager(dataset, datasetProfile);
		List<String> labeledColumnNames = decisionTreeManager.extractAllDecisionTrees();
		for (String labeledColumnName : labeledColumnNames) {
			System.out.println(String.format("Computed Decision Tree(s) for labeled column: %s", labeledColumnName));
		}
		
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);
		System.out.println(String.format("Duration of extractAllDecisionTrees: %s / %sms", duration, duration.toMillis()));
	}
	
	/*
	private void createOutputFolder(String path) throws IOException {
		if (isInvalidPath(path)) {
			path = new File(datasetProfile.getPath()).getParent();
		} else {
			// TODO: Maybe try and catch, and if exception -> set to default
			Files.createDirectories(Paths.get(path));
		}

		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));
		String outputDirectory = path + File.separator + datasetProfile.getAlias() + "_results_" + currentDateTime;
		Files.createDirectories(Paths.get(outputDirectory));
		datasetProfile.setAuxiliaryDataOutputDirectory(outputDirectory);
	}

	private boolean isInvalidPath(String path) {
		return path == null || path.isEmpty();
	}
	*/
	
}
