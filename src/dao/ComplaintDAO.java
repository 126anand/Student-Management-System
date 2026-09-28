package dao;

import model.Complaint;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    // =========================================================
    // ADD COMPLAINT
    // =========================================================
    public void addComplaint(Complaint complaint) {

        if (complaint.getSubject() == null
                || complaint.getSubject().trim().isEmpty()) {

            System.out.println("Complaint subject cannot be empty.");
            return;
        }

        if (complaint.getDescription() == null
                || complaint.getDescription().trim().isEmpty()) {

            System.out.println("Complaint description cannot be empty.");
            return;
        }

        java.sql.Date date = parseDate(complaint.getComplaintDate());

        if (date == null) {
            System.out.println(
                    "Invalid date. Please use the format YYYY-MM-DD."
            );
            return;
        }

        String sql =
                "INSERT INTO complaints " +
                        "(student_id, subject, description, complaint_date, status) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, complaint.getStudentId());
            statement.setString(2, complaint.getSubject());
            statement.setString(3, complaint.getDescription());
            statement.setDate(4, date);
            statement.setString(5, "Pending");

            statement.executeUpdate();

            System.out.println("Complaint submitted successfully!");

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Student ID does not exist.");
                return;
            }
            System.out.println("Unable to submit complaint.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // VIEW ALL COMPLAINTS
    // =========================================================
    public List<Complaint> getAllComplaints() {

        List<Complaint> complaints = new ArrayList<>();

        String sql =
                "SELECT c.*, s.name AS student_name FROM complaints c " +
                        "JOIN students s ON c.student_id = s.id " +
                        "ORDER BY c.complaint_date DESC, c.id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                complaints.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve complaints.");
            System.out.println("Database error: " + e.getMessage());
        }

        return complaints;
    }


    // =========================================================
    // VIEW COMPLAINTS BY STUDENT
    // =========================================================
    public List<Complaint> getComplaintsByStudentId(int studentId) {

        List<Complaint> complaints = new ArrayList<>();

        String sql =
                "SELECT c.*, s.name AS student_name FROM complaints c " +
                        "JOIN students s ON c.student_id = s.id " +
                        "WHERE c.student_id = ? " +
                        "ORDER BY c.complaint_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                complaints.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve complaints for student.");
            System.out.println("Database error: " + e.getMessage());
        }

        return complaints;
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
    // MAP RESULT SET ROW TO COMPLAINT OBJECT
    // =========================================================
    private Complaint mapRow(ResultSet resultSet) throws SQLException {

        Complaint complaint = new Complaint();

        complaint.setId(resultSet.getInt("id"));
        complaint.setStudentId(resultSet.getInt("student_id"));
        complaint.setStudentName(resultSet.getString("student_name"));
        complaint.setSubject(resultSet.getString("subject"));
        complaint.setDescription(resultSet.getString("description"));
        complaint.setComplaintDate(
                String.valueOf(resultSet.getDate("complaint_date"))
        );
        complaint.setStatus(resultSet.getString("status"));

        return complaint;
    }
}
