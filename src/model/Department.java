package model;

public class Department {

    private int id;
    private String departmentCode;
    private String departmentName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void displayInfo() {
        System.out.println("Department ID: " + id);
        System.out.println("Department Code: " + departmentCode);
        System.out.println("Department Name: " + departmentName);
    }
}
