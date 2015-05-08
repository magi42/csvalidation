package org.vaadin.csvalidation.examples;

import org.vaadin.csvalidation.CSValidator;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Example of validating postal code syntax with JavaScript.
 * 
 * @author Marko Grönroos
 */
public class JavaScriptPostCodeExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 4288738388331703351L;

    /**
     * 
     */
    public void init (String context) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        
        // BEGIN-EXAMPLE: javascript.postcode
        final TextField postcode = new TextField("Postal Code");
        
        final CSValidator validator = new CSValidator();
        validator.extend(postcode);
        validator.setJavaScript("if (value >= 0 && value < 10000)" +
                               "    \"partial\";" +
                               "else if (value >= 10000 && value <= 99999)" +
                               "    null;" +
                               "else" +
                               "    \"Postal Code must be a 5-digit number between 10000 and 99999\";");
        // END-EXAMPLE: javascript.postcode

        layout.addComponent(postcode);
        
        // Option for preventing invalid input.
        CheckBox prevent = new CheckBox("Prevent invalid input");
        prevent.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 2736306965983131854L;
            public void valueChange(ValueChangeEvent event) {
                validator.setPreventInvalidTyping((Boolean)event.getProperty().getValue());
                
                // Has to be cleared because the input can currently be invalid.
                postcode.setValue("");
            }
        });
        prevent.setImmediate(true);
        layout.addComponent(prevent);
        setCompositionRoot(layout);
    }

    public String getLongName() {
        return "Postal Code Syntax Validation with a JavaScript Validator";
    }

    public String getExampleDescription() {
        return "Shows how to validate the correctness of integer values.";
    }
}
