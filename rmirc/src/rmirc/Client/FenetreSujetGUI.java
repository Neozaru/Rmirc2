package rmirc.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rmirc.Interfaces.InterfaceSujetDiscussion;

public class FenetreSujetGUI extends JFrame implements ActionListener {

	
	
	private JTextField _message_field;
	private JButton _new_message_button;
	
	private JTextArea _messages_area;
	
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
		
		_new_message_button = new JButton("Send");
		
		_message_field = new JTextField();
		_message_field.setColumns(50);
		
		_messages_area = new JTextArea();
		_messages_area.setSize(200, 200);
		_messages_area.setEditable(false);
		
		_new_message_button.addActionListener(this);
		
		JPanel pane = new JPanel();
		pane.add(_messages_area);
		pane.add(_message_field);
		pane.add(_new_message_button);
		
		this.setContentPane(pane);
		
		this.setBounds(400, 400, 400, 400);
		this.pack();
		this.setVisible(true);
	}
	
	public void print_message( String msg ) {
		_messages_area.append("\n" + msg);
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
