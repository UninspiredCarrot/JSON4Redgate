package org.example;

import java.nio.file.*;
import java.io.*;
import java.util.*;

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

            // Parse rules using RuleParser
            RuleParser ruleParser = new RuleParser();
            Map<String, String> rules = ruleParser.parseRules(rulesContent);

            // Mask the JSON content using JsonMasker
            JsonMasker jsonMasker = new JsonMasker();
            String maskedContent = jsonMasker.maskJsonContent(jsonContent, rules);

            // Print the masked content
            System.out.println(maskedContent);
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing JSON: " + e.getMessage());
        }
    }
}
