package dk.dtu.SoftEngExamProjectG18.Business;

import dk.dtu.SoftEngExamProjectG18.Business.Enums.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.General.Dates;

import java.util.Date;

public class OutOfOfficeActivity {

    protected OOOActivityType type;

    protected Date start;
    protected Date end;

    /**
     * @author Someone
     */
    public OutOfOfficeActivity(OOOActivityType type, Date start, Date end) throws IllegalArgumentException {
        this.type = type;

        Dates.assertStartEndValid(start, end);

        this.start = start;
        this.end = end;
    }

    /**
     * @author Someone
     */
    public Date getEnd() {
        return this.end;
    }

    /**
     * @author Someone
     */
    public OOOActivityType getType() {
        return this.type;
    }

    /**
     * @author Someone
     */
    public Date getStart() {
        return this.start;
    }

}
