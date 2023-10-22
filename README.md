# LeetCode Playground

A small project for me to create a playground for Java LeetCode challenges.
Before that I always copied the code manually into IntelliJ and created the testcases.
As a small additional challenge, I wanted to automate this process. For IntelliJ there is actually already a plugin to open a challenge directly in the IDE.
Unfortunately I didn't find a way to debug the code locally.

With this project, I would like to focus on the following points:
- to have a simple solution for the manual process
- work with GraphQL (client side)
- Java code generation
- use one of the latest Java lts (update to Java 21 soon)
- use Gradle with Kotlin build configuration
- maybe writing a IntelliJ plugin in the future

## Requirements

- Java 17
- Gradle

## Getting started

1. Create a file named "session-token.txt" in the root directory and paste your session token.
You can get your session token from a browser session visiting https://leetcode.com/.
2. Change the value of `PlaygroundCreator.QUESTION_SLUG` to the question slug you want to create.
You can find the question slug in the url here: https://leetcode.com/problems/%QUESTION_SLUG%/description/
3. Run the main method of `PlaygroundCreator`

## Project package structure

    ├── src/main/java
    │   ├── com/github/ir31k0
    │   │   ├── conversion                  # Conversion logics, for various conversions
    │   │   ├── data                        # Data and implementations needed throughout the project
    │   │   ├── helper                      # Small trivial tasks that are needed throughout the project
    │   │   ├── query                       # Wrapper classes for the GraphQL queries
    │   │   ├── response                    # Responses from the HTTP requests to LeetCode
    │   │   ├── task                        # Encapsulation of the tasks to create a playground
    │   │   ├── PlaygroundCreator.java      # The main class to create the playground

## Todos / ideas

- solve the todos in the code
- extend the types in the conversion package
- refactor the query classes
- find a better solution for the `Global` java class?
- refactor the `RequestHelper` class
- update to Java 21
- create a UI?
- change the project to a IntelliJ plugin? maybe additional?

## Contribution
If you want to contribute to the project, you are welcome.
Create issues for bugs and requests or create a merge request if you want to extend the project.
If you are unsure about an extension, feel free to ask with an issue for now. I don't want to "over-engineer" the project, but I am open for new ideas.

# License
Distributed under the MIT License. See LICENSE.txt for more information.
