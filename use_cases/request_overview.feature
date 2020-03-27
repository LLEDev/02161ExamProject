Feature: Request overview of project
    Description: An employee who is a project manager recieves an overview
    of the time spent on the project and individual activities
    Actors: Employee

    Scenario: An employee who is project manager of a project with a single activity requests an overview of the project
        Given that there is a project with name "Test Project"
        And there is an employee
        And the activity with ID "1" has an estimated duration of "10" hours and registered "5" hours spent
        And the employee is the project manager for the project
        When the employee requests an overview of the project
        Then the employee sees that the project has a single activity with "5" hours spent out of "10" estimated hours needed

    Scenario: An employee assigned to an activity within a project with a single activity requests an overview of the project
        Given that there is a project with name "Test Project"
        And there is an employee
        When the employee requests an overview of the project
        Then the error message "Project manager role required" is given
