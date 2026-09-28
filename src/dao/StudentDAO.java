package dao;

import exception.StudentNotFoundException;
import model.Student;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {

    // =========================================================
    // ADD STUDENT
    // =========================================================
    public void addStudent(Student student) {

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }

        if (!student.getEmail().contains("@")
                || !student.getEmail().contains(".")) {
            System.out.println("Invalid email format.");
            return;
        }

        String duplicateEmail =
                "SELECT id FROM students WHERE email = ?";

        String sql =
                "INSERT INTO students (name, email, phone, address, department_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            // Check duplicate email
            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateEmail)) {

                statement.setString(1, student.getEmail());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Email already exists.");
                    System.out.println("Please use a different email.");
                    return;
                }
            }

            // Insert student
            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, student.getName());
                statement.setString(2, student.getEmail());
                statement.setString(3, student.getPhone());
                statement.setString(4, student.getAddress());

                if (student.getDepartmentId() > 0) {
                    statement.setInt(5, student.getDepartmentId());
                } else {
                    statement.setNull(5, java.sql.Types.INTEGER);
                }

                statement.executeUpdate();

                System.out.println("Student added successfully!");
            }

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Department ID does not exist.");
                return;
            }
            System.out.println("Unable to add student.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // GET ALL STUDENTS
    // =========================================================
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql =
                "SELECT s.*, d.department_name FROM students s " +
                        "LEFT JOIN departments d ON s.department_id = d.id " +
                        "ORDER BY s.id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Student student = new Student();

                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setEmail(resultSet.getString("email"));
                student.setPhone(resultSet.getString("phone"));
                student.setAddress(resultSet.getString("address"));
                student.setDepartmentId(resultSet.getInt("department_id"));
                student.setDepartmentName(resultSet.getString("department_name"));

                students.add(student);
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve students.");
            System.out.println("Database error: " + e.getMessage());
        }

        return students;
    }


    // =========================================================
    // GET STUDENT BY ID
    // =========================================================
    public Student getStudentById(int id) {

        String sql =
                "SELECT s.*, d.department_name FROM students s " +
                        "LEFT JOIN departments d ON s.department_id = d.id " +
                        "WHERE s.id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Student student = new Student();

                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setEmail(resultSet.getString("email"));
                student.setPhone(resultSet.getString("phone"));
                student.setAddress(resultSet.getString("address"));
                student.setDepartmentId(resultSet.getInt("department_id"));
                student.setDepartmentName(resultSet.getString("department_name"));

                return student;
            }

        } catch (SQLException e) {
            System.out.println("Unable to search student.");
            System.out.println("Database error: " + e.getMessage());
        }

        return null;
    }


    // =========================================================
    // GET STUDENT BY ID OR THROW CUSTOM EXCEPTION
    // =========================================================
    public Student getStudentByIdOrThrow(int id)
            throws StudentNotFoundException {

        Student student = getStudentById(id);

        if (student == null) {

            throw new StudentNotFoundException(
                    "Student with ID " + id + " was not found."
            );
        }

        return student;
    }


    // =========================================================
    // GET STUDENT MAP
    // HashMap requirement
    // =========================================================
    public Map<Integer, Student> getStudentMap() {

        Map<Integer, Student> studentMap = new HashMap<>();

        String sql =
                "SELECT id, name, email, phone, address " +
                        "FROM students ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Student student = new Student();

                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setEmail(resultSet.getString("email"));
                student.setPhone(resultSet.getString("phone"));
                student.setAddress(resultSet.getString("address"));

                studentMap.put(student.getId(), student);
            }

        } catch (SQLException e) {
            System.out.println("Unable to create student lookup map.");
            System.out.println("Database error: " + e.getMessage());
        }

        return studentMap;
    }


    // =========================================================
    // SEARCH STUDENTS BY NAME
    // =========================================================
    public List<Student> getStudentsByName(String name) {

        List<Student> students = new ArrayList<>();

        String sql =
                "SELECT s.*, d.department_name FROM students s " +
                        "LEFT JOIN departments d ON s.department_id = d.id " +
                        "WHERE LOWER(s.name) LIKE LOWER(?) " +
                        "ORDER BY s.name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, "%" + name + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Student student = new Student();

                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setEmail(resultSet.getString("email"));
                student.setPhone(resultSet.getString("phone"));
                student.setAddress(resultSet.getString("address"));
                student.setDepartmentId(resultSet.getInt("department_id"));
                student.setDepartmentName(resultSet.getString("department_name"));

                students.add(student);
            }

        } catch (SQLException e) {
            System.out.println("Unable to search students by name.");
            System.out.println("Database error: " + e.getMessage());
        }

        return students;
    }


    // =========================================================
    // UPDATE STUDENT
    // =========================================================
    public void updateStudent(Student student) {

        if (student.getName() == null
                || student.getName().trim().isEmpty()) {

            System.out.println("Student name cannot be empty.");
            return;
        }

        if (student.getEmail() == null
                || student.getEmail().trim().isEmpty()) {

            System.out.println("Email cannot be empty.");
            return;
        }

        if (!student.getEmail().contains("@")
                || !student.getEmail().contains(".")) {

            System.out.println("Invalid email format.");
            return;
        }

        String duplicateEmail =
                "SELECT id FROM students " +
                        "WHERE email = ? AND id != ?";

        String sql =
                "UPDATE students " +
                        "SET name = ?, email = ?, phone = ?, address = ?, " +
                        "department_id = ? " +
                        "WHERE id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            // Check duplicate email
            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateEmail)) {

                statement.setString(1, student.getEmail());
                statement.setInt(2, student.getId());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    System.out.println(
                            "Email already belongs to another student."
                    );

                    return;
                }
            }

            // Update student
            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, student.getName());
                statement.setString(2, student.getEmail());
                statement.setString(3, student.getPhone());
                statement.setString(4, student.getAddress());

                if (student.getDepartmentId() > 0) {
                    statement.setInt(5, student.getDepartmentId());
                } else {
                    statement.setNull(5, java.sql.Types.INTEGER);
                }

                statement.setInt(6, student.getId());

                int rows = statement.executeUpdate();

                if (rows > 0) {

                    System.out.println(
                            "Student updated successfully!"
                    );

                } else {

                    System.out.println("Student not found.");
                }
            }

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Department ID does not exist.");
                return;
            }
            System.out.println("Unable to update student.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // DELETE STUDENT
    // =========================================================
    public void deleteStudent(int id) {

        String sql =
                "DELETE FROM students WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Student deleted successfully!"
                );

            } else {

                System.out.println("Student not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to delete student.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // STUDENT ACADEMIC REPORT
    // =========================================================
    public void getStudentAcademicReport(int studentId) {

        String sql =
                "SELECT " +
                        "s.id AS student_id, " +
                        "s.name AS student_name, " +
                        "s.email AS email, " +
                        "c.course_code, " +
                        "c.course_name, " +
                        "c.credit_hours, " +
                        "e.enrollment_date, " +
                        "m.marks, " +
                        "m.grade " +
                        "FROM students s " +
                        "JOIN enrollments e ON s.id = e.student_id " +
                        "JOIN courses c ON e.course_id = c.id " +
                        "LEFT JOIN marks m " +
                        "ON e.student_id = m.student_id " +
                        "AND e.course_id = m.course_id " +
                        "WHERE s.id = ? " +
                        "ORDER BY c.course_code";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();

            boolean found = false;

            while (resultSet.next()) {

                if (!found) {

                    System.out.println(
                            "\n=========================================="
                    );

                    System.out.println(
                            "          STUDENT ACADEMIC REPORT"
                    );

                    System.out.println(
                            "=========================================="
                    );

                    System.out.println(
                            "Student ID: "
                                    + resultSet.getInt("student_id")
                    );

                    System.out.println(
                            "Student Name: "
                                    + resultSet.getString("student_name")
                    );

                    System.out.println(
                            "Email: "
                                    + resultSet.getString("email")
                    );

                    System.out.println(
                            "------------------------------------------"
                    );

                    found = true;
                }

                System.out.println(
                        "Course Code: "
                                + resultSet.getString("course_code")
                );

                System.out.println(
                        "Course Name: "
                                + resultSet.getString("course_name")
                );

                System.out.println(
                        "Credit Hours: "
                                + resultSet.getInt("credit_hours")
                );

                System.out.println(
                        "Enrollment Date: "
                                + resultSet.getDate("enrollment_date")
                );

                Object marks =
                        resultSet.getObject("marks");

                if (marks != null) {

                    System.out.println(
                            "Marks: "
                                    + resultSet.getDouble("marks")
                    );

                    System.out.println(
                            "Grade: "
                                    + resultSet.getString("grade")
                    );

                } else {

                    System.out.println(
                            "Marks: Not entered"
                    );

                    System.out.println(
                            "Grade: Not available"
                    );
                }

                System.out.println(
                        "------------------------------------------"
                );
            }

            if (!found) {

                Student student =
                        getStudentById(studentId);

                if (student == null) {

                    System.out.println(
                            "Student not found."
                    );

                } else {

                    System.out.println(
                            "Student has no enrolled courses."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Unable to generate academic report."
            );

            System.out.println(
                    "Database error: " + e.getMessage()
            );
        }
    }
}
