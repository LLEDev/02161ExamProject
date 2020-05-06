package dk.dtu.SoftEngExamProjectG18.Business.Extractors;

import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.Extractor;

import java.util.*;

public class EmployeeAvailabilityExtractor implements Extractor<Employee> {

    protected boolean isEmployeeAvailable(Employee employee, Date date) {
        return employee.getOOOActivities().stream()
            .noneMatch(OOOActivity -> OOOActivity.getStart().compareTo(date) <= 0 && OOOActivity.getEnd().compareTo(date) >= 0);
    }

    @Override
    public ArrayList<HashMap<String, String>> extract(ArrayList<Employee> collection, HashMap<String, Object> metaData) throws IllegalArgumentException {
        if (!metaData.containsKey("date") || !(metaData.get("date") instanceof Date)) {
            throw new IllegalArgumentException("Date metadata has to be provided.");
        }

        return this.extractAvailability((Date) metaData.get("date"), collection);
    }

    public ArrayList<HashMap<String, String>> extractAvailability(Date date, ArrayList<Employee> collection) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for (Employee employee : collection) {
            if (this.isEmployeeAvailable(employee, date)) {
                int activeActivities = 0;
                for (Activity activity : employee.getAllActiveActivities()) {
                    Date start = activity.getStartWeek();

                    Calendar endCal = new GregorianCalendar();
                    endCal.add(Calendar.WEEK_OF_YEAR, 1);
                    endCal.add(Calendar.SECOND, -1);
                    Date end = endCal.getTime();

                    if (start.compareTo(date) <= 0 && end.compareTo(date) >= 0) {
                        activeActivities++;
                    }
                }

                if (activeActivities >= employee.getWeeklyActivityCap()) {
                    continue;
                }

                HashMap<String, String> entry = new HashMap<>();
                entry.put("ID", employee.getID());
                entry.put("Name", employee.getName());
                entry.put("Available activity slots", String.valueOf(employee.getWeeklyActivityCap() - activeActivities));

                result.add(entry);
            }
        }

        return result;
    }

}
