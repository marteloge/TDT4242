package no.ntnu.fp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Software {
	//Software-objektets egenskaper/attributter
	private int swVersion;
	private int minorVersion;
	private String url;
	//Slutt egenskaper
	
	private PropertyChangeSupport propChangeSupp;
	
	public final static String VERSION_PROPERTY_NAME = "version";
	public final static String minorVersion_PROPERTY_NAME = "minorVersion";
	public final static String URL_PROPERTY_NAME = "url";
	
	public Software() {
		this.swVersion = 0;
		this.minorVersion = 0;
		this.url = "";
		propChangeSupp = new PropertyChangeSupport(this);
	}

	public Software(int swVersion, int minorVersion, String url) {
		this.swVersion = swVersion;
		this.minorVersion = minorVersion;
		this.url = url;
		propChangeSupp = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupp.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupp.removePropertyChangeListener(listener);
	}

	public int getSwVersion() {
		return swVersion;
	}

	public void setSwVersion(int swVersion) {
		int oldswVersion = this.swVersion;
		this.swVersion = swVersion;
		PropertyChangeEvent event = new PropertyChangeEvent(this, VERSION_PROPERTY_NAME, oldswVersion, swVersion);
		propChangeSupp.firePropertyChange(event);
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(int minorVersion) {
		int oldminorVersion = this.minorVersion;
		this.minorVersion = minorVersion;
		PropertyChangeEvent event = new PropertyChangeEvent(this, minorVersion_PROPERTY_NAME, oldminorVersion, minorVersion);
		propChangeSupp.firePropertyChange(event);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		String oldUrl = this.url;
		this.url = url;
		PropertyChangeEvent event = new PropertyChangeEvent(this, URL_PROPERTY_NAME, oldUrl, url);
		propChangeSupp.firePropertyChange(event);
	}

}
