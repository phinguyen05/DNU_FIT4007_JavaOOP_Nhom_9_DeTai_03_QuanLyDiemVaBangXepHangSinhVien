package com.StudentManager.model;

public class Lecturer extends Person {
    private String facultyId;

    public Lecturer(String id, String name, String dob, String facultyId) {
        super(id, name, dob);
        this.facultyId = facultyId;
    }

    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }

    @Override
    public String toCsvString() {
        return String.join(",", getId(), getName(), getDob(), facultyId);
    }
}
