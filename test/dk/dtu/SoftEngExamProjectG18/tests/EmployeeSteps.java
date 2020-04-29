package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Context.ProjectManagerInputContext;
import dk.dtu.SoftEngExamProjectG18.Business.Activity;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.Persistence.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
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
    public void theEmployeeIsTheProjectManagerForTheProject() throws Exception {
        this.theEmployeeWithInitialsIsTheProjectManagerOfTheProject(this.db.getSignedInEmployee().getID());
    }

    @Given("the employee is assigned to the activity with ID {string}")
    public void theEmployeeIsAssignedToTheActivityWithID(String id) {
        this.theEmployeeWithInitialsIsAssignedToTheActivityWithID(this.db.getSignedInEmployee().getID(), id);
    }

    @And("there is an employee with initials {string}")
    public void thereIsAnEmployeeWithInitials(String employeeID) {
        this.db.getEmployees().put(employeeID, new Employee(employeeID, employeeID));
        this.db.setSignedInEmployee(employeeID);
        this.application.setInputContext(new EmployeeInputContext());
    }

    @And("the following employees are given")
    public void theFollowingEmployeesAreGiven(List<String> employees) {
        for (String employeeID: employees) {
            this.thereIsAnEmployeeWithInitials(employeeID);
        }
    }

    @And("the employee with initials {string} is the project manager of the project")
    public void theEmployeeWithInitialsIsTheProjectManagerOfTheProject(String employeeID) throws Exception {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Employee employee = this.db.getEmployee(employeeID);
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();

        String[] args = {project.getID(),employeeID};
        this.callCmd(input, "cmdAssignPM", args);
        Assert.assertEquals(project.getPM(), employee);
    }

    @And("the employee with initials {string} is the actor")
    public void theEmployeeWithInitialsIsTheActor(String employeeID) {
        this.db.setSignedInEmployee(employeeID);
        Assert.assertEquals(this.db.getSignedInEmployee().getID(), employeeID);
    }

    @And("the employee with initials {string} is assigned to the the activity with ID {string}")
    public void theEmployeeWithInitialsIsAssignedToTheTheActivityWithID(String arg0, String arg1) {
        TestHolder testHolder = TestHolder.getInstance();
        Employee employee = this.db.getEmployee(arg0);
        Project project = testHolder.project;
        Activity activity = project.getActivity(1);

        EmployeeActivityIntermediate intermediate = new EmployeeActivityIntermediate(employee, activity);

        HashMap<Integer, EmployeeActivityIntermediate> args = new HashMap<>();
        args.put(activity.getID(),intermediate);
        employee.getActivities().put(project.getID(),args);
        this.db.getEmployees().put(arg0,employee);
    }

    @And("the employee is attached to all activities in the projects")
    public void theEmployeeIsAttachedToAllActivitiesInTheProjects(List<String> projects) throws Exception {
        Employee signedInEmployee = this.db.getSignedInEmployee();

        for(String projectID : projects) {
            Project project = this.db.getProject(projectID);

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
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = project.getActivity(Integer.parseInt(activityID));

        Employee employee = this.db.getEmployee(employeeID);
        EmployeeActivityIntermediate intermediate = new EmployeeActivityIntermediate(employee, activity);

        HashMap<Integer, EmployeeActivityIntermediate> args = new HashMap<>();
        args.put(activity.getID(), intermediate);
        employee.getActivities().put(project.getID(), args);
    }

    @And("the employee has the following work minutes")
    public void theEmployeeHasTheFollowingWorkMinutes(List<List<String>> workMinutes) throws Exception {
        Employee employee = this.db.getSignedInEmployee();

        for(List<String> entry : workMinutes) {
            if(entry.size() != 4) {
                throw new Exception("WorkMinutes entry has the wrong dimensions");
            }

            Project project = this.db.getProject(entry.get(0));
            Activity activity = project.getActivity(Integer.parseInt(entry.get(1)));
            Date date = this.formatter.parse(entry.get(2));
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
        Project project = testHolder.project;

        String[] args = {employeeID, project.getID(), activityID};
        this.callCmd(new ProjectManagerInputContext(), "cmdAssignEmployeeToActivity", args);
    }

    @When("the employee requests assistance from {string} on activity with ID {string} in the project")
    public void theEmployeeRequestsAssistanceFromOnActivityWithIDInTheProject(String otherEmployeeID, String activityID) {
        EmployeeInputContext context = (EmployeeInputContext) CompanyDB.getContext();
        Project project = TestHolder.getInstance().project;

        this.callCmd(context, "cmdRequestAssistance", new String[] {project.getID(), activityID, otherEmployeeID});
    }

    @When("the employee requests a view of the project")
    public void theEmployeeRequestsAViewOfTheProject() {
        String projectID = TestHolder.getInstance().project.getID();
        this.callCmd(new ProjectManagerInputContext(), "cmdViewProject", new String[]{projectID});
    }

    @When("the employee requests a view of activity {string}")
    public void theEmployeeRequestsAViewOfActivity(String activityID) {
        String projectID = TestHolder.getInstance().project.getID();
        this.callCmd(new ProjectManagerInputContext(), "cmdViewActivity", new String[]{projectID, activityID});
    }

    @When("the employee requests a view of available employees at the date {string}")
    public void theEmployeeRequestsAViewOfAvailableEmployeesAtTheDate(String date) {
        this.callCmd(new ProjectManagerInputContext(), "cmdViewAvailability", new String[]{date});
    }

    @When("the employee requests a view of the schedule of the employee with ID {string}")
    public void theEmployeeRequestsAViewOfTheScheduleOfTheEmployeeWithID(String employee) {
        this.callCmd(new ProjectManagerInputContext(), "cmdViewSchedule", new String[]{employee});
    }

    @When("the employee submits the work minutes")
    public void theEmployeeSubmitsTheWorkMinutes(List<List<String>> minutes) throws Exception {
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();

        for (List<String> submission : minutes) {
            String[] args = {submission.get(0), submission.get(1), this.formatter.format(new Date()), submission.get(2)};
            this.callCmd(input, "cmdSubmitHours", args);
        }
    }

    @When("the employee requests the following OOO activities")
    public void theEmployeeRequestsTheFollowingOOOActivities(List<List<String>> activities) throws Exception {
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();

        for(List<String> activity : activities) {
            if(activity.size() != 3) {
                throw new Exception("Invalid OOO activity entry given.");
            }

            String[] args = {activity.get(0), activity.get(1), activity.get(2)};
            this.callCmd(input, "cmdRequestOutOfOffice", args);
        }
    }

    /*
        Then (or and) methods (assertions)
     */

    @Then("the employee with initials {string} has been assigned to the activity with ID {string}")
    public void theEmployeeWithInitialsHasBeenAssignedToTheActivityWithID(String employeeID, String activityIDString) {
        Employee otherEmployee = this.db.getEmployee(employeeID);
        Project project = TestHolder.getInstance().project;

        HashMap<Integer, EmployeeActivityIntermediate> otherEmployeeIntermediates =
                otherEmployee.getActivities().get(project.getID());
        Assert.assertNotNull(otherEmployeeIntermediates);
        Assert.assertTrue(otherEmployeeIntermediates.containsKey(Integer.parseInt(activityIDString)));
    }

    @And("the employee with initials {string} has not reached the activity cap")
    public void theEmployeeWithInitialsHasNotReachedTheActivityCap(String arg0) {
        Employee employee = this.db.getEmployee(arg0);
        Assert.assertTrue(employee.getNumOpenActivities() > 0);
    }
}
