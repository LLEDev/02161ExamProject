package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Enum.InputContextType;

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

    public void printOutput() {
        System.out.println(this.output);
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

    protected boolean areArgumentsInvalid(int argsLength, int requiredLength) {
        return argsLength != requiredLength;
    }

    protected boolean isNull(Object obj) {
        return obj == null;
    }

    protected boolean isStringParseIntDoable(String possibleInt) {
        try {
            Integer.parseInt(possibleInt);
        } catch (NumberFormatException nfe) {
            this.writeOutput("Any number must be given as an integer");
            return false;
        }

        return true;
    }

    protected Activity getActivityFromProject(String projectID, String activityID) {
        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(projectID);

        if (isNull(project)) {
            this.writeOutput("Project does not exist");
            return null;
        }

        if (isStringParseIntDoable(activityID)) {
            int intActivityID = Integer.parseInt(activityID);
            if (!isNull(project.getActivity(intActivityID))) {
                return project.getActivity(intActivityID);
            }
        }

        this.writeOutput("Activity does not exist");
        return null;
    }

     /*
        Shared commands - warnings relating to use of reflection API are suppressed
      */

    // String projectID, String employeeID
    @SuppressWarnings({"unused", "UnusedReturnValue"})
    public boolean cmdAssignPM(String[] args) {
        if (areArgumentsInvalid(args.length, 2)) {
            return false;
        }

        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(args[0]);
        if (isNull(project)) {
            this.writeOutput("Project does not exist");
            return false;
        }

        Employee employee = db.getEmployee(args[1]);
        if (isNull(employee)) {
            this.writeOutput("Employee does not exist");
            return false;
        }

        try {
            project.assignPM(employee);
        } catch (Exception e) {
            this.writeOutput(e.getMessage());
            return false;
        }

        this.writeOutput("Employee assigned as PM");
        return true;
    }

}
