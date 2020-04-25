package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.OutOfOfficeActivity;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.Util.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        put("project activity estimate", new String[]{"project activity estimate {projectID} {activityID} {work hours}", "cmdSetActivityEstimatedDuration"});
        put("project activity finish", new String[]{"project activity finish {projectID}, {activityName}", "cmdFinishActivity"});
        put("project activity setweeks", new String[]{"project activity setWeeks {projectID} {activityID} {startWeek} {endWeek}", "cmdSetActivityInterval"});
        put("view activity", new String[]{"view activity {projectID} {activityID}", "cmdViewActivity"});
        put("view availability", new String[]{"view availability {date}", "cmdViewAvailability"});
        put("view project", new String[]{"view project {projectID}", "cmdViewProject"});
        put("view schedule", new String[]{"view schedule {employeeID}", "cmdViewSchedule"});
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
        Commands - warnings relating to use of reflection API are suppressed
     */

    // Command arguments: String employeeID, String projectID, int activityID
    @SuppressWarnings("unused")
    public void cmdAssignEmployeeToActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 3);
        this.assertStringParseIntDoable(args[2]);

        CompanyDB db = CompanyDB.getInstance();

        Project project = this.getProject(db, args[1]);
        this.assertSignedInEmployeePM(project);

        Activity activity = this.getActivity(project, args[2]);
        Employee employee = this.getEmployee(db, args[0]);
        this.assertAvailableActivities(employee);

        EmployeeActivityIntermediate EAI = new EmployeeActivityIntermediate(employee, activity);
        this.writeOutput("Employee added to activity.");
    }

    // Command arguments: String projectID, String activityName
    @SuppressWarnings("unused")
    public void cmdCreateActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);

        this.assertSignedInEmployeePM(project);

        new Activity(args[1], project);
        this.writeOutput("Activity created.");
    }

    // Command arguments: String projectID, String activityID
    @SuppressWarnings("unused")
    public void cmdFinishActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(args[0]);

        this.assertSignedInEmployeePM(project);

        Activity activity = this.getActivity(project, args[1]);
        activity.setDone(true);
        this.writeOutput("Activity finished.");
    }

    // Command arguments: String projectID, int activityID, int numWeeks
    @SuppressWarnings("unused")
    public void cmdSetActivityEstimatedDuration(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 3);
        this.assertStringParseIntDoable(args[2]);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);
        Activity activity = this.getActivity(project, args[1]);

        int numHours = Integer.parseInt(args[2]);
        if(numHours <= 0) {
            String output = String.format("The estimated number of work hours has to be bigger than 0. %s received.", numHours);
            throw new CommandException(output);
        }

        activity.setEstimatedHours(numHours);
        this.writeOutput("Estimated number of work hours updated.");
    }

    // Command arguments: String projectID, int activityID, Date start, Date end
    @SuppressWarnings("unused")
    public void cmdSetActivityInterval(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 4);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);
        Activity activity = this.getActivity(project, args[1]);

        try {
            Date start = this.weekFormatter.parse(args[2]);
            Date end = this.weekFormatter.parse(args[3]);

            if(start.compareTo(end) >= 0) {
                String output = String.format("The given start week, %s, is after the given end week, %s.", args[2], args[3]);
                throw new CommandException(output);
            }

            activity.setStartWeek(start);
            activity.setEndWeek(end);

            this.writeOutput("Start/end weeks updated.");
        } catch (ParseException e) {
            String output = String.format("Any week must be given in the format %s. Received %s and %s.", this.weekFormatter.toPattern(), args[2], args[3]);
            throw new CommandException(output);
        }
    }

    // Command arguments: String projectID, activityID
    @SuppressWarnings("unused")
    public void cmdViewActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);

        this.assertSignedInEmployeePM(project);

        Activity activity = this.getActivity(project, args[1]);

        String startWeek = null;
        String endWeek = null;
        try {
            startWeek = this.weekFormatter.format(activity.getStartWeek());
            endWeek = this.weekFormatter.format(activity.getEndWeek());
        } catch (Exception ignored) {}

        int trackedHours = (int) Math.ceil(activity.getTotalTrackedMinutes() / 60.0);

        this.writeOutput("Activity details:\n");
        this.writeOutput(String.format(" - ID: %s\n", activity.getID()));
        this.writeOutput(String.format(" - Name: %s\n", activity.getName()));
        this.writeOutput(String.format(" - Start Week: %s\n", startWeek));
        this.writeOutput(String.format(" - End Week: %s\n", endWeek));
        this.writeOutput(String.format(" - Estimated work hours: %s\n", activity.getEstimatedHours()));
        this.writeOutput(String.format(" - Tracked work hours: %s\n", trackedHours));

        this.writeOutput("\nTracked time:\n");
        ArrayList<EmployeeActivityIntermediate> collection = new ArrayList<>(activity.getTrackedTime().values());
        this.writeOutput(Table.make(
                "overview",
                new String[] {"Employee", "Date", "Minutes"},
                collection
        ));
    }

    // Command arguments: String date
    @SuppressWarnings("unused")
    public void cmdViewAvailability(String[] args) throws CommandException, ParseException {
        this.assertArgumentsValid(args.length, 1);
        this.assertStringParseDateDoable(args[0]);

        Date d = this.formatter.parse(args[0]);

        ArrayList<Employee> collection = new ArrayList<>(CompanyDB.getInstance().getEmployees().values());
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("date", d);

        this.writeOutput(String.format("\nAvailable employees at %s:\n", args[0]));
        this.writeOutput(Table.make(
                "availability",
                new String[] {"ID", "Name", "Available activity slots"},
                meta,
                collection
        ));
    }

    // Command arguments: String projectID
    @SuppressWarnings("unused")
    public void cmdViewProject(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 1);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);

        this.assertSignedInEmployeePM(project);

        this.writeOutput("Project details:\n");
        this.writeOutput(String.format(" - ID: %s\n", project.getID()));
        this.writeOutput(String.format(" - Name: %s\n", project.getName()));
        this.writeOutput(String.format(" - Estimated remaining work: %s work hours\n", project.getEstimatedWorkHoursLeft()));

        this.writeOutput("\nProject activities:\n");
        ArrayList<Activity> collection = new ArrayList<>(project.getActivities().values());
        this.writeOutput(Table.make(
                "overview",
                new String[] {"ID", "Name", "Start week", "End week", "Estimated work hours (in total)", "Tracked work hours (in total)"},
                collection
        ));
    }

    // Command arguments: String employeeID
    @SuppressWarnings("unused")
    public void cmdViewSchedule(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 1);

        CompanyDB db = CompanyDB.getInstance();
        Employee employee = this.getEmployee(db, args[0]);

        this.writeOutput("Employee details:\n");
        this.writeOutput(String.format(" - ID: %s\n", employee.getID()));
        this.writeOutput(String.format(" - Name: %s\n", employee.getName()));

        this.writeOutput("\nWorking on the following active activities:\n");
        ArrayList<Activity> activityCollection = employee.getAllActiveActivities();
        this.writeOutput(Table.make(
                "overview",
                new String[] {"ID", "Name", "Start week", "End week", "Estimated work hours (in total)", "Tracked work hours (in total)"},
                activityCollection
        ) + "\n");

        this.writeOutput("\nPlanned out-of-office activities:\n");
        ArrayList<OutOfOfficeActivity> OOOCollection = new ArrayList<>(employee.getOOOActivities());
        this.writeOutput(Table.make(
                "overview",
                new String[] {"Type", "Start", "End"},
                OOOCollection
        ));
    }
}
