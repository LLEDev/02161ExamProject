Feature: Add project
    Description: An employee adds a project
    Actors: Employee

    # Author: Someone
    Scenario: Employee creates a project
        Given there is an employee
        When the employee creates a project with name "Test Project"
        Then there is a project with ID "2020-000001" and name "Test Project"

    # Author: Someone
    Scenario: Employee creates a project with empty name
        Given there is an employee
        When the employee creates a project with name ""
        Then the error message "The given project name, , is not valid." is given
