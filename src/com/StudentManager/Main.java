package com.StudentManager;

import com.StudentManager.exception.CourseNotFoundException;
import com.StudentManager.exception.LecturerNotFoundException;
import com.StudentManager.exception.StudentNotFoundException;
import com.StudentManager.model.Course;
import com.StudentManager.model.Lecturer;
import com.StudentManager.model.Student;
import com.StudentManager.repository.DataStore;
import com.StudentManager.service.FileService;
import com.StudentManager.service.ManagementService;
import com.StudentManager.service.ReportingService;
import com.StudentManager.util.InputUtil;

import java.util.List;

public class Main {

    private static final DataStore dataStore = DataStore.getInstance();
    private static final FileService fileService = new FileService();
    private static final ManagementService managementService = new ManagementService(dataStore);
    private static final ReportingService reportingService = new ReportingService(dataStore);

    public static void main(String[] args) {
        // 1. Tải dữ liệu khi khởi động
        fileService.loadAllData();

        // 2. Chạy vòng lặp menu chính
        mainMenu();

        // 3. Lưu dữ liệu khi thoát
        fileService.saveAllData();
        System.out.println("Đã thoát chương trình.");
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n--- CHƯƠNG TRÌNH QUẢN LÝ ĐIỂM SINH VIÊN ---");
            System.out.println("1. Quản lý Sinh viên");
            System.out.println("2. Quản lý Giảng viên");
            System.out.println("3. Quản lý Học phần");
            System.out.println("4. Quản lý Điểm");
            System.out.println("5. Báo cáo & Thống kê");
            System.out.println("0. Lưu & Thoát");
            int choice = InputUtil.getInt("Chọn chức năng: ");

            switch (choice) {
                case 1 -> studentManagementMenu();
                case 2 -> lecturerManagementMenu();
                case 3 -> courseManagementMenu();
                case 4 -> gradeMenu();
                case 5 -> reportMenu();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void studentManagementMenu() {
        while (true) {
            System.out.println("\n--- MENU QUẢN LÝ SINH VIÊN ---");
            System.out.println("1. Thêm sinh viên mới");
            System.out.println("2. Chỉnh sửa thông tin sinh viên");
            System.out.println("3. Tìm sinh viên (theo Mã)");
            System.out.println("4. Tìm sinh viên (theo Tên)");
            System.out.println("0. Quay lại menu chính");
            int choice = InputUtil.getInt("Chọn chức năng: ");

            try {
                switch (choice) {
                    case 1 -> managementService.addNewStudent();
                    case 2 -> managementService.editStudent();
                    case 3 -> {
                        Student student = managementService.findStudentById(InputUtil.getString("Nhập mã SV: "));
                        System.out.println("Kết quả: " + student);
                    }
                    case 4 -> {
                        List<Student> students = managementService.findStudentsByName(InputUtil.getString("Nhập tên SV: "));
                        if (students.isEmpty()) {
                            System.out.println("Không tìm thấy sinh viên nào.");
                        } else {
                            students.forEach(System.out::println);
                        }
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ.");
                }
            } catch (StudentNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void lecturerManagementMenu() {
        while (true) {
            System.out.println("\n--- MENU QUẢN LÝ GIẢNG VIÊN ---");
            System.out.println("1. Thêm giảng viên mới");
            System.out.println("2. Chỉnh sửa thông tin giảng viên");
            System.out.println("3. Tìm giảng viên (theo Mã)");
            System.out.println("4. Tìm giảng viên (theo Tên)");
            System.out.println("0. Quay lại menu chính");
            int choice = InputUtil.getInt("Chọn chức năng: ");

            try {
                switch (choice) {
                    case 1 -> managementService.addNewLecturer();
                    case 2 -> managementService.editLecturer();
                    case 3 -> {
                        Lecturer lecturer = managementService.findLecturerById(InputUtil.getString("Nhập mã GV: "));
                        System.out.println("Kết quả: " + lecturer);
                    }
                    case 4 -> {
                        List<Lecturer> lecturers = managementService.findLecturersByName(InputUtil.getString("Nhập tên GV: "));
                        if (lecturers.isEmpty()) {
                            System.out.println("Không tìm thấy giảng viên nào.");
                        } else {
                            lecturers.forEach(System.out::println);
                        }
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ.");
                }
            } catch (LecturerNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void courseManagementMenu() {
        while (true) {
            System.out.println("\n--- MENU QUẢN LÝ HỌC PHẦN ---");
            System.out.println("1. Thêm học phần mới");
            System.out.println("2. Chỉnh sửa thông tin học phần");
            System.out.println("3. Xóa học phần");
            System.out.println("4. Tìm học phần (theo Mã)");
            System.out.println("5. Tìm học phần (theo Tên)");
            System.out.println("0. Quay lại menu chính");
            int choice = InputUtil.getInt("Chọn chức năng: ");

            try {
                switch (choice) {
                    case 1 -> managementService.addNewCourse();
                    case 2 -> managementService.editCourse();
                    case 3 -> managementService.deleteCourse();
                    case 4 -> {
                        Course course = managementService.findCourseById(InputUtil.getString("Nhập mã HP: "));
                        System.out.println("Kết quả: " + course);
                    }
                    case 5 -> {
                        List<Course> courses = managementService.findCoursesByName(InputUtil.getString("Nhập tên HP: "));
                        if (courses.isEmpty()) {
                            System.out.println("Không tìm thấy học phần nào.");
                        } else {
                            courses.forEach(System.out::println);
                        }
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ.");
                }
            } catch (CourseNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void gradeMenu() {
        System.out.println("\n--- MENU QUẢN LÝ ĐIỂM ---");
        System.out.println("Chức năng này cho phép nhập điểm mới hoặc cập nhật lại điểm (sửa điểm) nếu đã tồn tại.");
        System.out.println("1. Nhập/Cập nhật điểm học phần cho sinh viên");
        System.out.println("0. Quay lại menu chính");
        int choice = InputUtil.getInt("Chọn chức năng: ");

        if (choice == 1) {
            managementService.inputGrade();
        }
    }

    private static void reportMenu() {
        while (true) {
            System.out.println("\n--- MENU BÁO CÁO & THỐNG KÊ ---");
            System.out.println("1. In bảng điểm GPA toàn trường");
            System.out.println("2. In bảng xếp hạng theo Lớp");
            System.out.println("3. In bảng xếp hạng theo Khoa (Top 5)");
            System.out.println("4. Thống kê tỷ lệ xếp loại học lực");
            System.out.println("5. DS sinh viên học lại (trượt môn)");
            System.out.println("6. Xuất bảng xếp hạng toàn trường ra CSV");
            System.out.println("0. Quay lại menu chính");
            int choice = InputUtil.getInt("Chọn chức năng: ");

            switch (choice) {
                case 1 -> reportingService.printStudentListWithGpa();
                case 2 -> reportingService.printRankingByClass(InputUtil.getString("Nhập mã lớp (vd: C1): "));
                case 3 -> reportingService.printRankingByFaculty(InputUtil.getString("Nhập mã khoa (vd: F1): "));
                case 4 -> reportingService.printGradeDistribution();
                case 5 -> reportingService.printFailedStudentsByCourse(InputUtil.getString("Nhập mã học phần (vd: CRS101): "));
                case 6 -> reportingService.exportRankingToCsv();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }
}
