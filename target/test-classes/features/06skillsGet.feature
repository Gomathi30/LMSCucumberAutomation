Feature: As a tester , I want to validate and test the Get request of Skills API

Background: Select Authorization Type as Basic Auth and enter valid credentials

  Scenario: Test the Get request for all Skills of Skills API with valid auth
    Given user is on the Get method of SkillsApi
    When user sends get request for all skills with end
    Then Skills details are displayed with 200 OK message

  Scenario: Test the Api for get request for single skill with valid id and valid auth
    Given user is on the Get method of SkillsApi
    When user sends get skills request for valid id 
    Then particular skill details are displayed with 200 OK message

  Scenario: Test the Api for get request with invalid skill id
    Given user is on the Get method of SkillsApi
    When user sends get request for invalid skill id
    Then single skill details are displayed with 404 Not found message

  Scenario: Test the Get request for Skills API without authorization
    Given user is on the Get method of SkillsApi
    When user sends get request with no authorization for skills
    Then 401 unathorized status error code should be returned
