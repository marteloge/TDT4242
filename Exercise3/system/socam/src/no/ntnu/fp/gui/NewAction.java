/*
 * Created on Feb 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.ntnu.fp.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import no.ntnu.fp.model.Project;
import no.ntnu.fp.model.Person;
import no.ntnu.fp.storage.CreateGarageDB;
import no.ntnu.fp.storage.GarageDbStorage;

/**
 * Implements the action for creating a new group of persons.
 * 
 * @author Thomas Oslash;sterlie
 * 
 * @version $Revision: 1.6 $ - $Date: 2008-04-24 19:22:06 $
 */
public class NewAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/**
	 * Parent component.
	 */
	private ProjectPanel projectPanel;
	private FactoryProjectPanel fProjectPanel;
	
	/**
	 * Default constructor.  Initialises member variables.
	 * 
	 * @param projectPanel Parent component.
	 */
	public NewAction(ProjectPanel projectPanel) {
		super();
		putValue(Action.NAME, "New");
		this.projectPanel = projectPanel;
		this.fProjectPanel = null;
	}

	public NewAction(FactoryProjectPanel fProjectPanel) {
		super();
		putValue(Action.NAME, "New");
		this.fProjectPanel = fProjectPanel;
		this.projectPanel = null;
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param e The action event.
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (fProjectPanel == null) {
			CreateGarageDB activeDB = new CreateGarageDB();
			activeDB.executeStatements();
			GarageDbStorage dbs = new GarageDbStorage();
			projectPanel.setModel(new PersonListModel(dbs.load(), null));
			Project curProject = projectPanel.getModel().getProject();
			curProject.addPerson(new Person(curProject.getLargestCustId()+1));
		}
		else {
			//Implements this.
		}
	}

}
