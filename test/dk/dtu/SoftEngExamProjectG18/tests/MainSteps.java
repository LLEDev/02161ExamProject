package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Main;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MainSteps {

    // Inputted commands
    protected static String input = String.join("\n", new String[]{
        "help",
        "view availability",
        "view availability 2020-04-01",
        "view availability \"arg 2\"",
        "unknown command"
    });

    // System output can be collected using this variable
    protected static ByteArrayOutputStream outputTo = new ByteArrayOutputStream();

    // Empty InputStream resulting in input loop breaking right away
    protected static InputStream inSource = new ByteArrayInputStream(input.getBytes());

    // Output outputted through this outSource can later be retrieved using outputTo
    protected static PrintStream outSource = new PrintStream(outputTo);

    /**
     * @author Someone
     */
    @When("a user boots the application with the following arguments")
    public void aUserBootsTheApplicationWithTheFollowingArguments(List<String> args) {
        Main.setInSource(inSource);
        Main.setOutSource(outSource);
        Main.main(args.toArray(String[]::new));
    }

    /**
     * @author Someone
     */
    @When("a user boots the application with valid external data files and the following arguments")
    public void aUserBootsTheApplicationWithValidExternalDataFilesAndTheFollowingArguments(List<String> args) throws Exception {
        Main.setInSource(inSource);
        Main.setOutSource(outSource);

        ClassLoader cl = Main.class.getClassLoader();
        String path = Paths.get(Objects.requireNonNull(cl.getResource("data"))
            .toURI()).toString();

        Main.main(new String[]{args.get(0), args.get(1), path});
    }

    /**
     * @author Someone
     */
    @When("a user boots the application with test data files and the following arguments")
    public void aUserBootsTheApplicationWithTestDataFilesAndTheFollowingArguments(List<String> args) throws Exception {
        Main.setInSource(inSource);
        Main.setOutSource(outSource);

        ClassLoader cl = Main.class.getClassLoader();
        String path = Paths.get(Objects.requireNonNull(cl.getResource("data/test")).toURI()).toString();

        Main.main(new String[]{args.get(0), args.get(1), path});
    }

    /**
     * @author Someone
     */
    @Then("{string} is a part of the output")
    public void isAPartOfTheOutput(String arg0) {
        assertNotEquals(outputTo.toString().indexOf(arg0), -1);
    }

    /**
     * @author Someone
     */
    @Then("{string} is not a part of the output")
    public void isNotAPartOfTheOutput(String arg0) {
        assertEquals(outputTo.toString().indexOf(arg0), -1);
    }

    /**
     * @author Someone
     */
    @Then("the output will be reset")
    public void theOutputWillBeReset() {
        outputTo.reset();
        inSource = new ByteArrayInputStream(input.getBytes());
    }

    /**
     * @author Someone
     */
    @And("a user quits the application")
    public void aUserQuitsTheApplication() throws Exception {
        Main.redirectInput(new String[]{"quit"});
    }
}
