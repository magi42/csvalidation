package org.vaadin.csvalidation.widgetset.client.regexpeditor;

import org.vaadin.csvalidation.RegExpEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Client-side implementation of the {@code RegExpEditor}.
 * 
 * @author Marko Grönroos
 */
@Connect(RegExpEditor.class)
public class RegExpEditorConnector extends AbstractComponentConnector {
    private static final long serialVersionUID = -8832328346466837857L;

    @Override
    protected Widget createWidget() {
        return GWT.create(RegExpEditorWidget.class);
    }
}