package intentional.model;

/**
 * Whether a {@link ModelResult} was produced by the operator itself or by the model-extraction sweep.
 */
public enum ModelOrigin {
    OPERATOR,
    ARCHETYPE
}
