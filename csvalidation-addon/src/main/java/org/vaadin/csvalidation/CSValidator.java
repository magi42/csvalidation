package org.vaadin.csvalidation;

import org.vaadin.csvalidation.widgetset.client.csvalidator.CSValidatorState;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.TextField;

/**
 * Component extension for validating TextField, PasswordField, and TextArea components
 * on the client-side.
 * The extension supports both regular
 * expression and JavaScript based validation on the client side.<p>
 * 
 * The validation is executed at each key press.
 * If invalid input is allowed (the default), the {@code invalid} CSS style is
 * added when the value is invalid. If the value is correct, but incomplete,
 * the {@code partial} style is set. If the value is valid, the {@code valid} style is set.<p>
 * 
 * If typing invalid input is not allowed
 * ({@link #setPreventInvalidTyping(boolean) setPreventInvalidTyping()}
 * is set to {@code true}), and the resulting value would
 * be invalid, the text field value remains unchanged.<p>
 * 
 * To enable validation with a regular expression, set it with
 * {@link #setRegExp(String) setRegExp()}.
 * To enable validation with a JavaScript program, set it with
 * {@link #setJavaScript(String) setJavaScript()}. You can use both a regular expression
 * and JavaScript validation simultaneously; the regular expression is validated first
 * so you can use it to block unwanted input altogether by having
 * {@link #setPreventInvalidTyping(boolean) setPreventInvalidTyping(true)}.<p>
 * 
 * Otherwise, the functionality of the component is identical to the
 * standard {@link TextField}.
 * 
 * @author Marko Grönroos
 */
public class CSValidator extends AbstractExtension {
    private static final long serialVersionUID = 5303741662440600501L;

    
    /**
     * Extends a text field to enable client-side validation.<p>
     * 
     * The behavior of the created text field is identical to that of
     * the regular {@link TextField} component.
     * To enable validation with a regular expression, set it with
     * {@link #setRegExp(String) setRegExp()}.
     * To enable validation with a JavaScript program, set it with
     * {@link #setJavaScript(String) setJavaScript()}.
     *
     * @param tf the text field or area to extend 
     */
    public void extend(AbstractTextField tf) {
        super.extend(tf);

        // setPreventInvalidTyping(false);
    }

    @Override
    public CSValidatorState getState() {
        return (CSValidatorState) super.getState();
    }

    /**
     * Sets the regular expression for validating the input.
     * 
     * <p>You should also set the error message with
     * {@link #setErrorMessage(String) setErrorMessage()} to provide feedback
     * about the problem.</p>
     * 
     * <p>You can choose to not allow invalid input at all by setting
     * {@link #setPreventInvalidTyping(boolean) setPreventInvalidTyping(true)}.
     * If you do not allow invalid input, you need to provide an example value
     * with {@link #setInputPadding(String) setInputPadding()} to allow
     * determining partially correct values. This is usually possible only for
     * fields with fixed lengths.</p>
     * 
     * <p>Please refer to JavaScript documentation for the syntax of the regular
     * expressions. If you use the same expression to validate the value on the
     * server-side, notice that the Java regular expressions have slightly
     * different syntax.</p>
     * 
     * <pre>
     * // The Finnish Social Security Number
     * final TextField ssn = new TextField("SSN");
     * String ssn_regexp = "[0-9]{6}[+-A][0-9]{3}[0-9a-zA-Z]";
     * 
     * // The client-side validation
     * CSValidator validator = new CSValidator();
     * validator.setRegExp(ssn_regexp);
     * validator.extend(ssn);
     * 
     * // The server-side validation
     * ssn.addValidator(new RegexpValidator(ssn_regexp, "Invalid SSN"));
     * ssn.setRequired(true);
     * ssn.setRequiredError("SSN is required");
     * 
     * form.addField("ssn", ssn);
     * </pre>
     * 
     * @param regexp
     *            the regular expression to match
     */
    public void setRegExp(String regexp) {
        getState().regexp = regexp;
    }

    /**
     * Sets the error message for invalid regular expression validation.
     * 
     * <p>The error message is not used for JavaScript validation, where the
     * error message is returned by the script.</p>
     * 
     * <p>The error message is meaningless if
     * {@link #setPreventInvalidTyping(boolean) setPreventInvalidTyping(true)}
     * is set, as in that case it is not possible to type invalid input at all.</p>
     * 
     * @see #setRegExp(String)
     * @see #setPreventInvalidTyping(boolean)
     * @param msg error message for failed validation
     */
    public void setErrorMessage(String msg) {
        getState().regexpErrorMsg = msg;
    }

    /**
     * Sets the regular expression and a valid padding input.
     * 
     * <p>The padding must be a valid string or otherwise the value will
     * never match. See {@link #setInputPadding(String) #setInputPadding()}
     * for a more detailed description of the padding.</p>
     * 
     * <p>The component will automatically be set as
     * {@link #setPreventInvalidTyping(boolean) setPreventInvalidTyping(true)},
     * as the padding is meaningless in the non-preventive mode.</p>
     * 
     * <p>Example:</p>
     *   
     * <pre>
     * // The Finnish Social Security Number
     * final TextField ssn = new TextField("SSN");
     * String ssn_regexp = "[0-9]{6}[+-A][0-9]{3}[0-9a-zA-Z]";
     * 
     * // The client-side validation
     * CSValidator validator = new CSValidator();
     * validator.setRegExp(ssn_regexp, "000000-000A");
     * validator.extend(ssn);
     * 
     * // The server-side validation
     * ssn.addValidator(new RegexpValidator(ssn_regexp, "Invalid SSN"));
     * 
     * form.addField("ssn", ssn);
     * </pre>
     * 
     * @see #setRegExp(String)
     * @see #setInputPadding(String)
     * @param regexp the regular expression to be used for validating the input
     * @param padding padding string which should be valid, see {@link #setInputPadding(String) setExample()}
     */
    public void setRegExp(String regexp, String padding) {
        getState().regexp = regexp;    
        getState().inputPadding = padding;
        getState().preventInvalidTyping = (padding == null);
    }
    
    /**
     * Sets the JavaScript code for validating the value.
     * 
     * <p>The program gets a {@code value} variable, which contains the current
     * value of the field as a string.</p>
     * 
     * <p>The program must return null if validation is successful, or otherwise
     * an error description. If it returns the special string "{@code partial}",
     * it is interpreted as a partial match, so that the value is unfinished.
     * Returning the result must be done with the value of the final 
     * executed statement, not with the {@code return} keyword!</p>
     * 
     * <p>Example:</p>
     * 
     * <pre>
     *     if (value == "hello")
     *         null; // success
     *     else
     *         "Must greet!"; // failure
     * </pre>
     * 
     * <p>The {@code partial} keyword can be followed by a message, which is
     * displayed in the message element with style
     * {@code v-validatedtextfield-partialmessage}. If the return string begins with
     * {@code valid} keyword, the text following it is displayed in the message element
     * with style {@code v-validatedtextfield-validmessage}.</p>
     * 
     * <p>In error state, the message element has style
     * {@code v-validatedtextfield-validmessage} and the text field an
     * additional {@code invalid} style. A valid text field has {@code valid}
     * style and partial a {@code partial} style.</p>
     * 
     * @param javascript a JavaScript program
     */
    public void setJavaScript(String javascript) {
        getState().javascript = javascript;
    }

    /**
     * Sets an input padding template for fixed-length input with regular
     * expression validation.<p>
     * 
     * The padding is a value that matches the validation condition. It must
     * also match if the beginning, to any point, is replaced with user input so
     * that the combined content is a valid value. For example, if the regular
     * expression is {@code [0-9][0-9][0-9][0-9][0-9][0-9]}, a valid padding would be {@code
     * 000000}. Now, if the user inputs {@code 12} as the first letters, the
     * padded value would be {@code 120000}, which is also valid.<p>
     * 
     * The padding is usually meaningful if the input value is expected to have a
     * fixed length, but can be used in some cases as well, for example when
     * input is homogeneous and has a minimum length.<p>
     * 
     * @param padding
     *            the example value
     */
    public void setInputPadding(String padding) {
        getState().inputPadding = padding;
        getState().preventInvalidTyping = (padding == null);
    }

    /**
     * Sets whether the field should prevent invalid input.<p>
     * 
     * If invalid values are not allowed (parameter is {@code true}), the field
     * prevents typing them. This can cause problems if the regular expression
     * does not match if the input is partial. For example, if expression is
     * "{@code ...}" and you try to type the first "{@code a}".
     * In such case, you also need to set the {@code inputPadding} property, 
     * for example to "{@code xxx}". Giving the
     * padding does, however, help only with fixed-width patterns.<p>
     * 
     * If invalid values are allowed (parameter is {@code false}), the field does not
     * prevent writing an invalid value, but sets the {@code invalid}
     * CSS style class. If the value is valid, the {@code valid} style
     * class is set. If the value is partially valid, the {@code partial}
     * style is set.<p>
     * 
     * The {@code preventInvalid} is automatically set to {@code true} when
     * the {@code inputPadding} property is set to a non-null value, and to
     * {@code false} when the {@code inputPadding} is {@code null}.<p>
     * 
     * @param preventInvalid
     *            {@code true} if invalid values are be prevented,
     *            otherwise {@code false}
     */
    public void setPreventInvalidTyping(boolean preventInvalid) {
        getState().preventInvalidTyping = preventInvalid;
    }
    
    /**
     * Sets whether the field is validated also when it is empty.
     * 
     * <p>Used currently only for regular expression validation,
     * not for JavaScript.</p>
     * 
     * <p>When this setting is false, the text fields are validated
     * only if they have non-zero length.
     * If true, merely visiting a field will validate it.</p>
     * 
     * <p>Default value is true.</p>
     * 
     * @param validateEmpty should empty text fields be validated
     * @since 0.4
     */
    public void setValidateEmptyValue(boolean validateEmpty) {
        getState().validateEmpty = validateEmpty;
    }

    /**
     * Sets whether the field is validated immediately after creation.
     * 
     * @deprecated renamed to {@link #setValidateInitialEmpty(boolean)}
     * 
     * @param validateEmpty true if the field is to be validated immediately, false if not
     */
    @Deprecated
    public void setValidateEmpty(boolean validateEmpty) {
        setValidateInitialEmpty(validateEmpty);
    }
    
    /**
     * Sets whether the field is validated immediately after creation.
     * 
     * <p>Normally, when this setting is false, the text fields are validated
     * only after the user types something in the input box. In some cases
     * it can be desirable to run the validation immediately when a new
     * field is created, even if it is empty. For example, if you use the
     * validation to display a character counter, you want to have it
     * displayed even when the text field is empty in the beginning.</p>
     * 
     * <p>Regardless of this setting, new text fields are validated
     * automatically if they are not empty.</p>
     * 
     * @param validateInitialEmpty should new empty text fields be validated
     */
    public void setValidateInitialEmpty(boolean validateInitialEmpty) {
        getState().validateInitialEmpty = validateInitialEmpty;
    }
    
}
