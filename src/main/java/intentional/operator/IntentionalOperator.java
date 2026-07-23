package intentional.operator;

import java.util.List;

import intentional.result.LabeledResult;

/**
 * An intentional operator (ASSESS, DESCRIBE, ANALYZE, ...). Runs a query and returns one
 * {@link LabeledResult} per underlying cube query: a single-element list for ASSESS/DESCRIBE, one
 * entry per base/sibling/drill-down query for ANALYZE.
 */
public interface IntentionalOperator {

    List<LabeledResult> execute(String query);
}
