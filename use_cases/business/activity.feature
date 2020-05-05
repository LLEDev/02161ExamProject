Feature: Estimate and set interval for activities

   Scenario: An employee sets work hours under 0 again
     Given that there is a project with name "Test project"
     And there is an employee
     And the employee is attached to all activities in the projects
       | 2020-000001 |
     When the employee sets the work hours "-10" in the activity with ID "1".
     Then these activities with overall durations are found
       | 2020-000001 | 1 | -10 |

   Scenario: An employee estimates activity duration to negative hours
     Given that there is a project with name "Test project"
     And there is an employee
     And the employee is the project manager for the project
     When the employee estimates the duration to "-200" hours for the activity with ID "1"
     Then the error message "The estimated number of work hours has to be bigger than 0. -200 received." is given

  Scenario: An employee estimates activity duration to positive hours
    Given that there is a project with name "Test project"
    And there is an employee
    And the employee is the project manager for the project
    When the employee estimates the duration to "200" hours for the activity with ID "1"
    Then the activity with ID "1" has an estimated duration of "200" hours

  Scenario: An employee sets the end date before the start date of an activity
    Given that there is a project with name "Test project"
    And there is an employee
    And the employee is the project manager for the project
    When the employee sets the start date "2020-05-04" and the end date "2020-05-03" of the activity with ID "1"
    Then the error message "The given start week, 2020-05-04, is after the given end week, 2020-05-03." is given

  Scenario: An employee sets the end date after the start date of an activity
    Given that there is a project with name "Test project"
    And there is an employee
    And the employee is the project manager for the project
    When the employee sets the start date "2020-05-04" and the end date "2020-05-05" of the activity with ID "1"
    Then the activity with ID "1" has the start date "2020-05-04" and the end date "2020-05-05"