package rmirc.Client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rmirc.Interfaces.InterfaceAffichageClient;
import rmirc.Interfaces.InterfaceSujetDiscussion;

public class FenetreSujetGUI extends JFrame implements ActionListener {

	
	
	private JTextField _message_field;
	private JButton _new_message_button;
	private DefaultListModel _users_listmodel;

	private JTextArea _messages_area;
	private JList _user_list;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7232310000234601758L;

	private AffichageClient _client;
	private InterfaceSujetDiscussion _sujet;
	
	
	public FenetreSujetGUI( AffichageClient client, InterfaceSujetDiscussion sujet ) {
		super();
		_sujet = sujet;
		_client = client;
		
		try {
			this.setTitle(_sujet.get_titre());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.addWindowListener(new WindowAdapter()
		{
	         public void windowClosing(WindowEvent e)
	         {
	        	 System.out.println("Exiting...");
	        	 _client.unregister_from_subject(_sujet);
	         }
		});
		
		_users_listmodel = new DefaultListModel();
		
		_new_message_button = new JButton("Send");
		
		_message_field = new JTextField();
		_message_field.setColumns(50);
		
		_messages_area = new JTextArea();
		_messages_area.setEditable(false);
		//_messages_area.setLines(55);
		//_messages_area.set
		JScrollPane scroll_messages = new JScrollPane(_messages_area);

		_new_message_button.addActionListener(this);
		
		_user_list = new JList(_users_listmodel);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		//gbc.HORIZONTAL;
	    gbc.gridx = 1;
	    gbc.gridy = GridBagConstraints.RELATIVE;
	    gbc.fill = GridBagConstraints.BOTH;
	   
	    
		JPanel pane = new JPanel(gbl);

		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		pane.add(scroll_messages,gbc);
		
		
		gbc.gridx = 2;
		pane.add(_user_list,gbc);
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		gbc.gridy = 2;

		gbc.gridx = 1;
	    gbc.fill = GridBagConstraints.VERTICAL;
	    
	    
		pane.add(_message_field,gbc);
		
	    gbc.fill = GridBagConstraints.NONE;

		gbc.gridx = 2;
		pane.add(_new_message_button,gbc);
		
		this.refresh_user_list();
		
		this.setContentPane(pane);
		
		this.setBounds(400, 400, 400, 400);
		this.setSize(new Dimension(640,480));
		this.setMinimumSize(new Dimension(500,200));

		//this.pack();
		this.setVisible(false);
	}
	
	public void print_message( String msg ) {
		_messages_area.append("\n" + msg);
	}

	
	public void refresh_user_list() {

		_users_listmodel.removeAllElements();
		
		try {
			for ( InterfaceAffichageClient user : _sujet.recupererListeUtilisateurs() ) {
				_users_listmodel.addElement(user.getUsername());
			}
		} catch (RemoteException e) {
			_client.onSubjectUnavailable(_sujet);
		}
		
		_user_list.repaint();
		
		System.out.println("(User List refreshed)");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if ( e.getSource().equals(_new_message_button) && !_message_field.getText().isEmpty() ) {

			if ( !_client.send_message_on_subject(_sujet, _message_field.getText()) ) {
				this.dispose();
			}
			
			_message_field.setText("");
			
		
		}
		
	}

	
}
