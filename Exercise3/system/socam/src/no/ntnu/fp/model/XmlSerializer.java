/*
 * Created on Oct 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.ntnu.fp.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * @author tho
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XmlSerializer {

	public Document toXml(Project aProject) {
		Element root = new Element("project");
		
		Iterator it = aProject.iterator();
		while (it.hasNext()) {
			Person aPerson = (Person)it.next();
			Element element = personToXml(aPerson);
			root.appendChild(element);
		}
		
		return new Document(root);
	}
	
	public Project toProject(Document xmlDocument) throws ParseException {
		Project aProject = new Project();
		Element groupElement = xmlDocument.getRootElement();
		Elements personElements = groupElement.getChildElements("person");
		
		for (int i = 0; i < personElements.size(); i++) {
			Element childElement = personElements.get(i);
			aProject.addPerson(assemblePerson(childElement));
		}
		
		return aProject;
	}

    public Person toPerson(String xml) throws java.io.IOException, java.text.ParseException, nu.xom.ParsingException {
	nu.xom.Builder parser = new nu.xom.Builder(false);
	nu.xom.Document doc = parser.build(xml, "");
	return assemblePerson(doc.getRootElement());
    }
	
	private Element personToXml(Person aPerson) {
		Element element = new Element("person");
		Element custId = new Element("custId");
		custId.appendChild(String.valueOf(aPerson.getCustId()));
		Element name = new Element("name");
		name.appendChild(aPerson.getName());
		Element email = new Element("email");
		email.appendChild(aPerson.getEmail());
		Element city = new Element("city");
		city.appendChild(aPerson.getCity());
		Element street = new Element("street");
		street.appendChild(aPerson.getStreet());
		Element vehicleID = new Element("vehicleID");
		vehicleID.appendChild(String.valueOf(aPerson.getVehicleID()));
		element.appendChild(custId);
		element.appendChild(name);
		element.appendChild(email);
		element.appendChild(city);
		element.appendChild(street);
		element.appendChild(vehicleID);
		return element;
	}
	
	private Person assemblePerson(Element personElement) throws ParseException {
		String name = null, email = null, street = null, city = null, vehicleID = null;
		int custId = 0;
		Element element = personElement.getFirstChildElement("custId");
		if (element != null) {
			custId = Integer.parseInt(element.getValue());
		}
		element = personElement.getFirstChildElement("name");
		if (element != null) {
			name = element.getValue();
		}
		element = personElement.getFirstChildElement("email");
		if (element != null) {
			email = element.getValue();
		}
		element = personElement.getFirstChildElement("city");
		if (element != null) {
			city = element.getValue();
		}
		element = personElement.getFirstChildElement("street");
		if (element != null) {
			street = element.getValue();
		}
		element = personElement.getFirstChildElement("vehicleID");
		if (element != null) {
			vehicleID = element.getValue();
		}
		return new Person(custId, name, email, street, city, vehicleID);
	}
	
	/**
	 * TODO: handle this one to avoid duplicate code
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	private Date parseDate(String date) throws ParseException {
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, java.util.Locale.US);
		return format.parse(date);
	}

}

