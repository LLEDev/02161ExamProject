Feature: testing Request assistance in Application

    # Author: Thor Dueholm (s194589)
    Scenario: Set A: projectID not in database
        Given there is an employee with initials "HH"
        And there is an employee
        When request assistance is called in Application with arguments "2020-000001", "1" And "HH"
        Then the white box error exception of type "IllegalArgumentException" with message "The given project, 2020-000001, does not exist." is given

    # Author: Thor Dueholm (s194589)
    Scenario: Set B: signed in employee has no activities in projectID
        Given there is an employee with initials "HH"
        And there is an employee
        And that there is a project with name "Test-Project"
        When request assistance is called in Application with arguments "2020-000001", "1" And "HH"
        Then the white box error exception of type "AccessDeniedException" with message "You are not allowed to work with the given project, 2020-000001." is given

    # Author: Thor Dueholm (s194589)
    Scenario: Set C: signed in employee is not attached to given activityID
        Given there is an employee with initials "HH"
        And there is an employee
        And that there is a project with name "Test-Project"
        And the project has the following activities
            | First Activity  | 200 | 2020-01 | 2020-52 |
            | Second Activity | 200 | 2020-01 | 2020-52 |
        And the employee is assigned to the activity with ID "2"
        When request assistance is called in Application with arguments "2020-000001", "1" And "HH"
        Then the white box error exception of type "AccessDeniedException" with message "You are not allowed to work with the given activity, 1." is given

    # Author: Thor Dueholm (s194589)
    Scenario: Set D: employeeID has no room for activities
        Given there is an employee with initials "HH"
        And the employee with initials "HH" has reached the activity cap
        And that there is a project with name "Test-Project"
        And the project has the following activities
            | First Activity | 200 | 2020-01 | 2020-52 |
        And there is an employee
        And the employee is attached to all activities in the projects
            | 2020-000001 |
        When request assistance is called in Application with arguments "2020-000001", "1" And "HH"
        Then the white box error exception of type "IllegalArgumentException" with message "The employee HH has no room for any new activities at the moment." is given

    # Author: Thor Dueholm (s194589)
    Scenario: Set E: employeeID has room for activities
        Given there is an employee with initials "HH"
        And that there is a project with name "Test-Project"
        And the project has the following activities
            | First Activity | 200 | 2020-01 | 2020-52 |
        And there is an employee
        And the employee is attached to all activities in the projects
            | 2020-000001 |
        When request assistance is called in Application with arguments "2020-000001", "1" And "HH"
        Then the employee with initials "HH" is assigned to the activity with ID "1"

