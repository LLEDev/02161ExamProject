package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.General.Assertions;
import dk.dtu.SoftEngExamProjectG18.General.Dates;

import java.util.Date;
import java.util.HashMap;

public class EmployeeActivityIntermediate {

    public static EmployeeActivityIntermediate initAssociation(Employee employee, Activity activity) {
        employee.assertOpenActivities();
        return new EmployeeActivityIntermediate(employee, activity);
    }

    public static EmployeeActivityIntermediate getAssociation(Employee employee, Activity activity) throws AccessDeniedException {
        HashMap<String, EmployeeActivityIntermediate> trackedTime = activity.getTrackedTime();
        EmployeeActivityIntermediate employeeActivityIntermediate = trackedTime.get(employee.getID());

        if (employeeActivityIntermediate == null) {
            throw new AccessDeniedException("You are not associated with one or more of these projects.");
        }

        return employeeActivityIntermediate;
    }

    // Date -> Minutes
    protected HashMap<String, Integer> minutesSpent = new HashMap<>();

    protected Activity activity;
    protected Employee employee;

    public EmployeeActivityIntermediate(Employee e, Activity a) {
        this.employee = e;
        this.activity = a;

        Project project = a.getProject();

        HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> employeeProjectActivities = e.getActivities();
        if (!employeeProjectActivities.containsKey(project.getID())) {
            employeeProjectActivities.put(project.getID(), new HashMap<>());
        }

        employeeProjectActivities.get(project.getID()).put(a.getID(), this);

        a.getTrackedTime().put(e.getID(), this);
    }

    public Activity getActivity() {
        return this.activity;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public int getMinutes(Date d) throws IllegalArgumentException {
        return this.minutesSpent.getOrDefault(Dates.formatDate(d), 0);
    }

    public HashMap<String, Integer> getMinutesSpent() {
        return this.minutesSpent;
    }

    public int getTotalMinutes() {
        return this.minutesSpent.values().stream().mapToInt(i -> i).sum();
    }

    public void setMinutes(Date d, int minutes) throws IllegalArgumentException {
        Assertions.assertOrThrow(
            () -> new IllegalArgumentException(
                String.format("The set number of work minutes has to be more than or equal to 0. %s received.", minutes)
            ),
            minutes >= 0
        );

        String dateString = Dates.formatDate(d);

        this.minutesSpent.put(dateString, minutes);
    }

    public void submitMinutes(Date d, int minutes) throws IllegalArgumentException {
        Assertions.assertOrThrow(
            () -> new IllegalArgumentException(
                String.format("The submitted number of work minutes has to be more than or equal to 0. %s received.", minutes)
            ),
            minutes >= 0
        );

        int total = this.getMinutes(d) + minutes;

        Assertions.assertOrThrow(
            () -> new IllegalArgumentException("The total amount of work minutes is now too high. This action cannot be done."),
            total >= 0
        );

        String dateString = Dates.formatDate(d);

        assert d != null && minutes >= 0 : "Precondition of submitMinutes";

        this.minutesSpent.put(dateString, total);

        assert this.minutesSpent.containsKey(dateString) &&
            this.minutesSpent.get(dateString) == total :
            "Postcondition of submitMinutes";
    }

}
