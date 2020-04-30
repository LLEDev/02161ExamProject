package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.Context.ProjectManagerInputContext;
import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProjectSteps extends BaseSteps {

    /*
        Create project(s) methods
     */

    @Given("that there is a project with name {string}")
    public void thatThereIsAProjectWithName(String name) {
        Application application = Application.getInstance();
        TestHolder testHolder = TestHolder.getInstance();

        testHolder.setProject(application.createProject(name, true));
    }

    @Given("there are projects with names")
    public void thereAreProjectsWithNames(List<String> projects) {
        Application application = Application.getInstance();

        for (String name : projects) {
            application.createProject(name, true);
        }
    }

    @And("there is an activity with ID {string}")
    public void thereIsAnActivityWithID(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.getProject();
        int ID = Integer.parseInt(id);

        if(project.getActivities().containsKey(ID)) {
            return;
        }

        Activity activity = new Activity("Test Activity", project);
        project.getActivities().put(ID, activity);
    }

    @And("the project has the following activities")
    public void theProjectHasTheFollowingActivities(List<List<String>> activities) throws ParseException {
        TestHolder th = TestHolder.getInstance();
        Project project = th.getProject();
        Assert.assertNotNull(project);

        project.clearActivities();

        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-ww");
        for(List<String> activityString : activities) {
            if(activityString.size() == 0) {
                continue;
            }

            Activity activity = new Activity(activityString.get(0), project);

            for(int i = 1; i < activityString.size(); i++) {
                String cell = activityString.get(i);

                switch(i) {
                    case 1: activity.setEstimatedHours(Integer.parseInt(cell)); break;
                    case 2: activity.setStartWeek(weekFormatter.parse(cell)); break;
                    case 3: activity.setEndWeek(weekFormatter.parse(cell)); break;
                }
            }
        }
    }

    @When("the employee creates a project with name {string}")
    public void theEmployeeCreatesAProjectWithName(String name) {
        Application application = Application.getInstance();
        EmployeeInputContext input = (EmployeeInputContext) application.getContext();
        String[] projectArguments = new String[] {name, "false"};
        this.callCmd(input, input::cmdCreateProject, projectArguments);
    }

    /*
        Other
     */

    @When("the employee adds an activity with name {string} to the project")
    public void theEmployeeAddsAnActivityWithNameToTheProject(String name) {
        TestHolder testHolder = TestHolder.getInstance();
        ProjectManagerInputContext ic = new ProjectManagerInputContext();
        this.callCmd(ic, ic::cmdCreateActivity, new String[]{ testHolder.getProject().getID(), name });
    }

    @When("the actor assigns the employee with initials {string} as the project manager of the project")
    public void theActorAssignsTheEmployeeWithInitialsAsTheProjectManagerOfTheProject(String initials) {
        Application application = Application.getInstance();
        TestHolder testHolder = TestHolder.getInstance();
        InputContext input = application.getContext();
        this.callCmd(input, input::cmdAssignPM, new String[]{ testHolder.getProject().getID(), initials });
    }

    @And("the project does not have a project manager")
    public void theProjectDoesNotHaveAProjectManager() {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.getProject();
        project.setPM(null);
    }

    @When("the employee finishes the activity with ID {string} in the project")
    public void theEmployeeFinishesTheActivityWithIDInTheProject(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        ProjectManagerInputContext ic = new ProjectManagerInputContext();
        this.callCmd(ic, ic::cmdFinishActivity, new String[]{ testHolder.getProject().getID(), id });
    }

    @Given("the activity with ID {string} has an estimated duration of {string} weeks and registered {string} hours spent")
    public void theActivityWithIDHasAnEstimatedDurationOfWeeksAndRegisteredHoursSpent(String id, String weeks, String hours) {
        Application application = Application.getInstance();
        TestHolder testHolder = TestHolder.getInstance();

        Project project = testHolder.getProject();
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
        EmployeeActivityIntermediate employeeActivityIntermediate = trackedTime.get(application.getSignedInEmployee().getID());

        employeeActivityIntermediate.addMinutes(today, Integer.parseInt(hours) * 60);
    }


    /*
        Assert methods
     */

    @Then("the activity with ID {string} is marked as finished in the project")
    public void theActivityWithIDIsMarkedAsFinishedInTheProject(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.getProject();
        Activity activity = project.getActivity(Integer.parseInt(id));
        assertTrue(activity.isDone());
    }

    @Then("there is a project with ID {string} and name {string}")
    public void thereIsAProjectWithIDAndName(String id, String name) {
        Application application = Application.getInstance();
        Project project = application.getProject(id);
        assertEquals(project.getName(), name);
    }

    @Then("the project contains an activity with ID {string}")
    public void theProjectContainsAnActivityWithID(String id) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.getProject();
        Activity activity = project.getActivity(Integer.parseInt(id));
        assertNotNull(activity);
    }

    @Then("the project has a project manager with initials {string}")
    public void theProjectHasAProjectManagerWithInitials(String initials) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.getProject();
        assertNotNull(project.getPM());
        assertEquals(project.getPM().getID(), initials);
    }

    @Then("these activities with overall durations are found")
    public void theseActivitiesWithOverallDurationsAreFound(List<List<String>> durations) {
        Application application = Application.getInstance();

        for (List duration : durations) {
            Project project = application.getProject((String) duration.get(0));
        }

        // TODO
    }
}
