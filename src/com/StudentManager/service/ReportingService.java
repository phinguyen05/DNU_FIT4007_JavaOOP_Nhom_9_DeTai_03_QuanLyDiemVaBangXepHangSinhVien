package com.StudentManager.service;

import com.StudentManager.model.*;
import com.StudentManager.repository.DataStore;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReportingService {
    private final DataStore dataStore;
    private final DecimalFormat df = new DecimalFormat("#.##");

    // Lớp nội bộ để lưu cặp SV-GPA, hỗ trợ sắp xếp
    private static class StudentGpaDto implements Comparable<StudentGpaDto> {
        Student student;
        double gpa;
        AcademicRank rank;

        StudentGpaDto(Student student, double gpa, AcademicRank rank) {
            this.student = student;
            this.gpa = gpa;
            this.rank = rank;
        }

        @Override
        public int compareTo(StudentGpaDto other) {
            // Sắp xếp giảm dần
            return Double.compare(other.gpa, this.gpa);
        }
    }

    public ReportingService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    // 1. Tính GPA cho một sinh viên
    public double calculateGpa(String studentId) {
        List<Grade> studentGrades = dataStore.grades.stream()
                .filter(g -> g.getStudentId().equals(studentId))
                .toList();

        if (studentGrades.isEmpty()) {
            return -1; // Chưa học môn nào
        }

        double totalWeightedPoints = 0;
        int totalCredits = 0;

        for (Grade grade : studentGrades) {
            Course course = dataStore.courses.stream()
                    .filter(c -> c.getId().equals(grade.getCourseId()))
                    .findFirst().orElse(null);

            if (course != null) {
                totalWeightedPoints += grade.getGradeIn4PointScale(course) * course.getCredits();
                totalCredits += course.getCredits();
            }
        }

        return (totalCredits == 0) ? 0 : (totalWeightedPoints / totalCredits);
    }

    // 2. Lấy danh sách SV và GPA
    private List<StudentGpaDto> getStudentGpaList(List<Student> studentList) {
        List<StudentGpaDto> gpaList = new ArrayList<>();
        for (Student student : studentList) {
            double gpa = calculateGpa(student.getId());
            AcademicRank rank = AcademicRank.fromGpa(gpa);
            gpaList.add(new StudentGpaDto(student, gpa, rank));
        }
        Collections.sort(gpaList); // Sắp xếp theo GPA giảm dần
        return gpaList;
    }

    // 3. In bảng điểm
    public void printStudentListWithGpa() {
        System.out.println("\n--- BẢNG ĐIỂM TOÀN TRƯỜNG ---");
        List<StudentGpaDto> gpaList = getStudentGpaList(dataStore.students);
        printRankingTable(gpaList);
    }

    // 4. In bảng xếp hạng theo Lớp
    public void printRankingByClass(String classId) {
        StudentGroup group = dataStore.studentGroups.stream()
                .filter(g -> g.getId().equalsIgnoreCase(classId) && g.getType() == GroupType.LOP)
                .findFirst().orElse(null);

        if (group == null) {
            System.err.println("Không tìm thấy lớp với mã: " + classId);
            return;
        }

        List<Student> classStudents = dataStore.students.stream()
                .filter(s -> s.getClassId().equalsIgnoreCase(classId))
                .toList();

        System.out.println("\n--- BẢNG XẾP HẠNG LỚP: " + group.getName() + " ---");
        List<StudentGpaDto> gpaList = getStudentGpaList(classStudents);
        printRankingTable(gpaList);
    }

    // 5. In bảng xếp hạng theo Khoa
    public void printRankingByFaculty(String facultyId) {
        StudentGroup group = dataStore.studentGroups.stream()
                .filter(g -> g.getId().equalsIgnoreCase(facultyId) && g.getType() == GroupType.KHOA)
                .findFirst().orElse(null);

        if (group == null) {
            System.err.println("Không tìm thấy khoa với mã: " + facultyId);
            return;
        }

        // Lấy danh sách các lớp thuộc khoa
        Set<String> classIdsInFaculty = dataStore.studentGroups.stream()
                .filter(g -> g.getType() == GroupType.LOP && facultyId.equals(g.getParentId()))
                .map(StudentGroup::getId)
                .collect(Collectors.toSet());

        // Lấy tất cả sinh viên thuộc các lớp đó
        List<Student> facultyStudents = dataStore.students.stream()
                .filter(s -> classIdsInFaculty.contains(s.getClassId()))
                .toList();

        System.out.println("\n--- BẢNG XẾP HẠNG KHOA: " + group.getName() + " (Top 5) ---");
        List<StudentGpaDto> gpaList = getStudentGpaList(facultyStudents);

        // Chỉ lấy top 5
        printRankingTable(gpaList.stream().limit(5).toList());
    }

    // 6. Thống kê tỷ lệ xếp loại
    public void printGradeDistribution() {
        System.out.println("\n--- THỐNG KÊ XẾP LOẠI HỌC LỰC ---");
        List<StudentGpaDto> gpaList = getStudentGpaList(dataStore.students);

        if (gpaList.isEmpty()) {
            System.out.println("Chưa có dữ liệu sinh viên.");
            return;
        }

        Map<AcademicRank, Long> rankCounts = gpaList.stream()
                .collect(Collectors.groupingBy(dto -> dto.rank, Collectors.counting()));

        int totalStudents = gpaList.size();

        for (AcademicRank rank : AcademicRank.values()) {
            long count = rankCounts.getOrDefault(rank, 0L);
            double percentage = (double) count / totalStudents * 100;
            System.out.printf("%-15s: %d sinh viên (%.2f%%)\n",
                    rank.getVietnameseName(), count, percentage);
        }
    }

    // 7. Báo cáo SV học lại (Điểm F)
    public void printFailedStudentsByCourse(String courseId) {
        Course course = dataStore.courses.stream()
                .filter(c -> c.getId().equalsIgnoreCase(courseId))
                .findFirst().orElse(null);

        if (course == null) {
            System.err.println("Không tìm thấy học phần: " + courseId);
            return;
        }

        System.out.println("\n--- DANH SÁCH HỌC LẠI MÔN: " + course.getName() + " ---");

        List<Grade> failedGrades = dataStore.grades.stream()
                .filter(g -> g.getCourseId().equalsIgnoreCase(courseId) && g.getGradeInLetter(course).equals("F"))
                .toList();

        if (failedGrades.isEmpty()) {
            System.out.println("Không có sinh viên nào trượt môn này.");
            return;
        }

        for (Grade grade : failedGrades) {
            Student student = dataStore.students.stream()
                    .filter(s -> s.getId().equals(grade.getStudentId()))
                    .findFirst().orElse(null);

            if (student != null) {
                System.out.printf("Mã SV: %-6s | Tên: %-20s | Điểm (10): %-4.2f (F)\n",
                        student.getId(), student.getName(), grade.calculateFinalGrade10(course));
            }
        }
    }

    // 8. Xuất báo cáo ra CSV (Test case 10)
    public void exportRankingToCsv() {
        String filePath = "data/ranking_report.csv";
        System.out.println("\nĐang xuất báo cáo ra " + filePath + "...");

        List<StudentGpaDto> gpaList = getStudentGpaList(dataStore.students);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            // Header
            bw.write("MaSV,TenSinhVien,MaLop,GPA,XepLoai");
            bw.newLine();

            // Data
            for (StudentGpaDto dto : gpaList) {
                String[] data = {
                        dto.student.getId(),
                        dto.student.getName(),
                        dto.student.getClassId(),
                        df.format(dto.gpa),
                        dto.rank.getVietnameseName()
                };
                bw.write(String.join(",", data));
                bw.newLine();
            }
            System.out.println("Xuất báo cáo thành công!");

        } catch (IOException e) {
            System.err.println("Lỗi khi xuất file CSV: " + e.getMessage());
        }
    }

    // Hàm trợ giúp in bảng
    private void printRankingTable(List<StudentGpaDto> gpaList) {
        if (gpaList.isEmpty()) {
            System.out.println("Không có dữ liệu để hiển thị.");
            return;
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("| %-4s | %-6s | %-25s | %-5s | %-12s |\n", "Hạng", "Mã SV", "Tên Sinh Viên", "GPA", "Xếp Loại");
        System.out.println("----------------------------------------------------------------------");

        int rank = 1;
        for (StudentGpaDto dto : gpaList) {
            System.out.printf("| %-4d | %-6s | %-25s | %-5s | %-12s |\n",
                    rank++,
                    dto.student.getId(),
                    dto.student.getName(),
                    df.format(dto.gpa),
                    dto.rank.getVietnameseName()
            );
        }
        System.out.println("----------------------------------------------------------------------");
    }
}