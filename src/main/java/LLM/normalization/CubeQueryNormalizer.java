package LLM.normalization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LLM.query.ParsedCubeQuery;
import LLM.parsing.CubeQueryJsonParser;
import LLM.parsing.CubeQueryParseResult;

public final class CubeQueryNormalizer {

    private CubeQueryNormalizer() {
    }

    public static NormalizationResult normalize(String rawAnswer) {

        List<String> errors =
                new ArrayList<String>();

        if (rawAnswer == null
                || rawAnswer.trim().isEmpty()) {

            errors.add("LLM answer is empty.");

            return new NormalizationResult(
                    false,
                    "",
                    null,
                    errors
            );
        }

        String cleanedAnswer =
                cleanOuterFormatting(rawAnswer);

        ParsedCubeQuery parsedQuery =
                tryParseJson(cleanedAnswer);

        if (parsedQuery == null) {

            parsedQuery =
                    tryParseTextFormat(
                            cleanedAnswer
                    );
        }

        if (parsedQuery == null) {

            errors.add(
                    "Could not recognize query format."
            );

            return new NormalizationResult(
                    false,
                    "",
                    null,
                    errors
            );
        }

        List<String> normalizedGamma =
                normalizeGamma(
                        parsedQuery.getGammaFields()
                );

        List<String> normalizedSigma =
                normalizeSigma(
                        parsedQuery.getSigmaConditions(),
                        errors
                );

        if (!errors.isEmpty()) {

            return new NormalizationResult(
                    false,
                    "",
                    null,
                    errors
            );
        }

        ParsedCubeQuery normalizedQuery =
                new ParsedCubeQuery(
                        cleanSimpleValue(
                                parsedQuery.getCubeName()
                        ),
                        cleanSimpleValue(
                                parsedQuery.getQueryName()
                        ),
                        cleanSimpleValue(
                                parsedQuery.getAggregateFunction()
                        ),
                        cleanSimpleValue(
                                parsedQuery.getMeasure()
                        ),
                        normalizedGamma,
                        normalizedSigma
                );

        String canonicalQuery =
                buildCanonicalQuery(
                        normalizedQuery
                );

        return new NormalizationResult(
                true,
                canonicalQuery,
                normalizedQuery,
                errors
        );
    }

    private static ParsedCubeQuery tryParseJson(
            String answer
    ) {

        String json =
                extractJsonObject(answer);

        if (json == null) {
            return null;
        }

        CubeQueryParseResult parseResult =
                CubeQueryJsonParser.parse(json);

        if (parseResult == null
                || !parseResult.isValidJson()) {

            return null;
        }

        return parseResult.getParsedQuery();
    }

    private static ParsedCubeQuery tryParseTextFormat(
            String answer
    ) {

        Map<String, String> values =
                new HashMap<String, String>();

        String[] lines =
                answer.split("\\r?\\n");

        for (String line : lines) {

            if (line == null) {
                continue;
            }

            String cleanLine =
                    line.trim();

            if (cleanLine.isEmpty()) {
                continue;
            }

            int colonIndex =
                    cleanLine.indexOf(':');

            if (colonIndex == -1) {
                continue;
            }

            String key =
                    normalizeKey(
                            cleanLine.substring(
                                    0,
                                    colonIndex
                            )
                    );

            String value =
                    cleanLine.substring(
                            colonIndex + 1
                    ).trim();

            String canonicalKey =
                    mapKey(key);

            if (canonicalKey != null) {

                values.put(
                        canonicalKey,
                        value
                );
            }
        }

        if (!values.containsKey("cubeName")
                || !values.containsKey("aggregateFunction")
                || !values.containsKey("measure")
                || !values.containsKey("gamma")
                || !values.containsKey("sigma")) {

            return null;
        }

        List<String> gamma =
                parseTextGamma(
                        values.get("gamma")
                );

        List<String> sigma =
                new ArrayList<String>();

        String sigmaValue =
                values.get("sigma");

        if (sigmaValue != null
                && !sigmaValue.trim().isEmpty()) {

            sigma.add(
                    removeArrayBrackets(
                            sigmaValue
                    )
            );
        }

        return new ParsedCubeQuery(
                cleanSimpleValue(
                        values.get("cubeName")
                ),
                cleanSimpleValue(
                        values.get("queryName")
                ),
                cleanSimpleValue(
                        values.get("aggregateFunction")
                ),
                cleanSimpleValue(
                        values.get("measure")
                ),
                gamma,
                sigma
        );
    }

    private static List<String> normalizeGamma(
            List<String> gammaFields
    ) {

        List<String> result =
                new ArrayList<String>();

        if (gammaFields == null) {
            return result;
        }

        for (String gammaField : gammaFields) {

            if (gammaField == null) {
                continue;
            }

            String clean =
                    removeArrayBrackets(
                            gammaField.trim()
                    );

            List<String> parts =
                    splitOutsideQuotes(clean);

            for (String part : parts) {

                String field =
                        removeOuterQuotes(
                                part.trim()
                        )
                                .replaceAll(
                                        "\\s+",
                                        ""
                                );

                if (!field.isEmpty()) {

                    result.add(field);
                }
            }
        }

        return result;
    }

    private static List<String> normalizeSigma(
            List<String> sigmaConditions,
            List<String> errors
    ) {

        List<String> result =
                new ArrayList<String>();

        if (sigmaConditions == null) {
            return result;
        }

        for (String sigmaCondition
                : sigmaConditions) {

            if (sigmaCondition == null) {
                continue;
            }

            String clean =
                    removeArrayBrackets(
                            sigmaCondition.trim()
                    );

            if (clean.isEmpty()) {
                continue;
            }

            clean =
                    repairMergedSigmaQuotes(
                            clean
                    );

            List<String> conditions =
                    splitOutsideQuotes(clean);

            for (String condition
                    : conditions) {

                String normalized =
                        normalizeSingleSigma(
                                condition
                        );

                if (normalized == null) {

                    errors.add(
                            "Invalid sigma condition: "
                                    + condition
                    );

                    continue;
                }

                result.add(normalized);
            }
        }

        return result;
    }

    private static String normalizeSingleSigma(
            String condition
    ) {

        if (condition == null) {
            return null;
        }

        String clean =
                condition.trim();

        while (clean.startsWith("'")
                || clean.startsWith("\"")) {

            String withoutQuote =
                    clean.substring(1).trim();

            if (!withoutQuote.contains("=")) {
                break;
            }

            clean =
                    withoutQuote;
        }

        int equalsIndex =
                clean.indexOf('=');

        if (equalsIndex == -1) {
            return null;
        }

        String field =
                clean.substring(
                                0,
                                equalsIndex
                        )
                        .trim()
                        .replaceAll(
                                "\\s+",
                                ""
                        );

        String value =
                clean.substring(
                        equalsIndex + 1
                ).trim();

        if (field.isEmpty()) {
            return null;
        }

        value =
                removeTrailingSeparator(
                        value
                );

        if ((value.startsWith("'")
                && value.endsWith("'"))
                || (value.startsWith("\"")
                && value.endsWith("\""))) {

            value =
                    value.substring(
                            1,
                            value.length() - 1
                    );
        }

        return field
                + "='"
                + value.trim()
                + "'";
    }

    private static String repairMergedSigmaQuotes(
            String value
    ) {

        String fieldPattern =
                "[A-Za-z_][A-Za-z0-9_]*"
                        + "\\."
                        + "[A-Za-z_][A-Za-z0-9_]*"
                        + "\\s*=";

        String repaired =
                value.replaceAll(
                        "'\\s*,\\s*'(?="
                                + fieldPattern
                                + ")",
                        "',"
                );

        repaired =
                repaired.replaceAll(
                        "\"\\s*,\\s*\"(?="
                                + fieldPattern
                                + ")",
                        "\","
                );

        return repaired;
    }

    private static List<String> splitOutsideQuotes(
            String value
    ) {

        List<String> parts =
                new ArrayList<String>();

        if (value == null
                || value.trim().isEmpty()) {

            return parts;
        }

        StringBuilder current =
                new StringBuilder();

        boolean insideSingleQuote = false;
        boolean insideDoubleQuote = false;
        boolean escaped = false;

        for (int i = 0;
             i < value.length();
             i++) {

            char c =
                    value.charAt(i);

            if (escaped) {

                current.append(c);
                escaped = false;

                continue;
            }

            if (c == '\\') {

                current.append(c);
                escaped = true;

                continue;
            }

            if (c == '\''
                    && !insideDoubleQuote) {

                insideSingleQuote =
                        !insideSingleQuote;

                current.append(c);

                continue;
            }

            if (c == '"'
                    && !insideSingleQuote) {

                insideDoubleQuote =
                        !insideDoubleQuote;

                current.append(c);

                continue;
            }

            if (c == ','
                    && !insideSingleQuote
                    && !insideDoubleQuote) {

                String part =
                        current.toString().trim();

                if (!part.isEmpty()) {
                    parts.add(part);
                }

                current.setLength(0);

                continue;
            }

            current.append(c);
        }

        String last =
                current.toString().trim();

        if (!last.isEmpty()) {
            parts.add(last);
        }

        return parts;
    }

    private static List<String> parseTextGamma(
            String gamma
    ) {

        List<String> result =
                new ArrayList<String>();

        if (gamma == null
                || gamma.trim().isEmpty()) {

            return result;
        }

        String clean =
                removeArrayBrackets(gamma);

        result.add(clean);

        return result;
    }

    private static String buildCanonicalQuery(
            ParsedCubeQuery query
    ) {

        StringBuilder result =
                new StringBuilder();

        result.append("CubeName:")
                .append(
                        cleanSimpleValue(
                                query.getCubeName()
                        )
                )
                .append("\n");

        result.append("Name:")
                .append(
                        cleanSimpleValue(
                                query.getQueryName()
                        )
                )
                .append("\n");

        result.append("AggrFunc:")
                .append(
                        cleanSimpleValue(
                                query.getAggregateFunction()
                        )
                )
                .append("\n");

        result.append("Measure:")
                .append(
                        cleanSimpleValue(
                                query.getMeasure()
                        )
                )
                .append("\n");

        result.append("Gamma:")
                .append(
                        join(
                                query.getGammaFields()
                        )
                )
                .append("\n");

        result.append("Sigma:")
                .append(
                        join(
                                query.getSigmaConditions()
                        )
                );

        return result.toString();
    }

    private static String cleanOuterFormatting(
            String value
    ) {

        String result =
                value.trim();

        result =
                result.replaceAll(
                        "(?im)^\\s*(?:```|''')\\s*json\\s*$",
                        ""
                );

        result =
                result.replaceAll(
                        "(?im)^\\s*(?:```|''')\\s*$",
                        ""
                );

        result =
                result.replaceFirst(
                        "(?is)^\\s*final\\s+answer\\s*:\\s*",
                        ""
                );

        return result.trim();
    }

    private static String extractJsonObject(
            String value
    ) {

        if (value == null) {
            return null;
        }

        int start =
                value.indexOf('{');

        int end =
                value.lastIndexOf('}');

        if (start == -1
                || end == -1
                || end < start) {

            return null;
        }

        return value.substring(
                start,
                end + 1
        ).trim();
    }

    private static String mapKey(
            String key
    ) {

        if ("cubename".equals(key)
                || "cube".equals(key)) {

            return "cubeName";
        }

        if ("name".equals(key)
                || "queryname".equals(key)) {

            return "queryName";
        }

        if ("aggrfunc".equals(key)
                || "aggregatefunction".equals(key)
                || "aggregationfunction".equals(key)
                || "aggregate".equals(key)) {

            return "aggregateFunction";
        }

        if ("measure".equals(key)) {
            return "measure";
        }

        if ("gamma".equals(key)
                || "groupby".equals(key)) {

            return "gamma";
        }

        if ("sigma".equals(key)
                || "filter".equals(key)
                || "filters".equals(key)) {

            return "sigma";
        }

        return null;
    }

    private static String normalizeKey(
            String key
    ) {

        if (key == null) {
            return "";
        }

        return key
                .toLowerCase()
                .replaceAll(
                        "[\\s_\\-]",
                        ""
                );
    }

    private static String cleanSimpleValue(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return removeOuterQuotes(
                value.trim()
        );
    }

    private static String removeOuterQuotes(
            String value
    ) {

        if (value == null) {
            return "";
        }

        String result =
                value.trim();

        if (result.length() >= 2) {

            if ((result.startsWith("\"")
                    && result.endsWith("\""))
                    || (result.startsWith("'")
                    && result.endsWith("'"))) {

                result =
                        result.substring(
                                1,
                                result.length() - 1
                        );
            }
        }

        return result.trim();
    }

    private static String removeArrayBrackets(
            String value
    ) {

        if (value == null) {
            return "";
        }

        String result =
                value.trim();

        if (result.startsWith("[")
                && result.endsWith("]")) {

            result =
                    result.substring(
                            1,
                            result.length() - 1
                    );
        }

        return result.trim();
    }

    private static String removeTrailingSeparator(
            String value
    ) {

        if (value == null) {
            return "";
        }

        String result =
                value.trim();

        while (result.endsWith(",")
                || result.endsWith(";")) {

            result =
                    result.substring(
                            0,
                            result.length() - 1
                    ).trim();
        }

        return result;
    }

    private static String join(
            List<String> values
    ) {

        if (values == null
                || values.isEmpty()) {

            return "";
        }

        StringBuilder result =
                new StringBuilder();

        for (String value : values) {

            if (value == null
                    || value.trim().isEmpty()) {

                continue;
            }

            if (result.length() > 0) {
                result.append(",");
            }

            result.append(
                    value.trim()
            );
        }

        return result.toString();
    }

    public static final class NormalizationResult {

        private final boolean success;
        private final String canonicalQuery;
        private final ParsedCubeQuery parsedQuery;
        private final List<String> errors;

        private NormalizationResult(
                boolean success,
                String canonicalQuery,
                ParsedCubeQuery parsedQuery,
                List<String> errors
        ) {

            this.success = success;

            this.canonicalQuery =
                    canonicalQuery == null
                            ? ""
                            : canonicalQuery;

            this.parsedQuery =
                    parsedQuery;

            this.errors =
                    errors == null
                            ? new ArrayList<String>()
                            : new ArrayList<String>(
                            errors
                    );
        }

        public boolean isSuccess() {
            return success;
        }

        public String getCanonicalQuery() {
            return canonicalQuery;
        }

        public ParsedCubeQuery getParsedQuery() {
            return parsedQuery;
        }

        public List<String> getErrors() {

            return new ArrayList<String>(
                    errors
            );
        }
    }
}