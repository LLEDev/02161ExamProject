package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.General.DateFormatter;
import dk.dtu.SoftEngExamProjectG18.General.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.text.ParseException;
import java.util.Date;

public class WhiteBoxSteps {

    protected Exception exception = null;

    protected Activity getProjectActivity() {
        Project project = TestHolder.getInstance().getProject();
        Assert.assertNotNull(project);

        Activity activity = project.getActivity(1);
        Assert.assertNotNull(activity);

        return activity;
    }

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

    @When("the project activity start week is set to {string}")
    public void theProjectActivityStartWeekIsSetTo(String week) throws ParseException {
        this.exception = null;

        Date d = DateFormatter.parseWeek(week);

        try {
            this.getProjectActivity().setStartWeek(d);
        } catch (IllegalArgumentException e) {
            this.exception = e;
        }
    }

    @When("the project activity end week is set to {string}")
    public void theProjectActivityEndWeekIsSetTo(String week) throws ParseException {
        this.exception = null;

        Date d = DateFormatter.parseWeek(week);

        try {
            this.getProjectActivity().setEndWeek(d);
        } catch (IllegalArgumentException e) {
            this.exception = e;
        }
    }

    @Then("the white box error exception of type {string} with message {string} is given")
    public void theWhiteBoxErrorExceptionOfTypeWithMessageIsGiven(String type, String msg) {
        System.out.println(this.exception);

        Assert.assertNotNull(this.exception);
        Assert.assertEquals(type, this.exception.getClass().getSimpleName());
        Assert.assertEquals(msg, this.exception.getMessage());
    }

    @Then("the project activity start week is {string}")
    public void theProjectActivityStartWeekIs(String weekStr) {
        weekStr = !weekStr.equals("null") ? weekStr : null;
        Date week = this.getProjectActivity().getStartWeek();

        Assert.assertEquals(
            weekStr,
            week != null ? DateFormatter.formatWeek(week) : null
        );
    }

    @And("the project activity end week is {string}")
    public void theProjectActivityEndWeekIs(String weekStr) {
        weekStr = !weekStr.equals("null") ? weekStr : null;
        Date week = this.getProjectActivity().getEndWeek();

        Assert.assertEquals(
            weekStr,
            week != null ? DateFormatter.formatWeek(week) : null
        );
    }
}
