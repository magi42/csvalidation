package org.vaadin.csvalidation.demo;

import java.io.Serializable;

import org.vaadin.csvalidation.demo.examples.BeanValidationExample;
import org.vaadin.csvalidation.demo.examples.CounterExample;
import org.vaadin.csvalidation.demo.examples.JavaScriptEditorExample;
import org.vaadin.csvalidation.demo.examples.JavaScriptNumberExample;
import org.vaadin.csvalidation.demo.examples.JavaScriptPasswordExample;
import org.vaadin.csvalidation.demo.examples.JavaScriptPostCodeExample;
import org.vaadin.csvalidation.demo.examples.JavaScriptSSNExample;
import org.vaadin.csvalidation.demo.examples.RegExpEditorExample;
import org.vaadin.csvalidation.demo.examples.RegExpNumericExample;
import org.vaadin.csvalidation.demo.examples.RegExpSSNExample;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ItemStyleGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Demo application for client-side validation.
 * 
 * @author Marko Grönroos
 */
@Theme("csvalidationtheme")
public class CSValidationUI extends UI {
    private static final long serialVersionUID = -9126600498592169218L;
    
    class SingleViewNavigator extends Navigator {
		private static final long serialVersionUID = 5207014678028047648L;

		public SingleViewNavigator(UI ui, SingleComponentContainer container) {
    		super(ui, container);
		}
    	
    	public void setView(final View view) {
            removeView("");
            addProvider(new ViewProvider() {
				private static final long serialVersionUID = 3374088312674470282L;

				@Override
				public String getViewName(String viewAndParameters) {
					return "";
				}

				@Override
				public View getView(String viewName) {
					return view;
				}
            });
    	}
    	
    	@Override
        public void navigateTo(String navigationState) {
    		if (! navigationState.startsWith("/"))
    			navigationState = "/" + navigationState;
    		super.navigateTo(navigationState);
        }
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Client-Side Validation Demo");
        
        VerticalLayout root = new VerticalLayout();
        setContent(root);
        root.setSizeFull();
        
        HorizontalLayout titlebar = new HorizontalLayout();
        titlebar.addStyleName("titlebar");
        titlebar.setWidth("100%");
        Label title = new Label("Client-Side Validation Demo");
        title.addStyleName("title");
        titlebar.addComponent(title);
        Label doclinks = new Label("<a href=\"../\">Index</a> - " +
                                   "<a href=\"../doc/api/\">API Documentation</a>",
                                   ContentMode.HTML);
        doclinks.addStyleName("doclinks");
        titlebar.addComponent(doclinks);
        root.addComponent(titlebar);

        HorizontalLayout hor = new HorizontalLayout();
        hor.setSizeFull();
        root.addComponent(hor);
        root.setExpandRatio(hor, 1.0f);

        final Tree menu = new Tree();
        menu.setSizeUndefined();
        menu.setImmediate(true);
        
        final Panel menupanel = new Panel("Examples");
        final VerticalLayout menucontent = new VerticalLayout();
        menupanel.setContent(menucontent);
        menucontent.addComponent(menu);
        menupanel.setHeight("100%");
        menupanel.setWidth(null);
        hor.addComponent(menupanel);

        final Panel viewpanel = new Panel("");
        viewpanel.setSizeFull();
        hor.addComponent(viewpanel);
        hor.setExpandRatio(viewpanel, 1.0f);
        
        ExampleItem examples[] = {
                new ExampleItem("regexp", "Regular Expression Examples", null),
                new ExampleItem("regexp.numeric", "Numeric Input", RegExpNumericExample.class),
                new ExampleItem("regexp.ssn", "Social Security Number", RegExpSSNExample.class),
                new ExampleItem("javascript", "JavaScript Examples", null),
                new ExampleItem("javascript.number", "Numeric Input", JavaScriptNumberExample.class),
                new ExampleItem("javascript.ssn", "Social Security Number", JavaScriptSSNExample.class),
                new ExampleItem("javascript.postcode", "Postal Code", JavaScriptPostCodeExample.class),
                new ExampleItem("javascript.password", "Password", JavaScriptPasswordExample.class),
                new ExampleItem("javascript.counter", "Counter", null),
                new ExampleItem("javascript.counter.textfield", "Ordinary Text Field", CounterExample.class),
                new ExampleItem("javascript.counter.textarea", "Text Area", CounterExample.class),
                new ExampleItem("javascript.counter.limited", "Counter with Limit", CounterExample.class),
                new ExampleItem("javascript.counter.countdown", "Countdown", CounterExample.class),
                new ExampleItem("beanvalidation", "Bean Validation", null),
                new ExampleItem("beanvalidation.basic", "Basic Use", BeanValidationExample.class),
                new ExampleItem("utility", "Utilities", null),
                new ExampleItem("utility.javascript-editor", "JavaScript Editor", JavaScriptEditorExample.class),
                new ExampleItem("utility.regexp-editor", "Regular Expression Editor", RegExpEditorExample.class),
        };

        final SingleViewNavigator navigator = new SingleViewNavigator(this, viewpanel);
        navigator.setView(new ExampleView(examples));
        
        // Build the menu
        for (int i=0; i<examples.length; i++) {
            String itemid = examples[i].itemid;
            menu.addItem(itemid);
            menu.setItemCaption(itemid, examples[i].shortName);
            if (examples[i].parentid != null)
                menu.setParent(itemid, examples[i].parentid);
        }
        
        // Expand the menu
        for (int i=0; i<examples.length; i++) {
            if (examples[i].parentid == null)
                menu.expandItemsRecursively(examples[i].itemid);
            
            if (examples[i].closed)
                menu.collapseItem(examples[i].itemid);

            if (menu.getChildren(examples[i].itemid) == null)
                menu.setChildrenAllowed(examples[i].itemid, false);
        }
        
        navigator.addViewChangeListener(new ViewChangeListener() {
			private static final long serialVersionUID = 3894647372858641608L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
                String fragment = event.getParameters();
                if (fragment != null) {
                    menu.setValue(fragment);
                    
                    // Open the tree nodes leading to the example
                    for (Object parent = menu.getParent(fragment);
                         parent != null;
                         parent = menu.getParent(parent))
                        menu.expandItem(parent);
                }
			}
        });

        // Handle menu selection
        menu.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
                String selection = (String) event.getProperty().getValue();
            	navigator.navigateTo(selection);
			}
        });
        
        Tree.ItemStyleGenerator itemStyleGenerator = new Tree.ItemStyleGenerator() {
            private static final long serialVersionUID = -3231268865512947125L;

            public String getStyle(Tree source, Object itemId) {
                // Chapter title items do not contain a period
                if (!((String)itemId).contains("."))
                    return "chaptertitle";
                else
                    // Include the example ID in the menu item styles
    				return "item-" + itemId;
            }
        }; 
        menu.setItemStyleGenerator(itemStyleGenerator);
	}

    class ExampleItem implements Serializable {
        private static final long serialVersionUID = 8205608518115765928L;
        
        public String itemid;
        public String parentid;
        public String shortName;
        public String context;
        public Class<?>  exclass;
        public boolean closed;
        
        ExampleItem (String itemid, String shortName, Class<?> exclass) {
            if (itemid.substring(itemid.length()-1).equals("-")) {
                this.closed = true;
                itemid = itemid.substring(0, itemid.length()-1);
            } else
                closed = false;
            
            this.itemid    = itemid;
            this.shortName = shortName;
            this.exclass   = exclass;

            // Determine parent node and context
            int lastdot = itemid.lastIndexOf(".");
            if (lastdot != -1) {
                parentid = itemid.substring(0, lastdot);
                context = itemid.substring(lastdot+1);
            } else
                parentid = null;
        }
    }
}
