package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.OutOfOfficeActivity;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.Persistence.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.Util.DateFormatter;
import dk.dtu.SoftEngExamProjectG18.Util.Table;

import java.text.ParseException;
import java.util.*;

public class ProjectManagerInputContext extends InputContext {

    /*
        General
     */

    protected ActionMap triggers = ActionMap.build(super.getTriggers(), new Action[]{
        new Action("project activity assign", new String[]{"employeeID", "projectID", "activityID"}, this::cmdAssignEmployeeToActivity),
        new Action("project activity create", new String[]{"projectID", "activityName"}, this::cmdCreateActivity),
        new Action("project activity estimate", new String[]{"projectID", "activityID", "workHours"}, this::cmdSetActivityEstimatedDuration),
        new Action("project activity finish", new String[]{"projectID", "activityName"}, this::cmdFinishActivity),
        new Action("project activity setweeks", new String[]{"projectID", "activityID", "startWeek", "endWeek"}, this::cmdSetActivityInterval),
        new Action("view activity", new String[]{"projectID", "activityID"}, this::cmdViewActivity),
        new Action("view availability", new String[]{"date"}, this::cmdViewAvailability),
        new Action("view project", new String[]{"projectID"}, this::cmdViewProject),
        new Action("view schedule", new String[]{"employeeID"}, this::cmdViewSchedule),
    });

    public String getSingularContextName() {
        return "a project manager";
    }

    public ActionMap getTriggers() {
        return this.triggers;
    }

    /*
        Commands
     */

    // Command arguments: String employeeID, String projectID, int activityID
    @SuppressWarnings("unused")
    public void cmdAssignEmployeeToActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 3);
        this.assertStringParseIntDoable(args[2]);

        String employeeID = args[0];
        String projectID = args[1];
        int activityID = Integer.parseInt(args[2]);

        this.wrapExceptions(() -> this.application.assignEmployeeToActivity(employeeID, projectID, activityID))
            .outputOnSuccess(() -> "Employee added to activity.")
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    // Command arguments: String projectID, String activityName
    @SuppressWarnings("unused")
    public void cmdCreateActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);

        String projectID = args[0];
        String activityName = args[1];

        this.wrapExceptions(() -> this.application.createActivity(projectID, activityName))
            .outputOnSuccess(() -> "Activity created.")
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    // Command arguments: String projectID, String activityID
    @SuppressWarnings("unused")
    public void cmdFinishActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);

        String projectID = args[0];
        int activityID = Integer.parseInt(args[1]);

        this.wrapExceptions(() -> this.application.finishActivity(projectID, activityID))
            .outputOnSuccess(() -> "Activity finished.")
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    // Command arguments: String projectID, int activityID, int numHours
    @SuppressWarnings("unused")
    public void cmdSetActivityEstimatedDuration(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 3);
        this.assertStringParseIntDoable(args[2]);

        String projectID = args[0];
        int activityID = Integer.parseInt(args[1]);
        int numHours = Integer.parseInt(args[2]);

        this.wrapExceptions(() -> this.application.estimateActivityDuration(projectID, activityID, numHours))
            .outputOnSuccess(() -> "Estimated number of work hours updated.")
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    // Command arguments: String projectID, int activityID, Date start, Date end
    @SuppressWarnings("unused")
    public void cmdSetActivityInterval(String[] args) throws CommandException, ParseException {
        this.assertArgumentsValid(args.length, 4);
        this.assertStringParseIntDoable(args[1]);
        this.assertStringParseDateDoable(args[2]);
        this.assertStringParseDateDoable(args[3]);

        String projectID = args[0];
        int activityID = Integer.parseInt(args[1]);
        Date start = DateFormatter.parseDate(args[2]);
        Date end = DateFormatter.parseDate(args[3]);

        this.wrapExceptions(() -> this.application.setActivityInterval(projectID, activityID, start, end))
            .outputOnSuccess(() -> "Start/end weeks updated.")
            .outputOnError(e -> "An error occurred: " + e.getMessage())
            .run();
    }

    // Command arguments: String projectID, activityID
    @SuppressWarnings("unused")
    public void cmdViewActivity(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);
        this.assertStringParseIntDoable(args[1]);

        String projectID = args[0];
        int activityID = Integer.parseInt(args[1]);

        try {
            Project project = this.application.getProject(args[0]);
            this.application.assertSignedInEmployeePM(project);

            Activity activity = this.application.getActivity(project, activityID);

            String startWeek = null;
            String endWeek = null;
            try {
                startWeek = DateFormatter.formatWeek(activity.getStartWeek());
                endWeek = DateFormatter.formatWeek(activity.getEndWeek());
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
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }

    // Command arguments: String date
    @SuppressWarnings("unused")
    public void cmdViewAvailability(String[] args) throws CommandException, ParseException {
        this.assertArgumentsValid(args.length, 1);
        this.assertStringParseDateDoable(args[0]);

        Date d = DateFormatter.parseDate(args[0]);

        ArrayList<Employee> collection = new ArrayList<>(this.application.getEmployees().values());
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

        try {
            Project project = this.application.getProject(args[0]);

            this.application.assertSignedInEmployeePM(project);

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
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }

    // Command arguments: String employeeID
    @SuppressWarnings("unused")
    public void cmdViewSchedule(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 1);

        try {
            Employee employee = this.application.getEmployee(args[0]);

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
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
