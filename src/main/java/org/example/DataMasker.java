package org.example;

import java.nio.file.*;
import java.io.*;
import java.util.*;

public class DataMasker {

    /**
     * Entry point of the application. Reads input JSON and rules files, applies masking, and outputs the result.
     *
     * @param args Command-line arguments. Expects two arguments:
     *             1. Path to the input JSON file to be masked (Example ones in TestData)
     *             2. Path to the rules JSON file containing masking rules (Example ones in TestData)
     */
    public static void main(String[] args) {
        // Ensure exactly two arguments are provided, otherwise display usage information and exit
        if (args.length != 2) {
            System.out.println("Usage: java -jar out/artifacts/JSON4Redgate_jar/JSON4Redgate.jar <TestData/input-json-file> <TestData/rules-json-file>");
            return;
        }

        String inputFilePath = args[0];
        String rulesFilePath = args[1];

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            String rulesContent = new String(Files.readAllBytes(Paths.get(rulesFilePath)));

            // Use the RuleParser class to parse the rules into a map
            RuleParser ruleParser = new RuleParser();
            Map<String, String> rules = ruleParser.parseRules(rulesContent);

            // Use the JsonMasker class to apply the parsed rules to the input JSON content
            JsonMasker jsonMasker = new JsonMasker();
            String maskedContent = jsonMasker.maskJsonContent(jsonContent, rules);

            // Print the masked JSON content to the console
            System.out.println(maskedContent);
        } catch (IOException e) {
            // File reading errors such as missing or inaccessible files
            System.err.println("Error reading files: " + e.getMessage());
        } catch (Exception e) {
            // All other exceptions that may occur during processing
            System.err.println("Error processing JSON: " + e.getMessage());
        }
    }
}
