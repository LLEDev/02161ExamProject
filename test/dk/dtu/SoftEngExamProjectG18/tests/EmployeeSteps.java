package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Input.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.Business.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.General.DateFormatter;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.text.ParseException;
import java.util.*;

public class EmployeeSteps extends StepsBase {

    /*
        Given (or and) methods
     */

    @Given("there is an employee")
    public void thereIsAnEmployee() {
        this.thereIsAnEmployeeWithInitials("AA");
    }

    @Given("the employee is the project manager for the project")
    public void theEmployeeIsTheProjectManagerForTheProject() {
        this.theEmployeeWithInitialsIsTheProjectManagerOfTheProject(
            Application.getInstance().getSignedInEmployee().getID()
        );
    }

    @Given("the employee is assigned to the activity with ID {string}")
    public void theEmployeeIsAssignedToTheActivityWithID(String id) {
        this.theEmployeeWithInitialsIsAssignedToTheActivityWithID(
            Application.getInstance().getSignedInEmployee().getID(),
            id
        );
    }

    @And("there is an employee with initials {string}")
    public void thereIsAnEmployeeWithInitials(String employeeID) {
        Application application = Application.getInstance();

        application.createEmployee(employeeID, employeeID);
        application.setSignedInEmployee(employeeID);
        application.setContext(new EmployeeInputContext());
    }

    @And("the following employees are given")
    public void theFollowingEmployeesAreGiven(List<String> employees) {
        for (String employeeID: employees) {
            this.thereIsAnEmployeeWithInitials(employeeID);
        }
    }

    @And("the employee with initials {string} is the project manager of the project")
    public void theEmployeeWithInitialsIsTheProjectManagerOfTheProject(String employeeID) {
        String projectID = TestHolder.getInstance().getProject().getID();
        this.wrap(() -> Application.getInstance().assignPM(projectID, employeeID));
    }

    @And("the employee with initials {string} is the actor")
    public void theEmployeeWithInitialsIsTheActor(String employeeID) {
        Application application = Application.getInstance();
        application.setSignedInEmployee(employeeID);
    }

    // TODO: Refactor
    @And("the employee is attached to all activities in the projects")
    public void theEmployeeIsAttachedToAllActivitiesInTheProjects(List<String> projects) {
        Application application = Application.getInstance();
        Employee signedInEmployee = application.getSignedInEmployee();

        for(String projectID : projects) {
            Project project = application.getProject(projectID);
            Assert.assertNotNull(project);

            HashMap<Integer, EmployeeActivityIntermediate> employeeProjectActivities = signedInEmployee.getActivities().get(project.getID());

            for(Activity activity : project.getActivities().values()) {
                if(employeeProjectActivities == null || !employeeProjectActivities.containsKey(activity.getID())) {
                    new EmployeeActivityIntermediate(signedInEmployee, activity);
                }

                Assert.assertTrue(signedInEmployee.getActivities().get(project.getID()).containsKey(activity.getID()));
            }
        }
    }

    @And("the employee with initials {string} is assigned to the activity with ID {string}")
    public void theEmployeeWithInitialsIsAssignedToTheActivityWithID(String employeeID, String activityID) {
        Application application = Application.getInstance();

        Project project = TestHolder.getInstance().getProject();
        Activity activity = project.getActivity(Integer.parseInt(activityID));
        Employee employee = application.getEmployee(employeeID);

        this.wrap(() -> EmployeeActivityIntermediate.initAssociation(employee, activity));
    }

    // TODO: Refactor
    @And("the employee has the following work minutes")
    public void theEmployeeHasTheFollowingWorkMinutes(List<List<String>> workMinutes) throws ParseException {
        Application application = Application.getInstance();
        Employee employee = application.getSignedInEmployee();

        for(List<String> entry : workMinutes) {
            Assert.assertEquals(entry.size(), 4);

            Project project = application.getProject(entry.get(0));
            Activity activity = project.getActivity(Integer.parseInt(entry.get(1)));
            Date date = DateFormatter.parseDate(entry.get(2));
            int minutes = Integer.parseInt(entry.get(3));

            Assert.assertNotNull(project);
            Assert.assertNotNull(activity);
            Assert.assertNotNull(date);

            EmployeeActivityIntermediate eai = new EmployeeActivityIntermediate(employee, activity);
            eai.setMinutes(date, minutes);
        }
    }

    /*
        When (or and) methods (actions)
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

    /*
        Then (or and) methods (assertions)
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

    @And("the employee with initials {string} has not reached the activity cap")
    public void theEmployeeWithInitialsHasNotReachedTheActivityCap(String arg0) {
        Application application = Application.getInstance();
        Employee employee = application.getEmployee(arg0);
        employee.assertOpenActivities();
    }
}
