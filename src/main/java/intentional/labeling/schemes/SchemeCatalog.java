package intentional.labeling.schemes;

import java.util.List;

import intentional.labeling.LabelingScheme;

/**
 * Resolves the ready-made schemes a query names in its labeling clause. A name may carry arguments:
 * the bucketing schemes take their ordered labels ({@code EquiDepth(low, high, ultra)}), the clustering
 * scheme its cluster count ({@code KMeansApache(4)}); the rest take none. Unknown names resolve to
 * {@code null} — the caller decides whether that is an error.
 */
public final class SchemeCatalog {

    private SchemeCatalog() {}

    public static LabelingScheme byName(String name) {
        return byName(name, null);
    }

    public static LabelingScheme byName(String name, List<String> args) {
        boolean hasArgs = args != null && !args.isEmpty();
        if (MedianDistanceScheme.NAME.equals(name)) {
            rejectArgs(name, hasArgs);
            return new MedianDistanceScheme();
        }
        if (ZScoreLabelingScheme.NAME.equals(name)) {
            if (!hasArgs) return new ZScoreLabelingScheme();
            if (args.size() != 2) {
                throw new IllegalArgumentException(
                        name + " takes the near and extreme z thresholds, got " + args);
            }
            return new ZScoreLabelingScheme(
                    Double.parseDouble(args.get(0)), Double.parseDouble(args.get(1)));
        }
        if (KMeansScheme.NAME.equals(name)) {
            if (!hasArgs) return new KMeansScheme();
            if (args.size() != 1) {
                throw new IllegalArgumentException(name + " takes a single cluster count, got " + args);
            }
            return new KMeansScheme(Integer.parseInt(args.get(0)));
        }
        if (EquiDepthScheme.NAME.equals(name)) {
            return hasArgs ? new EquiDepthScheme(args) : new EquiDepthScheme();
        }
        if (EquiWidthScheme.NAME.equals(name)) {
            return hasArgs ? new EquiWidthScheme(args) : new EquiWidthScheme();
        }
        return null;
    }

    private static void rejectArgs(String name, boolean hasArgs) {
        if (hasArgs) {
            throw new IllegalArgumentException(name + " takes no arguments");
        }
    }
}
