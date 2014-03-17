package no.ntnu.fp.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Vehicle;
import no.ntnu.fp.swingutil.EmailFormatter;

/**
 * A panel for viewing and editing {@link no.ntnu.fp.model.Person} objects. The various
 * interfaces are implemented to react to sub-components and model changes.
 * 
 * @author Hallvard Tr�tteberg
 * @author Thomas &Oslash;sterlie 
 * 
 * @version $Revision: 1.9 $ - $Date: 2008-05-02 16:16:24 $
 */
public class PersonPanel extends JPanel
    implements KeyListener, PropertyChangeListener, ActionListener, ItemListener, FocusListener
{
	private static final long serialVersionUID = 1L;
	/**
     * The underlying data model is the {@link no.ntnu.fp.model.Person} class.
     */
    private Person model;

    /**
     * Text field for displaying and editing the person's name.
     */
    private JFormattedTextField nameTextField;
    
    /**
     * Text field for displaying and editing the person's email address.
     */
    private JFormattedTextField emailTextField;
    
    /**
     * Text field for displaying and editing the person's date of birth.
     */
    private JFormattedTextField streetTextField;
    
    /**
     * Text field for displaying and editing the person's city.
     */
    private JFormattedTextField cityTextField;
    
    private JButton sendEmailButton;
    
    private ProjectPanel projectPanel;
    
    /**
     * Used by {@link #propertyChanged(String, String, JTextField)} and
     * {@link #sourceChanged(Object)}.
     */
    private Object eventSource = null;

    /**
     * Constructor for objects of class PersonPanel
     */
    public PersonPanel(ProjectPanel ppanel) {
    	this.projectPanel = ppanel;
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
 
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Insets insets = new Insets(2, 2, 2, 2);
        constraints.insets = insets;
        constraints.anchor = GridBagConstraints.LINE_START;
        
        nameTextField = new JFormattedTextField();
        nameTextField.addKeyListener(this);
        nameTextField.setColumns(20);
        addGridBagLabel("Name: ", 0, constraints);
        addGridBagComponent(nameTextField, 0, constraints);
        
        emailTextField = new JFormattedTextField(new EmailFormatter());
        emailTextField.addKeyListener(this);
        emailTextField.setColumns(20);
        addGridBagLabel("Email: ", 1, constraints);
        addGridBagComponent(emailTextField, 1, constraints);

        cityTextField = new JFormattedTextField();
        cityTextField.addKeyListener(this);
        cityTextField.setColumns(20);
        addGridBagLabel("City: ", 2, constraints);
        addGridBagComponent(cityTextField, 2, constraints);
        
        streetTextField = new JFormattedTextField();
        streetTextField.addKeyListener(this);
        streetTextField.setColumns(20);
        addGridBagLabel("Street: ", 3, constraints);
        addGridBagComponent(streetTextField, 3, constraints);
        
        sendEmailButton = new JButton("Send E-Mail");
        sendEmailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	String msg = streetTextField.getText();
            	JOptionPane.showMessageDialog(projectPanel,msg);
            }
        });
        addGridBagComponent(sendEmailButton, 4, constraints);
        
        setEditable(false);
    }
    
    /**
     * Controls whether the components are editable or not.
     * 
     * @param editable whether to turn on or off the editable property
     */
    public void setEditable(boolean editable) {
        nameTextField.setEditable(editable);
        emailTextField.setEditable(editable);
        streetTextField.setEditable(editable);
        cityTextField.setEditable(editable);
        sendEmailButton.setEnabled(editable);
    }


    /**
     * This method is defined in {@link java.beans.PropertyChangeListener} and
     * is called when a property of an object listened to is changed.<P>
     * 
     * Note that <code>PersonPanel</code> listens to both the underlying model object,
     * i.e. {@link no.ntnu.fp.model.Person}, and to the value property of the 
     * {@link javax.swing.JFormattedTextField}, i.e. nameTextField, emailTextField, and
     * dateOfBirthTextField.
     * 
     * @see java.beans.PropertyChangeListener <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/beans/PropertyChangeListener.html">java.beans.PropertyChangeListener</a>
     * @see javax.swing.JFormattedTextField <a href="http://java.sun.com/j2se/1.4.2/docs/api/javax/swing/JFormattedTextField.html">javax.swing.JFormattedTextField</a>
     */
    public void propertyChange(PropertyChangeEvent evt) {
        updatePanel(evt.getPropertyName());
    }
    
    /**
     * Sets the <code>PersonPanel</code>'s underlying data model.
     * 
     * @param p The underlying data model.
     */
    public void setModel(Person p) {
    		if (p != null) {
    			if (model != null)
    				model.removePropertyChangeListener(this);
    			model = p;
    			model.addPropertyChangeListener(this);
    			updatePanel(null);
    		}
     }
    
    public Person getModel() {
    	return this.model;
    }
    
    /**
     * Utility method for adding a label to the GridBagLayout.
     * Labels are placed in column 0, occupy only one cell and do not stretch.
     * 
     * @param s the label
     * @param row the row
     * @param constraints the GridBagConstraints
     */
    private void addGridBagLabel(String s, int row, GridBagConstraints constraints) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridheight = 1;
        constraints.gridwidth  = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.0;
        add(new JLabel(s), constraints);
    }

    /**
     * Utility method for adding a component to the GridBagLayout.
     * Components are placed in column 1, occupy only one cell and stretches.
     * 
     * @param s the label
     * @param row the row
     * @param constraints the GridBagConstraints
     */
    private void addGridBagComponent(Component c, int row, GridBagConstraints constraints) {
        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.gridheight = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 1.0;
        add(c, constraints);
    }

    private boolean propertyChanged(String changed, String prop, JTextField tf) {
        return (changed == null || (changed.equals(prop) && eventSource != tf && eventSource != tf.getDocument()));
    }

    /**
     * Updates the sub-components according to the underlying model.
     * 
     * @param property the name of the property that triggered the update (may be null)
     */
    private void updatePanel(String property) {
        if (model == null) {
            setEditable(false);
        }
 
        if (propertyChanged(property, Person.NAME_PROPERTY_NAME, nameTextField)) {
            String name = (model != null ? model.getName() : "");
            nameTextField.setText(name != null ? name : "");
        }

        if (propertyChanged(property, Person.EMAIL_PROPERTY_NAME, emailTextField)) {
            String email = (model != null ? model.getEmail() : "");
            emailTextField.setValue(email);
        }

        if (propertyChanged(property, Person.CITY_PROPERTY_NAME, cityTextField)) {
            String city = (model != null ? model.getCity() : null);
            cityTextField.setValue(city);
        }
        
        if (propertyChanged(property, Person.STREET_PROPERTY_NAME, streetTextField)) {
            String street = (model != null ? model.getStreet() : null);
            streetTextField.setValue(street);
        }
        
        if (propertyChanged(property, Person.VEHICLEID_PROPERTY_NAME, null)) {
            String vehicleID = (model != null ? model.getVehicleID() : null);
            projectPanel.getVehiclePanel().setModel(new Vehicle());
            projectPanel.getVehiclePanel().getModel().setVehicleID(vehicleID);
            projectPanel.getVehiclePanel().removeListItms();
            projectPanel.getVehiclePanel().updatePanel(null);
        }
    }

    /**
     * Handles changes in sub-components,
     * that should be propagated to the underlying Person model object.
     * 
     * @param source the source of the change, typical a sub-component
     */
    private void sourceChanged(Object source) {
        if (model == null) {
            return;
        }
        eventSource = source;
        if (source == nameTextField) {
            model.setName(nameTextField.getText());
        } else if (source == emailTextField) {
        	try {
				emailTextField.commitEdit();
				model.setEmail((String)emailTextField.getValue());
			} catch (ParseException e) {
				// not valid email
			}
        } else if (source == streetTextField) {
            model.setStreet((String)streetTextField.getText());
        } else if (source == cityTextField) {
            model.setCity((String)cityTextField.getText());
        }
       
        eventSource = null;
    }

    /**
     * This method is defined in ActionListener and
     * is called when ENTER is typed in a JTextField
     * (or a JButton is hit or JMenuItem is selected, but that's not relevant for PersonPanel)
     * 
     * @param event the ActionEvent describing the action
     */
    public void actionPerformed(ActionEvent event) {
        sourceChanged(event.getSource());
    }

    /**
     * This method is defined in ItemListener and
     * is called when an item is selected in a JComboBox
     * (or a JSlider or JSpinner, but that's not relevant for PersonPanel)
     * 
     * @param event the ItemEvent describing the selection
     */
    public void itemStateChanged(ItemEvent event) {
        sourceChanged(event.getSource());
    }

    /**
     * This method is defined in FocusListener and
     * is called when a JTextField (or in fact any component) loses the keyboard focus.
     * This normally happens when the user TABs out of the text field or clicks outside it.
     * 
     * @param event the FocusEvent describing what happened
     */
    public void focusLost(FocusEvent event) {
        sourceChanged(event.getSource());
    }

    public void focusGained(FocusEvent event) {}

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent evt) {
		sourceChanged(evt.getSource());
	}

	@Override
	public void keyTyped(KeyEvent e) {	}
}