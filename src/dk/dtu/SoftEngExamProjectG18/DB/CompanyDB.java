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
        return this.employees.getOrDefault(ID, null);
    }

    public HashMap<String, Employee> getEmployees() {
        return employees;
    }

    public int incrementNextProjectID(int year) {
        if(!nextProjectID.containsKey(year)) {
            nextProjectID.put(year, 1);
        }

        int nextID = nextProjectID.get(year);
        nextProjectID.put(year, nextID + 1);

        return nextID;
    }

    public HashMap<String, Project> getProjects() {
        return projects;
    }

    public Employee getSignedInEmployee() {
        return signedInEmployee;
    }

    public boolean setSignedInEmployee(String ID) {
        if(this.employees.containsKey(ID)) {
            this.signedInEmployee = this.employees.get(ID);
            return true;
        }

        return false;
    }

}
