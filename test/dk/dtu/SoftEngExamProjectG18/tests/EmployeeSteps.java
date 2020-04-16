package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;

public class EmployeeSteps {

    private CompanyDB db;

    public EmployeeSteps () {
       this.db = CompanyDB.getInstance();
    }

    /*
        Create methods
     */

    @Given("there is an employee")
    public void thereIsAnEmployee() {
        thereIsAnEmployeeWithInitials("HH");
    }

    @And("there is an employee with initials {string}")
    public void thereIsAnEmployeeWithInitials(String employeeID) {
        this.db.getEmployees().put(employeeID, new Employee(employeeID, employeeID));
        this.db.setSignedInEmployee(employeeID);
        this.db.setInputContext(new EmployeeInputContext());
    }

    @And("the following employees are given")
    public void theFollowingEmployeesAreGiven(List<String> employees) {
        for (String employeeID: employees) {
            thereIsAnEmployeeWithInitials(employeeID);
        }
    }

    /*
        Other
     */

    @Given("the employee is the project manager for the project")
    public void theEmployeeIsTheProjectManagerForTheProject() {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Employee employee = this.db.getSignedInEmployee();
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();
        String[] args = new String[2];
        args[0] = project.getID();
        args[1] = employee.getID();
        input.cmdAssignPM(args);
    }

    @And("the employee with initials {string} is the project manager of the project")
    public void theEmployeeWithInitialsIsTheProjectManagerOfTheProject(String employeeID) {
        Employee employee = this.db.getEmployee(employeeID);
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();
        String[] args = {project.getID(),employeeID};
        input.cmdAssignPM(args);
        Assert.assertEquals((project.getPM()), employee);
    }

    @And("the employee with initials {string} is the actor")
    public void theEmployeeWithInitialsIsTheActor(String employeeID) {
        this.db.setSignedInEmployee(employeeID);
    }

    @When("the actor adds the employee with initials {string} to the activity with ID {string}")
    public void theActorAddsTheEmployeeWithInitialsToTheActivityWithID(String employeeID, String activityID) {
        theEmployeeWithInitialsIsTheActor("HH");
        thereIsAnEmployeeWithInitials(employeeID);
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = new Activity("test",project);
        project.getActivities().put(Integer.parseInt(activityID), activity);
        EmployeeActivityIntermediate intermediate = new EmployeeActivityIntermediate(this.db.getEmployee(employeeID),activity);
    }

    @And("the employee with initials {string} is assigned to the the activity with ID {string}")
    public void theEmployeeWithInitialsIsAssignedToTheTheActivityWithID(String arg0, String arg1) {
        theActorAddsTheEmployeeWithInitialsToTheActivityWithID(arg0, arg1);
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = project.getActivity(Integer.parseInt(arg1));
        Assert.assertEquals(activity.getID(),Integer.parseInt(arg1));
        //TODO: Find a way to check employee from argument with employee from employeeActivityIntermediate or elsewhere
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
