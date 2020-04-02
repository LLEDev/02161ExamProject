package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;

import java.util.HashMap;
import java.util.Map;

public class EmployeeInputContext extends InputContext {

    /*
        General
     */

    public final Map<String, String[]> triggers = new HashMap<String, String[]>() {{
        put("hours set", new String[] {"hours set {projectID} {activityID} {date} {hours}", "cmdSetHours"});
        put("hours submit", new String[] {"hours submit {projectID} {activityID} {date} {hours}", "cmdSubmitHours"});
        put("project activity markDone", new String[] {"project activity markDone {projectID} {activityID}", "cmdMarkActivityAsDone"});
        put("project assign PM", new String[] {"project assign PM {projectID} {PMID}", "cmdAssignPM"});
        put("project create", new String[] {"project create {name} {billable}", "cmdCreateProject"});
        put("request assistance", new String[] {"request assistance {projectID} {activityID} {employeeID}", "cmdRequestAssistance"});
        put("request ooo", new String[] {"request ooo {type} {start} {end}", "cmdRequestOutOfOffice"});
        put("request overview daily", new String[] {"request overview daily", "cmdRequestDailyOverview"});
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

    /*
        TODO: !! Important !!
        TODO: Remember to use this.writeOutput instead of System.out.print!
     */

    // String projectID, String PMID
    public boolean cmdAssignPM(String[] args) {
        return true;
    }

    // String name, boolean isBillable
    public boolean cmdCreateProject(String name) {
        if (name.length() == 0) {
            return false;
        }
        CompanyDB db = CompanyDB.getInstance();
        Project project = new Project(name);
        db.getProjects().put(project.getID(), project);
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
