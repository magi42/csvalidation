package org.vaadin.csvalidation.demo.examples;

import org.vaadin.csvalidation.CSValidator;
import org.vaadin.csvalidation.demo.CSValidationUtil;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Example of validating postal code syntax with JavaScript.
 * 
 * @author Marko Grönroos
 */
public class JavaScriptPasswordExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 4288738388331703351L;

    /**
     * 
     */
    public void init (String context) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        
        // BEGIN-EXAMPLE: javascript.password
        final PasswordField password = new PasswordField("Secret Password");
        
        String js = CSValidationUtil.readFile(getClass().getClassLoader(),
        				"/com/vaadin/csvalidation/examples/javascript/password.js");
        CSValidator validator = new CSValidator();
        validator.extend(password);
        validator.setJavaScript(js);
        
        layout.addComponent(password);

        final TextField plain = new TextField("Plain Password");
        CSValidator validator2 = new CSValidator();
        validator2.extend(plain);
        validator2.setJavaScript(js);
        layout.addComponent(plain);
        // END-EXAMPLE: javascript.password
        
        setCompositionRoot(layout);
    }

    public String getLongName() {
        return "Password Minimal Requirements Validation with a JavaScript Validator";
    }

    public String getExampleDescription() {
        return "The password must be at least 8 characters long with at least " +
        	   "two lower and two upper case letters, two numbers, and two special characters.";
    }
}
