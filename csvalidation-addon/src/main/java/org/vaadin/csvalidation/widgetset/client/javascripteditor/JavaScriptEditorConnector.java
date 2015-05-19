package org.vaadin.csvalidation.widgetset.client.javascripteditor;

import org.vaadin.csvalidation.JavaScriptEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Client-side implementation of the {@code JavaScriptEditor}.
 * 
 * @author Marko Grönroos
 */
@Connect(JavaScriptEditor.class)
public class JavaScriptEditorConnector extends AbstractComponentConnector {
    private static final long serialVersionUID = 1685246894016373193L;

    @Override
    protected Widget createWidget() {
        return GWT.create(JavaScriptEditorWidget.class);
    }
}
