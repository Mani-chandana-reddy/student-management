# Student Management System

A console application for managing students, courses and enrollments. It is written in Java and stores data in PostgreSQL through JDBC. The project is built with Maven, so anyone can build it with one command, and its history is managed with Git and GitHub.

## Features

- Add, view, update and delete students
- Search students by ID, by name, or by course name
- Add and view courses, and enroll or remove students from courses
- Filter students by department, year, course and marks
- Counts, average, highest and lowest marks
- Logging with SLF4J and Logback (log file at `logs/sms.log`)

## Prerequisites

- JDK 17 or newer
- Maven 3.8 or newer
- PostgreSQL with a database named `student_management`
- Git

## Database setup

Run `sql/schema.sql` on the `student_management` database. It creates the tables and adds some sample data.

## Configuration

The database login comes from environment variables, so no password is stored in the code.

    export DB_PASSWORD=your_password
    export DB_USER=postgres
    export DB_URL=jdbc:postgresql://localhost:5432/student_management

`DB_PASSWORD` is required. `DB_USER` and `DB_URL` are optional and default to the values above.

## Build and run

    git clone https://github.com/chandana/student-management.git
    cd student-management
    mvn clean package
    java -jar target/student-management-1.0.0-SNAPSHOT.jar

`mvn clean package` compiles the code, runs the unit tests and builds a single runnable JAR that includes all dependencies. Other commands used during the task: `mvn clean`, `mvn compile`, `mvn test`, `mvn install` and `mvn dependency:tree`.

## Project structure

    src/main/java/com/chandana/sms/
        app/      Main.java (menu and user input)
        model/    Student, Course, Enrollment
        dao/      database queries
        service/  validation and business rules
        util/     DatabaseConnection, ValidationUtil
    src/main/resources/logback.xml
    src/test/java/                  JUnit 5 tests
    sql/schema.sql                  tables and sample data

## Branching strategy

- `main` holds stable, release-ready code. Nobody commits to it directly.
- `develop` is where finished features are combined.
- `feature/*` branches are created from `develop`, one per feature, and merged back through a Pull Request.
- `hotfix/*` branches are created from `main` for urgent fixes. A hotfix is merged into `main` and then also into `develop`.
- Releases are tagged on `main`, for example `v1.0.0`.

Feature branches used in this project: `feature/maven-setup`, `feature/logging`, `feature/unit-tests` and `feature/search-student`. The hotfix branch was `hotfix/fix-null-email`.

Commit messages follow the style `feat: ...`, `fix: ...`, `refactor: ...`, `test: ...`, `build: ...`, `docs: ...` and `chore: ...`.

## How the merge conflict was resolved

To create a conflict on purpose, two branches were made from `develop`, and both changed the same line (the menu title in `Main.java`) to different text. The first branch was merged into `develop` with no problem. Merging the second branch stopped with a conflict in `Main.java`, and Git marked the file with `<<<<<<<`, `=======` and `>>>>>>>` lines.

To resolve it, the file was opened, the two versions of the line were compared, one final wording was kept and the marker lines were deleted. Then the file was added with `git add` and the merge was finished with `git commit`. `git log --oneline --graph --all` shows the merge commit.

## About amend and rebase

`git commit --amend` replaces the last commit, for example to fix a typo in the message or to add a forgotten file. It is safe only for commits that have not been pushed, because it gives the commit a new ID. If the old commit is already on GitHub and other people have pulled it, rewriting it causes trouble for them. The same rule applies to interactive rebase. On shared branches, use `git revert`, which adds a new commit that undoes an old one and keeps the history intact.

## Git commands used

    git init                       start a repository
    git status                     see changed files
    git add <file>                 stage a file
    git commit -m "message"        save staged changes
    git log --oneline --graph --all   see history with branches
    git diff                       see unstaged changes
    git branch / git checkout -b   list and create branches
    git merge <branch>             merge a branch into the current one
    git stash / git stash pop      set work aside and bring it back
    git revert <commit>            undo a commit with a new commit
    git commit --amend             change the last commit (before pushing)
    git tag v1.0.0                 mark a release
    git remote add origin <url>    connect to GitHub
    git push -u origin <branch>    push a branch
    git push origin --tags         push tags
    git pull                       get changes from GitHub

## Screenshots

Screenshots of the Git graph, a merged Pull Request and the successful Maven build are in the `docs/` folder.
