Feature: Activity view
    Description: A project manager views details about a given activity from a given project
    Actors: Employee

    Scenario: An employee who is a project manager of a project requests an overview of the project
        Given that there is a project with name "Test Project"
        And there is an employee
        And the employee is assigned to the activity with ID "1"
        # TODO: Implement ability to set date on below
        And the activity with ID "1" has an estimated duration of "10" weeks and registered "5" hours spent
        And the employee is the project manager for the project
        When the employee requests a view of activity "1"
        Then the following table is presented
            | Employee | Date | Minutes |
            | AA | 2020-04-25 | 300 |

    Scenario: An employee who is not project manager of a project requests an overview of the project
        Given that there is a project with name "Test Project"
        And there is an employee
        When the employee requests a view of activity "1"
        Then the error message "Project manager role required." is given