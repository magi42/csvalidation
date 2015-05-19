package org.vaadin.csvalidation.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.vaadin.csvalidation.demo.examples.BeanValidationExample;
import org.vaadin.csvalidation.demo.examples.CSValidationExample;
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
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
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
        final VerticalLayout viewlayout = new VerticalLayout();
        viewlayout.addStyleName("viewlayout");
        viewlayout.setSpacing(true);
        viewlayout.setMargin(true);
        viewpanel.setContent(viewlayout);
        hor.addComponent(viewpanel);
        hor.setExpandRatio(viewpanel, 1.0f);

        final ExampleItem examples[] = {
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

        getPage().addUriFragmentChangedListener(new UriFragmentChangedListener() {
            private static final long serialVersionUID = -6588416218607827834L;

            @Override
            public void uriFragmentChanged(UriFragmentChangedEvent event) {
                String fragment = event.getUriFragment();
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
        menu.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 8236533959795019956L;
    
            public void valueChange(ValueChangeEvent event) {
                viewlayout.removeAllComponents();
    
                String selection = (String) event.getProperty().getValue();
                
                // Find the example
                for (int i=0; i<examples.length; i++) {
                    ExampleItem example = examples[i];
                    if (example.itemid.equals(selection)) 
                        if (example.exclass == null) {
                            if (menu.hasChildren(example.itemid)) {
                                menu.select((String) menu.getChildren(example.itemid).toArray()[0]);
                            }
                        } else { // A leaf
                            // Display the component
                            Component component = null;
                            try {
                                // Instantiate and initialize the example
                                component = (Component) example.exclass.newInstance();
                                
                                // Display the description of the example.
                                CSValidationExample csexample = (CSValidationExample)component;
                                Label description = new Label(csexample.getExampleDescription(), ContentMode.HTML);
                                description.addStyleName("example-description");
                                viewlayout.addComponent(description);
                                
                                // Set example panel caption
                                viewpanel.setCaption(csexample.getLongName());
                           
                                CSValidationExample be = (CSValidationExample) component;
                                be.init(examples[i].context);
                                viewlayout.addComponent(component);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            
                            HorizontalLayout srcContainer = new HorizontalLayout();
                            srcContainer.addStyleName("sourcecontainer");
                            srcContainer.setSpacing(true);
                            srcContainer.setMargin(true);
    
                            // Read and, if found, display the source code fragment.
                            // Open the source file.
                            String srcname = "/" + example.exclass.getName().replace('.', '/') + ".java";
                            InputStream ins = getClass().getResourceAsStream(srcname);
                            if (ins != null) {
                                String srccode = readSourceFragment(ins, selection);
                                if (srccode != null)
                                    srcContainer.addComponent(new SourceListing("Source Code", srccode));
                                else
                                    System.out.println("Could not read '"+srcname+"'.");
                            }
                            
                            // Show associated CSS
                            File basedir = VaadinService.getCurrent().getBaseDirectory();
                            String csspath = basedir.toString()+"/VAADIN/themes/csvalidationtheme/styles.css";
                            File   cssfile = new File(csspath);
                            FileInputStream cssins;
                            String csscode = null;
                            try {
                                cssins = new FileInputStream(cssfile);
                                csscode = readSourceFragment(cssins, selection);
                            } catch (FileNotFoundException e) {
                                System.err.println("Unable to open styles.css: " + e.getMessage());
                            }
                            if (csscode != null)
                                srcContainer.addComponent(new SourceListing("CSS Code", csscode));
                            
                            if (srcContainer.iterator().hasNext())
                                viewlayout.addComponent(srcContainer);
                            
                            getPage().setUriFragment(example.itemid);
                        }
                }
            }
        });
        
        Tree.ItemStyleGenerator itemStyleGenerator = new Tree.ItemStyleGenerator() {
            private static final long serialVersionUID = -3231268865512947125L;

            public String getStyle(Tree source, Object itemId) {
                // Chapter title items do not contain a period
                if (!((String)itemId).contains("."))
                    return "chaptertitle";
                return null;
            }
        }; 
        menu.setItemStyleGenerator(itemStyleGenerator);
	}

    private String readSourceFragment(InputStream ins, String location) {
        // Read the proper source fragment from the stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
        StringBuffer src = new StringBuffer();
        int state = 0;
        try {
            for (String line = reader.readLine(); null != line; line = reader.readLine()) {
                line = line.replace("\t", "    ");
                
                switch (state) {
                case 0:
                    if (line.indexOf("BEGIN-EXAMPLE: " + location) != -1)
                        state = 1;
                    break;
                case 1:
                    if (line.indexOf("END-EXAMPLE: " + location) != -1)
                        state = 999;
                    else if (line.indexOf("serialVersionUID") != -1)
                        state = 3;
                    else {
                        src.append(line);
                        src.append("\n");
                    }
                    break;
                case 3: // Skip one line
                    if (line.indexOf("END-EXAMPLE: " + location) != -1)
                        state = 999;
                    else
                        state = 1;
                    break;
                default:
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read example source code because: " + e.getMessage());
            return null;
        }
        
        // Post-process
        String srccode = src.toString();
        if (srccode.length() == 0)
            return null;
        
        // Shorten to minimum indentation
        int maxindent = 999;
        String lines[] = srccode.split("\n");
        for (int i=0; i<lines.length; i++) {
            int spacecount = 0;
            for (; spacecount<lines[i].length(); spacecount++)
                if (lines[i].charAt(spacecount) != ' ')
                    break;
            if (spacecount < lines[i].length() && spacecount < maxindent)
                maxindent = spacecount;
        }
        if (maxindent < 999) {
            StringBuffer shortsrc = new StringBuffer();
            for (int i=0; i<lines.length; i++) {
                // The line can be shorter if it's all space
                if (lines[i].length() > maxindent)
                    shortsrc.append(lines[i].substring(maxindent));
                else
                    shortsrc.append(lines[i]);
                shortsrc.append("\n");
            }
            srccode = shortsrc.toString();
        }
            
        return srccode;
    }

    /** Source code listing. */
    public class SourceListing extends CustomComponent {
        private static final long serialVersionUID = -1864980807288021761L;

        /**
         * @param caption caption for the source listing box
         * @param srccode the source code
         */
        public SourceListing(String caption, final String srccode) {
            VerticalLayout layout = new VerticalLayout();
            
            HorizontalLayout titlebar = new HorizontalLayout();
            titlebar.setWidth("100%");
            Label captionLabel = new Label(caption);
            titlebar.addComponent(captionLabel);
            titlebar.setComponentAlignment(captionLabel, Alignment.BOTTOM_LEFT);
            layout.addComponent(titlebar);
            
            final Label srcview = new Label(srccode, ContentMode.PREFORMATTED);
            srcview.addStyleName("sourcecode");
            srcview.setWidth("-1");
            layout.addComponent(srcview);

            final NativeSelect mode = new NativeSelect();
            mode.addItem("Plain");
            mode.addItem("DocBook");
            mode.addItem("JavaDoc");
            mode.setValue("Plain");
            mode.setNullSelectionAllowed(false);
            mode.setMultiSelect(false);
            
            layout.addComponent(mode);
            layout.setComponentAlignment(mode, Alignment.MIDDLE_RIGHT);

            mode.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 2161991423208388790L;

                public void valueChange(ValueChangeEvent event) {
                    String selected = (String)mode.getValue();
                    
                    if (selected.equals("Plain")) {
                        srcview.setValue(srccode);
                    } else if (selected.equals("DocBook")) {
                        String trimmed = srccode.trim();
                        String dbcode = "<programlisting><?pocket-size 65% ?><![CDATA[" +
                                        trimmed + "]]></programlisting>\n";
                        srcview.setValue(dbcode);
                    } else if (selected.equals("JavaDoc")) {
                        String trimmed = "     * " + srccode.trim().replace("\n", "\n     * ");
                        String dbcode = "     * <pre>\n" +
                                        trimmed + "\n     * </pre>\n";
                        srcview.setValue(dbcode);
                    }
                }
            });
            mode.setImmediate(true);
            
            setCompositionRoot(layout);
        }
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
