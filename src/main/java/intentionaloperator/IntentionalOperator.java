package intentionaloperator;

import java.util.List;

/**
 * An intentional operator (ASSESS, DESCRIBE, ANALYZE, ...). Runs a query and returns one
 * {@link OperatorResult} per underlying cube query: a single-element list for ASSESS/DESCRIBE, one
 * entry per base/sibling/drill-down query for ANALYZE.
 */
public interface IntentionalOperator {

    List<OperatorResult> execute(String query);
}
