package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({test.interestingnessengine.historybased.DirectNoveltyTest.class,
	test.interestingnessengine.historybased.IndirectNoveltyTest.class,
	test.interestingnessengine.expectedvaluesbased.LabelSurpriseStrictTest.class,
	test.interestingnessengine.expectedvaluesbased.LabelSurpriseTest.class,
	test.interestingnessengine.historybased.PartialDetailedExtensionalRelevanceTest.class,
	test.interestingnessengine.historybased.PartialDetailedExtensionalJaccardBasedPeculiarityTest.class,
	test.interestingnessengine.expectedvaluesbased.ValueSurpriseTest.class,
	test.interestingnessengine.expectedvaluesbased.PartialExtensionalValueBasedSurpriseTest.class,
	test.interestingnessengine.expectedvaluesbased.PartialDetailedExtensionalBeliefBasedNoveltyTest.class,
	test.interestingnessengine.expectedvaluesbased.PartialSameLevelExtensionalRelevanceTest.class,
	test.interestingnessengine.historybased.PartialDetailedExtensionalNoveltyTest.class,
	test.interestingnessengine.FamilyBasedRelevanceTest.class,
	test.interestingnessengine.historybased.PartialSyntacticAveragePeculiarityTest.class,
	test.mainengine.nlq.NLTranslatorTest.class,
	test.mainengine.nlq.SimplifiedNLQueriesTest.class,
	test.spark.SparkNLQueriesTest.class,
	test.spark.SparkSQPTest.class,
	test.mainengine.SessionQueryProcessorEngineTest.class,
	test.analyzeOperator.AnalyzeInputValidationTest.class,
	test.analyzeOperator.AnalyzeTranslationManagerTest.class})
public class AllTests {

}
