package org.vaadin.csvalidation.demo.examples;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.TreeElement;

public class CSValidationTestCase extends TestBenchTestCase {
	@Before
	public void setUp() throws Exception {
	    setDriver(new FirefoxDriver());
		getDriver().get("http://localhost:8080/csvalidation-demo/demo");
	}

	@After
	public void tearDown() throws Exception {
	    driver.quit();
	}
	
	protected void selectMenuItem(String itemId) {
		TreeElement menu = $(TreeElement.class).first();
		WebElement item = menu.findElement(By.className("v-tree-node-caption-item-" + itemId)).findElement(By.tagName("span"));
		item.click();
	}
}
