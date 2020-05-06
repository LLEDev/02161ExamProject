package dk.dtu.SoftEngExamProjectG18.Persistence;

import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.General.Assertions;

import java.util.HashMap;
import java.util.function.Supplier;

public class CompanyDB {

    protected HashMap<String, Employee> employees = new HashMap<>();
    protected HashMap<Integer, Integer> nextProjectID = new HashMap<>();
    protected HashMap<String, Project> projects = new HashMap<>();
    protected Employee signedInEmployee;

    public void addEmployee(Employee employee) {
        this.employees.put(employee.getID(), employee);
    }

    public void addProject(Project project) {
        this.projects.put(project.getID(), project);
    }

    public Employee getEmployee(String ID) {
        return this.employees.get(ID);
    }

    public HashMap<String, Employee> getEmployees() {
        return this.employees;
    }

    public int incrementNextProjectID(int year) {
        if(!this.nextProjectID.containsKey(year)) {
            this.nextProjectID.put(year, 1);
        }

        int nextID = this.nextProjectID.get(year);
        this.nextProjectID.put(year, nextID + 1);

        return nextID;
    }

    public Project getProject(String ID) {
        return this.projects.get(ID);
    }

    public Employee getSignedInEmployee() throws IllegalStateException {
        Assertions.assertOrThrow(
            () -> new IllegalStateException("No employee signed in."),
            this.signedInEmployee != null
        );

        return this.signedInEmployee;
    }

    public void setSignedInEmployee(String ID) {
        Assertions.assertOrThrow(
            () -> new IllegalArgumentException("Given employee does not exist."),
            this.employees.containsKey(ID)
        );

        this.signedInEmployee = this.employees.get(ID);
    }

}
