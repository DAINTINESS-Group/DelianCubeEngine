package intentional.model;

import java.util.List;

/**
 * A named pattern hypothesis, tested by its {@link Model}s.
 */
public final class ArchetypeProperty {
    public final String name;
    public final List<Model> models;

    public ArchetypeProperty(String name, List<Model> models) {
        this.name = name;
        this.models = models;
    }
}
