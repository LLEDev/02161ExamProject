Feature: Add activity
    Description: An activity is added to the project
    Actors: Employee

    # Author: Someone
    Scenario: Employee who is a project manager adds an activity to a project
        Given that there is a project with name "Test Project"
        And there is an employee
        And the employee is the project manager for the project
        When the employee adds an activity with name "Test Activity" to the project
        Then the project contains an activity with ID "2"

    # Author: Someone
    Scenario: Employee who is not a project manager adds an activity to a project
        Given that there is a project with name "Test Project"
        And there is an employee
        When the employee adds an activity with name "Test Activity" to the project
        Then the error message "Project manager role required." is given
