package no.ntnu.fp.gui;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;

public class RecallPanel extends JPanel implements ActionListener {
	private FactoryProjectPanel fpPanel;
	private JFormattedTextField SwTextField;
	private JFormattedTextField MinorSwTextField;
	private JButton sendBtn;
	private JLabel headerLbl;
	private JList emailView;
	private JButton getEmailsBtn;
	private boolean pushedGetEmails = false;
	private boolean pushedSendBtn = false;
	
	public RecallPanel(FactoryProjectPanel fpPanel) {
		this.fpPanel = fpPanel;
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(2, 2, 2, 2);
		constraints.insets = insets;
		constraints.anchor = GridBagConstraints.LINE_START;
		
		//Header:
		headerLbl = new JLabel("                              Recall vehicles");
		Font curFont = headerLbl.getFont();
		headerLbl.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 16));
		addGridBagComponent(headerLbl, 0, constraints);
				
		SwTextField = new JFormattedTextField();
        SwTextField.setColumns(10);
        addGridBagLabel("Major software version: ", 1, constraints);
        addGridBagComponent(SwTextField, 1, constraints);
		
		MinorSwTextField = new JFormattedTextField();
        MinorSwTextField.setColumns(10);
        addGridBagLabel("Minor software version: ", 2, constraints);
        addGridBagComponent(MinorSwTextField, 2, constraints);


        sendBtn = new JButton("Send");
        sendBtn.addActionListener(this);
        addGridBagComponent(sendBtn, 3, constraints, 0);

        
        getEmailsBtn = new JButton("Get list");
        getEmailsBtn.addActionListener(this);
        addGridBagComponent(getEmailsBtn, 3, constraints,1);
        
        DefaultListModel model = new DefaultListModel();
        JList list = new JList(model);
        
        

//        list.setMinimumSize(new Dimension(250,300));
//        model.add(0, "hOhO");	
//        model.add(1, "element");
//        model.add(2, "heHE");
//        constraints.gridx = 3;
//		constraints.gridy = 1;
//		constraints.gridheight = 1;
//		constraints.gridwidth = 1;
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.weightx = 1.0;
//		add(list, constraints);
//        //addGridBagComponent(y,6,constraints);
        
 	}
        
    	private void addGridBagLabel(String s, int row, GridBagConstraints constraints) {
    		constraints.gridx = 0;
    		constraints.gridy = row;
    		constraints.gridheight = 1;
    		constraints.gridwidth  = 1;
    		constraints.fill = GridBagConstraints.NONE;
    		constraints.weightx = 0.0;
    		add(new JLabel(s), constraints);
    	}
    	
    	private void addGridBagComponent(Component c, int row, GridBagConstraints constraints) {
    		constraints.gridx = 1;
    		constraints.gridy = row;
    		constraints.gridheight = 1;
    		constraints.gridwidth = GridBagConstraints.REMAINDER;
    		constraints.fill = GridBagConstraints.NONE;
    		constraints.weightx = 1.0;
    		add(c, constraints);
    	}

    	private void addGridBagComponent(Component c, int row, GridBagConstraints constraints, int col) {
    		constraints.gridx = col;
    		constraints.gridy = row;
    		constraints.gridheight = 1;
    		constraints.gridwidth = GridBagConstraints.REMAINDER;
    		constraints.fill = GridBagConstraints.NONE;
    		constraints.weightx = 1.0;
    		add(c, constraints);
    	}

		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == getEmailsBtn) {
				pushedGetEmails = true;
				FactoryProjectPanel.setStatusBar("You may now push the 'Send' button in order to recall vehicles.");
			}
			else if (ae.getSource() == sendBtn && pushedGetEmails == false) {
				FactoryProjectPanel.setStatusBar("ERROR: You need to push the 'Get list' button in order to recall vehicles!");
			}
			else if (ae.getSource() == sendBtn && pushedGetEmails == true) {
				FactoryProjectPanel.setStatusBar("Emails sent!");
				pushedSendBtn = true;
			}
		}
}
