Feature: Add project
    Description: An employee adds a project
    Actors: Employee

    Scenario: Employee creates a project
        Given there is an employee
        When the employee creates a project with name "Test Project"
        Then there is a project with ID "2020-000001" and name "Test Project"

    Scenario: Employee creates a project with empty name
        Given there is an employee
        When the employee creates a project with name ""
        Then the error message "Invalid name" is given
