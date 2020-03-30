package dk.dtu.SoftEngExamProjectG18.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProjectManagerInputContext extends InputContext {

    /*
        General
     */

    // List of commands:
    // Structure: command -> [usage, method]
    public final Map<String, String[]> triggers = new HashMap<String, String[]>() {{
        put("project activity assign", new String[] {
                "project activity assign {employeeID} {projectID} {activityID}",
                "cmdAssignEmployeeToActivity"
        });
        put("project activity create", new String[] {
                "project activity create {projectID}, {activityName}",
                "cmdCreateActivity"
        });
        put("project activity estimate", new String[] {
                "project activity estimate {projectID} {activityID} {minutes}",
                "cmdSetActivityEstimatedDuration"
        });
        put("project activity setDates", new String[] {
                "project activity setDates {projectID} {activityID} {start} {end}",
                "cmdSetActivityInterval"
        });
        put("request overview", new String[] {"request overview", "cmdRequestOverview"});
        put("request report", new String[] {"request report", "cmdRequestOverview"});
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

    /*
        TODO: !! Important !!
        TODO: Remember to use this.writeOutput instead of System.out.print!
     */

    // String employeeID, String projectID, int activityID
    public boolean cmdAssignEmployeeToActivity(String[] args) {
        if(args.length != 3) {
            return false;
        }

        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        // TODO: Do something

        this.writeOutput("Test output");

        return true;
    }

    // String projectID, String activityName
    public boolean cmdCreateActivity(String[] args) {
        return true;
    }

    public boolean cmdRequestOverview(String[] args) {
        if(args.length > 0) {
            return false;
        }

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
