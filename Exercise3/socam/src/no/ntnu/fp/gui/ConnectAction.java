package no.ntnu.fp.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.derby.impl.sql.compile.LengthOperatorNode;

import no.ntnu.fp.net.co.GUIConnect;
import no.ntnu.fp.net.co.GUIServer;
import no.ntnu.fp.net.co.TestCoServer;

/**
 * This is a dummy implementation of the functionality for making a network connection with
 * another application.
 * 
 * @author Thomas &Oslash;sterlie
 *
 * @version $Revision: 1.6 $ - $Date: 2008-05-02 15:00:53 $
 */
public class ConnectAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/**
	 * Parent component.
	 */
	final private ProjectPanel projectPanel;
	final private FactoryProjectPanel fProjectPanel;
	/**
	 * Default constructor.  Initialises member variables.
	 * 
	 * @param projectPanel Parent component.
	 */
	public ConnectAction(ProjectPanel projectPanel) {
		this.projectPanel = projectPanel;
		this.fProjectPanel = null;
	}
	public ConnectAction(FactoryProjectPanel fProjectPanel) {
		this.fProjectPanel = fProjectPanel;
		this.projectPanel = null;
	}
   /**
    * Invoked when an action occurs.
    * 
    * @param e The action event.
    */
	public void actionPerformed(ActionEvent arg0) {
		//GARAGE->projectPanel
		if (this.fProjectPanel == null) { //sjekk for � avgj�re om vi er i Factory eller Garage
			String IPadr = JOptionPane.showInputDialog("Connect to Factory IP-adress:");
			if(IPadr != null){
				projectPanel.garageConnection = new GUIConnect(IPadr,projectPanel);
			}
		}
		//FACTORY->fProjectPanel
		else {
	     ( new Thread() {

				public void run() {
					fProjectPanel.factoryConnection = new GUIServer(fProjectPanel);
				}
			}).start();
		}
	}

}

