Feature: A white box test for method projectInstance.assignPM

    # Author: Someone
    Scenario: Set A: PM is null
        Given there is an employee
        And that there is a project with name "Test project"
        And the project does not have a project manager
        When assignPM is called in the project instance with "AA" as newPM
        Then the employee is the project manager for the project

    # Author: Someone
    Scenario: Set B: The signed in employee is not PM
        Given there is an employee with initials "BB"
        And that there is a project with name "Test project"
        And the employee with initials "BB" is the project manager of the project
        And there is an employee
        When assignPM is called in the project instance with "AA" as newPM
        Then the white box error exception of type "AccessDeniedException" with message "Project manager role required." is given

    # Author: Someone
    Scenario: Set C: PM is not null, and the signed in employee is PM
        Given there is an employee
        And there is an employee with initials "BB"
        And that there is a project with name "Test project"
        And the employee with initials "BB" is the project manager of the project
        When assignPM is called in the project instance with "AA" as newPM
        Then the employee with initials "AA" is the project manager of the project
