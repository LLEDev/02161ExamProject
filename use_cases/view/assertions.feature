Feature: Test assertion helpers in input context

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Test parse int assertion
        Given there is an employee
        When string "1" is asserted integer-parseable
        Then no exception related to parsing is thrown

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Test parse int assertion
        Given there is an employee
        When string "NOT-AN-INT" is asserted integer-parseable
        Then an exception related to parsing is thrown

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Test parse double assertion
        Given there is an employee
        When string "1.5" is asserted double-parseable
        Then no exception related to parsing is thrown

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Test parse double assertion
        Given there is an employee
        When string "NOT-A-DOUBLE" is asserted double-parseable
        Then an exception related to parsing is thrown

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Test parse date assertion
        Given there is an employee
        When string "2020-01-01" is asserted date-parseable
        Then no exception related to parsing is thrown

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Test parse date assertion
        Given there is an employee
        When string "NOT-A-DATE" is asserted date-parseable
        Then an exception related to parsing is thrown

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Test parse week assertion
        Given there is an employee
        When string "2020-05" is asserted week date-parseable
        Then no exception related to parsing is thrown

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: Test parse week assertion
        Given there is an employee
        When string "NOT-A-WEEK" is asserted week date-parseable
        Then an exception related to parsing is thrown