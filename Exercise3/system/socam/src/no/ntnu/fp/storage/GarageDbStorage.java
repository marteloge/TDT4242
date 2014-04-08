package no.ntnu.fp.storage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

import no.ntnu.fp.gui.ProjectPanel;
import no.ntnu.fp.model.Project;
import no.ntnu.fp.model.Person;

public class GarageDbStorage implements Storage {
	
	final String query = "Select * from customer";
	Connection connection;
	Project project;
	Statement statement;
	ResultSet rs;
	
	public GarageDbStorage(){
		
	}
	
	public Project load(URL url) throws java.io.IOException, ParseException{
		
		return null;
		
	}
	
	// Metode for � koble til GarageDB
	
	private Connection connectToGarageDb(){
		
		try{		
			// Laster inn Derbys Embedded JDBC-driver. Kan kaste ClassNotFoundException
	        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	        // URL som identifiserer databasen
	        String dbURL = "jdbc:derby:GarageDB;";
	        // Oppretter, og kobler til, databasen
	        this.connection = DriverManager.getConnection(dbURL);
		}
		catch (ClassNotFoundException e) {
	        System.err.println("S�rg for at derby.jar er i classpathen.\n" +
	                           "Du kan hente dem fra http://www.idi.ntnu.no/emner/tdt4145/programvare/javadb.html");
		}
		catch (SQLException e) {
	        System.err.println("Det ble noe SQL-tr�bbel; n�rmere bestemt " + e);
	      }
		catch (ArrayIndexOutOfBoundsException e) {
	        System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
	      } 
		
		return this.connection;
	}
	
	// Metode for � hente ut alle kundene i GarageDB
	
	private ResultSet getPersonsFromDb(){
		
		connection = connectToGarageDb();
		
		try{
			this.statement = connection.createStatement();
			rs = this.statement.executeQuery(query);
			
			connection.commit();
							
		}
		
		catch (SQLException e) {
	        System.err.println("Det ble noe SQL-tr�bbel; n�rmere bestemt " + e);
	      }
		catch (ArrayIndexOutOfBoundsException e) {
	        System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
	      }
		
		return rs;
						
	}
	
	// Metode for � flytte kundene i GarageDB til et prosjekt
	
	public Project load(){


		project = new Project();
		try{
			ResultSet resultSet = getPersonsFromDb();

			while(resultSet.next()){

				Person person;
				int custId = Integer.parseInt(resultSet.getString(1));
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				String street = resultSet.getString(4);
				String city = resultSet.getString(5);
				String vehicleId = resultSet.getString(6);

				person = new Person(custId,name,email,street,city,vehicleId);
				project.addPerson(person);
				
			}
			rs.close();
			resultSet.close();
			connection.close();
			statement.close();
			//ProjectPanel.setStatusBar("Database loaded successfully");
		}
		catch(SQLException e){
			System.err.println(e);
			//ProjectPanel.setStatusBar("SQL error when trying to load the database");
		}

		return project;
	}
	
	// Hjelpemetode for � utf�re sp�rringe
	
	public void executeQueries(ArrayList<String> queries){
		
		try{
			connection = connectToGarageDb();

			statement = connection.createStatement();
			
			for(int i = 0; i < queries.size(); ++i)
				statement.execute(queries.get(i));
			
			connection.close();
			statement.close();
			
		}
		catch(SQLException e){
			System.err.println(e);
		}
	}

	
	
	public void save(URL url, Project project){
		
	}
	
	// Hjelpemetode for � sjekke om en "custId" finnes i et prosjekt
	
	public boolean isInExisting(Project existing, Person person){
		
		boolean result = false;
		
		for(int i = 0; i < existing.getPersonCount();++i){
			
			if (existing.getPerson(i).getCustId() == person.getCustId())
				result = true;		
		}	
		return result;
	}
	
	// Metode for � sammenligne og smelte sammen to prosjekter
	
	public ArrayList<String> compareAndChangeProjects(Project incoming, Project existing) throws SQLException{
		
		ArrayList<String> result = new ArrayList<String>();
	
		if(existing.getPersonCount() == 0){
			
			for(int i = 0; i < incoming.getPersonCount(); i++){
				
				Person newPers = incoming.getPerson(i);			
				String SQL = "INSERT INTO customer VALUES(" + newPers.getCustId() + ",\'" + newPers.getName() +"\',\'";
				SQL += newPers.getEmail() + "\',\'" + newPers.getStreet() + "\',\'" + newPers.getCity() + "\'," + newPers.getVehicleID() + ")"; 
				result.add(SQL);			
			}	
			return result;
		}
		
		int max = Math.max(incoming.getPersonCount(), existing.getPersonCount());
		int min = Math.min(incoming.getPersonCount(), existing.getPersonCount());

		outer: for(int i = 0; i < max; i++){
			Person newPers;
			Person oldPers;
			
			inner: for(int j = 0; j < min; j++){

				if(incoming.getPersonCount() == max){
					newPers = incoming.getPerson(i);
					oldPers = existing.getPerson(j);
					
					if (existing.contains(newPers)) {
						
						continue inner;
					}
				}
				else{
					newPers = incoming.getPerson(j);
					oldPers = existing.getPerson(i);
					if (incoming.contains(oldPers)) {
						continue outer;
					}
				}
	
				if(newPers.getCustId() == oldPers.getCustId()){
					

					String SQL = "delete from customer where cust_id=" + oldPers.getCustId();			
					result.add(SQL);
					String SQL2 = "INSERT INTO customer VALUES(" + newPers.getCustId() + ",\'" + newPers.getName() +"\',\'";
					SQL2 += newPers.getEmail() + "\',\'" + newPers.getStreet() + "\',\'" + newPers.getCity() + "\'," + newPers.getVehicleID() + ")"; 
					result.add(SQL2);
					continue inner;
				
				}
				else if (newPers.getCustId() != oldPers.getCustId() && !isInExisting(existing,newPers)) {
					
					String SQL = "INSERT INTO customer VALUES(" + newPers.getCustId() + ",\'" + newPers.getName() +"\',\'";
					SQL += newPers.getEmail() + "\',\'" + newPers.getStreet() + "\',\'" + newPers.getCity() + "\'," + newPers.getVehicleID() + ")"; 
					result.add(SQL);
					existing.addPerson(newPers);
					continue inner;
				}
				
				else if (!incoming.contains(oldPers)){
					String SQL = "DELETE FROM customer WHERE cust_id=" + oldPers.getCustId();
					result.add(SQL);
				}
			}
		}		
		return result;	
	}
	
	// Metode for � skrive et innkommende prosjekt til GarageDB
	
	public void save(Project incomingProject){
		
		try{
			Project oldProject = load();
			
			ArrayList<String> queries = compareAndChangeProjects(incomingProject,oldProject);
			
			for(int i = 0; i < queries.size();++i)
				System.out.println(queries.get(i));
			
			executeQueries(queries);
			//ProjectPanel.setStatusBar("Changes successfully saved");
		}

		catch(SQLException e){
			System.err.println(e);
			//ProjectPanel.setStatusBar("SQL error when trying to save");
		}
				
	}
	
}
