# DNU_FIT4007_JavaOOP_Nhom_9_DeTai_03_QuanLyDiemVaBangXepHangSinhVien
# DNU_FIT4007_JavaOOP_Nhom_9_DeTai_03_QuanLyDiemVaBangXepHangSinhVien

Hướng dẫn chạy ứng dụng
- Yêu cầu:
    - Java Development Kit (JDK): đảm bảo bạn đã cài đặt JDK (phiên bản 11 trở lên).
    - IDE (Tùy chọn): một trình soạn thảo code như IntelliJ IDEA, Eclipse, hoặc VS Code sẽ giúp việc chạy ứng dụng dễ dàng hơn.
- Cấu trúc thư mục
    - Ứng dụng này yêu cầu một cấu trúc thư mục cụ thể để đọc và ghi dữ liệu. Bạn bắt buộc phải tạo một thư mục tên là data ở thư mục gốc của dự án (ngang hàng với thư mục src).
- Cách 1: Chạy bằng IDE (Khuyên dùng)
    - Mở Dự án: mở thư mục gốc của dự án (ví dụ: StudentManagerProject/) bằng IDE của bạn (IntelliJ, Eclipse, VS Code, v.v.).
    - Tạo thư mục data:
        - Nhấn chuột phải vào thư mục gốc của dự án trong cây thư mục (thường là thư mục trên cùng).
        - Chọn New -> Directory.
        - Đặt tên thư mục là data.
    - Thêm file CSV: sao chép 5 file .csv ( class_groups.csv, courses.csv, grades.csv, lecturers.csv, students.csv) vào thư mục data bạn vừa tạo.
    - Chạy ứng dụng:
        - Tìm file Main.java (trong src/com/StudentManager/).
        - Nhấn chuột phải vào file và chọn Run 'Main.main()'.
    - Kiểm tra: ứng dụng sẽ khởi động trên terminal của IDE và hiển thị menu chính. Mọi dữ liệu (bao gồm cả file ranking_report.csv khi xuất báo cáo) sẽ được đọc/ghi từ thư mục data.
- Cách 2: Chạy bằng Dòng lệnh (Terminal)
    - Mở Terminal: mở Terminal (macOS/Linux) hoặc Command Prompt/PowerShell (Windows).
    - Di chuyển đến thư mục dự án: dùng lệnh cd để di chuyển đến thư mục gốc của dự án.
        -     cd /đường/dẫn/đến/StudentManagerProject
    - Tạo thư mục data:
        -     mkdir data
    - Thêm file CSV: Đảm bảo 5 file .csv đã được sao chép vào thư mục data này.
    - Tạo thư mục out (để lưu file đã biên dịch):
        -     mkdir out
    - Biên dịch (Compile) code Java:
        - Chúng ta cần biên dịch tất cả các file .java từ thư mục src và lưu file .class vào thư mục out.
        - Sử dụng cờ -encoding UTF-8 để đảm bảo các ký tự tiếng Việt được xử lý đúng.
        -     javac -encoding UTF-8 -d out -sourcepath src src/com/StudentManager/Main.java
    - Chạy (Run) ứng dụng:
        - Sử dụng cờ -cp out (classpath) để chỉ cho Java biết nơi tìm các file .class.
        - Sử dụng cờ -Dfile.encoding=UTF-8 để đảm bảo terminal hiển thị đúng tiếng Việt.
        - Gọi tên class chính (bao gồm cả package): com.StudentManager.Main.
    -     java -Dfile.encoding=UTF-8 -cp out com.StudentManager.Main
    - Sử dụng: Terminal sẽ hiển thị menu chính và bạn có thể bắt đầu sử dụng ứng dụng.
