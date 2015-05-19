package org.vaadin.csvalidation.demo.examples;

import org.vaadin.csvalidation.CSValidator;

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
public class JavaScriptNumberExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 4288738388331703351L;

    /**
     * Creates the example component and loads the JavaScript validator
     * from classpath.
     */
    public void init (String context) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        
        // BEGIN-EXAMPLE: javascript.number
        final TextField number = new TextField("Number");

        // Set an initial value
        number.setValue("42");
        
        final CSValidator validator = new CSValidator();
        validator.extend(number);
        
        String js = "if (value.match(\"^[0-9]*$\"))\n" +
        		"    null; // Success\n" +
        		"else\n" +
        		"    \"Fail\";\n";
        validator.setJavaScript(js);
        // END-EXAMPLE: javascript.number

        layout.addComponent(number);

        // Option for preventing invalid input.
        CheckBox prevent = new CheckBox("Prevent invalid input");
        prevent.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 2736306965983131854L;
            public void valueChange(ValueChangeEvent event) {
                validator.setPreventInvalidTyping((Boolean)event.getProperty().getValue());
                
                // Set to some valid value, because the input can currently be invalid
                number.setValue("42");
            }
        });
        prevent.setImmediate(true);
        layout.addComponent(prevent);
        
        setCompositionRoot(layout);
    }

    public String getLongName() {
        return "Validating a numeric value";
    }

    public String getExampleDescription() {
        return "The same as the regexp example, but by using a regexp inside JavaScript";
    }
}
