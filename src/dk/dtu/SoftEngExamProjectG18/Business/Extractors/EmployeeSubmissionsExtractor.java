package dk.dtu.SoftEngExamProjectG18.Business.Extractors;

import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.Extractor;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.General.Dates;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EmployeeSubmissionsExtractor implements Extractor<Employee> {

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @Override
    public ArrayList<HashMap<String, String>> extract(ArrayList<Employee> collection, HashMap<String, Object> metaData) {
        return this.extractSubmissions(collection.get(0));
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public ArrayList<HashMap<String, String>> extractSubmissions(Employee employee) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        Date today = new Date();

        for (HashMap<Integer, EmployeeActivityIntermediate> projectActivities : employee.getActivities().values()) {
            for (EmployeeActivityIntermediate eai : projectActivities.values()) {
                String formattedDate = Dates.formatDate(today);
                HashMap<String, Integer> minutesSpent = eai.getMinutesSpent();

                if (minutesSpent.containsKey(formattedDate)) {
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
        }

        return result;
    }

}
