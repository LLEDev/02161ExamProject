Feature: A white box test for method activityInstance.assertStartEndValid

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Path A: End is null
        Given that there is a project with name "Test project"
        When the project activity start week is set to "2020-01"
        Then the project activity start week is "2020-01"
        And the project activity end week is "null"

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Path A': Start is null
        Given that there is a project with name "Test project"
        When the project activity end week is set to "2020-01"
        Then the project activity start week is "null"
        And the project activity end week is "2020-01"

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Path B: Start after end
        Given that there is a project with name "Test project"
        When the project activity end week is set to "2020-01"
        And the project activity start week is set to "2020-02"
        Then the white box error exception of type "IllegalArgumentException" with message "The given start week, 2020-02, is after the given end week, 2020-01." is given

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Path C: Start before end
        Given that there is a project with name "Test project"
        When the project activity start week is set to "2020-01"
        And the project activity end week is set to "2020-02"
        Then the project activity start week is "2020-01"
        And the project activity end week is "2020-02"