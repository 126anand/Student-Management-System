package model;

public class Fee {

    private int id;
    private int studentId;
    private String studentName;
    private double amount;
    private String dueDate;
    private String paidDate;
    private String status;
    private String paymentMethod;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void displayInfo() {
        System.out.println("Fee Record ID: " + id);
        System.out.println("Student ID: " + studentId
                + (studentName != null ? " (" + studentName + ")" : ""));
        System.out.println("Amount: " + amount);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Paid Date: "
                + (paidDate != null ? paidDate : "Not paid yet"));
        System.out.println("Status: " + status);
        System.out.println("Payment Method: "
                + (paymentMethod != null ? paymentMethod : "N/A"));
    }
}
