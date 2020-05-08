Feature: Submit daily work hours
    Description: An employee (not a project manager) submits which projects
    he/she has worked on (and how long) the given day
    Actors: Employee

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: An employee submits daily work hours
        Given there are projects with names
            | Test Project   |
            | Test Project 2 |
            | Test Project 3 |
        And there is an employee
        And the employee is attached to all activities in the projects
            | 2020-000001 |
            | 2020-000002 |
            | 2020-000003 |
        When the employee submits the work minutes
            | 2020-000001 | 1 | 90 |
            | 2020-000002 | 1 | 90 |
            | 2020-000003 | 1 | 60 |
        Then these activities with overall durations are found
            | 2020-000001 | 1 | 1.5 |
            | 2020-000002 | 1 | 1.5 |
            | 2020-000003 | 1 | 1   |

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: An employee submits work hours to a project he/she is not attached to
        Given that there is a project with name "Test Project"
        And there is an employee
        When the employee submits the work minutes
            | 2020-000001 | 1 | 90 |
        Then the error message "You are not associated with one or more of these projects." is given

    # Author: Thor Dueholm (s194589)
    Scenario: An employee submits work hours under 0
        Given there are projects with names
            | Test Project |
        And there is an employee
        And the employee is attached to all activities in the projects
            | 2020-000001 |
        When the employee submits the work minutes
            | 2020-000001 | 1 | -90 |
        Then the error message "The submitted number of work minutes has to be more than or equal to 0. -90 received." is given