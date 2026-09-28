package model;

public class Attendance {

    private int id;
    private int studentId;
    private String studentName;
    private int courseId;
    private String courseName;
    private String attendanceDate;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayInfo() {
        System.out.println("Attendance ID: " + id);
        System.out.println("Student ID: " + studentId
                + (studentName != null ? " (" + studentName + ")" : ""));
        System.out.println("Course ID: " + courseId
                + (courseName != null ? " (" + courseName + ")" : ""));
        System.out.println("Date: " + attendanceDate);
        System.out.println("Status: " + status);
    }
}
