package dk.dtu.SoftEngExamProjectG18.tests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

public class EmployeeSteps {

    /*
        Create methods
     */

    @Given("there is an employee")
    public void thereIsAnEmployee() {
        // TODO: Create employee
        // TODO: Sign employee in
        // TODO: Set correct inputContext
    }

    @And("there is an employee with initials {string}")
    public void thereIsAnEmployeeWithInitials(String arg0) {

    }

    @And("the following employees are given")
    public void theFollowingEmployeesAreGiven(List<String> employees) {

    }

    /*
        Other
     */

    @Given("the employee is the project manager for the project")
    public void theEmployeeIsTheProjectManagerForTheProject() {

    }

    @And("the employee with initials {string} is the project manager of the project")
    public void theEmployeeWithInitialsIsTheProjectManagerOfTheProject(String arg0) {

    }

    @And("the employee with initials {string} is the actor")
    public void theEmployeeWithInitialsIsTheActor(String arg0) {

    }

    @When("the actor adds the employee with initials {string} to the activity with ID {string}")
    public void theActorAddsTheEmployeeWithInitialsToTheActivityWithID(String arg0, String arg1) {

    }

    @And("the employee with initials {string} is assigned to the the activity with ID {string}")
    public void theEmployeeWithInitialsIsAssignedToTheTheActivityWithID(String arg0, String arg1) {
    }

    @And("the employee is attached to all activities in the projects")
    public void theEmployeeIsAttachedToAllActivitiesInTheProjects(List<String> projects) {
        
    }

    @When("the employee requests assistance from {string} on activity with ID {string} in the project")
    public void theEmployeeRequestsAssistanceFromOnActivityWithIDInTheProject(String arg0, String arg1) {
        
    }

    @When("the employee requests an overview of the project")
    public void theEmployeeRequestsAnOverviewOfTheProject() {
        
        
    }

    @When("the employee submits the work minutes")
    public void theEmployeeSubmitsTheWorkMinutes(List<List<String>> minutes) {
    }

    /*
        Assert
     */

    @Then("the employee with initials {string} is assigned to the activity with ID {string}")
    public void theEmployeeWithInitialsIsAssignedToTheActivityWithID(String arg0, String arg1) {

    }

    @Then("the employee sees that the project has a single activity with {string} hours spent out of {string} estimated hours needed")
    public void theEmployeeSeesThatTheProjectHasASingleActivityWithHoursSpentOutOfEstimatedHoursNeeded(String arg0, String arg1) {
    }

    @And("the employee with initials {string} has not reached the activity cap")
    public void theEmployeeWithInitialsHasNotReachedTheActivityCap(String arg0) {

    }

}
