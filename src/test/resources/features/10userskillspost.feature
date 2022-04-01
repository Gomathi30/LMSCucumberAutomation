Feature: As a tester,I want to test and validate Post request in UserSkills API

  Background: The Authorization is set as basic auth with valid username and password

  Scenario: Test the userSkills Api for post request with valid authorization and valid details
    Given User is on post method of userskills API
    When User sends post request with valid values with endpoint URL in userskills
    Then The status code is 201 with success message OK
