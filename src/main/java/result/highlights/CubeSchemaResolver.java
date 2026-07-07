package result.highlights;

import result.highlights.instance.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;

/**
 * Resolves a cube query and its result cells into the typed building blocks of a highlight:
 * the studied {@link Measure}, the explanator {@link Level}s, and the {@link Character}s of a cell.
 *
 * This is the operator-agnostic part of producing highlights, shared by every producer (ASSESS,
 * DESCRIBE, ANALYZE, the chart algorithms). It only resolves schema elements; constructing the
 * highlights themselves is left to the producer, which knows the archetype, role and scores.
 * Depends only on the cube schema types, not on the CubeManager orchestrator.
 */
public final class CubeSchemaResolver {
    private final List<Dimension> dimensions;
    private final List<Measure> measures;

    public CubeSchemaResolver(List<Dimension> dimensions, List<Measure> measures) {
        this.dimensions = dimensions;
        this.measures = measures;
    }

    /** Builds a resolver from a CubeManager's current cube schema. */
    public static CubeSchemaResolver from(CubeManager cubeManager) {
        List<Measure> measures = cubeManager.getCubes().isEmpty()
                ? new ArrayList<Measure>() : cubeManager.getCubes().get(0).getMeasuresList();
        return new CubeSchemaResolver(cubeManager.getDimensions(), measures);
    }

    /** Resolves the group-by levels, in gamma order, so they align with each cell's members. */
    public List<Level> resolveExplanators(CubeQuery query) {
        List<Level> levels = new ArrayList<>();
        for (String[] gamma : query.getGammaExpressions()) {
            String dimName = gamma[0];
            String levelName = gamma[1];
            Level resolved = null;
            for (Dimension d : dimensions) {
                if (d.hasSameName(dimName)) { resolved = d.getLevel(levelName).orElse(null); break; }
            }
            if (resolved == null) { // fall back: match the level name across any dimension
                for (Dimension d : dimensions) {
                    Optional<Level> o = d.getLevel(levelName);
                    if (o.isPresent()) { resolved = o.get(); break; }
                }
            }
            if (resolved != null) levels.add(resolved);
        }
        return levels;
    }

    /** Resolves the cube Measure for the studied measurement (falls back to the first measure). */
    public Measure resolveMainMeasure(CubeQuery query) {
        String attr = query.getQueryMeasures().isEmpty()
                ? null : query.getQueryMeasures().get(0).getName();
        return resolveMeasure(attr);
    }

    /** Resolves the named cube Measure by attribute, falling back to the first measure. */
    public Measure resolveMeasure(String attr) {
        if (attr != null) {
            for (Measure m : measures) {
                if (attr.equalsIgnoreCase(m.getName())) return m;
            }
        }
        return measures.isEmpty() ? null : measures.get(0);
    }

    /** Resolves the bound dimension members (by group-by position) into characters. */
    public List<Character> charactersOf(int[] dimensionIndices, String[] members, List<Level> explanators) {
        List<Character> characters = new ArrayList<>();
        for (int k = 0; k < dimensionIndices.length; k++) {
            int idx = dimensionIndices[k];
            if (idx >= 0 && idx < explanators.size()) {
                characters.add(new Character(explanators.get(idx), members[k]));
            }
        }
        return characters;
    }
}
