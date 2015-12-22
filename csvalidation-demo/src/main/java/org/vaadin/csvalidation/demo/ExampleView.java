package org.vaadin.csvalidation.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.vaadin.csvalidation.demo.CSValidationUI.ExampleItem;
import org.vaadin.csvalidation.demo.examples.CSValidationExample;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

public class ExampleView extends VerticalLayout implements View {
	private static final long serialVersionUID = 7678137608344460283L;

	ExampleItem examples[];
	VerticalLayout viewlayout = new VerticalLayout();
	
	public ExampleView(ExampleItem examples[]) {
		this.examples = examples;

        viewlayout.addStyleName("viewlayout");
        viewlayout.setSpacing(false);
        viewlayout.setMargin(true);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
        removeAllComponents();
        viewlayout.removeAllComponents();

        String selection = event.getParameters();

        // Find the example
        for (int i=0; i<examples.length; i++) {
            ExampleItem example = examples[i];
            if (example.itemid.equals(selection)) 
                if (example.exclass == null) {
                	/*
                    if (menu.hasChildren(example.itemid)) {
                        menu.select((String) menu.getChildren(example.itemid).toArray()[0]);
                    }
                    */
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
                        addComponent(description);
                        
                        // Set example panel caption
                        // TODO viewpanel.setCaption(csexample.getLongName());
                   
                        CSValidationExample be = (CSValidationExample) component;
                        be.init(examples[i].context);
                        component.addStyleName("example");
                        viewlayout.addComponent(component);
                        addComponent(viewlayout);
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
                    	addComponent(srcContainer);
                }
        }
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
}
