Feature: Demo

  @demo
  Scenario: I try to login with invalid credentials
    When I open 'Login Screen' view
    Then Login Screen is opened
    When I save Login Screen dump
      And I log in with data:
      | Username | MyUsername      |
      | Password | InVal1dPa$$w0rd |
    Then 'Invalid login credentials, please try again' alert appears
    When I accept the alert
    Then Login Screen dump is different

  @demo
  Scenario Outline: ScenarioContext demo
    When I store '<value1>' as 'value1'
    And I store '<value2>' as 'value2'
    And I add 'value1' to 'value2' and store it as 'value3'
    Then 'value3' should be equal to '<result>'

    Examples:
      | value1 | value2 | result |
      | 2      | 3      | 5      |
      | 3      | 4      | 7      |
