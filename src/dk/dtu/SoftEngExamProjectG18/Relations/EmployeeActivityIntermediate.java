package dk.dtu.SoftEngExamProjectG18.Relations;

import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.Interfaces.Extractable;

import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeActivityIntermediate implements Extractable<EmployeeActivityIntermediate> {

    protected SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    // Date -> Minutes
    protected HashMap<String, Integer> minutesSpent = new HashMap<>();

    protected Activity activity;
    protected Employee employee;

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

    public Activity getActivity() {
        return this.activity;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public SimpleDateFormat getFormatter() {
        return this.formatter;
    }

    public int getMinutes(Date d) {
        return this.minutesSpent.getOrDefault(this.formatter.format(d), 0);
    }

    public HashMap<String, Integer> getMinutesSpent() {
        return this.minutesSpent;
    }

    public int getTotalMinutes() {
        return this.minutesSpent.values().stream().mapToInt(i -> i).sum();
    }

    public void setMinutes(Date d, int minutes) {
        this.minutesSpent.put(this.formatter.format(d), minutes);
    }

    /*
        Table extraction methods
     */

    @Override
    public ArrayList<HashMap<String, String>> extract(String context, HashMap<String, Object> metaData, ArrayList<? extends Extractable<?>> collection) {
        if(context.equals("overview")) {
            return this.extractOverview(collection);
        }

        return null;
    }

    public ArrayList<HashMap<String, String>> extractOverview(ArrayList<? extends Extractable<?>> collection) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for(Extractable<?> extractable : collection) {
            if(!(extractable instanceof EmployeeActivityIntermediate)) {
                continue;
            }

            EmployeeActivityIntermediate eai = (EmployeeActivityIntermediate) extractable;

            for(String date : eai.minutesSpent.keySet()) {
                int minutes = eai.minutesSpent.get(date);

                HashMap<String, String> entry = new HashMap<>();
                entry.put("Employee", eai.getEmployee().getName());
                entry.put("Date", date);
                entry.put("Minutes", String.valueOf(minutes));

                result.add(entry);
            }
        }

        return result;
    }
}
