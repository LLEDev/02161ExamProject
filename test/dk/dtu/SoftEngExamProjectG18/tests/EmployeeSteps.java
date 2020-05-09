package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.*;
import dk.dtu.SoftEngExamProjectG18.Business.Enums.OOOActivityType;
import dk.dtu.SoftEngExamProjectG18.Business.Exceptions.AccessDeniedException;
import dk.dtu.SoftEngExamProjectG18.Controller.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.General.Dates;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeSteps extends StepsBase {

    /**
     * @author Thor Dueholm (s194589)
     */
    @Given("there is an employee")
    public void thereIsAnEmployee() {
        this.thereIsAnEmployeeWithInitials("AA");
    }

    /**
     * @author Thor Dueholm (s194589)
     */
    @Given("the employee is assigned to the activity with ID {string}")
    public void theEmployeeIsAssignedToTheActivityWithID(String id) {
        this.theEmployeeWithInitialsIsAssignedToTheActivityWithID(
            Application.getInstance().getSignedInEmployee().getID(),
            id
        );
    }

    /**
     * @author Thor Dueholm (s194589)
     */
    @And("there is an employee with initials {string}")
    public void thereIsAnEmployeeWithInitials(String employeeID) {
        Application application = Application.getInstance();

        application.createEmployee(employeeID, employeeID);
        application.setSignedInEmployee(employeeID);
        application.setContext(new EmployeeInputContext());
    }

    /**
     * @author Thor Dueholm (s194589)
     */
    @And("the following employees are given")
    public void theFollowingEmployeesAreGiven(List<String> employees) {
        for (String employeeID : employees) {
            this.thereIsAnEmployeeWithInitials(employeeID);
        }
    }

    /**
     * @author Johannes s. Busse (s194604)
     */
    @And("the employee with initials {string} is the actor")
    public void theEmployeeWithInitialsIsTheActor(String employeeID) {
        Application application = Application.getInstance();
        application.setSignedInEmployee(employeeID);
    }

    /**
     * @author Johannes S. Busse (s194604)
     */
    @And("the employee is attached to all activities in the projects")
    public void theEmployeeIsAttachedToAllActivitiesInTheProjects(List<String> projects) throws AccessDeniedException {
        Application application = Application.getInstance();
        Employee signedInEmployee = application.getSignedInEmployee();

        for (String projectID : projects) {
            Project project = application.getProject(projectID);
            Assert.assertNotNull(project);

            // Get employees' activity associations related to project
            HashMap<Integer, EmployeeActivityIntermediate> ePA = signedInEmployee.getActivities().get(project.getID());

            for (Activity activity : project.getActivities().values()) {
                if (ePA == null || !ePA.containsKey(activity.getID())) {
                    EmployeeActivityIntermediate.initAssociation(signedInEmployee, activity);
                }

                // Make sure employee now have association to activity
                Assert.assertTrue(signedInEmployee.getActivities().get(project.getID()).containsKey(activity.getID()));
            }
        }
    }

    /**
     * @author Johannes S. Busse (s194604)
     */
    @And("the employee with initials {string} is assigned to the activity with ID {string}")
    public void theEmployeeWithInitialsIsAssignedToTheActivityWithID(String employeeID, String activityID) {
        Application application = Application.getInstance();

        Project project = TestHolder.getInstance().getProject();
        Activity activity = project.getActivity(Integer.parseInt(activityID));
        Employee employee = application.getEmployee(employeeID);

        this.wrap(() -> EmployeeActivityIntermediate.initAssociation(employee, activity));
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @And("the employee has the following work minutes")
    public void theEmployeeHasTheFollowingWorkMinutes(List<List<String>> workMinutes) throws ParseException, AccessDeniedException {
        Application application = Application.getInstance();
        Employee employee = application.getSignedInEmployee();

        for (List<String> entry : workMinutes) {
            Assert.assertEquals(entry.size(), 4);

            Project project = application.getProject(entry.get(0));
            Activity activity = project.getActivity(Integer.parseInt(entry.get(1)));
            Date date = Dates.parseDate(entry.get(2));
            int minutes = Integer.parseInt(entry.get(3));

            Assert.assertNotNull(project);
            Assert.assertNotNull(activity);
            Assert.assertNotNull(date);

            EmployeeActivityIntermediate eai = EmployeeActivityIntermediate.initAssociation(employee, activity);
            eai.setMinutes(date, minutes);
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @And("the employee has the following work minutes today")
    public void theEmployeeHasTheFollowingWorkMinutesToday(List<List<String>> workMinutes) throws ParseException, AccessDeniedException {
        final String today = Dates.formatDate(new Date());
        List<List<String>> newWorkMinutes = workMinutes.stream().peek(l -> l.add(2, today)).collect(Collectors.toList());
        this.theEmployeeHasTheFollowingWorkMinutes(newWorkMinutes);
    }

    /**
     * @author Johannes S. Busse (s194604)
     */
    @When("the actor adds the employee with initials {string} to the activity with ID {string}")
    public void theActorAddsTheEmployeeWithInitialsToTheActivityWithID(String employeeID, String activityID) {
        Project project = TestHolder.getInstance().getProject();

        this.wrap(() ->
            Application.getInstance().assignEmployeeToActivity(
                employeeID,
                project.getID(),
                Integer.parseInt(activityID)
            )
        );
    }

    /**
     * @author Johannes S. Busse (s194604)
     */
    @When("the employee requests assistance from {string} on activity with ID {string} in the project")
    public void theEmployeeRequestsAssistanceFromOnActivityWithIDInTheProject(String otherEmployeeID, String activityID) {
        Project project = TestHolder.getInstance().getProject();

        this.wrap(() ->
            Application.getInstance().requestAssistance(
                project.getID(),
                Integer.parseInt(activityID),
                otherEmployeeID
            )
        );
    }

    /**
     * @author Johannes S. Busse (s194604)
     */
    @When("the employee submits the work minutes")
    public void theEmployeeSubmitsTheWorkMinutes(List<List<String>> minutes) throws AccessDeniedException, IllegalArgumentException {
        Application application = Application.getInstance();

        for (List<String> submission : minutes) {
            if (submission.size() != 3) {
                throw new IllegalArgumentException("Invalid submission entry given.");
            }

            double hours = Double.parseDouble(submission.get(2)) / 60;

            this.wrap(() -> application.submitHours(
                submission.get(0),
                Integer.parseInt(submission.get(1)),
                new Date(),
                hours
            ));
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @When("the employee requests the following OOO activities")
    public void theEmployeeRequestsTheFollowingOOOActivities(List<List<String>> activities) throws Exception {
        Application application = Application.getInstance();

        for (List<String> activity : activities) {
            if (activity.size() != 3) {
                throw new Exception("Invalid OOO activity entry given.");
            }

            this.wrap(() -> application.requestOutOfOffice(
                OOOActivityType.valueOf(activity.get(0)),
                Dates.parseDate(activity.get(1)),
                Dates.parseDate(activity.get(2))
            ));
        }
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @When("the employee switches to context {string}")
    public void theEmployeeSwitchesToContext(String context) {
        this.wrap(() -> Application.getInstance().switchContext(context));
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @Then("the employee with initials {string} has been assigned to the activity with ID {string}")
    public void theEmployeeWithInitialsHasBeenAssignedToTheActivityWithID(String employeeID, String activityIDString) {
        Application application = Application.getInstance();
        Employee otherEmployee = application.getEmployee(employeeID);
        Project project = TestHolder.getInstance().getProject();

        HashMap<Integer, EmployeeActivityIntermediate> otherEmployeeIntermediates =
            otherEmployee.getActivities().get(project.getID());
        Assert.assertNotNull(otherEmployeeIntermediates);
        Assert.assertTrue(otherEmployeeIntermediates.containsKey(Integer.parseInt(activityIDString)));
    }

    /**
     * @author Johannes S. Busse (s194604)
     */
    @And("the employee with initials {string} has not reached the activity cap")
    public void theEmployeeWithInitialsHasNotReachedTheActivityCap(String arg0) {
        Application application = Application.getInstance();
        Employee employee = application.getEmployee(arg0);
        employee.assertOpenActivities();
    }

    /**
     * @author Mikkel Theiss Westermann (s194601)
     */
    @And("the employee with initials {string} has reached the activity cap")
    public void theEmployeeWithInitialsHasReachedTheActivityCap(String arg0) {
        Application application = Application.getInstance();
        Employee employee = application.getEmployee(arg0);

        employee.setWeeklyActivityCap(0);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @Then("the current context is {string}")
    public void theCurrentContextIs(String context) {
        Assert.assertEquals(Application.getInstance().getContext().getClass().getSimpleName(), context);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @When("the employee sets the work hours {string} in the activity with ID {string}.")
    public void theEmployeeSetsTheWorkHoursInTheActivityWithID(String workHours, String activityID) {
        TestHolder th = TestHolder.getInstance();

        Project project = th.getProject();
        Assert.assertNotNull(project);

        Application application = Application.getInstance();

        this.wrap(() -> application.setHours(
            project.getID(),
            Integer.parseInt(activityID),
            new Date(),
            Integer.parseInt(workHours)
        ));
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @When("the employee estimates the duration to {string} hours for the activity with ID {string}")
    public void theEmployeeEstimatesTheDurationToHoursForTheActivityWithID(String hours, String activityID) {
        TestHolder th = TestHolder.getInstance();

        Project project = th.getProject();
        Assert.assertNotNull(project);

        Application application = Application.getInstance();

        this.wrap(() -> application.estimateActivityDuration(
            project.getID(),
            Integer.parseInt(activityID),
            Integer.parseInt(hours)
        ));

    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    @When("the employee sets the start date {string} and the end date {string} of the activity with ID {string}")
    public void theEmployeeSetsTheStartDateAndTheEndDateOfTheActivityWithID(String start, String end, String activityID) throws ParseException {
        TestHolder th = TestHolder.getInstance();

        Project project = th.getProject();
        Assert.assertNotNull(project);

        Application application = Application.getInstance();

        this.wrap(() -> application.setActivityInterval(
            project.getID(),
            Integer.parseInt(activityID),
            Dates.parseDate(start),
            Dates.parseDate(end)
        ));

    }
}
