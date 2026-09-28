package dao;

import model.Fee;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeeDAO {

    // =========================================================
    // ADD FEE RECORD
    // =========================================================
    public void addFee(Fee fee) {

        if (fee.getAmount() <= 0) {
            System.out.println("Fee amount must be greater than 0.");
            return;
        }

        if (fee.getDueDate() == null || fee.getDueDate().trim().isEmpty()) {
            System.out.println("Due date cannot be empty.");
            return;
        }

        java.sql.Date dueDate = parseDate(fee.getDueDate());

        if (dueDate == null) {
            System.out.println(
                    "Invalid date. Please use the format YYYY-MM-DD."
            );
            return;
        }

        String sql =
                "INSERT INTO fees " +
                        "(student_id, amount, due_date, status) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, fee.getStudentId());
            statement.setDouble(2, fee.getAmount());
            statement.setDate(3, dueDate);
            statement.setString(4, "Unpaid");

            statement.executeUpdate();

            System.out.println("Fee record added successfully!");

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Student ID does not exist.");
                return;
            }
            System.out.println("Unable to add fee record.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // VIEW ALL FEE RECORDS
    // =========================================================
    public List<Fee> getAllFees() {

        List<Fee> fees = new ArrayList<>();

        String sql =
                "SELECT f.*, s.name AS student_name FROM fees f " +
                        "JOIN students s ON f.student_id = s.id " +
                        "ORDER BY f.id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                fees.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve fee records.");
            System.out.println("Database error: " + e.getMessage());
        }

        return fees;
    }


    // =========================================================
    // VIEW FEE RECORDS BY STUDENT
    // =========================================================
    public List<Fee> getFeesByStudentId(int studentId) {

        List<Fee> fees = new ArrayList<>();

        String sql =
                "SELECT f.*, s.name AS student_name FROM fees f " +
                        "JOIN students s ON f.student_id = s.id " +
                        "WHERE f.student_id = ? " +
                        "ORDER BY f.due_date";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                fees.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve fee records for student.");
            System.out.println("Database error: " + e.getMessage());
        }

        return fees;
    }


    // =========================================================
    // MARK FEE AS PAID
    // =========================================================
    public void markFeeAsPaid(int id, String paidDate, String paymentMethod) {

        java.sql.Date date = parseDate(paidDate);

        if (date == null) {
            System.out.println(
                    "Invalid date. Please use the format YYYY-MM-DD."
            );
            return;
        }

        String sql =
                "UPDATE fees SET " +
                        "status = 'Paid', paid_date = ?, payment_method = ? " +
                        "WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(1, date);
            statement.setString(2, paymentMethod);
            statement.setInt(3, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Fee marked as paid successfully!");
            } else {
                System.out.println("Fee record not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to update fee record.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // DELETE FEE RECORD
    // =========================================================
    public void deleteFee(int id) {

        String sql = "DELETE FROM fees WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Fee record deleted successfully!");
            } else {
                System.out.println("Fee record not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to delete fee record.");
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
    // MAP RESULT SET ROW TO FEE OBJECT
    // =========================================================
    private Fee mapRow(ResultSet resultSet) throws SQLException {

        Fee fee = new Fee();

        fee.setId(resultSet.getInt("id"));
        fee.setStudentId(resultSet.getInt("student_id"));
        fee.setStudentName(resultSet.getString("student_name"));
        fee.setAmount(resultSet.getDouble("amount"));
        fee.setDueDate(String.valueOf(resultSet.getDate("due_date")));

        if (resultSet.getDate("paid_date") != null) {
            fee.setPaidDate(String.valueOf(resultSet.getDate("paid_date")));
        }

        fee.setStatus(resultSet.getString("status"));
        fee.setPaymentMethod(resultSet.getString("payment_method"));

        return fee;
    }
}
