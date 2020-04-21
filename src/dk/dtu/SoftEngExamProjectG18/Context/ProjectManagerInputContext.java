package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProjectManagerInputContext extends InputContext {

    protected SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

    /*
        General
     */

    // List of commands:
    // Structure: command -> [usage, method]
    public static final Map<String, String[]> triggers = new HashMap<String, String[]>() {{
        putAll(InputContext.getTriggersStatic());
        put("project activity assign", new String[] {
                "project activity assign {employeeID} {projectID} {activityID}",
                "cmdAssignEmployeeToActivity"
        });
        put("project activity create", new String[] {
                "project activity create {projectID}, {activityName}",
                "cmdCreateActivity"
        });
        put("project activity finish", new String[] {
                "project activity finish {projectID}, {activityName}",
                "cmdFinishActivity"
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

    public static Map<String, String[]> getTriggersStatic() {
        return ProjectManagerInputContext.triggers;
    }
    public Map<String, String[]> getTriggers() {
        return ProjectManagerInputContext.getTriggersStatic();
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
        if (checkArgumentlength(args.length,3)) {
            return false;
        }

        if(!(isStringParseIntDoable(args[0]) && isStringParseIntDoable(args[2]))){
            return false;
        }

        Activity activity = this.getActivityFromProject(args[1], args[2]);
        if (activity!=null) {
            CompanyDB db = CompanyDB.getInstance();
            Project project = db.getProject(args[1]);
            Employee PM = db.getSignedInEmployee();
            if (!(PM.equals(project.getPM()))) {
                this.writeOutput("Project Manager status required");
                return false;
            }
            Employee employee = db.getEmployee(args[0]);
            if (employee.amountOfOpenActivities()!=0) {
                EmployeeActivityIntermediate EAI = new EmployeeActivityIntermediate(employee,activity);
                this.writeOutput("Employee added to activity");
                return true;
            }

        }
        return false;
    }

    // String projectID, String activityName
    public boolean cmdCreateActivity(String[] args) {
        if (args.length != 2)
            return false;

        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(args[0]);
        if (isSignedInEmployeePM(project)) {
            new Activity(args[1], project);
            return true;
        }
        return false;
    }

    // String projectID, String activityID
    public boolean cmdFinishActivity(String[] args) {
        if (args.length != 2)
            return false;

        CompanyDB db = CompanyDB.getInstance();
        Project project = db.getProject(args[0]);
        if (isSignedInEmployeePM(project)) {
            if (isStringParseIntDoable(args[1])) {
                Activity activity = project.getActivity(Integer.parseInt(args[1]));
                activity.setDone(true);
            }
            return true;
        }
        return false;
    }

    // TODO: put in command structur
    //String employeeID, String Date
    public boolean cmdRequestEmployeeAvailability(String[] args) throws ParseException {
        if (checkArgumentlength(args.length,2)) {
            return false;
        }

        CompanyDB db = CompanyDB.getInstance();
        Employee employee = db.getEmployee(args[0]);
        try {
            Date date = this.formatter.parse(args[1]);
            if (employee.isOutOfOffie(date)) {
                this.writeOutput("Employee is not available");
                return false;
            } else {
                this.writeOutput("Employee is available");
                return true;
            }
        } catch (ParseException e) {
            this.writeOutput("Date must be in format " + this.formatter.toPattern());
            return false;
        }
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


    /*
        Utils
     */

    private boolean isSignedInEmployeePM(Project project) {
        CompanyDB db = CompanyDB.getInstance();
        return db.getSignedInEmployee() == project.getPM();
    }
}
