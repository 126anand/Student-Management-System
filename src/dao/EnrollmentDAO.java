package dao;

import model.Enrollment;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // ==========================================
    // ADD ENROLLMENT
    // ==========================================
    public void addEnrollment(Enrollment enrollment) {

        String studentCheck =
                "SELECT id FROM students WHERE id = ?";

        String courseCheck =
                "SELECT id FROM courses WHERE id = ?";

        String duplicateCheck =
                "SELECT id FROM enrollments " +
                        "WHERE student_id = ? AND course_id = ?";

        String insertSQL =
                "INSERT INTO enrollments " +
                        "(student_id, course_id, enrollment_date) " +
                        "VALUES (?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection()) {

            // Check student
            try (PreparedStatement statement =
                         connection.prepareStatement(studentCheck)) {

                statement.setInt(
                        1,
                        enrollment.getStudentId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (!resultSet.next()) {

                    System.out.println(
                            "Student ID not found."
                    );

                    System.out.println(
                            "Please enter a valid student ID."
                    );

                    return;
                }
            }

            // Check course
            try (PreparedStatement statement =
                         connection.prepareStatement(courseCheck)) {

                statement.setInt(
                        1,
                        enrollment.getCourseId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (!resultSet.next()) {

                    System.out.println(
                            "Course ID not found."
                    );

                    System.out.println(
                            "Please enter a valid course ID."
                    );

                    return;
                }
            }

            // Check duplicate enrollment
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 duplicateCheck)) {

                statement.setInt(
                        1,
                        enrollment.getStudentId()
                );

                statement.setInt(
                        2,
                        enrollment.getCourseId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (resultSet.next()) {

                    System.out.println(
                            "Student is already enrolled " +
                                    "in this course."
                    );

                    System.out.println(
                            "Duplicate enrollment is not allowed."
                    );

                    return;
                }
            }

            // Insert enrollment
            try (PreparedStatement statement =
                         connection.prepareStatement(insertSQL)) {

                statement.setInt(
                        1,
                        enrollment.getStudentId()
                );

                statement.setInt(
                        2,
                        enrollment.getCourseId()
                );

                statement.setDate(
                        3,
                        java.sql.Date.valueOf(
                                enrollment.getEnrollmentDate()
                        )
                );

                statement.executeUpdate();

                System.out.println(
                        "Enrollment added successfully!"
                );
            }

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid enrollment date."
            );

            System.out.println(
                    "Please use YYYY-MM-DD format."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database error while adding enrollment."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }
    }


    // ==========================================
    // VIEW ALL ENROLLMENTS
    // ==========================================
    public List<Enrollment> getAllEnrollments() {

        List<Enrollment> enrollments =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "e.id AS enrollment_id, " +
                        "e.student_id, " +
                        "s.name AS student_name, " +
                        "e.course_id, " +
                        "c.course_code, " +
                        "c.course_name, " +
                        "e.enrollment_date " +
                        "FROM enrollments e " +
                        "JOIN students s " +
                        "ON e.student_id = s.id " +
                        "JOIN courses c " +
                        "ON e.course_id = c.id " +
                        "ORDER BY e.id";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Enrollment enrollment =
                        new Enrollment();

                enrollment.setId(
                        resultSet.getInt(
                                "enrollment_id"
                        )
                );

                enrollment.setStudentId(
                        resultSet.getInt(
                                "student_id"
                        )
                );

                enrollment.setStudentName(
                        resultSet.getString(
                                "student_name"
                        )
                );

                enrollment.setCourseId(
                        resultSet.getInt(
                                "course_id"
                        )
                );

                enrollment.setCourseCode(
                        resultSet.getString(
                                "course_code"
                        )
                );

                enrollment.setCourseName(
                        resultSet.getString(
                                "course_name"
                        )
                );

                enrollment.setEnrollmentDate(
                        resultSet
                                .getDate(
                                        "enrollment_date"
                                )
                                .toString()
                );

                enrollments.add(enrollment);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while retrieving enrollments."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }

        return enrollments;
    }


    // ==========================================
    // SEARCH ENROLLMENT BY ID
    // ==========================================
    public Enrollment getEnrollmentById(int id) {

        String sql =
                "SELECT " +
                        "e.id AS enrollment_id, " +
                        "e.student_id, " +
                        "s.name AS student_name, " +
                        "e.course_id, " +
                        "c.course_code, " +
                        "c.course_name, " +
                        "e.enrollment_date " +
                        "FROM enrollments e " +
                        "JOIN students s " +
                        "ON e.student_id = s.id " +
                        "JOIN courses c " +
                        "ON e.course_id = c.id " +
                        "WHERE e.id = ?";

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

                Enrollment enrollment =
                        new Enrollment();

                enrollment.setId(
                        resultSet.getInt(
                                "enrollment_id"
                        )
                );

                enrollment.setStudentId(
                        resultSet.getInt(
                                "student_id"
                        )
                );

                enrollment.setStudentName(
                        resultSet.getString(
                                "student_name"
                        )
                );

                enrollment.setCourseId(
                        resultSet.getInt(
                                "course_id"
                        )
                );

                enrollment.setCourseCode(
                        resultSet.getString(
                                "course_code"
                        )
                );

                enrollment.setCourseName(
                        resultSet.getString(
                                "course_name"
                        )
                );

                enrollment.setEnrollmentDate(
                        resultSet
                                .getDate(
                                        "enrollment_date"
                                )
                                .toString()
                );

                return enrollment;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while searching enrollment."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }

        return null;
    }


    // ==========================================
    // LIST STUDENTS BY COURSE
    // ==========================================
    public List<Enrollment> getStudentsByCourse(
            int courseId) {

        List<Enrollment> enrollments =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "e.id AS enrollment_id, " +
                        "e.student_id, " +
                        "s.name AS student_name, " +
                        "s.email AS email, " +
                        "e.course_id, " +
                        "c.course_code, " +
                        "c.course_name, " +
                        "e.enrollment_date " +
                        "FROM enrollments e " +
                        "JOIN students s " +
                        "ON e.student_id = s.id " +
                        "JOIN courses c " +
                        "ON e.course_id = c.id " +
                        "WHERE e.course_id = ? " +
                        "ORDER BY s.name";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    courseId
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Enrollment enrollment =
                        new Enrollment();

                enrollment.setId(
                        resultSet.getInt(
                                "enrollment_id"
                        )
                );

                enrollment.setStudentId(
                        resultSet.getInt(
                                "student_id"
                        )
                );

                enrollment.setStudentName(
                        resultSet.getString(
                                "student_name"
                        )
                );

                enrollment.setCourseId(
                        resultSet.getInt(
                                "course_id"
                        )
                );

                enrollment.setCourseCode(
                        resultSet.getString(
                                "course_code"
                        )
                );

                enrollment.setCourseName(
                        resultSet.getString(
                                "course_name"
                        )
                );

                enrollment.setEnrollmentDate(
                        resultSet
                                .getDate(
                                        "enrollment_date"
                                )
                                .toString()
                );

                enrollments.add(enrollment);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while retrieving students by course."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }

        return enrollments;
    }


    // ==========================================
    // DELETE ENROLLMENT
    // ==========================================
    public void deleteEnrollment(int id) {

        String sql =
                "DELETE FROM enrollments WHERE id = ?";

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
                        "Enrollment deleted successfully!"
                );

            } else {

                System.out.println(
                        "Enrollment not found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while deleting enrollment."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }
    }
}
