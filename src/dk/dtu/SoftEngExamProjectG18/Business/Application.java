package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.Enum.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Persistence.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.Util.DateFormatter;

import java.text.ParseException;
import java.util.*;

public class Application {

    protected static Application instance;

    public static void init(String context) throws IllegalArgumentException {
        InputContextType ic = InputContextType.valueOf(context);

        init(ic);
    }

    public static void init(InputContextType contextType) throws IllegalArgumentException {
        if(contextType == null) {
            throw new IllegalArgumentException("Invalid context given.");
        }

        instance = new Application(contextType);
    }

    public static Application getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Application has not been initialized.");
        }

        return instance;
    }

    protected InputContext context;
    public CompanyDB db = new CompanyDB();

    public Application(InputContextType ict) {
        this.context = InputContext.getContext(ict);
    }

    /*
        Assertions
     */

    public void assertAvailableActivities(Employee employee) throws IllegalArgumentException {
        if (employee.getNumOpenActivities() > 0) {
            return;
        }

        String output = String.format(
                "The employee %s has no room for any new activities at the moment.",
                employee.getID()
        );
        throw new IllegalArgumentException(output);
    }

    public void assertSignedInEmployeePM(Project project) throws IllegalArgumentException {
        if (!project.isPM(this.db.getSignedInEmployee())) {
            throw new IllegalArgumentException("Project manager role required.");
        }
    }

    /*
        Getters
     */

    public Activity getActivity(Project project, int activityID) throws IllegalArgumentException {
        Activity activity = project.getActivity(activityID);

        if (activity == null) {
            String eMsg = String.format(
                    "The given activity, %s, does not exist within project, %s.",
                    activityID,
                    project.getID()
            );
            throw new IllegalArgumentException(eMsg);
        }

        return activity;
    }

    public InputContext getContext() {
        return this.context;
    }

    public Employee getEmployee(String employeeID) throws IllegalArgumentException {
        Employee employee = db.getEmployee(employeeID);

        if (employee == null) {
            throw new IllegalArgumentException(String.format("The given employee, %s, does not exist.", employeeID));
        }

        return employee;
    }

    public HashMap<String, Employee> getEmployees() {
        return this.db.getEmployees();
    }

    public Project getProject(String projectID) throws IllegalArgumentException {
        Project project = db.getProject(projectID);

        if (project == null) {
            throw new IllegalArgumentException(String.format("The given project, %s, does not exist.", projectID));
        }

        return project;
    }

    public HashMap<String, Project> getProjects() {
        return this.db.getProjects();
    }

    public Employee getSignedInEmployee() {
        return this.db.getSignedInEmployee();
    }

    /*
     * Action helpers
     */

    protected void helperSetSubmitHours(String projectID, int activityID, Date date, double hours, boolean shouldSet) throws IllegalArgumentException {
        Project project = this.getProject(projectID);
        Activity activity = this.getActivity(project, activityID);

        // Find the right intermediate
        Employee signedInEmployee = db.getSignedInEmployee();
        HashMap<String, EmployeeActivityIntermediate> trackedTime = activity.getTrackedTime();
        EmployeeActivityIntermediate employeeActivityIntermediate = trackedTime.get(signedInEmployee.getID());

        if(employeeActivityIntermediate == null) {
            throw new IllegalArgumentException("You are not associated with one or more of these projects.");
        }

        // Set the minutes
        int minutes = (int) (hours * 60.0);

        if(shouldSet) {
            employeeActivityIntermediate.setMinutes(date, minutes);
        } else {
            employeeActivityIntermediate.addMinutes(date, minutes);
        }
    }

    /*
     * Actions
     */

    public void addEmployee(String ID, String name) {
        Employee employee = new Employee(ID, name);

        this.db.getEmployees().put(employee.getID(), employee);
    }

    public void addEmployee(String ID, String name, int weeklyActivityCap) {
        Employee employee = new Employee(ID, name, weeklyActivityCap);

        this.db.getEmployees().put(employee.getID(), employee);
    }

    public void assignEmployeeToActivity(String employeeID, String projectID, int activityID) throws IllegalArgumentException {
        Project project = this.getProject(projectID);
        this.assertSignedInEmployeePM(project);

        Activity activity = this.getActivity(project, activityID);
        Employee employee = this.getEmployee(employeeID);
        this.assertAvailableActivities(employee);

        new EmployeeActivityIntermediate(employee, activity);
    }

    public void assignPM(String projectID, String employeeID) throws AccessDeniedException, IllegalArgumentException {
        Project project = this.getProject(projectID);
        Employee employee = this.getEmployee(employeeID);
        Employee signedInEmployee = this.db.getSignedInEmployee();

        project.assignPM(employee, signedInEmployee);
    }

    public void createActivity(String projectID, String activityID) throws IllegalArgumentException {
        Project project = this.getProject(projectID);

        this.assertSignedInEmployeePM(project);

        new Activity(activityID, project);
    }

    public void createProject(String name, boolean isBillable) throws IllegalArgumentException {
        int year = (new GregorianCalendar()).get(Calendar.YEAR);
        int nextID = this.db.incrementNextProjectID(year);

        Project project = new Project(nextID, name, isBillable);
        this.db.getProjects().put(project.getID(), project);
    }

    public void estimateActivityDuration(String projectID, int activityID, int numHours) throws IllegalArgumentException {
        Project project = this.getProject(projectID);
        Activity activity = this.getActivity(project, activityID);

        if(numHours <= 0) {
            String output = String.format("The estimated number of work hours has to be bigger than 0. %s received.", numHours);
            throw new IllegalArgumentException(output);
        }

        activity.setEstimatedHours(numHours);
    }

    public void finishActivity(String projectID, int activityID) {
        Project project = db.getProject(projectID);

        this.assertSignedInEmployeePM(project);

        Activity activity = this.getActivity(project, activityID);
        activity.setDone(true);
    }

    public boolean isSignedInEmployeePM(String projectID) {
        return this.db.getSignedInEmployee().equals(this.getProject(projectID).getPM());
    }

    public void markActivityDone(String projectID, int activityID) throws IllegalArgumentException {
        Project project = this.getProject(projectID);
        Activity activity = this.getActivity(project, activityID);
        activity.setDone(true);
    }

    public void requestAssistance(String projectID, int activityID, String employeeID) throws AccessDeniedException, IllegalArgumentException {
        Project project = this.getProject(projectID);
        Activity activity = this.getActivity(project, activityID);
        Employee employee = this.getEmployee(employeeID);

        Employee signedInEmployee = db.getSignedInEmployee();
        HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> signedInEmployeeActivities
            = signedInEmployee.getActivities();
        HashMap<Integer, EmployeeActivityIntermediate> signedEmployeeProjectActivities =
            signedInEmployeeActivities.get(project.getID());

        if (signedEmployeeProjectActivities == null) {
            String output = String.format("You are not allowed to work with the given project, %s.", projectID);
            throw new AccessDeniedException(output);
        }

        boolean signedInEmployeeIsNotAttachedToActivity = !signedEmployeeProjectActivities.containsKey(activity.getID());

        if (signedInEmployeeIsNotAttachedToActivity) {
            String output = String.format("You are not allowed to work with the given activity, %s.", activityID);
            throw new AccessDeniedException(output);
        }

        this.assertAvailableActivities(employee);

        employee.getActivities().put(project.getID(), signedInEmployeeActivities.get(project.getID()));
    }

    public void requestOutOfOffice(OOOActivityType type, Date start, Date end) throws IllegalArgumentException {
        Employee signedInEmployee = this.db.getSignedInEmployee();

        signedInEmployee.getOOOActivities().add(new OutOfOfficeActivity(type, start, end));
    }

    public void setActivityInterval(String projectID, int activityID, Date start, Date end) throws IllegalArgumentException {
        if(start.compareTo(end) >= 0) {
            String output = String.format(
                "The given start week, %s, is after the given end week, %s.",
                DateFormatter.formatDate(start),
                DateFormatter.formatDate(end)
            );
            throw new IllegalArgumentException(output);
        }

        Project project = this.getProject(projectID);
        Activity activity = this.getActivity(project, activityID);

        activity.setStartWeek(start);
        activity.setEndWeek(end);
    }

    public void setContext(InputContext ic) {
        this.context = ic;
    }

    public void setHours(String projectID, int activityID, Date date, int setHours) throws IllegalArgumentException {
        this.helperSetSubmitHours(projectID, activityID, date, setHours, true);
    }

    public void setSignedInEmployee(String employeeID) {
        this.db.setSignedInEmployee(employeeID);
    }

    public void submitHours(String projectID, int activityID, Date date, int addedHours) throws IllegalArgumentException {
        this.helperSetSubmitHours(projectID, activityID, date, addedHours, false);
    }
}
