package dk.dtu.SoftEngExamProjectG18.Business.Extractors;

import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.Extractor;
import dk.dtu.SoftEngExamProjectG18.General.Dates;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityOverviewExtractor implements Extractor<Activity> {

    @Override
    public ArrayList<HashMap<String, String>> extract(ArrayList<Activity> collection, HashMap<String, Object> metaData) {
        return this.extractOverview(collection);
    }

    public ArrayList<HashMap<String, String>> extractOverview(ArrayList<Activity> collection) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for (Activity activity : collection) {
            String startWeek = Dates.formatWeek(activity.getStartWeek());
            String endWeek = Dates.formatWeek(activity.getEndWeek());

            int trackedHours = (int) Math.ceil(activity.getTotalTrackedMinutes() / 60.0);

            HashMap<String, String> entry = new HashMap<>();
            entry.put("ID", String.valueOf(activity.getID()));
            entry.put("Name", activity.getName());
            entry.put("Start week", startWeek);
            entry.put("End week", endWeek);
            entry.put("Estimated work hours (in total)", String.valueOf(activity.getEstimatedHours()));
            entry.put("Tracked work hours (in total)", String.valueOf(trackedHours));

            result.add(entry);
        }

        return result;
    }

}
