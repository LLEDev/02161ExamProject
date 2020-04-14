package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Enum.InputContextType;

import java.util.HashMap;
import java.util.Map;

abstract public class InputContext {

    abstract public String getSingularContextName();
    abstract public Map<String, String[]> getTriggers();

    public final Map<String, String[]> triggers = new HashMap<String, String[]>();

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

    // TODO: Review
    // I have updated this as it was working opposite of what it was supposed to - it returned true if the arg length
    // was equal to the required, which resultated in execution of what was inside the if statement in the method
    // in which it was utilised. It's a minor change, but thought I'd leave a comment so noone gets mad 😁
    // Maybe we should consider renaming it to something along the lines of isArgumentInvalid
    protected boolean checkArgumentlength(int argsLength, int requiredLength) {
        if (argsLength != requiredLength) {
            this.writeOutput("Wrong usage.");
            return true;
        }
        return false;
    }

}
