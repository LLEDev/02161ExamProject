Feature: Project view
    Description: A project manager views details about a given project
    Actors: Employee

    Scenario: An employee who is a project manager of a project requests an overview of the project
         Given that there is a project with name "Test Project"
         And there is an employee
         And there is an activity with ID "1"
         And the employee is assigned to the activity with ID "1"
         And the activity with ID "1" has an estimated duration of "10" weeks and registered "5" hours spent
         And the employee is the project manager for the project
         When the employee requests a view of the project
         Then the following table is presented
              | ID | Name | Start week | End week | Estimated work hours (in total) | Tracked work hours (in total) |
              | 1  | First Activity | 2020-17 | 2020-27 | 0 | 5 |

    Scenario: An employee who is not project manager of a project requests an overview of the project
         Given that there is a project with name "Test Project"
         And there is an employee
         When the employee requests a view of the project
         Then the error message "Project manager role required." is given