Feature: Add employee to project activity
    Description: An employee is added to a project
    Actors: Employee

    Scenario: Employee who is a project manager adds another employee to a project
        Given that there is a project with name "Test Project"
        And the following employees are given
            | ABCD |
            | BS |
        And the employee with initials "BS" is the project manager of the project
        And the employee with initials "ABCD" has not reached the activity cap
        And the employee with initials "BS" is the actor
        When the actor adds the employee with initials "ABCD" to the activity with ID "1"
        Then the employee with initials "ABCD" is assigned to the activity with ID "1"

    Scenario: Employee adds another employee to a project activity
        Given that there is a project with name "Test Project"
        And the following employees are given
            | BS |
            | ABCD |
        And the employee with initials "ABCD" is assigned to the the activity with ID "1"
        And the employee with initials "BS" has not reached the activity cap
        And the employee with initials "ABCD" is the actor
        When the actor adds the employee with initials "BS" to the activity with ID "1"
        Then the employee with initials "BS" is assigned to the activity with ID "1"

