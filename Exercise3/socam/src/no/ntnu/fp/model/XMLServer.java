package no.ntnu.fp.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.derby.tools.sysinfo;
import nu.xom.*;

import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Project;
import no.ntnu.fp.model.Vehicle;
import no.ntnu.fp.storage.EcuDbStorage;
import no.ntnu.fp.storage.SoftwareDbStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;
import no.ntnu.fp.storage.VehicleDbStorage;

public class XMLServer {

	/**
	 * Metode som sjekker at headeren fra XMLClient er <get>
	 */
	
	private Document createDocumentFromXML(String xmlString) {
		Document dc = null;
		try {
			dc = new Builder().build(new StringReader(xmlString));
		} catch(Exception e) {
			System.err.println("Noe feilet: " + e);
		}
		return dc;
	}
	
	public boolean validateHeaderFromXMLClient(String xmlString) {
		Document dc = null;
		try {
			dc = new Builder().build(new StringReader(xmlString));
			String getHeader = String.valueOf(dc.getRootElement().getLocalName());
			if (getHeader == "get") {
				return true;
			}
		}
		catch (Exception e) {
			
		}
		
		return false;
	}
	
	public Vehicle getVehicleFromXML(String xmlString) {
		Document dc = null;
		int vehicleID = 0;
		try {
			dc = new Builder().build(new StringReader(xmlString));
			vehicleID = Integer.parseInt(dc.getRootElement().getChild(0).getValue());
		}catch (Exception e) {
			System.err.println("Noe feilet i getVehicleIDFromXML: " + e);
		}
		VehicleDbStorage bil = new VehicleDbStorage();
		Vehicle v = bil.getVehicle(vehicleID);
		return v;
	}
	public Vehicle toVehicleFromClient(String xmlString) {
		Document dc;
		String vehicleId = "";
		String series = "";
		String historyLog = "";
		ArrayList<Ecu> ecuList = new ArrayList<Ecu>();
		Vehicle bila = null;
		try {
			dc = new Builder().build(new StringReader(xmlString));
			vehicleId = String.valueOf(dc.getRootElement().getChild(0).getValue());
//			System.out.println("VehicleId: " + vehicleId);
			series = String.valueOf(dc.getRootElement().getChild(1).getValue());
//			System.out.println("Series: " + series);
			historyLog = String.valueOf(dc.getRootElement().getChild(2).getValue());
//			System.out.println("HistoryLog: " + historyLog);
//			System.out.println("Antall ecu'er i ecus: " + dc.getRootElement().getChild(3).getChildCount());

			for (int i = 0;i < dc.getRootElement().getChild(3).getChildCount(); i++) {
				Element e = (Element)dc.getRootElement().getChild(3).getChild(i);
				int ecuId = Integer.parseInt(e.getChild(0).getValue());
				int swId = Integer.parseInt(e.getChild(1).getValue());
				int subSwId = Integer.parseInt(e.getChild(2).getValue());
				boolean newestSw = (e.getChild(3).getValue().equalsIgnoreCase("true") ? true : false);
				int newSub = Integer.parseInt(e.getChild(4).getValue());
				Ecu ecu = new Ecu(ecuId,swId,subSwId,newestSw, newSub);
//				System.out.println(ecuId + ": " + swId + "." + subSwId + " ," + newestSw + ", " + newSub );
				ecuList.add(ecu);
			}
			bila = new Vehicle(vehicleId,historyLog,ecuList,series);
			
		} catch (Exception e) {
			System.err.println("Noe feilet: " + e);
			e.printStackTrace();
		}
		return bila;
	}
	public String vehicleObjToXML(Vehicle aVehicle) {

		Element root = new Element("vehicle");
		
		Element vehicleId = new Element("vehicleId");
		vehicleId.appendChild(aVehicle.getVehicleID());
		root.appendChild(vehicleId);
		
		Element series = new Element("series");
		series.appendChild(aVehicle.getSeries());
		root.appendChild(series);
		
		Element historyLog = new Element("historyLog");
		historyLog.appendChild(aVehicle.getHistoryLog());
		root.appendChild(historyLog);
		
		Element ecus = new Element("ecus");
		
		EcuDbStorage ecudv = new EcuDbStorage();
		SoftwareDbStorage sdbs = new SoftwareDbStorage();
		
		for (int i = 0; i < aVehicle.getEcuCount();i++) {
			Element ecu = new Element("ecu");
			Element ecuId = new Element("ecuId");
			Element swId = new Element("swId");
			Element subSwId = new Element("subSwId");
			Element newestSw = new Element("newestSw"); //boolean
			Element newestSub = new Element("newestSub"); //integer
			ecuId.appendChild(String.valueOf(aVehicle.getEcu(i).getEcuId()));
			swId.appendChild(String.valueOf(aVehicle.getEcu(i).getSwId()));
			subSwId.appendChild(String.valueOf(aVehicle.getEcu(i).getSubSwId()));
			boolean ecuUpt = ecudv.isUpdated(aVehicle.getEcu(i));
			newestSw.appendChild(String.valueOf(ecuUpt));
			newestSub.appendChild(String.valueOf(sdbs.getBiggestSubId(aVehicle.getEcu(i).getSwId())));
			
			ecu.appendChild(ecuId);
			ecu.appendChild(swId);
			ecu.appendChild(subSwId);
			ecu.appendChild(newestSw);
			ecu.appendChild(newestSub);
			ecus.appendChild(ecu);
		}
		root.appendChild(ecus);
		Document doc = new Document(root);
		
		return doc.toXML();
	}
	
	/**
	 * Takes id makes Vehicle Object
	 * 
	 * @param xmlString
	 * @return
	 */
	public Vehicle toVehicleFromXml(String xmlString) {
		Document dc;
		String vehicleId = "";
		String series = "";
		String historyLog = "";
		ArrayList<Ecu> ecuList = new ArrayList<Ecu>();
		Vehicle bila = null;
		try {
			dc = new Builder().build(new StringReader(xmlString));
			vehicleId = String.valueOf(dc.getRootElement().getChild(0).getValue());
			System.out.println("VehicleId: " + vehicleId);
			series = String.valueOf(dc.getRootElement().getChild(1).getValue());
			System.out.println("Series: " + series);
			historyLog = String.valueOf(dc.getRootElement().getChild(2).getValue());
			System.out.println("HistoryLog: " + historyLog);
			System.out.println("Antall ecu'er i ecus: " + dc.getRootElement().getChild(3).getChildCount());

			for (int i = 0;i < dc.getRootElement().getChild(3).getChildCount(); i++) {
				Element e = (Element)dc.getRootElement().getChild(3).getChild(i);
				int ecuId = Integer.parseInt(e.getChild(0).getValue());
				int swId = Integer.parseInt(e.getChild(1).getValue());
				int subSwId = Integer.parseInt(e.getChild(2).getValue());
				boolean newestSw = Boolean.getBoolean(e.getChild(3).getValue());
				int newSub = Integer.parseInt(e.getChild(4).getValue());
				Ecu ecu = new Ecu(ecuId,swId,subSwId,newestSw, newSub);
				ecuList.add(ecu);
			}
			bila = new Vehicle(vehicleId,historyLog,ecuList,series);
			
		} catch (Exception e) {
			System.err.println("Noe feilet: " + e);
			e.printStackTrace();
		}
		return bila;
	}
}
