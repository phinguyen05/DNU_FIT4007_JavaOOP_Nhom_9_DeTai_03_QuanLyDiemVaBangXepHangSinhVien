package com.StudentManager.model;

public class Student extends Person {
    private String classId;

    public Student(String id, String name, String dob, String classId) {
        super(id, name, dob);
        this.classId = classId;
    }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    @Override
    public String toCsvString() {
        return String.join(",", getId(), getName(), getDob(), classId);
    }
}
