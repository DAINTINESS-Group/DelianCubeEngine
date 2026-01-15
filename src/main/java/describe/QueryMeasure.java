package describe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class where it's purpose is to decompose a raw measure string from the input Describe query into the following parts
 * the aggregation function, the attribute (or formula) and the alias
 * 
 * It handles the following formats:
 * Standard Aggregation: SUM(amount) -> func="SUM", attr="amount'
 * Derived Measures: "amount - payments" -> func="", atttr="amount - payments"
 * Aliased Measures: "SUM(amount) AS Total -> func="SUM", attr="amount", alias = "Total"
 * @author Nik-Pt
 *
 */
public class QueryMeasure {
    private String function;  
    private String attribute; 
    private String alias;     

    /**
     * Converts "SUM(amount) AS Total" -> function="SUM", attribute="amount", alias="Total"
     */
    public QueryMeasure(String rawString) {
        String workStr = rawString.trim();
        
        //Extract Alias
        if (workStr.toUpperCase().contains(" AS ")) {
            String[] parts = workStr.split(" (?i)AS "); 
            workStr = parts[0].trim();
            this.alias = parts[1].trim().replaceAll("['\"]", ""); 
        } else {
            this.alias = null;
        }
        
        //Parse Function and Attribute
        Pattern funcPattern = Pattern.compile("^([a-zA-Z]+)\\((.*)\\)$");
        Matcher matcher = funcPattern.matcher(workStr);

        if (matcher.find()) {
            this.function = matcher.group(1); //"SUM"
            this.attribute = matcher.group(2); //"amount"
        } 
        else if (workStr.matches(".*[\\+\\-\\*\\/].*")) {
            this.function = ""; 
            this.attribute = workStr; 
        } 
        else {
            this.function = ""; 
            this.attribute = workStr;
        }
    }
    
    public QueryMeasure(String function, String attribute, String alias) {
        this.function = function;
        this.attribute = attribute;
        this.alias = alias;
    }

    public String getFunction() {
    	return function;
    }
    
    public String getAttribute() {
    	return attribute; 
    }
    
    public String getAlias() {
    	return alias; 
    }
    
    public void setFunction(String function) {
        this.function = function;
    }
}