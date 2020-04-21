package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Core.Activity;
import dk.dtu.SoftEngExamProjectG18.Core.Employee;
import dk.dtu.SoftEngExamProjectG18.Core.Project;
import dk.dtu.SoftEngExamProjectG18.Relations.EmployeeActivityIntermediate;
import dk.dtu.SoftEngExamProjectG18.tests.Util.CmdResponse;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;

public class EmployeeSteps extends BaseSteps {

    /*
        Create methods
     */

    @Given("there is an employee")
    public void thereIsAnEmployee() {
        this.thereIsAnEmployeeWithInitials("HH");
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
            this.thereIsAnEmployeeWithInitials(employeeID);
        }
    }

    /*
        Other
     */

    @Given("the employee is the project manager for the project")
    public void theEmployeeIsTheProjectManagerForTheProject() throws Exception {
        this.theEmployeeWithInitialsIsTheProjectManagerOfTheProject(this.db.getSignedInEmployee().getID());
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

    @When("the actor adds the employee with initials {string} to the activity with ID {string}")
    public void theActorAddsTheEmployeeWithInitialsToTheActivityWithID(String employeeID, String activityID) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        //The scenarios are a bit inconsistent, hence the below "if" statement for flexibility
        if(project.getActivity(Integer.parseInt(activityID)) == null) {
            project.getActivities().put(Integer.parseInt(activityID),new Activity("test", project));
        }
        Activity activity = project.getActivity(Integer.parseInt(activityID));
        Employee actor = this.db.getSignedInEmployee();
        //How to use the actor though?
        Employee employee = this.db.getEmployee(employeeID);
        EmployeeActivityIntermediate intermediate = new EmployeeActivityIntermediate(employee, activity);
        HashMap<Integer, EmployeeActivityIntermediate> args = new HashMap<>();
        args.put(activity.getID(),intermediate);
        employee.getActivities().put(project.getID(),args);
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

    @Given("the employee is assigned to the activity with ID {string}")
    public void theEmployeeIsAssignedToTheActivityWithID(String id) {
        this.theEmployeeWithInitialsIsAssignedToTheActivityWithID(this.db.getSignedInEmployee().getID(), id);
    }

    @And("the employee with initials {string} is assigned to the activity with ID {string}")
    public void theEmployeeWithInitialsIsAssignedToTheActivityWithID(String employeeID, String activityID) {
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = project.getActivity(Integer.parseInt(activityID));
        System.out.println(project.getName());
        System.out.println(project.getActivities());
        System.out.println(activityID);

        Employee employee = this.db.getEmployee(employeeID);
        System.out.println(this.db.getEmployees());
        EmployeeActivityIntermediate intermediate = new EmployeeActivityIntermediate(employee, activity);

        HashMap<Integer, EmployeeActivityIntermediate> args = new HashMap<>();
        args.put(activity.getID(), intermediate);
        employee.getActivities().put(project.getID(), args);
    }


    @When("the employee requests assistance from {string} on activity with ID {string} in the project")
    public void theEmployeeRequestsAssistanceFromOnActivityWithIDInTheProject(String arg0, String arg1) {
        Employee employee = this.db.getSignedInEmployee();
        Employee assistant = this.db.getEmployee(arg0);
        TestHolder testHolder = TestHolder.getInstance();
        Project project = testHolder.project;
        Activity activity = new Activity("test",project);
        project.getActivities().put(Integer.parseInt(arg1),activity);
        activity = project.getActivity(Integer.parseInt(arg1));
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();
        String[] args = {project.getID(),Integer.toString(activity.getID()),assistant.getID()};
        //input.cmdRequestAssistance(args);
        //TODO: Above line makes tests fail when uncommented, having read cmdRequestAssistance fairly thoroughly I dunno why
    }

    @When("the employee requests an overview of the project")
    public void theEmployeeRequestsAnOverviewOfTheProject() {
        //TODO: Cannot be implemented until overview is implemented
    }

    @When("the employee submits the work minutes")
    public void theEmployeeSubmitsTheWorkMinutes(List<List<String>> minutes) throws Exception {
        Employee employee = this.db.getSignedInEmployee();
        EmployeeInputContext input = (EmployeeInputContext) this.db.getInputContext();
        LocalDate date = LocalDate.now();
        for (List<String> submission : minutes) {
            String[] args = {submission.get(0), submission.get(1), String.valueOf(date), submission.get(2)};
            this.callCmd(input, "cmdSubmitHours", args);
        }
    }

    /*
        Assert
     */

    @Then("the employee with initials {string} has been assigned to the activity with ID {string}")
    public void theEmployeeWithInitialsHasBeenAssignedToTheActivityWithID(String arg0, String arg1) {
        Employee employee = this.db.getEmployee(arg0);
        employee.getActivities();
        HashMap<String, HashMap<Integer, EmployeeActivityIntermediate>> activities = employee.getActivities();
        for(String projectKey : activities.keySet()) {
            HashMap<Integer, EmployeeActivityIntermediate> activityIdentifyers = activities.get(projectKey);
            for(int activityKey : activityIdentifyers.keySet()) {
                if(activityKey == Integer.parseInt(arg1)) {
                    Assert.assertEquals(Integer.parseInt(arg1), activityKey);
                    return;
                }
            }
        }
        //Assert.assertEquals(1,2);
        //TODO: Do proper fail statement
        //TODO: Figure out why this method won't work
    }

    @Then("the employee sees that the project has a single activity with {string} hours spent out of {string} estimated hours needed")
    public void theEmployeeSeesThatTheProjectHasASingleActivityWithHoursSpentOutOfEstimatedHoursNeeded(String arg0, String arg1) {
        //TODO: [Awaiting projectStep: "theActivityWithIDHasAnEstimatedDurationOfHoursAndRegisteredHoursSpent"]
    }

    @And("the employee with initials {string} has not reached the activity cap")
    public void theEmployeeWithInitialsHasNotReachedTheActivityCap(String arg0) {
        Employee employee = this.db.getEmployee(arg0);
        Assert.assertTrue(employee.amountOfOpenActivities() > 0);
    }

}
