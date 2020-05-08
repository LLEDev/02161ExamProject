Feature: Switch context
    Description: A user requests a context switch
    Actors: Employee

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user switches context
        Given there is an employee
        When the employee switches to context "PM"
        Then the current context is "ProjectManagerInputContext"

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user switches context
        Given there is an employee
        When the employee switches to context "NOT-EXISTING"
        Then the error message "Given context type is not valid. Valid values are: Emp, PM." is given