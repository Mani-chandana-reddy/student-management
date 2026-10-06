package com.chandana.sms.dao;

import com.chandana.sms.util.DatabaseConnection;
import com.chandana.sms.model.Course;
import com.chandana.sms.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public boolean studentExists(int studentId, Connection conn) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE student_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean courseExists(int courseId, Connection conn) throws SQLException {
        String sql = "SELECT 1 FROM courses WHERE course_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean enrollmentExists(int studentId, int courseId, Connection conn) throws SQLException {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insertEnrollment(int studentId, int courseId, Connection conn) throws SQLException {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        }
    }

    public boolean removeEnrollment(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Course> getCoursesForStudent(int studentId) throws SQLException {
        String sql = "SELECT c.* FROM courses c INNER JOIN enrollments e ON c.course_id = e.course_id " +
                     "WHERE e.student_id = ? ORDER BY c.course_id";
        List<Course> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Course(rs.getInt("course_id"), rs.getString("course_name"),
                            rs.getString("department"), rs.getInt("credits")));
                }
            }
        }
        return list;
    }

    public List<Student> getStudentsForCourse(int courseId) throws SQLException {
        String sql = "SELECT s.* FROM students s INNER JOIN enrollments e ON s.student_id = e.student_id " +
                     "WHERE e.course_id = ? ORDER BY s.student_id";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(rs.getInt("student_id"), rs.getString("name"), rs.getInt("age"),
                            rs.getString("email"), rs.getString("phone"), rs.getString("department"),
                            rs.getInt("year"), rs.getDate("admission_date"), rs.getDouble("marks"),
                            rs.getString("status")));
                }
            }
        }
        return list;
    }
}
