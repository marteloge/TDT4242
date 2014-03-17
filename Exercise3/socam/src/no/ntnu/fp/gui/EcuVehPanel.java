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

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import no.ntnu.fp.model.Ecu;
import no.ntnu.fp.model.Vehicle;
import no.ntnu.fp.storage.EcuDbStorage;

public class EcuVehPanel extends JPanel implements KeyListener, PropertyChangeListener, ActionListener, ItemListener, FocusListener {
	private Ecu model;
	private JFormattedTextField ecuIdTextField;
	private JFormattedTextField swIdTextField;
	private JFormattedTextField subSwIdTextField;
	private NWPEcuPanel nwpEcuPanel;
	private Object eventSource = null;
	
	public EcuVehPanel(NWPEcuPanel nvp) {
		this.nwpEcuPanel = nvp;
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(2, 2, 2, 2);
		constraints.insets = insets;
		constraints.anchor = GridBagConstraints.LINE_START;

		ecuIdTextField = new JFormattedTextField();
		ecuIdTextField.addKeyListener(this);
		ecuIdTextField.setColumns(20);
		addGridBagLabel("ECU ID: ", 0, constraints);
		addGridBagComponent(ecuIdTextField, 0, constraints);
		
		swIdTextField = new JFormattedTextField();
		swIdTextField.setColumns(20);
		swIdTextField.setEditable(false);
		addGridBagLabel("Major SW version: ", 1, constraints);
		addGridBagComponent(swIdTextField, 1, constraints);
		
		subSwIdTextField = new JFormattedTextField();
		subSwIdTextField.setColumns(20);
		subSwIdTextField.setEditable(false);
		addGridBagLabel("Minor SW version: ", 2, constraints);
		addGridBagComponent(subSwIdTextField, 2, constraints);
		
		setEditable(false);
	}
	public void setEditable(boolean editable) {
		this.ecuIdTextField.setEditable(editable);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() == ecuIdTextField) {
			System.out.println("property change - " + ecuIdTextField.getText());
		}
        updatePanel(evt.getPropertyName());
    }
	
	public void setModel(Ecu e) {
		if (e != null) {
			if (model != null)
				model.removePropertyChangeListener(this);
			model = e;
			model.addPropertyChangeListener(this);
			updatePanel(null);
		}
	}

	public Ecu getModel() {
		return this.model;
	}
	
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
    
    private void updatePanel(String property) {
        if (model == null) {
            setEditable(false);
        }
 
        if (propertyChanged(property, Ecu.ECUID_PROPERTY_NAME, ecuIdTextField)) {
            String name = (model != null ? String.valueOf(model.getEcuId()) : "");
            ecuIdTextField.setText(name != null ? name : "");
        }

        if (propertyChanged(property, Ecu.SOFTWARE_PROPERTY_NAME, swIdTextField)) {
            String email = (model != null ? String.valueOf(model.getSwId()) : "");
            swIdTextField.setValue(email);
        }

        if (propertyChanged(property, Ecu.SUBSOFTWARE_PROPERTY_NAME, subSwIdTextField)) {
            String city = (model != null ? String.valueOf(model.getSubSwId()) : null);
            subSwIdTextField.setValue(city);
        }
    }
    public void updateEcuSoft() {
    	EcuDbStorage edbs = new EcuDbStorage();
    	Vehicle v = this.nwpEcuPanel.getNvPanel().getModel();
    	for (Object o: v.getEcus()) {
    		Ecu e = (Ecu)o;
    		int[] ret = edbs.getEcuSoft(e);
    		e.setSwId(ret[0]);
    		e.setSubSwId(ret[1]);
    	}
    	updatePanel(null);
    }
    
    private void sourceChanged(Object source) {
        if (model == null) {
            return;
        }
        
        eventSource = source;
        
        if (source == ecuIdTextField) {
			if (ecuIdTextField.getText().length() != 0) {
        		try {
        			int tmpEcuId = Integer.parseInt(ecuIdTextField.getText());
        			EcuDbStorage edbs = new EcuDbStorage();
        			model.setEcuId(tmpEcuId);
        			int[] ret = edbs.getEcuSoft(this.model);
        			model.setSwId(ret[0]);
        			model.setSubSwId(ret[1]);
        		} catch (NumberFormatException e) {
        			// do nothing
        		} finally {
        			updatePanel(null);        			
        		}
        	}
        }
        
        eventSource = null;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        sourceChanged(event.getSource());
    }
    
    @Override
    public void itemStateChanged(ItemEvent event) {
        sourceChanged(event.getSource());
    }
    
    @Override
    public void focusLost(FocusEvent event) {
        sourceChanged(event.getSource());
    }

    @Override
    public void focusGained(FocusEvent event) {}

	@Override
	public void keyPressed(KeyEvent arg0) { }
	@Override
	public void keyReleased(KeyEvent evt) {
		sourceChanged(ecuIdTextField);
	}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
