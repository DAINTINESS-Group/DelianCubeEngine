package mainengine.managers;

import java.util.List;

import report.AnalyzeReportWriter;
import report.AssessReportWriter;
import report.DescribeReportWriter;
import report.ReportWriter;
import intentional.model.ArchetypeProperty;
import intentional.model.archetypes.DefaultArchetypes;
import intentional.operator.IntentionalOperatorType;

/**
 * The archetypes to test and the writer to render with for an {@link IntentionalOperatorType}. Each profile
 * declares the type it serves, so {@link #forType(IntentionalOperatorType)} is a lookup over that mapping —
 * the type is the single source of truth, the profile only adds the reporting configuration.
 */
public enum IntentionalProfile {

    DESCRIBE(IntentionalOperatorType.DESCRIBE, DefaultArchetypes.all(), new DescribeReportWriter()),
    ASSESS(IntentionalOperatorType.ASSESS, DefaultArchetypes.all(), new AssessReportWriter()),
    ANALYZE(IntentionalOperatorType.ANALYZE,
            DefaultArchetypes.subset("MegaContributor", "TopKContributors", "Outlier"), new AnalyzeReportWriter());

    private final IntentionalOperatorType type;
    private final List<ArchetypeProperty> archetypes;
    private final ReportWriter writer;

    IntentionalProfile(IntentionalOperatorType type, List<ArchetypeProperty> archetypes, ReportWriter writer) {
        this.type = type;
        this.archetypes = archetypes;
        this.writer = writer;
    }

    public List<ArchetypeProperty> archetypes() {
        return archetypes;
    }

    public ReportWriter writer() {
        return writer;
    }

    /** The profile serving the given operator type. */
    public static IntentionalProfile forType(IntentionalOperatorType type) {
        for (IntentionalProfile profile : values()) {
            if (profile.type == type) return profile;
        }
        throw new IllegalArgumentException("No profile for operator type: " + type);
    }
}
