package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.util.regex.*;

public class JsonMasker {

    public String maskJsonContent(String jsonContent, Map<String, String> rules) {
        if (jsonContent.trim().startsWith("[")) {
            // Handle JSON array
            JSONArray jsonArray = new JSONArray(jsonContent);
            if (jsonArray.length() == 1) {
                // If there's only one element, return it as a single JSONObject (not an array)
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                applyMasking(jsonObject, rules);
                return jsonObject.toString(); // Return the masked object directly
            } else {
                // Handle the array with multiple elements
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    applyMasking(jsonObject, rules);
                }
                return jsonArray.toString(); // Return as an array
            }
        } else {
            // Handle JSON object
            JSONObject jsonObject = new JSONObject(jsonContent);
            applyMasking(jsonObject, rules);
            return jsonObject.toString();
        }
    }

    // Apply masking to a JSON object
    private void applyMasking(JSONObject jsonObject, Map<String, String> rules) {
        for (String ruleKey : rules.keySet()) {
            String ruleValue = rules.get(ruleKey);

            if (ruleKey.startsWith("key:")) {
                // Key-based masking
                String keyRegex = ruleKey.substring(4);
                Pattern keyPattern = Pattern.compile(keyRegex);
                for (String key : jsonObject.keySet()) {
                    if (keyPattern.matcher(key).matches()) {
                        String value = jsonObject.getString(key);
                        jsonObject.put(key, maskValue(value)); // Mask entire value for matching keys
                    }
                }
            } else if (ruleKey.equals("value")) {
                // Value-based masking using regex
                Pattern valuePattern = Pattern.compile(ruleValue);
                for (String key : jsonObject.keySet()) {
                    String value = jsonObject.getString(key);
                    Matcher matcher = valuePattern.matcher(value);
                    if (matcher.find()) {
                        // Replace only the matching part in the value
                        String maskedValue = matcher.replaceAll(m -> "*".repeat(m.group().length()));
                        jsonObject.put(key, maskedValue);
                    }
                }
            }
        }
    }

    // Mask a value by replacing characters with '*'
    private String maskValue(String value) {
        return "*".repeat(value.length());
    }
}
