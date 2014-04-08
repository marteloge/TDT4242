package no.ntnu.fp.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import no.ntnu.fp.gui.FactoryProjectPanel;
import no.ntnu.fp.gui.NewVehiclePanel;
import no.ntnu.fp.model.Project;
import no.ntnu.fp.model.Vehicle;
import no.ntnu.fp.model.Ecu;

public class VehicleDbStorage extends FactoryDbStorage implements Storage {

	Connection connection;

	final String getVehicles = "SELECT * FROM vehicle";
	Vehicle vehicle;
	ArrayList<String> list;
	int swId;
	int subId;
	int newestMinorVersion;
	int ecuId;
	int vehicleId;
	int biggestVehicleId = -1;
	ArrayList<Vehicle> vehicleList;
	ArrayList<String> newestSoftwareSerials;
	ArrayList<Ecu> eculist;
	ArrayList<String> swVersions;
	ArrayList<Integer> sws;
	ArrayList<Integer> subs;
	ArrayList<Integer> vehicleIds;
	String historyLog;
	String series;

	public VehicleDbStorage() {

	}
	public Project load() {
		return null;
	}

	public ArrayList<Vehicle> openVehicles() {

		vehicleList = new ArrayList<Vehicle>();

		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = getAllVehiclesFromDb();
			while (rs.next()) {

				series = rs.getString(1);
				vehicleId = rs.getInt(2);
				eculist = getEcus(vehicleId);
				historyLog = rs.getString(3);

				vehicleList.add(new Vehicle(String.valueOf(vehicleId),
						historyLog, eculist, series));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("error i openVehicles: " + e);
		}
		return vehicleList;
	}

	private ArrayList<Ecu> getEcus(int vehID) {

		eculist = new ArrayList<Ecu>();
		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "SELECT ecu_no,sw_version,sub_version FROM installed_ecus "
					+ "WHERE vehicle_id = " + vehID;

			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {

				eculist.add(new Ecu(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL-klikk i getEcus: "+ e);
		}
		return eculist;
	}

	public ArrayList<Integer> getVehicleIds(int softId, int subId) {

		vehicleIds = new ArrayList<Integer>();
		ecuId = getEcuId(softId);
		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "SELECT vehicle_id FROM installed_ecus "
					+ "WHERE ecu_no = " + ecuId + " AND sw_version = " + softId
					+ " AND sub_version = " + subId + ")";

			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				vehicleIds.add(Integer.valueOf(rs.getInt(1)));
			}

			rs.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			System.err.println("SQL-klikk i getVehicleIds: " + e);
		}
		return vehicleIds;
	}

	private int getEcuId(int softId) {
		
		int tmpecuId = -1;
		
		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "SELECT ecu_no FROM action_script"
						+" WHERE sw_version = " + softId;
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				tmpecuId = rs.getInt(1);
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL-klikk i getEcuId :" + e);
		}
		return tmpecuId;
	}

	private ResultSet getAllVehiclesFromDb() {
		ResultSet rs = null;
		try {
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(getVehicles);

		} catch (SQLException e) {
			System.err.println(e);
		}
		return rs;
	}

	public Vehicle getVehicle(int vehId) {

		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM vehicle WHERE vehicle_id ="
							+ vehId);
			while (rs.next()) {

				series = rs.getString(1);
				vehicleId = rs.getInt(2);
				eculist = getEcus(vehId);
				historyLog = rs.getString(3);

				vehicle = new Vehicle(String.valueOf(vehicleId), historyLog,
						eculist, series);
			}

			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println(e);
		}
		return vehicle;
	}

	public ArrayList<Integer> getSoftwareList(Vehicle v) {

		for (int i = 0; i < v.getEcuCount(); i++) {
			try {
				connection = connectToFactoryDb();
				Statement statement = connection.createStatement();
				String sql = "SELECT software_version FROM action_script " +
						"WHERE ecu_no = " + v.getEcu(i);
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next())
					sws.add(rs.getInt(1));

				rs.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		return sws;
	}

	public ArrayList<Integer> getSubs(Vehicle v) {
		
		subs = new ArrayList<Integer>();
		
		sws = getSoftwareList(v);

		for (int i = 0; i < sws.size(); i++) {

			try {
				connection = connectToFactoryDb();
				Statement statement = connection.createStatement();
				String sql = "SELECT sub_version FROM installed_ecus WHERE "
						+ "ecu_no=" + v.getEcu(i).getEcuId()
						+ " AND sw_version = " + v.getEcu(i).getSwId()
						+ " AND vehicle_id = " + v.getVehicleID();
				ResultSet rs = statement.executeQuery(sql);

				while (rs.next()) {
					subs.add(rs.getInt(1));
				}

				rs.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		return subs;
	}

	public String addVehicle(Vehicle newVehicle, NewVehiclePanel panel) {

		String message = "Error when adding vehicle, check your code";
		int tmpVehicleId = Integer.parseInt(newVehicle.getVehicleID());

		if (isVehicleInVehicleDb(newVehicle)) {
			tmpVehicleId = getBiggestVehicleId() + 1;
			message = "VehicleId is allready in database, changed to: "
					+ tmpVehicleId + " and added to database";
			addNewVehicle(Integer.parseInt(newVehicle.getSeries()), tmpVehicleId,
					newVehicle.getHistoryLog());
			panel.getModel().setVehicleID(String.valueOf(tmpVehicleId));
		} else if (tmpVehicleId != getBiggestVehicleId() + 1) {
			tmpVehicleId = getBiggestVehicleId() + 1;
			message = "Wrong vehicleId, changed to: " + tmpVehicleId
					+ " and added to database";
			addNewVehicle(Integer.parseInt(newVehicle.getSeries()), tmpVehicleId,
					newVehicle.getHistoryLog());
			panel.getModel().setVehicleID(String.valueOf(tmpVehicleId));
		} else {
			message = "Vehicle added to database";
			addNewVehicle(Integer.parseInt(newVehicle.getSeries()), tmpVehicleId,
					newVehicle.getHistoryLog());
			panel.getModel().setVehicleID(String.valueOf(tmpVehicleId));
		}
		return message;
	}

	private void addNewVehicle(int seriesNo, int vehicleId, String historyLog) {

		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "INSERT INTO vehicle VALUES(" + +seriesNo + ","
					+ vehicleId + ",\'" + historyLog + "\')";
			statement.execute(sql);
			statement.close();
			connection.close();

		} catch (SQLException e) {
			System.err.println("SQL-klikk i addNewVehicle: " + e);
		}
	}

	private boolean isVehicleInVehicleDb(Vehicle v) {

		vehicleId = Integer.parseInt(v.getVehicleID());

		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "SELECT vehicle_id FROM vehicle";
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				if (rs.getInt(1) == vehicleId)
					return true;
			}

			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL-klikk i isVehicleInVehicleDb: " + e);
		}

		return false;
	}

	private int getBiggestVehicleId() {

		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT MAX(vehicle_id) FROM vehicle");
			while (rs.next()) {
				biggestVehicleId = rs.getInt(1);
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL-klikk i getBiggestVehicleId: " + e);
		}

		return biggestVehicleId;
	}

	public boolean okToAdd(Vehicle v) {

		//SoftwareDbStorage swStorage = new SoftwareDbStorage();
		ArrayList<String> messages = new ArrayList<String>();
		int tmpvehicleId = Integer.parseInt(v.getVehicleID());

		for (int i = 0; i < v.getEcuCount(); i++) {

			Ecu e = v.getEcu(i);

			int tmpecuId = e.getEcuId();
			int tmpswId = e.getSwId();

			if (isInInstalledEcus(tmpecuId,tmpswId,tmpvehicleId)) {
				String msg = "Error.This configuration is allready in database";
				messages.add(msg);
				FactoryProjectPanel.setStatusBar(msg);
			} /*else if (isInActionScript(tmpecuId,tmpswId)){
				String msg = "Error. The software you have chosen does not control this ecu";
				FactoryProjectPanel.setStatusBar(msg);
				messages.add(msg);
			}*/
			/*
			else if (!swStorage.swInSwArchive(tmpswId)) {
				String msg = "Error. Software for the Ecu: " + tmpecuId
				+ " is not in the software_archive, please add it";
				FactoryProjectPanel.setStatusBar(msg);
				messages.add(msg);
			}*/ else {
				messages.add("Vehicle updated");
			}
		}
		if (messages.size() == 0)
			messages.add("Nothing happened, error in your code");
		for (int i = 0; i < messages.size(); i++) {

			if (messages.get(i).startsWith("Error")){
				return false;
			}
		}
		return true;
	}
	public ArrayList<String> addEcus(Vehicle v, NewVehiclePanel nvp) {

		//SoftwareDbStorage swStorage = new SoftwareDbStorage();
		ArrayList<String> messages = new ArrayList<String>();
		int tmpvehicleId = Integer.parseInt(v.getVehicleID());

		for (int i = 0; i < v.getEcuCount(); i++) {

			Ecu e = v.getEcu(i);
			int tmpecuId = e.getEcuId();
			int tmpswId = e.getSwId();
			int tmpsubId = e.getSubSwId();

			if (isInInstalledEcus(tmpvehicleId, tmpecuId, tmpswId)) {

				messages.add("Error. This configuration is allready in database");
			}
			else if(tmpswId < 0){
				messages.add("Error. Illegal major SW version");
			}
			else if(tmpsubId < 0){
				messages.add("Error. Illegal sub version");
			}
			/*
			else if (!swStorage.swInSwArchive(tmpswId)) {
				messages.add("Error. Software for the Ecu: " + tmpecuId
						+ " is not in the software_archive, please add it");
			}*/ else {
				messages.add("Vehicle updated");
				int[] ids = updateInstalledEcus(v, e);
				nvp.getModel().getEcu(i).setSwId(ids[0]);
				nvp.getModel().getEcu(i).setSubSwId(ids[1]);

			}
		}
		if (messages.size() == 0)
			messages.add("Nothing happened, error in your code");
		return messages;
	}

	private int[] updateInstalledEcus(Vehicle v, Ecu ecu) {

		SoftwareDbStorage swStorage = new SoftwareDbStorage();
		int tmpvehicleId = Integer.parseInt(v.getVehicleID());
		int tmpecuId = ecu.getEcuId();
		int tmpswId = ecu.getSwId();
		int tmpsubId = swStorage.getBiggestSubId(tmpswId);
		int[] result = { tmpswId, tmpsubId };

		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "INSERT INTO installed_ecus VALUES(" + tmpecuId + ","
					+ tmpswId + "," + tmpvehicleId + "," + tmpsubId + ")";
			statement.execute(sql);
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL-klikk i updateInstalledEcus: " + e);
		}

		return result;
	}
	
	private boolean isInInstalledEcus(int ecuNo, int softNo, int vehNo) {

		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			String sql = "SELECT sub_version FROM installed_ecus"
					+ " WHERE ecu_no=" + ecuNo + " AND sw_version = " + softNo
					+ " AND vehicle_id = " + vehNo;
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				return true;
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL-klikk i isInInstalledEcus: " + e);
		}
		return false;
	}

	
	public void updateVehicle(Vehicle v){

		for(int i = 0; i < v.getEcuCount();i++){
			Ecu tmpEcu = v.getEcu(i);
			int tmpSwId = tmpEcu.getSwId();
			
			if (!tmpEcu.isNewest()) {
			try{
				connection = connectToFactoryDb();
				Statement statement = connection.createStatement();
	
					String sql = "UPDATE installed_ecus SET " +
							"sub_version="+tmpEcu.getSubSwId() + " WHERE " +
									"ecu_no = " + tmpEcu.getEcuId() + " AND sw_version=" +
											+ tmpEcu.getSwId()+" AND vehicle_id = " +
											+ Integer.parseInt(v.getVehicleID());
				
					String historyLog = v.getHistoryLog();
					
					setHistoryLog(Integer.parseInt(v.getVehicleID()),historyLog);

					statement.execute(sql);
					
					statement.close();
					connection.close();
			}
			catch(SQLException e){
				System.err.println("SQL-klikk i updateVehicle "+ e);
			}
			}
		}
	}
	
	private void setHistoryLog(int vehiId, String historyLog) throws SQLException{
		
		connection = connectToFactoryDb();
		Statement s = connection.createStatement();
		
		String sql = "UPDATE vehicle SET sw_history_log = \'" + historyLog + "\' WHERE vehicle_id ="+ vehiId;
		s.execute(sql);
		
		s.close();
		connection.close();
	}
	/*
	 * T-E: Overfl�dig metode
	private boolean isInActionScript(int ecuNo, int softNo) {

		try {
			connection = connectToFactoryDb();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT sw_version FROM action_script");
			while (rs.next()) {
				if(rs.getInt(1) == softNo)
					return true;
			}
		} catch (SQLException e) {
			System.err.println("SQL-klikk i isInActionScript: " + e);
		}

		return false;
	}
	*/

	/*
	 * TE: Denne brukes ikke til noe
	 * 
	 * private int getNewestSw(int swVersion){
	 * 
	 * try{ connection = connectToFactoryDb(); Statement statement =
	 * connection.createStatement(); ResultSet rs =
	 * statement.executeQuery("SELECT MAX(sub_version) FROM software_archive
	 * WHERE sw_version ="+swVersion); while(rs.next()) newestMinorVersion =
	 * rs.getInt(1); connection.close(); statement.close(); rs.close(); }
	 * catch(SQLException e){ System.err.println(e); } return newestMinorVersion; }
	 */
	/*
	 * TE: Denne brukes ikke til noe, men kan bli nyttig. private ArrayList<String>
	 * getData(String row,int vehicleId){
	 * 
	 * try{ connection = connectToFactoryDb(); statement =
	 * connection.createStatement(); rs = statement.executeQuery("SELECT " +row+ "
	 * FROM vehicle WHERE vehicle_id =" + vehicleId);
	 * 
	 * while(rs.next()) list.add(rs.getString(1));
	 * 
	 * connection.close(); statement.close(); rs.close(); } catch(SQLException
	 * e){ System.err.println(e); } return list; }
	 */

	// Må finne ut av dette connectiongreiene, closing hvor?
	// Må også bli enig om int eller String på vehicleId.
	public void save(Project project) {
	}
}
