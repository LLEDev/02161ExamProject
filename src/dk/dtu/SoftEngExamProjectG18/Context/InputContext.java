package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Enum.InputContextType;

import java.util.HashMap;
import java.util.Map;

abstract public class InputContext {

    abstract public String getSingularContextName();

    abstract public Map<String, String[]> getTriggers();

    public static Map<String, String[]> getTriggersStatic() {
        return triggers;
    }

     /*
        Shared Methods
      */

    // String projectID, String employeeID
    public boolean cmdAssignPM(String[] args) {
        if (checkArgumentlength(args.length,2)) {
            return false;
        }
        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(args[0]);
        if (checkIfNull(project)) {
            this.writeOutput("Project does not exist");
            return false;
        }
        Employee employee = db.getEmployee(args[1]);
        if (checkIfNull(employee)) {
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

    public static final Map<String, String[]> triggers = new HashMap<String, String[]>() {{
        put("project assign pm", new String[] {"project assign PM {projectID} {PMID}", "cmdAssignPM"});
    }};

    public static InputContext getContext(InputContextType ict) {
        if(ict == InputContextType.PM) {
            return new ProjectManagerInputContext();
        }

        return new EmployeeInputContext();
    }

    protected String output = "";

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
    protected boolean isStringParseIntDoable (String possibleInt) {
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

        if (checkIfNull(project)) {
            this.writeOutput("Project does not exist");
            return null;
        }

        if (isStringParseIntDoable(activityID)) {
            int intActivityID = Integer.parseInt(activityID);
            if (!checkIfNull(project.getActivity(intActivityID))){
                return project.getActivity(intActivityID);
            }
        }
        this.writeOutput("Activity does not exist");
        return null;
    }

    protected boolean checkIfNull(Object obj) {
        return (obj==null);
    }

    // TODO: Maybe we should consider renaming it to something along the lines of isArgumentInvalid
    protected boolean checkArgumentlength(int argsLength, int requiredLength) {
        if (argsLength != requiredLength) {
            this.writeOutput("Wrong usage.");
            return true;
        }
        return false;
    }

}
