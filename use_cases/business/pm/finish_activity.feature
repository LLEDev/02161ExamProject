Feature: Finish activity
    Description: An activity is finished in the project
    Actors: Employee

    # Author: Someone
    Scenario: Employee who is a project manager finishes activity
        Given that there is a project with name "Test Project"
        And there is an employee
        And the employee is the project manager for the project
        When the employee finishes the activity with ID "1" in the project
        Then the activity with ID "1" is marked as finished in the project

    # Author: Someone
    Scenario: Employee who is not a project manager adds an activity to a project
        Given that there is a project with name "Test Project"
        And there is an employee
        When the employee finishes the activity with ID "1" in the project
        Then the error message "Project manager role required." is given