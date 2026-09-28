package dao;

import model.Department;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // =========================================================
    // ADD DEPARTMENT
    // =========================================================
    public void addDepartment(Department department) {

        if (department.getDepartmentCode() == null
                || department.getDepartmentCode().trim().isEmpty()) {

            System.out.println("Department code cannot be empty.");
            return;
        }

        if (department.getDepartmentName() == null
                || department.getDepartmentName().trim().isEmpty()) {

            System.out.println("Department name cannot be empty.");
            return;
        }

        String duplicateCode =
                "SELECT id FROM departments WHERE department_code = ?";

        String sql =
                "INSERT INTO departments (department_code, department_name) " +
                        "VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateCode)) {

                statement.setString(1, department.getDepartmentCode());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Department code already exists.");
                    return;
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, department.getDepartmentCode());
                statement.setString(2, department.getDepartmentName());

                statement.executeUpdate();

                System.out.println("Department added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Unable to add department.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // VIEW ALL DEPARTMENTS
    // =========================================================
    public List<Department> getAllDepartments() {

        List<Department> departments = new ArrayList<>();

        String sql = "SELECT * FROM departments ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Department department = new Department();

                department.setId(resultSet.getInt("id"));
                department.setDepartmentCode(resultSet.getString("department_code"));
                department.setDepartmentName(resultSet.getString("department_name"));

                departments.add(department);
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve departments.");
            System.out.println("Database error: " + e.getMessage());
        }

        return departments;
    }


    // =========================================================
    // GET DEPARTMENT BY ID
    // =========================================================
    public Department getDepartmentById(int id) {

        String sql = "SELECT * FROM departments WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Department department = new Department();

                department.setId(resultSet.getInt("id"));
                department.setDepartmentCode(resultSet.getString("department_code"));
                department.setDepartmentName(resultSet.getString("department_name"));

                return department;
            }

        } catch (SQLException e) {
            System.out.println("Unable to search department.");
            System.out.println("Database error: " + e.getMessage());
        }

        return null;
    }


    // =========================================================
    // UPDATE DEPARTMENT
    // =========================================================
    public void updateDepartment(Department department) {

        if (department.getDepartmentCode() == null
                || department.getDepartmentCode().trim().isEmpty()) {

            System.out.println("Department code cannot be empty.");
            return;
        }

        if (department.getDepartmentName() == null
                || department.getDepartmentName().trim().isEmpty()) {

            System.out.println("Department name cannot be empty.");
            return;
        }

        String duplicateCode =
                "SELECT id FROM departments " +
                        "WHERE department_code = ? AND id != ?";

        String sql =
                "UPDATE departments SET " +
                        "department_code = ?, department_name = ? " +
                        "WHERE id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateCode)) {

                statement.setString(1, department.getDepartmentCode());
                statement.setInt(2, department.getId());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println(
                            "Department code already belongs to another department."
                    );
                    return;
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, department.getDepartmentCode());
                statement.setString(2, department.getDepartmentName());
                statement.setInt(3, department.getId());

                int rows = statement.executeUpdate();

                if (rows > 0) {
                    System.out.println("Department updated successfully!");
                } else {
                    System.out.println("Department not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to update department.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // DELETE DEPARTMENT
    // =========================================================
    public void deleteDepartment(int id) {

        String sql = "DELETE FROM departments WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Department deleted successfully!");
            } else {
                System.out.println("Department not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to delete department.");
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
