Feature: Test all "view X" commands

    # Author: Johannes S. Busse (s194604)
    Scenario: An employee who is a project manager of a project requests an overview of the project
        Given that there is a project with name "Test Project"
        And there is an employee
        And the employee is assigned to the activity with ID "1"
        And the activity with ID "1" has an estimated duration of "10" weeks and registered "5" hours spent on date "2020-04-25"
        And the employee is the project manager for the project
        When the employee requests a view of activity "1"
        Then the following table is presented
            | Employee | Date       | Minutes |
            | AA       | 2020-04-25 | 300     |

    # Author: Johannes S. Busse (s194604)
    Scenario: An employee who is not project manager of a project requests an overview of the project
        Given that there is a project with name "Test Project"
        And there is an employee
        When the employee requests a view of activity "1"
        Then the error message "Project manager role required." is given

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A project manager requests a view of available employees
        Given that there is a project with name "Test project"
        And the project has the following activities
            | First Activity   | 200 | 2020-01 | 2020-52 |
            | Second Activity  | 200 | 2020-01 | 2020-52 |
            | Third Activity   | 200 | 2020-01 | 2020-52 |
            | Fourth Activity  | 200 | 2020-01 | 2020-52 |
            | Fifth Activity   | 200 | 2020-01 | 2020-52 |
            | Sixth Activity   | 200 | 2020-01 | 2020-52 |
            | Seventh Activity | 200 | 2020-01 | 2020-52 |
            | Eighth Activity  | 200 | 2020-01 | 2020-52 |
            | Ninth Activity   | 200 | 2020-01 | 2020-52 |
            | Tenth Activity   | 200 | 2020-01 | 2020-52 |
        And there is an employee with initials "BB"
        And the employee has the following work minutes
            | 2020-000001 | 1  | 2020-04-01 | 0 |
            | 2020-000001 | 2  | 2020-04-01 | 0 |
            | 2020-000001 | 3  | 2020-04-01 | 0 |
            | 2020-000001 | 4  | 2020-04-01 | 0 |
            | 2020-000001 | 5  | 2020-04-01 | 0 |
            | 2020-000001 | 6  | 2020-04-01 | 0 |
            | 2020-000001 | 7  | 2020-04-01 | 0 |
            | 2020-000001 | 8  | 2020-04-01 | 0 |
            | 2020-000001 | 9  | 2020-04-01 | 0 |
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
            | CC | CC   | 5                        |
            | AA | AA   | 10                       |

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Employee availability is extracted without date metadata
        Given there is an employee
        When the employee requests a view of available employees without a given date
        Then the extractor output is "An error occurred: Date metadata has to be provided."

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: An employee who is a project manager of a project requests an overview of the project
        Given that there is a project with name "Test Project"
        And there is an employee
        And the employee is assigned to the activity with ID "1"
        And the activity with ID "1" has an estimated duration of "10" weeks and registered "5" hours spent on date "2020-04-25"
        And the employee is the project manager for the project
        When the employee requests a view of the project
        Then the following table is presented
            | ID | Name           | Start week | End week | Estimated work hours (in total) | Tracked work hours (in total) |
            | 1  | First Activity | 2020-17    | 2020-27  | 0                               | 5                             |

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: An employee who is not project manager of a project requests an overview of the project
        Given that there is a project with name "Test Project"
        And there is an employee
        When the employee requests a view of the project
        Then the error message "Project manager role required." is given

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A project manager views a schedule
        Given that there is a project with name "Test project"
        And the project has the following activities
            | First Activity  | 200 | 2020-01 | 2020-52 |
            | Second Activity | 200 | 2020-01 | 2020-52 |
            | Third Activity  | 200 | 2020-01 | 2020-52 |
            | Fourth Activity | 200 | 2020-01 | 2020-52 |
            | Fifth Activity  | 200 | 2020-01 | 2020-52 |
        And there is an employee with initials "BB"
        And the employee has the following work minutes
            | 2020-000001 | 1 | 2020-04-01 | 10 |
            | 2020-000001 | 2 | 2020-04-01 | 20 |
            | 2020-000001 | 3 | 2020-04-01 | 30 |
            | 2020-000001 | 4 | 2020-04-01 | 40 |
            | 2020-000001 | 5 | 2020-04-01 | 50 |
        And the employee requests the following OOO activities
            | Vacation  | 2020-04-01 | 2020-04-14 |
            | Illness   | 2020-04-15 | 2020-04-18 |
            | Education | 2020-04-10 | 2020-04-14 |
        And there is an employee
        When the employee requests a view of the schedule of the employee with ID "BB"
        Then the following table is presented
            | ID | Name            | Start week | End week | Estimated work hours (in total) | Tracked work hours (in total) |
            | 2  | Second Activity | 2020-01    | 2020-52  | 200                             | 1                             |
            | 1  | First Activity  | 2020-01    | 2020-52  | 200                             | 1                             |
            | 4  | Fourth Activity | 2020-01    | 2020-52  | 200                             | 1                             |
            | 3  | Third Activity  | 2020-01    | 2020-52  | 200                             | 1                             |
            | 5  | Fifth Activity  | 2020-01    | 2020-52  | 200                             | 1                             |
        And the following table is presented
            | Type      | Start      | End        |
            | Vacation  | 2020-04-01 | 2020-04-14 |
            | Illness   | 2020-04-15 | 2020-04-18 |
            | Education | 2020-04-10 | 2020-04-14 |

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A project manager views a schedule of a non-existing employee
        Given there is an employee
        When the employee requests a view of the schedule of the employee with ID "CC"
        Then a CommandException is thrown

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: An employee request a view of daily submissions
        Given that there is a project with name "Test project"
        And the project has the following activities
            | First Activity  | 200 | 2020-01 | 2020-52 |
            | Second Activity | 200 | 2020-01 | 2020-52 |
            | Third Activity  | 200 | 2020-01 | 2020-52 |
            | Fourth Activity | 200 | 2020-01 | 2020-52 |
            | Fifth Activity  | 200 | 2020-01 | 2020-52 |
        And there is an employee
        And the employee has the following work minutes today
            | 2020-000001 | 1 | 30 |
            | 2020-000001 | 2 | 60 |
            | 2020-000001 | 3 | 90 |
            | 2020-000001 | 4 | 30 |
            | 2020-000001 | 5 | 60 |
        When the employee requests of the daily submissions
        Then the following table is presented
            | Project ID  | Activity ID | Tracked hours |
            | 2020-000001 | 1           | 0.5           |
            | 2020-000001 | 2           | 1.0           |
            | 2020-000001 | 3           | 1.5           |
            | 2020-000001 | 4           | 0.5           |
            | 2020-000001 | 5           | 1.0           |