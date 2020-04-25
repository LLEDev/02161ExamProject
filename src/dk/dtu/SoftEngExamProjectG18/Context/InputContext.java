package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Enum.CommandExceptionReason;
import dk.dtu.SoftEngExamProjectG18.Enum.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

abstract public class InputContext {

    /*
        Abstract methods
     */

    abstract public String getSingularContextName();

    abstract public Map<String, String[]> getTriggers();

    /*
        Map of shared commands
     */

    public static final Map<String, String[]> triggers = new HashMap<>() {{
        put("project assign pm", new String[]{"project assign PM {projectID} {PMID}", "cmdAssignPM"});
    }};

    /*
        Misc. fields
     */

    protected SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    protected SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-ww");
    protected String output = "";

    /*
        Static getters
     */

    public static InputContext getContext(InputContextType ict) {
        if (ict == InputContextType.PM) {
            return new ProjectManagerInputContext();
        }

        return new EmployeeInputContext();
    }

    public static Map<String, String[]> getTriggersStatic() {
        return triggers;
    }

    /*
        Output methods
     */

    public String getOutput() {
        return this.output;
    }

    public void resetOutput() {
        this.output = "";
    }

    public void writeOutput(String s) {
        this.output += s;
    }

    /*
        Utils
     */

    protected boolean isNull(Object obj) {
        return obj == null;
    }


    /*
        Command specific utils
     */

    protected void assertArgumentsValid(int argsLength, int requiredLength) throws CommandException {
        if(argsLength != requiredLength) {
            throw new CommandException(CommandExceptionReason.INVALID_ARGUMENTS);
        }
    }

    protected void assertAvailableActivities(Employee employee) throws CommandException {
        if (employee.getNumOpenActivities() > 0) {
            return;
        }

        String output = String.format(
                "The employee %s has no room for any new activities at the moment.",
                employee.getID()
        );
        throw new CommandException(output);
    }

    protected void assertSignedInEmployeePM(Project project) throws CommandException {
        CompanyDB db = CompanyDB.getInstance();
        if (db.getSignedInEmployee() != project.getPM()) {
            throw new CommandException("Project manager role required.");
        }
    }

    protected void assertStringParseDateDoable(String possibleDate) throws CommandException {
        try {
            this.formatter.parse(possibleDate);
        } catch (ParseException e) {
            String output = String.format("Any date must be given in the format %s. Received %s.", this.formatter.toPattern(), possibleDate);
            throw new CommandException(output);
        }
    }

    protected void assertStringParseIntDoable(String possibleInt) throws CommandException {
        try {
            Integer.parseInt(possibleInt);
        } catch (NumberFormatException nfe) {
            String output = String.format("Any number must be given as an integer. Received %s.", possibleInt);
            throw new CommandException(output);
        }
    }

    protected void assertValidProjectName(String name) throws CommandException {
        if(name.length() == 0) {
            throw new CommandException(String.format("The given project name, %s, is not valid.", name));
        }
    }

    protected Activity getActivity(Project project, String activityID) throws CommandException {
        assertStringParseIntDoable(activityID);

        int intActivityID = Integer.parseInt(activityID);
        Activity activity = project.getActivity(intActivityID);

        if (isNull(activity)) {
            String eMsg = String.format(
                    "The given activity, %s, does not exist within project, %s.",
                    activityID,
                    project.getID()
            );
            throw new CommandException(eMsg);
        }

        return activity;
    }

    protected Employee getEmployee(CompanyDB db, String employeeID) throws CommandException {
        Employee employee = db.getEmployee(employeeID);

        if (isNull(employee)) {
            throw new CommandException(String.format("The given employee, %s, does not exist.", employeeID));
        }

        return employee;
    }

    protected Project getProject(CompanyDB db, String projectID) throws CommandException {
        Project project = db.getProject(projectID);

        if (isNull(project)) {
            throw new CommandException(String.format("The given project, %s, does not exist.", projectID));
        }

        return project;
    }

     /*
        Shared commands - warnings relating to use of reflection API are suppressed
      */

    // String projectID, String employeeID
    @SuppressWarnings("unused")
    public void cmdAssignPM(String[] args) throws CommandException {
        assertArgumentsValid(args.length, 2);

        CompanyDB db = CompanyDB.getInstance();
        Project project = this.getProject(db, args[0]);
        Employee employee = this.getEmployee(db, args[1]);

        boolean hasAssignedPM = project.assignPM(employee);

        if (hasAssignedPM) {
            this.writeOutput("Employee assigned as PM.");
            return;
        }
        throw new CommandException("Project manager role required.");
    }

}
