# **JSON4Redgate - Data Masker**

## Overview

This project is a **Data Masker** tool for JSON files, which masks sensitive data within JSON objects based on customizable rules. The tool is inspired by Redgate's existing database tool, adapted for JSON data manipulation. It reads a JSON data file and a rules file, applies the masking logic as defined in the rules file, and outputs the masked JSON data.

The tool works via the command line and accepts two arguments:
1. **A JSON data file** to be processed (e.g., `TestData/people.json`).
2. **A JSON rules file** that defines the masking logic using regular expressions (e.g., `a.rules.json`).

## Key Features:
- Supports **key-based and value-based masking** using regular expressions.
- Masks values or keys by replacing matching parts with `*`.
- Provides an easy-to-use **command-line interface**.
- Built using **Java** (IntelliJ project setup).
- Includes automated **unit and integration testing** for robust functionality.

## Setup and Installation

To run the project, follow these steps:

### 1. **Clone the repository**:
```bash
git clone https://github.com/UninspiredCarrot/JSON4Redgate.git
cd JSON4Redgate
```

### 2. **Build the project** using Maven:
This project uses **Maven** for dependency management and building the Java code.

```bash
mvn clean install
```
This will compile the code, run the tests, and generate the `JSON4Redgate.jar` file in the `out/artifacts` directory.

### 3. **Run the tool**:
Once the project is built, you can run the Data Masker tool from the command line by passing two arguments:
- A JSON data file (input)
- A rules file (input)

#### Example:

```bash
java -jar out/artifacts/JSON4Redgate_jar/JSON4Redgate.jar TestData/people.json TestData/a.rules.json
```

This will process the `people.json` file using the rules defined in `a.rules.json`, and output the masked data to the terminal.

### 4. **Input Data Files**:
- **people.json**: Contains sensitive personal data (e.g., names, emails).
- **nuts.json**: A test file containing nut-related information.
- **rules files** (e.g., `a.rules.json`, `b.rules.json`, `c.rules.json`): Contain regex-based rules for masking data.

## Command Line Example

Here’s how you can run the tool with different rules:

```bash
java -jar out/artifacts/JSON4Redgate_jar/JSON4Redgate.jar TestData/people.json TestData/a.rules.json
```

Expected output:
```json
[{"Name":"****","Email":"Jack@example.com"},{"Name":"*******","Email":"Bethany@redgate.com"}]
```

```bash
java -jar out/artifacts/JSON4Redgate_jar/JSON4Redgate.jar TestData/people.json TestData/b.rules.json
```

Expected output:
```json
[{"Name":"****","Email":"****@example.com"},{"Name":"*******","Email":"*******@redgate.com"}]
```

```bash
java -jar out/artifacts/JSON4Redgate_jar/JSON4Redgate.jar TestData/nuts.json TestData/c.rules.json
```

Expected output:
```json
{"nut":"******","nuts":"pea***, brazil ***"}
```

## Project Structure

```
.
├── TestData
│   ├── a.rules.json        # Masking rules for test case A
│   ├── b.rules.json        # Masking rules for test case B
│   ├── c.rules.json        # Masking rules for test case C
│   ├── nuts.json           # Test data for nuts
│   └── people.json         # Test data for people
├── out
│   └── artifacts
│       └── JSON4Redgate_jar
│           └── JSON4Redgate.jar
├── pom.xml                 # Maven build configuration
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org
│   │   │       └── example
│   │   │           ├── DataMasker.java   # Core logic for data masking
│   │   │           ├── JsonMasker.java   # Utility class for JSON masking
│   │   │           └── RuleParser.java   # Rule parsing and processing logic
│   │   └── resources
│   │       └── META-INF
│   │           └── MANIFEST.MF          # Project manifest file
│   └── test
│       └── java
│           └── org
│               └── example
│                   └── DataMaskerTest.java # Unit tests for core components
├── target
│   ├── classes
│   ├── generated-sources
│   ├── generated-test-sources
│   └── test-classes
└── test.bash               # Bash script to automate test execution
```

## Code Explanation

### Key Classes:
- **DataMasker.java**: This class handles the core functionality of processing the input JSON file and applying the rules from the rules file to mask sensitive data. It takes care of reading the JSON files, processing them, and outputting the result.

- **JsonMasker.java**: This utility class provides methods for performing the actual masking logic, including matching keys and values using regex patterns and replacing them with `*` characters.

- **RuleParser.java**: This class parses the rules file (in JSON format) and extracts the key-value pairs that define the regex patterns to apply to the JSON data.

### Test Strategy

- **DataMaskerTest**: Tests for `DataMasker` class using JUnit to ensure correctness. The unit tests are automatically run with `mvn test`.

- **Test Script**: A Bash script (`test.bash`) is provided to run a series of  tests, validating the overall functionality of the tool, ensuring the correct output for the provided test cases.

To execute the Test Script via the Bash script:

```bash
brew install jq
chmod +x ./test.bash
./test.bash
```

## Considerations
- This version of the tool does not support nested objects or arrays. Only flat JSON structures are processed.
- The regex patterns defined in the rules file are applied to keys or values depending on the rule type (`k:` for key-based matching and `v:` for value-based matching).
- The tool replaces matched strings with `*`, maintaining the original length of the data.
