package dk.dtu.SoftEngExamProjectG18.Core;

import java.util.Date;
import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;

public class OutOfOfficeActivity {
    protected OOOActivityType type;

    protected boolean isDone = false;

    protected Date start = null;
    protected Date end = null;

    public OutOfOfficeActivity(OOOActivityType type,Date start, Date end) {
        this.type = type;
        this.start=start;
        this.end=end;
        //TODO: Put into Project hashmap
    }
    public boolean isDone() {
        Date today = new Date();
        if(today.compareTo(this.end)>0) {
            this.isDone=true;
        }
        return isDone;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public OOOActivityType getType() {
        return type;
    }
}
