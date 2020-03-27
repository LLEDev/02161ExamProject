package dk.dtu.SoftEngExamProjectG18.Relations;

import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;

import java.util.Date;
import java.util.HashMap;

public class EmployeeActivityIntermediate {

    // Date -> Minutes
    protected HashMap<String, Integer> minutesSpent = new HashMap<>();

    protected Activity activity;
    protected Employee employee;

    protected String stringifyDate(Date d) {
        return d.getYear() + "-" + (d.getMonth() + 1) + "-" + d.getDay();
    }

    public EmployeeActivityIntermediate(Employee e, Activity a) {
        this.employee = e;
        this.activity = a;

        Project project = a.getProject();

        HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> employeeProjectActivities = e.getActivities();
        if(!employeeProjectActivities.containsKey(project.getID())) {
            employeeProjectActivities.put(project.getID(), new HashMap<>());
        }

        employeeProjectActivities.get(project.getID()).put(a.getID(), this);

        a.getTrackedTime().put(e.getID(), this);
    }

    public void addMinutes(Date d, int minutes) {
        int cumulatedMinutes = minutes + this.getMinutes(d);
        this.setMinutes(d, cumulatedMinutes);
    }

    public int getMinutes(Date d) {
        return this.minutesSpent.getOrDefault(this.stringifyDate(d), 0);
    }

    public void setMinutes(Date d, int minutes) {
        this.minutesSpent.put(this.stringifyDate(d), minutes);
    }


}
