package no.ntnu.fp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import no.ntnu.fp.model.Ecu;
import no.ntnu.fp.model.Vehicle;

public class AddEcuAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NWPEcuPanel fpp;
	public AddEcuAction(NWPEcuPanel fpp) {
		super();
		this.fpp = fpp;
	}
	public void actionPerformed(ActionEvent evt) {
    	EcuListModel ecm = fpp.getModel();
    	Vehicle vh = ecm.getProject();
    	vh.addEcu(new Ecu(vh.getLargestEcuId()+1));
    }
}
