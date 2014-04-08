package no.ntnu.fp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SimpleEcu {
	//Ecu-objektets egenskaper/attributter
	private int ecuId;
	private int swId;
	//Slutt egenskaper
	
	private PropertyChangeSupport propChangeSupp;
	
	public final static String SOFTWARE_PROPERTY_NAME = "swId";
	public final static String ECUID_PROPERTY_NAME = "ecuId";
	
	public SimpleEcu(int ecuId) {
		this.ecuId = ecuId;
		this.swId = 0;
		propChangeSupp = new PropertyChangeSupport(this);
	}
	public SimpleEcu() {
		this.ecuId = 0;
		this.swId = 0;
		propChangeSupp = new PropertyChangeSupport(this);
	}
	public SimpleEcu(int ecuId, int swId) {
		this.ecuId = ecuId;
		this.swId = swId;
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
}
