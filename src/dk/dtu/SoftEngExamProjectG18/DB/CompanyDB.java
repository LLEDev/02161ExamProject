package dk.dtu.SoftEngExamProjectG18.DB;

import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;

import java.util.HashMap;

public class CompanyDB {

    private static CompanyDB instance;

    public static CompanyDB getInstance() {
        if (instance == null) {
            instance = new CompanyDB();
        }

        return instance;
    }

    private HashMap<String, Employee> employees = new HashMap<>();
    private HashMap<Integer, Integer> nextProjectID = new HashMap<>();
    private HashMap<String, Project> projects = new HashMap<>();
    private Employee signedInEmployee;

    private CompanyDB() {}

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

    public Employee getSignedInEmployee() {
        return this.signedInEmployee;
    }

    public boolean setSignedInEmployee(String ID) {
        if(this.employees.containsKey(ID)) {
            this.signedInEmployee = this.employees.get(ID);
            return true;
        }

        return false;
    }

}
