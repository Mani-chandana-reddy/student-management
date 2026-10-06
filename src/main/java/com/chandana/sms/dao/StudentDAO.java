package com.chandana.sms.dao;

import com.chandana.sms.util.DatabaseConnection;
import com.chandana.sms.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean insertStudent(Student s) throws SQLException {
        String sql = "INSERT INTO students (student_id, name, age, email, phone, department, year, admission_date, marks, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setInt(3, s.getAge());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setString(6, s.getDepartment());
            ps.setInt(7, s.getYear());
            ps.setDate(8, s.getAdmissionDate());
            ps.setDouble(9, s.getMarks());
            ps.setString(10, s.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY student_id";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Student> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM students WHERE name ILIKE ? ORDER BY name";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Student> searchByCourseName(String courseName) throws SQLException {
        String sql = "SELECT DISTINCT s.* FROM students s " +
                     "INNER JOIN enrollments e ON s.student_id = e.student_id " +
                     "INNER JOIN courses c ON c.course_id = e.course_id " +
                     "WHERE c.course_name ILIKE ? ORDER BY s.student_id";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + courseName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean updateStudent(Student s) throws SQLException {
        String sql = "UPDATE students SET name=?, age=?, email=?, phone=?, department=?, year=?, marks=?, status=? " +
                     "WHERE student_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getDepartment());
            ps.setInt(6, s.getYear());
            ps.setDouble(7, s.getMarks());
            ps.setString(8, s.getStatus());
            ps.setInt(9, s.getStudentId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Student> filterByDepartment(String dept) throws SQLException {
        String sql = "SELECT * FROM students WHERE department = ? ORDER BY student_id";
        return runFilter(sql, dept);
    }

    public List<Student> filterByYear(int year) throws SQLException {
        String sql = "SELECT * FROM students WHERE year = ? ORDER BY student_id";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Student> filterByCourse(int courseId) throws SQLException {
        String sql = "SELECT s.* FROM students s INNER JOIN enrollments e ON s.student_id = e.student_id " +
                     "WHERE e.course_id = ? ORDER BY s.student_id";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Student> studentsAboveMarks(double marks) throws SQLException {
        String sql = "SELECT * FROM students WHERE marks > ? ORDER BY marks DESC";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, marks);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Student> studentsInMarksRange(double low, double high) throws SQLException {
        String sql = "SELECT * FROM students WHERE marks BETWEEN ? AND ? ORDER BY marks DESC";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, low);
            ps.setDouble(2, high);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Student> sortStudents(String column, boolean ascending) throws SQLException {
        // column is restricted to a whitelist by the caller (StudentService), not raw user input,
        // so this is not string-concatenated from untrusted input.
        String sql = "SELECT * FROM students ORDER BY " + column + (ascending ? " ASC" : " DESC");
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<String[]> countByDepartment() throws SQLException {
        String sql = "SELECT department, COUNT(*) FROM students GROUP BY department ORDER BY department";
        return groupCount(sql);
    }

    public List<String[]> countByCourse() throws SQLException {
        String sql = "SELECT c.course_name, COUNT(e.student_id) FROM courses c " +
                     "LEFT JOIN enrollments e ON c.course_id = e.course_id " +
                     "GROUP BY c.course_name ORDER BY c.course_name";
        return groupCount(sql);
    }

    public double averageMarks() throws SQLException {
        String sql = "SELECT AVG(marks) FROM students";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0;
    }

    public Student highestScoring() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY marks DESC LIMIT 1";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Student lowestScoring() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY marks ASC LIMIT 1";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Student> studentsWithEnrolledCourses() throws SQLException {
        String sql = "SELECT DISTINCT s.* FROM students s INNER JOIN enrollments e ON s.student_id = e.student_id " +
                     "ORDER BY s.student_id";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private List<Student> runFilter(String sql, String param) throws SQLException {
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    private List<String[]> groupCount(String sql) throws SQLException {
        List<String[]> result = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new String[]{rs.getString(1), rs.getString(2)});
            }
        }
        return result;
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("department"),
                rs.getInt("year"),
                rs.getDate("admission_date"),
                rs.getDouble("marks"),
                rs.getString("status")
        );
    }
}
