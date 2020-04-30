package dk.dtu.SoftEngExamProjectG18.Business;

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

    public void assertOpenActivities() throws IllegalArgumentException {
        if (this.getNumOpenActivities() == 0) {
            String output = String.format(
                "The employee %s has no room for any new activities at the moment.",
                this.getID()
            );
            throw new IllegalArgumentException(output);
        }
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

        if(context.equals("submissions") && collection.size() == 1 && collection.get(0) instanceof Employee) {
            return this.extractSubmissions((Employee) collection.get(0));
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
            for(Activity activity : employee.getAllActiveActivities()) {
                Date start = activity.getStartWeek();

                Calendar endCal = new GregorianCalendar();
                endCal.add(Calendar.WEEK_OF_YEAR, 1);
                endCal.add(Calendar.SECOND, -1);
                Date end = endCal.getTime();

                if(start.compareTo(date) <= 0 && end.compareTo(date) >= 0) {
                    activeActivities++;
                }
            }

            if(activeActivities >= employee.getWeeklyActivityCap()) {
                continue;
            }

            HashMap<String, String> entry = new HashMap<>();
            entry.put("ID", employee.getID());
            entry.put("Name", employee.getName());
            entry.put("Available activity slots", String.valueOf(employee.getWeeklyActivityCap() - activeActivities));

            result.add(entry);
        }

        return result;
    }

    public ArrayList<HashMap<String, String>> extractSubmissions(Employee employee) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        Date today = new Date();

        for(HashMap<Integer, EmployeeActivityIntermediate> projectActivities : employee.getActivities().values()) {
            for(EmployeeActivityIntermediate eai : projectActivities.values()) {
                String formattedDate = eai.getFormatter().format(today);
                HashMap<String, Integer> minutesSpent = eai.getMinutesSpent();

                if(!minutesSpent.containsKey(formattedDate)) {
                    continue;
                }

                Activity activity = eai.getActivity();
                Project project = activity.getProject();

                double trackedHours = minutesSpent.get(formattedDate) / 60.0;

                HashMap<String, String> entry = new HashMap<>();
                entry.put("Project ID", project.getID());
                entry.put("Activity ID", String.valueOf(activity.getID()));
                entry.put("Tracked hours", String.valueOf(trackedHours));
                result.add(entry);
            }
        }

        return result;
    }
}
