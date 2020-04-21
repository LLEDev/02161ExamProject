package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import io.cucumber.java.ca.Cal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Project {

    protected HashMap<Integer, Activity> activities = new HashMap<>();
    protected Calendar createdAt;
    protected int ID;
    protected boolean isBillable = true;
    protected String name;
    protected int nextActivityID = 1;
    protected Employee PM = null;

    protected void setupActivity(String name) {
        new Activity(name == null ? "First Activity" : name, this);
    }

    protected void setupCreatedAt(Date createdAt) {
        Calendar cal = new GregorianCalendar();

        if(createdAt != null) {
            cal.setTime(createdAt);
        }

        this.createdAt = cal;
    }

    protected void setupID() {
        this.ID = CompanyDB.getInstance().incrementNextProjectID(this.createdAt.get(Calendar.YEAR));
    }

    public Project(String name) {
        this.name = name;

        this.setupCreatedAt(null);
        this.setupID();
        this.setupActivity(null);
    }

    public Project(String name, boolean isBillable) {
        this.isBillable = isBillable;
        this.name = name;

        this.setupCreatedAt(null);
        this.setupID();
        this.setupActivity(null);
    }

    public Project(String name, Date createdAt, boolean isBillable) {
        this.isBillable = isBillable;
        this.name = name;

        this.setupCreatedAt(createdAt);
        this.setupID();
        this.setupActivity(null);
    }

    public Project(String name, Date createdAt, boolean isBillable, Employee PM) {
        this.isBillable = isBillable;
        this.name = name;
        this.PM = PM;

        this.setupCreatedAt(createdAt);
        this.setupID();
        this.setupActivity(null);
    }

    public int incrementNextActivityID() {
        this.nextActivityID++;
        return this.nextActivityID - 1;
    }

    public boolean assignPM(Employee employee) {
        if (this.PM == null) {
            this.PM = employee;
            return true;
        }

        CompanyDB db = CompanyDB.getInstance();
        Employee signedInEmployee = db.getSignedInEmployee();

        if (signedInEmployee == this.PM) {
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
        StringBuilder IDBuilder = new StringBuilder();
        IDBuilder.append(this.ID);

        while(IDBuilder.length() < 6) {
            IDBuilder.append("0");
        }

        return this.createdAt.get(Calendar.YEAR) + "-" + IDBuilder.reverse().toString();
    }

    public String getName() {
        return this.name;
    }

    public Employee getPM() {
        return this.PM;
    }

    public void setPM(Employee PM) {
        this.PM = PM;
    }
}
