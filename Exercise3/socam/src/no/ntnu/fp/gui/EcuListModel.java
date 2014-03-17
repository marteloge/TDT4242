package no.ntnu.fp.gui;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.net.URL;

import javax.swing.AbstractListModel;

import no.ntnu.fp.model.Ecu;
import no.ntnu.fp.model.Vehicle;

class EcuListModel extends AbstractListModel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	/**
     * The data model that is wrapped
     */
    private Vehicle vehicle;

    /**
     * Path to where the data model is saved.  <code>null</code> means that the
     * user has not assigned a file to save the model.
     */
    private URL url;

    /**
     * Default constructor. Initialises member variables.
     * 
     * @param project The underlying data model
     * @param url Path to save the data model
     */
    EcuListModel(Vehicle project, URL url) {
    		setVehicle(project);
    		setUrl(url);
    	}

    /**
     * Sets a new underlying data model.
     * 
     * @param project The new underlying data model.
     */
    public void setVehicle(Vehicle project) {
        if (this.vehicle == project) {
            return;
        }

        if (this.vehicle != null) {
        		this.vehicle.removePropertyChangeListener(this);
        }
        this.vehicle = project;
        if (this.vehicle != null) {
        		this.vehicle.addPropertyChangeListener(this);
        }
    }
    
    /**
     * Returns the underlying data model.
     * 
     * @return The underlying data model.
     */
    Vehicle getProject() {
    		return vehicle;
    }

    /**
     * This method is defined in ListModel and
     * is called to get the number of element in the list.
     * In our case it is the number of Person objects in the project.
     * 
     * @return the number of Person objects in the underlying Project object
     */
    public int getSize() {
        return (vehicle == null ? 0 : vehicle.getEcuCount());
    }
    
    /**
     * This method is defined in ListModel and
     * is called to get specific elements in the list.
     * In our case it returns the appropriate Person.
     * 
     * @return the Person object at the specific position in the underlying Project object
     */
    public Object getElementAt(int i) {
      try {
        return (vehicle == null ? null : (Ecu)vehicle.getEcu(i));
      } catch (java.lang.IndexOutOfBoundsException e) { //handling of empty models
	return null;
      }
    }

    /**
     * This method is defined in ProjectListener and
     * is called to notify of changes in the Project structure or
     * to changes in the contained objects' properties
     * 
     * @param event the ProjectEvent detailing what has changed
     */
     public void propertyChange(PropertyChangeEvent event) {
        Object source = event.getSource();
        Ecu ecu = null;
        
        int index;
        if ((source instanceof Vehicle) && (event.getNewValue() instanceof Ecu)) {
        	ecu = (Ecu)event.getNewValue();
        	index = vehicle.indexOf(ecu);
        } else if ((source instanceof Vehicle) && (event.getNewValue() instanceof Integer)) {
        	ecu = (Ecu)event.getOldValue();
        	Integer i = (Integer)event.getNewValue();
        	index = i.intValue();
        } else if (source instanceof Ecu) { 
        	ecu = (Ecu)source;
        	index = vehicle.indexOf(ecu);
        } else {
        	return;
        }

        if ((source instanceof Vehicle) && (event.getNewValue() instanceof Ecu))
        		fireIntervalAdded(vehicle, index, index);
        else if ((source instanceof Vehicle) && (event.getNewValue() instanceof Integer))
        		fireIntervalRemoved(vehicle, index, index);
        else if (source instanceof Vehicle)
        		fireContentsChanged(vehicle, index, index);
        else if (source instanceof Ecu) {
        		fireContentsChanged(ecu, index, index);
        }
    }
     
     /**
      * Sets the path to save the data model.
      * 
      * @param url The path.
      */
     public void setUrl(URL url) {
     	this.url = url;
     }
     
     /**
      * Get the path for the data model.  Returns <code>null</code> if no path has been
      * assigned.
      * 
      * @return The path
      */
     public URL getUrl() {
     	return url;
     }
}

