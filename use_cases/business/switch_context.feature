Feature: Switch context
    Description: A user requests a context switch
    Actors: Employee

    Scenario: A user switches context
        Given there is an employee
        When the employee switches to context "PM"
        Then the current context is "ProjectManagerInputContext"