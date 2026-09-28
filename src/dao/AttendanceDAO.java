package dao;

import model.Attendance;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // =========================================================
    // MARK / ADD ATTENDANCE
    // =========================================================
    public void addAttendance(Attendance attendance) {

        if (attendance.getStatus() == null
                || attendance.getStatus().trim().isEmpty()) {

            System.out.println("Attendance status cannot be empty.");
            return;
        }

        String status = attendance.getStatus().trim();

        if (status.equalsIgnoreCase("present")) {
            status = "Present";
        } else if (status.equalsIgnoreCase("absent")) {
            status = "Absent";
        } else if (status.equalsIgnoreCase("late")) {
            status = "Late";
        } else {
            System.out.println(
                    "Status must be Present, Absent, or Late."
            );
            return;
        }

        java.sql.Date date = parseDate(attendance.getAttendanceDate());

        if (date == null) {
            System.out.println(
                    "Invalid date. Please use the format YYYY-MM-DD."
            );
            return;
        }

        String duplicateRecord =
                "SELECT id FROM attendance " +
                        "WHERE student_id = ? AND course_id = ? AND attendance_date = ?";

        String sql =
                "INSERT INTO attendance " +
                        "(student_id, course_id, attendance_date, status) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateRecord)) {

                statement.setInt(1, attendance.getStudentId());
                statement.setInt(2, attendance.getCourseId());
                statement.setDate(3, date);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println(
                            "Attendance for this student, course, and date " +
                                    "already exists."
                    );
                    return;
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(1, attendance.getStudentId());
                statement.setInt(2, attendance.getCourseId());
                statement.setDate(3, date);
                statement.setString(4, status);

                statement.executeUpdate();

                System.out.println("Attendance recorded successfully!");
            }

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Student ID or course ID does not exist.");
                return;
            }
            System.out.println("Unable to record attendance.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // VIEW ALL ATTENDANCE RECORDS
    // =========================================================
    public List<Attendance> getAllAttendance() {

        List<Attendance> attendanceList = new ArrayList<>();

        String sql =
                "SELECT a.*, s.name AS student_name, c.course_name " +
                        "FROM attendance a " +
                        "JOIN students s ON a.student_id = s.id " +
                        "JOIN courses c ON a.course_id = c.id " +
                        "ORDER BY a.attendance_date DESC, a.id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                attendanceList.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve attendance records.");
            System.out.println("Database error: " + e.getMessage());
        }

        return attendanceList;
    }


    // =========================================================
    // VIEW ATTENDANCE BY STUDENT
    // =========================================================
    public List<Attendance> getAttendanceByStudentId(int studentId) {

        List<Attendance> attendanceList = new ArrayList<>();

        String sql =
                "SELECT a.*, s.name AS student_name, c.course_name " +
                        "FROM attendance a " +
                        "JOIN students s ON a.student_id = s.id " +
                        "JOIN courses c ON a.course_id = c.id " +
                        "WHERE a.student_id = ? " +
                        "ORDER BY a.attendance_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                attendanceList.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve attendance for student.");
            System.out.println("Database error: " + e.getMessage());
        }

        return attendanceList;
    }


    // =========================================================
    // DELETE ATTENDANCE RECORD
    // =========================================================
    public void deleteAttendance(int id) {

        String sql = "DELETE FROM attendance WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Attendance record deleted successfully!");
            } else {
                System.out.println("Attendance record not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to delete attendance record.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // ATTENDANCE PERCENTAGE REPORT FOR A STUDENT IN A COURSE
    // =========================================================
    public void getAttendancePercentage(int studentId, int courseId) {

        String sql =
                "SELECT " +
                        "COUNT(*) AS total_classes, " +
                        "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) " +
                        "AS present_count " +
                        "FROM attendance " +
                        "WHERE student_id = ? AND course_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                int totalClasses = resultSet.getInt("total_classes");
                int presentCount = resultSet.getInt("present_count");

                if (totalClasses == 0) {

                    System.out.println(
                            "No attendance records found for this student " +
                                    "in this course."
                    );

                } else {

                    double percentage =
                            (presentCount * 100.0) / totalClasses;

                    System.out.println("Total Classes: " + totalClasses);
                    System.out.println("Classes Present: " + presentCount);

                    System.out.printf(
                            "Attendance Percentage: %.2f%%%n",
                            percentage
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to calculate attendance percentage.");
            System.out.println("Database error: " + e.getMessage());
        }
    }



    // =========================================================
    // CONVERT TEXT (YYYY-MM-DD) TO SQL DATE
    // =========================================================
    private java.sql.Date parseDate(String text) {

        try {
            return java.sql.Date.valueOf(
                    java.time.LocalDate.parse(text.trim())
            );
        } catch (Exception e) {
            return null;
        }
    }


    // =========================================================
    // MAP RESULT SET ROW TO ATTENDANCE OBJECT
    // =========================================================
    private Attendance mapRow(ResultSet resultSet) throws SQLException {

        Attendance attendance = new Attendance();

        attendance.setId(resultSet.getInt("id"));
        attendance.setStudentId(resultSet.getInt("student_id"));
        attendance.setStudentName(resultSet.getString("student_name"));
        attendance.setCourseId(resultSet.getInt("course_id"));
        attendance.setCourseName(resultSet.getString("course_name"));
        attendance.setAttendanceDate(
                String.valueOf(resultSet.getDate("attendance_date"))
        );
        attendance.setStatus(resultSet.getString("status"));

        return attendance;
    }
}
