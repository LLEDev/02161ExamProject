package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Context.ProjectManagerInputContext;
import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.Util.DateFormatter;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.*;

public class EmployeeSteps extends BaseSteps {

    /*
        Given (or and) methods
     */

    @Given("there is an employee")
    public void thereIsAnEmployee() {
        this.thereIsAnEmployeeWithInitials("AA");
    }

    @Given("the employee is the project manager for the project")
    public void theEmployeeIsTheProjectManagerForTheProject() {
        this.theEmployeeWithInitialsIsTheProjectManagerOfTheProject(Application.getInstance().getSignedInEmployee().getID());
    }

    @Given("the employee is assigned to the activity with ID {string}")
    public void theEmployeeIsAssignedToTheActivityWithID(String id) {
        this.theEmployeeWithInitialsIsAssignedToTheActivityWithID(Application.getInstance().getSignedInEmployee().getID(), id);
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
        Application application = Application.getInstance();
        TestHolder testHolder = TestHolder.getInstance();

        Project project = testHolder.getProject();
        Employee employee = application.getEmployee(employeeID);
        EmployeeInputContext input = (EmployeeInputContext) application.getContext();

        String[] args = {project.getID(),employeeID};
        this.callCmd(input, input::cmdAssignPM, args);
        Assert.assertEquals(project.getPM(), employee);
    }

    @And("the employee with initials {string} is the actor")
    public void theEmployeeWithInitialsIsTheActor(String employeeID) {
        Application application = Application.getInstance();

        application.setSignedInEmployee(employeeID);
        Assert.assertEquals(application.getSignedInEmployee().getID(), employeeID);
    }

    @And("the employee with initials {string} is assigned to the the activity with ID {string}")
    public void theEmployeeWithInitialsIsAssignedToTheTheActivityWithID(String employeeID, String activityID) {
        Application application = Application.getInstance();
        TestHolder testHolder = TestHolder.getInstance();

        Employee employee = application.getEmployee(employeeID);
        Project project = testHolder.getProject();
        Activity activity = project.getActivity(Integer.parseInt(activityID));

        new EmployeeActivityIntermediate(employee, activity);
    }

    @And("the employee is attached to all activities in the projects")
    public void theEmployeeIsAttachedToAllActivitiesInTheProjects(List<String> projects) throws Exception {
        Application application = Application.getInstance();
        Employee signedInEmployee = application.getSignedInEmployee();

        for(String projectID : projects) {
            Project project = application.getProject(projectID);

            if(project == null) {
                throw new Exception("Project with ID " + projectID + " does not exist.");
            }

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
        TestHolder testHolder = TestHolder.getInstance();

        Project project = testHolder.getProject();
        Activity activity = project.getActivity(Integer.parseInt(activityID));

        Employee employee = application.getEmployee(employeeID);

        new EmployeeActivityIntermediate(employee, activity);
    }

    @And("the employee has the following work minutes")
    public void theEmployeeHasTheFollowingWorkMinutes(List<List<String>> workMinutes) throws Exception {
        Application application = Application.getInstance();
        Employee employee = application.getSignedInEmployee();

        for(List<String> entry : workMinutes) {
            if(entry.size() != 4) {
                throw new Exception("WorkMinutes entry has the wrong dimensions");
            }

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
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.getProject();

        String[] args = {employeeID, project.getID(), activityID};
        ProjectManagerInputContext ic = new ProjectManagerInputContext();
        this.callCmd(ic, ic::cmdAssignEmployeeToActivity, args);
    }

    @When("the employee requests assistance from {string} on activity with ID {string} in the project")
    public void theEmployeeRequestsAssistanceFromOnActivityWithIDInTheProject(String otherEmployeeID, String activityID) {
        Application application = Application.getInstance();
        EmployeeInputContext context = (EmployeeInputContext) application.getContext();
        Project project = TestHolder.getInstance().getProject();

        this.callCmd(context, context::cmdRequestAssistance, new String[] {project.getID(), activityID, otherEmployeeID});
    }

    @When("the employee requests a view of the project")
    public void theEmployeeRequestsAViewOfTheProject() {
        String projectID = TestHolder.getInstance().getProject().getID();
        ProjectManagerInputContext ic = new ProjectManagerInputContext();
        this.callCmd(ic, ic::cmdViewProject, new String[]{projectID});
    }

    @When("the employee requests a view of activity {string}")
    public void theEmployeeRequestsAViewOfActivity(String activityID) {
        String projectID = TestHolder.getInstance().getProject().getID();
        ProjectManagerInputContext ic = new ProjectManagerInputContext();
        this.callCmd(ic, ic::cmdViewActivity, new String[]{projectID, activityID});
    }

    @When("the employee requests a view of available employees at the date {string}")
    public void theEmployeeRequestsAViewOfAvailableEmployeesAtTheDate(String date) {
        ProjectManagerInputContext ic = new ProjectManagerInputContext();
        this.callCmd(ic, ic::cmdViewAvailability, new String[]{date});
    }

    @When("the employee requests a view of the schedule of the employee with ID {string}")
    public void theEmployeeRequestsAViewOfTheScheduleOfTheEmployeeWithID(String employee) {
        ProjectManagerInputContext ic = new ProjectManagerInputContext();
        this.callCmd(ic, ic::cmdViewSchedule, new String[]{employee});
    }

    @When("the employee submits the work minutes")
    public void theEmployeeSubmitsTheWorkMinutes(List<List<String>> minutes) {
        Application application = Application.getInstance();
        EmployeeInputContext input = (EmployeeInputContext) application.getContext();

        for (List<String> submission : minutes) {
            String[] args = {submission.get(0), submission.get(1), DateFormatter.formatDate(new Date()), submission.get(2)};
            this.callCmd(input, input::cmdSubmitHours, args);
        }
    }

    @When("the employee requests the following OOO activities")
    public void theEmployeeRequestsTheFollowingOOOActivities(List<List<String>> activities) throws Exception {
        Application application = Application.getInstance();
        EmployeeInputContext input = (EmployeeInputContext) application.getContext();

        for(List<String> activity : activities) {
            if(activity.size() != 3) {
                throw new Exception("Invalid OOO activity entry given.");
            }

            String[] args = {activity.get(0), activity.get(1), activity.get(2)};
            this.callCmd(input, input::cmdRequestOutOfOffice, args);
        }
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
        Assert.assertTrue(employee.getNumOpenActivities() > 0);
    }
}
