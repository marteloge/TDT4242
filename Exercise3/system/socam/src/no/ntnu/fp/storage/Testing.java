package no.ntnu.fp.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Testing {

	
	public static void main(String[] args) {
		try{		
			// Laster inn Derbys Embedded JDBC-driver. Kan kaste ClassNotFoundException
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			// URL som identifiserer databasen
			String dbURL = "jdbc:derby:GarageDb;";
			// Oppretter, og kobler til, databasen
			Connection connection = DriverManager.getConnection(dbURL);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM customer");

			while(rs.next()){

				System.out.print("cust id : " + rs.getInt(1)+" name : "+rs.getString(2)+"\n");
			}	
		}
		catch (ClassNotFoundException e) {
			System.err.println("Sørg for at derby.jar er i classpathen.\n" +
			"Du kan hente dem fra http://www.idi.ntnu.no/emner/tdt4145/programvare/javadb.html");
		}
		catch (SQLException e) {
			System.err.println("Det ble noe SQL-tr�bbel; n�rmere bestemt " + e);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
		} 


	}

}
