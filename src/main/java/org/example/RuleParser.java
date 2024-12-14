package org.example;

import org.json.JSONArray;
import java.util.*;

public class RuleParser {

    // Parse rules from JSON array
    public Map<String, String> parseRules(String rulesContent) {
        JSONArray rulesArray = new JSONArray(rulesContent);
        Map<String, String> rules = new HashMap<>();
        for (int i = 0; i < rulesArray.length(); i++) {
            String rule = rulesArray.getString(i);
            if (rule.startsWith("k:")) {
                rules.put("key:" + rule.substring(2).trim(), null); // Key-based masking
            } else if (rule.startsWith("v:")) {
                rules.put("value", rule.substring(2).trim()); // Value-based masking
            } else {
                System.err.println("Skipping invalid rule: " + rule);
            }
        }
        return rules;
    }
}
