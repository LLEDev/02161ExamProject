Feature: Submit daily work hours
    Description: An employee (not a project manager) submits which projects
    he/she has worked on (and how long) the given day
    Actors: Employee

    Scenario: An employee submits daily work hours
        Given there are projects with names
            | Test Project |
            | Test Project 2 |
            | Test Project 3 |
        And there is an employee
        And the employee is attached to all activities in the projects
            | 2020-000001 |
            | 2020-000002 |
            | 2020-000003 |
        When the employee submits the work minutes (project ID, activity ID, duration)
            | 2020-000001 | 1 | 90 |
            | 2020-000002 | 1 | 90 |
            | 2020-000003 | 1 | 60 |
        Then these activities with overall durations are found
            | 2020-000001 | 1 | 90 |
            | 2020-000002 | 1 | 90 |
            | 2020-000003 | 1 | 60 |

    Scenario: An employee submits work hours to a project he/she is not attached to
        Given there is a project with name "Test Project"
        And there is an employee
        When the employee submits the work minutes (project ID, activity ID, duration)
            | 2020-000001 | 1 | 90 |
        Then the error message "You are not associated with one or more of these projects" is given