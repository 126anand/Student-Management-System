package dao;

import model.Course;
import model.Teacher;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    // =========================================================
    // ADD TEACHER
    // =========================================================
    public void addTeacher(Teacher teacher) {

        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            System.out.println("Teacher name cannot be empty.");
            return;
        }

        if (teacher.getEmail() == null || teacher.getEmail().trim().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }

        if (!teacher.getEmail().contains("@")
                || !teacher.getEmail().contains(".")) {
            System.out.println("Invalid email format.");
            return;
        }

        String duplicateEmail =
                "SELECT id FROM teachers WHERE email = ?";

        String sql =
                "INSERT INTO teachers (name, email, phone, designation, department_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateEmail)) {

                statement.setString(1, teacher.getEmail());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Email already exists.");
                    return;
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, teacher.getName());
                statement.setString(2, teacher.getEmail());
                statement.setString(3, teacher.getPhone());
                statement.setString(4, teacher.getDesignation());

                if (teacher.getDepartmentId() > 0) {
                    statement.setInt(5, teacher.getDepartmentId());
                } else {
                    statement.setNull(5, java.sql.Types.INTEGER);
                }

                statement.executeUpdate();

                System.out.println("Teacher added successfully!");
            }

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Department ID does not exist.");
                return;
            }
            System.out.println("Unable to add teacher.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // VIEW ALL TEACHERS
    // =========================================================
    public List<Teacher> getAllTeachers() {

        List<Teacher> teachers = new ArrayList<>();

        String sql =
                "SELECT t.*, d.department_name FROM teachers t " +
                        "LEFT JOIN departments d ON t.department_id = d.id " +
                        "ORDER BY t.id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                teachers.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve teachers.");
            System.out.println("Database error: " + e.getMessage());
        }

        return teachers;
    }


    // =========================================================
    // GET TEACHER BY ID
    // =========================================================
    public Teacher getTeacherById(int id) {

        String sql =
                "SELECT t.*, d.department_name FROM teachers t " +
                        "LEFT JOIN departments d ON t.department_id = d.id " +
                        "WHERE t.id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapRow(resultSet);
            }

        } catch (SQLException e) {
            System.out.println("Unable to search teacher.");
            System.out.println("Database error: " + e.getMessage());
        }

        return null;
    }


    // =========================================================
    // SEARCH TEACHERS BY NAME
    // =========================================================
    public List<Teacher> getTeachersByName(String name) {

        List<Teacher> teachers = new ArrayList<>();

        String sql =
                "SELECT t.*, d.department_name FROM teachers t " +
                        "LEFT JOIN departments d ON t.department_id = d.id " +
                        "WHERE LOWER(t.name) LIKE LOWER(?) " +
                        "ORDER BY t.name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, "%" + name + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                teachers.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Unable to search teachers by name.");
            System.out.println("Database error: " + e.getMessage());
        }

        return teachers;
    }


    // =========================================================
    // UPDATE TEACHER
    // =========================================================
    public void updateTeacher(Teacher teacher) {

        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            System.out.println("Teacher name cannot be empty.");
            return;
        }

        if (teacher.getEmail() == null || teacher.getEmail().trim().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }

        String duplicateEmail =
                "SELECT id FROM teachers WHERE email = ? AND id != ?";

        String sql =
                "UPDATE teachers SET " +
                        "name = ?, email = ?, phone = ?, designation = ?, department_id = ? " +
                        "WHERE id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateEmail)) {

                statement.setString(1, teacher.getEmail());
                statement.setInt(2, teacher.getId());

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Email already belongs to another teacher.");
                    return;
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, teacher.getName());
                statement.setString(2, teacher.getEmail());
                statement.setString(3, teacher.getPhone());
                statement.setString(4, teacher.getDesignation());

                if (teacher.getDepartmentId() > 0) {
                    statement.setInt(5, teacher.getDepartmentId());
                } else {
                    statement.setNull(5, java.sql.Types.INTEGER);
                }

                statement.setInt(6, teacher.getId());

                int rows = statement.executeUpdate();

                if (rows > 0) {
                    System.out.println("Teacher updated successfully!");
                } else {
                    System.out.println("Teacher not found.");
                }
            }

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Department ID does not exist.");
                return;
            }
            System.out.println("Unable to update teacher.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // DELETE TEACHER
    // =========================================================
    public void deleteTeacher(int id) {

        String sql = "DELETE FROM teachers WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Teacher deleted successfully!");
            } else {
                System.out.println("Teacher not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to delete teacher.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // ASSIGN TEACHER TO COURSE
    // =========================================================
    public void assignTeacherToCourse(int teacherId, int courseId) {

        String sql = "UPDATE courses SET teacher_id = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, teacherId);
            statement.setInt(2, courseId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Teacher assigned to course successfully!");
            } else {
                System.out.println("Course not found.");
            }

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Teacher ID does not exist.");
                return;
            }
            System.out.println("Unable to assign teacher to course.");
            System.out.println("Database error: " + e.getMessage());
        }
    }


    // =========================================================
    // VIEW COURSES TAUGHT BY A TEACHER
    // =========================================================
    public List<Course> getCoursesByTeacherId(int teacherId) {

        List<Course> courses = new ArrayList<>();

        String sql =
                "SELECT * FROM courses WHERE teacher_id = ? ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, teacherId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Course course = new Course();

                course.setId(resultSet.getInt("id"));
                course.setCourseCode(resultSet.getString("course_code"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setCreditHours(resultSet.getInt("credit_hours"));
                course.setDepartmentId(resultSet.getInt("department_id"));
                course.setTeacherId(resultSet.getInt("teacher_id"));

                courses.add(course);
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve courses for teacher.");
            System.out.println("Database error: " + e.getMessage());
        }

        return courses;
    }


    // =========================================================
    // MAP RESULT SET ROW TO TEACHER OBJECT
    // =========================================================
    private Teacher mapRow(ResultSet resultSet) throws SQLException {

        Teacher teacher = new Teacher();

        teacher.setId(resultSet.getInt("id"));
        teacher.setName(resultSet.getString("name"));
        teacher.setEmail(resultSet.getString("email"));
        teacher.setPhone(resultSet.getString("phone"));
        teacher.setDesignation(resultSet.getString("designation"));
        teacher.setDepartmentId(resultSet.getInt("department_id"));
        teacher.setDepartmentName(resultSet.getString("department_name"));

        return teacher;
    }
}
