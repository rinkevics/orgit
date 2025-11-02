# Development Tasks

## Project: JavaFX Application (orgit)

### Features
- [ ] 1 Create a list of git directories, show it inside of MyController
  - [x] 1.1 Make a list of random text items and add it to MyController
  - [x] 1.2 Create cade that will create test git directories each time test suite is executed
    - [x] 1.2.1 Code should create a tmp directory orgit in system tmp directory
    - [x] 1.2.2 The code should create 2 subdirectories, each subdirectory should be a git project
    - [x] 1.2.3 In each git project create and commit a dummy text file
    - [x] 1.2.4 Add 3rd directory, but it should not be a git project. Add a dummy text file to this directory.
  - [x] 1.3 Using TDD approach implement a new method in GitService class, method name is getGitDirs
    - [x] 1.3.1 Create a failing test that asserts that orgit returns a list of all directories in tmp folder orgit.
      - Make sure that the list contains only folders that are git repos
    - [x] 1.3.2 Check that the newly created test is failing
    - [x] 1.3.3 Implement the getGitDirs method
    - [x] 1.3.4 Make sure the test is green
- [x] 2 For each git project in GitTestSetup create a local-origin in a local directory under orgit/origin and push changes to it
- Backlog 
  - Show git folder status
    - local uncommited changes
  - Fetch latest changes from origin
  - Get list of directories inside of C:\wwork\learn directory
  - Filter only directories that contain git project
  - show git status for each git directory

