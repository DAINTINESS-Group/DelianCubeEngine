package LLM.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import LLM.query.ParsedCubeQuery;

public final class CubeQueryJsonParser {

    private static final Set<String> REQUIRED_JSON_FIELDS =
            createRequiredJsonFields();

    private CubeQueryJsonParser() {
    }

    public static CubeQueryParseResult parse(String answer) {

        InternalJsonParseResult internalResult =
                new InternalJsonParseResult();

        if (answer == null || answer.trim().isEmpty()) {
            internalResult.addError("Response is null or empty.");
            return buildResult(internalResult);
        }

        String normalizedAnswer = normalizeAnswer(answer);

        if (normalizedAnswer.isEmpty()) {
            internalResult.addError(
                    "Response is empty after normalization."
            );
            return buildResult(internalResult);
        }

        try {
            SimpleJsonParser parser =
                    new SimpleJsonParser(normalizedAnswer);

            parser.parse(internalResult);

        } catch (Exception e) {
            internalResult.addError(
                    e.getClass().getSimpleName()
                            + ": "
                            + safe(e.getMessage())
            );
        }

        return buildResult(internalResult);
    }

    private static CubeQueryParseResult buildResult(
            InternalJsonParseResult internalResult
    ) {

        ParsedCubeQuery parsedQuery =
                buildParsedCubeQuery(internalResult);

        List<String> errors =
                new ArrayList<String>(
                        internalResult.getErrors()
                );

        boolean formatValid =
                validateFormat(
                        internalResult,
                        errors
                );

        return new CubeQueryParseResult(
                parsedQuery,
                internalResult.isValidJson(),
                formatValid,
                internalResult.getFieldNames(),
                errors
        );
    }

    private static ParsedCubeQuery buildParsedCubeQuery(
            InternalJsonParseResult parseResult
    ) {

        if (parseResult == null) {
            return new ParsedCubeQuery(
                    "",
                    "",
                    "",
                    "",
                    new ArrayList<String>(),
                    new ArrayList<String>()
            );
        }

        return new ParsedCubeQuery(
                parseResult.getStringField("cubeName"),
                parseResult.getStringField("queryName"),
                parseResult.getStringField("aggregateFunction"),
                parseResult.getStringField("measure"),
                parseResult.getArrayField("gamma"),
                parseResult.getArrayField("sigma")
        );
    }

    private static boolean validateFormat(
            InternalJsonParseResult parseResult,
            List<String> errors
    ) {

        if (parseResult == null) {
            errors.add("JSON parsing failed.");
            return false;
        }

        if (!parseResult.isValidJson()) {
            return false;
        }

        boolean valid = true;

        Set<String> actualFields =
                parseResult.getFieldNames();

        Set<String> missingFields =
                new HashSet<String>(
                        REQUIRED_JSON_FIELDS
                );
        missingFields.removeAll(actualFields);

        Set<String> extraFields =
                new HashSet<String>(
                        actualFields
                );
        extraFields.removeAll(REQUIRED_JSON_FIELDS);

        if (!missingFields.isEmpty()) {
            valid = false;
            errors.add(
                    "Missing JSON fields: "
                            + missingFields
            );
        }

        if (!extraFields.isEmpty()) {
            valid = false;
            errors.add(
                    "Unexpected JSON fields: "
                            + extraFields
            );
        }

        valid &= validateStringField(
                parseResult,
                "cubeName",
                errors
        );

        valid &= validateStringField(
                parseResult,
                "queryName",
                errors
        );

        valid &= validateStringField(
                parseResult,
                "aggregateFunction",
                errors
        );

        valid &= validateStringField(
                parseResult,
                "measure",
                errors
        );

        valid &= validateArrayField(
                parseResult,
                "gamma",
                errors
        );

        valid &= validateArrayField(
                parseResult,
                "sigma",
                errors
        );

        return valid;
    }

    private static boolean validateStringField(
            InternalJsonParseResult result,
            String field,
            List<String> errors
    ) {

        if (result.hasStringField(field)) {
            return true;
        }

        errors.add(
                field + " must be a JSON string."
        );

        return false;
    }

    private static boolean validateArrayField(
            InternalJsonParseResult result,
            String field,
            List<String> errors
    ) {

        if (result.hasArrayField(field)) {
            return true;
        }

        errors.add(
                field
                        + " must be a JSON array of strings."
        );

        return false;
    }

    private static Set<String> createRequiredJsonFields() {

        Set<String> fields =
                new HashSet<String>();

        fields.add("cubeName");
        fields.add("queryName");
        fields.add("aggregateFunction");
        fields.add("measure");
        fields.add("gamma");
        fields.add("sigma");

        return fields;
    }

    private static class InternalJsonParseResult {

        private boolean validJson;

        private final Map<String, String> stringFields =
                new HashMap<String, String>();

        private final Map<String, List<String>> arrayFields =
                new HashMap<String, List<String>>();

        private final Set<String> fieldNames =
                new HashSet<String>();

        private final List<String> errors =
                new ArrayList<String>();

        public boolean isValidJson() {
            return validJson;
        }

        public void setValidJson(boolean validJson) {
            this.validJson = validJson;
        }

        public void addStringField(
                String key,
                String value
        ) {
            stringFields.put(key, value);
        }

        public void addArrayField(
                String key,
                List<String> value
        ) {
            arrayFields.put(key, value);
        }

        public void addFieldName(String key) {
            fieldNames.add(key);
        }

        public boolean hasStringField(String key) {
            return stringFields.containsKey(key);
        }

        public boolean hasArrayField(String key) {
            return arrayFields.containsKey(key);
        }

        public String getStringField(String key) {
            String value = stringFields.get(key);
            return value == null ? "" : value;
        }

        public List<String> getArrayField(String key) {

            List<String> value =
                    arrayFields.get(key);

            return value == null
                    ? new ArrayList<String>()
                    : new ArrayList<String>(value);
        }

        public Set<String> getFieldNames() {
            return new HashSet<String>(
                    fieldNames
            );
        }

        public void addError(String error) {
            errors.add(safe(error));
        }

        public List<String> getErrors() {
            return new ArrayList<String>(
                    errors
            );
        }
    }

    private static class SimpleJsonParser {

        private final String json;
        private int position;

        public SimpleJsonParser(String json) {
            this.json = json;
            this.position = 0;
        }

        public void parse(
                InternalJsonParseResult result
        ) {

            skipWhitespace();

            if (!consume('{')) {
                result.addError(
                        "JSON object must start with '{'."
                );
                return;
            }

            skipWhitespace();

            if (consume('}')) {
                skipWhitespace();

                if (position != json.length()) {
                    result.addError(
                            "Unexpected text after JSON object."
                    );
                    return;
                }

                result.setValidJson(true);
                return;
            }

            while (position < json.length()) {

                skipWhitespace();

                if (!peek('"')) {
                    result.addError(
                            "Expected JSON field name at position "
                                    + position
                                    + "."
                    );
                    return;
                }

                String key =
                        parseString(result);

                if (key == null) {
                    return;
                }

                if (result
                        .getFieldNames()
                        .contains(key)) {

                    result.addError(
                            "Duplicate JSON field: "
                                    + key
                    );
                    return;
                }

                result.addFieldName(key);

                skipWhitespace();

                if (!consume(':')) {
                    result.addError(
                            "Expected ':' after field "
                                    + key
                                    + "."
                    );
                    return;
                }

                skipWhitespace();

                if (peek('"')) {

                    String value =
                            parseString(result);

                    if (value == null) {
                        return;
                    }

                    result.addStringField(
                            key,
                            value
                    );

                } else if (peek('[')) {

                    List<String> values =
                            parseStringArray(result);

                    if (values == null) {
                        return;
                    }

                    result.addArrayField(
                            key,
                            values
                    );

                } else {

                    result.addError(
                            "Field "
                                    + key
                                    + " must contain either "
                                    + "a JSON string or an array of strings."
                    );
                    return;
                }

                skipWhitespace();

                if (consume(',')) {
                    continue;
                }

                if (consume('}')) {
                    break;
                }

                result.addError(
                        "Expected ',' or '}' at position "
                                + position
                                + "."
                );
                return;
            }

            skipWhitespace();

            if (position != json.length()) {
                result.addError(
                        "Unexpected text after JSON object."
                );
                return;
            }

            result.setValidJson(true);
        }

        private List<String> parseStringArray(
                InternalJsonParseResult result
        ) {

            List<String> values =
                    new ArrayList<String>();

            if (!consume('[')) {
                return null;
            }

            skipWhitespace();

            if (consume(']')) {
                return values;
            }

            while (position < json.length()) {

                skipWhitespace();

                if (!peek('"')) {
                    result.addError(
                            "JSON arrays gamma and sigma "
                                    + "must contain only strings."
                    );
                    return null;
                }

                String value =
                        parseString(result);

                if (value == null) {
                    return null;
                }

                values.add(value);

                skipWhitespace();

                if (consume(',')) {
                    continue;
                }

                if (consume(']')) {
                    return values;
                }

                result.addError(
                        "Expected ',' or ']' inside JSON array."
                );
                return null;
            }

            result.addError(
                    "JSON array was not closed."
            );

            return null;
        }

        private String parseString(
                InternalJsonParseResult result
        ) {

            if (!consume('"')) {
                result.addError(
                        "Expected '\"' at position "
                                + position
                                + "."
                );
                return null;
            }

            StringBuilder value =
                    new StringBuilder();

            while (position < json.length()) {

                char c =
                        json.charAt(position++);

                if (c == '"') {
                    return value.toString();
                }

                if (c != '\\') {
                    value.append(c);
                    continue;
                }

                if (position >= json.length()) {
                    result.addError(
                            "Invalid JSON escape sequence."
                    );
                    return null;
                }

                char escaped =
                        json.charAt(position++);

                switch (escaped) {
                    case '"':
                        value.append('"');
                        break;

                    case '\\':
                        value.append('\\');
                        break;

                    case '/':
                        value.append('/');
                        break;

                    case 'b':
                        value.append('\b');
                        break;

                    case 'f':
                        value.append('\f');
                        break;

                    case 'n':
                        value.append('\n');
                        break;

                    case 'r':
                        value.append('\r');
                        break;

                    case 't':
                        value.append('\t');
                        break;

                    case 'u':

                        Character unicodeCharacter =
                                parseUnicodeCharacter(
                                        result
                                );

                        if (unicodeCharacter == null) {
                            return null;
                        }

                        value.append(
                                unicodeCharacter
                        );
                        break;

                    default:
                        result.addError(
                                "Unsupported JSON escape sequence: \\"
                                        + escaped
                        );
                        return null;
                }
            }

            result.addError(
                    "JSON string was not closed."
            );

            return null;
        }

        private Character parseUnicodeCharacter(
                InternalJsonParseResult result
        ) {

            if (position + 4 > json.length()) {
                result.addError(
                        "Incomplete Unicode escape sequence."
                );
                return null;
            }

            String hex =
                    json.substring(
                            position,
                            position + 4
                    );

            position += 4;

            try {
                int codePoint =
                        Integer.parseInt(
                                hex,
                                16
                        );

                return Character.valueOf(
                        (char) codePoint
                );

            } catch (NumberFormatException e) {
                result.addError(
                        "Invalid Unicode escape sequence: \\u"
                                + hex
                );
                return null;
            }
        }

        private void skipWhitespace() {

            while (position < json.length()
                    && Character.isWhitespace(
                    json.charAt(position)
            )) {
                position++;
            }
        }

        private boolean peek(char expected) {
            return position < json.length()
                    && json.charAt(position) == expected;
        }

        private boolean consume(char expected) {

            if (!peek(expected)) {
                return false;
            }

            position++;
            return true;
        }
    }

    private static String normalizeAnswer(
            String answer
    ) {

        if (answer == null) {
            return "";
        }

        String normalized =
                answer.trim();

        if (!normalized.startsWith("```")) {
            return normalized;
        }

        int firstLineEnd =
                normalized.indexOf('\n');

        if (firstLineEnd < 0) {
            return normalized;
        }

        String openingFence =
                normalized
                        .substring(
                                0,
                                firstLineEnd
                        )
                        .trim();

        boolean supportedFence =
                "```".equals(openingFence)
                        || "```json".equalsIgnoreCase(
                        openingFence
                );

        if (!supportedFence
                || !normalized.endsWith("```")) {

            return normalized;
        }

        return normalized
                .substring(
                        firstLineEnd + 1,
                        normalized.length() - 3
                )
                .trim();
    }

    private static String safe(String value) {
        return value == null
                ? ""
                : value.trim();
    }
}