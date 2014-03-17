package no.ntnu.fp.gui;

import java.awt.Component;
import java.awt.Dimension;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import no.ntnu.fp.model.Ecu;
import no.ntnu.fp.model.Vehicle;
import no.ntnu.fp.model.XMLClient;

public class VehiclePanel extends JPanel implements KeyListener, PropertyChangeListener, ActionListener, ItemListener, FocusListener {
	private static final long serialVersionUID = 1L;
	//final pga tr�ding
	final private ProjectPanel projectPanel;
	private Object eventSource = null;
	public Vehicle model;
	private JFormattedTextField serialTextField;
	private JFormattedTextField seriesTextField;
	private JTextArea historyLogTextArea;
	private JList ecuSerial;
	private DefaultListModel listModel;
	private JButton getInfoButton;
	private JButton updateCarButton;
	private JButton uploadInfoButton;
	
	public VehiclePanel(ProjectPanel ppanel) {
		setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createEtchedBorder(),
	            BorderFactory.createEmptyBorder(5, 5, 5, 5)
	            ));
		this.projectPanel = ppanel;
		setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Insets insets = new Insets(2, 2, 2, 2);
        constraints.insets = insets;
        constraints.anchor = GridBagConstraints.LINE_START;
        
        serialTextField = new JFormattedTextField();
        serialTextField.addKeyListener(this);
        serialTextField.setColumns(20);
        addGridBagLabel("Vehicle serial no: ", 0, constraints);
        addGridBagComponent(serialTextField, 0, constraints);
        
        seriesTextField = new JFormattedTextField();
        seriesTextField.setColumns(20);
        seriesTextField.setEditable(false);
        addGridBagLabel("Series: ", 1, constraints);
        addGridBagComponent(seriesTextField, 1, constraints);
        
        historyLogTextArea = new JTextArea();
        historyLogTextArea.setWrapStyleWord(true);
        historyLogTextArea.setLineWrap(true);
        historyLogTextArea.setRows(7);
        historyLogTextArea.setColumns(20);
        historyLogTextArea.addKeyListener(this);
        JScrollPane historyScrollPane = new JScrollPane(historyLogTextArea);
        addGridBagLabel("History log: ", 2, constraints);
        addGridBagComponent(historyScrollPane, 2, constraints);
        
        listModel = new DefaultListModel();
        //temp. to show how its supposed to look like
        listModel.addElement("");
        
        ecuSerial = new JList(listModel);
        ecuSerial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ecuSerial.setSelectedIndex(0);
        ecuSerial.setVisibleRowCount(5);
        ecuSerial.addPropertyChangeListener(this);
        JScrollPane listScrollPane = new JScrollPane(ecuSerial);
        listScrollPane.setPreferredSize(new Dimension(223,80));
        addGridBagLabel("ECU-ID: installed, newest?: ", 3, constraints);
        addGridBagComponent(listScrollPane, 3, constraints);
        
        getInfoButton = new JButton("Get vehicle info");
        getInfoButton.addActionListener(this);
        addGridBagComponent(getInfoButton, 4, constraints, 1);
        
        updateCarButton = new JButton("Update car");
        updateCarButton.addActionListener(this);
        addGridBagComponent(updateCarButton, 5, constraints, 1);
        
        uploadInfoButton = new JButton("Upload new info");
        uploadInfoButton.addActionListener(this);
        addGridBagComponent(uploadInfoButton, 6, constraints, 1);
        
        showButtons(false);
  	}
	public void fillVehList() {
		listModel.removeAllElements();
		for (int i = 0; i < model.getEcuCount(); i++) {
			Ecu e = model.getEcu(i);
			String tmpItem = String.valueOf(e.getEcuId()) + ": " + String.valueOf(e.getSwId()) + "." + String.valueOf(e.getSubSwId()) + ", " + (e.isNewest() ? "YES": "NO");
			listModel.addElement(tmpItem);
		}
	}
	public void fillVehList(boolean b) {
		listModel.removeAllElements();
		for (int i = 0; i < model.getEcuCount(); i++) {
			Ecu e = model.getEcu(i);
			String tmpItem = String.valueOf(e.getEcuId()) + ": " + String.valueOf(e.getSwId()) + "." + String.valueOf(e.getSubSwId()) + ", " + (b ? "YES": "NO");
			listModel.addElement(tmpItem);
		}
	}
	public void showButtons(boolean show) {
		updateCarButton.setVisible(show);
		uploadInfoButton.setVisible(show);
		
	}
	
	public void setEditable(boolean editable) {
		serialTextField.setEditable(editable);
		historyLogTextArea.setEditable(editable);
		ecuSerial.setEnabled(editable);
		getInfoButton.setEnabled(editable);
		updateCarButton.setEnabled(editable);
		uploadInfoButton.setEnabled(editable);
	}
	
	public void propertyChange(PropertyChangeEvent evt)
    {
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
	
	public Vehicle getModel() {
		return this.model;
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
	private boolean propertyChanged(String changed, String prop, JTextField tf) {
	        return (changed == null || (changed.equals(prop) && eventSource != tf && eventSource != tf.getDocument()));
	}
	public void removeListItms() {
		this.listModel.removeAllElements();
	}
	public void updatePanel(String property) {
        if (model == null) {
            setEditable(false);
        }
        if (property == null) {
        	seriesTextField.setText(model.getSeries());
        	historyLogTextArea.setText(model.getHistoryLog());
        }
        if (propertyChanged(property, Vehicle.VEHICLEID_PROPERTY_NAME, serialTextField)) {
            String vehicleID = (model != null ? model.getVehicleID() : null);
            serialTextField.setText(vehicleID != null ? vehicleID : "");
        }
//        if (propertyChanged(property, Vehicle.SERIES_PROPERTY_NAME, seriesTextField)) {
//            String series = (model != null ? model.getSeries() : null);
//            serialTextField.setText(series != null ? series : "");
//        }
	}
	private void sourceChanged(Object source) {
        if (model == null) {
            return;
        }
        eventSource = source;
        if (source == serialTextField) {
        	projectPanel.getPersonPanel().getModel().setVehicleID(serialTextField.getText());
        	model.setVehicleID(serialTextField.getText());
        	showButtons(false);
			listModel.removeAllElements();
			updateGuiReceived(null);
        } else if(source == historyLogTextArea) {
        	model.setHistoryLog(historyLogTextArea.getText());
        }
        eventSource = null;
    }
	public static void updateGuiReceived(Vehicle tmpV) {
		if(tmpV != null) {
			ProjectPanel.statVehPanel.model.setHistoryLog(tmpV.getHistoryLog());
			ProjectPanel.statVehPanel.model.setSeries(tmpV.getSeries());
			ProjectPanel.statVehPanel.model.setEcus(tmpV.getEcus());
			ProjectPanel.statVehPanel.fillVehList();			
		} else {
			ProjectPanel.statVehPanel.model.setHistoryLog("");
			ProjectPanel.statVehPanel.model.setSeries("");
			ProjectPanel.statVehPanel.model.setEcus(new ArrayList<Ecu>());
		}
		ProjectPanel.statVehPanel.updatePanel(null);
	}
	
	private void getInfo() {
		( new Thread() {
		   public void run() {
			   XMLClient xc = new XMLClient();
		
				//Garage sender VehicleID forespørsel
				projectPanel.garageConnection.sendString(xc.vehicleIDToXml(projectPanel.vehiclePanel.model));
				
	        	//Klient venter aVehicleXML fra Factory
	        	String aVehicleXML = projectPanel.garageConnection.getString();
	        	ProjectPanel.setStatusBar("Fresh Vehicle info is here");
	        	Vehicle v = xc.toVehicleFromServer(aVehicleXML);
	        	
	        	VehiclePanel.updateGuiReceived(v);
		   }
		}).start();
	}
	
	private void uploadInfo() {
		//Lage aVehicleXML
		XMLClient xc = new XMLClient();
		String msg = xc.vehicleObjToXML(model);
		//Send oppdateringer til Factory
		projectPanel.garageConnection.sendString(msg);
		ProjectPanel.setStatusBar("Updated Vehicle-info sent to Factory");
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == getInfoButton) {
			showButtons(true);
			getInfo();
		} else if (event.getSource() == updateCarButton) {
			updateEcus();
			uploadInfo();
		} else if (event.getSource() == uploadInfoButton) {
			uploadInfo();
		}
		sourceChanged(event.getSource());
    }
	private void updateEcus() {
		for (Object o : this.model.getEcus()) {
			Ecu e = (Ecu)o;
			if (!e.isNewest()) {
				e.setSubSwId(e.getNewestSub());
			}
			
		}
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		Date det = new Date();
		
		String newHistory = this.model.getHistoryLog() + "\n" + df.format(det) + ": " + "ECUs upgraded.";
		this.model.setHistoryLog(newHistory);
		fillVehList(true);
		updatePanel(null);
	}
	public void itemStateChanged(ItemEvent event) {
	        sourceChanged(event.getSource());
	}
	public void focusLost(FocusEvent event) {
        sourceChanged(event.getSource());
    }

    public void focusGained(FocusEvent event) {}

	public JFormattedTextField getSerialTextField() {
		return serialTextField;
	}

	public void setSerialTextField(JFormattedTextField serialTextField) {
		this.serialTextField = serialTextField;
	}
	@Override
	public void keyPressed(KeyEvent arg0) {}
	@Override
	public void keyReleased(KeyEvent evt) {
		sourceChanged(evt.getSource());
	}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
