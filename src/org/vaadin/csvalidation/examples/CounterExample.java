package org.vaadin.csvalidation.examples;

import org.vaadin.csvalidation.CSValidator;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Example of creating a content length counter with JavaScript.
 * 
 * @author Marko Grönroos
 */
public class CounterExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 1380442481960261529L;

    /**
     * 
     */
    public void init (String context) {
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName("counterexample");
        layout.setSpacing(true);
        layout.setMargin(true);
        
        if (context.equals("textfield"))
            textField(layout);
        else if (context.equals("textarea"))
            textArea(layout);
        else if (context.equals("limited"))
            limited(layout);
        else if (context.equals("countdown"))
            countdown(layout);
        else
            layout.addComponent(new Label("Invalid context: " + context));
        
        setCompositionRoot(layout);
    }
    
    void textField(VerticalLayout layout) {
        // BEGIN-EXAMPLE: javascript.counter.textfield
        final TextField textfield = new TextField("Text Field with Counter");
        textfield.setColumns(30);
        
        CSValidator validator = new CSValidator();
        validator.extend(textfield);
        validator.setJavaScript("\"valid \" + value.length");
        validator.setValidateInitialEmpty(true);
        // END-EXAMPLE: javascript.counter.textfield

        layout.addComponent(textfield);
    }
    
    void textArea(VerticalLayout layout) {
        // BEGIN-EXAMPLE: javascript.counter.textarea
        final TextArea textarea = new TextArea("Text Area with Counter");
        textarea.setColumns(30);
        textarea.setRows(5);
        
        CSValidator validator = new CSValidator();
        validator.extend(textarea);
        validator.setJavaScript("\"valid \" + value.length + \" characters\"");
        validator.setValidateInitialEmpty(true);
        // END-EXAMPLE: javascript.counter.textarea

        layout.addComponent(textarea);
    }
    
    void limited(VerticalLayout layout) {
        // BEGIN-EXAMPLE: javascript.counter.limited
        final TextArea limited = new TextArea("Length Limit");
        limited.setColumns(30);
        limited.setRows(5);

        CSValidator validator = new CSValidator();
        validator.extend(limited);
        validator.setJavaScript("var maxvalue = 20;\n" +
                              "var charsleft = maxvalue - value.length;\n" +
                              "var result = null;\n" +
                              "if (charsleft > 0)\n" +
                              "    result = \"valid \" + value.length + \" of \" + maxvalue;\n" +
                              "else\n" +
                              "    result = \"\" + value.length + \" of \" + maxvalue;\n" +
                              "result;");
        validator.setValidateInitialEmpty(true);
        // END-EXAMPLE: javascript.counter.limited

        layout.addComponent(limited);
    }
    
    void countdown(VerticalLayout layout) {
        // BEGIN-EXAMPLE: javascript.counter.countdown
        final TextArea countdown = new TextArea("Countdown");
        countdown.setColumns(30);
        countdown.setRows(5);

        CSValidator validator = new CSValidator();
        validator.extend(countdown);
        validator.setJavaScript("var maxvalue = 20;\n" +
                                "var charsleft = maxvalue - value.length;\n" +
                                "var result = null;\n" +
                                "if (charsleft > 0)\n" +
                                "    result = \"valid \" + charsleft + \" left\";\n" +
                                "else\n" +
                                "    result = \"\" + charsleft + \" left\";\n" +
                                "result;");
        validator.setValidateInitialEmpty(true);
        // END-EXAMPLE: javascript.counter.countdown

        layout.addComponent(countdown);
    }

    public String getLongName() {
        return "Counting characters with a JavaScript Validator";
    }

    public String getExampleDescription() {
        return "Shows how to use a validation success message for counting characters. " +
               "If the JavaScript validator returns a message that begins with a \"<tt>valid</tt>\" keyword, " +
               "the message can contain a message string for a valid case. Also messages in a " +
               "partial case can be given with the \"<tt>partial</tt>\" keyword in the beginning.";
    }
}
