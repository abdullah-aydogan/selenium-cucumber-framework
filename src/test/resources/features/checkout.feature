@checkout
Feature: End-to-End Purchase Flow
  As an authenticated customer
  I want to select items, add them to my cart, and complete checkout
  So that I can successfully purchase products

  Background:
    Given user navigates to the login page
    And user enters username "standard_user" and password "secret_sauce"
    And user clicks on the login button
    Then user should be redirected to the inventory catalog page

  @e2e @smoke
  Scenario: Complete successful order checkout flow
    When user adds product "Sauce Labs Backpack" to the cart
    And user adds product "Sauce Labs Bike Light" to the cart
    Then the shopping cart badge should display "2"
    When user navigates to the shopping cart
    Then product "Sauce Labs Backpack" should be visible in the cart
    And product "Sauce Labs Bike Light" should be visible in the cart
    When user proceeds to checkout
    And user fills customer details with first name "John", last name "Doe", and postal code "10001"
    And user clicks continue on checkout overview
    And user completes the order by clicking finish
    Then the order confirmation message should be "Thank you for your order!"

  @negative
  Scenario: Checkout fails when customer information is incomplete
    When user adds product "Sauce Labs Backpack" to the cart
    And user navigates to the shopping cart
    And user proceeds to checkout
    And user fills customer details with first name "", last name "Doe", and postal code "10001"
    And user clicks continue on checkout overview
    Then a checkout error stating "Error: First Name is required" should be displayed