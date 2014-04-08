package no.ntnu.fp.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import no.ntnu.fp.gui.SoftwarePanel;
import no.ntnu.fp.model.Project;
import no.ntnu.fp.model.Software;
import no.ntnu.fp.model.FactoryProject;

public class SoftwareDbStorage extends FactoryDbStorage implements Storage {
	
	FactoryProject factoryProject;
	
	Connection connection;

	//Software software;
	final String getSoftware = "SELECT * FROM software_archive";
	int swId;
	int subId;
	String url;
	int maxSubFromDb;
	ArrayList<Software> softwareList;
	
	public SoftwareDbStorage(){
		
	}
	
	public Project load(){
		
		return null;
	}
	
	public void save(Project project){
			
	}
	
	public ArrayList<Software> openSoftware(){
		
		softwareList = new ArrayList<Software>(); 
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(getSoftware);
			while(rs.next()){
				
				swId = rs.getInt(1);
				subId = rs.getInt(2);
				url = rs.getString(3);
	
				Software software = new Software(swId,subId,url);
				softwareList.add(software);
			}
			
			rs.close();
			statement.close();
			connection.close();
		}
		catch(SQLException e){
			System.err.println("error i openSoftware "+e);
		}
		
		return softwareList;
	}
	
	public String addSoftware(Software sw, SoftwarePanel sp){
		
		String message = "Software added";

		swId = sw.getSwVersion();
		subId = sw.getMinorVersion();
		url = sw.getUrl();

		if(swInSwArchive(swId)){
			maxSubFromDb = getBiggestSubId(swId);
			if(!(subId == maxSubFromDb +1)){
				subId = maxSubFromDb + 1;
				message = "Software id allready in db and you entered a wrong sub id, it was changed to " +subId +" and added to db";
				//sw.setMinorVersion(subId);
				sp.getModel().setMinorVersion(subId);
				sp.updatePanel(null);
				addNewMinorVersion(swId,url);
				return message;
			}
			else{
				message = "Software id allready in db, added new sub id";
				addNewMinorVersion(swId,url);
				return message;
			}
		}
		else{

			if(subId == 0){
				addNewSoftware(swId,url);
				return message;
			}
			else{
				message = "SubId not 0, it was changed and added to db";
				addNewSoftware(swId,url);
				return message;
			}
		}

	}
	
	public boolean swInSwArchive(int softId){
		
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT sw_version FROM software_archive");
			
			while(rs.next()){
				
				if(!tmp.contains(Integer.valueOf(rs.getInt(1))))
					tmp.add(rs.getInt(1));
			}
			rs.close();
			statement.close();
			connection.close();
			
		}
		catch(SQLException e){
			System.err.println("klikk i swInSwArchive: "+e);
		}
		return tmp.contains(Integer.valueOf(softId));
	}
	
	public int getBiggestSubId(int softId){
		
		maxSubFromDb = -1;
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "SELECT MAX(sub_version) FROM software_archive WHERE " +
					"sw_version = "+softId;
				
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				maxSubFromDb = rs.getInt(1);
			}
			
			rs.close();
			statement.close();
			connection.close();
		}
		catch(SQLException e){
			System.err.println("SQL-klikk i getBiggestSubId: "+e);
		}
		
		return maxSubFromDb;
	}
	
	private void addNewSoftware(int softId,String URL){
		
		ArrayList<String> tmp = new ArrayList<String>();
		
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT sw_version FROM software_archive");
			while(rs.next()){
				
				if(!tmp.contains(rs.getString(1)));
					tmp.add(rs.getString(1));	
			}
			
			if(!tmp.contains(String.valueOf(softId))){

				String sql = "INSERT INTO software_archive VALUES(" +
				+softId+ "," +0+ ",\'" +URL+"\')";
				statement.execute(sql);
			}
			else{
				addNewMinorVersion(softId,URL);
			}
			rs.close();
			statement.close();
			connection.close();
			
		}
		catch(SQLException e){
			System.err.println("SQL-klikk i addNewSoftware: "+e);
		}
		
	}
	
	private void addNewMinorVersion(int softId,String URL){
	
		try{
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT max(sub_version) FROM software_archive WHERE sw_version = "+ softId);
			
			while(rs.next()){
				
				maxSubFromDb = rs.getInt(1) + 1;
			}
			String sql = "INSERT INTO software_archive VALUES(" +
					+softId+ "," +maxSubFromDb+ ",\'" +URL+"\')";
			statement.execute(sql);
		
			rs.close();
			statement.close();
			connection.close();
		}
		
		catch(SQLException e){
			System.err.println("klikk i addNewMinorVersion: "+e);
		}
	}
	
}
