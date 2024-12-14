package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class DataMasker {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java DataMasker <input-json-file> <rules-json-file>");
            return;
        }

        String inputFilePath = args[0];
        String rulesFilePath = args[1];

        try {
            // Read the input JSON file and rules file
            String jsonContent = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            String rulesContent = new String(Files.readAllBytes(Paths.get(rulesFilePath)));

            // Parse rules
            Map<String, String> rules = parseRules(rulesContent);

            // Mask the JSON content
            String maskedContent = maskJsonContent(jsonContent, rules);

            // Print the masked content
            System.out.println(maskedContent);
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing JSON: " + e.getMessage());
        }
    }

    // Parse rules from JSON array
    private static Map<String, String> parseRules(String rulesContent) {
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

    private static String maskJsonContent(String jsonContent, Map<String, String> rules) {
        if (jsonContent.trim().startsWith("[")) {
            // Handle JSON array
            JSONArray jsonArray = new JSONArray(jsonContent);
            if (jsonArray.length() == 1) {
                // If there's only one element, return it as a JSONObject
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                applyMasking(jsonObject, rules);
                return jsonObject.toString(); // Return as a single object
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
    // Apply masking to a JSON object
    // Apply masking to a JSON object
    private static void applyMasking(JSONObject jsonObject, Map<String, String> rules) {
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
    private static String maskValue(String value) {
        return "*".repeat(value.length());
    }
}
