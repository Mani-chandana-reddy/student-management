package com.chandana.sms.service;

import com.chandana.sms.dao.CourseDAO;
import com.chandana.sms.model.Course;

import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO = new CourseDAO();

    public void addCourse(Course c) throws SQLException {
        if (c.getCourseId() <= 0) throw new IllegalArgumentException("Course ID must be greater than 0.");
        if (c.getCourseName() == null || c.getCourseName().trim().isEmpty())
            throw new IllegalArgumentException("Course name must not be empty.");
        if (c.getCredits() <= 0) throw new IllegalArgumentException("Credits must be valid.");
        if (courseDAO.getCourseById(c.getCourseId()) != null)
            throw new IllegalArgumentException("Course ID " + c.getCourseId() + " already exists.");
        courseDAO.insertCourse(c);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }

    public Course getCourseById(int id) throws SQLException {
        return courseDAO.getCourseById(id);
    }

    public List<Course> coursesWithEnrolledStudents() throws SQLException {
        return courseDAO.coursesWithEnrolledStudents();
    }
}
