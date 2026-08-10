package highlights.instance;

import java.util.List;
import java.util.stream.Collectors;

import highlights.metamodel.ElementaryHighlightRole;
import result.Result;

/**
 * A specific fact within a holistic highlight: contextualizing characters with a measure value, playing a
 * role, carrying the model's per-cell magnitude.
 */
public class ElementaryHighlight extends Highlight {
    public final List<Character> characters;
    public final MeasureValue measureValue;
    public final ElementaryHighlightRole role;
    public final String label;
    public final double magnitude;

    public ElementaryHighlight(Result dataset, List<Character> characters, MeasureValue measureValue,
                               ElementaryHighlightRole role, String label, double magnitude) {
        super(dataset);
        this.characters = characters;
        this.measureValue = measureValue;
        this.role = role;
        this.label = label;
        this.magnitude = magnitude;
    }

    @Override
    public String toText() {
        String chars = characters.stream()
                .map(c -> c.type.getName() + "=" + c.id)
                .collect(Collectors.joining(", "));
        return String.format("The combination of characters {%s} with value %s serves as %s.",
                chars, measureValue.value, role);
    }
}
