package dk.dtu.SoftEngExamProjectG18.Business.Extractors;

import dk.dtu.SoftEngExamProjectG18.Business.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.Extractor;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeActivityIntermediateOverviewExtractor implements Extractor<EmployeeActivityIntermediate> {

    @Override
    public ArrayList<HashMap<String, String>> extract(ArrayList<EmployeeActivityIntermediate> collection, HashMap<String, Object> metaData) {
        return this.extractOverview(collection);
    }

    public ArrayList<HashMap<String, String>> extractOverview(ArrayList<EmployeeActivityIntermediate> collection) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for(EmployeeActivityIntermediate eai : collection) {
            HashMap<String, Integer> minutesSpent = eai.getMinutesSpent();

            for(String date : minutesSpent.keySet()) {
                int minutes = minutesSpent.get(date);

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
