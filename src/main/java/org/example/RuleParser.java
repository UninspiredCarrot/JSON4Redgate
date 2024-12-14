package org.example;

import org.json.JSONArray;
import java.util.*;

public class RuleParser {

    /**
     * Parses a JSON array of rules into a map of masking rules
     *
     * @param rulesContent The JSON content as a string which represents an array of rules
     *                     Each rule prefixed with either "k:" for key-based rule or "v:" for value-based rules
     * @return A map of parsed rules where keys specify the rule type and values specify the patterns
     *         key-based rules      -> the map keys are prefixed with "key:" and the values are null
     *         value-based rules    -> the map key is "value" and the value contains the regex pattern
     */
    public Map<String, String> parseRules(String rulesContent) {
        JSONArray rulesArray = new JSONArray(rulesContent); // Parse the input string into a JSON array
        Map<String, String> rules = new HashMap<>();

        for (int i = 0; i < rulesArray.length(); i++) {
            String rule = rulesArray.getString(i); // Extract the rule as a string

            if (rule.startsWith("k:")) {
                // Add a key-based rule to the map, with "key:" prefix and null value
                rules.put("key:" + rule.substring(2).trim(), null);
            } else if (rule.startsWith("v:")) {
                // Add a value-based rule to the map with "value" key and extracted pattern as value
                rules.put("value", rule.substring(2).trim());
            } else {
                // Handle invalid rules by logging a warning message
                System.err.println("Found invalid rule: " + rule);
            }
        }

        return rules;
    }
}
