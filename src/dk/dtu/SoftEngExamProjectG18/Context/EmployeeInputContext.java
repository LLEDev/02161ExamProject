package dk.dtu.SoftEngExamProjectG18.Context;

import java.util.HashMap;
import java.util.Map;

public class EmployeeInputContext extends InputContext {

    /*
        General
     */

    public final Map<String, String[]> triggers = new HashMap<String, String[]>() {{
    }};

    public String getSingularContextName() {
        return "an employee";
    }

    public Map<String, String[]> getTriggers() {
        return this.triggers;
    }

    /*
        Commands
     */

    // String projectID, String PMID
    public boolean cmdAssignPM(String[] args) {
        return true;
    }

    // String name, boolean isBillable
    public boolean cmdCreateProject(String[] args) {
        return true;
    }

    // String projectID, int activityID
    public boolean cmdMarkActivityAsDone(String[] args) {
        return true;
    }

    // String projectID, int activityID, String employeeID
    public boolean cmdRequestAssistance(String[] args) {
        return true;
    }

    public boolean cmdRequestDailyOverview(String[] args) {
        return true;
    }

    // OOOActivityType type, Date start, Date end
    public boolean cmdRequestOutOfOffice(String[] args) {
        return true;
    }

    // String projectID, int activityID, Date date, int setHours
    public boolean cmdSetHours(String[] args) {
        return true;
    }

    // String projectID, int activityID, Date date, int addedHours
    public boolean cmdSubmitHours(String[] args) {
        return true;
    }

}
