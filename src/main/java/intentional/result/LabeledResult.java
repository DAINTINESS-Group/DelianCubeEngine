package intentional.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.QueryMeasure;
import intentional.labeling.Labeling;
import intentional.labeling.consensus.ConsensusRule;
import intentional.model.ModelOrigin;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import result.Result;

/**
 * A cube query's {@link Result} together with the {@link ModelResult}s produced over it. Each result carries
 * its {@link ModelResult#origin()}, so {@link #labelings()} views the operator's labellings and
 * {@link #archetypeModels()} the model-extraction sweep's results.
 */
public final class LabeledResult {
    public final CubeQuery query;
    public final Result data;
    private final List<ModelResult> models = new ArrayList<>();

    public LabeledResult(CubeQuery query, Result data, List<? extends ModelResult> models) {
        this.query = query;
        this.data = data;
        if (models != null) this.models.addAll(models);
    }

    /** Every model over this result. */
    public List<ModelResult> models() {
        return Collections.unmodifiableList(models);
    }

    /** The models the model-extraction sweep produced. */
    public List<ModelResult> archetypeModels() {
        List<ModelResult> out = new ArrayList<>();
        for (ModelResult model : models) {
            if (model.origin() == ModelOrigin.ARCHETYPE) out.add(model);
        }
        return out;
    }

    /** Appends the models produced over this result. */
    public void addModels(List<? extends ModelResult> produced) {
        if (produced != null) models.addAll(produced);
    }

    /**
     * The display identity of one of this result's labelings: its scheme name, qualified by the labeled
     * parameters of the model that produced it — for a comparison, the benchmark it ran against; for a
     * derived consensus, the labelings it was consensed over.
     */
    public String labelingTag(Labeling labeling) {
        for (ModelResult model : models) {
            if (model.origin() == ModelOrigin.OPERATOR && model.labelling() == labeling) {
                StringBuilder tag = new StringBuilder(labeling.schemeName());
                for (ParameterInstantiation parameter : model.parameters()) {
                    if (parameter.label != null) {
                        tag.append(" vs ").append(parameter.label);
                    }
                }
                return tag.toString();
            }
        }
        List<Labeling> voters = new ArrayList<>();
        for (ModelResult model : models) {
            Labeling produced = model.origin() == ModelOrigin.OPERATOR ? model.labelling() : null;
            if (produced != null && produced.ordered() && produced.domain().equals(labeling.domain())) {
                voters.add(produced);
            }
        }
        if (labeling.ordered() && voters.size() >= 2) {
            StringBuilder tag = new StringBuilder(labeling.schemeName()).append(" over ");
            for (int i = 0; i < voters.size(); i++) {
                if (i > 0) tag.append(" + ");
                tag.append(labelingTag(voters.get(i)));
            }
            return tag.toString();
        }
        return labeling.schemeName();
    }

    /** Every labelling the operator produced, including the consensuses derived over them. */
    public List<Labeling> labelings() {
        List<Labeling> base = new ArrayList<>();
        for (ModelResult model : models) {
            if (model.origin() == ModelOrigin.OPERATOR && model.labelling() != null) {
                base.add(model.labelling());
            }
        }
        List<Labeling> labelings = new ArrayList<>(base);
        labelings.addAll(deriveConsensuses(base));
        return Collections.unmodifiableList(labelings);
    }

    public List<QueryMeasure> measures() {
        return query == null ? Collections.<QueryMeasure>emptyList() : query.getQueryMeasures();
    }

    public String measureName(int measureIndex) {
        List<QueryMeasure> measures = measures();
        return measureIndex >= 0 && measureIndex < measures.size()
                ? measures.get(measureIndex).getName() : null;
    }

    private static List<Labeling> deriveConsensuses(List<Labeling> labelings) {
        Map<List<String>, List<Labeling>> groups = new LinkedHashMap<>();
        for (Labeling labeling : labelings) {
            if (!labeling.ordered()) {
                continue;
            }
            groups.computeIfAbsent(labeling.domain(), domain -> new ArrayList<>()).add(labeling);
        }
        List<Labeling> out = new ArrayList<>();
        for (List<Labeling> group : groups.values()) {
            if (group.size() >= 2) {
                out.add(ConsensusRule.KEMENY.consense(group));
            }
        }
        return out;
    }
}
