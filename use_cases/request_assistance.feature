Feature: Request assistance when working on a project activity

    Scenario: An employee requests assistance
        Given that there is a project with name "Test Project"
        And the following employees are given
            | HLPR |
            | BS |
        And the employee is attached to all activities in the projects
            | 2020-000001 |
        When the employee requests assistance from "HLPR" on activity with ID "1" in the project
        Then the employee with initials "HLPR" is assigned to the activity with ID "1"

    Scenario: An employee requests assistance when not associated with the requested activity
        Given that there is a project with name "Test Project"
        And the following employees are given
            | HLPR |
            | BS |
        When the employee requests assistance from "HLPR" on activity with ID "1" in the project
        Then the error message "You are not allowed to work with the given project, 2020-000001." is given