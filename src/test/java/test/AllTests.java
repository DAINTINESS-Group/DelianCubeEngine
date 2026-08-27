package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({interestingnessengine.historybased.DirectNoveltyTest.class,
	interestingnessengine.historybased.IndirectNoveltyTest.class,
	interestingnessengine.expectedvaluesbased.LabelSurpriseStrictTest.class,
	interestingnessengine.expectedvaluesbased.LabelSurpriseTest.class,
	interestingnessengine.historybased.PartialDetailedExtensionalRelevanceTest.class,
	interestingnessengine.historybased.PartialDetailedExtensionalJaccardBasedPeculiarityTest.class,
	interestingnessengine.expectedvaluesbased.ValueSurpriseTest.class,
	interestingnessengine.expectedvaluesbased.PartialExtensionalValueBasedSurpriseTest.class,
	interestingnessengine.expectedvaluesbased.PartialDetailedExtensionalBeliefBasedNoveltyTest.class,
	interestingnessengine.expectedvaluesbased.PartialSameLevelExtensionalRelevanceTest.class,
	interestingnessengine.historybased.PartialDetailedExtensionalNoveltyTest.class,
	interestingnessengine.FamilyBasedRelevanceTest.class,
	interestingnessengine.historybased.PartialSyntacticAveragePeculiarityTest.class,
	mainengine.nlq.NLTranslatorTest.class,
	mainengine.nlq.SimplifiedNLQueriesTest.class,
	spark.SparkNLQueriesTest.class,
	spark.SparkSQPTest.class,
	mainengine.SessionQueryProcessorEngineTest.class,
	intentional.analyze.AnalyzeHighlightsTest.class,
	intentional.analyze.AnalyzeInputValidationTest.class,
	intentional.analyze.AnalyzeOptimizerTranslationAndExecutionTest.class,
	intentional.analyze.AnalyzeTranslationManagerTest.class,
	intentional.analyze.AnalyzeQueriesExecutionTest.class,
	intentional.analyze.AnalyzeWrongExpressions.class,
	intentional.analyze.AnalyzeUpdatedQueriesTranslationAndExecutionTest.class,
	intentional.assess.fetch.SerialFetcherTest.class,
	intentional.assess.fetch.BatchedFetcherTest.class,
	intentional.assess.fetch.SingleScanFetcherTest.class,
	intentional.labeling.schemes.CustomLabelingSchemeTest.class,
	intentional.assess.AssessQueryParserTest.class,
	intentional.assess.AssessOperatorTest.class,
	intentional.assess.deltas.DeltaSchemeTest.class,
	intentional.assess.utils.DatesHandlerTest.class,
	chartManagement.ChartQueryEditorControllerTest.class,
	chartManagement.VisualizationManagerTest.class,
	intentional.describe.DescribeInputValidationTest.class,
	intentional.describe.DescribeTranslationAndExecutionTest.class})
public class AllTests {

}
