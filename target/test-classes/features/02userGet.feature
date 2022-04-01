Feature: As a tester, I want to test User API so that I can validate GET method

Background: Select Authorization Type as Basic Auth and enter valid credentials

  Scenario: Test the Get request for all users of User API
    Given user is on the Get method
    When user sends get request with valid authorization for all users
    Then user details are displayed with 200 OK message

  Scenario: Test the Api for get request for single user with valid id
    Given user is on the Get method
    When user sends get request for valid id with valid authorization
    Then single user details are displayed with 200 OK message
    
  Scenario: Test the Get request for User API without authorization
    Given user is on the Get method
    When user sends get request with no authorization
    Then 401 unathorized status code should be returned
    
 Scenario: Test the Api for get request with invalid id
    Given user is on the Get method
    When user sends get request for invalid id 
    Then single user details are displayed with 404 Not found message
