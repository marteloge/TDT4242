package no.ntnu.fp.net.co;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import no.ntnu.fp.gui.ProjectPanel;
import no.ntnu.fp.net.admin.Log;

public class GUIConnect {
	
	ConnectionImpl garageConnection;
	ProjectPanel projectPanel;
	String ip;
	
	public GUIConnect(String IPadr,ProjectPanel projectPanel){
		this.ip = IPadr;
		this.projectPanel = projectPanel;
		setConnection();
	}
	/**
	 * You can send string if you are connected.
	 * 
	 * @param str
	 */
	public void sendString(String str) {	    
		try {
			ProjectPanel.setStatusBar("Waiting for vehicle from Factory...");
			garageConnection.send(str);
		} catch (ConnectException e) {
			ProjectPanel.setStatusBar("Sending SocketTimeout, please try again");
		} catch (IOException e) {
			ProjectPanel.setStatusBar("Sending IOTroubles, please try again and maybe check your LAN-cable and TCP");
		}
	}
	/**
	 * Lytter og returnerer String
	 * 
	 * @return
	 */
	public String getString() {
		try {
			//aVehicle 
			ProjectPanel.setStatusBar(garageConnection.toString());
			return garageConnection.receive();
		} catch (ConnectException e) {
			ProjectPanel.setStatusBar("[receiving] ConnectTroubles, please try again");
		} catch (IOException e) {
			ProjectPanel.setStatusBar("[receiving] IOTroubles, please try again and maybe check your LAN-cable and TCP-setup");
		}
		JOptionPane.showConfirmDialog(projectPanel,"aVehicleXML er null\ni GUIconnect linje 50");
		return null;
	}
	private void setConnection(){
	      // get your own address and connect
	    try {
	    	garageConnection = new ConnectionImpl(4001, ip);
	    	//InetAdress kun fordi grensesnittet krever det
	    	InetAddress addr = IPTools.findPublicIPv4adr();
	    	garageConnection.connect(addr,5555);
			ProjectPanel.setStatusBar("Connected to "+ip);
		} catch (SocketTimeoutException e) {
			ProjectPanel.setStatusBar("[connecting] SocketTimeout, please try again");
		} catch (IOException e) {
			ProjectPanel.setStatusBar("[connecting] IOTroubles, please try again and maybe check your LAN-cable and TCP-setup");
		}
	}
	public void closeConnection(){
		 try {
			garageConnection.close();
			ProjectPanel.setStatusBar("Disconnected");
		} catch (IOException e) {
			ProjectPanel.setStatusBar("[close] IOTroubles, please try again and maybe check your LAN-cable and TCP");
		}
	}
}
