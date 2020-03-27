package dk.dtu.SoftEngExamProjectG18.Context;

import dk.dtu.SoftEngExamProjectG18.Enum.OOOActivityType;

import java.util.Date;

public class EmployeeInputContext extends InputContext {

    public void assignPM(String projectID, String PMID) {

    }

    public void createProject(String name, boolean isBillable) {

    }

    public void markActivityAsDone(String projectID, int activityID) {

    }

    public void requestAssistance(String projectID, int activityID, String employeeID) {

    }

    public void requestDailyOverview() {

    }

    public void requestOutOfOffice(OOOActivityType type, Date start, Date end) {

    }

    public void setHours(String projectID, int activityID, Date date, int setHours) {

    }

    public void submitHours(String projectID, int activityID, Date date, int addedHours) {

    }

}
