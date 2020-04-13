package dk.dtu.SoftEngExamProjectG18.Core;

import java.util.Date;
import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;

public class OutOfOfficeActivity {
    protected int ID;
    protected String name;
    protected Project project;
    protected OOOActivityType type;

    protected boolean isDone = false;

    protected Date start = null;
    protected Date end = null;

    public OutOfOfficeActivity(String name, Project project, OOOActivityType type) {
        this.ID = project.incrementNextActivityID();
        this.name = name;
        this.project = project;
        this.type = type;

        //TODO: Put into Project hashmap
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public boolean isDone() {
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
