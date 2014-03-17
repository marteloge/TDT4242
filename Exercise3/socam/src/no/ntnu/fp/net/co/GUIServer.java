package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import no.ntnu.fp.model.*;
import no.ntnu.fp.gui.ConnectAction;
import no.ntnu.fp.gui.FactoryProjectPanel;
import no.ntnu.fp.gui.ProjectPanel;
import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.storage.VehicleDbStorage;
/**
 * 
 * Class to start the server
 * 
 * @author Jo Mehmet
 *
 */
public class GUIServer {
	
	ConnectionImpl factoryConnection;
	final FactoryProjectPanel fProjectPanel;
	Log log;
	/**
	* Just to test
	* 
	* @param args
	*/

	public GUIServer(FactoryProjectPanel fProjectPanel){
		// Set up log
	    Log.setLogName("Client");
		this.fProjectPanel = fProjectPanel;
		startServer();
		startReceive();
	}
	/**
	 * Just for testing
	 * 
	 */
	private void kjor() {
		// Create log
	    //Log log = new Log();
	    //log.setLogName("Server");
	    try {
	      
	      //La serveren ta imot flere klienter
/*
	      ( new Thread() {

				public void run() {
					new TestCoServer();
				}
			}).start();
*/
	    try {
	    	FactoryProjectPanel.setStatusBar("Connected | Working in background...");
	    	String msg = null; 
	    	do{
	    		//Wait for vehicle request  
	    		msg = factoryConnection.receive();
				if(msg != null){
					XMLServer x = new XMLServer();
					Vehicle v = x.getVehicleFromXML(msg);
					msg = x.vehicleObjToXML(v);
					//Send vehicle
					factoryConnection.send(msg);
				}
			}while(msg == null);
		} catch (EOFException e){
		FactoryProjectPanel.setStatusBar("Client wants to close the connection");
		factoryConnection.close();
	      }
	    }
	    catch (IOException e){
	    	e.printStackTrace();
	    	FactoryProjectPanel.setStatusBar("I tsink: Troubles with your TCP-network, maybe to server running at once");
	    }
	}
	/**
	 * Start listening to client connection.
	 */
	public void startServer(){
	    // server connection instance, listen on port 5555
	    factoryConnection = new ConnectionImpl(5555);
	    FactoryProjectPanel.setStatusBar("Server started,waiting for connection...");
	    try {
			factoryConnection = (ConnectionImpl) factoryConnection.accept();
		} catch (SocketTimeoutException e) {
			FactoryProjectPanel.setStatusBar("[connecting] SocketTimeout, please try again");
		} catch (IOException e) {
			FactoryProjectPanel.setStatusBar("[connecting] IOTroubles, please try again and maybe check your LAN-cable and TCP-setup");
		}
	}
    /**
     * Receive VehicleID for Garage
     * 
     */
	public void startReceive(){
		(new Thread() {	
			public void run() {
				FactoryProjectPanel.setStatusBar(factoryConnection.toString());
				FactoryProjectPanel.setStatusBar("Connected |Working in background...");
				String msg =""; 
				try {
					
					while(true){
						//Wait for VehicleID 
						if(factoryConnection != null){
							//JOptionPane.showConfirmDialog(fProjectPanel,factoryConnection.toString());
							msg = factoryConnection.receive();
						}
						else{
							JOptionPane.showConfirmDialog(fProjectPanel,"factoryConnection er null");
						}
						if(msg != null){
							XMLServer x = new XMLServer();
							if (x.validateHeaderFromXMLClient(msg)) { //Henter Vehicle fra DB ut i fra vehicleID
								Vehicle v = x.getVehicleFromXML(msg);
								msg = x.vehicleObjToXML(v);
								factoryConnection.send(msg);
							}
							else { //Får ny info
								Vehicle v = x.toVehicleFromClient(msg);
								VehicleDbStorage vDbs = new VehicleDbStorage();
								vDbs.updateVehicle(v);
							}
							
						}
					}
				}catch (EOFException e){
					FactoryProjectPanel.setStatusBar("Client wants to close the connection");
					stopServer();
				}
				catch (IOException e){
					e.printStackTrace();
					FactoryProjectPanel.setStatusBar("I tsink: Troubles with your TCP-network, maybe to server running at once");
				}
			}
		}).start();
	}
    /*
     * Stop connection from listening to new connections and data.
     */
	public void stopServer(){
    	try {
			factoryConnection.close();
			FactoryProjectPanel.setStatusBar("Disconnected");
		} catch (IOException e) {
			FactoryProjectPanel.setStatusBar("I tsink: Troubles with your TCP-network, maybe to server running at once");
		}
    }
}

