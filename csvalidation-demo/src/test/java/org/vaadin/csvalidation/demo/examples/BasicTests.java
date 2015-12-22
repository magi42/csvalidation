package org.vaadin.csvalidation.demo.examples;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class BasicTests extends CSValidationTestCase {
	@Test
	public void smoketest() {
		LabelElement title = $(LabelElement.class).first();
		Assert.assertTrue("No title", "Client-Side Validation Demo".equals(title.getText()));
	}

	@Test
	public void numericInput() {
		selectMenuItem("regexp.numeric");

		WebElement example = findElement(By.className("example"));

		TextFieldElement tf = $(TextFieldElement.class).context(example).first();

		// Before input it should not be valid nor invalid
		Assert.assertEquals("123", tf.getValue());
		Assert.assertTrue(!tf.getAttribute("class").contains("valid") && 
                          !tf.getAttribute("class").contains("invalid"));

		// After this it should be valid
		tf.sendKeys("456");
		Assert.assertEquals("123456", tf.getValue());
		Assert.assertTrue(tf.getAttribute("class").contains("valid") && 
                         !tf.getAttribute("class").contains("invalid"));

		// After invalid input it should still be valid...
		tf.sendKeys("abc");
		Assert.assertEquals("123456", tf.getValue());
		Assert.assertTrue(tf.getAttribute("class").contains("valid") && 
                         !tf.getAttribute("class").contains("invalid"));

		// ...but there should be error displayed
		FormLayoutElement myForm = $(FormLayoutElement.class).context(example).caption("My Form").first();
		WebElement errorMessage = myForm.findElement(By.className("csvalidator-errormessage"));
		Assert.assertEquals("Must be a number", errorMessage.getText());
	}
}
