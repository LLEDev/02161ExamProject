package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.General.Dates;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProjectSteps extends StepsBase {

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Given("that there is a project with name {string}")
    public void thatThereIsAProjectWithName(String name) {
        TestHolder.getInstance().setProject(
            Application.getInstance().createProject(name, true)
        );
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Given("there are projects with names")
    public void thereAreProjectsWithNames(List<String> projects) {
        Application application = Application.getInstance();

        for (String name : projects) {
            application.createProject(name, true);
        }
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Given("the employee is the project manager for the project")
    public void theEmployeeIsTheProjectManagerForTheProject() {
        this.theEmployeeWithInitialsIsTheProjectManagerOfTheProject(
            Application.getInstance().getSignedInEmployee().getID()
        );
    }

    /**
     * @author Thor Dueholm (s194589)
     */
    @And("the project has the following activities")
    public void theProjectHasTheFollowingActivities(List<List<String>> activities) throws ParseException, AccessDeniedException {
        TestHolder th = TestHolder.getInstance();
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-ww");

        Project project = th.getProject();
        Assert.assertNotNull(project);

        // Remove auto-created activities
        project.clearActivities();

        // Create all given activities
        for (List<String> activityString : activities) {
            if (activityString.size() > 0) {

                Activity activity = new Activity(activityString.get(0), project);
                for (int i = 1; i < activityString.size(); i++) {
                    String cell = activityString.get(i);

                    switch (i) {
                        case 1:
                            activity.setEstimatedHours(Integer.parseInt(cell));
                            break;
                        case 2:
                            activity.setStartWeek(weekFormatter.parse(cell));
                            break;
                        case 3:
                            activity.setEndWeek(weekFormatter.parse(cell));
                            break;
                    }
                }
            }
        }
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @When("the employee creates a project with name {string}")
    public void theEmployeeCreatesAProjectWithName(String name) {
        this.wrap(() -> Application.getInstance().createProject(name, true));
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @When("the employee adds an activity with name {string} to the project")
    public void theEmployeeAddsAnActivityWithNameToTheProject(String name) {
        String projectID = TestHolder.getInstance().getProject().getID();
        this.wrap(() -> Application.getInstance().createActivity(projectID, name));
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @When("the actor assigns the employee with initials {string} as the project manager of the project")
    public void theActorAssignsTheEmployeeWithInitialsAsTheProjectManagerOfTheProject(String initials) {
        String projectID = TestHolder.getInstance().getProject().getID();
        this.wrap(() -> Application.getInstance().assignPM(projectID, initials));
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @And("the employee with initials {string} is the project manager of the project")
    public void theEmployeeWithInitialsIsTheProjectManagerOfTheProject(String employeeID) {
        String projectID = TestHolder.getInstance().getProject().getID();
        this.wrap(() -> Application.getInstance().assignPM(projectID, employeeID));
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @And("the project does not have a project manager")
    public void theProjectDoesNotHaveAProjectManager() {
        TestHolder.getInstance().getProject().setPM(null);
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @When("the employee finishes the activity with ID {string} in the project")
    public void theEmployeeFinishesTheActivityWithIDInTheProject(String activityID) {
        String projectID = TestHolder.getInstance().getProject().getID();
        this.wrap(() -> Application.getInstance().finishActivity(projectID, Integer.parseInt(activityID)));
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Given("the activity with ID {string} has an estimated duration of {string} weeks and registered {string} hours spent on date {string}")
    public void theActivityWithIDHasAnEstimatedDurationOfWeeksAndRegisteredHoursSpentOnDate(String id, String weeks, String hours, String date) throws ParseException, AccessDeniedException {
        Application application = Application.getInstance();
        TestHolder testHolder = TestHolder.getInstance();

        Project project = testHolder.getProject();
        Activity activity = project.getActivity(Integer.parseInt(id));

        // Create dates
        Date today = Dates.parseDate(date), newDate = new Date(today.getTime());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, Integer.parseInt(weeks) * 7);
        newDate.setTime(calendar.getTime().getTime());

        // Set start/end weeks
        activity.setStartWeek(today);
        activity.setEndWeek(newDate);

        application.submitHours(project.getID(), activity.getID(), today, Integer.parseInt(hours));
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Then("the activity with ID {string} is marked as finished in the project")
    public void theActivityWithIDIsMarkedAsFinishedInTheProject(String id) {
        Project project = TestHolder.getInstance().getProject();
        assertTrue(project.getActivity(Integer.parseInt(id)).isDone());
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Then("there is a project with ID {string} and name {string}")
    public void thereIsAProjectWithIDAndName(String id, String name) {
        assertEquals(Application.getInstance().getProject(id).getName(), name);
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Then("the project contains an activity with ID {string}")
    public void theProjectContainsAnActivityWithID(String id) {
        Project project = TestHolder.getInstance().getProject();
        assertNotNull(project.getActivity(Integer.parseInt(id)));
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Then("the project has a project manager with initials {string}")
    public void theProjectHasAProjectManagerWithInitials(String initials) {
        Project project = TestHolder.getInstance().getProject();
        assertNotNull(project.getPM());
        assertEquals(project.getPM().getID(), initials);
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Then("these activities with overall durations are found")
    public void theseActivitiesWithOverallDurationsAreFound(List<List<String>> durations) {
        Application application = Application.getInstance();

        for (List<String> duration : durations) {
            Project project = application.getProject(duration.get(0));
            Activity activity = project.getActivity(Integer.parseInt(duration.get(1)));
            double hours = activity.getTotalTrackedMinutes() / 60.0;
            Assert.assertEquals(Double.parseDouble(duration.get(2)), hours, 1e-15);
        }
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @Then("the activity with ID {string} has an estimated duration of {string} hours")
    public void theActivityWithIDHasAnEstimatedDurationOfHours(String activityID, String hours) {
        TestHolder th = TestHolder.getInstance();

        Project project = th.getProject();
        Assert.assertNotNull(project);

        Activity activity = project.getActivity(Integer.parseInt(activityID));

        int estimatedHours = activity.getEstimatedHours();
        Assert.assertEquals(estimatedHours, Integer.parseInt(hours));

    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @Then("the activity with ID {string} has the start date {string} and the end date {string}")
    public void theActivityWithIDHasTheStartDateAndTheEndDate(String activityID, String start, String end) throws ParseException {
        TestHolder th = TestHolder.getInstance();

        Project project = th.getProject();
        Assert.assertNotNull(project);

        Activity activity = project.getActivity(Integer.parseInt(activityID));

        Assert.assertEquals(activity.getStartWeek(), Dates.parseDate(start));
        Assert.assertEquals(activity.getEndWeek(), Dates.parseDate(end));

    }
}
