package org.vaadin.csvalidation.widgetset.client.javascripteditor;

import com.vaadin.shared.AbstractComponentState;

/**
 * Allows editing JavaScript validators.
 *
 * It is not currently possible to get the edited JavaScript.
 * This is intended purely for developing the regular expressions.
 *  
 * @author magi
 */
public class JavaScriptEditorState extends AbstractComponentState {
    private static final long serialVersionUID = -3166061695282948830L;

    @SuppressWarnings("javadoc")
    public String javascript;
}
