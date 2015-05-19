package org.vaadin.csvalidation.widgetset.client.regexpeditor;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Client-side implementation of the {@code RegExpEditor}.
 * 
 * @author Marko Grönroos
 */
public class RegExpEditorState extends Composite implements KeyUpHandler {
    
    private TextBox regexp     = new TextBox();
    private TextBox teststring = new TextBox();
    private Label   result     = new Label();

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public RegExpEditorState() {
        Grid grid = new Grid(3, 2);
        initWidget(grid);

        grid.setText(0, 0, "Regular Expression");
        grid.setWidget(0, 1, regexp);
        
        grid.setText(1, 0, "Test String");
        grid.setWidget(1, 1, teststring);
        
        grid.setText(2, 0, "Result:");
        grid.setWidget(2, 1, result);
        
        regexp.addKeyUpHandler(this);
        teststring.addKeyUpHandler(this);

        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        // setStyleName(CLASSNAME);
    }

    public void onKeyUp(KeyUpEvent arg0) {
        if (teststring.getValue().matches(regexp.getValue()))
            result.setText("matches");
        else
            result.setText("does not match");
    }
}
