package com.StudentManager.model;

public abstract class Person {
    private String id;
    private String name;
    private String dob; // Định dạng YYYY-MM-DD

    public Person(String id, String name, String dob) {
        this.id = id;
        this.name = name;
        this.dob = dob;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    @Override
    public String toString() {
        return "id='" + id + '\'' + ", name='" + name + '\'';
    }

    // Phương thức trừu tượng để lưu CSV
    public abstract String toCsvString();
}
