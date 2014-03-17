package no.ntnu.fp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

/**
 * The <code>Project</code> class is a list of zero or more {@link Person} objects.
 * 
 * @author Thomas &Oslash;sterlie
 * @version $Revision: 1.6 $ - $Date: 2008-04-29 18:28:39 $
 *
 */
public class Project implements PropertyChangeListener {

	/**
	 * The member variable storing all registered {@link Person} objects.
	 */
	//Project-objektets egenskaper/attributter
	private java.util.ArrayList personList;
	//Slutt egenskaper
	
	/**
	 * This member variable provides functionality for notifying of changes to
	 * the <code>Project</code> class.
	 */
	private java.beans.PropertyChangeSupport propChangeSupp;
	
	/**
	 * Default constructor.  Must be called to initialise the object's member variables.
	 *
	 */
	public Project() {
		personList = new java.util.ArrayList();
		propChangeSupp = new java.beans.PropertyChangeSupport(this);
	}

	/**
	 * Returns the number of {@linkplain #addPerson(Person) <code>Person</code> objects
	 * registered} with this class.
	 * 
	 * @return The number of {@link Person} objects in this class.
	 */
	public int getPersonCount() {
		return personList.size();
	}
	
	public int getLargestCustId() {
		int largest = 0;
		if (this.personList.size() == 0) {
			return 0;
		}
		for (int i=0; i < this.personList.size(); i++) {
			Person tmpPerson = (Person)this.personList.get(i);
			if (tmpPerson.getCustId() > largest) {
				largest = tmpPerson.getCustId();
			}
		}
		return largest;
	}
	
	/**
	 * Returns the {@link Person} object at the specified position in the list.
	 * 
	 * @param i Index of object to return.
	 * 
	 * @return The {@link Person} object at the specified position in the list.
	 */
	public Person getPerson(int i) {
		return (Person)personList.get(i);
	}
	/**
	 * Search for customer
	 * 
	 */
	public java.util.ArrayList<Integer> getPersonIndex(String srcVal) {
		srcVal = srcVal.toLowerCase();
		java.util.ArrayList<Integer> results = new java.util.ArrayList<Integer>();
		for (int i = 0; i < personList.size(); i++) {
			Person p = (Person)personList.get(i);
			if (p.getName().toLowerCase().contains(srcVal)) {
				results.add(i);
			}
		}
		
		return results;
	}
	/**
	 * Search for vehicleID
	 * 
	 */
	public int getPersonIndex(int vehicleID) {
		for (int i = 0; i < personList.size(); i++) {
			Person p = (Person)personList.get(i);
			if (Integer.parseInt(p.getVehicleID()) == vehicleID) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the index of the first occurrence of the specified object, or 
	 * -1 if the list does not contain this object.
	 * 
	 * @param obj Object to search for.
	 *
	 * @return The index in this list of the first occurrence of the specified element, 
	 * or -1 if this list does not contain this element.
	 */
	public int indexOf(Object obj) {
		return personList.indexOf(obj);
	}

	/**
	 * Returns an iterator over the elements in this list in proper sequence.<P>
	 *
	 * @return A {@link java.util.Iterator} over the elements in this list in proper sequence.
	 * 
	 * @see java.util.Iterator <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Iterator.html">java.util.Iterator</a>.
	 */
	public Iterator iterator() {
		return personList.iterator();
	}

	/**
	 * Adds a new {@link Person} object to the <code>Project</code>.<P>
	 * 
	 * Calling this method will invoke the 
	 * <code>propertyChange(java.beans.PropertyChangeEvent)</code> method on 
	 * all {@linkplain
	 * #addPropertyChangeListener(java.beans.PropertyChangeListener) registered
	 * <code>PropertyChangeListener<code> objecs}.  The {@link java.beans.PropertyChangeEvent}
	 * passed with the {@link java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)}
	 * method has the following characteristics:
	 * 
	 * <ul>
	 * <li>the <code>getNewValue()</code> method returns the {@link Person} object added</li>
	 * <li>the <code>getOldValue()</code> method returns <code>null</code></li>
	 * <li>the <code>getSource()</code> method returns this {@link Project} object
	 * </ul>
	 * 
	 * @param person The {@link Person} object added.
	 * 
	 * @see java.beans.PropertyChangeListener <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/beans/PropertyChangeListener.html">java.beans.PropertyChangeListener</a>
	 * @see java.beans.PropertyChangeEvent <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/beans/PropertyChangeEvent.html">java.beans.PropertyChangeEvent</a>
	 */
	public void addPerson(Person person) {
		personList.add(person);
		person.addPropertyChangeListener(this);
		propChangeSupp.firePropertyChange("person", null, person);
	}
	
	public boolean contains(Person person){
		
		return (personList.contains(person));
	}

	/**
	 * Removes the specified {@link Person} object from the <code>Project</code>.<P>
	 * 
	 * Calling this method will invoke the 
	 * <code>propertyChange(java.beans.PropertyChangeEvent)</code> method on 
	 * all {@linkplain
	 * #addPropertyChangeListener(java.beans.PropertyChangeListener) registered
	 * <code>PropertyChangeListener<code> objecs}.  The {@link java.beans.PropertyChangeEvent}
	 * passed with the {@link java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)}
	 * method has the following characteristics:
	 * 
	 * <ul>
	 * <li>the <code>getNewValue()</code> method returns an {@link java.lang.Integer} object 
	 *     with the index of the removed element</li>
	 * <li>the <code>getOldValue()</code> method returns the {@link Person} object added</li>
	 * <li>the <code>getSource()</code> method returns this {@link Project} object
	 * </ul>
	 * 
	 * @param person The {@link Person} object to be removed.
	 * 
	 * @see java.beans.PropertyChangeListener <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/beans/PropertyChangeListener.html">java.beans.PropertyChangeListener</a>
	 * @see java.beans.PropertyChangeEvent <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/beans/PropertyChangeEvent.html">java.beans.PropertyChangeEvent</a>
	 */
	public void removePerson(Person person) {
		int i = personList.indexOf(person);
		Integer index = new Integer(i);
		personList.remove(person);
		person.removePropertyChangeListener(this);
		propChangeSupp.firePropertyChange("person", person, index);
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
		
		Project aProject = (Project)o;
		
		if (aProject.getPersonCount() != getPersonCount())
			return false;
		
		Iterator it = this.iterator();
		while (it.hasNext()) {
			Person aPerson = (Person)it.next();
			if (aProject.indexOf(aPerson) < 0)
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
