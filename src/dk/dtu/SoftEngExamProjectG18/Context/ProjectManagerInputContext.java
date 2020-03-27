package dk.dtu.SoftEngExamProjectG18.Context;

import java.util.HashMap;
import java.util.Map;

public class ProjectManagerInputContext extends InputContext {

    /*
        General
     */

    // List of commands:
    // Structure: command -> [usage, method]
    public final Map<String, String[]> triggers = new HashMap<String, String[]>() {{
        put("request overview", new String[] {"request overview [input]", "cmdRequestOverview"});
    }};

    public String getSingularContextName() {
        return "a project manager";
    }

    public Map<String, String[]> getTriggers() {
        return this.triggers;
    }

    /*
        Commands
     */

    // String employeeID, String projectID, int activityID
    public boolean cmdAssignEmployeeToActivity(String[] args) {
        return true;
    }

    // String projectID, String activityName
    public boolean cmdCreateActivity(String[] args) {
        return true;
    }

    public boolean cmdRequestOverview(String[] args) {
        return true;
    }

    public boolean cmdRequestReport(String[] args) {
        return true;
    }

    // int numWeeks
    public boolean cmdSetActivityEstimatedDuration(String[] args) {
        return true;
    }

    // Date start, Date end
    public boolean cmdSetActivityInterval(String[] args) {
        return true;
    }
}
