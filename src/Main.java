import dao.StudentDAO;
import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.MarksDAO;
import dao.DepartmentDAO;
import dao.TeacherDAO;
import dao.AttendanceDAO;
import dao.FeeDAO;
import dao.ComplaintDAO;
import exception.StudentNotFoundException;

import model.Student;
import model.Course;
import model.Enrollment;
import model.Marks;
import model.Department;
import model.Teacher;
import model.Attendance;
import model.Fee;
import model.Complaint;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        MarksDAO marksDAO = new MarksDAO();
        DepartmentDAO departmentDAO = new DepartmentDAO();
        TeacherDAO teacherDAO = new TeacherDAO();
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        FeeDAO feeDAO = new FeeDAO();
        ComplaintDAO complaintDAO = new ComplaintDAO();

        while (true) {

            System.out.println("\n========================================");
            System.out.println("       STUDENT MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Enrollment Management");
            System.out.println("4. Marks Management");
            System.out.println("5. Department Management");
            System.out.println("6. Teacher Management");
            System.out.println("7. Attendance Management");
            System.out.println("8. Fee Management");
            System.out.println("9. Complaint Management");
            System.out.println("10. Exit");
            System.out.println("========================================");

            int mainChoice = readInt(
                    scanner,
                    "Enter your choice: "
            );

            switch (mainChoice) {

                // ==========================================
                // STUDENT MANAGEMENT
                // ==========================================
                case 1: {

                    while (true) {

                        System.out.println(
                                "\n---------- STUDENT MANAGEMENT ----------"
                        );

                        System.out.println("1. Add Student");
                        System.out.println("2. View All Students");
                        System.out.println("3. Search Student");
                        System.out.println("4. Update Student");
                        System.out.println("5. Delete Student");
                        System.out.println("6. Student Academic Report");
                        System.out.println("7. Back to Main Menu");

                        int studentMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (studentMenuChoice == 7) {
                            break;
                        }

                        switch (studentMenuChoice) {

                            // ==================================
                            // ADD STUDENT
                            // ==================================
                            case 1: {

                                Student newStudent =
                                        new Student();

                                newStudent.setName(
                                        readText(
                                                scanner,
                                                "Enter name: "
                                        )
                                );

                                newStudent.setEmail(
                                        readText(
                                                scanner,
                                                "Enter email: "
                                        )
                                );

                                newStudent.setPhone(
                                        readText(
                                                scanner,
                                                "Enter phone: "
                                        )
                                );

                                newStudent.setAddress(
                                        readText(
                                                scanner,
                                                "Enter address: "
                                        )
                                );

                                newStudent.setDepartmentId(
                                        readInt(
                                                scanner,
                                                "Enter department ID (0 for none): "
                                        )
                                );

                                studentDAO.addStudent(
                                        newStudent
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL STUDENTS
                            // ==================================
                            case 2: {

                                List<Student> studentList =
                                        studentDAO.getAllStudents();

                                if (studentList.isEmpty()) {

                                    System.out.println(
                                            "No students found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- STUDENT LIST -----------"
                                    );

                                    for (Student currentStudent :
                                            studentList) {

                                        currentStudent.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // SEARCH STUDENT
                            // ==================================
                            case 3: {

                                System.out.println(
                                        "\n---------- SEARCH STUDENT ----------"
                                );

                                System.out.println(
                                        "1. Search by ID"
                                );

                                System.out.println(
                                        "2. Search by Name"
                                );

                                int searchStudentChoice =
                                        readInt(
                                                scanner,
                                                "Enter your choice: "
                                        );

                                // SEARCH BY ID USING CUSTOM EXCEPTION
                                if (searchStudentChoice == 1) {

                                    int searchStudentId =
                                            readInt(
                                                    scanner,
                                                    "Enter student ID: "
                                            );

                                    try {

                                        Student searchedStudent =
                                                studentDAO.getStudentByIdOrThrow(
                                                        searchStudentId
                                                );

                                        System.out.println(
                                                "\nStudent found:"
                                        );

                                        searchedStudent.displayInfo();

                                    } catch (StudentNotFoundException e) {

                                        System.out.println(
                                                "Error: " + e.getMessage()
                                        );
                                    }

                                }

                                // SEARCH BY NAME
                                else if (searchStudentChoice == 2) {

                                    String searchStudentName =
                                            readText(
                                                    scanner,
                                                    "Enter student name: "
                                            );

                                    List<Student> matchingStudents =
                                            studentDAO.getStudentsByName(
                                                    searchStudentName
                                            );

                                    if (matchingStudents.isEmpty()) {

                                        System.out.println(
                                                "No student found with that name."
                                        );

                                    } else {

                                        System.out.println(
                                                "\nStudents found:"
                                        );

                                        for (Student currentStudent :
                                                matchingStudents) {

                                            currentStudent.displayInfo();

                                            System.out.println(
                                                    "------------------------------------"
                                            );
                                        }
                                    }

                                } else {

                                    System.out.println(
                                            "Invalid choice."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // UPDATE STUDENT
                            // ==================================
                            case 4: {

                                int updateStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID to update: "
                                        );

                                Student studentForUpdate =
                                        studentDAO.getStudentById(
                                                updateStudentId
                                        );

                                if (studentForUpdate != null) {

                                    studentForUpdate.setName(
                                            readText(
                                                    scanner,
                                                    "Enter new name: "
                                            )
                                    );

                                    studentForUpdate.setEmail(
                                            readText(
                                                    scanner,
                                                    "Enter new email: "
                                            )
                                    );

                                    studentForUpdate.setPhone(
                                            readText(
                                                    scanner,
                                                    "Enter new phone: "
                                            )
                                    );

                                    studentForUpdate.setAddress(
                                            readText(
                                                    scanner,
                                                    "Enter new address: "
                                            )
                                    );

                                    studentForUpdate.setDepartmentId(
                                            readInt(
                                                    scanner,
                                                    "Enter new department ID (0 for none): "
                                            )
                                    );

                                    studentDAO.updateStudent(
                                            studentForUpdate
                                    );

                                } else {

                                    System.out.println(
                                            "Student not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // DELETE STUDENT
                            // ==================================
                            case 5: {

                                int deleteStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID to delete: "
                                        );

                                Student studentForDelete =
                                        studentDAO.getStudentById(
                                                deleteStudentId
                                        );

                                if (studentForDelete != null) {

                                    System.out.println(
                                            "\nStudent found:"
                                    );

                                    studentForDelete.displayInfo();

                                    if (confirmDelete(scanner)) {

                                        studentDAO.deleteStudent(
                                                deleteStudentId
                                        );
                                    }

                                } else {

                                    System.out.println(
                                            "Student not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // STUDENT ACADEMIC REPORT
                            // ==================================
                            case 6: {

                                int academicReportStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        );

                                studentDAO
                                        .getStudentAcademicReport(
                                                academicReportStudentId
                                        );

                                break;
                            }


                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // COURSE MANAGEMENT
                // ==========================================
                case 2: {

                    while (true) {

                        System.out.println(
                                "\n---------- COURSE MANAGEMENT ----------"
                        );

                        System.out.println("1. Add Course");
                        System.out.println("2. View All Courses");
                        System.out.println("3. Search Course");
                        System.out.println("4. Update Course");
                        System.out.println("5. Delete Course");
                        System.out.println("6. Back to Main Menu");

                        int courseMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (courseMenuChoice == 6) {
                            break;
                        }

                        switch (courseMenuChoice) {

                            // ==================================
                            // ADD COURSE
                            // ==================================
                            case 1: {

                                Course newCourse =
                                        new Course();

                                newCourse.setCourseCode(
                                        readText(
                                                scanner,
                                                "Enter course code: "
                                        )
                                );

                                newCourse.setCourseName(
                                        readText(
                                                scanner,
                                                "Enter course name: "
                                        )
                                );

                                newCourse.setCreditHours(
                                        readInt(
                                                scanner,
                                                "Enter credit hours: "
                                        )
                                );

                                newCourse.setDepartmentId(
                                        readInt(
                                                scanner,
                                                "Enter department ID (0 for none): "
                                        )
                                );

                                newCourse.setTeacherId(
                                        readInt(
                                                scanner,
                                                "Enter teacher ID (0 for none): "
                                        )
                                );

                                courseDAO.addCourse(
                                        newCourse
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL COURSES
                            // ==================================
                            case 2: {

                                List<Course> courseList =
                                        courseDAO.getAllCourses();

                                if (courseList.isEmpty()) {

                                    System.out.println(
                                            "No courses found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- COURSE LIST -----------"
                                    );

                                    for (Course currentCourse :
                                            courseList) {

                                        currentCourse.displayInfo();

                                        System.out.println(
                                                "-----------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // SEARCH COURSE
                            // ==================================
                            case 3: {

                                int searchCourseId =
                                        readInt(
                                                scanner,
                                                "Enter course ID: "
                                        );

                                Course searchedCourse =
                                        courseDAO.getCourseById(
                                                searchCourseId
                                        );

                                if (searchedCourse != null) {

                                    System.out.println(
                                            "\nCourse found:"
                                    );

                                    searchedCourse.displayInfo();

                                } else {

                                    System.out.println(
                                            "Course not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // UPDATE COURSE
                            // ==================================
                            case 4: {

                                int updateCourseId =
                                        readInt(
                                                scanner,
                                                "Enter course ID to update: "
                                        );

                                Course courseForUpdate =
                                        courseDAO.getCourseById(
                                                updateCourseId
                                        );

                                if (courseForUpdate != null) {

                                    courseForUpdate.setCourseCode(
                                            readText(
                                                    scanner,
                                                    "Enter new course code: "
                                            )
                                    );

                                    courseForUpdate.setCourseName(
                                            readText(
                                                    scanner,
                                                    "Enter new course name: "
                                            )
                                    );

                                    courseForUpdate.setCreditHours(
                                            readInt(
                                                    scanner,
                                                    "Enter new credit hours: "
                                            )
                                    );

                                    courseForUpdate.setDepartmentId(
                                            readInt(
                                                    scanner,
                                                    "Enter new department ID (0 for none): "
                                            )
                                    );

                                    courseForUpdate.setTeacherId(
                                            readInt(
                                                    scanner,
                                                    "Enter new teacher ID (0 for none): "
                                            )
                                    );

                                    courseDAO.updateCourse(
                                            courseForUpdate
                                    );

                                } else {

                                    System.out.println(
                                            "Course not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // DELETE COURSE
                            // ==================================
                            case 5: {

                                int deleteCourseId =
                                        readInt(
                                                scanner,
                                                "Enter course ID to delete: "
                                        );

                                Course courseForDelete =
                                        courseDAO.getCourseById(
                                                deleteCourseId
                                        );

                                if (courseForDelete != null) {

                                    System.out.println(
                                            "\nCourse found:"
                                    );

                                    System.out.println(
                                            "Course ID: "
                                                    + courseForDelete.getId()
                                    );

                                    System.out.println(
                                            "Course Code: "
                                                    + courseForDelete
                                                    .getCourseCode()
                                    );

                                    System.out.println(
                                            "Course Name: "
                                                    + courseForDelete
                                                    .getCourseName()
                                    );

                                    System.out.println(
                                            "Credit Hours: "
                                                    + courseForDelete
                                                    .getCreditHours()
                                    );

                                    if (confirmDelete(scanner)) {

                                        courseDAO.deleteCourse(
                                                deleteCourseId
                                        );
                                    }

                                } else {

                                    System.out.println(
                                            "Course not found."
                                    );
                                }

                                break;
                            }


                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // ENROLLMENT MANAGEMENT
                // ==========================================
                case 3: {

                    while (true) {

                        System.out.println(
                                "\n---------- ENROLLMENT MANAGEMENT ----------"
                        );

                        System.out.println("1. Add Enrollment");
                        System.out.println("2. View All Enrollments");
                        System.out.println("3. Search Enrollment");
                        System.out.println("4. Delete Enrollment");
                        System.out.println("5. List Students by Course");
                        System.out.println("6. Back to Main Menu");

                        int enrollmentMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (enrollmentMenuChoice == 6) {
                            break;
                        }

                        switch (enrollmentMenuChoice) {

                            // ==================================
                            // ADD ENROLLMENT
                            // ==================================
                            case 1: {

                                Enrollment newEnrollment =
                                        new Enrollment();

                                newEnrollment.setStudentId(
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        )
                                );

                                newEnrollment.setCourseId(
                                        readInt(
                                                scanner,
                                                "Enter course ID: "
                                        )
                                );

                                newEnrollment.setEnrollmentDate(
                                        readDate(scanner)
                                );

                                enrollmentDAO.addEnrollment(
                                        newEnrollment
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL ENROLLMENTS
                            // ==================================
                            case 2: {

                                List<Enrollment> enrollmentList =
                                        enrollmentDAO
                                                .getAllEnrollments();

                                if (enrollmentList.isEmpty()) {

                                    System.out.println(
                                            "No enrollments found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- ENROLLMENT LIST -----------"
                                    );

                                    for (Enrollment currentEnrollment :
                                            enrollmentList) {

                                        System.out.println(
                                                "Enrollment ID: "
                                                        + currentEnrollment
                                                        .getId()
                                        );

                                        System.out.println(
                                                "Student ID: "
                                                        + currentEnrollment
                                                        .getStudentId()
                                        );

                                        System.out.println(
                                                "Student Name: "
                                                        + currentEnrollment
                                                        .getStudentName()
                                        );

                                        System.out.println(
                                                "Course ID: "
                                                        + currentEnrollment
                                                        .getCourseId()
                                        );

                                        System.out.println(
                                                "Course Code: "
                                                        + currentEnrollment
                                                        .getCourseCode()
                                        );

                                        System.out.println(
                                                "Course Name: "
                                                        + currentEnrollment
                                                        .getCourseName()
                                        );

                                        System.out.println(
                                                "Enrollment Date: "
                                                        + currentEnrollment
                                                        .getEnrollmentDate()
                                        );

                                        System.out.println(
                                                "---------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // SEARCH ENROLLMENT
                            // ==================================
                            case 3: {

                                int searchEnrollmentId =
                                        readInt(
                                                scanner,
                                                "Enter enrollment ID: "
                                        );

                                Enrollment searchedEnrollment =
                                        enrollmentDAO
                                                .getEnrollmentById(
                                                        searchEnrollmentId
                                                );

                                if (searchedEnrollment != null) {

                                    System.out.println(
                                            "\n=========================================="
                                    );

                                    System.out.println(
                                            "          ENROLLMENT DETAILS"
                                    );

                                    System.out.println(
                                            "=========================================="
                                    );

                                    System.out.println(
                                            "Enrollment ID: "
                                                    + searchedEnrollment
                                                    .getId()
                                    );

                                    System.out.println(
                                            "Student ID: "
                                                    + searchedEnrollment
                                                    .getStudentId()
                                    );

                                    System.out.println(
                                            "Student Name: "
                                                    + searchedEnrollment
                                                    .getStudentName()
                                    );

                                    System.out.println(
                                            "Course ID: "
                                                    + searchedEnrollment
                                                    .getCourseId()
                                    );

                                    System.out.println(
                                            "Course Code: "
                                                    + searchedEnrollment
                                                    .getCourseCode()
                                    );

                                    System.out.println(
                                            "Course Name: "
                                                    + searchedEnrollment
                                                    .getCourseName()
                                    );

                                    System.out.println(
                                            "Enrollment Date: "
                                                    + searchedEnrollment
                                                    .getEnrollmentDate()
                                    );

                                    System.out.println(
                                            "=========================================="
                                    );

                                } else {

                                    System.out.println(
                                            "Enrollment not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // LIST STUDENTS BY COURSE
                            // ==================================
                            case 5: {

                                int courseId =
                                        readInt(
                                                scanner,
                                                "Enter course ID: "
                                        );

                                List<Enrollment> studentsByCourse =
                                        enrollmentDAO
                                                .getStudentsByCourse(
                                                        courseId
                                                );

                                if (studentsByCourse.isEmpty()) {

                                    System.out.println(
                                            "No students are enrolled in this course."
                                    );

                                } else {

                                    Enrollment firstEnrollment =
                                            studentsByCourse.get(0);

                                    System.out.println(
                                            "\n=========================================="
                                    );

                                    System.out.println(
                                            "          STUDENTS BY COURSE"
                                    );

                                    System.out.println(
                                            "=========================================="
                                    );

                                    System.out.println(
                                            "Course ID: "
                                                    + firstEnrollment
                                                    .getCourseId()
                                    );

                                    System.out.println(
                                            "Course Code: "
                                                    + firstEnrollment
                                                    .getCourseCode()
                                    );

                                    System.out.println(
                                            "Course Name: "
                                                    + firstEnrollment
                                                    .getCourseName()
                                    );

                                    System.out.println(
                                            "------------------------------------------"
                                    );

                                    for (Enrollment currentEnrollment :
                                            studentsByCourse) {

                                        System.out.println(
                                                "Student ID: "
                                                        + currentEnrollment
                                                        .getStudentId()
                                        );

                                        System.out.println(
                                                "Student Name: "
                                                        + currentEnrollment
                                                        .getStudentName()
                                        );

                                        System.out.println(
                                                "Enrollment Date: "
                                                        + currentEnrollment
                                                        .getEnrollmentDate()
                                        );

                                        System.out.println(
                                                "------------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // DELETE ENROLLMENT
                            // ==================================
                            case 4: {

                                int deleteEnrollmentId =
                                        readInt(
                                                scanner,
                                                "Enter enrollment ID to delete: "
                                        );

                                Enrollment enrollmentForDelete =
                                        enrollmentDAO
                                                .getEnrollmentById(
                                                        deleteEnrollmentId
                                                );

                                if (enrollmentForDelete != null) {

                                    System.out.println(
                                            "\nEnrollment found:"
                                    );

                                    System.out.println(
                                            "Enrollment ID: "
                                                    + enrollmentForDelete
                                                    .getId()
                                    );

                                    System.out.println(
                                            "Student ID: "
                                                    + enrollmentForDelete
                                                    .getStudentId()
                                    );

                                    System.out.println(
                                            "Student Name: "
                                                    + enrollmentForDelete
                                                    .getStudentName()
                                    );

                                    System.out.println(
                                            "Course ID: "
                                                    + enrollmentForDelete
                                                    .getCourseId()
                                    );

                                    System.out.println(
                                            "Course Code: "
                                                    + enrollmentForDelete
                                                    .getCourseCode()
                                    );

                                    System.out.println(
                                            "Course Name: "
                                                    + enrollmentForDelete
                                                    .getCourseName()
                                    );

                                    System.out.println(
                                            "Enrollment Date: "
                                                    + enrollmentForDelete
                                                    .getEnrollmentDate()
                                    );

                                    if (confirmDelete(scanner)) {

                                        enrollmentDAO.deleteEnrollment(
                                                deleteEnrollmentId
                                        );
                                    }

                                } else {

                                    System.out.println(
                                            "Enrollment not found."
                                    );
                                }

                                break;
                            }


                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // MARKS MANAGEMENT
                // ==========================================
                case 4: {

                    while (true) {

                        System.out.println(
                                "\n---------- MARKS MANAGEMENT ----------"
                        );

                        System.out.println("1. Add Marks");
                        System.out.println("2. View All Marks");
                        System.out.println("3. Search Marks");
                        System.out.println("4. Update Marks");
                        System.out.println("5. Delete Marks");
                        System.out.println("6. Student Average");
                        System.out.println("7. Class Ranking by Average");
                        System.out.println("8. Back to Main Menu");

                        int marksMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (marksMenuChoice == 8) {
                            break;
                        }

                        switch (marksMenuChoice) {

                            // ==================================
                            // ADD MARKS
                            // ==================================
                            case 1: {

                                Marks newMarks =
                                        new Marks();

                                newMarks.setStudentId(
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        )
                                );

                                newMarks.setCourseId(
                                        readInt(
                                                scanner,
                                                "Enter course ID: "
                                        )
                                );

                                double enteredMark =
                                        readDouble(
                                                scanner,
                                                "Enter marks: "
                                        );

                                if (enteredMark < 0 ||
                                        enteredMark > 100) {

                                    System.out.println(
                                            "Invalid marks! " +
                                                    "Marks must be between 0 and 100."
                                    );

                                    break;
                                }

                                newMarks.setMarks(
                                        enteredMark
                                );

                                String calculatedGrade =
                                        calculateGrade(
                                                enteredMark
                                        );

                                newMarks.setGrade(
                                        calculatedGrade
                                );

                                System.out.println(
                                        "Calculated Grade: "
                                                + calculatedGrade
                                );

                                marksDAO.addMarks(
                                        newMarks
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL MARKS
                            // ==================================
                            case 2: {

                                List<Marks> marksList =
                                        marksDAO.getAllMarks();

                                if (marksList.isEmpty()) {

                                    System.out.println(
                                            "No marks found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- MARKS LIST -----------"
                                    );

                                    for (Marks currentMarks :
                                            marksList) {

                                        System.out.println(
                                                "Marks ID: "
                                                        + currentMarks.getId()
                                        );

                                        System.out.println(
                                                "Student ID: "
                                                        + currentMarks
                                                        .getStudentId()
                                        );

                                        System.out.println(
                                                "Course ID: "
                                                        + currentMarks
                                                        .getCourseId()
                                        );

                                        System.out.println(
                                                "Marks: "
                                                        + currentMarks.getMarks()
                                        );

                                        System.out.println(
                                                "Grade: "
                                                        + currentMarks.getGrade()
                                        );

                                        System.out.println(
                                                "----------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // SEARCH MARKS
                            // ==================================
                            case 3: {

                                int searchMarksId =
                                        readInt(
                                                scanner,
                                                "Enter marks ID: "
                                        );

                                Marks searchedMarks =
                                        marksDAO.getMarksById(
                                                searchMarksId
                                        );

                                if (searchedMarks != null) {

                                    System.out.println(
                                            "\nMarks found:"
                                    );

                                    System.out.println(
                                            "Marks ID: "
                                                    + searchedMarks.getId()
                                    );

                                    System.out.println(
                                            "Student ID: "
                                                    + searchedMarks
                                                    .getStudentId()
                                    );

                                    System.out.println(
                                            "Course ID: "
                                                    + searchedMarks
                                                    .getCourseId()
                                    );

                                    System.out.println(
                                            "Marks: "
                                                    + searchedMarks.getMarks()
                                    );

                                    System.out.println(
                                            "Grade: "
                                                    + searchedMarks.getGrade()
                                    );

                                } else {

                                    System.out.println(
                                            "Marks record not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // UPDATE MARKS
                            // ==================================
                            case 4: {

                                int marksUpdateStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        );

                                int marksUpdateCourseId =
                                        readInt(
                                                scanner,
                                                "Enter course ID: "
                                        );

                                double updatedMarkValue =
                                        readDouble(
                                                scanner,
                                                "Enter new marks: "
                                        );

                                if (updatedMarkValue < 0 ||
                                        updatedMarkValue > 100) {

                                    System.out.println(
                                            "Invalid marks! " +
                                                    "Marks must be between 0 and 100."
                                    );

                                    break;
                                }

                                String updatedGrade =
                                        calculateGrade(
                                                updatedMarkValue
                                        );

                                Marks marksForUpdate =
                                        new Marks();

                                marksForUpdate.setStudentId(
                                        marksUpdateStudentId
                                );

                                marksForUpdate.setCourseId(
                                        marksUpdateCourseId
                                );

                                marksForUpdate.setMarks(
                                        updatedMarkValue
                                );

                                marksForUpdate.setGrade(
                                        updatedGrade
                                );

                                System.out.println(
                                        "Calculated Grade: "
                                                + updatedGrade
                                );

                                marksDAO.updateMarks(
                                        marksForUpdate
                                );

                                break;
                            }


                            // ==================================
                            // DELETE MARKS
                            // ==================================
                            case 5: {

                                int deleteMarksId =
                                        readInt(
                                                scanner,
                                                "Enter marks ID to delete: "
                                        );

                                Marks marksForDelete =
                                        marksDAO.getMarksById(
                                                deleteMarksId
                                        );

                                if (marksForDelete != null) {

                                    System.out.println(
                                            "\nMarks record found:"
                                    );

                                    System.out.println(
                                            "Marks ID: "
                                                    + marksForDelete.getId()
                                    );

                                    System.out.println(
                                            "Student ID: "
                                                    + marksForDelete
                                                    .getStudentId()
                                    );

                                    System.out.println(
                                            "Course ID: "
                                                    + marksForDelete
                                                    .getCourseId()
                                    );

                                    System.out.println(
                                            "Marks: "
                                                    + marksForDelete.getMarks()
                                    );

                                    System.out.println(
                                            "Grade: "
                                                    + marksForDelete.getGrade()
                                    );

                                    if (confirmDelete(scanner)) {

                                        marksDAO.deleteMarks(
                                                deleteMarksId
                                        );
                                    }

                                } else {

                                    System.out.println(
                                            "Marks record not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // STUDENT AVERAGE
                            // ==================================
                            case 6: {

                                int averageStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        );

                                Student averageStudent =
                                        studentDAO.getStudentById(
                                                averageStudentId
                                        );

                                if (averageStudent == null) {

                                    System.out.println(
                                            "Student not found."
                                    );

                                    break;
                                }

                                List<Marks> allMarks =
                                        marksDAO.getAllMarks();

                                double totalMarks = 0;
                                int markCount = 0;

                                for (Marks currentMarks : allMarks) {

                                    if (currentMarks.getStudentId()
                                            == averageStudentId) {

                                        totalMarks +=
                                                currentMarks.getMarks();

                                        markCount++;
                                    }
                                }

                                if (markCount == 0) {

                                    System.out.println(
                                            "No marks found for this student."
                                    );

                                } else {

                                    double average =
                                            totalMarks / markCount;

                                    System.out.println(
                                            "\n=========================================="
                                    );

                                    System.out.println(
                                            "          STUDENT AVERAGE"
                                    );

                                    System.out.println(
                                            "=========================================="
                                    );

                                    System.out.println(
                                            "Student ID: "
                                                    + averageStudent.getId()
                                    );

                                    System.out.println(
                                            "Student Name: "
                                                    + averageStudent.getName()
                                    );

                                    System.out.printf(
                                            "Average Marks: %.2f%n",
                                            average
                                    );

                                    System.out.println(
                                            "Subjects with Marks: "
                                                    + markCount
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // CLASS RANKING BY AVERAGE
                            // ==================================
                            case 7: {

                                List<Marks> allMarks =
                                        marksDAO.getAllMarks();

                                if (allMarks.isEmpty()) {

                                    System.out.println(
                                            "No marks found. Cannot generate ranking."
                                    );

                                    break;
                                }

                                Map<Integer, Double> totalMap =
                                        new HashMap<>();

                                Map<Integer, Integer> countMap =
                                        new HashMap<>();

                                for (Marks currentMarks : allMarks) {

                                    int studentId =
                                            currentMarks.getStudentId();

                                    double currentTotal =
                                            totalMap.getOrDefault(
                                                    studentId,
                                                    0.0
                                            );

                                    int currentCount =
                                            countMap.getOrDefault(
                                                    studentId,
                                                    0
                                            );

                                    totalMap.put(
                                            studentId,
                                            currentTotal
                                                    + currentMarks.getMarks()
                                    );

                                    countMap.put(
                                            studentId,
                                            currentCount + 1
                                    );
                                }

                                Map<Integer, Double> averageMap =
                                        new HashMap<>();

                                for (Integer studentId : totalMap.keySet()) {

                                    double average =
                                            totalMap.get(studentId)
                                                    / countMap.get(studentId);

                                    averageMap.put(
                                            studentId,
                                            average
                                    );
                                }

                                List<Map.Entry<Integer, Double>> ranking =
                                        new ArrayList<>(
                                                averageMap.entrySet()
                                        );

                                ranking.sort(
                                        (a, b) -> Double.compare(
                                                b.getValue(),
                                                a.getValue()
                                        )
                                );

                                Map<Integer, Student> studentMap =
                                        studentDAO.getStudentMap();

                                System.out.println(
                                        "\n================================================"
                                );

                                System.out.println(
                                        "          CLASS RANKING BY AVERAGE"
                                );

                                System.out.println(
                                        "================================================"
                                );

                                int rank = 1;

                                for (Map.Entry<Integer, Double> entry : ranking) {

                                    int studentId = entry.getKey();
                                    double average = entry.getValue();

                                    Student rankedStudent =
                                            studentMap.get(studentId);

                                    String studentName =
                                            rankedStudent != null
                                                    ? rankedStudent.getName()
                                                    : "Unknown Student";

                                    System.out.printf(
                                            "%d. ID: %d | Name: %s | Average: %.2f%n",
                                            rank,
                                            studentId,
                                            studentName,
                                            average
                                    );

                                    rank++;
                                }

                                break;
                            }

                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // DEPARTMENT MANAGEMENT
                // ==========================================
                case 5: {

                    while (true) {

                        System.out.println(
                                "\n---------- DEPARTMENT MANAGEMENT ----------"
                        );

                        System.out.println("1. Add Department");
                        System.out.println("2. View All Departments");
                        System.out.println("3. Update Department");
                        System.out.println("4. Delete Department");
                        System.out.println("5. Back to Main Menu");

                        int departmentMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (departmentMenuChoice == 5) {
                            break;
                        }

                        switch (departmentMenuChoice) {

                            // ==================================
                            // ADD DEPARTMENT
                            // ==================================
                            case 1: {

                                Department newDepartment =
                                        new Department();

                                newDepartment.setDepartmentCode(
                                        readText(
                                                scanner,
                                                "Enter department code: "
                                        )
                                );

                                newDepartment.setDepartmentName(
                                        readText(
                                                scanner,
                                                "Enter department name: "
                                        )
                                );

                                departmentDAO.addDepartment(
                                        newDepartment
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL DEPARTMENTS
                            // ==================================
                            case 2: {

                                List<Department> departmentList =
                                        departmentDAO.getAllDepartments();

                                if (departmentList.isEmpty()) {

                                    System.out.println(
                                            "No departments found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- DEPARTMENT LIST -----------"
                                    );

                                    for (Department currentDepartment :
                                            departmentList) {

                                        currentDepartment.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // UPDATE DEPARTMENT
                            // ==================================
                            case 3: {

                                int updateDepartmentId =
                                        readInt(
                                                scanner,
                                                "Enter department ID to update: "
                                        );

                                Department departmentForUpdate =
                                        departmentDAO.getDepartmentById(
                                                updateDepartmentId
                                        );

                                if (departmentForUpdate != null) {

                                    departmentForUpdate.setDepartmentCode(
                                            readText(
                                                    scanner,
                                                    "Enter new department code: "
                                            )
                                    );

                                    departmentForUpdate.setDepartmentName(
                                            readText(
                                                    scanner,
                                                    "Enter new department name: "
                                            )
                                    );

                                    departmentDAO.updateDepartment(
                                            departmentForUpdate
                                    );

                                } else {

                                    System.out.println(
                                            "Department not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // DELETE DEPARTMENT
                            // ==================================
                            case 4: {

                                int deleteDepartmentId =
                                        readInt(
                                                scanner,
                                                "Enter department ID to delete: "
                                        );

                                Department departmentForDelete =
                                        departmentDAO.getDepartmentById(
                                                deleteDepartmentId
                                        );

                                if (departmentForDelete != null) {

                                    if (confirmDelete(scanner)) {

                                        departmentDAO.deleteDepartment(
                                                deleteDepartmentId
                                        );
                                    }

                                } else {

                                    System.out.println(
                                            "Department not found."
                                    );
                                }

                                break;
                            }

                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // TEACHER MANAGEMENT
                // ==========================================
                case 6: {

                    while (true) {

                        System.out.println(
                                "\n---------- TEACHER MANAGEMENT ----------"
                        );

                        System.out.println("1. Add Teacher");
                        System.out.println("2. View All Teachers");
                        System.out.println("3. Search Teacher by Name");
                        System.out.println("4. Update Teacher");
                        System.out.println("5. Delete Teacher");
                        System.out.println("6. Assign Teacher to Course");
                        System.out.println("7. View Courses Taught by Teacher");
                        System.out.println("8. Back to Main Menu");

                        int teacherMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (teacherMenuChoice == 8) {
                            break;
                        }

                        switch (teacherMenuChoice) {

                            // ==================================
                            // ADD TEACHER
                            // ==================================
                            case 1: {

                                Teacher newTeacher =
                                        new Teacher();

                                newTeacher.setName(
                                        readText(
                                                scanner,
                                                "Enter teacher name: "
                                        )
                                );

                                newTeacher.setEmail(
                                        readText(
                                                scanner,
                                                "Enter email: "
                                        )
                                );

                                newTeacher.setPhone(
                                        readText(
                                                scanner,
                                                "Enter phone: "
                                        )
                                );

                                newTeacher.setDesignation(
                                        readText(
                                                scanner,
                                                "Enter designation: "
                                        )
                                );

                                newTeacher.setDepartmentId(
                                        readInt(
                                                scanner,
                                                "Enter department ID (0 for none): "
                                        )
                                );

                                teacherDAO.addTeacher(
                                        newTeacher
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL TEACHERS
                            // ==================================
                            case 2: {

                                List<Teacher> teacherList =
                                        teacherDAO.getAllTeachers();

                                if (teacherList.isEmpty()) {

                                    System.out.println(
                                            "No teachers found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- TEACHER LIST -----------"
                                    );

                                    for (Teacher currentTeacher :
                                            teacherList) {

                                        currentTeacher.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // SEARCH TEACHER BY NAME
                            // ==================================
                            case 3: {

                                String searchTeacherName =
                                        readText(
                                                scanner,
                                                "Enter teacher name: "
                                        );

                                List<Teacher> matchingTeachers =
                                        teacherDAO.getTeachersByName(
                                                searchTeacherName
                                        );

                                if (matchingTeachers.isEmpty()) {

                                    System.out.println(
                                            "No teacher found with that name."
                                    );

                                } else {

                                    System.out.println(
                                            "\nTeachers found:"
                                    );

                                    for (Teacher currentTeacher :
                                            matchingTeachers) {

                                        currentTeacher.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // UPDATE TEACHER
                            // ==================================
                            case 4: {

                                int updateTeacherId =
                                        readInt(
                                                scanner,
                                                "Enter teacher ID to update: "
                                        );

                                Teacher teacherForUpdate =
                                        teacherDAO.getTeacherById(
                                                updateTeacherId
                                        );

                                if (teacherForUpdate != null) {

                                    teacherForUpdate.setName(
                                            readText(
                                                    scanner,
                                                    "Enter new name: "
                                            )
                                    );

                                    teacherForUpdate.setEmail(
                                            readText(
                                                    scanner,
                                                    "Enter new email: "
                                            )
                                    );

                                    teacherForUpdate.setPhone(
                                            readText(
                                                    scanner,
                                                    "Enter new phone: "
                                            )
                                    );

                                    teacherForUpdate.setDesignation(
                                            readText(
                                                    scanner,
                                                    "Enter new designation: "
                                            )
                                    );

                                    teacherForUpdate.setDepartmentId(
                                            readInt(
                                                    scanner,
                                                    "Enter new department ID (0 for none): "
                                            )
                                    );

                                    teacherDAO.updateTeacher(
                                            teacherForUpdate
                                    );

                                } else {

                                    System.out.println(
                                            "Teacher not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // DELETE TEACHER
                            // ==================================
                            case 5: {

                                int deleteTeacherId =
                                        readInt(
                                                scanner,
                                                "Enter teacher ID to delete: "
                                        );

                                Teacher teacherForDelete =
                                        teacherDAO.getTeacherById(
                                                deleteTeacherId
                                        );

                                if (teacherForDelete != null) {

                                    if (confirmDelete(scanner)) {

                                        teacherDAO.deleteTeacher(
                                                deleteTeacherId
                                        );
                                    }

                                } else {

                                    System.out.println(
                                            "Teacher not found."
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // ASSIGN TEACHER TO COURSE
                            // ==================================
                            case 6: {

                                int assignTeacherId =
                                        readInt(
                                                scanner,
                                                "Enter teacher ID: "
                                        );

                                int assignCourseId =
                                        readInt(
                                                scanner,
                                                "Enter course ID: "
                                        );

                                teacherDAO.assignTeacherToCourse(
                                        assignTeacherId,
                                        assignCourseId
                                );

                                break;
                            }


                            // ==================================
                            // VIEW COURSES TAUGHT BY TEACHER
                            // ==================================
                            case 7: {

                                int viewTeacherId =
                                        readInt(
                                                scanner,
                                                "Enter teacher ID: "
                                        );

                                List<Course> teacherCourses =
                                        teacherDAO.getCoursesByTeacherId(
                                                viewTeacherId
                                        );

                                if (teacherCourses.isEmpty()) {

                                    System.out.println(
                                            "No courses found for this teacher."
                                    );

                                } else {

                                    System.out.println(
                                            "\nCourses taught:"
                                    );

                                    for (Course currentCourse :
                                            teacherCourses) {

                                        System.out.println(
                                                currentCourse.getCourseCode()
                                                        + " - "
                                                        + currentCourse.getCourseName()
                                        );
                                    }
                                }

                                break;
                            }

                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // ATTENDANCE MANAGEMENT
                // ==========================================
                case 7: {

                    while (true) {

                        System.out.println(
                                "\n---------- ATTENDANCE MANAGEMENT ----------"
                        );

                        System.out.println("1. Mark Attendance");
                        System.out.println("2. View All Attendance");
                        System.out.println("3. View Attendance by Student");
                        System.out.println("4. Delete Attendance Record");
                        System.out.println("5. Attendance Percentage Report");
                        System.out.println("6. Back to Main Menu");

                        int attendanceMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (attendanceMenuChoice == 6) {
                            break;
                        }

                        switch (attendanceMenuChoice) {

                            // ==================================
                            // MARK ATTENDANCE
                            // ==================================
                            case 1: {

                                Attendance newAttendance =
                                        new Attendance();

                                newAttendance.setStudentId(
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        )
                                );

                                newAttendance.setCourseId(
                                        readInt(
                                                scanner,
                                                "Enter course ID: "
                                        )
                                );

                                System.out.print(
                                        "Enter attendance date (YYYY-MM-DD): "
                                );

                                newAttendance.setAttendanceDate(
                                        scanner.nextLine().trim()
                                );

                                newAttendance.setStatus(
                                        readText(
                                                scanner,
                                                "Enter status (Present / Absent / Late): "
                                        )
                                );

                                attendanceDAO.addAttendance(
                                        newAttendance
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL ATTENDANCE
                            // ==================================
                            case 2: {

                                List<Attendance> attendanceList =
                                        attendanceDAO.getAllAttendance();

                                if (attendanceList.isEmpty()) {

                                    System.out.println(
                                            "No attendance records found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- ATTENDANCE LIST -----------"
                                    );

                                    for (Attendance currentAttendance :
                                            attendanceList) {

                                        currentAttendance.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // VIEW ATTENDANCE BY STUDENT
                            // ==================================
                            case 3: {

                                int viewStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        );

                                List<Attendance> studentAttendance =
                                        attendanceDAO.getAttendanceByStudentId(
                                                viewStudentId
                                        );

                                if (studentAttendance.isEmpty()) {

                                    System.out.println(
                                            "No attendance records found " +
                                                    "for this student."
                                    );

                                } else {

                                    for (Attendance currentAttendance :
                                            studentAttendance) {

                                        currentAttendance.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // DELETE ATTENDANCE RECORD
                            // ==================================
                            case 4: {

                                int deleteAttendanceId =
                                        readInt(
                                                scanner,
                                                "Enter attendance ID to delete: "
                                        );

                                if (confirmDelete(scanner)) {

                                    attendanceDAO.deleteAttendance(
                                            deleteAttendanceId
                                    );
                                }

                                break;
                            }


                            // ==================================
                            // ATTENDANCE PERCENTAGE REPORT
                            // ==================================
                            case 5: {

                                int reportStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        );

                                int reportCourseId =
                                        readInt(
                                                scanner,
                                                "Enter course ID: "
                                        );

                                attendanceDAO.getAttendancePercentage(
                                        reportStudentId,
                                        reportCourseId
                                );

                                break;
                            }

                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // FEE MANAGEMENT
                // ==========================================
                case 8: {

                    while (true) {

                        System.out.println(
                                "\n---------- FEE MANAGEMENT ----------"
                        );

                        System.out.println("1. Add Fee Record");
                        System.out.println("2. View All Fee Records");
                        System.out.println("3. View Fee Records by Student");
                        System.out.println("4. Mark Fee as Paid");
                        System.out.println("5. Delete Fee Record");
                        System.out.println("6. Back to Main Menu");

                        int feeMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (feeMenuChoice == 6) {
                            break;
                        }

                        switch (feeMenuChoice) {

                            // ==================================
                            // ADD FEE RECORD
                            // ==================================
                            case 1: {

                                Fee newFee =
                                        new Fee();

                                newFee.setStudentId(
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        )
                                );

                                newFee.setAmount(
                                        readDouble(
                                                scanner,
                                                "Enter fee amount: "
                                        )
                                );

                                System.out.print(
                                        "Enter due date (YYYY-MM-DD): "
                                );

                                newFee.setDueDate(
                                        scanner.nextLine().trim()
                                );

                                feeDAO.addFee(
                                        newFee
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL FEE RECORDS
                            // ==================================
                            case 2: {

                                List<Fee> feeList =
                                        feeDAO.getAllFees();

                                if (feeList.isEmpty()) {

                                    System.out.println(
                                            "No fee records found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- FEE RECORDS -----------"
                                    );

                                    for (Fee currentFee :
                                            feeList) {

                                        currentFee.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // VIEW FEE RECORDS BY STUDENT
                            // ==================================
                            case 3: {

                                int viewStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        );

                                List<Fee> studentFees =
                                        feeDAO.getFeesByStudentId(
                                                viewStudentId
                                        );

                                if (studentFees.isEmpty()) {

                                    System.out.println(
                                            "No fee records found for " +
                                                    "this student."
                                    );

                                } else {

                                    for (Fee currentFee :
                                            studentFees) {

                                        currentFee.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // MARK FEE AS PAID
                            // ==================================
                            case 4: {

                                int payFeeId =
                                        readInt(
                                                scanner,
                                                "Enter fee record ID: "
                                        );

                                System.out.print(
                                        "Enter paid date (YYYY-MM-DD): "
                                );

                                String paymentDate =
                                        scanner.nextLine().trim();

                                String paymentMethod =
                                        readText(
                                                scanner,
                                                "Enter payment method " +
                                                        "(e.g. Cash, Esewa, Bank Transfer): "
                                        );

                                feeDAO.markFeeAsPaid(
                                        payFeeId,
                                        paymentDate,
                                        paymentMethod
                                );

                                break;
                            }


                            // ==================================
                            // DELETE FEE RECORD
                            // ==================================
                            case 5: {

                                int deleteFeeId =
                                        readInt(
                                                scanner,
                                                "Enter fee record ID to delete: "
                                        );

                                if (confirmDelete(scanner)) {

                                    feeDAO.deleteFee(
                                            deleteFeeId
                                    );
                                }

                                break;
                            }

                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // COMPLAINT MANAGEMENT
                // ==========================================
                case 9: {

                    while (true) {

                        System.out.println(
                                "\n---------- COMPLAINT MANAGEMENT ----------"
                        );

                        System.out.println("1. Add Complaint");
                        System.out.println("2. View All Complaints");
                        System.out.println("3. View Complaints by Student");
                        System.out.println("4. Back to Main Menu");

                        int complaintMenuChoice =
                                readInt(
                                        scanner,
                                        "Enter your choice: "
                                );

                        if (complaintMenuChoice == 4) {
                            break;
                        }

                        switch (complaintMenuChoice) {

                            // ==================================
                            // ADD COMPLAINT
                            // ==================================
                            case 1: {

                                Complaint newComplaint =
                                        new Complaint();

                                newComplaint.setStudentId(
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        )
                                );

                                newComplaint.setSubject(
                                        readText(
                                                scanner,
                                                "Enter complaint subject: "
                                        )
                                );

                                newComplaint.setDescription(
                                        readText(
                                                scanner,
                                                "Enter complaint description: "
                                        )
                                );

                                System.out.print(
                                        "Enter complaint date (YYYY-MM-DD): "
                                );

                                newComplaint.setComplaintDate(
                                        scanner.nextLine().trim()
                                );

                                complaintDAO.addComplaint(
                                        newComplaint
                                );

                                break;
                            }


                            // ==================================
                            // VIEW ALL COMPLAINTS
                            // ==================================
                            case 2: {

                                List<Complaint> complaintList =
                                        complaintDAO.getAllComplaints();

                                if (complaintList.isEmpty()) {

                                    System.out.println(
                                            "No complaints found."
                                    );

                                } else {

                                    System.out.println(
                                            "\n----------- COMPLAINT LIST -----------"
                                    );

                                    for (Complaint currentComplaint :
                                            complaintList) {

                                        currentComplaint.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }


                            // ==================================
                            // VIEW COMPLAINTS BY STUDENT
                            // ==================================
                            case 3: {

                                int viewStudentId =
                                        readInt(
                                                scanner,
                                                "Enter student ID: "
                                        );

                                List<Complaint> studentComplaints =
                                        complaintDAO.getComplaintsByStudentId(
                                                viewStudentId
                                        );

                                if (studentComplaints.isEmpty()) {

                                    System.out.println(
                                            "No complaints found for " +
                                                    "this student."
                                    );

                                } else {

                                    for (Complaint currentComplaint :
                                            studentComplaints) {

                                        currentComplaint.displayInfo();

                                        System.out.println(
                                                "------------------------------------"
                                        );
                                    }
                                }

                                break;
                            }

                            default:

                                System.out.println(
                                        "Invalid choice."
                                );
                        }
                    }

                    break;
                }


                // ==========================================
                // EXIT
                // ==========================================
                case 10: {

                    System.out.println(
                            "\nThank you for using " +
                                    "Student Management System!"
                    );

                    scanner.close();

                    return;
                }


                default:

                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }


    // ==========================================
    // SAFE INTEGER INPUT
    // ==========================================
    public static int readInt(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. " +
                                "Please enter a whole number."
                );
            }
        }
    }


    // ==========================================
    // SAFE DOUBLE INPUT
    // ==========================================
    public static double readDouble(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                return Double.parseDouble(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. " +
                                "Please enter a valid number."
                );
            }
        }
    }


    // ==========================================
    // NON-EMPTY TEXT INPUT
    // ==========================================
    public static String readText(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (!input.isEmpty()) {

                return input;
            }

            System.out.println(
                    "Input cannot be empty. " +
                            "Please try again."
            );
        }
    }


    // ==========================================
    // DATE INPUT
    // ==========================================
    public static String readDate(
            Scanner scanner) {

        while (true) {

            System.out.print(
                    "Enter enrollment date (YYYY-MM-DD): "
            );

            String input =
                    scanner.nextLine().trim();

            try {

                LocalDate.parse(input);

                return input;

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date format."
                );

                System.out.println(
                        "Please use YYYY-MM-DD."
                );
            }
        }
    }


    // ==========================================
    // AUTOMATIC GRADE CALCULATION
    // ==========================================
    public static String calculateGrade(
            double mark) {

        if (mark >= 90) {

            return "A+";

        } else if (mark >= 80) {

            return "A";

        } else if (mark >= 70) {

            return "B+";

        } else if (mark >= 60) {

            return "B";

        } else if (mark >= 50) {

            return "C+";

        } else if (mark >= 40) {

            return "C";

        } else {

            return "F";
        }
    }


    // ==========================================
    // DELETE CONFIRMATION
    // ==========================================
    public static boolean confirmDelete(
            Scanner scanner) {

        while (true) {

            System.out.print(
                    "Are you sure you want to delete? (yes/no): "
            );

            String answer =
                    scanner.nextLine()
                            .trim()
                            .toLowerCase();

            if (answer.equals("yes") ||
                    answer.equals("y")) {

                return true;

            } else if (answer.equals("no") ||
                    answer.equals("n")) {

                System.out.println(
                        "Delete operation cancelled."
                );

                return false;

            } else {

                System.out.println(
                        "Please enter yes or no."
                );
            }
        }
    }
}