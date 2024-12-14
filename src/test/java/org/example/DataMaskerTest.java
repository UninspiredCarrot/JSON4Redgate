package org.example;

import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

import static org.example.DataMasker.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataMaskerFileTest {

    private String runDataMasker(String inputFile, String rulesFile) {
        // Capture the output of DataMasker.main()
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream)); // Redirect output

        try {
            main(new String[]{
                    Paths.get("TestData", inputFile).toString(),
                    Paths.get("TestData", rulesFile).toString()
            });
        } finally {
            System.setOut(originalOut); // Restore original output
        }

        return outputStream.toString().trim();
    }

    private boolean jsonEquals(String actual, String expected) {
        // Check if the input strings represent JSON objects or arrays
        if (actual.trim().startsWith("[")) {
            // Handle JSONArray
            JSONArray actualJsonArray = new JSONArray(actual);
            JSONArray expectedJsonArray = new JSONArray(expected);
            return actualJsonArray.similar(expectedJsonArray);
        } else if (actual.trim().startsWith("{")) {
            // Handle JSONObject
            JSONObject actualJsonObject = new JSONObject(actual);
            JSONObject expectedJsonObject = new JSONObject(expected);
            return actualJsonObject.similar(expectedJsonObject);
        }
        return false; // If it is neither an array nor an object, return false
    }

    @Test
    void testPeopleARules() {
        String actualOutput = runDataMasker("people.json", "a.rules.json");
        String expectedOutput = """
            [{"Name":"****","Email":"Jack@example.com"},{"Name":"*******","Email":"Bethany@redgate.com"}]
        """.trim();

        assertTrue(jsonEquals(actualOutput, expectedOutput), "The output JSON for peopleA rules does not match the expected value.");
    }

    @Test
    void testPeopleBRules() {
        String actualOutput = runDataMasker("people.json", "b.rules.json");
        String expectedOutput = """
            [{"Name":"****","Email":"****@example.com"},{"Name":"*******","Email":"*******@redgate.com"}]
        """.trim();

        assertTrue(jsonEquals(actualOutput, expectedOutput), "The output JSON for peopleB rules does not match the expected value.");
    }

    @Test
    void testNutsCRules() {
        String actualOutput = runDataMasker("nuts.json", "c.rules.json");
        String expectedOutput = """
            {"nut":"******","nuts":"pea***, brazil ***"}
        """.trim();

        assertTrue(jsonEquals(actualOutput, expectedOutput), "The output JSON for nutsC rules does not match the expected value.");
    }

    // Add more test cases for edge scenarios, such as empty JSON objects, invalid files, etc.
}
