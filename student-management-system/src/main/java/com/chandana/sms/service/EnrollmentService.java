package com.chandana.sms.service;

import com.chandana.sms.dao.EnrollmentDAO;
import com.chandana.sms.util.DatabaseConnection;
import com.chandana.sms.model.Course;
import com.chandana.sms.model.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

  
    public void enrollStudent(int studentId, int courseId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            if (!enrollmentDAO.studentExists(studentId, conn)) {
                throw new IllegalArgumentException("Student ID " + studentId + " does not exist.");
            }
            if (!enrollmentDAO.courseExists(courseId, conn)) {
                throw new IllegalArgumentException("Course ID " + courseId + " does not exist.");
            }
            if (enrollmentDAO.enrollmentExists(studentId, courseId, conn)) {
                throw new IllegalArgumentException("Student is already enrolled in this course.");
            }

            enrollmentDAO.insertEnrollment(studentId, courseId, conn);
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            if (e instanceof SQLException) throw (SQLException) e;
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    public boolean removeEnrollment(int studentId, int courseId) throws SQLException {
        return enrollmentDAO.removeEnrollment(studentId, courseId);
    }

    public List<Course> getCoursesForStudent(int studentId) throws SQLException {
        return enrollmentDAO.getCoursesForStudent(studentId);
    }

    public List<Student> getStudentsForCourse(int courseId) throws SQLException {
        return enrollmentDAO.getStudentsForCourse(courseId);
    }
}
