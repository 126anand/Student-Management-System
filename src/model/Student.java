package model;

public class Student extends Person {

    private String phone;
    private String address;
    private int departmentId;
    private String departmentName;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        System.out.println("Student ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + phone);
        System.out.println("Address: " + address);

        if (departmentName != null) {
            System.out.println("Department: " + departmentName);
        } else if (departmentId > 0) {
            System.out.println("Department ID: " + departmentId);
        } else {
            System.out.println("Department: Not assigned");
        }
    }
}