package com.chandana.sms.app;

import com.chandana.sms.util.DatabaseConnection;
import com.chandana.sms.model.Course;
import com.chandana.sms.model.Student;
import com.chandana.sms.service.CourseService;
import com.chandana.sms.service.EnrollmentService;
import com.chandana.sms.service.StudentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService = new EnrollmentService();

    public static void main(String[] args) {
        try {
            DatabaseConnection.getConnection();
        } catch (SQLException e) {
            log.error("Could not connect to the database", e);
            System.out.println("Could not connect to the database: " + e.getMessage());
            return;
        }

        log.info("Application started");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: viewAllStudents(); break;
                    case 3: searchById(); break;
                    case 4: searchByName(); break;
                    case 5: updateStudent(); break;
                    case 6: deleteStudent(); break;
                    case 7: addCourse(); break;
                    case 8: viewCourses(); break;
                    case 9: enrollStudent(); break;
                    case 10: removeEnrollment(); break;
                    case 11: viewEnrollmentsForStudent(); break;
                    case 12: viewByDepartment(); break;
                    case 13: viewByYear(); break;
                    case 14: viewByCourse(); break;
                    case 15: viewAboveMarks(); break;
                    case 16: viewMarksRange(); break;
                    case 17: countByDepartment(); break;
                    case 18: countByCourse(); break;
                    case 19: viewAverageMarks(); break;
                    case 20: viewHighest(); break;
                    case 21: viewLowest(); break;
                    case 22: searchByCourseName(); break;
                    case 23: running = false; break;
                    default: System.out.println("Invalid choice. Try again.");
                }
            } catch (IllegalArgumentException e) {
                log.warn("Invalid input: {}", e.getMessage());
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                log.error("Database error", e);
                System.out.println("Database error: " + e.getMessage());
            }
        }

        DatabaseConnection.closeConnection();
        log.info("Application stopped");
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\n===== Student Management System =====");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. View All Students");
        System.out.println(" 3. Search Student by ID");
        System.out.println(" 4. Search Student by Name");
        System.out.println(" 5. Update Student");
        System.out.println(" 6. Delete Student");
        System.out.println(" 7. Add Course");
        System.out.println(" 8. View Courses");
        System.out.println(" 9. Enroll Student in Course");
        System.out.println("10. Remove Student Enrollment");
        System.out.println("11. View Student Enrollments");
        System.out.println("12. View Students by Department");
        System.out.println("13. View Students by Year");
        System.out.println("14. View Students by Course");
        System.out.println("15. View Students Above Marks");
        System.out.println("16. View Students Within Marks Range");
        System.out.println("17. View Student Count by Department");
        System.out.println("18. View Student Count by Course");
        System.out.println("19. View Average Marks");
        System.out.println("20. View Highest-Scoring Student");
        System.out.println("21. View Lowest-Scoring Student");
        System.out.println("22. Search Student by Course Name");
        System.out.println("23. Exit");
    }

    private static void addStudent() throws SQLException {
        int id = readInt("Student ID: ");
        System.out.print("Name: "); String name = scanner.nextLine();
        int age = readInt("Age: ");
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Phone: "); String phone = scanner.nextLine();
        System.out.print("Department: "); String dept = scanner.nextLine();
        int year = readInt("Year: ");
        double marks = readDouble("Marks: ");
        Student s = new Student(id, name, age, email, phone, dept, year,
                new Date(System.currentTimeMillis()), marks, "ACTIVE");
        studentService.addStudent(s);
        System.out.println("Student added.");
    }

    private static void viewAllStudents() throws SQLException {
        List<Student> list = studentService.getAllStudents();
        list.forEach(System.out::println);
        if (list.isEmpty()) System.out.println("No students found.");
    }

    private static void searchById() throws SQLException {
        int id = readInt("Student ID: ");
        Student s = studentService.getStudentById(id);
        System.out.println(s != null ? s : "Student not found.");
    }

    private static void searchByName() throws SQLException {
        System.out.print("Name (or part of it): "); String name = scanner.nextLine();
        List<Student> list = studentService.searchByName(name);
        list.forEach(System.out::println);
        if (list.isEmpty()) System.out.println("No matching students.");
    }

    private static void searchByCourseName() throws SQLException {
        System.out.print("Course name (or part of it): "); String course = scanner.nextLine();
        List<Student> list = studentService.searchByCourseName(course);
        list.forEach(System.out::println);
        if (list.isEmpty()) System.out.println("No students found for that course.");
    }

    private static void updateStudent() throws SQLException {
        int id = readInt("Student ID to update: ");
        Student existing = studentService.getStudentById(id);
        if (existing == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.print("New Name (" + existing.getName() + "): "); String name = scanner.nextLine();
        int age = readInt("New Age (" + existing.getAge() + "): ");
        System.out.print("New Email (" + existing.getEmail() + "): "); String email = scanner.nextLine();
        System.out.print("New Phone (" + existing.getPhone() + "): "); String phone = scanner.nextLine();
        System.out.print("New Department (" + existing.getDepartment() + "): "); String dept = scanner.nextLine();
        int year = readInt("New Year (" + existing.getYear() + "): ");
        double marks = readDouble("New Marks (" + existing.getMarks() + "): ");
        Student s = new Student(id, keepIfBlank(name, existing.getName()), age,
                keepIfBlank(email, existing.getEmail()), keepIfBlank(phone, existing.getPhone()),
                keepIfBlank(dept, existing.getDepartment()), year, existing.getAdmissionDate(), marks, existing.getStatus());
        studentService.updateStudent(s);
        System.out.println("Student updated.");
    }

    // Pressing Enter on an update prompt keeps the current value (also avoids a null or empty email)
    private static String keepIfBlank(String input, String current) {
        return (input == null || input.trim().isEmpty()) ? current : input.trim();
    }

    private static void deleteStudent() throws SQLException {
        int id = readInt("Student ID to delete: ");
        studentService.deleteStudent(id);
        System.out.println("Student deleted.");
    }

    private static void addCourse() throws SQLException {
        int id = readInt("Course ID: ");
        System.out.print("Course Name: "); String name = scanner.nextLine();
        System.out.print("Department: "); String dept = scanner.nextLine();
        int credits = readInt("Credits: ");
        courseService.addCourse(new Course(id, name, dept, credits));
        System.out.println("Course added.");
    }

    private static void viewCourses() throws SQLException {
        List<Course> list = courseService.getAllCourses();
        list.forEach(System.out::println);
        if (list.isEmpty()) System.out.println("No courses found.");
    }

    private static void enrollStudent() throws SQLException {
        int studentId = readInt("Student ID: ");
        int courseId = readInt("Course ID: ");
        enrollmentService.enrollStudent(studentId, courseId);
        System.out.println("Enrollment successful.");
    }

    private static void removeEnrollment() throws SQLException {
        int studentId = readInt("Student ID: ");
        int courseId = readInt("Course ID: ");
        boolean removed = enrollmentService.removeEnrollment(studentId, courseId);
        System.out.println(removed ? "Enrollment removed." : "Enrollment not found.");
    }

    private static void viewEnrollmentsForStudent() throws SQLException {
        int studentId = readInt("Student ID: ");
        List<Course> courses = enrollmentService.getCoursesForStudent(studentId);
        courses.forEach(System.out::println);
        if (courses.isEmpty()) System.out.println("No enrollments found.");
    }

    private static void viewByDepartment() throws SQLException {
        System.out.print("Department: "); String dept = scanner.nextLine();
        studentService.filterByDepartment(dept).forEach(System.out::println);
    }

    private static void viewByYear() throws SQLException {
        int year = readInt("Year: ");
        studentService.filterByYear(year).forEach(System.out::println);
    }

    private static void viewByCourse() throws SQLException {
        int courseId = readInt("Course ID: ");
        studentService.filterByCourse(courseId).forEach(System.out::println);
    }

    private static void viewAboveMarks() throws SQLException {
        double marks = readDouble("Marks threshold: ");
        studentService.studentsAboveMarks(marks).forEach(System.out::println);
    }

    private static void viewMarksRange() throws SQLException {
        double low = readDouble("Low: ");
        double high = readDouble("High: ");
        studentService.studentsInMarksRange(low, high).forEach(System.out::println);
    }

    private static void countByDepartment() throws SQLException {
        for (String[] row : studentService.countByDepartment()) {
            System.out.println(row[0] + ": " + row[1]);
        }
    }

    private static void countByCourse() throws SQLException {
        for (String[] row : studentService.countByCourse()) {
            System.out.println(row[0] + ": " + row[1]);
        }
    }

    private static void viewAverageMarks() throws SQLException {
        System.out.printf("Average marks: %.2f%n", studentService.averageMarks());
    }

    private static void viewHighest() throws SQLException {
        System.out.println(studentService.highestScoring());
    }

    private static void viewLowest() throws SQLException {
        System.out.println(studentService.lowestScoring());
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Double.parseDouble(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
