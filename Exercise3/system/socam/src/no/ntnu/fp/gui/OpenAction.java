package no.ntnu.fp.gui;

import java.awt.event.ActionEvent;
import java.net.URL;

import no.ntnu.fp.model.*;
import no.ntnu.fp.storage.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import no.ntnu.fp.storage.FileStorage;
import no.ntnu.fp.swingutil.FPFileFilter;

/**
 * Implements the application's open command.
 * 
 * @author Hallvard Tr�tteberg
 * @author Thomas &Oslash;sterlie
 * @author Rune Molden
 * 
 * @version $Revision: 1.17 $ - $Date: 2008-05-02 08:38:42 $
 */
public class OpenAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/**
	 * The parent component.
	 */
    private ProjectPanel projectPanel;
    private FactoryProjectPanel fProjectPanel;
    /**
     * Default constructor.  Initialises all member variables.
     * 
     * @param projectPanel Parent component.
     */
    public OpenAction(ProjectPanel projectPanel) {
        super();
        putValue(Action.NAME, "Log on");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        this.projectPanel = projectPanel;
        this.fProjectPanel = null;
    }
    public OpenAction(FactoryProjectPanel fProjectPanel) {
		super();
		putValue(Action.NAME, "Log on");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		this.fProjectPanel = fProjectPanel;
		this.projectPanel = null;
	}
    /**
     * Invoked when an action occurs.
     * 
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
    	if (this.fProjectPanel == null) {
    		GarageDbStorage dbs = new GarageDbStorage();
    		projectPanel.setModel(new PersonListModel(dbs.load(), null));
    		ProjectPanel.setStatusBar("Load OK.");
    	}
    	else {
    		
    		FactoryDbStorage fdbs = new FactoryDbStorage();
    		fProjectPanel.setModel(fdbs.openFactoryProject());
    		
    		if (fProjectPanel.getModel().getVehicleCount() <= 0) {
    			FactoryProject curProj = fProjectPanel.getModel();
    			curProj.addVehicle(new Vehicle());
    			curProj.addEcu(new SimpleEcu(1));
    		}
    		fProjectPanel.getNewVehiclePanel().collectVehicles();
    		fProjectPanel.getNewVehiclePanel().setModel(fProjectPanel.getModel().getVehicle(0));
    		fProjectPanel.getNewVehiclePanel().getNwpecuPanel().setModel(new EcuListModel(fProjectPanel.getModel().getVehicle(0),null));
    		
    		fProjectPanel.getSoftwarePanel().collectSoftware();
    		fProjectPanel.getSoftwarePanel().setModel(fProjectPanel.getModel().getSoftware(0));
    		
    		fProjectPanel.getEcuPanel().collectSimpleEcus();
    		fProjectPanel.getEcuPanel().setModel(fProjectPanel.getModel().getEcu(0)); 
    		
    		FactoryProjectPanel.setStatusBar("Load OK.");
    		
//    		Ecu ec1 = new Ecu(45,23,41);
//    		Ecu ec2 = new Ecu(46,27,50);
//    		Ecu ec3 = new Ecu(47,43,10);
//    		Ecu ec4 = new Ecu(48,29,50);
//    		Software sw1 = new Software(23,41,"http://www.volvo.se/23.41.zip");
//    		Software sw2 = new Software(27,50,"http://www.volvo.se/27.50.zip");
//    		Software sw3 = new Software(43,10,"http://www.volvo.se/43.10.zip");
//    		Software sw4 = new Software(29,50,"http://www.volvo.se/29.50.zip");
//    		java.util.ArrayList eculst = new java.util.ArrayList();
//    		eculst.add(ec1);
//    		eculst.add(ec2);
//    		eculst.add(ec3);
//    		eculst.add(ec4);
//    		Vehicle vc = new Vehicle("4123","23.2.08 - Soft.upgr",eculst,"A5");
//    		FactoryProject fp = new FactoryProject();
//    		fp.addVehicle(vc);
//    		fp.addEcu(ec1);
//    		fp.addEcu(ec2);
//    		fp.addEcu(ec3);
//    		fp.addEcu(ec4);
//    		fp.addSoftware(sw1);
//    		fp.addSoftware(sw2);
//    		fp.addSoftware(sw3);
//    		fp.addSoftware(sw4);
//    		
//    		fProjectPanel.setModel(fp);
//    		fProjectPanel.getNewVehiclePanel().getNwpecuPanel().setModel(new EcuListModel(fProjectPanel.getModel().getVehicle(0),null));
//    		fProjectPanel.getSoftwarePanel().collectSoftware();
//    		fProjectPanel.getSoftwarePanel().setModel(fp.getSoftware(0));
//    		
//    		fProjectPanel.getEcuPanel().setModel(fp.getEcu(0));
    		
    	}
      /** OLD XML FILE-OPEN CODE
    	try {
        String urlString = getFileUrlFromUser();
        if (urlString == null || urlString.length() == 0) {
	  return;
        }        
        loadGroupFromFile(urlString);   
      } catch (java.net.MalformedURLException mue) {
	JOptionPane.showMessageDialog(projectPanel, "No such file.");
	mue.printStackTrace();
      } catch (java.text.ParseException pe) {
	JOptionPane.showMessageDialog(projectPanel, "Wrong file format.");
	pe.printStackTrace();
      } catch (Exception anException) {
	JOptionPane.showMessageDialog(projectPanel, "Wrong file format.");
	anException.printStackTrace();
      }
      
      */
    }
    
    /**
     * Loads the data from a file
     * 
     * @param urlString Absolute path to the file to be loaded.
     */
    private void loadGroupFromFile(String urlString) throws java.io.IOException, 
      java.net.MalformedURLException, java.text.ParseException
  {
      URL url = new URL(urlString);
      FileStorage storage = new FileStorage();
      projectPanel.setModel(new PersonListModel(storage.load(url), url));
    }

    /**
     * Retrieves the absolute path of the file picked in the file chooser.
     * 
     * @return The absolute path of the file to open.
     */
    private String getFileUrlFromUser() {
    		JFileChooser fc = new JFileChooser();
    		FPFileFilter fpFilter = new FPFileFilter();
        fpFilter.addExtension("XML");
        fpFilter.addExtension("DATA");
        fpFilter.setDescription("XML & Flat data files");
        fc.addChoosableFileFilter(fpFilter);
        
        int result = fc.showOpenDialog(projectPanel);
        return (result == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile().toURI().toString() : null);
    }
    
}

