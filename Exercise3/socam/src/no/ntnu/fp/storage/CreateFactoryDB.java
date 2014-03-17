package no.ntnu.fp.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateFactoryDB {
	
	/*
	 * Denne klassen oppretter databasen FactoryDB i katalog: workspace\"prosjektnavn"
	 */	
	Connection connection;
	Statement statement;
	
	public CreateFactoryDB(){
			
	}
		
	private String[] insertTables(){
		
		// SQL-spørringer som oppretter tabellene
		
		String q1 = "CREATE TABLE software_archive " +
				"(sw_version INTEGER , " +
				"sub_version INTEGER, " +
				"url VARCHAR(100), " +
				"PRIMARY KEY(sw_version,sub_version))";
							
		String q2 = "CREATE TABLE action_script"+
				"(ecu_no INTEGER , " +
				"sw_version INTEGER, " +
				"PRIMARY KEY(ecu_no,sw_version))";
				
		String q3 = "CREATE TABLE installed_ecus" +
				"(ecu_no INTEGER , " +
				"sw_version INTEGER , " +
				"vehicle_id INTEGER ," +
				"sub_version INTEGER, " +
				"PRIMARY KEY(ecu_no,sw_version,vehicle_id))";
				
		String q4 = "CREATE TABLE vehicle" +
				"(series_no INTEGER , " +
				"vehicle_id INTEGER, " +
				"sw_history_log VARCHAR(32000)," +
				"PRIMARY KEY(vehicle_id))";
		
		String q5 = "CREATE TABLE garage" +
				"(garage_id INTEGER , " +
				"phone INTEGER, " +
				"email VARCHAR(100), " +
				"PRIMARY KEY(garage_id))";
				
		
		String[] queries = {q1,q2,q3,q4,q5};
		
		return queries;
	} 
	
	private Connection createDB(){
		
		try{
			// Laster inn Derbys Embedded JDBC-driver. Kan kaste ClassNotFoundException
	        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	        // URL som identifiserer databasen
	        String dbURL = "jdbc:derby:FactoryDB;create = true";
	        // Oppretter, og kobler til, databasen
	        this.connection = DriverManager.getConnection(dbURL);
		}
		catch (ClassNotFoundException e) {
	        System.err.println("Sørg for at derby.jar er i classpathen.\n" +
	                           "Du kan hente dem fra http://www.idi.ntnu.no/emner/tdt4145/programvare/javadb.html");
		}
		catch (SQLException e) {
	        System.err.println("Det ble noe SQL-trøbbel; nærmere bestemt " + e);
	      }
		catch (ArrayIndexOutOfBoundsException e) {
	        System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
	      } 
		
		return this.connection;
	}
	
	private void executeStatements(){
		
		String[] queries = insertTables();
		Connection conn = createDB();
		
		
		try{
			this.statement = conn.createStatement();
			
			for(int i = 0; i < queries.length;++i){
				this.statement.executeUpdate(queries[i]);
			}
			
			conn.commit();
			conn.close();
			this.statement.close();
					
		}
		catch (SQLException e) {
	        System.err.println("Det ble noe SQL-trøbbel; nærmere bestemt " + e);
	      }
		catch (ArrayIndexOutOfBoundsException e) {
	        System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
	      } 
				
	}
		
	public static void main(String[] args){
		
			CreateFactoryDB createFactoryDb = new CreateFactoryDB();
			createFactoryDb.executeStatements();
				
	    }	
}
