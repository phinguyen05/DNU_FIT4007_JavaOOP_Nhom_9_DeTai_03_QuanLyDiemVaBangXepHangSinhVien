package com.StudentManager.model;

import java.text.DecimalFormat;

public class Grade {
    private String id;
    private String studentId;
    private String courseId;
    private double quizScore;
    private double midtermScore;
    private double projectScore;
    private double finalScore;

    public Grade(String id, String studentId, String courseId, double quizScore, double midtermScore, double projectScore, double finalScore) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.quizScore = quizScore;
        this.midtermScore = midtermScore;
        this.projectScore = projectScore;
        this.finalScore = finalScore;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public double getQuizScore() { return quizScore; }
    public void setQuizScore(double quizScore) { this.quizScore = quizScore; }
    public double getMidtermScore() { return midtermScore; }
    public void setMidtermScore(double midtermScore) { this.midtermScore = midtermScore; }
    public double getProjectScore() { return projectScore; }
    public void setProjectScore(double projectScore) { this.projectScore = projectScore; }
    public double getFinalScore() { return finalScore; }
    public void setFinalScore(double finalScore) { this.finalScore = finalScore; }

    // Tính điểm tổng kết hệ 10
    public double calculateFinalGrade10(Course course) {
        return (quizScore * course.getQuizWeight()) +
                (midtermScore * course.getMidtermWeight()) +
                (projectScore * course.getProjectWeight()) +
                (finalScore * course.getFinalWeight());
    }

    // Chuyển điểm hệ 10 sang hệ 4 để tính GPA
    public double getGradeIn4PointScale(Course course) {
        double grade10 = calculateFinalGrade10(course);
        if (grade10 >= 8.5) return 4.0;
        if (grade10 >= 8.0) return 3.7;
        if (grade10 >= 7.0) return 3.0;
        if (grade10 >= 6.5) return 2.7;
        if (grade10 >= 5.5) return 2.0;
        if (grade10 >= 5.0) return 1.7;
        if (grade10 >= 4.0) return 1.0;
        return 0.0;
    }

    // Chuyển điểm hệ 10 sang điểm chữ
    public String getGradeInLetter(Course course) {
        double grade10 = calculateFinalGrade10(course);
        if (grade10 >= 8.5) return "A";
        if (grade10 >= 8.0) return "B+";
        if (grade10 >= 7.0) return "B";
        if (grade10 >= 6.5) return "C+";
        if (grade10 >= 5.5) return "C";
        if (grade10 >= 5.0) return "D+";
        if (grade10 >= 4.0) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }

    public String toCsvString() {
        DecimalFormat df = new DecimalFormat("#.#");
        return String.join(",", id, studentId, courseId,
                df.format(quizScore), df.format(midtermScore),
                df.format(projectScore), df.format(finalScore));
    }
}

