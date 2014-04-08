package no.ntnu.fp.gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * This is a dummy implementation of the functionality for breaking off a network connection with
 * another application.
 * 
 * @author Thomas &Oslash;sterlie
 *
 * @version $Revision: 1.6 $ - $Date: 2008-05-02 15:00:53 $
 */
class DisconnectAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/**
	 * Parent component.
	 */
	private ProjectPanel projectPanel;
	private FactoryProjectPanel fProjectPanel;
	
	/**
	 * Default constructor.  Initialises member variables.
	 *
	 */
	public DisconnectAction(ProjectPanel projectPanel) {
		this.projectPanel = projectPanel;
		this.fProjectPanel = null;
	}
	
	public DisconnectAction(FactoryProjectPanel fProjectPanel) {
		this.fProjectPanel = fProjectPanel;
		this.projectPanel = null;
	}
	
	
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param e The action event.
	 */
	public void actionPerformed(ActionEvent arg0) {
		//Garage->
		if (this.fProjectPanel == null) {
			if(projectPanel.garageConnection != null){
				projectPanel.garageConnection.closeConnection();
			}
		}
		//Factory
		else {
			if(fProjectPanel.factoryConnection != null){
				fProjectPanel.factoryConnection.stopServer();
			}
		}
	}
	
}

