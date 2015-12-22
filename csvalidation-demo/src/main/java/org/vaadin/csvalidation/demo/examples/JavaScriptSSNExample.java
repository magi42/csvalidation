package org.vaadin.csvalidation.demo.examples;

import org.vaadin.csvalidation.CSValidator;
import org.vaadin.csvalidation.demo.CSValidationUtil;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Example of SSN validation with JavaScript.
 * 
 * @author Marko Grönroos
 */
public class JavaScriptSSNExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 4288738388331703351L;

    /**
     * Creates the example component and loads the JavaScript validator
     * from classpath.
     */
    public void init (String context) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        
        // BEGIN-EXAMPLE: javascript.ssn
        TextField ssn = new TextField("SSN");
        
        CSValidator validator = new CSValidator();
        validator.extend(ssn);
        String js = CSValidationUtil.readFile(getClass().getClassLoader(),
                "/com/vaadin/csvalidation/examples/javascript/ssn-fi.js");
        validator.setJavaScript(js);
        // END-EXAMPLE: javascript.ssn

        layout.addComponent(ssn);

        // Option for preventing invalid input.
        CheckBox prevent = new CheckBox("Prevent invalid input");
        prevent.addValueChangeListener(event -> {
            validator.setPreventInvalidTyping((Boolean)event.getProperty().getValue());
            
            // Has to be cleared because the input can currently be invalid.
            ssn.setValue("");
        });
        prevent.setImmediate(true);
        layout.addComponent(prevent);
        
        setCompositionRoot(layout);
    }

    public String getLongName() {
        return "Finnish Social Security Number Validation with JavaScript";
    }

    public String getExampleDescription() {
        return "Shows how to do almost complete validation "+
               "of a (Finnish) social security number using JavaScript.";
    }
}
