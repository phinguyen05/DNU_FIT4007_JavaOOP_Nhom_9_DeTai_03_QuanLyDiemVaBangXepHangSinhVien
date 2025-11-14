package com.StudentManager.service;

import com.StudentManager.model.*;
import com.StudentManager.repository.DataStore;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileService {
    private static final String DATA_DIR = "data/";
    private static final String STUDENTS_FILE = DATA_DIR + "students.csv";
    private static final String LECTURERS_FILE = DATA_DIR + "lecturers.csv";
    private static final String COURSES_FILE = DATA_DIR + "courses.csv";
    private static final String GRADES_FILE = DATA_DIR + "grades.csv";
    private static final String GROUPS_FILE = DATA_DIR + "class_groups.csv";

    private final DataStore dataStore;

    public FileService() {
        this.dataStore = DataStore.getInstance();
    }

    public void loadAllData() {
        loadStudents();
        loadLecturers();
        loadCourses();
        loadStudentGroups();
        loadGrades();
        System.out.println("Tải dữ liệu thành công!");
    }

    public void saveAllData() {
        saveStudents();
        saveLecturers();
        saveCourses();
        saveStudentGroups();
        saveGrades();
        System.out.println("Lưu dữ liệu thành công!");
    }

    // --- LOADERS ---

    private void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    dataStore.students.add(new Student(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file students.csv: " + e.getMessage());
        }
    }

    private void loadLecturers() {
        try (BufferedReader br = new BufferedReader(new FileReader(LECTURERS_FILE, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    dataStore.lecturers.add(new Lecturer(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file lecturers.csv: " + e.getMessage());
        }
    }

    private void loadCourses() {
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    dataStore.courses.add(new Course(parts[0], parts[1], Integer.parseInt(parts[2]),
                            Double.parseDouble(parts[3]), Double.parseDouble(parts[4]),
                            Double.parseDouble(parts[5]), Double.parseDouble(parts[6])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi khi đọc file courses.csv: " + e.getMessage());
        }
    }

    private void loadStudentGroups() {
        try (BufferedReader br = new BufferedReader(new FileReader(GROUPS_FILE, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String parentId = (parts.length == 4) ? parts[3] : null;
                    dataStore.studentGroups.add(new StudentGroup(parts[0], parts[1], GroupType.valueOf(parts[2]), parentId));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Lỗi khi đọc file class_groups.csv: " + e.getMessage());
        }
    }

    private void loadGrades() {
        try (BufferedReader br = new BufferedReader(new FileReader(GRADES_FILE, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    dataStore.grades.add(new Grade(parts[0], parts[1], parts[2],
                            Double.parseDouble(parts[3]), Double.parseDouble(parts[4]),
                            Double.parseDouble(parts[5]), Double.parseDouble(parts[6])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi khi đọc file grades.csv: " + e.getMessage());
        }
    }

    // --- SAVERS ---

    private <T extends Person> void saveData(List<T> list, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            for (T item : list) {
                bw.write(item.toCsvString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file " + filePath + ": " + e.getMessage());
        }
    }

    private void saveStudents() {
        saveData(dataStore.students, STUDENTS_FILE);
    }

    private void saveLecturers() {
        saveData(dataStore.lecturers, LECTURERS_FILE);
    }

    private void saveCourses() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COURSES_FILE, StandardCharsets.UTF_8))) {
            for (Course item : dataStore.courses) {
                bw.write(item.toCsvString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file " + COURSES_FILE + ": " + e.getMessage());
        }
    }

    private void saveStudentGroups() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(GROUPS_FILE, StandardCharsets.UTF_8))) {
            for (StudentGroup item : dataStore.studentGroups) {
                bw.write(item.toCsvString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file " + GROUPS_FILE + ": " + e.getMessage());
        }
    }

    private void saveGrades() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(GRADES_FILE, StandardCharsets.UTF_8))) {
            for (Grade item : dataStore.grades) {
                bw.write(item.toCsvString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file " + GRADES_FILE + ": " + e.getMessage());
        }
    }
}