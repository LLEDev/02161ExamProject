package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.Business.Application;
import dk.dtu.SoftEngExamProjectG18.Input.EmployeeInputContext;
import dk.dtu.SoftEngExamProjectG18.Input.InputContext;
import dk.dtu.SoftEngExamProjectG18.Business.Employee;
import dk.dtu.SoftEngExamProjectG18.Business.Project;
import dk.dtu.SoftEngExamProjectG18.Input.Enums.InputContextType;
import dk.dtu.SoftEngExamProjectG18.Input.Exceptions.CommandException;
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
        Application.init(InputContextType.Emp);
    }

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String message) {
        TestHolder testHolder = TestHolder.getInstance();
        assertTrue(testHolder.getResponse().exceptionMessageIs(message));
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

    @When("the InputContext assertions are tested with invalid values, CommandExceptions are thrown")
    public void theInputContextAssertionsAreTestedWithInvalidValuesCommandExceptionsAreThrown() {
        InputContext context = Application.getInstance().getContext();

        Class<CommandException> e = CommandException.class;
        Assert.assertThrows(e, () -> context.assertArgumentsValid(2, 1));
        Assert.assertThrows(e, () -> context.assertStringParseDateDoable("NOT-A-DATE"));
        Assert.assertThrows(e, () -> context.assertStringParseIntDoable("NOT-AN-INT"));

    }

    @When("the Application assertions are tested with invalid values, IllegalArgumentExceptions are thrown")
    public void theApplicationAssertionsAreTestedWithInvalidValuesIllegalArgumentExceptionsAreThrown() {
        Application application = Application.getInstance();
        String employeeID = "AA";

        application.createEmployee(employeeID, employeeID);
        application.setSignedInEmployee(employeeID);
        application.setContext(new EmployeeInputContext());

        Employee employee = application.getSignedInEmployee();
        employee.setWeeklyActivityCap(0);
        Project project = new Project(1, "Project");

        Class<IllegalArgumentException> e = IllegalArgumentException.class;
        Assert.assertThrows(e, () -> application.getEmployee("NOT-AN-EMPLOYEE"));
        Assert.assertThrows(e, () -> application.getProject("NOT-A-PROJECT"));
    }
}
