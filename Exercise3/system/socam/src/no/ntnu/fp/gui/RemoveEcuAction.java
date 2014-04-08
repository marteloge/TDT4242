package no.ntnu.fp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import no.ntnu.fp.model.Ecu;
import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Vehicle;

public class RemoveEcuAction implements ActionListener {
	private NWPEcuPanel fpp;
	public RemoveEcuAction(NWPEcuPanel fpp) {
		super();
		this.fpp = fpp;
	}
	public void actionPerformed(ActionEvent evt) {
    	EcuListModel ecm = fpp.getModel();
    	Vehicle vh = ecm.getProject();
    	int index = fpp.getSelectedElement();
		Ecu ecu = vh.getEcu(index);
		vh.removeEcu(ecu);
	}
}
