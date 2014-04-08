package no.ntnu.fp.gui;

import javax.swing.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import no.ntnu.fp.model.Ecu;
import no.ntnu.fp.model.FactoryProject;
import no.ntnu.fp.model.SimpleEcu;
import no.ntnu.fp.model.Software;
import no.ntnu.fp.storage.EcuDbStorage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;

public class EcuPanel extends JPanel implements KeyListener, PropertyChangeListener, ActionListener, ItemListener, FocusListener {
	private static final long serialVersionUID = 1L;
	private FactoryProjectPanel fpPanel;
	private SimpleEcu model;
	private Object eventSource = null;
	private JFormattedTextField ecuTextField;
	private JFormattedTextField versionTextField;
	private JComboBox ecuCb;
	private JButton saveBtn;
	private JButton newBtn;
	private JLabel explanationLbl;
	private JLabel headerLbl;
	
	public EcuPanel(FactoryProjectPanel fpPanel) {
		this.fpPanel = fpPanel;
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(2, 2, 2, 2);
		constraints.insets = insets;
		constraints.anchor = GridBagConstraints.LINE_START;
		
		//Header:
		headerLbl = new JLabel("                           Bind ECU to Software");
		Font curFont = headerLbl.getFont();
		headerLbl.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 16));
		addGridBagComponent(headerLbl, 0, constraints, 1);
		
		String[] cmbItems = {};
		ecuCb = new JComboBox(cmbItems);
		ecuCb.setPrototypeDisplayValue("WWWWWWWWW");
		ecuCb.addItemListener(this);
		addGridBagComponent(ecuCb, 2, constraints, 1);
		
		ecuTextField = new JFormattedTextField();
        ecuTextField.addKeyListener(this);
        ecuTextField.setColumns(20);
        addGridBagLabel("ECU ID: ", 3, constraints);
        addGridBagComponent(ecuTextField, 3, constraints);
		
		versionTextField = new JFormattedTextField();
        versionTextField.addKeyListener(this);
        versionTextField.setColumns(20);
        addGridBagLabel("Software ID: ", 4, constraints);
        addGridBagComponent(versionTextField, 4, constraints);
        
        newBtn = new JButton("New");
        newBtn.addActionListener(this);
        addGridBagComponent(newBtn, 5, constraints, 0);
        
        saveBtn = new JButton("Save");
        saveBtn.addActionListener(this);
        addGridBagComponent(saveBtn, 5, constraints, 1);
        
        explanationLbl = new JLabel("Choose ECU in drop-down box, or make new by typing in the \"ECU ID\"-field.");
        addGridBagComponent(explanationLbl, 7, constraints, 0);
        
        setEditable(false);
	}
	private void clearTextFields() {
		ecuTextField.setText("");
		versionTextField.setText("");
	}
	public void collectSimpleEcus() {
		FactoryProject fp = this.fpPanel.getModel();
		if (fp == null || fp.getEcuCount() <= 0)
			return;
		for (int i=0; i < fp.getEcuCount(); i++) {
			this.ecuCb.addItem(String.valueOf(fp.getEcu(i).getEcuId()) + "(" + String.valueOf(fp.getEcu(i).getSwId()) + ")");
		}
		this.ecuCb.setSelectedIndex(0);
	}
	public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getSource() == versionTextField) {
            sourceChanged(versionTextField);
        } else if (evt.getSource() == ecuTextField) {
            sourceChanged(ecuTextField);
        } else {
            updatePanel(evt.getPropertyName());
        }
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

	private void addGridBagComponent(Component c, int row, GridBagConstraints constraints) {
		constraints.gridx = 1;
		constraints.gridy = row;
		constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		add(c, constraints);
	}

	private void addGridBagComponent(Component c, int row, GridBagConstraints constraints, int col) {
		constraints.gridx = col;
		constraints.gridy = row;
		constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		add(c, constraints);
	}
	public SimpleEcu getModel() {
		return this.model;
	}
	
	public void setEditable(boolean editable) {
		versionTextField.setEditable(editable);
		ecuTextField.setEditable(editable);
		saveBtn.setEnabled(editable);
		newBtn.setEnabled(!editable);
		ecuCb.setEnabled(!editable);
	}
	
	public void setModel(SimpleEcu e) {
		if (e != null) {
			if (model != null)
				model.removePropertyChangeListener(this);
			model = e;
			model.addPropertyChangeListener(this);
			updatePanel(null);
		}
	}
	
	private boolean propertyChanged(String changed, String prop, JTextField tf) {
        return (changed == null || (changed.equals(prop) && eventSource != tf && eventSource != tf.getDocument()));
	}
	public void updatePanel(String property) {
		if (model == null) {
            setEditable(false);
        }
        if (property == null) {
            int ecuID = (model != null ? model.getEcuId() : 0);
            ecuTextField.setText(ecuID != 0 ? String.valueOf(ecuID) : "");
            int versionID = (model != null ? model.getEcuId() : 0);
            versionTextField.setText(ecuID != 0 ? String.valueOf(versionID) : "");
        }
        else if (model != null && property.equals("openAct")) {
        	int version = (model != null ? model.getSwId() : 0);
            versionTextField.setText(version != 0 ? String.valueOf(version) : "");
            int ecuID = (model != null ? model.getEcuId() : 0);
            ecuTextField.setText(ecuID != 0 ? String.valueOf(ecuID) : "");
           // verCb.setSelectedIndex(fpPanel.getModel().getSoftwareIndex(model));
        }
 
        if (propertyChanged(property, SimpleEcu.SOFTWARE_PROPERTY_NAME, versionTextField)) {
            int version = (model != null ? model.getSwId() : 0);
            versionTextField.setText(version != 0 ? String.valueOf(version) : "");
        } else if (propertyChanged(property, SimpleEcu.ECUID_PROPERTY_NAME, ecuTextField)) {
            int ecuID = (model != null ? model.getEcuId() : 0);
            ecuTextField.setText(ecuID != 0 ? String.valueOf(ecuID) : "");
        } 
	}
	private void sourceChanged(Object source) {
		eventSource = source;
		if (model == null && source == ecuCb) {
			model = fpPanel.getModel().getEcu(ecuCb.getSelectedIndex());
			if (source == versionTextField) {
				if (versionTextField.getText() != null && !versionTextField.getText().equals(""))
					model.setSwId(Integer.parseInt(versionTextField.getText()));
	        } else if (source == ecuTextField) {
	        	if (ecuTextField.getText() != null && !ecuTextField.getText().equals(""))
	        		model.setEcuId(Integer.parseInt(ecuTextField.getText()));
	        } else if (source == ecuCb) {
	        	//model.setEcuId(Integer.parseInt((String)ecuCb.getSelectedItem()));
	        }
		}
		
		if (model == null) {
			eventSource = null;
            return;
        }
		
		//model = fpPanel.getModel().getEcu(ecuCb.getSelectedIndex());
		if (source == versionTextField) {
			if (versionTextField.getText() != null && !versionTextField.getText().equals(""))
				model.setSwId(Integer.parseInt(versionTextField.getText()));
        } else if (source == ecuTextField) {
        	if (ecuTextField.getText() != null && !ecuTextField.getText().equals(""))
        		model.setEcuId(Integer.parseInt(ecuTextField.getText()));
        } else if (source == ecuCb) {
        	model = fpPanel.getModel().getEcu(ecuCb.getSelectedIndex());
        	//model.setEcuId(Integer.parseInt((String)ecuCb.getSelectedItem()));
        	updatePanel(null);
        }
		eventSource = null;
    }
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == newBtn) {
			setEditable(true);
			SimpleEcu ecu = new SimpleEcu();
			fpPanel.getModel().addEcu(ecu);
			this.setModel(ecu);
		}
		else if (event.getSource() == saveBtn) {
			
			EcuDbStorage ecuDbStorage = new EcuDbStorage();
			String[] message = ecuDbStorage.addEcu(this.model, this);
			if (message[1].equals("ok")) {
				setEditable(false);
				FactoryProject fp = this.fpPanel.getModel();
				this.ecuCb.addItem(String.valueOf(fp.getLatestEcu().getEcuId()) + "(" + String.valueOf(fp.getLatestEcu().getSwId()) + ")");
				this.ecuCb.setSelectedIndex(ecuCb.getItemCount()-1);
			}
			
			this.getFpPanel().getNewVehiclePanel().getNwpecuPanel().getEcuVehPanel().updateEcuSoft();
			
			FactoryProjectPanel.setStatusBar(message[0]);
		}
		else {
			sourceChanged(event.getSource());
		}
	}
	public void itemStateChanged(ItemEvent event) {
	    sourceChanged(event.getSource());
	}
	public void focusLost(FocusEvent event) {
        sourceChanged(event.getSource());
    }

    public void focusGained(FocusEvent event) {}
	public FactoryProjectPanel getFpPanel() {
		return fpPanel;
	}
	public void setFpPanel(FactoryProjectPanel fpPanel) {
		this.fpPanel = fpPanel;
	}
	@Override
	public void keyPressed(KeyEvent arg0) {	}
	
	@Override
	public void keyReleased(KeyEvent evt) {
		char key = evt.getKeyChar();
		boolean isDigit = Character.isDigit(key);
		boolean isDefined = (key != KeyEvent.CHAR_UNDEFINED);
		
		if (evt.getSource() == versionTextField) {
			if(isDigit) {
				sourceChanged(versionTextField);
			} else if(isDefined) {
				versionTextField.setText("");
			}
        } else if (evt.getSource() == ecuTextField) {
        	if(isDigit) {
				sourceChanged(ecuTextField);
			} else if(isDefined) {
				ecuTextField.setText("");
			}
        }
	}
	@Override
	public void keyTyped(KeyEvent arg0) { }
}
