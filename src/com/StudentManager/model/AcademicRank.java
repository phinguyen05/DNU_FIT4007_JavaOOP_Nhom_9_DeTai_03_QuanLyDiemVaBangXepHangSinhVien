package com.StudentManager.model;

public enum AcademicRank {
    XUAT_SAC("Xuất sắc"),
    GIOI("Giỏi"),
    KHA("Khá"),
    TRUNG_BINH("Trung bình"),
    YEU("Yếu"),
    CHUA_XEP_LOAI("Chưa xếp loại");

    private final String vietnameseName;

    AcademicRank(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String getVietnameseName() {
        return vietnameseName;
    }

    // Xếp loại dựa trên GPA hệ 4
    public static AcademicRank fromGpa(double gpa) {
        if (gpa >= 3.6) {
            return XUAT_SAC;
        } else if (gpa >= 3.2) {
            return GIOI;
        } else if (gpa >= 2.5) {
            return KHA;
        } else if (gpa >= 2.0) {
            return TRUNG_BINH;
        } else if (gpa >= 0) {
            return YEU;
        } else {
            return CHUA_XEP_LOAI;
        }
    }
}