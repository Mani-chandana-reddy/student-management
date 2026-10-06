package com.chandana.sms.model;

import java.sql.Date;

public class Student {
    private int studentId;
    private String name;
    private int age;
    private String email;
    private String phone;
    private String department;
    private int year;
    private Date admissionDate;
    private double marks;
    private String status;

    public Student() {}

    public Student(int studentId, String name, int age, String email, String phone,
                    String department, int year, Date admissionDate, double marks, String status) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.year = year;
        this.admissionDate = admissionDate;
        this.marks = marks;
        this.status = status;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public Date getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(Date admissionDate) { this.admissionDate = admissionDate; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("ID:%-4d %-20s Age:%-3d Dept:%-6s Year:%-2d Marks:%-6.2f Status:%s",
                studentId, name, age, department, year, marks, status);
    }
}
