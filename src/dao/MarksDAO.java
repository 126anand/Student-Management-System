package dao;

import model.Marks;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarksDAO {

    // ==========================================
    // ADD MARKS
    // ==========================================
    public void addMarks(Marks marks) {

        // Validate marks
        if (marks.getMarks() < 0 ||
                marks.getMarks() > 100) {

            System.out.println(
                    "Invalid marks! Marks must be between 0 and 100."
            );

            return;
        }

        String studentCheck =
                "SELECT id FROM students WHERE id = ?";

        String courseCheck =
                "SELECT id FROM courses WHERE id = ?";

        String enrollmentCheck =
                "SELECT id FROM enrollments " +
                        "WHERE student_id = ? AND course_id = ?";

        String duplicateCheck =
                "SELECT id FROM marks " +
                        "WHERE student_id = ? AND course_id = ?";

        String insertSQL =
                "INSERT INTO marks " +
                        "(student_id, course_id, marks, grade) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection()) {

            // ------------------------------------------
            // Check student
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(studentCheck)) {

                statement.setInt(
                        1,
                        marks.getStudentId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (!resultSet.next()) {

                    System.out.println(
                            "Student ID not found."
                    );

                    return;
                }
            }

            // ------------------------------------------
            // Check course
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(courseCheck)) {

                statement.setInt(
                        1,
                        marks.getCourseId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (!resultSet.next()) {

                    System.out.println(
                            "Course ID not found."
                    );

                    return;
                }
            }

            // ------------------------------------------
            // Check enrollment
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 enrollmentCheck)) {

                statement.setInt(
                        1,
                        marks.getStudentId()
                );

                statement.setInt(
                        2,
                        marks.getCourseId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (!resultSet.next()) {

                    System.out.println(
                            "Student is not enrolled in this course."
                    );

                    System.out.println(
                            "Please enroll the student first."
                    );

                    return;
                }
            }

            // ------------------------------------------
            // Check duplicate marks
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 duplicateCheck)) {

                statement.setInt(
                        1,
                        marks.getStudentId()
                );

                statement.setInt(
                        2,
                        marks.getCourseId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (resultSet.next()) {

                    System.out.println(
                            "Marks already exist for this student and course."
                    );

                    System.out.println(
                            "Use Update Marks to change the marks."
                    );

                    return;
                }
            }

            // ------------------------------------------
            // Insert marks
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(insertSQL)) {

                statement.setInt(
                        1,
                        marks.getStudentId()
                );

                statement.setInt(
                        2,
                        marks.getCourseId()
                );

                statement.setDouble(
                        3,
                        marks.getMarks()
                );

                statement.setString(
                        4,
                        marks.getGrade()
                );

                statement.executeUpdate();

                System.out.println(
                        "Marks added successfully!"
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while adding marks."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }
    }


    // ==========================================
    // VIEW ALL MARKS
    // ==========================================
    public List<Marks> getAllMarks() {

        List<Marks> marksList =
                new ArrayList<>();

        String sql =
                "SELECT * FROM marks";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Marks marks =
                        new Marks();

                marks.setId(
                        resultSet.getInt("id")
                );

                marks.setStudentId(
                        resultSet.getInt("student_id")
                );

                marks.setCourseId(
                        resultSet.getInt("course_id")
                );

                marks.setMarks(
                        resultSet.getDouble("marks")
                );

                marks.setGrade(
                        resultSet.getString("grade")
                );

                marksList.add(marks);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while retrieving marks."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }

        return marksList;
    }


    // ==========================================
    // FIND MARKS BY ID
    // ==========================================
    public Marks getMarksById(int id) {

        String sql =
                "SELECT * FROM marks WHERE id = ?";

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

                Marks marks =
                        new Marks();

                marks.setId(
                        resultSet.getInt("id")
                );

                marks.setStudentId(
                        resultSet.getInt("student_id")
                );

                marks.setCourseId(
                        resultSet.getInt("course_id")
                );

                marks.setMarks(
                        resultSet.getDouble("marks")
                );

                marks.setGrade(
                        resultSet.getString("grade")
                );

                return marks;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while searching marks."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }

        return null;
    }


    // ==========================================
    // UPDATE MARKS
    // ==========================================
    public void updateMarks(Marks marks) {

        // Validate marks
        if (marks.getMarks() < 0 ||
                marks.getMarks() > 100) {

            System.out.println(
                    "Invalid marks! Marks must be between 0 and 100."
            );

            return;
        }

        String studentCheck =
                "SELECT id FROM students WHERE id = ?";

        String courseCheck =
                "SELECT id FROM courses WHERE id = ?";

        String enrollmentCheck =
                "SELECT id FROM enrollments " +
                        "WHERE student_id = ? AND course_id = ?";

        String updateSQL =
                "UPDATE marks SET marks = ?, grade = ? " +
                        "WHERE student_id = ? AND course_id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            // ------------------------------------------
            // Check student
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(studentCheck)) {

                statement.setInt(
                        1,
                        marks.getStudentId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (!resultSet.next()) {

                    System.out.println(
                            "Student ID not found."
                    );

                    return;
                }
            }

            // ------------------------------------------
            // Check course
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(courseCheck)) {

                statement.setInt(
                        1,
                        marks.getCourseId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (!resultSet.next()) {

                    System.out.println(
                            "Course ID not found."
                    );

                    return;
                }
            }

            // ------------------------------------------
            // Check enrollment
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 enrollmentCheck)) {

                statement.setInt(
                        1,
                        marks.getStudentId()
                );

                statement.setInt(
                        2,
                        marks.getCourseId()
                );

                ResultSet resultSet =
                        statement.executeQuery();

                if (!resultSet.next()) {

                    System.out.println(
                            "Student is not enrolled in this course."
                    );

                    System.out.println(
                            "Marks cannot be updated."
                    );

                    return;
                }
            }

            // ------------------------------------------
            // Update marks
            // ------------------------------------------
            try (PreparedStatement statement =
                         connection.prepareStatement(updateSQL)) {

                statement.setDouble(
                        1,
                        marks.getMarks()
                );

                statement.setString(
                        2,
                        marks.getGrade()
                );

                statement.setInt(
                        3,
                        marks.getStudentId()
                );

                statement.setInt(
                        4,
                        marks.getCourseId()
                );

                int rows =
                        statement.executeUpdate();

                if (rows > 0) {

                    System.out.println(
                            "Marks updated successfully!"
                    );

                } else {

                    System.out.println(
                            "Marks record not found."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while updating marks."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }
    }


    // ==========================================
    // DELETE MARKS
    // ==========================================
    public void deleteMarks(int id) {

        String sql =
                "DELETE FROM marks WHERE id = ?";

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
                        "Marks deleted successfully!"
                );

            } else {

                System.out.println(
                        "Marks record not found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error while deleting marks."
            );

            System.out.println(
                    "SQL Error: " + e.getMessage()
            );
        }
    }
}
