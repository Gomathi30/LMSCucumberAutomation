package stepDefinitions;

import org.junit.runner.RunWith;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/features",
glue= {"stepDefinitions"},monochrome = true,
//plugin= {"pretty","html:target/HtmlReports/inder.html"})
publish = true,
plugin = {
		  "html:target/MyReports/LMS-html-report.html",
		  "json:target/MyReports/report.json",
		  "junit:target/MyReports/report.xml",
		  "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:target",
		  "rerun:target/failed_scenarios.txt"
		 })
public class LMSRunner {
	
}
