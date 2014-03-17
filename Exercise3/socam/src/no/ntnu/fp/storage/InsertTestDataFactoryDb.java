package no.ntnu.fp.storage;

import java.sql.*;
import java.util.ArrayList;

public class InsertTestDataFactoryDb {

	static Connection connection;
	static Statement statement;

	public static ArrayList<String> softwareQueries(){
		
		String q1 = "INSERT INTO software_archive VALUES(1,0,'sw1.0.no')";
		String q2 = "INSERT INTO software_archive VALUES(1,1,'sw1.1.no')";
		String q3 = "INSERT INTO software_archive VALUES(1,2,'sw1.2.no')";
		String q4 = "INSERT INTO software_archive VALUES(2,0,'sw2.0.org')";
		String q5 = "INSERT INTO software_archive VALUES(3,0,'sw3.0.net')";
		String q6 = "INSERT INTO software_archive VALUES(3,1,'sw3.1.net')";
		String q7 = "INSERT INTO software_archive VALUES(4,0,'sw4.0.net')";
		String q8 = "INSERT INTO software_archive VALUES(5,0,'sw5.0.net')";
		
		ArrayList<String> swQueries = new ArrayList<String>();
		swQueries.add(q1);
		swQueries.add(q2);
		swQueries.add(q3);
		swQueries.add(q4);
		swQueries.add(q5);
		swQueries.add(q6);
		swQueries.add(q7);
		swQueries.add(q8);
		
		return swQueries;
	}
	
	public static ArrayList<String> actionQueries(){
		ArrayList<String> actionQueries = new ArrayList<String>();
		String q1 = "INSERT INTO action_script VALUES(1,1)";
		String q2 = "INSERT INTO action_script VALUES(2,2)";
		String q3 = "INSERT INTO action_script VALUES(3,3)";
		
		actionQueries.add(q1);
		actionQueries.add(q2);
		actionQueries.add(q3);
		
		return actionQueries;
	}
	
	public static ArrayList<String> installedQueries(){
		ArrayList<String> installedEcusQueries = new ArrayList<String>(); 
		String q1 = "INSERT INTO installed_ecus VALUES(1,1,1,0)";
		String q6 = "INSERT INTO installed_ecus VALUES(2,2,1,0)";
		String q7 = "INSERT INTO installed_ecus VALUES(3,3,1,1)";
		String q2 = "INSERT INTO installed_ecus VALUES(1,1,2,1)";
		String q3 = "INSERT INTO installed_ecus VALUES(2,2,3,0)";
		String q4 = "INSERT INTO installed_ecus VALUES(2,2,4,0)";
		String q5 = "INSERT INTO installed_ecus VALUES(3,3,5,0)";
		
		installedEcusQueries.add(q1);
		installedEcusQueries.add(q2);
		installedEcusQueries.add(q3);
		installedEcusQueries.add(q4);
		installedEcusQueries.add(q5);
		installedEcusQueries.add(q6);
		installedEcusQueries.add(q7);

		return installedEcusQueries;
	}
	
	public static ArrayList<String> vehicleQueries(){
		ArrayList<String> vehicleQueries = new ArrayList<String>();
		String q1 = "INSERT INTO vehicle VALUES(100,1,'l�s teftning')";
		String q2 = "INSERT INTO vehicle VALUES(100,2,'rusk i forgasser')";
		String q3 = "INSERT INTO vehicle VALUES(100,3,'ukjent')";
		String q4 = "INSERT INTO vehicle VALUES(200,4,'totalvrak')";
		String q5 = "INSERT INTO vehicle VALUES(200,5,'totalvrak')";

		vehicleQueries.add(q1);
		vehicleQueries.add(q2);
		vehicleQueries.add(q3);
		vehicleQueries.add(q4);
		vehicleQueries.add(q5);
		
		return vehicleQueries;
	}
	
	public static ArrayList<String> garageQueries(){
		ArrayList<String> garageQueries = new ArrayList<String>();
		String q1 = "INSERT INTO garage VALUES(1,90000000,'test@test.no')";
		String q2 = "INSERT INTO garage VALUES(2,21604030,'mail@mail.no')";

		garageQueries.add(q1);
		garageQueries.add(q2);
		
		return garageQueries;
	}
	
	public static void main(String[] args){

		try{		
			// Laster inn Derbys Embedded JDBC-driver. Kan kaste ClassNotFoundException
	        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	        // URL som identifiserer databasen
	        String dbURL = "jdbc:derby:FactoryDB;";
	        // Oppretter, og kobler til, databasen
	        connection = DriverManager.getConnection(dbURL);
	        statement = connection.createStatement();
		}
		catch (ClassNotFoundException e) {
	        System.err.println("S�rg for at derby.jar er i classpathen.\n" +
	                           "Du kan hente dem fra http://www.idi.ntnu.no/emner/tdt4145/programvare/javadb.html");
		}
		catch (SQLException e) {
	        System.err.println("Det ble noe SQL-tr�bel; n�rmere bestemt " + e);
	      }
		catch (ArrayIndexOutOfBoundsException e) {
	        System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
	      } 
		
		ArrayList<String> swQueries = softwareQueries();
		ArrayList<String> actionQueries = actionQueries();
		ArrayList<String> installedEcusQueries = installedQueries();
		ArrayList<String> garageQueries = garageQueries();
		ArrayList<String> vehicleQueries = vehicleQueries();
		
		String q10 = "DELETE FROM software_archive";
		String q20 = "DELETE FROM vehicle";
		String q30 = "DELETE FROM action_script";
		String q40 = "DELETE FROM installed_ecus";
		String q50 = "DELETE FROM garage";
		

		try{
			
			statement.execute(q10);
			statement.execute(q20);
			statement.execute(q30);
			statement.execute(q40);
			statement.execute(q50);
			
			for(int i = 0; i < swQueries.size(); i++){
				statement.execute(swQueries.get(i));
			}
			for(int i = 0; i < actionQueries.size(); i++){
				statement.execute(actionQueries.get(i));
			}
			for(int i = 0; i < installedEcusQueries.size(); i++){
				statement.execute(installedEcusQueries.get(i));
			}
			for(int i = 0; i < garageQueries.size();i++){
				statement.execute(garageQueries.get(i));
			}
			for(int i = 0; i < vehicleQueries.size(); i++){
				statement.execute(vehicleQueries.get(i));
			}
			
			/*
			ResultSet rs = statement.executeQuery("SELECT * FROM vehicle");
			while(rs.next()){
				System.out.print("vehicleId: " + rs.getInt(2) + " History log: " + rs.getString(3)+"\n");
			}
			*/
			
			connection.close();
			statement.close();
		}
		catch(SQLException e){
			System.err.println(e);
		}

	}
	
}
