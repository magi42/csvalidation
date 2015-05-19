package org.vaadin.csvalidation.demo.examples;

/**
 * A client-side validation example.
 * 
 * @author Marko Grönroos
 */
public interface CSValidationExample {
    
    /**
     * Initializes the example.
     * 
     * @param context the last part of exact example ID
     */
    public void init (String context);
    
    /**
     * Returns a long name for the example.
     * 
     * @return the long name
     */
    public String getLongName();
    
    /**
     * Returns a description of the example.
     * 
     * The description must currently be plain text (not HTML).
     * 
     * @return the example.
     */
    public String getExampleDescription();
}
