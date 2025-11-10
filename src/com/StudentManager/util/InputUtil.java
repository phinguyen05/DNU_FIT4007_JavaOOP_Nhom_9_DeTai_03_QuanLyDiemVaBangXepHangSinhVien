package com.StudentManager.util;

import com.StudentManager.exception.InvalidScoreException;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int getInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(getString(prompt));
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập một số nguyên. Thử lại.");
            }
        }
    }

    public static double getDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(getString(prompt).replace(",", "."));
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập một số. Thử lại.");
            }
        }
    }

    // Kiểm tra điểm hợp lệ (yêu cầu test case 2)
    public static double getScore(String prompt) throws InvalidScoreException {
        double score = getDouble(prompt);
        if (score < 0.0 || score > 10.0) {
            throw new InvalidScoreException("Điểm không hợp lệ: " + score + ". Điểm phải nằm trong khoảng [0, 10].");
        }
        return score;
    }
}