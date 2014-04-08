package no.ntnu.fp.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import no.ntnu.fp.gui.*;
import no.ntnu.fp.model.Ecu;
import no.ntnu.fp.model.FactoryProject;
import no.ntnu.fp.model.Vehicle;
import no.ntnu.fp.storage.VehicleDbStorage;

public class NWPEcuPanel extends JPanel implements ListSelectionListener, ListDataListener {
	private static final long serialVersionUID = 1L;
	private NewVehiclePanel nvPanel;
	private JList ecuList;
	private EcuListModel model;
	private ListSelectionModel ecuSelection;
    private EcuVehPanel ecuVehPanel;
    private JButton addEcu;
	private JButton removeEcu;
	private JPanel container;
	private JPanel btnCont;
    
    public NWPEcuPanel(NewVehiclePanel nvPanel) {
    	this.nvPanel = nvPanel;
    	container = new JPanel();
    	container.setLayout(new GridBagLayout());
    	GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(2, 2, 2, 2);
		constraints.insets = insets;
		constraints.anchor = GridBagConstraints.LINE_START;
		
		GridBagConstraints constraints2 = new GridBagConstraints();
		Insets insets2 = new Insets(2, 2, 2, 2);
		constraints2.insets = insets2;
		constraints2.anchor = GridBagConstraints.LINE_START;
    	ecuList = new JList();
    	ecuList.setCellRenderer(new EcuCellRenderer());
    	ecuSelection = ecuList.getSelectionModel();
    	ecuSelection.addListSelectionListener(this);
    	JScrollPane listScrollPane = new JScrollPane(ecuList);
    	listScrollPane.setMinimumSize(new Dimension(250, 300));
    	constraints2.gridx = 0;
    	constraints2.gridy = 0;
    	constraints2.gridheight = 1;
		constraints2.gridwidth = 1;
		constraints2.ipadx = 75;
		constraints2.weightx = 1.0;
		
		container.add(listScrollPane, constraints2);
		container.add(Box.createHorizontalStrut(5));
		
    	ecuVehPanel = new EcuVehPanel(this);
    	ecuVehPanel.setMinimumSize(new Dimension(400,400));
    	constraints2.gridx = 1;
    	constraints2.ipadx = 0;
    	constraints2.fill = GridBagConstraints.HORIZONTAL;
		container.add(ecuVehPanel, constraints2);
		
    	btnCont = new JPanel();
    	btnCont.setLayout(new GridBagLayout());
    	
    	addEcu = new JButton("Add ECU");
    	addEcu.addActionListener(new AddEcuAction(this));
    	constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		btnCont.add(addEcu, constraints);

    	removeEcu = new JButton("Remove ECU");
    	removeEcu.addActionListener(new RemoveEcuAction(this));
    	constraints.gridx = 1;
    	btnCont.add(removeEcu, constraints);
    	
    	setLayout(new GridBagLayout());
		addGridBagComponent(container, 0, constraints);
		addGridBagComponent(btnCont, 1, constraints);
		
		setEditable(false);
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
   
	EcuListModel getModel() {
        return model;
    }
	
	public void setEditable(boolean editable) {
		addEcu.setEnabled(editable);
		removeEcu.setEnabled(editable);
		ecuVehPanel.setEditable(editable);
		
	}
    
    public void setModel(EcuListModel model) {
		this.model = model;
		ecuList.setModel(model);
		model.addListDataListener(this);
		listElementSelected((Ecu)model.getElementAt(0));
		ecuList.setSelectedIndex(0);
    }
    
	public int getSelectedElement() {
		return ecuList.getSelectedIndex();
	}

	public void setSelectedElement(int index) {
		ecuList.setSelectedIndex(index);
	}

	private void listElementSelected(Ecu e) {
		ecuVehPanel.setModel(e);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == ecuSelection) {
			listElementSelected((Ecu)ecuList.getSelectedValue());
		}
	}
	
	public EcuVehPanel getEcuVehPanel() {
		return ecuVehPanel;
	}
	public void setEcuVehPanel(EcuVehPanel ecuVehPanel) {
		this.ecuVehPanel = ecuVehPanel;
	}
	
	@Override
	public void intervalAdded(ListDataEvent e) {
		ecuList.setSelectedIndex(e.getIndex0());
	}
	
	@Override
	public void contentsChanged(ListDataEvent e) {
	}
	
	@Override
	public void intervalRemoved(ListDataEvent e) {
		int index = e.getIndex0();
		if (index == 0) 
			ecuList.setSelectedIndex(1);
		else if (index >= model.getSize()-1)
			ecuList.setSelectedIndex(ecuList.getSelectedIndex()-1);
		else
			ecuList.setSelectedIndex(index+1);
	}
	public NewVehiclePanel getNvPanel() {
		return nvPanel;
	}
	public void setNvPanel(NewVehiclePanel nvPanel) {
		this.nvPanel = nvPanel;
	}
}
