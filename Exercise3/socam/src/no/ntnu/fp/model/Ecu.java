package no.ntnu.fp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Ecu {
	//Ecu-objektets egenskaper/attributter
	private int ecuId;
	private int swId;
	private int subSwId;
	private boolean newest; //brukt (ikke obligatorisk) for nyeste subversjon av software tilh�rende aktuell Ecu.
	private int newestSub;
	//Slutt egenskaper
	
	private PropertyChangeSupport propChangeSupp;
	
	public final static String SOFTWARE_PROPERTY_NAME = "swId";
	public final static String SUBSOFTWARE_PROPERTY_NAME = "subSwId";
	public final static String ECUID_PROPERTY_NAME = "ecuId";
	
	public Ecu(int ecuId) {
		this.ecuId = ecuId;
		this.swId = 0;
		this.subSwId = 0;
		propChangeSupp = new PropertyChangeSupport(this);
	}
	public Ecu(int ecuId, int swId, int sub) {
		super();
		this.ecuId = ecuId;
		this.swId = swId;
		this.subSwId = sub;
		propChangeSupp = new PropertyChangeSupport(this);
	}

	public Ecu(int ecuId, int swId, int sub, boolean newest, int newSub) {
		this.ecuId = ecuId;
		this.swId = swId;
		this.subSwId = sub;
		this.newest = newest;
		this.newestSub = newSub;
		propChangeSupp = new PropertyChangeSupport(this);
	}
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupp.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupp.removePropertyChangeListener(listener);
	}
	public int getEcuId() {
		return ecuId;
	}
	public void setEcuId(int ecuId) {
		int oldEcu = this.ecuId;
		this.ecuId = ecuId;
		PropertyChangeEvent event = new PropertyChangeEvent(this, ECUID_PROPERTY_NAME, oldEcu, ecuId);
		propChangeSupp.firePropertyChange(event);
	}
	public int getSwId() {
		return swId;
	}
	public void setSwId(int swId) {
		int oldSw = this.swId;
		this.swId = swId;
		PropertyChangeEvent event = new PropertyChangeEvent(this, SOFTWARE_PROPERTY_NAME, oldSw, swId);
		propChangeSupp.firePropertyChange(event);
	}
	public int getSubSwId() {
		return subSwId;
	}
	public void setSubSwId(int subSwId) {
		int oldSub = this.subSwId;
		this.subSwId = subSwId;
		PropertyChangeEvent event = new PropertyChangeEvent(this, SUBSOFTWARE_PROPERTY_NAME, oldSub, subSwId);
		propChangeSupp.firePropertyChange(event);
	}
	public boolean isNewest() {
		return newest;
	}
	public void setNewest(boolean newest) {
		this.newest = newest;
	}
	public static String getECUID_PROPERTY_NAME() {
		return ECUID_PROPERTY_NAME;
	}
	public int getNewestSub() {
		return newestSub;
	}
	public void setNewestSub(int newestSub) {
		this.newestSub = newestSub;
	}
}
