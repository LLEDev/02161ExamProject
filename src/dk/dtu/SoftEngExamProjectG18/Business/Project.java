package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.AccessDeniedException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Project {

    protected HashMap<Integer, Activity> activities = new HashMap<>();
    protected Calendar createdAt = new GregorianCalendar();
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

        if (createdAt != null) {
            cal.setTime(createdAt);
        }

        this.createdAt = cal;
    }

    protected void setupName(String name) throws IllegalArgumentException {
        if (name.length() == 0) {
            throw new IllegalArgumentException(String.format("The given project name, %s, is not valid.", name));
        }

        this.name = name;
    }

    public Project(int nextID, String name) throws IllegalArgumentException {
        this.ID = nextID;
        this.setupName(name);
        this.setupActivity(null);
    }

    public Project(int nextID, String name, boolean isBillable) throws IllegalArgumentException {
        this(nextID, name);
        this.isBillable = isBillable;
    }

    public Project(int nextID, String name, Date createdAt, boolean isBillable, Employee PM) {
        this.ID = nextID;
        this.setupName(name);

        this.isBillable = isBillable;
        this.PM = PM;

        this.setupCreatedAt(createdAt);

        // Do not setup activity if data is imported through CSVReader
    }

    public void assertPM(Employee employee) throws AccessDeniedException {
        if (this.getPM() == null || !this.getPM().equals(employee)) {
            throw new AccessDeniedException("Project manager role required.");
        }
    }

    public void assignPM(Employee newPM, Employee signedInEmployee) throws AccessDeniedException {
        if (this.PM != null && signedInEmployee != this.PM) {
            throw new AccessDeniedException("Project manager role required.");
        }
        assert (this.PM == null || signedInEmployee == this.PM) : "Precondition of assignPM";

        this.PM = newPM;

        assert (this.PM == newPM) : "Postcondition of assignPM";
    }

    public void clearActivities() {
        this.getActivities().clear();
        this.nextActivityID = 1;
    }

    public int incrementNextActivityID() {
        this.nextActivityID++;
        return this.nextActivityID - 1;
    }

    public Activity getActivity(int ID) {
        if (!this.activities.containsKey(ID)) {
            String eMsg = String.format(
                "The given activity, %s, does not exist within project, %s.",
                ID,
                this.getID()
            );
            throw new IllegalArgumentException(eMsg);
        }

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

        while (IDBuilder.length() < 6) {
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
