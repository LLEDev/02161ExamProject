package dk.dtu.SoftEngExamProjectG18.tests;

import dk.dtu.SoftEngExamProjectG18.DB.CompanyDB;
import dk.dtu.SoftEngExamProjectG18.tests.Util.TestHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
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
}
