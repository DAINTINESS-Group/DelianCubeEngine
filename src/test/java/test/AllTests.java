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
	analyze.AnalyzeInputValidationTest.class,
	analyze.AnalyzeTranslationManagerTest.class,
	assess.benchmarks.BenchmarkFactoryTest.class,
	assess.deltas.DeltaSchemeTest.class,
	assess.labelers.CustomLabelingSchemeTest.class,
	assess.AssessQueryBuilderTest.class,
	assess.AssessQueryParserTest.class})
public class AllTests {

}
