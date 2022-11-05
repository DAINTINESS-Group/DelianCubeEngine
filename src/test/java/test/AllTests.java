package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({test.interestingness.DirectNoveltyTest.class,
	test.interestingness.IndirectNoveltyTest.class,
	test.interestingness.LabelSurpriseStrictTest.class,
	test.interestingness.LabelSurpriseTest.class,
	test.interestingness.PartialDetailedExtensionalRelevanceTest.class,
	test.interestingness.PartialDetailedExtensionalJaccardBasedPeculiarityTest.class,
	test.interestingness.ValueSurpriseTest.class,
	test.interestingness.PartialExtensionalValueBasedSurpriseTest.class,
	test.interestingness.PartialDetailedExtensionalBeliefBasedNoveltyTest.class,
	test.interestingness.PartialSameLevelExtensionalRelevanceTest.class,
	test.interestingness.PartialDetailedExtensionalNoveltyTest.class,
	test.interestingness.FamilyBasedRelevanceTest.class,
	test.interestingness.PartialSyntacticAveragePeculiarityTest.class,
	test.nlqueries.NLTranslatorTest.class,
	test.nlqueries.SimplifiedNLQueriesTest.class,
	test.spark.SparkNLQueriesTest.class,
	test.spark.SparkSQPTest.class,
	test.SessionQueryProcessorEngineTest.class})
public class AllTests {

}
