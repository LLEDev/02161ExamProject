package dk.dtu.SoftEngExamProjectG18.tests;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;


@RunWith(Cucumber.class)
@CucumberOptions(
    features = "use_cases",
    plugin = { "html:target/cucumber/wikipedia.html"},
    monochrome = true,
    glue = { "dk.dtu.SoftEngExamProjectG18.tests"},
    snippets = SnippetType.CAMELCASE,
    strict = true
)
public class AcceptanceTest {

}
