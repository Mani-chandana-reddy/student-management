package com.chandana.sms.service;

import com.chandana.sms.dao.StudentDAO;
import com.chandana.sms.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class StudentService {
    private final StudentDAO studentDAO = new StudentDAO();
    private static final Set<String> SORTABLE_COLUMNS = Set.of("student_id", "name", "marks");

    public void addStudent(Student s) throws SQLException {
        validate(s);
        if (studentDAO.getStudentById(s.getStudentId()) != null) {
            throw new IllegalArgumentException("Student ID " + s.getStudentId() + " already exists.");
        }
        studentDAO.insertStudent(s);
    }


    private void validate(Student s) {
        if (s.getStudentId() <= 0) throw new IllegalArgumentException("Student ID must be greater than 0.");
        if (s.getName() == null || s.getName().trim().isEmpty()) throw new IllegalArgumentException("Name must not be empty.");
        if (s.getAge() <= 0 || s.getAge() > 100) throw new IllegalArgumentException("Age is invalid.");
        if (s.getEmail() == null || s.getEmail().trim().isEmpty()) throw new IllegalArgumentException("Email must not be empty.");
        if (s.getDepartment() == null || s.getDepartment().trim().isEmpty()) throw new IllegalArgumentException("Department must not be empty.");
        if (s.getYear() < 1 || s.getYear() > 6) throw new IllegalArgumentException("Year is invalid.");
        if (s.getMarks() < 0 || s.getMarks() > 100) throw new IllegalArgumentException("Marks must be between 0 and 100.");
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public Student getStudentById(int id) throws SQLException {
        return studentDAO.getStudentById(id);
    }

    public List<Student> searchByName(String name) throws SQLException {
        return studentDAO.searchByName(name);
    }

    public void updateStudent(Student s) throws SQLException {
        validate(s);
        if (studentDAO.getStudentById(s.getStudentId()) == null) {
            throw new IllegalArgumentException("Student ID " + s.getStudentId() + " does not exist.");
        }
        studentDAO.updateStudent(s);
    }

    public void deleteStudent(int id) throws SQLException {
        if (studentDAO.getStudentById(id) == null) {
            throw new IllegalArgumentException("Student ID " + id + " does not exist.");
        }
        studentDAO.deleteStudent(id);
    }

    public List<Student> filterByDepartment(String dept) throws SQLException {
        return studentDAO.filterByDepartment(dept);
    }

    public List<Student> filterByYear(int year) throws SQLException {
        return studentDAO.filterByYear(year);
    }

    public List<Student> filterByCourse(int courseId) throws SQLException {
        return studentDAO.filterByCourse(courseId);
    }

    public List<Student> studentsAboveMarks(double marks) throws SQLException {
        return studentDAO.studentsAboveMarks(marks);
    }

    public List<Student> studentsInMarksRange(double low, double high) throws SQLException {
        return studentDAO.studentsInMarksRange(low, high);
    }

    public List<Student> sortStudents(String column, boolean ascending) throws SQLException {
        String col = SORTABLE_COLUMNS.contains(column) ? column : "student_id";
        return studentDAO.sortStudents(col, ascending);
    }

    public List<String[]> countByDepartment() throws SQLException {
        return studentDAO.countByDepartment();
    }

    public List<String[]> countByCourse() throws SQLException {
        return studentDAO.countByCourse();
    }

    public double averageMarks() throws SQLException {
        return studentDAO.averageMarks();
    }

    public Student highestScoring() throws SQLException {
        return studentDAO.highestScoring();
    }

    public Student lowestScoring() throws SQLException {
        return studentDAO.lowestScoring();
    }
}
