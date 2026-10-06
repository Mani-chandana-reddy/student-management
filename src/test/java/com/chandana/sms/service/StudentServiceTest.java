package com.chandana.sms.service;

import com.chandana.sms.model.Student;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** These tests only exercise validation, so no database is needed. */
class StudentServiceTest {
    private final StudentService service = new StudentService();

    private Student validStudent() {
        return new Student(1, "Aarav Sharma", 20, "aarav@example.com", "9000000001",
                "CSE", 2, new Date(System.currentTimeMillis()), 78.5, "ACTIVE");
    }

    @Test
    void validStudentPassesValidation() {
        assertDoesNotThrow(() -> service.validate(validStudent()));
    }

    @Test
    void nullEmailIsRejected() {
        Student s = validStudent();
        s.setEmail(null);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.validate(s));
        assertEquals("Email must not be empty.", e.getMessage());
    }

    @Test
    void badEmailFormatIsRejected() {
        Student s = validStudent();
        s.setEmail("not-an-email");
        assertThrows(IllegalArgumentException.class, () -> service.validate(s));
    }

    @Test
    void blankNameIsRejected() {
        Student s = validStudent();
        s.setName("   ");
        assertThrows(IllegalArgumentException.class, () -> service.validate(s));
    }

    @Test
    void marksOutOfRangeAreRejected() {
        Student s = validStudent();
        s.setMarks(120);
        assertThrows(IllegalArgumentException.class, () -> service.validate(s));
    }

    @Test
    void addStudentFailsBeforeTouchingDatabaseWhenInvalid() {
        Student s = validStudent();
        s.setStudentId(0);
        assertThrows(IllegalArgumentException.class, () -> service.addStudent(s));
    }

    @Test
    void searchByCourseNameRejectsBlankInput() {
        assertThrows(IllegalArgumentException.class, () -> service.searchByCourseName("  "));
        assertThrows(IllegalArgumentException.class, () -> service.searchByCourseName(null));
    }

    @Test
    void searchByNameRejectsBlankInput() {
        assertThrows(IllegalArgumentException.class, () -> service.searchByName(""));
    }
}
