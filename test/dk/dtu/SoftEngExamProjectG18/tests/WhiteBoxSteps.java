package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.*;
import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.General.Dates;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.text.ParseException;
import java.util.Date;

public class WhiteBoxSteps {

    protected Exception exception = null;

    /**
     * @author Someone
     */
    protected Activity getProjectActivity() {
        Project project = TestHolder.getInstance().getProject();
        Assert.assertNotNull(project);

        Activity activity = project.getActivity(1);
        Assert.assertNotNull(activity);

        return activity;
    }

    /**
     * @author Someone
     */
    @When("assignPM is called in the project instance with {string} as newPM")
    public void assignPMIsCalledInTheProjectInstanceWithAsNewPM(String newPMStr) {
        this.exception = null;

        Application application = Application.getInstance();
        Employee signedIn = application.getSignedInEmployee();
        Employee newPM = newPMStr != null ? application.getEmployee(newPMStr) : null;

        Project project = TestHolder.getInstance().getProject();
        try {
            project.assignPM(newPM, signedIn);
        } catch (AccessDeniedException e) {
            this.exception = e;
        }
    }

    /**
     * @author Someone
     */
    @When("the project activity start week is set to {string}")
    public void theProjectActivityStartWeekIsSetTo(String week) throws ParseException {
        this.exception = null;

        Date d = Dates.parseWeek(week);

        try {
            this.getProjectActivity().setStartWeek(d);
        } catch (IllegalArgumentException e) {
            this.exception = e;
        }
    }

    /**
     * @author Someone
     */
    @When("the project activity end week is set to {string}")
    public void theProjectActivityEndWeekIsSetTo(String week) throws ParseException {
        this.exception = null;

        Date d = Dates.parseWeek(week);

        try {
            this.getProjectActivity().setEndWeek(d);
        } catch (IllegalArgumentException e) {
            this.exception = e;
        }
    }

    /**
     * @author Someone
     */
    @Then("the white box error exception of type {string} with message {string} is given")
    public void theWhiteBoxErrorExceptionOfTypeWithMessageIsGiven(String type, String msg) {
        Assert.assertNotNull(this.exception);
        Assert.assertEquals(type, this.exception.getClass().getSimpleName());
        Assert.assertEquals(msg, this.exception.getMessage());
    }

    /**
     * @author Thor Dueholm (s194589)
     */
    @When("request assistance is called in Application with arguments {string}, {string} And {string}")
    public void requestAssistanceIsCalledInApplicationWithArgumentsAnd(String projectID, String activitID, String employeeID) {
        Application application = Application.getInstance();

        try {
            application.requestAssistance(projectID, Integer.parseInt(activitID), employeeID);
        } catch (IllegalArgumentException | AccessDeniedException e) {
            this.exception = e;
        }
    }

    /**
     * @author Someone
     */
    @Then("the project activity start week is {string}")
    public void theProjectActivityStartWeekIs(String weekStr) {
        weekStr = !weekStr.equals("null") ? weekStr : null;
        Date week = this.getProjectActivity().getStartWeek();

        Assert.assertEquals(
            weekStr,
            week != null ? Dates.formatWeek(week) : null
        );
    }

    /**
     * @author Someone
     */
    @And("the project activity end week is {string}")
    public void theProjectActivityEndWeekIs(String weekStr) {
        weekStr = !weekStr.equals("null") ? weekStr : null;
        Date week = this.getProjectActivity().getEndWeek();

        Assert.assertEquals(
            weekStr,
            week != null ? Dates.formatWeek(week) : null
        );
    }

    /**
     * @author Thor Dueholm (s194589)
     */
    @When("submit minutes is called with date {string} and minutes {string} on the relation between the activity and the employee")
    public void submitMinutesIsCalledWithDateAndMinutesOnTheRelationBetweenTheActivityAndTheEmployee(String dateStr, String minutes) throws ParseException {
        Project project = TestHolder.getInstance().getProject();
        Assert.assertNotNull(project);
        Activity activity = project.getActivity(1);
        Assert.assertNotNull(activity);

        Employee employee = Application.getInstance().getSignedInEmployee();
        Assert.assertNotNull(employee);

        EmployeeActivityIntermediate eai = activity.getTrackedTime().get(employee.getID());
        Assert.assertNotNull(eai);

        Date date = !dateStr.equals("null") ? Dates.parseDate(dateStr) : null;
        try {
            eai.submitMinutes(date, Integer.parseInt(minutes));
        } catch (IllegalArgumentException e) {
            this.exception = e;
        }
    }

    /**
     * @author Thor Dueholm (s194589)
     */
    @Then("the employee has spent {string} minutes on the activity on the day {string}")
    public void theEmployeeHasSpentMinutesOnTheActivityOnTheDay(String minutesStr, String dateStr) throws ParseException {
        Project project = TestHolder.getInstance().getProject();
        Assert.assertNotNull(project);
        Activity activity = project.getActivity(1);
        Assert.assertNotNull(activity);

        Employee employee = Application.getInstance().getSignedInEmployee();
        Assert.assertNotNull(employee);

        EmployeeActivityIntermediate eai = activity.getTrackedTime().get(employee.getID());
        Assert.assertNotNull(eai);

        Date date = !dateStr.equals("null") ? Dates.parseDate(dateStr) : null;

        int minutes = eai.getMinutes(date);
        Assert.assertEquals(minutes, Integer.parseInt(minutesStr));
    }
}
