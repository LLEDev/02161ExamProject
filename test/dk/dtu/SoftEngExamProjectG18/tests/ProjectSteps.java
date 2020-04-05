package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

public class ProjectSteps {

    /*
        Create project(s) methods
     */

    @Given("that there is a project with name {string}")
    public void thatThereIsAProjectWithName(String name) {
        CompanyDB db = CompanyDB.getInstance();
        Project project = new Project(name);
        db.getProjects().put(project.getID(), project);
    }

    @Given("there are projects with names")
    public void thereAreProjectsWithNames(List<String> projects) {
        CompanyDB db = CompanyDB.getInstance();
        for (String name : projects) {
            Project project = new Project(name);
            db.getProjects().put(project.getID(), project);
        }
    }

    @When("the employee creates a project with name {string}")
    public void theEmployeeCreatesAProjectWithName(String name) {
        CompanyDB db = CompanyDB.getInstance();
        EmployeeInputContext input = (EmployeeInputContext) db.getInputContext();
        String[] projectArguments = new String[] {name};
        input.cmdCreateProject(projectArguments);
    }

    /*
        Other
     */

    @When("the employee adds an activity with name {string} to the project")
    public void theEmployeeAddsAnActivityWithNameToTheProject(String arg0) {

    }

    @When("the actor assigns the employee with initials {string} as the project manager of the project")
    public void theActorAssignsTheEmployeeWithInitialsAsTheProjectManagerOfTheProject(String arg0) {

    }

    @When("the employee finishes the activity with ID {string} in the project")
    public void theEmployeeFinishesTheActivityWithIDInTheProject(String arg0) {

    }

    @Then("the activity with ID {string} is marked as finished in the project")
    public void theActivityWithIDIsMarkedAsFinishedInTheProject(String arg0) {
    }

    @And("the activity with ID {string} has an estimated duration of {string} hours and registered {string} hours spent")
    public void theActivityWithIDHasAnEstimatedDurationOfHoursAndRegisteredHoursSpent(String arg0, String arg1, String arg2) {

    }


    /*
        Assert methods
     */

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
