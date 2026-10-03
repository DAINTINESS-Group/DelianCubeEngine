package LLM.parsing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import LLM.query.ParsedCubeQuery;

public class CubeQueryParseResult {

    private final ParsedCubeQuery parsedQuery;

    /*
     * True when the complete response is syntactically valid JSON.
     */
    private final boolean validJson;

    /*
     * True when the response follows the exact output contract:
     *
     * - exactly the six required fields
     * - correct JSON types
     * - gamma and sigma are arrays of strings
     */
    private final boolean formatValid;
    private final Set<String> fieldNames;
    private final List<String> errors;

    public CubeQueryParseResult(
            ParsedCubeQuery parsedQuery,
            boolean validJson,
            boolean formatValid,
            Set<String> fieldNames,
            List<String> errors
    ) {
        this.parsedQuery =
                parsedQuery == null
                        ? emptyParsedQuery()
                        : parsedQuery;

        this.validJson =
                validJson;

        this.formatValid =
                formatValid;

        this.fieldNames =
                copySet(fieldNames);

        this.errors =
                copyList(errors);
    }

    public ParsedCubeQuery getParsedQuery() {
        return parsedQuery;
    }

    public boolean isValidJson() {
        return validJson;
    }

    public boolean isFormatValid() {
        return formatValid;
    }

    public Set<String> getFieldNames() {
        return Collections.unmodifiableSet(
                fieldNames
        );
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(
                errors
        );
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private static ParsedCubeQuery emptyParsedQuery() {

        return new ParsedCubeQuery(
                "",
                "",
                "",
                "",
                Collections.<String>emptyList(),
                Collections.<String>emptyList()
        );
    }

    private static Set<String> copySet(
            Set<String> values
    ) {
        Set<String> result =
                new HashSet<String>();

        if (values != null) {
            result.addAll(values);
        }

        return result;
    }

    private static List<String> copyList(
            List<String> values
    ) {
        List<String> result =
                new ArrayList<String>();

        if (values != null) {
            result.addAll(values);
        }

        return result;
    }

    @Override
    public String toString() {

        return "CubeQueryParseResult{" +
                "validJson=" + validJson +
                ", formatValid=" + formatValid +
                ", fieldNames=" + fieldNames +
                ", errors=" + errors +
                ", parsedQuery=" + parsedQuery +
                '}';
    }
}