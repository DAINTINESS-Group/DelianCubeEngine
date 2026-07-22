package highlights.instance;

import java.util.List;
import java.util.stream.Collectors;

import highlights.metamodel.ElementaryHighlightRole;
import result.Result;

/**
 * A specific fact within a holistic highlight: a combination of contextualizing characters with
 * a measure value, playing a role (peak, mega-contributor, top-k, assessed cell, ...). Labels and
 * deltas are carried as {@link Score}s rather than baked into the role.
 */
public class ElementaryHighlight extends Highlight {
    public final List<Character> characters;
    public final MeasureValue measureValue;
    public final ElementaryHighlightRole role;

    public ElementaryHighlight(Result dataset, List<Character> characters,
                               MeasureValue measureValue, ElementaryHighlightRole role) {
        super(dataset);
        this.characters = characters;
        this.measureValue = measureValue;
        this.role = role;
    }

    @Override
    public String toText() {
        String chars = characters.stream()
                .map(c -> c.type.getName() + "=" + c.id)
                .collect(Collectors.joining(", "));
        String scoreText = scores.stream().map(Score::toString).collect(Collectors.joining(", "));
        return String.format(
                "The combination of characters {%s} with value %s serves as %s with {%s}.",
                chars, measureValue.value, role, scoreText);
    }
}
