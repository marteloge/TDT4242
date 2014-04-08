package no.ntnu.fp.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;

public class Vehicle implements PropertyChangeListener  {
	//Vehicle-objektets egenskaper/attributter
	private String vehicleID;
	private String series;
	private String historyLog;
	private java.util.ArrayList ecus;
	//Slutt egenskaper
	
	private PropertyChangeSupport propChangeSupp;
	
	public final static String VEHICLEID_PROPERTY_NAME = "vehicleID";
	public final static String HISTORYLOG_PROPERTY_NAME = "historyLog";
	public final static String SERIES_PROPERTY_NAME = "series";
	
	public Vehicle() {
		this.vehicleID = "";
		this.historyLog = "";
		this.series = "";
		this.ecus = new java.util.ArrayList();
		propChangeSupp = new PropertyChangeSupport(this);
	}
	
	public Vehicle(String vehicleID, String historyLog, java.util.ArrayList ecus, String series) {
		this.vehicleID = vehicleID;
		this.historyLog = historyLog;
		this.ecus = ecus;
		this.series = series;
		propChangeSupp = new PropertyChangeSupport(this);
	}
	
	public int getEcuCount() {
		return this.ecus.size();
	}
	
	public Ecu getEcu(int index) {
		return (Ecu)(this.ecus.get(index));
	}
	
	public void addEcu(Ecu ecu) {
		this.ecus.add(ecu);
		ecu.addPropertyChangeListener(this);
		propChangeSupp.firePropertyChange("ecu", null, ecu);
	}
	
	public void removeEcu(Ecu ecu) {
		int i = this.ecus.indexOf(ecu);
		Integer index = new Integer(i);
		ecus.remove(ecu);
		ecu.removePropertyChangeListener(this);
		propChangeSupp.firePropertyChange("ecu", ecu, index);
	}
	public int getLargestEcuId() {
		int largest = 0;
		if (this.ecus.size() == 0) {
			return 0;
		}
		for (int i=0; i < this.ecus.size(); i++) {
			Ecu tmpEcu = (Ecu)this.ecus.get(i);
			if (tmpEcu.getEcuId() > largest) {
				largest = tmpEcu.getEcuId();
			}
		}
		return largest;
	}
	
	public boolean contains(Ecu ecu){

		return (ecus.contains(ecu));
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupp.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupp.removePropertyChangeListener(listener);
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		propChangeSupp.firePropertyChange(event);
	}
	public String getVehicleID() {
		return vehicleID;
	}
	
	public int findDot(String string){
		
		for(int i = 0; i < string.toCharArray().length; i++){
			if(string.toCharArray()[i] == '.')
				return i;
		}
		
		return -1;
	}
	//Denne m� skrives om til � sjekke inne i ECU-OBJEKTENE!!
//	public boolean conatinsSoftware(Vehicle vehicle,int softwareId){
//		
//		String id;
//		for(int i = 0; i < vehicle.getSoftwareSerial().size(); i++){
//			id = vehicle.getSoftwareSerial().get(i);
//			swVersion = id.substring(0,findDot(id));
//			
//			if(Integer.parseInt(swVersion) == softwareId)
//				return true;
//		}
//		return false;		
//	}

	public void setVehicleID(String vehicleID) {
		String oldVeID = this.vehicleID;
		this.vehicleID = vehicleID;
		PropertyChangeEvent event = new PropertyChangeEvent(this, VEHICLEID_PROPERTY_NAME, oldVeID, vehicleID);
		propChangeSupp.firePropertyChange(event);
	}

	public String getHistoryLog() {
		return historyLog;
	}

	public void setHistoryLog(String historyLog) {
		String oldHisLog = this.historyLog;
		this.historyLog = historyLog;
		PropertyChangeEvent event = new PropertyChangeEvent(this, HISTORYLOG_PROPERTY_NAME, oldHisLog, historyLog);
		propChangeSupp.firePropertyChange(event);
	}

	public java.util.ArrayList getEcus() {
		return ecus;
	}

	public void setEcus(java.util.ArrayList ecuSerial) {
		this.ecus = ecuSerial;
	}

	public int indexOf(Ecu e) {
		return this.ecus.indexOf(e);
	}
	
	public Iterator iterator() {
		return ecus.iterator();
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}
}
