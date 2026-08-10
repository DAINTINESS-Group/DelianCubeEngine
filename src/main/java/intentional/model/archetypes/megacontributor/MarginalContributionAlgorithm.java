package intentional.model.archetypes.megacontributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cubemanager.cubebase.QueryMeasure;
import intentional.labeling.LabelDomain;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingScheme;
import intentional.model.Model;
import intentional.result.LabeledResult;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import intentional.model.ParameterRole;
import intentional.model.ModelResultImpl;
import result.Cell;

/**
 * Tests the mega-contributor hypothesis as a marginal contribution: for each breakdown dimension it sums the
 * measure per member, marginalizing the others, and labels each member {@code contributor} when its share of
 * the total exceeds a threshold, {@code minor} otherwise. Each member is an aggregate cell, {@code ALL} at
 * the marginalized dimensions; the per-cell magnitude is the share.
 */
public final class MarginalContributionAlgorithm implements Model {

    public static final String NAME = "MarginalContribution";
    public static final String CONTRIBUTOR = "contributor";
    public static final String MINOR = "minor";

    public static final ParameterRole DOMINANCE_THRESHOLD =
            new ParameterRole("dominanceThreshold", "Minimum share of the total to dominate", 0.5);

    public static final String CONTRIBUTION_SHARE = "ContributionShare";

    @Override public String name() { return NAME; }

    @Override public List<ParameterRole> parameterRoles() {
        return Collections.singletonList(DOMINANCE_THRESHOLD);
    }

    @Override
    public List<ModelResult> run(LabeledResult context) {
        List<ModelResult> out = new ArrayList<>();
        List<QueryMeasure> measures = context.measures();
        for (int index = 0; index < measures.size(); index++) {
            if (!measures.get(index).getAggregationFunction().additive) continue;
            ModelResult result = runMeasure(context, index);
            if (result != null) out.add(result);
        }
        return out;
    }

    private ModelResult runMeasure(LabeledResult context, int measureIndex) {
        double threshold = DOMINANCE_THRESHOLD.defaultValue;
        List<Cell> cells = context.data.getCells();
        double total = 0.0;
        for (Cell c : cells) total += c.toDouble(measureIndex);
        int dimensions = cells.isEmpty() ? 0 : cells.get(0).getDimensionMembers().size();

        Map<Cell, Double> shareByCell = new LinkedHashMap<>();
        double topShare = 0.0;
        String topMember = null;
        boolean anyContributor = false;
        for (int d = 0; d < dimensions; d++) {
            Map<String, Double> marginal = new LinkedHashMap<>();
            for (Cell c : cells) {
                marginal.merge(c.getDimensionMembers().get(d), c.toDouble(measureIndex), Double::sum);
            }
            if (marginal.size() < 2) continue;
            for (Map.Entry<String, Double> e : marginal.entrySet()) {
                double share = total == 0.0 ? 0.0 : e.getValue() / total;
                shareByCell.put(Cell.aggregate(dimensions, d, e.getKey(), e.getValue()), share);
                if (share > topShare) {
                    topShare = share;
                    topMember = e.getKey();
                }
                if (share > threshold) anyContributor = true;
            }
        }
        if (shareByCell.isEmpty()) return null;

        Labeling labelling = new Labeling(scheme(threshold), shareByCell, 0);
        return new ModelResultImpl(NAME, anyContributor, labelling,
                Collections.singletonList(ParameterInstantiation.ofDefault(DOMINANCE_THRESHOLD)))
                .holistic(topMember, topShare).measure(context.measureName(measureIndex))
                .metric("count", (double) cells.size());
    }

    static LabelingScheme scheme(double threshold, String contributor, String minor) {
        LabelDomain domain = new LabelDomain(Arrays.asList(minor, contributor), true);
        return new LabelingScheme() {
            @Override public String name() { return NAME; }
            @Override public String applyLabels(double share) { return share > threshold ? contributor : minor; }
            @Override public LabelDomain domain() { return domain; }
        };
    }

    private static LabelingScheme scheme(double threshold) {
        return scheme(threshold, CONTRIBUTOR, MINOR);
    }
}
