package com.StudentManager.service;

import com.StudentManager.exception.CourseNotFoundException;
import com.StudentManager.exception.InvalidScoreException;
import com.StudentManager.exception.LecturerNotFoundException;
import com.StudentManager.exception.StudentNotFoundException;
import com.StudentManager.model.*;
import com.StudentManager.repository.DataStore;
import com.StudentManager.util.InputUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ManagementService {
    private final DataStore dataStore;

    public ManagementService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    // --- Quản lý Sinh viên ---
    public void addNewStudent() {
        String id = "S" + (dataStore.students.size() + 101); // ID tự tăng đơn giản
        String name = InputUtil.getString("Nhập tên sinh viên: ");
        String dob = InputUtil.getString("Nhập ngày sinh (YYYY-MM-DD): ");
        String classId = InputUtil.getString("Nhập mã lớp: ");

        // Kiểm tra classId có tồn tại không
        boolean classExists = dataStore.studentGroups.stream()
                .anyMatch(g -> g.getId().equalsIgnoreCase(classId) && g.getType() == GroupType.LOP);
        if (!classExists) {
            System.err.println("Cảnh báo: Mã lớp '" + classId + "' không tồn tại trong danh sách lớp.");
        }

        Student student = new Student(id, name, dob, classId);
        dataStore.students.add(student);
        System.out.println("Đã thêm sinh viên: " + student);
    }

    public void editStudent() throws StudentNotFoundException {
        String id = InputUtil.getString("Nhập mã sinh viên cần sửa: ");
        Student student = findStudentById(id);

        System.out.println("Đang sửa thông tin cho: " + student.getName());
        System.out.println("Nhấn Enter để giữ nguyên giá trị cũ.");

        String name = InputUtil.getString("Tên mới (" + student.getName() + "): ");
        String dob = InputUtil.getString("Ngày sinh mới (" + student.getDob() + "): ");
        String classId = InputUtil.getString("Mã lớp mới (" + student.getClassId() + "): ");

        if (!name.isEmpty()) {
            student.setName(name);
        }
        if (!dob.isEmpty()) {
            student.setDob(dob);
        }
        if (!classId.isEmpty()) {
            // Kiểm tra classId mới
            boolean classExists = dataStore.studentGroups.stream()
                    .anyMatch(g -> g.getId().equalsIgnoreCase(classId) && g.getType() == GroupType.LOP);
            if (!classExists) {
                System.err.println("Cảnh báo: Mã lớp '" + classId + "' không tồn tại. Giữ nguyên mã lớp cũ.");
            } else {
                student.setClassId(classId);
            }
        }

        System.out.println("Cập nhật thông tin sinh viên thành công!");
    }

    public Student findStudentById(String id) throws StudentNotFoundException {
        return dataStore.students.stream()
                .filter(s -> s.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Không tìm thấy sinh viên với mã: " + id));
    }

    public List<Student> findStudentsByName(String name) {
        return dataStore.students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // --- Quản lý Giảng viên ---
    public void addNewLecturer() {
        String id = "L" + (dataStore.lecturers.size() + 101);
        String name = InputUtil.getString("Nhập tên giảng viên: ");
        String dob = InputUtil.getString("Nhập ngày sinh (YYYY-MM-DD): ");
        String facultyId = InputUtil.getString("Nhập mã khoa: ");

        // Kiểm tra facultyId
        boolean facultyExists = dataStore.studentGroups.stream()
                .anyMatch(g -> g.getId().equalsIgnoreCase(facultyId) && g.getType() == GroupType.KHOA);
        if (!facultyExists) {
            System.err.println("Cảnh báo: Mã khoa '" + facultyId + "' không tồn tại.");
        }

        Lecturer lecturer = new Lecturer(id, name, dob, facultyId);
        dataStore.lecturers.add(lecturer);
        System.out.println("Đã thêm giảng viên: " + lecturer);
    }

    public void editLecturer() throws LecturerNotFoundException {
        String id = InputUtil.getString("Nhập mã giảng viên cần sửa: ");
        Lecturer lecturer = findLecturerById(id);

        System.out.println("Đang sửa thông tin cho: " + lecturer.getName());
        System.out.println("Nhấn Enter để giữ nguyên giá trị cũ.");

        String name = InputUtil.getString("Tên mới (" + lecturer.getName() + "): ");
        String dob = InputUtil.getString("Ngày sinh mới (" + lecturer.getDob() + "): ");
        String facultyId = InputUtil.getString("Mã khoa mới (" + lecturer.getFacultyId() + "): ");

        if (!name.isEmpty()) {
            lecturer.setName(name);
        }
        if (!dob.isEmpty()) {
            lecturer.setDob(dob);
        }
        if (!facultyId.isEmpty()) {
            // Kiểm tra facultyId mới
            boolean facultyExists = dataStore.studentGroups.stream()
                    .anyMatch(g -> g.getId().equalsIgnoreCase(facultyId) && g.getType() == GroupType.KHOA);
            if (!facultyExists) {
                System.err.println("Cảnh báo: Mã khoa '" + facultyId + "' không tồn tại. Giữ nguyên mã khoa cũ.");
            } else {
                lecturer.setFacultyId(facultyId);
            }
        }

        System.out.println("Cập nhật thông tin giảng viên thành công!");
    }

    public Lecturer findLecturerById(String id) throws LecturerNotFoundException {
        return dataStore.lecturers.stream()
                .filter(l -> l.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new LecturerNotFoundException("Không tìm thấy giảng viên với mã: " + id));
    }

    public List<Lecturer> findLecturersByName(String name) {
        return dataStore.lecturers.stream()
                .filter(l -> l.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // --- Quản lý Học phần ---
    public void addNewCourse() {
        String id = "CRS" + (dataStore.courses.size() + 101);
        String name = InputUtil.getString("Nhập tên học phần: ");
        int credits = InputUtil.getInt("Nhập số tín chỉ: ");

        double quizW, midtermW, projectW, finalW;
        while (true) {
            quizW = InputUtil.getDouble("Nhập trọng số Quiz (vd: 0.1): ");
            midtermW = InputUtil.getDouble("Nhập trọng số Giữa kỳ (vd: 0.3): ");
            projectW = InputUtil.getDouble("Nhập trọng số Bài tập lớn (vd: 0.2): ");
            finalW = InputUtil.getDouble("Nhập trọng số Cuối kỳ (vd: 0.4): ");

            double totalWeight = quizW + midtermW + projectW + finalW;
            if (Math.abs(totalWeight - 1.0) < 0.001) { // So sánh số thực
                break;
            } else {
                System.err.println("Tổng trọng số (" + totalWeight + ") phải bằng 1.0. Vui lòng nhập lại.");
            }
        }

        Course course = new Course(id, name, credits, quizW, midtermW, projectW, finalW);
        dataStore.courses.add(course);
        System.out.println("Đã thêm học phần: " + course);
    }

    public void editCourse() throws CourseNotFoundException {
        String id = InputUtil.getString("Nhập mã học phần cần sửa: ");
        Course course = findCourseById(id);

        System.out.println("Đang sửa thông tin cho: " + course.getName());
        System.out.println("Nhấn Enter để giữ nguyên giá trị cũ. Nhập -1 cho số/trọng số để giữ nguyên.");

        String name = InputUtil.getString("Tên mới (" + course.getName() + "): ");
        int credits = InputUtil.getInt("Số tín chỉ mới (" + course.getCredits() + "): ");
        double quizW = InputUtil.getDouble("Trọng số Quiz mới (" + course.getQuizWeight() + "): ");
        double midtermW = InputUtil.getDouble("Trọng số Giữa kỳ mới (" + course.getMidtermWeight() + "): ");
        double projectW = InputUtil.getDouble("Trọng số BTL mới (" + course.getProjectWeight() + "): ");
        double finalW = InputUtil.getDouble("Trọng số Cuối kỳ mới (" + course.getFinalWeight() + "): ");

        if (!name.isEmpty()) {
            course.setName(name);
        }
        if (credits != -1) {
            course.setCredits(credits);
        }

        // Chỉ cập nhật trọng số nếu TỔNG trọng số mới hợp lệ
        if (quizW != -1 || midtermW != -1 || projectW != -1 || finalW != -1) {
            double nQuiz = (quizW == -1) ? course.getQuizWeight() : quizW;
            double nMid = (midtermW == -1) ? course.getMidtermWeight() : midtermW;
            double nProj = (projectW == -1) ? course.getProjectWeight() : projectW;
            double nFinal = (finalW == -1) ? course.getFinalWeight() : finalW;

            double totalWeight = nQuiz + nMid + nProj + nFinal;
            if (Math.abs(totalWeight - 1.0) < 0.001) {
                course.setQuizWeight(nQuiz);
                course.setMidtermWeight(nMid);
                course.setProjectWeight(nProj);
                course.setFinalWeight(nFinal);
                System.out.println("Đã cập nhật trọng số.");
            } else {
                System.err.println("Tổng trọng số mới (" + totalWeight + ") không bằng 1.0. Trọng số KHÔNG được cập nhật.");
            }
        }

        System.out.println("Cập nhật thông tin học phần thành công!");
    }

    public void deleteCourse() throws CourseNotFoundException {
        String id = InputUtil.getString("Nhập mã học phần cần xóa: ");
        Course course = findCourseById(id);

        // Kiểm tra ràng buộc: Không xóa học phần nếu đã có điểm
        boolean hasGrades = dataStore.grades.stream()
                .anyMatch(g -> g.getCourseId().equalsIgnoreCase(id));

        if (hasGrades) {
            System.err.println("Không thể xóa học phần '" + course.getName() + "' vì đã có sinh viên được nhập điểm cho môn này.");
            System.err.println("Vui lòng xóa các bản ghi điểm liên quan trước.");
            return;
        }

        dataStore.courses.remove(course);
        System.out.println("Đã xóa học phần: " + course.getName());
    }

    public Course findCourseById(String id) throws CourseNotFoundException {
        return dataStore.courses.stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("Không tìm thấy học phần với mã: " + id));
    }

    public List<Course> findCoursesByName(String name) {
        return dataStore.courses.stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // --- Quản lý Điểm ---
    public void inputGrade() {
        try {
            String studentId = InputUtil.getString("Nhập mã sinh viên: ");
            Student student = findStudentById(studentId);
            System.out.println("Sinh viên: " + student.getName());

            String courseId = InputUtil.getString("Nhập mã học phần: ");
            Course course = findCourseById(courseId);
            System.out.println("Học phần: " + course.getName());

            System.out.println("Nhập điểm (0-10).");
            double quiz = (course.getQuizWeight() > 0) ? InputUtil.getScore("Nhập điểm Quiz: ") : 0;
            double midterm = (course.getMidtermWeight() > 0) ? InputUtil.getScore("Nhập điểm Giữa kỳ: ") : 0;
            double project = (course.getProjectWeight() > 0) ? InputUtil.getScore("Nhập điểm Bài tập lớn: ") : 0;
            double finalExam = (course.getFinalWeight() > 0) ? InputUtil.getScore("Nhập điểm Cuối kỳ: ") : 0;

            String gradeId = "G" + UUID.randomUUID().toString().substring(0, 4);
            Grade grade = new Grade(gradeId, studentId, courseId, quiz, midterm, project, finalExam);

            // Kiểm tra và ghi đè nếu điểm đã tồn tại (Thực hiện yêu cầu "Sửa điểm")
            dataStore.grades.removeIf(g -> g.getStudentId().equals(studentId) && g.getCourseId().equals(courseId));
            dataStore.grades.add(grade);

            double finalGrade10 = grade.calculateFinalGrade10(course);
            System.out.printf("Đã nhập/cập nhật điểm. Điểm tổng kết (hệ 10): %.2f (%s)\n",
                    finalGrade10, grade.getGradeInLetter(course));

        } catch (StudentNotFoundException | CourseNotFoundException | InvalidScoreException e) {
            System.err.println(e.getMessage());
        }
    }
}