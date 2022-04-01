

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class) 
@CucumberOptions(features="/Users/suresh/eclipse-workspace/LMSCucumberAutomation/src/test/resources/features",
glue={"LMSCucumberAutomation/stepDefinitions"}, 
dryRun=false,
monochrome=true,
plugin = {"pretty","html:test-output",
		  "json:target/MyReports/report.json",
		  "junit:target/MyReports/report.xml",
		 // "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
        }
//publish = true
		)
public class TestRun {

}
