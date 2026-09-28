package dao;

import model.Course;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Add course
    public void addCourse(Course course) {

        // Validate course code
        if (course.getCourseCode() == null ||
                course.getCourseCode().trim().isEmpty()) {

            System.out.println(
                    "Course code cannot be empty."
            );

            return;
        }

        // Validate course name
        if (course.getCourseName() == null ||
                course.getCourseName().trim().isEmpty()) {

            System.out.println(
                    "Course name cannot be empty."
            );

            return;
        }

        // Validate credit hours
        if (course.getCreditHours() <= 0) {

            System.out.println(
                    "Credit hours must be greater than 0."
            );

            return;
        }

        String duplicateCode =
                "SELECT id FROM courses WHERE course_code = ?";

        String sql =
                "INSERT INTO courses " +
                        "(course_code, course_name, credit_hours, department_id, teacher_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection()) {

            // Check duplicate course code
            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateCode)) {

                statement.setString(
                        1,
                        course.getCourseCode()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (resultSet.next()) {

                    System.out.println(
                            "Course code already exists."
                    );

                    System.out.println(
                            "Please use a different course code."
                    );

                    return;
                }
            }

            // Insert course
            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(
                        1,
                        course.getCourseCode()
                );

                statement.setString(
                        2,
                        course.getCourseName()
                );

                statement.setInt(
                        3,
                        course.getCreditHours()
                );

                if (course.getDepartmentId() > 0) {
                    statement.setInt(4, course.getDepartmentId());
                } else {
                    statement.setNull(4, java.sql.Types.INTEGER);
                }

                if (course.getTeacherId() > 0) {
                    statement.setInt(5, course.getTeacherId());
                } else {
                    statement.setNull(5, java.sql.Types.INTEGER);
                }

                statement.executeUpdate();

                System.out.println(
                        "Course added successfully!"
                );
            }

        } catch (SQLException e) {

            if ("23503".equals(e.getSQLState())) {
                System.out.println(
                        "Department ID or teacher ID does not exist."
                );
                return;
            }

            System.out.println(
                    "Database error while adding course."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }
    }


    // View all courses
    public List<Course> getAllCourses() {

        List<Course> courses =
                new ArrayList<>();

        String sql =
                "SELECT c.*, d.department_name, t.name AS teacher_name " +
                        "FROM courses c " +
                        "LEFT JOIN departments d ON c.department_id = d.id " +
                        "LEFT JOIN teachers t ON c.teacher_id = t.id " +
                        "ORDER BY c.id";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Course course =
                        new Course();

                course.setId(
                        resultSet.getInt("id")
                );

                course.setCourseCode(
                        resultSet.getString("course_code")
                );

                course.setCourseName(
                        resultSet.getString("course_name")
                );

                course.setCreditHours(
                        resultSet.getInt("credit_hours")
                );

                course.setDepartmentId(
                        resultSet.getInt("department_id")
                );

                course.setDepartmentName(
                        resultSet.getString("department_name")
                );

                course.setTeacherId(
                        resultSet.getInt("teacher_id")
                );

                course.setTeacherName(
                        resultSet.getString("teacher_name")
                );

                courses.add(course);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while retrieving courses."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }

        return courses;
    }


    // Find course by ID
    public Course getCourseById(int id) {

        String sql =
                "SELECT c.*, d.department_name, t.name AS teacher_name " +
                        "FROM courses c " +
                        "LEFT JOIN departments d ON c.department_id = d.id " +
                        "LEFT JOIN teachers t ON c.teacher_id = t.id " +
                        "WHERE c.id = ?";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    id
            );

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                Course course =
                        new Course();

                course.setId(
                        resultSet.getInt("id")
                );

                course.setCourseCode(
                        resultSet.getString("course_code")
                );

                course.setCourseName(
                        resultSet.getString("course_name")
                );

                course.setCreditHours(
                        resultSet.getInt("credit_hours")
                );

                course.setDepartmentId(
                        resultSet.getInt("department_id")
                );

                course.setDepartmentName(
                        resultSet.getString("department_name")
                );

                course.setTeacherId(
                        resultSet.getInt("teacher_id")
                );

                course.setTeacherName(
                        resultSet.getString("teacher_name")
                );

                return course;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while searching course."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }

        return null;
    }


    // Update course
    public void updateCourse(Course course) {

        if (course.getCourseCode() == null ||
                course.getCourseCode().trim().isEmpty()) {

            System.out.println(
                    "Course code cannot be empty."
            );

            return;
        }

        if (course.getCourseName() == null ||
                course.getCourseName().trim().isEmpty()) {

            System.out.println(
                    "Course name cannot be empty."
            );

            return;
        }

        if (course.getCreditHours() <= 0) {

            System.out.println(
                    "Credit hours must be greater than 0."
            );

            return;
        }

        String duplicateCode =
                "SELECT id FROM courses " +
                        "WHERE course_code = ? AND id != ?";

        String sql =
                "UPDATE courses SET " +
                        "course_code = ?, course_name = ?, credit_hours = ?, " +
                        "department_id = ?, teacher_id = ? " +
                        "WHERE id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            // Check duplicate course code
            try (PreparedStatement statement =
                         connection.prepareStatement(duplicateCode)) {

                statement.setString(
                        1,
                        course.getCourseCode()
                );

                statement.setInt(
                        2,
                        course.getId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (resultSet.next()) {

                    System.out.println(
                            "Course code already belongs to another course."
                    );

                    return;
                }
            }

            // Update course
            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(
                        1,
                        course.getCourseCode()
                );

                statement.setString(
                        2,
                        course.getCourseName()
                );

                statement.setInt(
                        3,
                        course.getCreditHours()
                );

                if (course.getDepartmentId() > 0) {
                    statement.setInt(4, course.getDepartmentId());
                } else {
                    statement.setNull(4, java.sql.Types.INTEGER);
                }

                if (course.getTeacherId() > 0) {
                    statement.setInt(5, course.getTeacherId());
                } else {
                    statement.setNull(5, java.sql.Types.INTEGER);
                }

                statement.setInt(
                        6,
                        course.getId()
                );

                int rows =
                        statement.executeUpdate();

                if (rows > 0) {

                    System.out.println(
                            "Course updated successfully!"
                    );

                } else {

                    System.out.println(
                            "Course not found."
                    );
                }
            }

        } catch (SQLException e) {

            if ("23503".equals(e.getSQLState())) {
                System.out.println(
                        "Department ID or teacher ID does not exist."
                );
                return;
            }

            System.out.println(
                    "Database error while updating course."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }
    }


    // Delete course
    public void deleteCourse(int id) {

        String sql =
                "DELETE FROM courses WHERE id = ?";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    id
            );

            int rows =
                    statement.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Course deleted successfully!"
                );

            } else {

                System.out.println(
                        "Course not found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while deleting course."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }
    }
}
