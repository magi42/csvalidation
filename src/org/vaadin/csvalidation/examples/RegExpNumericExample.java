package org.vaadin.csvalidation.examples;

import org.vaadin.csvalidation.CSValidator;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Regular expression validation example.
 * 
 * Demonstrates the use of {@link CSValidator} for client-side
 * validation and the use of {@code RegexpValidator} for server-side
 * validation using the same regular expression.
 * 
 * @author Marko Grönroos
 */
public class RegExpNumericExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 4288738388331703351L;

    /**
     * Creates the example.
     */
    public void init (String context) {
        final VerticalLayout layout = new VerticalLayout();

        // BEGIN-EXAMPLE: regexp.numeric
        // Have a form with some bound data
        FormLayout form = new FormLayout();
        form.setCaption("My Form");
        final FieldGroup binder = new FieldGroup();
        
        // Have a field
        final TextField number = new TextField("Your Number");
        number.setValue("123");
        
        // Validate the input both on client and server-side.
        CSValidator validator = new CSValidator();
        validator.extend(number);
        String regexp = "[0-9]*"; 
        validator.setRegExp(regexp);
        validator.setPreventInvalidTyping(true);
        validator.setErrorMessage("Must be a number");
        number.addValidator(new RegexpValidator(regexp, "Not a number"));
        number.setRequired(true);
        number.setRequiredError("Value is required");

        form.addComponent(number);
        binder.bind(number, "number");

        number.setValue("123");
        
        Button ok = new Button("OK", new Button.ClickListener() {
            private static final long serialVersionUID = 1000961338335062784L;

            public void buttonClick(ClickEvent event) {
                try {
                    binder.commit();
                } catch (CommitException e) {
                	Notification.show("Server-side validation failed");
                }
            }
        });
        // END-EXAMPLE: regexp.numeric
        
        layout.addComponents(form, ok);
        setCompositionRoot(layout);
    }

    public String getLongName() {
        return "Validating that the field value is numeric";
    }

    public String getExampleDescription() {
        return "Shows also how to use identical regular expressions "+
               "to validate values both on the client and server side.";
    }
}
