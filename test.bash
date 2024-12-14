#!/bin/bash

INPUT_DIR="TestData"

# Helper function to compare actual output with expected output
compare_output() {
    local actual="$1"
    local expected="$2"
    local test_name="$3"

    # shellcheck disable=SC2155
    local sorted_actual=$(echo "$actual" | jq -S .)
    # shellcheck disable=SC2155
    local sorted_expected=$(echo "$expected" | jq -S .)

    if [ "$sorted_actual" == "$sorted_expected" ]; then
        echo "$test_name PASSED"
    else
        echo "$test_name FAILED"
        echo "Expected:"
        echo "$sorted_expected"
        echo "Actual:"
        echo "$sorted_actual"
    fi
}

# Test 1: People with A rules
echo "Running test: People A rules"
actual_output=$(java -jar out/artifacts/JSON4Redgate_jar/JSON4Redgate.jar "$INPUT_DIR/people.json" "$INPUT_DIR/a.rules.json")
expected_output='[{"Name":"****","Email":"Jack@example.com"},{"Name":"*******","Email":"Bethany@redgate.com"}]'
compare_output "$actual_output" "$expected_output" "People A rules"

# Test 2: People with B rules
echo "Running test: People B rules"
actual_output=$(java -jar out/artifacts/JSON4Redgate_jar/JSON4Redgate.jar "$INPUT_DIR/people.json" "$INPUT_DIR/b.rules.json")
expected_output='[{"Name":"****","Email":"****@example.com"},{"Name":"*******","Email":"*******@redgate.com"}]'
compare_output "$actual_output" "$expected_output" "People B rules"

# Test 3: Nuts with C rules
echo "Running test: Nuts C rules"
actual_output=$(java -jar out/artifacts/JSON4Redgate_jar/JSON4Redgate.jar "$INPUT_DIR/nuts.json" "$INPUT_DIR/c.rules.json")
expected_output='{"nut":"******","nuts":"pea***, brazil ***"}'
compare_output "$actual_output" "$expected_output" "Nuts C rules"

echo "All tests complete"
