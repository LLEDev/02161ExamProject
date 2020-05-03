package dk.dtu.SoftEngExamProjectG18.Persistence;

import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;

import java.util.HashMap;
import java.util.function.Supplier;

public class CompanyDB {

    protected HashMap<String, Employee> employees = new HashMap<>();
    protected HashMap<Integer, Integer> nextProjectID = new HashMap<>();
    protected HashMap<String, Project> projects = new HashMap<>();
    protected Employee signedInEmployee;

    protected <X extends Throwable> void assertOrThrow(Supplier<? extends X> exceptionSupplier, boolean statement) throws X {
        X throwable = exceptionSupplier.get();

        if(!statement) {
            throw throwable;
        }
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

    public HashMap<String, Project> getProjects() {
        return this.projects;
    }

    public Employee getSignedInEmployee() throws IllegalStateException {
        this.assertOrThrow(
            () -> new IllegalStateException("No employee signed in."),
            this.signedInEmployee != null
        );

        return this.signedInEmployee;
    }

    public void setSignedInEmployee(String ID) {
        this.assertOrThrow(
            () -> new IllegalArgumentException("Given employee does not exist."),
            this.employees.containsKey(ID)
        );

        this.signedInEmployee = this.employees.get(ID);
    }

}
