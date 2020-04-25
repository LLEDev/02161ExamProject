package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.OutOfOfficeActivity;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.Util.Table;

import java.text.ParseException;
import java.util.*;

public class EmployeeInputContext extends InputContext {

    /*
        General
     */

    public static final Map<String, String[]> triggers = new HashMap<>() {{
        putAll(InputContext.getTriggersStatic());
        put("hours set", new String[]{"hours set {projectID} {activityID} {date} {hours}", "cmdSetHours"});
        put("hours submit", new String[]{"hours submit {projectID} {activityID} {date} {hours}", "cmdSubmitHours"});
        put("project activity markDone", new String[]{"project activity markDone {projectID} {activityID}", "cmdMarkActivityAsDone"});
        put("project create", new String[]{"project create {name} {billable}", "cmdCreateProject"});
        put("request assistance", new String[]{"request assistance {projectID} {activityID} {employeeID}", "cmdRequestAssistance"});
        put("request ooo", new String[]{"request ooo {type} {start} {end}", "cmdRequestOutOfOffice"});
        put("view submissions", new String[]{"view submissions", "cmdViewSubmissions"});
    }};

    public static Map<String, String[]> getTriggersStatic() {
        return EmployeeInputContext.triggers;
    }

    public String getSingularContextName() {
        return "an employee";
    }

    public Map<String, String[]> getTriggers() {
        return EmployeeInputContext.getTriggersStatic();
    }

    /*
        Command helpers
     */

    // Command arguments: String projectID, int activityID, Date date, int setHours
    @SuppressWarnings("unused")
    public void helperSetSubmitHours(String[] args, boolean shouldSet) throws CommandException, ParseException {
        this.assertArgumentsValid(args.length, 4);
        this.assertStringParseDateDoable(args[2]);
        this.assertStringParseIntDoable(args[3]);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);
        Activity activity = this.getActivity(project, args[1]);

        Employee signedInEmployee = db.getSignedInEmployee();
        HashMap<String, EmployeeActivityIntermediate> trackedTime = activity.getTrackedTime();
        EmployeeActivityIntermediate employeeActivityIntermediate = trackedTime.get(signedInEmployee.getID());

        if(employeeActivityIntermediate == null) {
            throw new CommandException("You are not associated with one or more of these projects.");
        }

        int minutes = Integer.parseInt(args[3]) * 60;
        Date date = this.formatter.parse(args[2]);

        if(shouldSet) {
            employeeActivityIntermediate.setMinutes(date, minutes);
            this.writeOutput("Hours set.");
        } else {
            employeeActivityIntermediate.addMinutes(date, minutes);
            this.writeOutput("Hours submitted.");
        }
    }

    /*
        Commands - warnings relating to use of reflection API are suppressed
     */

    // Command arguments: String name, boolean isBillable
    @SuppressWarnings("unused")
    public void cmdCreateProject(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);
        this.assertValidProjectName(args[0]);

        Project project;
        if (args.length > 1) {
            boolean isBillable = Boolean.parseBoolean(args[1]);
            project = new Project(args[0], isBillable);
        } else {
            project = new Project(args[0]);
        }

        CompanyDB db = CompanyDB.getInstance();
        db.getProjects().put(project.getID(), project);
        this.writeOutput("Project created.");
    }

    // Command arguments: String projectID, int activityID
    @SuppressWarnings("unused")
    public void cmdMarkActivityAsDone(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);
        Activity activity = this.getActivity(project, args[1]);

        activity.setDone(true);
        this.writeOutput("Activity completed.");
    }

    // Command arguments: String projectID, int activityID, String employeeID
    @SuppressWarnings("unused")
    public void cmdRequestAssistance(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 3);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);
        Activity activity = this.getActivity(project, args[1]);
        Employee employee = this.getEmployee(db, args[2]);

        Employee signedInEmployee = db.getSignedInEmployee();
        HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> signedInEmployeeActivities
                = signedInEmployee.getActivities();
        HashMap<Integer, EmployeeActivityIntermediate> signedEmployeeProjectActivities =
                signedInEmployeeActivities.get(project.getID());

        if(signedEmployeeProjectActivities == null) {
            String output = String.format("You are not allowed to work with the given project, %s.", args[0]);
            throw new CommandException(output);
        }

        boolean signedInEmployeeIsNotAttachedToActivity = !signedEmployeeProjectActivities.containsKey(activity.getID());

        if(signedInEmployeeIsNotAttachedToActivity) {
            String output = String.format("You are not allowed to work with the given activity, %s.", args[1]);
            throw new CommandException(output);
        }

        this.assertAvailableActivities(employee);

        employee.getActivities().put(project.getID(), signedInEmployeeActivities.get(project.getID()));
        this.writeOutput("Assistance requested.");
    }

    // Command arguments: OOOActivityType type, Date start, Date end
    @SuppressWarnings("unused")
    public void cmdRequestOutOfOffice(String[] args) throws CommandException, ParseException {
        this.assertArgumentsValid(args.length, 3);
        this.assertStringParseDateDoable(args[1]);
        this.assertStringParseDateDoable(args[2]);

        CompanyDB db = CompanyDB.getInstance();
        Employee signedInEmployee = db.getSignedInEmployee();

        OOOActivityType type = null;
        for(OOOActivityType OOOType : OOOActivityType.values()) {
            if(args[0].toLowerCase().equals(OOOType.toString().toLowerCase())) {
                type = OOOType;
                break;
            }
        }

        if(type == null) {
            String optionDelimiter = ", ";
            String options = Arrays.stream(OOOActivityType.values())
                    .map(t -> optionDelimiter + t.toString().toLowerCase())
                    .reduce("", String::concat)
                    .substring(optionDelimiter.length());

            String output = String.format(
                    "Please specify a valid out-of-office activity type. Valid options are: %s. Received: %s.",
                    options,
                    args[0]
            );
            throw new CommandException(output);
        }

        Date start = this.formatter.parse(args[1]);
        Date end = this.formatter.parse(args[2]);

        signedInEmployee.getOOOActivities().add(new OutOfOfficeActivity(type, start, end));
        this.writeOutput("Out-of-office activity requested.");
    }

    // Command arguments: String projectID, int activityID, Date date, int setHours
    @SuppressWarnings("unused")
    public void cmdSetHours(String[] args) throws CommandException, ParseException {
        this.helperSetSubmitHours(args, true);
    }

    // Command arguments: String projectID, int activityID, Date date, int addedHours
    @SuppressWarnings("unused")
    public void cmdSubmitHours(String[] args) throws CommandException, ParseException {
        this.helperSetSubmitHours(args, false);
    }

    // Command arguments:
    @SuppressWarnings("unused")
    public void cmdViewSubmissions(String[] args) throws CommandException {
        this.assertArgumentsValid(args.length, 0);

        Employee signedInEmployee = CompanyDB.getInstance().getSignedInEmployee();

        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(signedInEmployee);

        this.writeOutput(Table.make(
                "submissions",
                new String[] {"Project ID", "Activity ID", "Tracked hours"},
                employeeList
        ));
    }

}
