package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Interfaces.Extractable;

import java.util.*;

public class Project implements Extractable<Project> {

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

    public Project(String name, Date createdAt, boolean isBillable, Employee PM, boolean setupActivity) {
        this.isBillable = isBillable;
        this.name = name;
        this.PM = PM;

        this.setupCreatedAt(createdAt);
        this.setupID();

        if(setupActivity) { // Do not setup activity if data is imported through CSVReader
            this.setupActivity(null);
        }
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

    public int getEstimatedWorkHoursLeft() {
        double minutesLeft = this.getActivities().values().stream()
                .mapToInt(a -> Math.max(a.getEstimatedHours() * 60 - a.getTotalTrackedMinutes(), 0)).sum();

        return (int) Math.ceil(minutesLeft / 60.0);
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

    /*
        Table extraction methods
     */

    @Override
    public ArrayList<HashMap<String, String>> extract(String context, HashMap<String, Object> metaData, ArrayList<? extends Extractable<?>> collection) {
        if(context.equals("overview")) {
            return this.extractOverview(collection);
        }

        return null;
    }

    public ArrayList<HashMap<String, String>> extractOverview(ArrayList<? extends Extractable<?>> collection) {
        return null;
    }
}
