package dk.dtu.SoftEngExamProjectG18.Business.Extractors;

import dk.dtu.SoftEngExamProjectG18.Business.Interfaces.Extractor;
import dk.dtu.SoftEngExamProjectG18.Business.OutOfOfficeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class OutOfOfficeActivityOverviewExtractor implements Extractor<OutOfOfficeActivity> {

    /**
     * @author Someone
     */
    @Override
    public ArrayList<HashMap<String, String>> extract(ArrayList<OutOfOfficeActivity> collection, HashMap<String, Object> metaData) {
        return this.extractOverview(collection);
    }

    /**
     * @author Someone
     */
    public ArrayList<HashMap<String, String>> extractOverview(ArrayList<OutOfOfficeActivity> collection) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for (OutOfOfficeActivity OOOActivity : collection) {
            HashMap<String, String> entry = new HashMap<>();
            entry.put("Type", OOOActivity.getType().toString());
            entry.put("Start", formatter.format(OOOActivity.getStart()));
            entry.put("End", formatter.format(OOOActivity.getEnd()));

            result.add(entry);
        }

        return result;
    }

}
