package no.ntnu.fp.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import no.ntnu.fp.gui.EcuPanel;
import no.ntnu.fp.model.*;

public class EcuDbStorage extends FactoryDbStorage{
	
	Connection connection;
	FactoryProject project;
	SoftwareDbStorage swStorage;
	int ecuId;
	int swId;
	int subId;
	int newestMinorVersion;
	String SQL;
	Vehicle vehicle;
	int subIdFromInstalledEcu = -1;
	Ecu ecu;
	SimpleEcu simpleEcu;
	ArrayList<Ecu> ecuList;
	ArrayList<SimpleEcu> simpleEcuList;
	char[] softId;
	
	public EcuDbStorage(){
		
	}
	
	private int getLatestMinorVersion(Ecu ecu){
	
		int tmpswId = ecu.getSwId();
		int tmpNewest = -1;
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT MAX(sub_version) FROM software_archive WHERE sw_version="+tmpswId);
			while(rs.next())
				tmpNewest= rs.getInt(1);
			
			rs.close();
			statement.close();
			connection.close();
			
		}
		catch(SQLException e){
			System.err.println("SQL-klikk i getLatestMinorVersion "+e);
		}
		
		return tmpNewest;
	}
	
	public ArrayList<SimpleEcu> openSimpleEcu(){
		
		simpleEcuList = new ArrayList<SimpleEcu>();
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM action_script");
			while(rs.next()){
				
				ecuId = rs.getInt(1);
				swId = rs.getInt(2);

				simpleEcu = new SimpleEcu(ecuId,swId);
				simpleEcuList.add(simpleEcu);
			}
			
			rs.close();
			statement.close();
			connection.close();
		}
		catch(SQLException e){
			System.err.println("error i openEcu "+e);
		}
		return simpleEcuList;
	}
	
	public boolean isUpdated(Ecu tmpEcu){
		return (tmpEcu.getSubSwId() == getLatestMinorVersion(tmpEcu));
	}
	
	public String[] addEcu(SimpleEcu simpleEcu, EcuPanel panel){
		
		String[] message = {"Nothing happened, there is an error in your code",""};
		
		if(isEcuInActionScript(simpleEcu)){
			message[0] = "ECU is already in database";
		}
		
		else if(!isSoftwareInSoftwareArchive(simpleEcu)){
			message[0] = "This ecu does not have a software, you must add one";
		}
		else if(isSoftwareInActionScript(simpleEcu)){
			message[0] = "This software is controlling another ecu, please enter a new one";
		}
		
		else{
			addEcuToActionScript(simpleEcu);
			
			message[0] = "Ecu added to database";
			message[1] = "ok";
		}
		
		return message;
	}
	
	private void addEcuToActionScript(SimpleEcu simpleEcu){
		
		swId = simpleEcu.getSwId();
		ecuId = simpleEcu.getEcuId();
		
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "INSERT INTO action_script VALUES(" +
					ecuId+","+swId +")";
			statement.execute(sql);
			
			statement.close();
			connection.close();
		}
		catch(SQLException e){
			System.err.println("SQL-klikk i addEcuToActionScript: " + e);
		}
	}
	
	private boolean isSoftwareInActionScript(SimpleEcu simpleEcu){
		
		swId = simpleEcu.getSwId();
		
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "SELECT sw_version FROM action_script";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				if(rs.getInt(1) == swId)
					return true;
			}
			rs.close();
			statement.close();
			connection.close();
		}
		catch(SQLException e){
			System.err.println("SQL-klikk i isSoftwareInActionScript: " + e);
		}
		return false;
	}
	
	private boolean isSoftwareInSoftwareArchive(SimpleEcu simpleEcu){
		
		swId = simpleEcu.getSwId();
		
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			
			String sql = "SELECT sw_version FROM software_archive WHERE sub_version = 0";
			ResultSet rs = statement.executeQuery(sql);
			
			while(rs.next()){
				if(rs.getInt(1) == swId){
					return true;
				}
			}
			
			rs.close();
			statement.close();
			connection.close();
		}
		catch(SQLException e){
			System.err.println("SQL-klikk i isSoftwareInSoftwareArchive: " + e);
		}
		
		return false;
	}
	private int getSw(Ecu ecu) throws SQLException{
		
		int result = -1;
		
		connection = connectToFactoryDb();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT sw_version FROM action_script" +
				" WHERE ecu_no=" + ecu.getEcuId());
		while(rs.next()){
			result = rs.getInt(1);
		}
		rs.close();
		stmt.close();
		connection.close();
		return result;
	}
	
	public int[] getEcuSoft(Ecu ecu){
		
		int[] result = {-1,-1};
		try{
			connection = connectToFactoryDb();
			Statement stmt = connection.createStatement();
			String sql = "SELECT max(sub_version) FROM software_archive WHERE sw_version =" +getSw(ecu);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				result[0] = getSw(ecu);
				result[1] = rs.getInt(1);
			}
		}
		catch(SQLException e){
			System.err.println("SQL-klikk i getEcuSoft: "+e);
		}
		return result;
	}
	
	private boolean isEcuInActionScript(SimpleEcu simpleEcu){
		
		ecuId = simpleEcu.getEcuId();
		
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT ecu_no FROM action_script");
			
			while(rs.next()){
				
				if(rs.getInt(1) == ecuId)
					return true;
			}
			
			rs.close();
			statement.close();
			connection.close();
			
		}
		catch(SQLException e){
			System.err.println("SQL-klikk i isEcuInActionScript: " + e);
		}
		
		return false;
	}
	
	 // Denne brukes ikke til noe, men kan absolutt v�re nyttig.

	
}
