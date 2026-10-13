package highlights;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;
import highlights.HighlightRecipes.Recipe;
import highlights.instance.Character;
import highlights.instance.ElementaryHighlight;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import highlights.instance.MeasureValue;
import highlights.metamodel.ElementaryHighlightRole;
import intentional.labeling.Labeling;
import intentional.model.ModelResult;
import intentional.result.LabeledResult;
import result.Cell;

/**
 * Builds a {@link HolisticHighlight} from each {@link ModelResult} of a {@link LabeledResult}, with an
 * {@link ElementaryHighlight} per highlight-worthy label. A result with no recipe bears no highlight.
 */
public final class HighlightExtractor {

    public HighlightSet extract(LabeledResult result, HighlightRecipes recipes, CubeManager cubeManager) {
        List<Highlight> out = new ArrayList<>();
        List<Level> explanators = resolveExplanators(result.query, cubeManager);

        for (ModelResult model : result.models()) {
            Recipe recipe = recipes.forResult(model);
            if (recipe == null) continue;

            Measure mainMeasure = resolveMeasure(model.measureName(), cubeManager);
            HolisticHighlight holistic = new HolisticHighlight(
                    result.data, recipe.displayName, model, mainMeasure, explanators);

            Labeling labelling = model.labelling();
            if (labelling != null) {
                for (Map.Entry<Cell, String> entry : labelling.assignment().entrySet()) {
                    ElementaryHighlightRole role = recipe.roleFor(entry.getValue());
                    if (role == null) continue;
                    Cell cell = entry.getKey();
                    List<Character> characters = charactersOf(cell, explanators);
                    MeasureValue value = new MeasureValue(mainMeasure, cell.toDouble(labelling.measureIndex()));
                    holistic.addElementary(new ElementaryHighlight(
                            result.data, characters, value, role, entry.getValue(), labelling.magnitudeOf(cell)));
                }
            }
            out.add(holistic);
        }
        return new HighlightSet(out);
    }

    private List<Level> resolveExplanators(CubeQuery query, CubeManager cubeManager) {
        List<Level> levels = new ArrayList<>();
        for (String[] gamma : query.getGammaExpressions()) {
            String dimName = gamma[0];
            String levelName = gamma[1];
            Level resolved = null;
            for (Dimension d : cubeManager.getDimensions()) {
                if (d.hasSameName(dimName)) { resolved = d.getLevel(levelName).orElse(null); break; }
            }
            if (resolved == null) { // fall back to the level name, but only when it is unambiguous
                Level unique = null;
                int matches = 0;
                for (Dimension d : cubeManager.getDimensions()) {
                    Optional<Level> o = d.getLevel(levelName);
                    if (o.isPresent()) { unique = o.get(); matches++; }
                }
                if (matches == 1) resolved = unique;
            }
            if (resolved != null) levels.add(resolved);
        }
        return levels;
    }

    /** Resolves the cube Measure for the studied measurement (falls back to the first measure). */
    public Measure resolveMainMeasure(CubeQuery query, CubeManager cubeManager) {
        String attr = query.getQueryMeasures().isEmpty()
                ? null : query.getQueryMeasures().get(0).getName();
        return resolveMeasure(attr, cubeManager);
    }

    /** Resolves the named cube Measure by attribute, falling back to the first measure. */
    public Measure resolveMeasure(String attr, CubeManager cubeManager) {
        List<Measure> measures = cubeManager.getCubes().isEmpty()
                ? new ArrayList<Measure>() : cubeManager.getCubes().get(0).getMeasuresList();
        if (attr != null) {
            for (Measure m : measures) {
                if (attr.equalsIgnoreCase(m.getName())) return m;
            }
        }
        return measures.isEmpty() ? null : measures.get(0);
    }
    
    /** Resolves a cell's bound dimension members into characters, skipping the {@link Cell#ALL} positions. */
    private List<Character> charactersOf(Cell cell, List<Level> explanators) {
        List<Character> characters = new ArrayList<>();
        List<String> members = cell.getDimensionMembers();
        for (int i = 0; i < members.size(); i++) {
            if (Cell.ALL.equals(members.get(i))) continue; // a dimension aggregated over, not a character
            if (i < explanators.size()) {
                characters.add(new Character(explanators.get(i), members.get(i)));
            }
        }
        return characters;
    }
}
