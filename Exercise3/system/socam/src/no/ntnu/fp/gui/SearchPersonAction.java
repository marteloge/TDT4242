package no.ntnu.fp.gui;
import java.awt.event.ActionEvent;

import javax.swing.*;

import no.ntnu.fp.model.Project;

public class SearchPersonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private ProjectPanel projectPanel;
	
	public SearchPersonAction(ProjectPanel projectPanel) {
		super();
		putValue(Action.NAME, "Search customer");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		this.projectPanel = projectPanel;
	}
	
	public void actionPerformed(ActionEvent e) {
		PersonListModel plm = projectPanel.getModel();
		Project project = plm.getProject();
		
		String val = JOptionPane.showInputDialog("Enter part of name to search for:");
		java.util.ArrayList<Integer> results = project.getPersonIndex(val);
		if (results.size() == 0) {
			ProjectPanel.setStatusBar("Search gave no results!");
			return;
		}
		else {
			ProjectPanel.setStatusBar("Search gave " + results.size() + " hit(s). Displaying first hit.");
			projectPanel.setSelectedElement(results.get(0));
		}
	}

}
