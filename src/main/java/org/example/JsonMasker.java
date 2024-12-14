package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.util.regex.*;

public class JsonMasker {

    /**
     * Masks the content of a JSON string based on specified rules
     *
     * @param jsonContent The JSON content as a string which can be either a JSON object or a JSON array
     * @param rules A map of masking rules, where the keys specify the rule type of either "key:<regex>" or "value" and the values specifies the pattern
     * @return The masked JSON content as a string.
     */
    public String maskJsonContent(String jsonContent, Map<String, String> rules) {
        // Check if the JSON content starts with '[' indicating it's a JSON array
        if (jsonContent.trim().startsWith("[")) {
            JSONArray jsonArray = new JSONArray(jsonContent);

            // In case the array contains only one element
            if (jsonArray.length() == 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                applyMasking(jsonObject, rules);
                return jsonObject.toString();
            } else {
                // Iterate over each element in the array when it contains multiple elements
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    applyMasking(jsonObject, rules);
                }
                return jsonArray.toString();
            }
        } else {
            // If the JSON content is not an array, treat it as a JSON object
            JSONObject jsonObject = new JSONObject(jsonContent);
            applyMasking(jsonObject, rules);
            return jsonObject.toString();
        }
    }

    /**
     * Applies the specified masking rules to a JSON object.
     *
     * @param jsonObject The JSON object to which the masking rules will be applied.
     * @param rules A map of masking rules where keys specify the rule type and the values specify patterns.
     */
    private void applyMasking(JSONObject jsonObject, Map<String, String> rules) {
        for (String ruleKey : rules.keySet()) {
            String ruleValue = rules.get(ruleKey);

            if (ruleKey.startsWith("key:")) {
                String keyRegex = ruleKey.substring(4); // Extract the regex pattern from the rule key
                Pattern keyPattern = Pattern.compile(keyRegex);

                for (String key : jsonObject.keySet()) {
                    // Check if the key matches the regex pattern
                    if (keyPattern.matcher(key).matches()) {
                        String value = jsonObject.getString(key);
                        // Mask the value directly by replacing all characters with asterisks
                        jsonObject.put(key, "*".repeat(value.length()));
                    }
                }
            } else if (ruleKey.equals("value")) {
                // Handle masking based on matching values using the regex pattern in ruleValue
                Pattern valuePattern = Pattern.compile(ruleValue);

                for (String key : jsonObject.keySet()) {
                    String value = jsonObject.getString(key);
                    Matcher matcher = valuePattern.matcher(value); // Check if the value matches the pattern

                    if (matcher.find()) {
                        // Replace the matching part in the value with asterisks of equal length
                        String maskedValue = matcher.replaceAll(m -> "*".repeat(m.group().length()));
                        jsonObject.put(key, maskedValue); // Update the JSON object with the masked value
                    }
                }
            }
        }
    }
}
