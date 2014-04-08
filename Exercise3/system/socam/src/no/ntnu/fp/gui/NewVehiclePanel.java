package no.ntnu.fp.gui;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import no.ntnu.fp.model.*;
import no.ntnu.fp.storage.VehicleDbStorage;

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
import java.util.ArrayList;

public class NewVehiclePanel extends JPanel implements PropertyChangeListener, KeyListener, ActionListener, ItemListener, FocusListener  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FactoryProjectPanel fpPanel;
	private Vehicle model;
	
	
	private Object eventSource = null;
	
	private JLabel headerLbl;
	private JComboBox vehCb;
	private JFormattedTextField vehicleIDTextField;
	private JFormattedTextField seriesTextField;
	private JTextArea historyLogTextArea;
	private NWPEcuPanel nwpecuPanel;
	private JButton saveBtn;
	private JButton newBtn;
	private JButton removeBtn;


	public NewVehiclePanel(FactoryProjectPanel fpPanel) {
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
		headerLbl = new JLabel("                             Vehicle DB");
		Font curFont = headerLbl.getFont();
		headerLbl.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 16));
		addGridBagComponent(headerLbl, 0, constraints, 1);
		
		String[] cmbItems = {};
		vehCb = new JComboBox(cmbItems);
		vehCb.setPrototypeDisplayValue("WWWWWWWWW");
		vehCb.addItemListener(this);
		addGridBagComponent(vehCb, 2, constraints, 1);
		
		vehicleIDTextField = new JFormattedTextField();
		vehicleIDTextField.addKeyListener(this);
		vehicleIDTextField.setColumns(20);
        addGridBagLabel("Vehicle ID: ", 3, constraints);
        addGridBagComponent(vehicleIDTextField, 3, constraints);
        
        seriesTextField = new JFormattedTextField();
        seriesTextField.addKeyListener(this);
        seriesTextField.setColumns(20);
        addGridBagLabel("Series: ", 4, constraints);
        addGridBagComponent(seriesTextField, 4, constraints);
        
        historyLogTextArea = new JTextArea();
        historyLogTextArea.setWrapStyleWord(true);
        historyLogTextArea.setLineWrap(true);
        historyLogTextArea.setRows(6);
        historyLogTextArea.setColumns(20);
        historyLogTextArea.addKeyListener(this);
        JScrollPane historyScrollPane = new JScrollPane(historyLogTextArea);
        addGridBagLabel("History log: ", 5, constraints);
        addGridBagComponent(historyScrollPane, 5, constraints);
        
        nwpecuPanel = new NWPEcuPanel(this);
        addGridBagComponent(nwpecuPanel, 6, constraints, 0);
        
        JPanel btnConta = new JPanel();
        GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.insets = insets;
		constraints2.anchor = GridBagConstraints.LINE_START;
        btnConta.setLayout(new GridBagLayout());
        
        newBtn = new JButton("New");
        newBtn.addActionListener(this);
        constraints2.gridx = 0;
		constraints2.gridy = 0;
		constraints2.gridheight = 1;
		constraints2.gridwidth = 1;
		constraints2.fill = GridBagConstraints.NONE;
		constraints2.weightx = 1.0;
        btnConta.add(newBtn, constraints2);
        
        saveBtn = new JButton("Save");
        saveBtn.addActionListener(this);
        constraints2.gridx = 1;
        btnConta.add(saveBtn, constraints2);
        
        removeBtn = new JButton("Remove vehicle");
        removeBtn.addActionListener(this);
        constraints2.gridx = 2;
        btnConta.add(removeBtn, constraints2);
        
        addGridBagComponent(btnConta, 7, constraints, 0);
        
        setEditable(false);
        
	}
	
	public void collectVehicles() {
		FactoryProject fp = this.fpPanel.getModel();
		if (fp == null || fp.getVehicleCount() <= 0)
			return;
		for (int i=0; i < fp.getVehicleCount(); i++) {
			this.vehCb.addItem(fp.getVehicle(i).getVehicleID());
		}
		this.vehCb.setSelectedIndex(0);
	}
   
	public void propertyChange(PropertyChangeEvent evt) {
        updatePanel(evt.getPropertyName());    
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
	public void setModel(Vehicle v) {
		if (v != null) {
			if (model != null)
				model.removePropertyChangeListener(this);
			model = v;
			model.addPropertyChangeListener(this);
			updatePanel(null);
		}
	}
	public Vehicle getModel() {
		return model;
	}

	private void setEditable(boolean editable) {
		vehicleIDTextField.setEditable(editable);
		seriesTextField.setEditable(editable);
		historyLogTextArea.setEditable(editable);
		saveBtn.setEnabled(editable);
		newBtn.setEnabled(!editable);
		nwpecuPanel.setEditable(editable);
	}
	private boolean propertyChanged(String changed, String prop, JTextField tf) {
	        return (changed == null || (changed.equals(prop) && eventSource != tf && eventSource != tf.getDocument()));
	}
	private boolean propertyChanged(String changed, String prop, JTextArea ta) {
        return (changed == null || (changed.equals(prop) && eventSource != ta && eventSource != ta.getDocument()));
}
	
	public void updatePanel(String property) {
        if (model == null) {
            setEditable(false);
        }
        if (property == null) {
        	String vehId = (model != null ? model.getVehicleID() : null);
            vehicleIDTextField.setText(vehId != null ? vehId : "");
            String series = (model != null ? model.getSeries() : null);
            seriesTextField.setText(series != null ? series : "");
            String hLog = (model != null ? model.getHistoryLog(): null);
        	historyLogTextArea.setText(hLog != null ? hLog : "");
        	nwpecuPanel.setModel(new EcuListModel(model, null));
        }
        else if (model != null && property.equals("openAct")) {
        	String vehId = (model != null ? model.getVehicleID() : null);
            vehicleIDTextField.setText(vehId != null ? vehId : "");
            String series = (model != null ? model.getSeries() : null);
            seriesTextField.setText(series != null ? series : "");
            String hLog = (model != null ? model.getHistoryLog(): null);
        	historyLogTextArea.setText(hLog != null ? hLog : "");
        }
        
        if (propertyChanged(property, Vehicle.VEHICLEID_PROPERTY_NAME, vehicleIDTextField)) {
            String vehId = (model != null ? model.getVehicleID() : null);
            vehicleIDTextField.setText(vehId != null ? vehId : "");
        } else if (propertyChanged(property, Vehicle.HISTORYLOG_PROPERTY_NAME, historyLogTextArea)) {
        	String hLog = (model != null ? model.getHistoryLog(): null);
        	historyLogTextArea.setText(hLog != null ? hLog : "");
        } else if (propertyChanged(property, Vehicle.SERIES_PROPERTY_NAME, seriesTextField)) {
            String series = (model != null ? model.getSeries() : null);
            seriesTextField.setText(series != null ? series : "");
        }
	}
	private void sourceChanged(Object source) {
		eventSource = source;
		if (model == null && source == vehCb) {
			model = fpPanel.getModel().getVehicle(vehCb.getSelectedIndex());
			nwpecuPanel.setModel(new EcuListModel(model, null));
			if (source == vehicleIDTextField) {
				if (vehicleIDTextField.getText() != null && !vehicleIDTextField.getText().equals(""))
					model.setVehicleID(vehicleIDTextField.getText());
	        } else if (source == seriesTextField) {
	        	if (seriesTextField.getText() != null && !seriesTextField.getText().equals(""))
	        		model.setSeries(seriesTextField.getText());
	        } else if (source == historyLogTextArea) {
	        	if (historyLogTextArea.getText() != null && !historyLogTextArea.getText().equals(""))
	        		model.setHistoryLog(historyLogTextArea.getText());
	        } else if (source == vehCb) {
	        	//model.setVehicleID((String)vehCb.getSelectedItem());
	        }
		}
		
		if (model == null) {
			eventSource = null;
            return;
        }
		//model = fpPanel.getModel().getVehicle(Integer.parseInt((String)vehCb.getSelectedItem()));
		if (source == vehicleIDTextField) {
			if (vehicleIDTextField.getText() != null && !vehicleIDTextField.getText().equals(""))
				model.setVehicleID(vehicleIDTextField.getText());
        } else if (source == seriesTextField) {
        	if (seriesTextField.getText() != null && !seriesTextField.getText().equals(""))
        		model.setSeries(seriesTextField.getText());
        } else if (source == historyLogTextArea) {
        	if (historyLogTextArea.getText() != null && !historyLogTextArea.getText().equals(""))
        		model.setHistoryLog(historyLogTextArea.getText());
        } else if (source == vehCb) {
        	model = fpPanel.getModel().getVehicle(vehCb.getSelectedIndex());
        	updatePanel(null);
        	//model.setVehicleID((String)vehCb.getSelectedItem());
        }
		eventSource = null;
    }
	public void setHistoryLog() {
		this.model.setHistoryLog(historyLogTextArea.getText());
	}
	private boolean containsInvalidSoft() {
		for (Object o : model.getEcus()) {
			Ecu e = (Ecu)o;
			if (e.getSwId() == -1) {
				FactoryProjectPanel.setStatusBar("Invalid ECU, cannot add ECU without software!");
				return true;
			}
		}
		return false;
	}
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == newBtn) {
			setEditable(true);
			
			Vehicle newV = new Vehicle();
			fpPanel.getModel().addVehicle(newV);
			this.setModel(newV);
		}
		else if (event.getSource() == saveBtn) {
			if(vehicleIDTextField.getText().length() == 0) {
				FactoryProjectPanel.setStatusBar("A Vehicle ID must be entered");
			} else if(seriesTextField.getText().length() == 0) {
				FactoryProjectPanel.setStatusBar("A production series must be entered");
			} else {
				
				this.model.setHistoryLog(historyLogTextArea.getText());
				
				VehicleDbStorage vehicleDbStorage = new VehicleDbStorage();
				
				if(!containsInvalidSoft() && vehicleDbStorage.okToAdd(this.model)){
					setEditable(false);
					String message = vehicleDbStorage.addVehicle(this.model,this);
					ArrayList<String> messages = vehicleDbStorage.addEcus(this.model, this);
					
					for(int i = 0; i < messages.size(); i++){
						FactoryProjectPanel.setStatusBar(messages.get(i));
					}
					FactoryProject fp = this.fpPanel.getModel();
					this.vehCb.addItem(fp.getLatestVehicle().getVehicleID());
					this.vehCb.setSelectedIndex(vehCb.getItemCount()-1);
					FactoryProjectPanel.setStatusBar(message);
				}
			}
			
			
			
		}
       sourceChanged(event.getSource());
    }
	public void itemStateChanged(ItemEvent event) {
		sourceChanged(event.getSource());
	}
	public void focusLost(FocusEvent event) {
       sourceChanged(event.getSource());
    }
    public void focusGained(FocusEvent event) {}



	public NWPEcuPanel getNwpecuPanel() {
		return nwpecuPanel;
	}


	public void setNwpecuPanel(NWPEcuPanel nwpecuPanel) {
		this.nwpecuPanel = nwpecuPanel;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		char key = evt.getKeyChar();
		boolean isDigit = Character.isDigit(key);
		boolean isDefined = (key != KeyEvent.CHAR_UNDEFINED);
		
		if (evt.getSource() == vehicleIDTextField) {
			if(isDigit) {
				sourceChanged(vehicleIDTextField);
			} else if(isDefined) {
				vehicleIDTextField.setText("");
			}
        } else if (evt.getSource() == seriesTextField) {
        	if(isDigit) {
				sourceChanged(seriesTextField);
			} else if(isDefined) {
				seriesTextField.setText("");
			}
        } else if (evt.getSource() == historyLogTextArea) {
				sourceChanged(historyLogTextArea);
        }
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
    
}
