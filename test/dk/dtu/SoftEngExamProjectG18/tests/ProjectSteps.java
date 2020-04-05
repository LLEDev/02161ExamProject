package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.Context.ProjectManagerInputContext;
import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ProjectSteps {

    private CompanyDB db;

    public ProjectSteps () {
        this.db = CompanyDB.getInstance();
    }

    /*
        Create project(s) methods
     */

    @Given("that there is a project with name {string}")
    public void thatThereIsAProjectWithName(String name) {
        Project project = new Project(name);
        TestHolder testHolder = TestHolder.getInstance();
        testHolder.project = project;
        this.db.getProjects().put(project.getID(), project);
    }

    @Given("there are projects with names")
    public void thereAreProjectsWithNames(List<String> projects) {
        for (String name : projects) {
            Project project = new Project(name);
            this.db.getProjects().put(project.getID(), project);
        }
    }

    @And("there is an activity with ID {string}")
    public void thereIsAnActivityWithID(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = new Activity("Test Activity", project);
        project.getActivities().put(1, activity);
    }

    @When("the employee creates a project with name {string}")
    public void theEmployeeCreatesAProjectWithName(String name) {
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();
        String[] projectArguments = new String[] {name};
        input.cmdCreateProject(projectArguments);
    }

    /*
        Other
     */

    @When("the employee adds an activity with name {string} to the project")
    public void theEmployeeAddsAnActivityWithNameToTheProject(String name) {
        // TODO: Fix missing "employee is a pm" - test when that is implemented
        TestHolder testHolder = TestHolder.getInstance();
        ProjectManagerInputContext input = null;
        try {
            input = (ProjectManagerInputContext) this.db.getInputContext();
        } catch (ClassCastException e) {}
        if (input != null)
            input.cmdCreateActivity(new String[]{ name, testHolder.project.getID() });
    }

    @When("the actor assigns the employee with initials {string} as the project manager of the project")
    public void theActorAssignsTheEmployeeWithInitialsAsTheProjectManagerOfTheProject(String arg0) {

    }

    @When("the employee finishes the activity with ID {string} in the project")
    public void theEmployeeFinishesTheActivityWithIDInTheProject(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        ProjectManagerInputContext input = null;
        try {
            input = (ProjectManagerInputContext) this.db.getInputContext();
        } catch (ClassCastException e) {}
        if (input != null)
            input.cmdFinishActivity(new String[]{ id, testHolder.project.getID() });
    }

    @And("the activity with ID {string} has an estimated duration of {string} hours and registered {string} hours spent")
    public void theActivityWithIDHasAnEstimatedDurationOfHoursAndRegisteredHoursSpent(String id, String duration, String registeredHours) {

    }


    /*
        Assert methods
     */

    @Then("the activity with ID {string} is marked as finished in the project")
    public void theActivityWithIDIsMarkedAsFinishedInTheProject(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = project.getActivity(Integer.parseInt(id));
        // TODO: Uncomment next line when employee pm is implemented...
//        assertTrue(activity.isDone());
    }

    @Then("there is a project with ID {string} and name {string}")
    public void thereIsAProjectWithIDAndName(String arg0, String arg1) {

    }

    @Then("the project contains an activity with ID {string}")
    public void theProjectContainsAnActivityWithID(String arg0) {
    }

    @Then("the project has a project manager with initials {string}")
    public void theProjectHasAProjectManagerWithInitials(String arg0) {
    }

    @And("the project does not have a project manager")
    public void theProjectDoesNotHaveAProjectManager() {
    }

    @Then("these activities with overall durations are found")
    public void theseActivitiesWithOverallDurationsAreFound(List<List<String>> durations) {
    }
}
