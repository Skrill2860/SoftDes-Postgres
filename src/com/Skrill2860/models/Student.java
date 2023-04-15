package com.Skrill2860.models;

public class Student {
    private Long id;
    private String firstname;
    private String lastname;
    private boolean isPresent;
    private boolean hasAnswered;
    private int grade;

    public Student(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.isPresent = false;
        this.hasAnswered = false;
        this.grade = 0;
    }

    public Student(Long id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isPresent = false;
        this.hasAnswered = false;
        this.grade = 0;
    }

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean hasAnswered() {
        return hasAnswered;
    }

    public void setAnswered(boolean answered) {
        this.hasAnswered = answered;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    @Override
    public String toString() {
        return id + " " + firstname + " " + lastname + " " + (isPresent ? "присутствовал(а), оценка: " + grade : "не присутствовал(а)");
    }
}
