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

    protected boolean isStringParseIntDoable (String possibleInt) {
        try {
            Integer.parseInt(possibleInt);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    protected Activity getActivityFromProject(String projectID, String activityID) {
        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(projectID);
        if (isStringParseIntDoable(activityID)) {
            int intActivityID = Integer.parseInt(activityID);
            return project.getActivity(intActivityID);
        }
        return null;
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

}
