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
        put("view project", new String[]{"view project {employeeID}", "cmdViewProject"});
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
        Command utils
     */

    protected SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-ww");

    protected void assertSignedInEmployeePM(Project project) throws CommandException {
        CompanyDB db = CompanyDB.getInstance();
        if (db.getSignedInEmployee() != project.getPM()) {
            throw new CommandException("Project manager role required");
        }
    }

    /*
        Commands - warnings relating to use of reflection API are suppressed
     */

    // String employeeID, String projectID, int activityID
    @SuppressWarnings("unused")
    public void cmdAssignEmployeeToActivity(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 3);
        assertStringParseIntDoable(args[2]);

        CompanyDB db = CompanyDB.getInstance();

        Project project = this.getProject(db, args[1]);
        Activity activity = this.getActivity(project, args[2]);

        Employee signedInEmployee = db.getSignedInEmployee();

        if (project.getPM() != null && !signedInEmployee.equals(project.getPM())) {
            throw new CommandException("Project Manager status required.");
        }

        Employee employee = this.getEmployee(db, args[0]);
        if (employee.getNumOpenActivities() == 0) {
            String output = String.format(
                    "The employee, %s, you are requesting assistance from, has no room for any new activities at the moment.",
                    args[1]
            );
            throw new CommandException(output);
        }

        EmployeeActivityIntermediate EAI = new EmployeeActivityIntermediate(employee, activity);
        this.writeOutput("Employee added to activity.");
    }

    // String projectID, String activityName
    @SuppressWarnings("unused")
    public void cmdCreateActivity(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);

        assertSignedInEmployeePM(project);

        new Activity(args[1], project);
        this.writeOutput("Activity created.");
    }

    // String projectID, String activityID
    @SuppressWarnings("unused")
    public void cmdFinishActivity(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(args[0]);

        assertSignedInEmployeePM(project);

        Activity activity = this.getActivity(project, args[1]);
        activity.setDone(true);
        this.writeOutput("Activity finished.");
    }

    // String projectID, int activityID, int numWeeks
    @SuppressWarnings("unused")
    public void cmdSetActivityEstimatedDuration(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 3);
        assertStringParseIntDoable(args[2]);

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

    // String projectID, int activityID, Date start, Date end
    @SuppressWarnings("unused")
    public void cmdSetActivityInterval(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 4);

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

    // String projectID, activityID
    @SuppressWarnings("unused")
    public void cmdViewActivity(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);
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

    // String date
    @SuppressWarnings("unused")
    public void cmdViewAvailability(String[] args) throws CommandException, ParseException {
        assertArgumentsValid(args.length, 1);
        assertStringParseDateDoable(args[0]);

        Date d = this.formatter.parse(args[0]);

        System.out.println(d);
    }

    // String projectID
    @SuppressWarnings("unused")
    public void cmdViewProject(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 1);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);

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

    // String employeeID
    @SuppressWarnings("unused")
    public void cmdViewSchedule(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 1);

        CompanyDB db = CompanyDB.getInstance();
        Employee employee = this.getEmployee(db, args[0]);

        HashMap<String, Activity> allActivities = new HashMap<>();
        for(HashMap<Integer, EmployeeActivityIntermediate> activities : employee.getActivities().values()) {
            for(EmployeeActivityIntermediate intermediate : activities.values()) {
                Activity activity = intermediate.getActivity();
                String combinedID = activity.getProject().getID() + "-" + activity.getID();

                if(activity.isDone() || allActivities.containsKey(combinedID)) {
                    continue;
                }

                allActivities.put(combinedID, activity);
            }
        }

        this.writeOutput("Employee details:\n");
        this.writeOutput(String.format(" - ID: %s\n", employee.getID()));
        this.writeOutput(String.format(" - Name: %s\n", employee.getName()));

        this.writeOutput("\nWorking on the following active activities:\n");
        ArrayList<Activity> activityCollection = new ArrayList<>(allActivities.values());
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
