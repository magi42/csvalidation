package org.vaadin.csvalidation.examples;

import org.vaadin.csvalidation.RegExpEditor;
import com.vaadin.ui.CustomComponent;

/**
 * Example of using the regular expression editor component.
 * 
 * @author Marko Grönroos
 */
public class RegExpEditorExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 4288738388331703351L;

    /**
     * Creates the editor.
     * 
     * Does not load any content to the editor.
     */
    public void init (String context) {
        RegExpEditor editor = new RegExpEditor();
        setCompositionRoot(editor);
    }

    public String getLongName() {
        return "Regular Expression Validator Editor";
    }

    public String getExampleDescription() {
        return "Allows editing a regular expression for validating a test value.";
    }
}
