package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({test.interestingness.DirectNoveltyTest.class,
	test.interestingness.IndirectNoveltyTest.class,
	test.interestingness.LabelSurpriseStrictTest.class,
	test.interestingness.LabelSurpriseTest.class,
	test.interestingness.RelevanceWithDAITest.class,
	test.interestingness.ValuePeculiarityTest.class,
	test.interestingness.ValueSurpriseTest.class,
	test.nlqueries.NLTranslatorTest.class,
	test.nlqueries.SimplifiedNLQueriesTest.class,
	test.spark.SparkNLQueriesTest.class,
	test.spark.SparkSQPTest.class,
	test.SessionQueryProcessorEngineTest.class})
public class AllTests {

}
