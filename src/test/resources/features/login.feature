@login
Feature: Authentication and Access Verification
  As a registered user on SauceDemo
  I want to log into the application with valid credentials
  And receive clear error feedback when providing invalid credentials

  Background:
    Given user navigates to the login page

  @positive @smoke
  Scenario: Successful login with standard active credentials
    When user enters username "standard_user" and password "secret_sauce"
    And user clicks on the login button
    Then user should be redirected to the inventory catalog page
    And the page URL should contain "/inventory.html"

  @negative
  Scenario Outline: Failed login attempts with invalid or locked credentials
    When user enters username "<username>" and password "<password>"
    And user clicks on the login button
    Then an error message stating "<expected_error>" should be displayed

    Examples:
      | username        | password       | expected_error                                              |
      | locked_out_user | secret_sauce   | Epic sadface: Sorry, this user has been locked out.         |
      | standard_user   | wrong_password | Epic sadface: Username and password do not match any user in this service |
      |                 | secret_sauce   | Epic sadface: Username is required                         |