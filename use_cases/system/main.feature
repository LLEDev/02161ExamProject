Feature: Run Main application
    Description: Test the bridge between the user and the command methods in application
    Actors: User

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user boots the application with invalid external data files
        When a user boots the application with the following arguments
            | HH           |
            | PM           |
            | INVALID_PATH |
        Then "One or more data files are missing" is a part of the output
        And "Welcome, Hans Hansen" is not a part of the output
        Then the output will be reset

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user boots the application with valid external data files
        When a user boots the application with valid external data files and the following arguments
            | HH |
            | PM |
        Then "Welcome, Hans Hansen" is a part of the output
        And "Available commands:" is a part of the output
        Then the output will be reset

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user signs in
        When a user boots the application with the following arguments
            | HH |
            | PM |
        Then "Welcome, Hans Hansen" is a part of the output
        And "Available commands:" is a part of the output
        Then the output will be reset

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user tries to sign in without specifying arguments
        When a user boots the application with the following arguments
            |  |
        Then "Usage: java -jar" is a part of the output
        Then the output will be reset

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user tries to sign in with non-existing user
        When a user boots the application with the following arguments
            | ABCD |
            | PM   |
        Then "Given employee does not exist" is a part of the output
        And "Welcome, Hans Hansen" is not a part of the output
        Then the output will be reset

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user tries to sign in with non-existing context
        When a user boots the application with the following arguments
            | HH  |
            | ABC |
        Then "This context is not available" is a part of the output
        And "Welcome, Hans Hansen" is not a part of the output
        Then the output will be reset

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user signs in as project manager and quits
        When a user boots the application with the following arguments
            | HH |
            | PM |
        And a user quits the application
        Then "Bye!" is a part of the output
        Then the output will be reset

    # Author: Lasse Lund-Egmose (s194568)
    Scenario: A user signs in as employee and quits
        When a user boots the application with the following arguments
            | HH  |
            | EMP |
        And a user quits the application
        Then "Bye!" is a part of the output
        Then the output will be reset