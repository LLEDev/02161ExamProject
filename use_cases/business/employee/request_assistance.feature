Feature: Request assistance when working on a project activity

    # Author: Someone
    Scenario: An employee requests assistance
        Given that there is a project with name "Test Project"
        And the following employees are given
            | HLPR |
            | BS   |
        And the employee is attached to all activities in the projects
            | 2020-000001 |
        When the employee requests assistance from "HLPR" on activity with ID "1" in the project
        Then the employee with initials "HLPR" is assigned to the activity with ID "1"

    # Author: Someone
    Scenario: An employee requests assistance when not associated with the any activity
        Given that there is a project with name "Test Project"
        And the following employees are given
            | HLPR |
            | BS   |
        When the employee requests assistance from "HLPR" on activity with ID "1" in the project
        Then the error message "You are not allowed to work with the given project, 2020-000001." is given


    # Author: Someone
    Scenario: An employee requests assistance when not associated with the requested activity
        Given that there is a project with name "Test Project"
        And the following employees are given
            | HLPR |
            | BS   |
            | TED  |
        And the employee with initials "TED" is the project manager of the project
        And the employee adds an activity with name "Test Activity" to the project
        And the actor adds the employee with initials "BS" to the activity with ID "2"
        And the employee with initials "BS" is the actor
        When the employee requests assistance from "HLPR" on activity with ID "1" in the project
        Then the error message "You are not allowed to work with the given activity, 1." is given
