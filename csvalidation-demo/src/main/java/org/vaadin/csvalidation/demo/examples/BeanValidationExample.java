package org.vaadin.csvalidation.demo.examples;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

/**
 * Example of creating a content length counter with JavaScript.
 * 
 * @author Marko Grönroos
 */
public class BeanValidationExample extends CustomComponent implements CSValidationExample {
    private static final long serialVersionUID = 1380442481960261529L;

    /**
     * 
     */
    public void init (String context) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        
        if (context.equals("basic"))
            basic(layout);
        else
            layout.addComponent(new Label("Invalid context: " + context));
        
        setCompositionRoot(layout);
    }
    
    @SuppressWarnings("javadoc")
    // BEGIN-EXAMPLE: beanvalidation.basic
    /**
     * A bean to be validated
     */
    public class Planet implements Serializable {
        private static final long serialVersionUID = -4082722611892889974L;

        @NotNull
        @Size(max=20)
        @Pattern(regexp="[A-Z][a-z]+")
        private String name;
        
        private double distanceAU;
        
        @Min(0)
        @Max(100)
        private int moonCount;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        
        public double getDistanceAU() {
            return distanceAU;
        }
        public void setDistanceAU(double distanceAU) {
            this.distanceAU = distanceAU;
        }
        
        public int getMoonCount() {
            return moonCount;
        }
        public void setMoonCount(int moonCount) {
            this.moonCount = moonCount;
        }
    }
    
    void basic(VerticalLayout layout) {
        Planet planet = new Planet();
        BeanItem<Planet> item = new BeanItem<Planet>(planet);
        final FieldGroup binder = new BeanFieldGroup<Planet>(Planet.class);
        binder.setItemDataSource(item);
        FormLayout formlayout = new FormLayout();
        layout.addComponent(formlayout);
        
        for (Object pid : binder.getUnboundPropertyIds())
            formlayout.addComponent(binder.buildAndBind(pid));
        
        Button ok = new Button("OK");
        ok.addClickListener(new ClickListener() {
            private static final long serialVersionUID = -4782921039023045155L;

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    binder.commit();
                    Notification.show("OK");
                } catch (CommitException e) {
                    Notification.show("Failed", Type.WARNING_MESSAGE);
                }
            }
        });
        layout.addComponent(ok);
        
        // TODO What was I supposed to do here...
        // Validator ssvalidator = Validation.buildDefaultValidatorFactory().getValidator();
        // ssvalidator.
        // END-EXAMPLE: beanvalidation.basic
    }

    @Override
    public String getLongName() {
        return "Bean Validation Example";
    }

    @Override
    public String getExampleDescription() {
        return "Dummy example description";
    }
}
