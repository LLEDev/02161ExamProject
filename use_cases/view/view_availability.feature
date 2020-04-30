Feature: Availability view
    Description: A project manager views a list of available employees at a given date
    Actors: Employee

    Scenario: A project manager requests a view of available employees
        Given that there is a project with name "Test project"
        And the project has the following activities
            | First Activity | 200 | 2020-01 | 2020-52 |
            | Second Activity | 200 | 2020-01 | 2020-52 |
            | Third Activity | 200 | 2020-01 | 2020-52 |
            | Fourth Activity | 200 | 2020-01 | 2020-52 |
            | Fifth Activity | 200 | 2020-01 | 2020-52 |
            | Sixth Activity | 200 | 2020-01 | 2020-52 |
            | Seventh Activity | 200 | 2020-01 | 2020-52 |
            | Eighth Activity | 200 | 2020-01 | 2020-52 |
            | Ninth Activity | 200 | 2020-01 | 2020-52 |
            | Tenth Activity | 200 | 2020-01 | 2020-52 |
        And there is an employee with initials "BB"
        And the employee has the following work minutes
            | 2020-000001 | 1 | 2020-04-01 | 0 |
            | 2020-000001 | 2 | 2020-04-01 | 0 |
            | 2020-000001 | 3 | 2020-04-01 | 0 |
            | 2020-000001 | 4 | 2020-04-01 | 0 |
            | 2020-000001 | 5 | 2020-04-01 | 0 |
            | 2020-000001 | 6 | 2020-04-01 | 0 |
            | 2020-000001 | 7 | 2020-04-01 | 0 |
            | 2020-000001 | 8 | 2020-04-01 | 0 |
            | 2020-000001 | 9 | 2020-04-01 | 0 |
            | 2020-000001 | 10 | 2020-04-01 | 0 |
        And there is an employee with initials "CC"
        And the employee has the following work minutes
            | 2020-000001 | 1 | 2020-04-01 | 0 |
            | 2020-000001 | 2 | 2020-04-01 | 0 |
            | 2020-000001 | 3 | 2020-04-01 | 0 |
            | 2020-000001 | 4 | 2020-04-01 | 0 |
            | 2020-000001 | 5 | 2020-04-01 | 0 |
        And there is an employee
        When the employee requests a view of available employees at the date "2020-05-01"
        Then the following table is presented
            | ID | Name | Available activity slots |
            | CC | CC | 5 |
            | AA | AA | 10 |
