Feature: Add employee to project activity
    Description: An employee is added to a project
    Actors: Employee

    # Author: Someone
    Scenario: Employee who is a project manager adds another employee to a project
        Given that there is a project with name "Test Project"
        And the following employees are given
            | ABCD |
            | BS   |
        And the employee with initials "BS" is the project manager of the project
        And the employee with initials "ABCD" has not reached the activity cap
        And the employee with initials "BS" is the actor
        When the actor adds the employee with initials "ABCD" to the activity with ID "1"
        Then the employee with initials "ABCD" has been assigned to the activity with ID "1"

    # Author: Someone
    Scenario: Employee adds another employee to a project activity
        Given that there is a project with name "Test Project"
        And the following employees are given
            | BS   |
            | ABCD |
        And the employee with initials "ABCD" is assigned to the activity with ID "1"
        And the employee with initials "BS" has not reached the activity cap
        And the employee with initials "ABCD" is the actor
        When the actor adds the employee with initials "BS" to the activity with ID "1"
        Then the error message "Project manager role required." is given

    # Author: Someone
    Scenario: Employee who is a project manager adds another employee to a non existing project activity
        Given that there is a project with name "Test Project"
        And the following employees are given
            | ABCD |
            | BS   |
        And the employee with initials "BS" is the project manager of the project
        And the employee with initials "ABCD" has not reached the activity cap
        And the employee with initials "BS" is the actor
        When the actor adds the employee with initials "ABCD" to the activity with ID "2"
        Then the error message "The given activity, 2, does not exist within project, 2020-000001." is given

    # Author: Mikkel Theiss Westermann (s194601)
    Scenario: Employee who is a project manager adds employee who has reached activity cap to a project
        Given that there is a project with name "Test Project"
        And the following employees are given
            | ABCD |
            | BS   |
        And the employee with initials "BS" is the project manager of the project
        And the employee with initials "ABCD" has reached the activity cap
        And the employee with initials "BS" is the actor
        When the actor adds the employee with initials "ABCD" to the activity with ID "1"
        Then the error message "The employee ABCD has no room for any new activities at the moment." is given