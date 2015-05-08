package org.vaadin.csvalidation.widgetset.client.csvalidator;

import com.vaadin.shared.communication.SharedState;

@SuppressWarnings("javadoc")
public class CSValidatorState extends SharedState {
    private static final long serialVersionUID = 8705186358607049677L;
    
    public String regexp;
    public String javascript;
    public String inputPadding;
    public String regexpErrorMsg;
    public boolean preventInvalidTyping = false;
    public boolean validateEmpty        = true;
    public boolean validateInitialEmpty = false;
}
