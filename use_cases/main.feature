Feature: Run Main application
    Description: Test the bridge between the user and the command methods in application
    Actors: User

    Scenario: A user signs in
        When a user boots the application with the following arguments
            | HH |
            | PM |
        Then "Welcome, Hans Hansen" is a part of the output

    Scenario: A user signs in without specifying arguments
        When a user boots the application with the following arguments
            ||
        Then "Usage: java -jar" is a part of the output