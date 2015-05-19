package org.vaadin.csvalidation.demo.examples;

import org.vaadin.csvalidation.JavaScriptEditor;
import org.vaadin.csvalidation.demo.CSValidationUtil;

import com.vaadin.ui.CustomComponent;

/**
 * JavaScript validator editor.
 * 
 * @author Marko Grönroos
 */
public class JavaScriptEditorExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 4288738388331703351L;

    /**
     * Creates the JavaScript editor component.
     * 
     * The constructor currently loads an example code file; it should possibly be settable.
     */
    public void init (String context) {
        JavaScriptEditor editor = new JavaScriptEditor();
        
        String javascript = CSValidationUtil.readFile(getClass().getClassLoader(),
                                                             "/com/vaadin/csvalidation/examples/javascript/validator-example.js");
        editor.setJavaScript(javascript);
        
        setCompositionRoot(editor);
    }

    public String getLongName() {
        return "JavaScript Validator Editor";
    }

    public String getExampleDescription() {
        return "You can edit a JavaScript validator here. "+
               "The validator gets the test value in the 'value' variable. "+
               "The last executed statement must have the return value. "+
               "The return value must be null if validation is successful, or "+
               "an error string if it fails. The special 'partial' return value "+
               "indicates that the value is valid so far.";
    }
}
