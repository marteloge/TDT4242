package no.ntnu.fp.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import no.ntnu.fp.model.FactoryProject;

import no.ntnu.fp.model.Vehicle;
import no.ntnu.fp.net.co.GUIServer;
public class FactoryProjectPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * The underlying data model
	 */
	private FactoryProject model;
	
    private VehiclePanel vehiclePanel;
	
	private JPanel cards;
	private SoftwarePanel softwarePanel;
	private EcuPanel ecuPanel;
	private NewVehiclePanel newVehiclePanel;
	private RecallPanel recallPanel;
	private JButton softwareBtn;
	private JButton ecuBtn;
	private JButton vehicleBtn;
	private JButton recallBtn;
	public static java.util.ArrayList<String> statusBarArchive;
	
    private static JLabel lblStatusBar;
    
    public GUIServer factoryConnection;
    
    /**
     * Constructor for objects of class GroupPanel
     */
    public FactoryProjectPanel() {
    	 statusBarArchive = new java.util.ArrayList<String>();
    	 lblStatusBar = new JLabel("Status: ");
    	 
    	 //Menu START
    	 JPanel menuPane = new JPanel();
    	 softwareBtn = new JButton("Software archive");
         softwareBtn.addActionListener(this);
    	 menuPane.add(softwareBtn);
    	 
         ecuBtn = new JButton("Bind software to ECU");
         ecuBtn.addActionListener(this);
         menuPane.add(ecuBtn);
         
         vehicleBtn = new JButton("Vehicle DB");
         vehicleBtn.addActionListener(this);
         menuPane.add(vehicleBtn);
         
         recallBtn = new JButton("Initate recall");
         recallBtn.addActionListener(this);
         menuPane.add(recallBtn);
         
         softwarePanel = new SoftwarePanel(this);
         ecuPanel = new EcuPanel(this);
         newVehiclePanel = new NewVehiclePanel(this);
         newVehiclePanel.setOpaque(true);
         recallPanel = new RecallPanel(this);
         
         cards = new JPanel(new CardLayout());
         cards.add(softwarePanel, "1");
         cards.add(ecuPanel, "2");
         cards.add(newVehiclePanel, "3");
         cards.add(recallPanel, "4");
         JPanel cont = new JPanel();
         cont.setLayout(new BorderLayout());
         
         cont.add(menuPane, BorderLayout.CENTER);
         cont.add(cards, BorderLayout.SOUTH);
         add(cont);
         //Menu END
         
    }

    /**
     * Returns the underlying data model.
     * 
     * @return The underlying data model.
     */
    FactoryProject getModel() {
        return model;
    }
    
//    public void setStatus(String msg) {
//    	FactoryProjectPanel.lblStatusBar.setText(msg);
//    }

    public void setModel(FactoryProject model) {
    		this.model = model;
    }
    
    public VehiclePanel getVehiclePanel() {
    	return this.vehiclePanel;
    }
    
    public SoftwarePanel getSoftwarePanel() {
		return softwarePanel;
	}

	public EcuPanel getEcuPanel() {
		return ecuPanel;
	}

	public RecallPanel getRecallPanel() {
		return recallPanel;
	}

	public static void main(String args[]) {
        JFrame frame = new JFrame(args.length > 0 ? args[0] : "SOCAM - Factory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu netMenu = new JMenu("Net");
        FactoryProjectPanel fProjectPanel = new FactoryProjectPanel();

//        NewAction newAction = new NewAction(fProjectPanel);
//        newAction.putValue(Action.NAME, "New");
        
        OpenAction openAction = new OpenAction(fProjectPanel);
        openAction.putValue(Action.NAME, "Log on");
        
        //SaveAction saveAction = new SaveAction(fProjectPanel);
        //saveAction.putValue(Action.NAME, "Save");
        
        JMenuItem exitAction = new JMenuItem();
        exitAction.setText("Exit");
        exitAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.exit(0);
            }
        });
        DisconnectAction disconnectAction = new DisconnectAction(fProjectPanel);
        disconnectAction.putValue(Action.NAME, "Disconnect");
        
        ConnectAction connectAction = new ConnectAction(fProjectPanel);
        connectAction.putValue(Action.NAME, "Connect");
        
       // fileMenu.add(newAction);
        fileMenu.add(openAction);
       // fileMenu.add(saveAction);
        fileMenu.add(exitAction);
        menuBar.add(fileMenu);
        netMenu.add(connectAction);
        netMenu.add(disconnectAction);
    	menuBar.add(netMenu);
    	frame.setJMenuBar(menuBar);
        Container parent = frame.getContentPane();
        parent.setLayout(new BorderLayout());

        parent.add(fProjectPanel, BorderLayout.CENTER);
        
        lblStatusBar.addMouseListener(new MouseListener() {
    		public void mouseClicked(MouseEvent e) {
    			String list = "";
    			int i = 1;
    			for (String s : FactoryProjectPanel.statusBarArchive) {
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
        parent.add(FactoryProjectPanel.lblStatusBar, BorderLayout.SOUTH);
        frame.pack();
        frame.setSize (750,560);
        frame.setVisible(true);

        fProjectPanel.getNewVehiclePanel().getNwpecuPanel().setModel(new EcuListModel(new Vehicle(), null));
        setStatusBar("Press File->Log on to get data from SOCAM");
	}
    
	public static String getStatusBar() {
		return lblStatusBar.getText();
	}

	public static void setStatusBar(String newStatus) {
		statusBarArchive.add(newStatus);
		lblStatusBar.setText("Status: " + newStatus);
	}

	
	public void actionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout)(cards.getLayout());
		if (e.getSource() == softwareBtn) {
			cl.show(cards, "1" );
		}
		else if (e.getSource() == ecuBtn) {
			cl.show(cards, "2");
		}
		else if (e.getSource() == vehicleBtn) {
			cl.show(cards, "3");
		}
		else if (e.getSource() == recallBtn) {
			setStatusBar("Push the 'Get list' button to get a list of all the emails");
			cl.show(cards, "4");
		}
		else {
			return;
		}
	}

	public NewVehiclePanel getNewVehiclePanel() {
		return newVehiclePanel;
	}

	public void setNewVehiclePanel(NewVehiclePanel newVehiclePanel) {
		this.newVehiclePanel = newVehiclePanel;
	}
}
