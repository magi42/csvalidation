package org.vaadin.csvalidation.examples;

import org.vaadin.csvalidation.CSValidator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Regular expression validation example.
 * 
 * Demonstrates the use of {@link CSValidator} for client-side
 * validation and the use of {@code RegexpValidator} for server-side
 * validation using the same regular expression.
 * 
 * @author Marko Grönroos
 */
public class RegExpSSNExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 4288738388331703351L;

    /**
     * Creates the example.
     */
    public void init (String context) {
        final VerticalLayout layout = new VerticalLayout();

        // BEGIN-EXAMPLE: regexp.ssn
        final Form form = new Form();
        form.setCaption("Person");
        
        // Validate full name both on client- and server-side.
        TextField fn = new TextField("Full Name");

        // The regular expression for validation
        String fn_regexp = "\\w+ \\w+";
        
        // Validate the on client-side
        final CSValidator validator = new CSValidator();
        validator.extend(fn);
        validator.setRegExp(fn_regexp);
        validator.setErrorMessage("Full name must be the first name and the last name");
        
        // Validate the on server-side using the same regular expression
        fn.addValidator(new RegexpValidator(fn_regexp, "Invalid full name"));
        fn.setRequired(true);
        fn.setRequiredError("Full name is required");

        form.addField("fn", fn);

        // Validate SSN both on client- and server-side.
        final TextField ssn = new TextField("SSN");
        
        final CSValidator validator2 = new CSValidator();
        validator2.extend(ssn);
        String ssn_regexp = "[0-9]{6}-[0-9]{3}[0-9a-zA-Z]"; 
        validator2.setRegExp(ssn_regexp, "000000-000A");
        validator2.setPreventInvalidTyping(false);
        ssn.addValidator(new RegexpValidator(ssn_regexp, "Invalid SSN"));
        ssn.setRequired(true);
        ssn.setRequiredError("SSN is required");
        form.addField("ssn", ssn);

        // The postal code is validated only on the server-side.
        final TextField postcode = new TextField("Postal Code");
        postcode.addValidator(new RegexpValidator("[1-9][0-9]{4}", "Invalid postal code"));
        postcode.setRequired(true);
        postcode.setRequiredError("Postal code is required");
        form.addField("postcode", postcode);
        
        Button ok = new Button("Ok");
        form.getFooter().addComponent(ok);
        ok.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1000961338335062784L;

            public void buttonClick(ClickEvent event) {
                try {
                    form.commit();
                    layout.addComponent(new Label("Postal code:" + (String) postcode.getValue()));
                } catch (InvalidValueException e) {
                }
            }
        });
        // END-EXAMPLE: regexp.ssn
        
        layout.addComponent(form);
        setCompositionRoot(layout);
    }

    public String getLongName() {
        return "Social Security Number Validation with a Regular Expression";
    }

    public String getExampleDescription() {
        return "Shows how to use identical regular expressions "+
               "to validate values both on the client and server side. "+
               "The postal code is validated only on the server side.";
    }
}
