package dk.dtu.SoftEngExamProjectG18.Core;

import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Interfaces.Extractable;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;

import java.util.*;

public class Employee implements Extractable<Employee> {

    // ProjectID --> ActivityID --> EmployeeActivityIntermediate
    protected HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> activities = new HashMap<>();
    protected String ID;
    protected String name;
    protected ArrayList<OutOfOfficeActivity> OOOActivities = new ArrayList<>();
    protected int weeklyActivityCap = 10;

    public Employee(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Employee(String ID, String name, int weeklyActivityCap) {
        this.ID = ID;
        this.name = name;
        this.weeklyActivityCap = weeklyActivityCap;
    }

    public void addOOOActivity(OOOActivityType type, Date start, Date end) {
        this.OOOActivities.add(new OutOfOfficeActivity(type, start, end));
    }

    public boolean isOutOfOffice(Date date) {
        for (OutOfOfficeActivity OOOActivity : this.OOOActivities) {
            if (OOOActivity.start.compareTo(date) > 0 || OOOActivity.end.compareTo(date) < 0) {
                continue;
            }

            return true;
        }

        return false;
    }

    public HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> getActivities() {
        return this.activities;
    }

    public ArrayList<Activity> getAllActiveActivities() {
        HashMap<String, Activity> allActivities = new HashMap<>();
        for(HashMap<Integer, EmployeeActivityIntermediate> activities : this.getActivities().values()) {
            for(EmployeeActivityIntermediate intermediate : activities.values()) {
                Activity activity = intermediate.getActivity();
                String combinedID = activity.getProject().getID() + "-" + activity.getID();

                if(activity.isDone() || allActivities.containsKey(combinedID)) {
                    continue;
                }

                allActivities.put(combinedID, activity);
            }
        }

        return new ArrayList<>(allActivities.values());
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public int getNumOpenActivities() {
        return this.weeklyActivityCap - this.activities.size();
    }

    public ArrayList<OutOfOfficeActivity> getOOOActivities() {
        return this.OOOActivities;
    }

    public int getWeeklyActivityCap() {
        return this.weeklyActivityCap;
    }

    public void setWeeklyActivityCap(int weeklyActivityCap) {
        this.weeklyActivityCap = weeklyActivityCap;
    }


    /*
        Table extraction methods
     */

    @Override
    public ArrayList<HashMap<String, String>> extract(String context, HashMap<String, Object> metaData, ArrayList<? extends Extractable<?>> collection) {
        if(context.equals("availability") && metaData.containsKey("date") && metaData.get("date") instanceof Date) {
            return this.extractAvailability((Date) metaData.get("date"), collection);
        }

        return null;
    }

    public ArrayList<HashMap<String, String>> extractAvailability(Date date, ArrayList<? extends Extractable<?>> collection) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for(Extractable<?> extractable : collection) {
            if (!(extractable instanceof Employee)) {
                continue;
            }

            Employee employee = (Employee) extractable;

            boolean isAvailable = true;
            for(OutOfOfficeActivity OOOActivity : employee.getOOOActivities()) {
                if(OOOActivity.getStart().compareTo(date) <= 0 && OOOActivity.getEnd().compareTo(date) >= 0) {
                    isAvailable = false;
                    break;
                }
            }

            if(!isAvailable) {
                continue;
            }

            int activeActivities = 0;
            for(Activity activity : this.getAllActiveActivities()) {
                Date start = activity.getStartWeek();

                Calendar endCal = new GregorianCalendar();
                endCal.add(Calendar.WEEK_OF_YEAR, 1);
                endCal.add(Calendar.SECOND, -1);
                Date end = endCal.getTime();

                if(start.compareTo(date) <= 0 && end.compareTo(date) >= 0) {
                    activeActivities++;
                }
            }

            if(activeActivities >= this.getWeeklyActivityCap()) {
                continue;
            }

            HashMap<String, String> entry = new HashMap<>();
            entry.put("ID", employee.getID());
            entry.put("Name", employee.getName());
            entry.put("Available activity slots", String.valueOf(this.getWeeklyActivityCap() - activeActivities));

            result.add(entry);
        }

        return result;
    }
}
