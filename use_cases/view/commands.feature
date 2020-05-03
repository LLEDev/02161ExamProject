Feature: Test UI part of business logic commands

    Scenario: "project assign pm" is run in employee context
        Given there is an employee
        When the command "project assign pm" is run in employee context with the following arguments
            | 2020-000001 |
            | BB |
        Then no exception is thrown

    Scenario: "switch context" is run in employee context
        Given there is an employee
        When the command "switch context" is run in employee context with the following arguments
            | PM |
        Then no exception is thrown

    Scenario: "hours set" is run in employee context
        Given there is an employee
        When the command "hours set" is run in employee context with the following arguments
            | 2020-000001 |
            | 1 |
            | 2020-05-03 |
            | 2 |
        Then no exception is thrown

    Scenario: "hours submit" is run in employee context
        Given there is an employee
        When the command "hours submit" is run in employee context with the following arguments
            | 2020-000001 |
            | 1 |
            | 2020-05-03 |
            | 2 |
        Then no exception is thrown

    Scenario: "project create" is run in employee context
        Given there is an employee
        When the command "project create" is run in employee context with the following arguments
            | Test Project |
            | true |
        Then no exception is thrown

    Scenario: "request assistance" is run in employee context
        Given there is an employee
        When the command "request assistance" is run in employee context with the following arguments
            | 2020-000001 |
            | 1 |
            | BB |
        Then no exception is thrown

    Scenario: "request ooo" is run in employee context
        Given there is an employee
        When the command "request ooo" is run in employee context with the following arguments
            | Vacation |
            | 2020-05-01 |
            | 2020-05-05 |
        Then no exception is thrown

    Scenario: "request ooo" is run in employee context with incorrect type
        Given there is an employee
        When the command "request ooo" is run in employee context with the following arguments
            | IncorrectType |
            | 2020-05-01 |
            | 2020-05-05 |
        Then a CommandException is thrown

    Scenario: "project activity assign" is run in project manager context with incorrect type
        Given there is an employee
        When the command "project activity assign" is run in project manager context with the following arguments
            | BB |
            | 2020-000001 |
            | 1 |
        Then no exception is thrown

    Scenario: "project activity create" is run in project manager context with incorrect type
        Given there is an employee
        When the command "project activity create" is run in project manager context with the following arguments
            | 2020-000001 |
            | Test activity |
        Then no exception is thrown

    Scenario: "project activity estimate" is run in project manager context with incorrect type
        Given there is an employee
        When the command "project activity estimate" is run in project manager context with the following arguments
            | 2020-000001 |
            | 1 |
            | 10 |
        Then no exception is thrown

    Scenario: "project activity finish" is run in project manager context with incorrect type
        Given there is an employee
        When the command "project activity finish" is run in project manager context with the following arguments
            | 2020-000001 |
            | 1 |
        Then no exception is thrown

    Scenario: "project activity setweeks" is run in project manager context with incorrect type
        Given there is an employee
        When the command "project activity setweeks" is run in project manager context with the following arguments
            | 2020-000001 |
            | 1 |
            | 2020-05 |
            | 2020-06 |
        Then no exception is thrown