package co.uk.codeyogi.cucumber.demo.cucumber;

import co.uk.codeyogi.cucumber.demo.Application;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import com.epam.reportportal.cucumber.ScenarioReporter;

@RunWith(Cucumber.class)
@CucumberContextConfiguration
@SpringBootTest(classes = {Application.class,
                            CucumberIT.class},
                            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberOptions(plugin={"pretty","com.epam.reportportal.cucumber.StepReporter"}, tags = "",features="src/test/resources/features")
public class CucumberIT {
}
