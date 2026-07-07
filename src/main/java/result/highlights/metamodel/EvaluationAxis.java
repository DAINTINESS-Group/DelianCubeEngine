package result.highlights.metamodel;

/**
 * The subject an {@link ArchetypeProperty} is evaluated over. The extractor iterates the matching subjects
 * and tests the archetype once per subject:
 * <ul>
 *   <li>{@link #MEASURE} — once per query measure (mega-contributor, outlier, modality, top-k);</li>
 *   <li>{@link #LABELING} — once per per-cell labeling in the context (label predominance).</li>
 * </ul>
 */
public enum EvaluationAxis {
    MEASURE,
    LABELING
}
