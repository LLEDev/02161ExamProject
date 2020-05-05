Feature: A white box test for method employeeActivityIntermediateInstance.submitMinutes

  Scenario: Set A: minutes < 0
    Given there is an employee
    And that there is a project with name "Test-Project"
    And the employee is attached to all activities in the projects
      | 2020-000001 |
    When submit minutes is called with date "2020-04-30" and minutes "-90" on the relation between the activity and the employee
    Then the white box error exception of type "IllegalArgumentException" with message "The submitted number of work minutes has to be more than or equal to 0. -90 received." is given

  Scenario: Set B: minutes≥0, d = null
    Given there is an employee
    And that there is a project with name "Test-Project"
    And the employee is attached to all activities in the projects
      | 2020-000001 |
    When submit minutes is called with date "null" and minutes "90" on the relation between the activity and the employee
    Then the white box error exception of type "IllegalArgumentException" with message "Date cannot be null." is given

  Scenario: Set C: minutes≥0, d!= null
    Given there is an employee
    And that there is a project with name "Test-Project"
    And the employee is attached to all activities in the projects
      | 2020-000001 |
    When submit minutes is called with date "2020-04-30" and minutes "90" on the relation between the activity and the employee
    Then the employee has spent "90" minutes on the activity on the day "2020-04-30"