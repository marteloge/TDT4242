package no.ntnu.fp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;


public class FactoryProject implements PropertyChangeListener{
	    //FactoryProject-objektets egenskaper/attributter
		java.util.ArrayList vehicleList;
		java.util.ArrayList softwareList;
		java.util.ArrayList<SimpleEcu> ecuList;
		//Slutt egenskaper
		
		private java.beans.PropertyChangeSupport propChangeSupp;
		
		public FactoryProject() {
			vehicleList = new java.util.ArrayList();
			softwareList = new java.util.ArrayList();
			ecuList = new java.util.ArrayList<SimpleEcu>();
			propChangeSupp = new java.beans.PropertyChangeSupport(this);
		}
		
		public FactoryProject(java.util.ArrayList vehicleList, java.util.ArrayList softwareList, java.util.ArrayList<SimpleEcu> ecuList) {
			this.vehicleList = vehicleList;
			this.softwareList = softwareList;
			this.ecuList = ecuList;
			propChangeSupp = new java.beans.PropertyChangeSupport(this);
		}

		public int getVehicleCount() {
			return vehicleList.size();
		}
		public int getSoftwareCount() {
			return softwareList.size();
		}
		public Vehicle getVehicle(int i) {
			return (Vehicle)vehicleList.get(i);
		}
		
		public Software getSoftware(int i) {
			return (Software)softwareList.get(i);
		}
		public int getEcuCount() {
			return ecuList.size();
		}
		public Software getLatestSoftware() {
			return (Software)softwareList.get(softwareList.size()-1);
		}
		public Vehicle getLatestVehicle() {
			return (Vehicle)vehicleList.get(vehicleList.size()-1);
		}
		public SimpleEcu getLatestEcu() {
			return (SimpleEcu)ecuList.get(ecuList.size()-1);
		}
		public int getSoftwareIndex(Software s) {
			return this.softwareList.indexOf(s);
		}
		
		public SimpleEcu getEcu(int i) {
			return ecuList.get(i);
		}
		
		public int getVehicleIndex(String srcVal) {
			srcVal = srcVal.toLowerCase();
			for (int i = 0; i < vehicleList.size(); i++) {
				Vehicle p = (Vehicle)vehicleList.get(i);
				if (p.getVehicleID().toLowerCase().contains(srcVal)) {
					return i;
				}
			}
			
			return -1;
		}
		
		public int indexOf(Object obj) {
			return vehicleList.indexOf(obj);
		}

		public Iterator iterator() {
			return vehicleList.iterator();
		}

		public void addVehicle(Vehicle vehicle) {
			vehicleList.add(vehicle);
			vehicle.addPropertyChangeListener(this);
			propChangeSupp.firePropertyChange("vehicle", null, vehicle);
		}
		public void addSoftware(Software soft) {
			softwareList.add(soft);
			soft.addPropertyChangeListener(this);
			propChangeSupp.firePropertyChange("software", null, soft);
		}
		public void addEcu(SimpleEcu ecu) {
			ecuList.add(ecu);
			//ecu.addPropertyChangeListener(this);
			//propChangeSupp.firePropertyChange("ecu", null, ecu);
		}
		public void removeVehicle(Vehicle vehicle) {
			int i = vehicleList.indexOf(vehicle);
			Integer index = new Integer(i);
			vehicleList.remove(vehicle);
			vehicle.removePropertyChangeListener(this);
			propChangeSupp.firePropertyChange("vehicle", vehicle, index);
		}

		/**
		 * Add a {@link java.beans.PropertyChangeListener} to the listener list.
		 * 
		 * @param listener The {@link java.beans.PropertyChangeListener} to be added.
		 */
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			propChangeSupp.addPropertyChangeListener(listener);
		}
		
		/**
		 * Remove a {@link java.beans.PropertyChangeListener} from the listener list.
		 * 
		 * @param listener The {@link java.beans.PropertyChangeListener} to be removed.
		 */
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			propChangeSupp.removePropertyChangeListener(listener);
		}

		public void propertyChange(PropertyChangeEvent event) {
			propChangeSupp.firePropertyChange(event);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean equals(Object o) {
			if (super.equals(o))
				return true;

			if (o.getClass() != this.getClass())
				return false;
			
			FactoryProject aFactoryProject = (FactoryProject)o;
			
			if (aFactoryProject.getVehicleCount() != getVehicleCount())
				return false;
			
			Iterator it = this.iterator();
			while (it.hasNext()) {
				Vehicle aVehicle = (Vehicle)it.next();
				if (aFactoryProject.indexOf(aVehicle) < 0)
					return false;
			}
			
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			String s = "project:\n";
			Iterator it = this.iterator();
			while (it.hasNext()) {
				s += it.next().toString() + "\n";
			}
			return s;
		}
		
	}
