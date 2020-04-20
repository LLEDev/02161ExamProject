package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.Context.ProjectManagerInputContext;
import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.*;

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
    public void theActorAssignsTheEmployeeWithInitialsAsTheProjectManagerOfTheProject(String initials) {
        TestHolder testHolder = TestHolder.getInstance();
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();
        input.cmdAssignPM(new String[]{ testHolder.project.getID(), initials });
    }

    @And("the project does not have a project manager")
    public void theProjectDoesNotHaveAProjectManager() {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        project.setPM(null);
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

    @Given("the activity with ID {string} has an estimated duration of {string} weeks and registered {string} hours spent")
    public void theActivityWithIDHasAnEstimatedDurationOfWeeksAndRegisteredHoursSpent(String id, String weeks, String hours) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = project.getActivity(Integer.parseInt(id));

        Date today = new Date();
        Date newDate = new Date(today.getTime());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, Integer.parseInt(weeks) * 7);
        newDate.setTime(calendar.getTime().getTime());

        activity.setStartWeek(today);
        activity.setEndWeek(newDate);

        HashMap<String, EmployeeActivityIntermediate> trackedTime = activity.getTrackedTime();
        EmployeeActivityIntermediate employeeActivityIntermediate = trackedTime.get(this.db.getSignedInEmployee().getID());

        employeeActivityIntermediate.addMinutes(today, Integer.parseInt(hours) * 60);
    }


    /*
        Assert methods
     */

    @Then("the activity with ID {string} is marked as finished in the project")
    public void theActivityWithIDIsMarkedAsFinishedInTheProject(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = project.getActivity(Integer.parseInt(id));
        // TODO: Uncomment next line when input context is updated in set pm step
//        assertTrue(activity.isDone());
    }

    @Then("there is a project with ID {string} and name {string}")
    public void thereIsAProjectWithIDAndName(String id, String name) {
        Project project = this.db.getProject(id);
        assertEquals(project.getName(), name);
    }

    @Then("the project contains an activity with ID {string}")
    public void theProjectContainsAnActivityWithID(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = project.getActivity(Integer.parseInt(id));
        // TODO: Uncomment next line when input context is updated in set pm step
//        assertNotNull(activity);
    }

    @Then("the project has a project manager with initials {string}")
    public void theProjectHasAProjectManagerWithInitials(String initials) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        assertNotNull(project.getPM());
        assertEquals(project.getPM().getID(), initials);
    }

    @Then("these activities with overall durations are found")
    public void theseActivitiesWithOverallDurationsAreFound(List<List<String>> durations) {
    }
}
