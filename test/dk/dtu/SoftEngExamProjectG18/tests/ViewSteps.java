package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.General.DateFormatter;
import dk.dtu.SoftEngExamProjectG18.Input.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Input.InputContext;
import dk.dtu.SoftEngExamProjectG18.Input.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.General.Interfaces.ThrowingFunction;
import dk.dtu.SoftEngExamProjectG18.Input.ProjectManagerInputContext;
import dk.dtu.SoftEngExamProjectG18.tests.Util.CmdResponse;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ViewSteps {

    protected void callCmd(InputContext context, ThrowingFunction<String[]> tf, String[] args) {
        AtomicReference<Exception> atomicException = new AtomicReference<>();
        CommandException commandException = null;
        String response;

        Consumer<Exception> exceptionHook = atomicException::set;
        context.addCommandExceptionHook(exceptionHook);

        try {
            tf.apply(args);
        } catch (CommandException ce) {
            commandException = ce;
        } catch (Exception e) {
            handleNonCommandException(e);
            return;
        }

        context.removeCommandExceptionHook(exceptionHook);

        if(commandException == null) {
            Exception exception = atomicException.get();
            if (exception != null && !(exception instanceof CommandException)) {
                handleNonCommandException(exception);
                return;
            }

            commandException = (CommandException) exception;
        }

        response = context.getOutput();
        context.resetOutput();

        TestHolder.getInstance().setResponse(new CmdResponse(response, commandException));
    }

    protected void handleNonCommandException(Exception e) {
        e.printStackTrace();
        Assert.fail("A non-expected exception was thrown.");
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

    @Then("the following table is presented")
    public void theFollowingTableIsPresented(List<List<String>> table) {
        Scanner s = new Scanner(TestHolder.getInstance().getResponse().getResponse());

        boolean tableFound = false;
        int tableIndex = -1;
        while(s.hasNextLine()) {
            String next = s.nextLine();

            if(next.matches("^[-]+$")) { // Check if line only contains hyphens - if yes, a table is found
                tableIndex = Math.max(0, tableIndex);
                continue;
            }

            if(tableIndex == -1 || !next.contains("|")) { // If no pipes are found, we're not reading a table anymore
                tableIndex = -1;
                continue;
            }

            List<String> tableRow = table.get(tableIndex);
            List<String> cells = Arrays.stream(next.split("\\|"))
                .map(String::trim)
                .filter(cell -> cell.length() > 0)
                .collect(Collectors.toList());

            if(cells.size() < tableRow.size()) { // Are there fewer cells than required?
                tableIndex = -1;
                continue;
            }

            for(int i = 0; i < tableRow.size(); i++) {
                if(!cells.get(i).equals(tableRow.get(i))) { // If cells do not match, stop reading table
                    tableIndex = -1;
                    break;
                }
            }

            if(tableIndex >= 0) {
                tableIndex++;
            }

            if(tableIndex >= table.size()) {
                tableFound = true;
                break;
            }
        }

        Assert.assertTrue(tableFound);
    }

}
