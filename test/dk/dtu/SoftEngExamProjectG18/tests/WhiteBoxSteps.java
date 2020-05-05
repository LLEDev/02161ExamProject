package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.General.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class WhiteBoxSteps {

    protected Exception exception = null;

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

    @Then("the white box error exception of type {string} with message {string} is given")
    public void theWhiteBoxErrorExceptionOfTypeWithMessageIsGiven(String type, String msg) {
        Assert.assertNotNull(this.exception);
        Assert.assertEquals(type, this.exception.getClass().getSimpleName());
        Assert.assertEquals(msg, this.exception.getMessage());
    }
}
