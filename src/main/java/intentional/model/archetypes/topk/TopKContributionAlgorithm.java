package intentional.model.archetypes.topk;

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
import intentional.model.Synthema;
import result.Cell;

/**
 * Tests the top-k hypothesis as a marginal contribution: for each breakdown dimension it sums the measure per
 * member, ranks the members by contribution, and labels the top k {@code topContributor}, the rest
 * {@code other}. Each member is an aggregate cell; the per-cell magnitude is the share.
 */
public final class TopKContributionAlgorithm implements Model {

    public static final String NAME = "TopKContribution";
    public static final String TOP_CONTRIBUTOR = "topContributor";
    public static final String OTHER = "other";

    public static final ParameterRole K = new ParameterRole("k", "Number of top contributors to surface", 3);

    @Override public String name() { return NAME; }

    @Override public List<ParameterRole> parameterRoles() {
        return Collections.singletonList(K);
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
        int k = (int) K.defaultValue;
        List<Cell> cells = context.data.getCells();
        double total = 0.0;
        for (Cell c : cells) total += c.toDouble(measureIndex);
        int dimensions = cells.isEmpty() ? 0 : cells.get(0).getDimensionMembers().size();

        Map<Cell, Double> rankByCell = new LinkedHashMap<>();
        Map<Cell, Double> shareByCell = new LinkedHashMap<>();
        double topShare = 0.0;
        for (int d = 0; d < dimensions; d++) {
            Map<String, Double> marginal = new LinkedHashMap<>();
            for (Cell c : cells) {
                marginal.merge(c.getDimensionMembers().get(d), c.toDouble(measureIndex), Double::sum);
            }
            if (marginal.size() < 2) continue;
            List<Map.Entry<String, Double>> ranked = new ArrayList<>(marginal.entrySet());
            ranked.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
            for (int i = 0; i < ranked.size(); i++) {
                Map.Entry<String, Double> member = ranked.get(i);
                double share = total == 0.0 ? 0.0 : member.getValue() / total;
                Cell agg = Cell.aggregate(dimensions, d, member.getKey(), member.getValue());
                rankByCell.put(agg, (double) i);
                shareByCell.put(agg, share);
                if (share > topShare) topShare = share;
            }
        }
        if (rankByCell.isEmpty()) return null;

        Labeling labelling = Labeling.withInheritedMagnitudes(
                scheme(k), rankByCell, 0, shareByCell, Collections.<Cell, Double>emptyMap());
        return new Synthema(NAME, true, labelling,
                Collections.singletonList(ParameterInstantiation.ofDefault(K)))
                .holistic(null, topShare).measure(context.measureName(measureIndex)).metric("k", (double) k);
    }

    private static LabelingScheme scheme(int k) {
        LabelDomain domain = new LabelDomain(Arrays.asList(OTHER, TOP_CONTRIBUTOR), true);
        return new LabelingScheme() {
            @Override public String name() { return NAME; }
            @Override public String applyLabels(double rank) { return rank < k ? TOP_CONTRIBUTOR : OTHER; }
            @Override public LabelDomain domain() { return domain; }
        };
    }
}
