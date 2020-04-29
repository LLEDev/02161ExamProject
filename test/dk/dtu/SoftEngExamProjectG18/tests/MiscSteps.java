package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Context.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Context.InputContext;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.Exceptions.CommandException;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class MiscSteps {

    @Before // Reset CompanyDB before each scenario
    public void beforeScenario(){
        CompanyDB.initNewInstance();
    }

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String message) {
        TestHolder testHolder = TestHolder.getInstance();
        assertTrue(testHolder.response.exceptionMessageIs(message));
    }


    @Then("the following table is presented")
    public void theFollowingTableIsPresented(List<List<String>> table) {
        Scanner s = new Scanner(TestHolder.getInstance().response.getResponse());

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

    @When("the InputContext assertions are tested with invalid values, CommandExceptions are thrown")
    public void theInputContextAssertionsAreTestedWithInvalidValuesCommandExceptionsAreThrown() {
        CompanyDB db = CompanyDB.getInstance();
        String employeeID = "AA";

        db.getEmployees().put(employeeID, new Employee(employeeID, employeeID));
        db.setSignedInEmployee(employeeID);
        db.setInputContext(new EmployeeInputContext());

        InputContext context = CompanyDB.getContext();
        Employee employee = db.getSignedInEmployee();
        employee.setWeeklyActivityCap(0);
        Project project = new Project("Project");

        Class<CommandException> e = CommandException.class;
        Assert.assertThrows(e, () -> context.assertArgumentsValid(2, 1));
        Assert.assertThrows(e, () -> context.assertAvailableActivities(employee));
        Assert.assertThrows(e, () -> context.assertSignedInEmployeePM(project));
        Assert.assertThrows(e, () -> context.assertStringParseDateDoable("NOT-A-DATE"));
        Assert.assertThrows(e, () -> context.assertStringParseIntDoable("NOT-AN-INT"));
        Assert.assertThrows(e, () -> context.assertValidProjectName(""));
        Assert.assertThrows(e, () -> context.getActivity(project, "3"));
        Assert.assertThrows(e, () -> context.getEmployee(db, "NOT-AN-EMPLOYEE"));
        Assert.assertThrows(e, () -> context.getProject(db, "NOT-A-PROJECT"));
    }
}
