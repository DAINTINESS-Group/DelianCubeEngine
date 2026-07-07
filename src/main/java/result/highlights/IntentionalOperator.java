package result.highlights;

import java.util.List;

import result.highlights.archetypes.DefaultArchetypes;
import result.highlights.metamodel.ArchetypeProperty;

/**
 * An intentional operator (ASSESS, DESCRIBE, ANALYZE, ...). Its product is an {@link OperatorResult}
 * (data + the models it ran). Separately, the operator registers the {@link ArchetypeProperty}s worth
 * testing on that result and the cube context to resolve them. The operator does not build highlights
 * itself — Stage-2 evaluation runs on top of these:
 *
 * <pre>{@code
 * HighlightSet hs = new HighlightExtractor()
 *         .extract(op.toOperatorResult(), op.registeredArchetypes(), op.schemaResolver());
 * }</pre>
 */
public interface IntentionalOperator {

    /** The operator's produced result: data and the models it ran. */
    OperatorResult toOperatorResult();

    /**
     * The archetype properties the operator registers as worth testing on its result. Defaults to the
     * generic {@link DefaultArchetypes#all() data-driven archetypes}; an operator overrides to add its own
     * (e.g. ASSESS appends its benchmark-tendency archetype to the defaults).
     */
    default List<ArchetypeProperty> registeredArchetypes() {
        return DefaultArchetypes.all();
    }
}
