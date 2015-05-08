package org.vaadin.csvalidation.widgetset.client.javascripteditor;



import org.vaadin.csvalidation.widgetset.client.csvalidator.CSValidatorConnector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Client-side implementation of the {@code JavaScriptEditor}.
 * 
 * @author Marko Grönroos
 */
public class JavaScriptEditorWidget extends Composite implements ClickHandler {

    /** Set the tagname used to statically resolve widget from UIDL. */
    public static final String TAGNAME = "javascripteditor";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-" + TAGNAME;

    private TextArea javascript;
    private TextBox  teststring = new TextBox();
    private Label    result     = new Label();
    private Button   validate   = new Button("Validate");

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public JavaScriptEditorWidget() {
        Grid grid = new Grid(4, 2);
        initWidget(grid);

        javascript = new TextArea();
        javascript.setCharacterWidth(60);
        javascript.setVisibleLines(15);
        grid.setText(0, 0, "JavaScript Validator");
        grid.setWidget(0, 1, javascript);
        
        grid.setText(1, 1, "The last executed statement must have a value, null on success, otherwise an error string.");
        
        grid.setText(2, 0, "Test String");
        HorizontalPanel horiz = new HorizontalPanel();
        horiz.add(teststring);
        horiz.add(validate);
        grid.setWidget(2, 1, horiz);
        
        grid.setText(3, 0, "Result:");
        grid.setWidget(3, 1, result);
        
        validate.addClickHandler(this);
        
        javascript.setValue("if (value == \"expected\")\n    null; // Success\nelse\n    \"Fail\";\n");
        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    public void onClick(ClickEvent arg0) {
        String evalresult = CSValidatorConnector.evalJavaScript(javascript.getValue(), teststring.getValue());
        
        if (evalresult == null)
            result.setText("success");
        else if (evalresult == "partial")
            result.setText("success (partial)");
        else
            result.setText("error: " + evalresult);
    }
    
}
