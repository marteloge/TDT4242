package no.ntnu.fp.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import no.ntnu.fp.model.Project;

public class SearchVehicleIDAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private ProjectPanel projectPanel;
	
	public SearchVehicleIDAction(ProjectPanel projectPanel) {
		super();
		putValue(Action.NAME, "Search vehicleid");
		this.projectPanel = projectPanel;
	}
	
	public void actionPerformed(ActionEvent e) {
		PersonListModel plm = projectPanel.getModel();
		Project project = plm.getProject();
		
		int val = Integer.parseInt(JOptionPane.showInputDialog("Enter vehicle id to search for:"));
		int index = project.getPersonIndex(val);
		if (index == -1) {
			ProjectPanel.setStatusBar("Search gave no results!");
			return;
		}
		else {
			ProjectPanel.setStatusBar("Search found the requested vehicle.");
			projectPanel.setSelectedElement(index);
		}
	}

}