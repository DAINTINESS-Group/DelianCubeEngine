package LLM.prompts;

public class JsonOutputContract {

    public String getContent() {

        StringBuilder contract = new StringBuilder();

        contract.append("Output contract:\n");
        contract.append("\n");

        contract.append("Return exactly one valid minified JSON object with this structure:\n");
        contract.append("\n");

        contract.append("{\"cubeName\":\"\",\"queryName\":\"\",\"aggregateFunction\":\"\",\"measure\":\"\",\"gamma\":[],\"sigma\":[]}\n");

        contract.append("\n");

        contract.append("Mandatory format rules:\n");
        contract.append("- Return only the JSON object on a single line.\n");
        contract.append("- Do not include unnecessary whitespace or line breaks.\n");
        contract.append("- Do not include text before or after the JSON object.\n");
        contract.append("- Do not use Markdown code fences.\n");
        contract.append("- Do not add, remove, or rename fields.\n");
        contract.append("- Set \"queryName\" to \"\".\n");
        contract.append("- Use double quotes for JSON keys and string values.\n");
        contract.append("- \"gamma\" must always be a JSON array of strings.\n");
        contract.append("- Each gamma entry must use exactly the form dimension.attribute.\n");
        contract.append("- \"sigma\" must always be a JSON array of strings.\n");
        contract.append("- Each sigma entry must use exactly the form dimension.attribute='value'.\n");
        contract.append("- Every sigma entry must include both the attribute and its filter value.\n");
        contract.append("- Never return a sigma entry containing only dimension.attribute without ='value'.\n");
        contract.append("- Example valid sigma: [\"customer_dim.country='USA'\",\"date_dim.the_year='1997'\"]\n");
        contract.append("- Use [] when gamma or sigma is empty.\n");
        contract.append("- The result must be syntactically valid JSON.\n");

        return contract.toString();
    }
}