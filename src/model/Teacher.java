package model;

public class Teacher extends Person {

    private String phone;
    private String designation;
    private int departmentId;
    private String departmentName;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public void displayInfo() {
        System.out.println("Teacher ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + phone);
        System.out.println("Designation: " + designation);

        if (departmentName != null) {
            System.out.println("Department: " + departmentName);
        } else if (departmentId > 0) {
            System.out.println("Department ID: " + departmentId);
        } else {
            System.out.println("Department: Not assigned");
        }
    }
}
