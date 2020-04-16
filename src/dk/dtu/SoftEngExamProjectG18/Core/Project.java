package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import io.cucumber.java.ca.Cal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Project {

    protected HashMap<Integer, Activity> activities = new HashMap<>();
    protected Date createdAt = new Date(); // TODO: should this be dynamic?
    protected int ID;
    protected boolean isBillable = true;
    protected String name;
    protected int nextActivityID = 1;
    protected Employee PM = null;

    protected void setupID() {
        this.ID = CompanyDB.getInstance().incrementNextProjectID(this.createdAt.getYear());
    }

    public Project(String name) {
        this.name = name;

        this.setupID();
    }

    public Project(String name, boolean isBillable) {
        this.isBillable = isBillable;
        this.name = name;

        this.setupID();
    }

    public Project(String name, Date createdAt, boolean isBillable) {
        this.createdAt = createdAt;
        this.isBillable = isBillable;
        this.name = name;

        this.setupID();
    }

    public Project(String name, Date createdAt, boolean isBillable, Employee PM) {
        this.createdAt = createdAt;
        this.isBillable = isBillable;
        this.name = name;
        this.PM = PM;

        this.setupID();
    }

    public int incrementNextActivityID() {
        this.nextActivityID++;
        return this.nextActivityID - 1;
    }

    public boolean assignPM(Employee employee) throws Exception {
        if (this.PM==null) {
            this.PM = employee;
            return true;
        }
        CompanyDB db = CompanyDB.getInstance();
        Employee signedInEmployee = db.getSignedInEmployee();
        if (signedInEmployee==this.PM) {
            this.PM = employee;
            return true;
        }

        return false;
    }

    public Activity getActivity(int ID) {
        return this.activities.get(ID);
    }

    public HashMap<Integer, Activity> getActivities() {
        return this.activities;
    }

    public String getID() {
        Calendar c = new GregorianCalendar();
        c.setTime(this.createdAt);
        return c.get(Calendar.YEAR) + "-" + this.ID;
    }

    public Employee getPM(){
        return this.PM;
    }

    public String getName() {
        return this.name;
    }
}
