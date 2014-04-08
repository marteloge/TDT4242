package no.ntnu.fp.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Project;
import no.ntnu.fp.net.co.GUIConnect;

import no.ntnu.fp.storage.FileStorage;

/**
 * GroupPanel is JPanel for presenting the hierarhical Group structure and editing its contents.
 * GroupPanel instantiated three panes that support viewing and editing:
 * 
 * <ul>
 * <li>the Group tree (a JTree, with supporting GroupTreeModel, GroupTreeCellRenderer and GroupTreeCellEditor classes)</li>
 * <li>the Person list (JList and JTable in a JTabbedPane, with supporting PersonListModel, PersonTableModel and PersonCellRenderer)</li>
 * <li>Person panel (PersonPanel)</li>
 * </ul>
 * 
 * GroupPanel implements main logic connecting the three panes, by listening to selection events.
 * 
 * @author Hallvard Tr�tteberg
 * @author Thomas &Oslash;sterlie
 * 
 * @version $Revision: 1.22 $ - $Date: 2008-05-02 15:00:53 $
 */
public class ProjectPanel extends JPanel implements ListSelectionListener, ListDataListener {
	private static final long serialVersionUID = 1L;
	/**
	 * The underlying data model
	 */
	private PersonListModel model;
	public static VehiclePanel statVehPanel;
    private JList personList;
    private ListSelectionModel personSelection;
    private PersonPanel personPanel;
    public VehiclePanel vehiclePanel;
    private static JLabel lblStatusBar;
    public static java.util.ArrayList<String> statusBarArchive;
    
    //Inneholder det man trenger for å sende
    public GUIConnect garageConnection;
    
    /**
     * Constructor for objects of class GroupPanel
     */
    public ProjectPanel() {
    	 lblStatusBar = new JLabel("Status: Press File -> Log on to get data from SOCAM");
    	 statusBarArchive = new java.util.ArrayList<String>();
    	 
    	 setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
         personList = new JList();
         personList.setCellRenderer(new PersonCellRenderer());
         personSelection = personList.getSelectionModel();
         personSelection.addListSelectionListener(this);
         JScrollPane listScrollPane = new JScrollPane(personList);
         listScrollPane.setMinimumSize(new Dimension(250, 400));
         
         personPanel = new PersonPanel(this);
         personPanel.setMinimumSize(new Dimension(300,400));
         personPanel.setEditable(false);
         
         vehiclePanel = new VehiclePanel(this);
         vehiclePanel.setMinimumSize(new Dimension(400,400));
         vehiclePanel.setEditable(false);
         
         ProjectPanel.statVehPanel = vehiclePanel;
         
         add(listScrollPane);
         add(Box.createHorizontalStrut(20));
         add(personPanel);
         add(Box.createHorizontalStrut(20));
         add(vehiclePanel);
    }

    /**
     * Returns the underlying data model.
     * 
     * @return The underlying data model.
     */
    PersonListModel getModel() {
        return model;
    }
    
//    public void setStatus(String msg) {
//    	ProjectPanel.lblStatusBar.setText(msg);
//    }

    /**
     * Sets a new underlying data model for the {@link ProjectPanel}.  The first element of
     * the data model will automatically be selected.<P>
     * 
     * WARNING: Changes in the existing data model will be discarded.
     * 
     * @param model The underlying data model.
     */
    public void setModel(PersonListModel model) {
    		this.model = model;
    		personList.setModel(model);
    		model.addListDataListener(this);
    		listElementSelected((Person)model.getElementAt(0));
    		personList.setSelectedIndex(0);
    }

    /**
     * Returns the index of the element selected in <code>personList</code>.
     * 
     * @return Index of the selected element.
     */
    public int getSelectedElement() {
    	return personList.getSelectedIndex();
    }
    
    public void setSelectedElement(int index) {
		personList.setSelectedIndex(index);
    }
    
    /**
     * Called when a Person is selected in (one of) the list(s).
     * 
     * @param p The selected {@link no.ntnu.fp.model.Person} object.
     */
    private void listElementSelected(Person p) {
        personPanel.setModel(p);
        personPanel.setEditable(p != null);
        vehiclePanel.setEditable(p != null);
    }

    /**
     * ListSelectionListener, called when the JList or JTable selection changes.
     * 
     * @param e The ListSelectionEvent
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == personSelection) {
            listElementSelected((Person)personList.getSelectedValue());
        }
    }

    /**
     * ListDataListener methods, called when the underlying (Person)ListModel is added to
     */
    public void intervalAdded(ListDataEvent e) {
        personList.setSelectedIndex(e.getIndex0());
    }
    
    public VehiclePanel getVehiclePanel() {
    	return this.vehiclePanel;
    }
    
    public PersonPanel getPersonPanel() {
    	return this.personPanel;
    }
    
    public void contentsChanged(ListDataEvent e) {}
    
    /**
     * This method is invoked when a person is deleted from the list.  If the first person in
     * the list was removed, the method will select the new first person.  If the last person
     * in the list is removed, the method will select the new last person.  Else the method
     * will select the person after the person removed in the list.
     */
    public void intervalRemoved(ListDataEvent e) {
    		int index = e.getIndex0();
    		if (index == 0) 
    			personList.setSelectedIndex(1);
    		else if (index >= model.getSize()-1)
    			personList.setSelectedIndex(personList.getSelectedIndex()-1);
    		else
    			personList.setSelectedIndex(index+1);
    }

    /**
     * Main entry point for application.
     * Takes the frame title as a command line argument.
     */
    public static void main(String args[]) {
        JFrame frame = new JFrame(args.length > 0 ? args[0] : "SOCAM - Garage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu netMenu = new JMenu("Net");
        
        ProjectPanel projectPanel = new ProjectPanel();
        
        //NewAction newAction = new NewAction(projectPanel);
        //newAction.putValue(Action.NAME, "New");
        
        OpenAction openAction = new OpenAction(projectPanel);
        openAction.putValue(Action.NAME, "Log on");
        
        SaveAction saveAction = new SaveAction(projectPanel);
        saveAction.putValue(Action.NAME, "Save");
        
        JMenuItem exitAction = new JMenuItem();
        exitAction.setText("Exit");
        exitAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.exit(0);
            }
        });
        
        AddPersonAction addPersonAction = new AddPersonAction(projectPanel);
        addPersonAction.putValue(Action.NAME, "Add customer");
        
        RemovePersonAction removePersonAction = new RemovePersonAction(projectPanel);
        removePersonAction.putValue(Action.NAME, "Remove customer");
        
        SearchPersonAction searchPersonAction = new SearchPersonAction(projectPanel);
        searchPersonAction.putValue(Action.NAME, "Search customer");
        
        SearchVehicleIDAction searchVehicleIDAction = new SearchVehicleIDAction(projectPanel);
        searchVehicleIDAction.putValue(Action.NAME, "Search VehicleID");

        DisconnectAction disconnectAction = new DisconnectAction(projectPanel);
        disconnectAction.putValue(Action.NAME, "Disconnect");
        
        ConnectAction connectAction = new ConnectAction(projectPanel);
        connectAction.putValue(Action.NAME, "Connect");
        
        //fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.add(exitAction);
	menuBar.add(fileMenu);
	editMenu.add(addPersonAction);
	editMenu.add(removePersonAction);
	editMenu.add(searchPersonAction);
	editMenu.add(searchVehicleIDAction);
	menuBar.add(editMenu);
	netMenu.add(connectAction);
	netMenu.add(disconnectAction);
	menuBar.add(netMenu);
	
	frame.setJMenuBar(menuBar);
	Container parent = frame.getContentPane();
	parent.setLayout(new BorderLayout());
	
	parent.add(projectPanel, BorderLayout.CENTER);
	
	lblStatusBar.addMouseListener(new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			String list = "";
			int i = 1;
			for (String s : ProjectPanel.statusBarArchive) {
				list += i + ". " + s + "\n";
				i++;
			}
			JOptionPane.showMessageDialog(null, list, "Status updates", JOptionPane.INFORMATION_MESSAGE);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	});
	parent.add(ProjectPanel.lblStatusBar, BorderLayout.SOUTH);
	
        frame.pack();
        frame.setSize (1000,500);
        frame.setVisible(true);

	projectPanel.setModel(new PersonListModel(new Project(), null));
    }

	public static String getStatusBar() {
		return ProjectPanel.lblStatusBar.getText();
	}

	public static void setStatusBar(String newStatus) {
		statusBarArchive.add(newStatus);
		lblStatusBar.setText("Status: " + newStatus);
	}
}
