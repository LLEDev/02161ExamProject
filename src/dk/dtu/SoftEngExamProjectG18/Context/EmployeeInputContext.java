package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EmployeeInputContext extends InputContext {

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

    /*
        General
     */

    public static final Map<String, String[]> triggers = new HashMap<String, String[]>() {{
        putAll(InputContext.getTriggersStatic());
        put("hours set", new String[] {"hours set {projectID} {activityID} {date} {hours}", "cmdSetHours"});
        put("hours submit", new String[] {"hours submit {projectID} {activityID} {date} {hours}", "cmdSubmitHours"});
        put("project activity markDone", new String[] {"project activity markDone {projectID} {activityID}", "cmdMarkActivityAsDone"});
        put("project create", new String[] {"project create {name} {billable}", "cmdCreateProject"});
        put("request assistance", new String[] {"request assistance {projectID} {activityID} {employeeID}", "cmdRequestAssistance"});
        put("request ooo", new String[] {"request ooo {type} {start} {end}", "cmdRequestOutOfOffice"});
        put("request overview daily", new String[] {"request overview daily", "cmdRequestDailyOverview"});
    }};

    public String getSingularContextName() {
        return "an employee";
    }

    public static Map<String, String[]> getTriggersStatic() {
        return EmployeeInputContext.triggers;
    }
    public Map<String, String[]> getTriggers() {
        return EmployeeInputContext.getTriggersStatic();
    }
    /*
        Commands
     */

    /*
        TODO: !! Important !!
        TODO: Remember to use this.writeOutput instead of System.out.print!
     */

    // String name, boolean isBillable
    public boolean cmdCreateProject(String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (this.isValidProjectName(args[0])) {

            Project project;

            if (args.length > 1) {
                boolean isBillable = Boolean.parseBoolean(args[1]);
                project = new Project(args[0], isBillable);
            } else {
                project = new Project(args[0]);
            }

            this.addProjectToDB(project);
            Activity activity = new Activity("test", project);
            project.getActivities().put(activity.getID(),activity);


            return true;
        }
        return false;
    }

    // String projectID, int activityID
    public boolean cmdMarkActivityAsDone(String[] args) {
        if (checkArgumentlength(args.length,2)) {
            return false;
        }

        Activity activity = this.getActivityFromProject(args[0], args[1]);
        if (activity!=null) {
            activity.setDone(true);
            this.writeOutput("Activity marked as done");
            return true;
        }
        return false;
    }

    // String projectID, int activityID, String employeeID
    public boolean cmdRequestAssistance(String[] args) {
        if (checkArgumentlength(args.length,3)) {
            return false;
        }
        Activity activity = this.getActivityFromProject(args[0], args[1]);
        if (activity!=null) {
            CompanyDB db = CompanyDB.getInstance();
            Project project = db.getProject(args[0]);
            Employee employee = db.getEmployee(args[2]);
            if (checkIfNull(employee)) {
                this.writeOutput("Employee does not exist");
                return false;
            }
            Employee signedInEmployee = db.getSignedInEmployee();
            HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> signedInEmployeeActivities
                    = signedInEmployee.getActivities();
             if (employee.amountOfOpenActivities()!=0 &&
                     signedInEmployeeActivities.get(project.getID()).containsKey(activity.getID())) {
                 employee.getActivities().put(project.getID(),signedInEmployeeActivities.get(project.getID()));
                 return true;
             }
        }
        return false;

    }

    public boolean cmdRequestDailyOverview(String[] args) {
        return true;
    }

    // OOOActivityType type, Date start, Date end
    public boolean cmdRequestOutOfOffice(String[] args) { return true; }

    // String projectID, int activityID, Date date, int setHours
    public boolean cmdSetHours(String[] args) {
        if (checkArgumentlength(args.length,4)) {
            return false;
        }
        Activity activity = this.getActivityFromProject(args[0], args[1]);
        if (activity!=null) {
            CompanyDB db = CompanyDB.getInstance();
            Employee employee = db.getSignedInEmployee();
            HashMap<String, EmployeeActivityIntermediate> trackedTime = activity.getTrackedTime();
            EmployeeActivityIntermediate employeeActivityIntermediate = trackedTime.get(employee.getID());
            if(isStringParseIntDoable(args[4])) {
                int minutes = Integer.parseInt(args[4]) * 60;
                try {
                    Date date = this.formatter.parse(args[3]);
                    employeeActivityIntermediate.setMinutes(date, minutes);
                    this.writeOutput("Hours set");
                    return true;
                } catch (ParseException e) {
                    this.writeOutput("Date must be in format " + this.formatter.toPattern());
                    return false;
                }
            }
        }
        return false;
    }

    // String projectID, int activityID, Date date, int addedHours
    public boolean cmdSubmitHours(String[] args) {
        if (checkArgumentlength(args.length,4)) {
            return false;
        }
        Activity activity = this.getActivityFromProject(args[0], args[1]);
        if (activity!=null) {
            CompanyDB db = CompanyDB.getInstance();
            Employee employee = db.getSignedInEmployee();
            HashMap<String, EmployeeActivityIntermediate> trackedTime = activity.getTrackedTime();
            EmployeeActivityIntermediate employeeActivityIntermediate = trackedTime.get(employee.getID());
            if(isStringParseIntDoable(args[4])) {
                int minutes = Integer.parseInt(args[4]) * 60;
                try {
                    Date date = this.formatter.parse(args[3]);
                    employeeActivityIntermediate.addMinutes(date, minutes);
                    this.writeOutput("Hours added");
                    return true;
                } catch (ParseException e) {
                    this.writeOutput("Date must be in format " + this.formatter.toPattern());
                    return false;
                }
            }
        }
        return false;
    }

    /*
        Utils
     */

    private boolean isValidProjectName(String name) {
        return name.length() != 0;
    }

    private void addProjectToDB(Project project) {
        CompanyDB db = CompanyDB.getInstance();
        db.getProjects().put(project.getID(), project);
    }

}
