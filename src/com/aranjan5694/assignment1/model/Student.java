package com.aranjan5694.assignment1.model;

/**
 * A Student here has a name, redid and a GPA.
 *
 * @author Abhishek Ranjan <aranjan5694@sdsu.edu>
 */
public class Student implements Comparable<Student> {
    private Integer redId;
    private String name;
    private double gpa;

    public Student(Integer redId, String name, double gpa) {
        this.redId = redId;
        this.name = name;
        this.gpa = gpa;
    }

    public Integer getRedId() {
        return redId;
    }

    public void setRedId(Integer redId) {
        this.redId = redId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public int compareTo(Student o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "Student{" +
                "redId=" + redId +
                ", name='" + name + '\'' +
                ", gpa=" + gpa +
                '}';
    }
}
