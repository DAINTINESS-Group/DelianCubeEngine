package intentional.assess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import intentional.labeling.Labeling;
import intentional.labeling.consensus.ConsensusRule;
import intentional.model.Model;
import intentional.model.ModelOrigin;
import intentional.model.ModelResult;
import intentional.model.ModelResultImpl;
import intentional.model.ParameterInstantiation;
import intentional.model.ParameterRole;
import intentional.result.LabeledResult;

/**
 * Derives the consensus over the operator's labelings: every group of two or more sharing an ordered domain
 * is consensed under the rule, and each consensus joins the result as its own {@link ModelResult}, carrying
 * the rule as its parameter.
 */
public final class ConsensusModel implements Model {

    public static final String NAME = "Consensus";

    /** Identifies which rule consensed the labelings. */
    public static final ParameterRole RULE =
            new ParameterRole("rule", "the consensus rule applied over the labelings", 0);

    private final ConsensusRule rule;

    public ConsensusModel(ConsensusRule rule) {
        this.rule = rule;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public LabeledResult run(LabeledResult context) {
        Map<List<String>, List<Labeling>> groups = new LinkedHashMap<>();
        for (ModelResult model : context.models()) {
            Labeling labeling = model.origin() == ModelOrigin.OPERATOR ? model.labelling() : null;
            if (labeling != null && labeling.ordered()) {
                groups.computeIfAbsent(labeling.domain(), domain -> new ArrayList<>()).add(labeling);
            }
        }
        List<ModelResult> produced = new ArrayList<>();
        for (List<Labeling> group : groups.values()) {
            if (group.size() < 2) {
                continue;
            }
            Labeling consensus = rule.consense(group);
            produced.add(new ModelResultImpl(NAME, true, consensus,
                    Collections.singletonList(new ParameterInstantiation(RULE, rule.ordinal(), rule.name())))
                    .origin(ModelOrigin.OPERATOR));
        }
        context.addModels(produced);
        return context;
    }
}
