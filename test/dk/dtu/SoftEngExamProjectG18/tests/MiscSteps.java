package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Controller.Enums.InputContextType;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertTrue;

public class MiscSteps {

    @Before // Reset Application before each scenario
    public void beforeScenario(){
        Application.init(InputContextType.Emp);
    }

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String message) {
        TestHolder testHolder = TestHolder.getInstance();
        assertTrue(testHolder.getResponse().exceptionMessageIs(message));
    }
}
