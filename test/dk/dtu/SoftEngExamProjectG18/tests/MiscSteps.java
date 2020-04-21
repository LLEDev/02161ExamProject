package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertTrue;

public class MiscSteps {

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String message) {
        TestHolder testHolder = TestHolder.getInstance();
        assertTrue(testHolder.response.exceptionMessageIs(message));
    }


}
