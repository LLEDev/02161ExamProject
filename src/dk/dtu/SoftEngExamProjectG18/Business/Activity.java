package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.General.Dates;

import java.util.Date;
import java.util.HashMap;

public class Activity {

    protected int ID;
    protected String name;
    protected Project project;

    protected Date endWeek = null;
    protected Date startWeek = null;
    protected int estimatedHours = 0;

    protected boolean isDone = false;

    // EmployeeID --> EmployeeActivityIntermediate
    protected HashMap<String, EmployeeActivityIntermediate> trackedTime = new HashMap<>();

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public Activity(String name, Project project) {
        this.ID = project.incrementNextActivityID();
        this.name = name;
        this.project = project;

        project.getActivities().put(this.ID, this);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public Date getEndWeek() {
        return this.endWeek;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public int getEstimatedHours() {
        return this.estimatedHours;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public int getID() {
        return this.ID;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public String getName() {
        return this.name;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public Project getProject() {
        return this.project;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public Date getStartWeek() {
        return this.startWeek;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public int getTotalTrackedMinutes() {
        return this.getTrackedTime().values().stream().mapToInt(EmployeeActivityIntermediate::getTotalMinutes).sum();
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public HashMap<String, EmployeeActivityIntermediate> getTrackedTime() {
        return this.trackedTime;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void setDone(boolean done) {
        this.isDone = done;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void setEndWeek(Date endWeek) throws IllegalArgumentException {
        Dates.assertStartEndValid(this.startWeek, endWeek);

        this.endWeek = endWeek;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void setEstimatedHours(int estimatedHours) throws IllegalArgumentException {
        if (estimatedHours <= 0) {
            String output = String.format("The estimated number of work hours has to be bigger than 0. %s received.", estimatedHours);
            throw new IllegalArgumentException(output);
        }

        this.estimatedHours = estimatedHours;
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public void setStartWeek(Date startWeek) throws IllegalArgumentException {
        Dates.assertStartEndValid(startWeek, this.endWeek);

        this.startWeek = startWeek;
    }
}
