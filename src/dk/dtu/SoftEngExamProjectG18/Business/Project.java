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

    /**
     * @author Someone
     */
    protected void setupActivity(String name) {
        new Activity(name == null ? "First Activity" : name, this);
    }

    /**
     * @author Someone
     */
    protected void setupCreatedAt(Date createdAt) {
        Calendar cal = new GregorianCalendar();

        if (createdAt != null) {
            cal.setTime(createdAt);
        }

        this.createdAt = cal;
    }

    /**
     * @author Someone
     */
    protected void setupName(String name) throws IllegalArgumentException {
        if (name.length() == 0) {
            throw new IllegalArgumentException(String.format("The given project name, %s, is not valid.", name));
        }

        this.name = name;
    }

    /**
     * @author Someone
     */
    public Project(int nextID, String name) throws IllegalArgumentException {
        this.ID = nextID;
        this.setupName(name);
        this.setupActivity(null);
    }

    /**
     * @author Someone
     */
    public Project(int nextID, String name, boolean isBillable) throws IllegalArgumentException {
        this(nextID, name);
        this.isBillable = isBillable;
    }

    /**
     * @author Someone
     */
    public Project(int nextID, String name, Date createdAt, boolean isBillable, Employee PM) {
        this.ID = nextID;
        this.setupName(name);

        this.isBillable = isBillable;
        this.PM = PM;

        this.setupCreatedAt(createdAt);

        // Do not setup activity if data is imported through CSVReader
    }

    /**
     * @author Someone
     */
    public void assertPM(Employee employee) throws AccessDeniedException {
        if (this.getPM() == null || !this.getPM().equals(employee)) {
            throw new AccessDeniedException("Project manager role required.");
        }
    }

    /**
     * @author Thor Dueholm (s194589)
     */
    public void assignPM(Employee newPM, Employee signedInEmployee) throws AccessDeniedException {
        if (this.PM != null && signedInEmployee != this.PM) {
            throw new AccessDeniedException("Project manager role required.");
        }

        assert this.PM == null || signedInEmployee == this.PM : "Precondition of assignPM";

        this.PM = newPM;

        assert this.PM == newPM : "Postcondition of assignPM";
    }

    /**
     * @author Someone
     */
    public void clearActivities() {
        this.getActivities().clear();
        this.nextActivityID = 1;
    }

    /**
     * @author Someone
     */
    public int incrementNextActivityID() {
        this.nextActivityID++;
        return this.nextActivityID - 1;
    }

    /**
     * @author Someone
     */
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

    /**
     * @author Someone
     */
    public HashMap<Integer, Activity> getActivities() {
        return this.activities;
    }

    /**
     * @author Someone
     */
    public int getEstimatedWorkHoursLeft() {
        double minutesLeft = this.getActivities().values().stream()
            .mapToInt(a -> Math.max(a.getEstimatedHours() * 60 - a.getTotalTrackedMinutes(), 0)).sum();

        return (int) Math.ceil(minutesLeft / 60.0);
    }

    /**
     * @author Someone
     */
    public String getID() {
        StringBuilder IDBuilder = new StringBuilder();
        IDBuilder.append(this.ID);

        while (IDBuilder.length() < 6) {
            IDBuilder.append("0");
        }

        return this.createdAt.get(Calendar.YEAR) + "-" + IDBuilder.reverse().toString();
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    public String getName() {
        return this.name;
    }

    /**
     * @author Someone
     */
    public Employee getPM() {
        return this.PM;
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    public void setPM(Employee PM) {
        this.PM = PM;
    }
}
