package org.vaadin.csvalidation.widgetset.client.csvalidator;

import java.util.logging.Logger;

import org.vaadin.csvalidation.CSValidator;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VTextField;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("javadoc")
@Connect(CSValidator.class)
public class CSValidatorConnector extends AbstractExtensionConnector
        implements KeyUpHandler, KeyDownHandler {
    private static final long serialVersionUID = 975266906597051369L;
    
    Logger logger = Logger.getLogger(this.getClass().getName());

    VTextField field;
    private String oldtext;

    @Override
    protected void extend(ServerConnector target) {
        field = (VTextField) ((ComponentConnector)target).getWidget();
        field.addKeyUpHandler(this);
        field.addKeyDownHandler(this);
    }

    @Override
    public CSValidatorState getState() {
        return (CSValidatorState) super.getState();
    }
    
    /** Validation message types. */
    public enum Validity {
        /** Message for valid strings. */
        VALID,
        /** Message for partially valid strings. */
        PARTIAL,
        /** Message for invalid strings. */
        INVALID};

    // The old text value must be initialized from the most recent field value
    // before typing, or otherwise it bugs with preventInvalidTyping
    // if there's a value set from the server-side. Perhaps it could
    // be set somehow otherwise as well, when the field value is actually
    // received from the server-side.
	@Override
	public void onKeyDown(KeyDownEvent event) {
        Validity validity = validate(field.getText());

        if (validity == Validity.VALID || validity == Validity.PARTIAL) {
            oldtext = field.getText();
        }
	}
    
    public void onKeyUp(KeyUpEvent event) {
        validate();
    }
    
    void validate() {
    	// logger.info("Entering CSValidatorConnector.validate()");
    	
        // Clear all client-side validation artifacts if the input is empty.
        // This could be reconsidered.
    	/*
        if (value.length() == 0) {
            oldtext = "";
            removeStyleName("valid");
            removeStyleName("invalid");
            removeStyleName("partial");
            return;
        }*/
        
        Validity validity = validate(field.getText());
        logger.fine("Validity is " + validity);

        if (validity == Validity.VALID) {
            field.addStyleName("valid");
            field.removeStyleName("invalid");
            field.removeStyleName("partial");
            oldtext = field.getText();
        } else if (validity == Validity.PARTIAL) {
            field.addStyleName("partial");
            field.removeStyleName("valid");
            field.removeStyleName("invalid");
            oldtext = field.getText();
        } else if (validity == Validity.INVALID) {
            field.removeStyleName("partial");
            if (!getState().preventInvalidTyping) {
                field.addStyleName("invalid");
                field.removeStyleName("valid");
            } else {
                field.addStyleName("valid");
                field.removeStyleName("invalid");

                // Undo invalid typing because invalid values are not allowed.
                field.setText(oldtext);
                
                // I have no idea why this is done, perhaps fixes some
                // browser compatibility issue?
                Scheduler.get().scheduleDeferred(new Command() {
                    public void execute() {
                        field.setText(oldtext);
                    }
                });
            }
        }        
    }

    Validity validate(String value) {
        Validity validity = Validity.INVALID;
        
        // RegExp validation.
        if (getState().regexp != null && (value.length() > 0 || getState().validateEmpty)) {
            // Pad the current input with the input padding to have
            // a partially valid value when the current input is incomplete.
            boolean partial = false;
            if (getState().inputPadding != null && value.length() < getState().inputPadding.length()) {
                value += getState().inputPadding.substring(value.length());
                partial = true;
            }
            
            // It would be nice to be able to detect partial matches here.
            if (value.matches(getState().regexp)) {
                if (partial)
                    validity = Validity.PARTIAL;
                else
                    validity = Validity.VALID;
                renderValidationMessage(null, validity);
            } else      
                renderValidationMessage(getState().regexpErrorMsg, Validity.INVALID);
        }
        
        final String RESULT_KEYWORD_VALID   = "valid";
        final String RESULT_KEYWORD_PARTIAL = "partial";
        
        // JavaScript validation
        if (getState().javascript != null) {
            String evalresult = evalJavaScript(getState().javascript, value);
            if (evalresult == null) {
                validity = Validity.VALID;
                renderValidationMessage(null, validity);
            } else if (evalresult.startsWith(RESULT_KEYWORD_VALID)) {
                validity = Validity.VALID;
                String validMsg = evalresult.substring(RESULT_KEYWORD_VALID.length()).trim();
                renderValidationMessage(validMsg, validity);
            } else if (evalresult.equals(RESULT_KEYWORD_PARTIAL)) {
                validity = Validity.PARTIAL;
                String partialMsg = evalresult.substring(RESULT_KEYWORD_PARTIAL.length()).trim();
                if (partialMsg == null || partialMsg.equals(""))
                    renderValidationMessage(null, validity);
                else
                    renderValidationMessage(partialMsg, validity);
            } else {
                validity = Validity.INVALID;
                renderValidationMessage(evalresult, validity);
            }
        }

        return validity;
    }
    
    /**
     * Renders the validation message.
     * 
     * @param message the message string to render
     * @param msgtype type of the message
     */
    public void renderValidationMessage(String message, Validity msgtype) {
        String cssSuffix = "";
        if (msgtype == Validity.VALID)
            cssSuffix = "validmessage";
        else if (msgtype == Validity.PARTIAL)
            cssSuffix = "partialmessage";
        else if (msgtype == Validity.INVALID)
            cssSuffix = "errormessage";
        
        Element textbox = DOM.getParent(field.getElement());
        Element messageElement = DOM.getNextSibling(field.getElement());

        // Check that the span is one created by this widget and not
        // something else. Can happen in CssLayout, for example.
        if (messageElement != null) {
            String elementClass = messageElement.getAttribute("class");
            if (!(elementClass.contains("csvalidator-validmessage")
                            || elementClass.contains("csvalidator-partialmessage")
                            || elementClass.contains("csvalidator-errormessage")))
                messageElement = null;
        }
        
        if (messageElement == null && message != null) {
            messageElement = DOM.createDiv();
            messageElement.setAttribute("class", "csvalidator-message csvalidator-" + cssSuffix);
            
            // Can not use appendChild() here, as there could be some other
            // elements after the textbox, for example in a CssLayout.
            DOM.insertChild(textbox, messageElement, DOM.getChildIndex(textbox, field.getElement()) + 1);

            messageElement.setInnerText(message);
        } else if (messageElement != null && message != null) {
            messageElement.setInnerText(message);
            messageElement.setAttribute("class", "csvalidator-message csvalidator-" + cssSuffix);
        } else if (messageElement != null && message == null) {
            textbox.removeChild(messageElement);
        }
    }

    /**
     * Evaluates a JavaScript program.
     * 
     * <p>The value has to be defined as a variable in the beginning of the script,
     * because the "{@code value}" parameter does not work (at least) in Safari.</p>
     * 
     * @param javascript
     *            the JavaScript program
     * @param value
     *            parameter to be passed tor the JavaScript program in the
     *            "value" variable
     * @return the value of the last executed statement. Should be null on
     *         success, "partial" on partial success, and otherwise an error
     *         description.
     */
    public static native String evalJavaScript(String javascript, String value) /*-{
        var res = "evaluation failed";
        // this.value = value; // Does not work in Safari
        if (javascript == null)
                res = "no script";
        else {
            // Quote quotes and hyphens in the value parameter
            value = value.replace(/\\/g, "\\\\");
            value = value.replace(/\"/g, "\\\"");
            value = value.replace(/\n/g, "\\n");
            
            // Pass the value parameter
            value_js = "var value = \"" + value + "\";\n";
                try {
                $wnd.eval(value_js);
                res = $wnd.eval(javascript);
                } catch (e) {
                res = "evaluation failed due to " + e;
                }
        }
        return res;
        }-*/;
}
