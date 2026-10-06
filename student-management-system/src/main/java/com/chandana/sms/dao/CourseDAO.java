package com.chandana.sms.dao;

import com.chandana.sms.util.DatabaseConnection;
import com.chandana.sms.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public boolean insertCourse(Course c) throws SQLException {
        String sql = "INSERT INTO courses (course_id, course_name, department, credits) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, c.getCourseId());
            ps.setString(2, c.getCourseName());
            ps.setString(3, c.getDepartment());
            ps.setInt(4, c.getCredits());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        String sql = "SELECT * FROM courses ORDER BY course_id";
        List<Course> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Course getCourseById(int id) throws SQLException {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Course> coursesWithEnrolledStudents() throws SQLException {
        String sql = "SELECT DISTINCT c.* FROM courses c INNER JOIN enrollments e ON c.course_id = e.course_id " +
                     "ORDER BY c.course_id";
        List<Course> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        return new Course(rs.getInt("course_id"), rs.getString("course_name"),
                rs.getString("department"), rs.getInt("credits"));
    }
}
