Feature: CSVReader tests

    # Author: Someone
    Scenario: Run sample set of test data to test "edge" cases
        When a user boots the application with test data files and the following arguments
            | HH |
            | PM |
        Then "Welcome, Hans Hansen" is a part of the output
        And "Available commands:" is a part of the output
        Then the output will be reset