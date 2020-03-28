package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Main;

import static org.junit.Assert.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class MainSteps {

    // System output can be collected using this variable
    protected static ByteArrayOutputStream outputTo = new ByteArrayOutputStream();

    // Empty InputStream resulting in input loop breaking right away
    protected static InputStream inSource = new InputStream() {
        @Override
        public int read() { return -1; }
    };

    // Output outputted through this outSource can later be retrieved using outputTo
    protected static PrintStream outSource = new PrintStream(outputTo);

    @When("a user boots the application with the following arguments")
    public void aUserBootsTheApplicationWithTheFollowingArguments(List<String> args) {
        Main.setInSource(inSource);
        Main.setOutSource(outSource);
        Main.main(args.toArray(String[]::new));
    }

    @Then("{string} is a part of the output")
    public void isAPartOfTheOutput(String arg0) {
        assertNotEquals(outputTo.toString().indexOf(arg0), -1);
    }
}
