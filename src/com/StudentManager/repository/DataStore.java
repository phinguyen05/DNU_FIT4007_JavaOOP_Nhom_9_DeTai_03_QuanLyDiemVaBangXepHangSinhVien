package com.StudentManager.repository;

import com.StudentManager.model.*;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;

    public List<Student> students;
    public List<Lecturer> lecturers;
    public List<Course> courses;
    public List<Grade> grades;
    public List<StudentGroup> studentGroups;

    private DataStore() {
        students = new ArrayList<>();
        lecturers = new ArrayList<>();
        courses = new ArrayList<>();
        grades = new ArrayList<>();
        studentGroups = new ArrayList<>();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
}
