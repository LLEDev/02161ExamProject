package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProjectManagerInputContext extends InputContext {

    /*
        General
     */

    // List of commands:
    // Structure: command -> [usage, method]
    public static final Map<String, String[]> triggers = new HashMap<>() {{
        putAll(InputContext.getTriggersStatic());
        put("project activity assign", new String[]{"project activity assign {employeeID} {projectID} {activityID}", "cmdAssignEmployeeToActivity"});
        put("project activity create", new String[]{"project activity create {projectID}, {activityName}", "cmdCreateActivity"});
        put("project activity finish", new String[]{"project activity finish {projectID}, {activityName}", "cmdFinishActivity"});
        put("project activity estimate", new String[]{"project activity estimate {projectID} {activityID} {minutes}", "cmdSetActivityEstimatedDuration"});
        put("project activity setDates", new String[]{"project activity setDates {projectID} {activityID} {start} {end}", "cmdSetActivityInterval"});
        put("request overview", new String[]{"request overview", "cmdRequestOverview"});
        put("request report", new String[]{"request report", "cmdRequestOverview"});
    }};

    public static Map<String, String[]> getTriggersStatic() {
        return ProjectManagerInputContext.triggers;
    }

    public String getSingularContextName() {
        return "a project manager";
    }

    public Map<String, String[]> getTriggers() {
        return ProjectManagerInputContext.getTriggersStatic();
    }

    /*
        Utils
     */

    protected boolean isSignedInEmployeePM(Project project) {
        CompanyDB db = CompanyDB.getInstance();
        return db.getSignedInEmployee() == project.getPM();
    }

    /*
        Commands - warnings relating to use of reflection API are suppressed
     */

    // String employeeID, String projectID, int activityID
    @SuppressWarnings("unused")
    public void cmdAssignEmployeeToActivity(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 3);
        assertStringParseIntDoable(args[0]);
        assertStringParseIntDoable(args[2]);

        CompanyDB db = CompanyDB.getInstance();

        Project project = this.getProject(db, args[1]);
        Activity activity = this.getActivity(project, args[2]);

        Employee signedInEmployee = db.getSignedInEmployee();
        if (!signedInEmployee.equals(project.getPM())) {
            throw new CommandException("Project Manager status required.");
        }

        Employee employee = this.getEmployee(db, args[0]);
        if (employee.amountOfOpenActivities() == 0) {
            String output = String.format(
                    "The employee, %s, you are requesting assistance from, has no room for any new activities at the moment.",
                    args[1]
            );
            throw new CommandException(output);
        }

        EmployeeActivityIntermediate EAI = new EmployeeActivityIntermediate(employee, activity);
        this.writeOutput("Employee added to activity");
    }

    // String projectID, String activityName
    @SuppressWarnings("unused")
    public void cmdCreateActivity(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);

        if (!isSignedInEmployeePM(project)) {
            throw new CommandException("Project manager role required");
        }

        new Activity(args[1], project);
    }

    // String projectID, String activityID
    @SuppressWarnings("unused")
    public void cmdFinishActivity(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(args[0]);

        if (!isSignedInEmployeePM(project)) {
            throw new CommandException("Project manager role required");
        }

        Activity activity = this.getActivity(project, args[1]);
        activity.setDone(true);
    }

    // TODO: put in command structur
    //String employeeID, String date
    @SuppressWarnings("unused")
    public void cmdRequestEmployeeAvailability(String[] args) throws CommandException {
        /*
        assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Employee employee = db.getEmployee(args[0]);
        try {
            Date date = this.formatter.parse(args[1]);
            if (employee.isOutOfOffie(date)) {
                this.writeOutput("Employee is not available");
                return false;
            } else {
                this.writeOutput("Employee is available");
                return true;
            }
        } catch (ParseException e) {
            this.writeOutput("Date must be in format " + this.formatter.toPattern());
            return false;
        }
        */
    }

    @SuppressWarnings("unused")
    public void cmdRequestOverview(String[] args) throws CommandException {
        // assertArgumentsValid(args.length, 0);
    }

    @SuppressWarnings("unused")
    public void cmdRequestReport(String[] args) {

    }

    // int numWeeks
    @SuppressWarnings("unused")
    public void cmdSetActivityEstimatedDuration(String[] args) {

    }

    // Date start, Date end
    @SuppressWarnings("unused")
    public void cmdSetActivityInterval(String[] args) {

    }
}
