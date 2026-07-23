package mainengine.managers;

import java.util.Arrays;
import java.util.List;

import report.AnalyzeReportWriter;
import report.AssessReportWriter;
import report.DescribeReportWriter;
import report.ReportWriter;
import highlights.archetypes.DefaultArchetypes;
import highlights.archetypes.megacontributor.MegaContributorArchetype;
import highlights.archetypes.outlier.OutlierArchetype;
import highlights.archetypes.topk.TopKContributorsArchetype;
import highlights.metamodel.ArchetypeProperty;

/**
 * The archetypes to test and the writer to render with for each intentional command.
 */
public enum IntentionalProfile {

    DESCRIBE(DefaultArchetypes.all(), new DescribeReportWriter()),
    ASSESS(DefaultArchetypes.all(), new AssessReportWriter()),
    ANALYZE(Arrays.asList(
            MegaContributorArchetype.create(),
            TopKContributorsArchetype.create(),
            OutlierArchetype.create()), new AnalyzeReportWriter());

    private final List<ArchetypeProperty> archetypes;
    private final ReportWriter writer;

    IntentionalProfile(List<ArchetypeProperty> archetypes, ReportWriter writer) {
        this.archetypes = archetypes;
        this.writer = writer;
    }

    public List<ArchetypeProperty> archetypes() {
        return archetypes;
    }

    public ReportWriter writer() {
        return writer;
    }
}
