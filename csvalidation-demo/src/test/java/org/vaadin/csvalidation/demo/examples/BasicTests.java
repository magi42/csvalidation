package org.vaadin.csvalidation.demo.examples;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.LabelElement;

public class BasicTests extends TestBenchTestCase {
	@Before
	public void setUp() throws Exception {
	    setDriver(new FirefoxDriver());
	}

	@Test
	public void basic() {
		getDriver().get("http://localhost:8080/csvalidation-demo/demo");
		
		LabelElement title = $(LabelElement.class).first();
		Assert.assertTrue("No title", "Client-Side Validation Demo".equals(title.getText()));
	}
	
	@After
	public void tearDown() throws Exception {
	    driver.quit();
	}
}
