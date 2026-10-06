# Student Management System

Console application (Java + PostgreSQL via JDBC), built with Maven and managed with Git.

## Prerequisites

- JDK 17 or newer
- Maven 3.8 or newer
- PostgreSQL with a database named `student_management`

## Database setup

Run `sql/schema.sql` on the `student_management` database. It creates the tables and inserts sample data.

## Configuration

The database login is read from environment variables, so no password is stored in the code.

    export DB_PASSWORD=your_password
    export DB_USER=postgres                                          # optional, default postgres
    export DB_URL=jdbc:postgresql://localhost:5432/student_management  # optional

## Build and run

    mvn clean package
    java -jar target/student-management-1.0.0-SNAPSHOT.jar
