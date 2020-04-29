package dk.dtu.SoftEngExamProjectG18.Business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Interfaces.Extractable;

public class OutOfOfficeActivity implements Extractable<OutOfOfficeActivity> {

    protected OOOActivityType type;

    protected Date start;
    protected Date end;

    public OutOfOfficeActivity(OOOActivityType type, Date start, Date end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public Date getEnd() {
        return this.end;
    }

    public OOOActivityType getType() {
        return this.type;
    }

    public Date getStart() {
        return this.start;
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for(Extractable<?> extractable : collection) {
            if(!(extractable instanceof OutOfOfficeActivity)) {
                continue;
            }

            OutOfOfficeActivity OOOActivity = (OutOfOfficeActivity) extractable;

            HashMap<String, String> entry = new HashMap<>();
            entry.put("Type", OOOActivity.getType().toString());
            entry.put("Start", formatter.format(OOOActivity.getStart()));
            entry.put("End", formatter.format(OOOActivity.getEnd()));

            result.add(entry);
        }

        return result;
    }
}
