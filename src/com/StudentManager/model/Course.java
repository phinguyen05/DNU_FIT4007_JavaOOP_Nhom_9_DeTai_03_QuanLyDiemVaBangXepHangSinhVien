package com.StudentManager.model;

public class Course {
    private String id;
    private String name;
    private int credits;
    private double quizWeight;
    private double midtermWeight;
    private double projectWeight;
    private double finalWeight;

    public Course(String id, String name, int credits, double quizWeight, double midtermWeight, double projectWeight, double finalWeight) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.quizWeight = quizWeight;
        this.midtermWeight = midtermWeight;
        this.projectWeight = projectWeight;
        this.finalWeight = finalWeight;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public double getQuizWeight() { return quizWeight; }
    public void setQuizWeight(double quizWeight) { this.quizWeight = quizWeight; }
    public double getMidtermWeight() { return midtermWeight; }
    public void setMidtermWeight(double midtermWeight) { this.midtermWeight = midtermWeight; }
    public double getProjectWeight() { return projectWeight; }
    public void setProjectWeight(double projectWeight) { this.projectWeight = projectWeight; }
    public double getFinalWeight() { return finalWeight; }
    public void setFinalWeight(double finalWeight) { this.finalWeight = finalWeight; }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                '}';
    }

    // Dùng để lưu file CSV
    public String toCsvString() {
        return String.join(",", id, name, String.valueOf(credits),
                String.valueOf(quizWeight), String.valueOf(midtermWeight),
                String.valueOf(projectWeight), String.valueOf(finalWeight));
    }
}