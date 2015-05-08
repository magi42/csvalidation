package org.vaadin.csvalidation;

import org.vaadin.csvalidation.widgetset.client.javascripteditor.JavaScriptEditorState;

import com.vaadin.ui.AbstractComponent;

/**
 * Allows editing JavaScript validators.
 *
 * It is not currently possible to get the edited JavaScript.
 * This is intended purely for developing the regular expressions.
 *  
 * @author magi
 */
public class JavaScriptEditor extends AbstractComponent {
    private static final long serialVersionUID = -3166061695282948830L;

    String javascript;

    @Override
    public JavaScriptEditorState getState() {
        return (JavaScriptEditorState) super.getState();
    }

    /**
     * Sets the initial JavaScript code.
     * 
     * @param javascript JavaScript code to be used for validation
     */
    public void setJavaScript(String javascript) {
        getState().javascript = javascript;
    }
}
