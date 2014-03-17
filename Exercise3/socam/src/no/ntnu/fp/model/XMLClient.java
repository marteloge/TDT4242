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


public class XMLClient {
	private int vehicleId;
	private Project project;
	private String xmlProject;

	public XMLClient(){
	}
	
	private void setVehicleIDFromXml(String xml) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Bruker project for � sette xmlProject
	 * 
	 * IKKE FERDIG
	 * 
	 * @return
	 */
	private void makeXmlFromProject() {

	}
	/**
	 * Bruker vehicleId for � sette project.
	 * 
	 * IKKE FERDIG
	 * 
	 * @return
	 */
	private void makeProjectFromDB() {

	}


	/**
	 * [Client]
	 * 
	 * @param aPerson
	 * @return
	 */
	public String vehicleIDToXml(Vehicle v) {
		String h = null;
		Element root = new Element("get");
		Document testa = new Document(root);
		Element vehicleID = new Element("vehicleId");
		vehicleID.appendChild(v.getVehicleID());
		root.appendChild(vehicleID);
		//System.out.println(testa.getRootElement().toXML());
		h = testa.getRootElement().toXML();
		return h;
	}
	
	
	/**
	 * [Client]
	 * @param xmlDocument
	 * @return
	 * @throws ParseException
	 */
	public Project toProject(Document xmlDocument) throws ParseException {
		Project aProject = new Project();
		Element groupElement = xmlDocument.getRootElement();
		Elements personElements = groupElement.getChildElements("person");

		for (int i = 0; i < personElements.size(); i++) {
			//Element childElement = personElements.get(i);
			//aProject.addPerson(assemblePerson(childElement));
		}
		return aProject;
	}
	
	/**
	 * Denne metoden h�ndterer return fra Server, i form av en String. Genererer et object.
	 */
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
			newestSw.appendChild(String.valueOf(aVehicle.getEcu(i).getNewestSub()));
			newestSub.appendChild(String.valueOf(aVehicle.getEcu(i).getNewestSub()));
			
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
	
	public Vehicle toVehicleFromServer(String xmlString) {
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
	
	public String vehicleToXml(Vehicle aVehicle) {

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
		for (int i = 0; i < aVehicle.getEcuCount();i++) {
			Element ecu = new Element("ecu");
			Element ecuId = new Element("ecuId");
			Element swId = new Element("swId");
			Element subSwId = new Element("subSwId");
			Element newestSw = new Element("newestSw");
			
			ecuId.appendChild(String.valueOf(aVehicle.getEcu(i).getEcuId()));
			swId.appendChild(String.valueOf(aVehicle.getEcu(i).getSwId()));
			subSwId.appendChild(String.valueOf(aVehicle.getEcu(i).getSubSwId()));
			newestSw.appendChild(String.valueOf(ecudv.isUpdated(aVehicle.getEcu(i))));
			
			ecu.appendChild(ecuId);
			ecu.appendChild(swId);
			ecu.appendChild(subSwId);
			ecu.appendChild(newestSw);
			ecus.appendChild(ecu);
		}
		root.appendChild(ecus);
		Document doc = new Document(root);	
		return doc.toXML();
	}
	
	/**
	 * Teste litt
	 * @param args
	 */
//	public static void main(String[] args) {
//		String xml = "<vehicle><vehicleId>4123</vehicleId><series>A5</series><historyLog>23.2.08 - Soft.upgr</historyLog><ecus><ecu><ecuId>45</ecuId><swId>23</swId><subSwId>41</subSwId><newestSw>false</newestSw></ecu><ecu><ecuId>46</ecuId><swId>27</swId><subSwId>50</subSwId><newestSw>false</newestSw></ecu><ecu><ecuId>47</ecuId><swId>43</swId><subSwId>10</subSwId><newestSw>false</newestSw></ecu><ecu><ecuId>48</ecuId><swId>29</swId><subSwId>50</subSwId><newestSw>false</newestSw></ecu></ecus></vehicle>";
//		toVehicleFromServer(xml);
//		Person p2 = new Person(1,"stian","epost","","","2433");
//		vehicleIDToXml(p2);
//		System.out.println("Formatert XML string som sendes til factory: " + vehicleIDToXml(p2));
//		System.out.println("Formatert XML string som sendes til client igjen: " + xml);
////		System.out.println("Formatert XML string som blir returnert fra factory: " + );
//	}
}

