package org.example;

import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

import static org.example.DataMasker.*; // Import the DataMasker class
import static org.junit.jupiter.api.Assertions.assertTrue; // Import assertion methods for testing

/**
 * Test class for verifying the functionality of the DataMasker program with different input files and masking rule configurations
 * */
class DataMaskerFileTest {

    /**
     * Runs DataMasker with the provided input file and rules file, capturing and returning the output
     *
     * @param inputFile  the name of the input file
     * @param rulesFile  the name of the rules file
     * @return the captured output of the DataMasker as a string
     */

    private String runDataMasker(String inputFile, String rulesFile) {
        // Create a ByteArrayOutputStream to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Save the original System.out to restore later
        PrintStream originalOut = System.out;
        // Redirect System.out to our ByteArrayOutputStream
        System.setOut(new PrintStream(outputStream));

        try {
            main(new String[]{
                    Paths.get("TestData", inputFile).toString(),
                    Paths.get("TestData", rulesFile).toString()
            });
        } finally {
            // Restore the original System.out after the test
            System.setOut(originalOut);
        }

        // Return the captured output, trimmed to remove any surrounding whitespace
        return outputStream.toString().trim();
    }

    /**
     * Compares two JSON strings for equality, allowing for some flexibility in structure.
     *
     * @param actual    the actual JSON string produced by the DataMasker
     * @param expected  the expected JSON string to compare against
     * @return true if the two JSON strings represent the same data, false otherwise
     */
    private boolean jsonEquals(String actual, String expected) {
        // Check if the input strings represent JSON arrays
        if (actual.trim().startsWith("[")) {
            JSONArray actualJsonArray = new JSONArray(actual);
            JSONArray expectedJsonArray = new JSONArray(expected);
            return actualJsonArray.similar(expectedJsonArray);
        } else if (actual.trim().startsWith("{")) {
            // Check if the input strings represent JSON objects
            JSONObject actualJsonObject = new JSONObject(actual);
            JSONObject expectedJsonObject = new JSONObject(expected);
            return actualJsonObject.similar(expectedJsonObject);
        }
        // Return false if neither a JSON object nor an array
        return false;
    }

    @Test
    void testPeopleARules() {
        String actualOutput = runDataMasker("people.json", "a.rules.json");
        String expectedOutput = """
            [{"Name":"****","Email":"Jack@example.com"},{"Name":"*******","Email":"Bethany@redgate.com"}]
        """.trim();

        assertTrue(jsonEquals(actualOutput, expectedOutput), "The output JSON for a rules for people does not match the expected value.");
    }

    @Test
    void testPeopleBRules() {
        String actualOutput = runDataMasker("people.json", "b.rules.json");
        String expectedOutput = """
            [{"Name":"****","Email":"****@example.com"},{"Name":"*******","Email":"*******@redgate.com"}]
        """.trim();

        assertTrue(jsonEquals(actualOutput, expectedOutput), "The output JSON for b rules for people does not match the expected value.");
    }

    @Test
    void testNutsCRules() {
        String actualOutput = runDataMasker("nuts.json", "c.rules.json");
        String expectedOutput = """
            {"nut":"******","nuts":"pea***, brazil ***"}
        """.trim();

        assertTrue(jsonEquals(actualOutput, expectedOutput), "The output JSON for c rules with nuts does not match the expected value.");
    }
}
