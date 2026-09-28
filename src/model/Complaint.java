package model;

public class Complaint {

    private int id;
    private int studentId;
    private String studentName;
    private String subject;
    private String description;
    private String complaintDate;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayInfo() {
        System.out.println("Complaint ID: " + id);
        System.out.println("Student ID: " + studentId
                + (studentName != null ? " (" + studentName + ")" : ""));
        System.out.println("Subject: " + subject);
        System.out.println("Description: " + description);
        System.out.println("Date Filed: " + complaintDate);
        System.out.println("Status: " + status);
    }
}
