Feature: Assign project manager
    Description: A project manager is assigned to a project
    Actors: Employee

    # Author: Thor Eric Dueholm (s194589)
    Scenario: Employee assigns project manager to a project without project manager
        Given that there is a project with name "Test Project"
        And there is an employee with initials "ABCD"
        And the project does not have a project manager
        And there is an employee with initials "BS"
        And the employee with initials "ABCD" is the actor
        When the actor assigns the employee with initials "BS" as the project manager of the project
        Then the project has a project manager with initials "BS"

    # Author: Mikkel Theiss Westermann (s194601)
    Scenario: Employee assigns project manager to a project to which a project manager is already assigned
        Given that there is a project with name "Test Project"
        And the following employees are given
            | BS  |
            | TED |
            | MTW |
        And the employee with initials "BS" is the project manager of the project
        And the employee with initials "TED" is the actor
        When the actor assigns the employee with initials "MTW" as the project manager of the project
        Then the error message "Project manager role required." is given

    # Author: Someone
    Scenario: Employee who is a project manager assigns project manager to their project
        Given that there is a project with name "Test Project"
        And the following employees are given
            | BS  |
            | TED |
            | MTW |
        And the employee with initials "BS" is the project manager of the project
        And the employee with initials "BS" is the actor
        When the actor assigns the employee with initials "TED" as the project manager of the project
        Then the project has a project manager with initials "TED"
