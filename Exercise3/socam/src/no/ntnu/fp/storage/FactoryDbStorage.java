package no.ntnu.fp.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;


import no.ntnu.fp.gui.FactoryProjectPanel;
import no.ntnu.fp.model.*;

public class FactoryDbStorage {
	
	Connection connection;
	FactoryProject project;
	SoftwareDbStorage swDbStorage;
	EcuDbStorage ecuDbStorage;
	VehicleDbStorage vehicleDbStorage;
	ArrayList<Software> softwareList;
	ArrayList<SimpleEcu> simpleEcuList;
	ArrayList<Vehicle> vehicleList;
	
	public Connection connectToFactoryDb(){
		
		try{		
			// Laster inn Derbys Embedded JDBC-driver. Kan kaste ClassNotFoundException
	        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	        // URL som identifiserer databasen
	        String dbURL = "jdbc:derby:FactoryDB;";
	        // Oppretter, og kobler til, databasen
	        this.connection = DriverManager.getConnection(dbURL);
	        //this.statement = this.connection.createStatement();
	        //FactoryProjectPanel.setStatusBar("Database loaded ok");
		}
		catch (ClassNotFoundException e) {
	        System.err.println("Sørg for at derby.jar er i classpathen.\n" +
	                           "Du kan hente dem fra http://www.idi.ntnu.no/emner/tdt4145/programvare/javadb.html");
	        //FactoryProjectPanel.setStatusBar("SQL-error when trying to load database");
		}
		catch (SQLException e) {
	        System.err.println("Det ble noe SQL-trøbbel; nærmere bestemt " + e);
	       // FactoryProjectPanel.setStatusBar("SQL-error when trying to load database");
	      }
		catch (ArrayIndexOutOfBoundsException e) {
	        System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
	       // FactoryProjectPanel.setStatusBar("SQL-error when trying to load database");
	      } 
		
		return this.connection;
		
	}
	public FactoryProject openFactoryProject(){
		
		swDbStorage = new SoftwareDbStorage();
		ecuDbStorage = new EcuDbStorage();
		vehicleDbStorage = new VehicleDbStorage();
		
		softwareList = new ArrayList<Software>();
		simpleEcuList = new ArrayList<SimpleEcu>();
		vehicleList = new ArrayList<Vehicle>();
		
		softwareList = swDbStorage.openSoftware();
		simpleEcuList = ecuDbStorage.openSimpleEcu();
		vehicleList = vehicleDbStorage.openVehicles();
		
		
		project = new FactoryProject(vehicleList,softwareList,simpleEcuList);
		
		return project;
	}
}
