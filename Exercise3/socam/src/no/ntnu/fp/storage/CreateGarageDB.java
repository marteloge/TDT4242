package no.ntnu.fp.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateGarageDB {
	
	/**
	 * Denne klassen oppretter databasen FactoryDB i katalog: workspace\"prosjektnavn"
	 */	
	Connection connection;
	Statement statement;
	
	public CreateGarageDB(){
			
	}
		/**
		 * Denne metoden oppretter tabellene i databasen.
		 */
	
	private String[] insertTables(){
		
		String query1 = "create table customer" + 
		"(cust_id INTEGER PRIMARY KEY," + 
		"name varchar(40), " +
		"email varchar(40)," +
		"street varchar(40)," +
		"city varchar(40)," +
		"vehicle_id INTEGER)"; 

		/*
		 * Eksempel input
		 * 	
		String query = "INSERT INTO customer "+
		"VALUES("+
		"4,'navnet','eposten','street','cityyyyas',32"+
		")";
		 */
		
		String[] queries = {query1};
		
		return queries;
	} 
	
	/**
	 * Metode som oppretter forbindelse til databasen
	 */
	private Connection createDB(){
		
		try{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	        String dbURL = "jdbc:derby:GarageDB;create=true";
	        this.connection = DriverManager.getConnection(dbURL);
		}
		catch (ClassNotFoundException e) {
	        System.err.println("Sï¿½rg for at derby.jar er i classpathen.\n" +
	                           "Du kan hente dem fra http://www.idi.ntnu.no/emner/tdt4145/programvare/javadb.html");
		}
		catch (SQLException e) {
	        System.err.println("Det ble noe SQL-trøbbel; nærmere bestemt1 " + e);

	      }
		catch (ArrayIndexOutOfBoundsException e) {
	        System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
	      } 
		return this.connection;
	}
	
	/**
	 * Metode som kjører spørringene.
	 * @see insertTables()
	 */
	public void executeStatements(){
		
		String[] queries = insertTables();
		Connection conn = createDB();
		
		try{
			this.statement = conn.createStatement();
			
			for(int i = 0; i < queries.length;++i){
				this.statement.execute(queries[i]);
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
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		
			CreateGarageDB test = new CreateGarageDB();
			test.executeStatements();
				
	}	
	  
}
