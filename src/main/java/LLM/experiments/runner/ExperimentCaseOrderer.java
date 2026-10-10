package LLM.experiments.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import LLM.experiments.LLMExperimentCase;

public final class ExperimentCaseOrderer {

    /*
     * ==========================================================
     * DEFAULT RANDOM SEED
     * ==========================================================
     *
     * Fixed seed makes the experiment order reproducible.
     *
     * The same order can therefore be used for every:
     *
     * - model
     * - prompt combination
     *
     * while still avoiding the original fixed workload order.
     * ==========================================================
     */

    public static final long DEFAULT_SEED = 42L;

    private ExperimentCaseOrderer() {
    }

    public static List<LLMExperimentCase> createPairedOrder(
            List<LLMExperimentCase> experimentCases
    ) {

        return createPairedOrder(
                experimentCases,
                DEFAULT_SEED
        );
    }

    /*
     * ==========================================================
     * CREATE DETERMINISTIC PAIRED ORDER
     * ==========================================================
     *
     * Steps:
     *
     * 1. Validate all experiment cases.
     *
     * 2. Group cases by pairId.
     *
     * 3. Verify that every pair contains exactly:
     *
     *      - one structured case
     *      - one natural case
     *
     * 4. Shuffle the semantic pairs using a fixed seed.
     *
     * 5. Randomize which wording appears first while keeping
     *    the ordering balanced:
     *
     *      approximately half:
     *          structured -> natural
     *
     *      approximately half:
     *          natural -> structured
     *
     * 6. Return a NEW list.
     *
     * The input list is never modified.
     * ==========================================================
     */

    public static List<LLMExperimentCase> createPairedOrder(
            List<LLMExperimentCase> experimentCases,
            long seed
    ) {

        validateInput(
                experimentCases
        );

        Map<String, PairCases> pairsById =
                new HashMap<String, PairCases>();

        Set<String> testIds =
                new HashSet<String>();


        for (LLMExperimentCase experimentCase
                : experimentCases) {

            validateExperimentCase(
                    experimentCase
            );

            String testId =
                    experimentCase.getTestId();

            if (!testIds.add(testId)) {

                throw new IllegalArgumentException(
                        "Duplicate testId found: "
                                + testId
                );
            }

            String pairId =
                    experimentCase.getPairId();

            PairCases pairCases =
                    pairsById.get(
                            pairId
                    );

            if (pairCases == null) {

                pairCases =
                        new PairCases(
                                pairId
                        );

                pairsById.put(
                        pairId,
                        pairCases
                );
            }

            String wordingType =
                    experimentCase.getWordingType();


            if (LLMExperimentCase
                    .WORDING_TYPE_STRUCTURED
                    .equals(wordingType)) {

                if (pairCases.structuredCase != null) {

                    throw new IllegalArgumentException(
                            "Pair '"
                                    + pairId
                                    + "' contains more than one "
                                    + "structured case."
                    );
                }

                pairCases.structuredCase =
                        experimentCase;

            } else if (LLMExperimentCase
                    .WORDING_TYPE_NATURAL
                    .equals(wordingType)) {

                if (pairCases.naturalCase != null) {

                    throw new IllegalArgumentException(
                            "Pair '"
                                    + pairId
                                    + "' contains more than one "
                                    + "natural case."
                    );
                }

                pairCases.naturalCase =
                        experimentCase;

            } else {

                throw new IllegalArgumentException(
                        "Unsupported wordingType '"
                                + wordingType
                                + "' for test '"
                                + testId
                                + "'."
                );
            }
        }

        List<PairCases> pairs =
                new ArrayList<PairCases>(
                        pairsById.values()
                );

        for (PairCases pairCases : pairs) {

            validateCompletePair(
                    pairCases
            );
        }

        int expectedCaseCount =
                pairs.size() * 2;

        if (experimentCases.size()
                != expectedCaseCount) {

            throw new IllegalArgumentException(
                    "Invalid paired workload. "
                            + "Expected "
                            + expectedCaseCount
                            + " cases for "
                            + pairs.size()
                            + " pairs, but found "
                            + experimentCases.size()
                            + "."
            );
        }

        /*
         * ======================================================
         * RANDOM GENERATOR
         * ======================================================
         *
         * One Random instance is used so that the complete
         * ordering is reproducible from the supplied seed.
         * ======================================================
         */

        Random random =
                new Random(
                        seed
                );

        Collections.shuffle(
                pairs,
                random
        );

        /*
         * ======================================================
         * CREATE BALANCED WORDING ORIENTATIONS
         * ======================================================
         *
         * For 15 pairs, for example:
         *
         * 8 pairs may start with structured
         * 7 pairs may start with natural
         *
         * or vice versa.
         *
         * Which orientation receives the extra pair is itself
         * determined by the fixed random generator.
         * ======================================================
         */

        List<Boolean> structuredFirstFlags =
                createBalancedOrientations(
                        pairs.size(),
                        random
                );

        List<LLMExperimentCase> orderedCases =
                new ArrayList<LLMExperimentCase>(
                        experimentCases.size()
                );

        for (int i = 0;
             i < pairs.size();
             i++) {

            PairCases pair =
                    pairs.get(i);

            boolean structuredFirst =
                    structuredFirstFlags.get(i);


            if (structuredFirst) {

                orderedCases.add(
                        pair.structuredCase
                );

                orderedCases.add(
                        pair.naturalCase
                );

            } else {

                orderedCases.add(
                        pair.naturalCase
                );

                orderedCases.add(
                        pair.structuredCase
                );
            }
        }

        return orderedCases;
    }

    private static List<Boolean> createBalancedOrientations(
            int pairCount,
            Random random
    ) {

        List<Boolean> orientations =
                new ArrayList<Boolean>(
                        pairCount
                );


        int structuredFirstCount =
                pairCount / 2;

        int naturalFirstCount =
                pairCount / 2;

        if (pairCount % 2 != 0) {

            boolean extraStructuredFirst =
                    random.nextBoolean();

            if (extraStructuredFirst) {

                structuredFirstCount++;

            } else {

                naturalFirstCount++;
            }
        }

        for (int i = 0;
             i < structuredFirstCount;
             i++) {

            orientations.add(
                    Boolean.TRUE
            );
        }

        for (int i = 0;
             i < naturalFirstCount;
             i++) {

            orientations.add(
                    Boolean.FALSE
            );
        }

        Collections.shuffle(
                orientations,
                random
        );

        return orientations;
    }

    private static void validateInput(
            List<LLMExperimentCase> experimentCases
    ) {

        if (experimentCases == null) {

            throw new IllegalArgumentException(
                    "experimentCases cannot be null."
            );
        }


        if (experimentCases.isEmpty()) {

            throw new IllegalArgumentException(
                    "experimentCases cannot be empty."
            );
        }
    }

    private static void validateExperimentCase(
            LLMExperimentCase experimentCase
    ) {

        if (experimentCase == null) {

            throw new IllegalArgumentException(
                    "experimentCases cannot contain null values."
            );
        }


        validateRequiredString(
                "testId",
                experimentCase.getTestId()
        );


        validateRequiredString(
                "pairId",
                experimentCase.getPairId()
        );


        validateRequiredString(
                "wordingType",
                experimentCase.getWordingType()
        );


        if (experimentCase.getExpectedQuery()
                == null) {

            throw new IllegalArgumentException(
                    "Expected query cannot be null for test '"
                            + experimentCase.getTestId()
                            + "'."
            );
        }
    }

    private static void validateCompletePair(
            PairCases pairCases
    ) {

        if (pairCases == null) {

            throw new IllegalArgumentException(
                    "pairCases cannot be null."
            );
        }


        if (pairCases.structuredCase == null) {

            throw new IllegalArgumentException(
                    "Pair '"
                            + pairCases.pairId
                            + "' does not contain a "
                            + "structured case."
            );
        }


        if (pairCases.naturalCase == null) {

            throw new IllegalArgumentException(
                    "Pair '"
                            + pairCases.pairId
                            + "' does not contain a "
                            + "natural case."
            );
        }

        if (pairCases.structuredCase
                .getExpectedQuery()
                != pairCases.naturalCase
                .getExpectedQuery()) {

            throw new IllegalArgumentException(
                    "Pair '"
                            + pairCases.pairId
                            + "' does not use the same "
                            + "ExpectedCubeQuery object for "
                            + "structured and natural wording."
            );
        }

        String structuredDifficulty =
                safeString(
                        pairCases.structuredCase
                                .getDifficulty()
                );

        String naturalDifficulty =
                safeString(
                        pairCases.naturalCase
                                .getDifficulty()
                );


        if (!structuredDifficulty.equals(
                naturalDifficulty
        )) {

            throw new IllegalArgumentException(
                    "Pair '"
                            + pairCases.pairId
                            + "' has different difficulty values: "
                            + structuredDifficulty
                            + " vs "
                            + naturalDifficulty
                            + "."
            );
        }
    }

    private static void validateRequiredString(
            String fieldName,
            String value
    ) {

        if (value == null
                || value.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    fieldName
                            + " cannot be null or empty."
            );
        }
    }

    private static String safeString(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private static final class PairCases {

        private final String pairId;

        private LLMExperimentCase structuredCase;
        private LLMExperimentCase naturalCase;


        private PairCases(
                String pairId
        ) {

            this.pairId =
                    safeString(
                            pairId
                    );
        }
    }
}