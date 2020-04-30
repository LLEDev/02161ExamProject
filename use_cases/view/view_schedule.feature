Feature: Schedule view
    Description: A project manager views the schedule for a given employee
    Actors: Employee

    Scenario: A project manager views a schedule
        Given that there is a project with name "Test project"
        And the project has the following activities
            | First Activity | 200 | 2020-01 | 2020-52 |
            | Second Activity | 200 | 2020-01 | 2020-52 |
            | Third Activity | 200 | 2020-01 | 2020-52 |
            | Fourth Activity | 200 | 2020-01 | 2020-52 |
            | Fifth Activity | 200 | 2020-01 | 2020-52 |
        And there is an employee with initials "BB"
        And the employee has the following work minutes
            | 2020-000001 | 1 | 2020-04-01 | 10 |
            | 2020-000001 | 2 | 2020-04-01 | 20 |
            | 2020-000001 | 3 | 2020-04-01 | 30 |
            | 2020-000001 | 4 | 2020-04-01 | 40 |
            | 2020-000001 | 5 | 2020-04-01 | 50 |
        And the employee requests the following OOO activities
            | Vacation | 2020-04-01 | 2020-04-14 |
            | Illness | 2020-04-15 | 2020-04-18 |
            | Education | 2020-04-10 | 2020-04-14 |
        And there is an employee
        When the employee requests a view of the schedule of the employee with ID "BB"
        Then the following table is presented
            | ID | Name | Start week | End week | Estimated work hours (in total) | Tracked work hours (in total) |
            | 2 | Second Activity | 2019-01 | 2020-52 | 200 | 1 |
            | 1 | First Activity | 2019-01 | 2020-52 | 200 | 1 |
            | 4 | Fourth Activity | 2019-01 | 2020-52 | 200 | 1 |
            | 3 | Third Activity | 2019-01 | 2020-52 | 200 | 1 |
            | 5 | Fifth Activity | 2019-01 | 2020-52 | 200 | 1 |
        And the following table is presented
            | Type | Start | End |
            | Vacation | 2020-04-01 | 2020-04-14 |
            | Illness | 2020-04-15 | 2020-04-18 |
            | Education | 2020-04-10 | 2020-04-14 |