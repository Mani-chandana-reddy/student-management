
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;

CREATE TABLE students (
    student_id      INTEGER PRIMARY KEY CHECK (student_id > 0),
    name            VARCHAR(100) NOT NULL,
    age             INTEGER NOT NULL CHECK (age > 0 AND age < 120),
    email           VARCHAR(150) NOT NULL,
    phone           VARCHAR(20),
    department      VARCHAR(50) NOT NULL,
    year            INTEGER NOT NULL CHECK (year BETWEEN 1 AND 6),
    admission_date  DATE NOT NULL DEFAULT CURRENT_DATE,
    marks           NUMERIC(5,2) CHECK (marks >= 0 AND marks <= 100),
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE courses (
    course_id       INTEGER PRIMARY KEY CHECK (course_id > 0),
    course_name     VARCHAR(100) NOT NULL,
    department      VARCHAR(50) NOT NULL,
    credits         INTEGER NOT NULL CHECK (credits > 0)
);

CREATE TABLE enrollments (
    enrollment_id    SERIAL PRIMARY KEY,
    student_id       INTEGER NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    course_id        INTEGER NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    enrollment_date  DATE NOT NULL DEFAULT CURRENT_DATE,
    enrollment_status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
    UNIQUE (student_id, course_id)
);


INSERT INTO students (student_id, name, age, email, phone, department, year, admission_date, marks, status) VALUES
(1, 'Aarav Sharma', 20, 'aarav@example.com', '9000000001', 'CSE', 2, '2023-07-01', 78.5, 'ACTIVE'),
(2, 'Priya Nair', 21, 'priya@example.com', '9000000002', 'ECE', 3, '2022-07-01', 85.0, 'ACTIVE'),
(3, 'Rohan Verma', 19, 'rohan@example.com', '9000000003', 'CSE', 1, '2024-07-01', 62.0, 'ACTIVE'),
(4, 'Sneha Iyer', 22, 'sneha@example.com', '9000000004', 'ME', 4, '2021-07-01', 91.2, 'ACTIVE'),
(5, 'Karan Patel', 20, 'karan@example.com', '9000000005', 'CSE', 2, '2023-07-01', 55.5, 'ACTIVE'),
(6, 'Divya Reddy', 21, 'divya@example.com', '9000000006', 'EEE', 3, '2022-07-01', 74.0, 'ACTIVE'),
(7, 'Arjun Singh', 19, 'arjun@example.com', '9000000007', 'CSE', 1, '2024-07-01', 88.8, 'ACTIVE'),
(8, 'Meera Joshi', 22, 'meera@example.com', '9000000008', 'ECE', 4, '2021-07-01', 69.3, 'ACTIVE'),
(9, 'Vikram Rao', 20, 'vikram@example.com', '9000000009', 'ME', 2, '2023-07-01', 80.0, 'ACTIVE'),
(10, 'Ananya Das', 21, 'ananya@example.com', '9000000010', 'CSE', 3, '2022-07-01', 45.0, 'ACTIVE');

INSERT INTO courses (course_id, course_name, department, credits) VALUES
(1, 'Data Structures', 'CSE', 4),
(2, 'Digital Electronics', 'ECE', 3),
(3, 'Thermodynamics', 'ME', 4),
(4, 'Power Systems', 'EEE', 3),
(5, 'Database Systems', 'CSE', 4);

INSERT INTO enrollments (student_id, course_id) VALUES
(1, 1), (1, 5), (2, 2), (3, 1), (4, 3), (6, 4), (7, 1), (7, 5), (9, 3);
