package intentionaloperator;

import java.util.List;
import highlights.archetypes.DefaultArchetypes;
import highlights.metamodel.ArchetypeProperty;
import result.ResultFileMetadata;

/**
 * An intentional operator (ASSESS, DESCRIBE, ANALYZE, ...). Its product is an {@link OperatorResult}
 * (data + the models it ran). Separately, the operator registers the {@link ArchetypeProperty}s worth
 * testing on that result and the cube context to resolve them. The operator does not build highlights
 * itself — Stage-2 evaluation runs on top of these:
 * }</pre>
 */
public interface IntentionalOperator {

    /**
     * Stage-1 producer: runs the operator and returns one {@link OperatorResult} per underlying query.
     * Single-query operators (ASSESS, DESCRIBE) return a one-element list; ANALYZE returns one per
     * base/sibling/drill-down query.
     */
    List<OperatorResult> execute(String query);

    /**
     * Legacy file path (RMI): executes, extracts highlights over the result(s), renders the operator's
     * report, and returns the {@link ResultFileMetadata} pointing at it.
     */
    ResultFileMetadata executeToReport(String query);

    /**
     * The archetype properties the operator registers as worth testing on its result. Defaults to the
     * generic {@link DefaultArchetypes#all() data-driven archetypes}; an operator overrides to add its own
     * (e.g. ASSESS appends its benchmark-tendency archetype to the defaults).
     */
    default List<ArchetypeProperty> registeredArchetypes() {
        return DefaultArchetypes.all();
    }
}
