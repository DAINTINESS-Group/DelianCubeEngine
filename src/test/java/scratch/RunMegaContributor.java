package scratch;

import result.highlights.OperatorResult;
import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.instance.ArchetypeResult;
import result.highlights.instance.ScoredFinding;
import result.highlights.archetypes.MegaContributorArchetype;

import java.util.ArrayList;

import result.Result;
import java.util.Arrays;


public class RunMegaContributor {

    public static void main(String[] args) {
        Result data = new Result();
        data.setNumMeasures(1);
        data.addPair(new String[]{"Athens", "700", "1"});
        data.addPair(new String[]{"Thessaloniki", "120", "1"});
        data.addPair(new String[]{"Patras", "90", "1"});
        data.addPair(new String[]{"Volos", "40", "1"});

        OperatorResult op = new OperatorResult(null, data, new ArrayList<>());
        ArchetypeProperty mc = MegaContributorArchetype.create();
        Algorithm algorithm = mc.candidateAlgorithms.get(0);

        System.out.println("archetype  = " + mc.name);
        System.out.println("appliesTo  = " + algorithm.appliesTo(op));
        ArchetypeResult r = algorithm.run(op);
        System.out.println("algorithm  = " + r.execution.name);
        System.out.println("params     = " + r.execution.params.get("dominanceThreshold", -1));
        System.out.println("holds      = " + r.execution.result.verdict);
        System.out.println("metrics    = " + r.execution.result.auxiliaryMetrics);
        System.out.println("holisticScores = " + r.holisticScores);
        System.out.println("elementary (mega-contributors):");
        for (ScoredFinding sf : r.elementary) {
            System.out.println("  members=" + Arrays.toString(sf.members)
                    + "   value=" + sf.value + "   role=" + sf.role + "   " + sf.scores);
        }
    }
}
