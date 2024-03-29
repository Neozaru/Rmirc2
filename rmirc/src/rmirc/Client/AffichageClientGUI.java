package rmirc.Client;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import rmirc.Interfaces.InterfaceAffichageClient;
import rmirc.Interfaces.InterfaceServeurForum;
import rmirc.Interfaces.InterfaceSujetDiscussion;

public class AffichageClientGUI extends AffichageClient implements MouseListener, ActionListener {

	private JTextField _text_username;
	private JButton _button_change_username;
	
	private JFrame _main_frame;
	private DefaultListModel _subjects_listmodel;
	private JButton _refresh_button;
	private JList _subjects_list;
	
	private Map<InterfaceSujetDiscussion,FenetreSujetGUI> _fenetres_sujets;

	public AffichageClientGUI(InterfaceServeurForum srv) throws RemoteException {
		this(srv,"Unamed");
	}
	
	public AffichageClientGUI(InterfaceServeurForum srv, String nickname) throws RemoteException {
		super(srv,nickname);
		_fenetres_sujets = new HashMap<InterfaceSujetDiscussion,FenetreSujetGUI>();
	}
	
	@Override
	public void onStart() {
		this.start_gui();
	}
	
	private void start_gui() {
		
		
		_current_user_mode = UserMode.GUI_MODE;
		_main_frame = new JFrame("RMIrc");
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		//gbc.HORIZONTAL;
	    gbc.fill = GridBagConstraints.HORIZONTAL;;
	    gbc.gridx = 1;
	    gbc.gridy = GridBagConstraints.RELATIVE;
	    gbc.fill = GridBagConstraints.VERTICAL;
	    
	    _text_username = new JTextField(_username);
	    _button_change_username = new JButton("Change Username");
	    _button_change_username.addActionListener(this);
	    FlowLayout fl = new FlowLayout();
	    
	    JPanel nick_panel = new JPanel(fl);

	    nick_panel.add(_text_username);
	    nick_panel.add(_button_change_username);
	    
		JPanel _subject_list_panel = new JPanel(gbl);
		//JButton _a_button = new JButton("clic");
		
		_subjects_listmodel = new DefaultListModel();
		
		_refresh_button = new JButton("Refresh");
		_refresh_button.addActionListener(this);
		
		JLabel list_label = new JLabel("Subjects :");
		
		_subjects_list = new JList(_subjects_listmodel);
		_subjects_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//list.addListSelectionListener(this);
		_subjects_list.addMouseListener(this);
		
		_subjects_list.setFixedCellWidth(150);
		_subjects_list.setFixedCellHeight(20);
		
	    JScrollPane scrollPane = new JScrollPane(_subjects_list);
	    _subject_list_panel.add(nick_panel);
	    _subject_list_panel.add(_refresh_button,gbc);
	    _subject_list_panel.add(list_label,gbc);
		_subject_list_panel.add(scrollPane,gbc);
		
		
		_main_frame.setContentPane(_subject_list_panel);
		
		_main_frame.addWindowListener(new WindowAdapter()
		{
	         public void windowClosing(WindowEvent e)
	         {
	        	 System.out.println("Exiting...");
	        	 _main_frame.dispose();
	        	 System.exit(0); //calling the method is a must
	         }
		});
		_main_frame.setBounds(300, 300, 300, 300);
		_main_frame.pack();
		_main_frame.setVisible(true);
		

		this.refresh_subject_list();
	

	}
	
	private void refresh_subject_list() {
		
		
		this.pull_subjects_list();
		
		_subjects_listmodel.removeAllElements();
		for ( InterfaceSujetDiscussion sujet : _sujets_disponibles ) {
			try {
				_subjects_listmodel.addElement(sujet.get_titre());
			} catch (RemoteException e) {
				this.onSubjectUnavailable(sujet);
			}
		}
		
		_subjects_list.repaint();
		
		System.out.println("(Subject List refreshed)");
	}
	
	@Override
	public boolean register_to_subject( InterfaceSujetDiscussion subject ) {
		
		if ( !_fenetres_sujets.containsKey(subject) ) {
			FenetreSujetGUI f = new FenetreSujetGUI(this,subject);
			_fenetres_sujets.put(subject, f);
		
		
			if ( super.register_to_subject(subject) ) {
				
				f.refresh_user_list();
				f.setVisible(true);
				
				return true;
			}
			else {
				f.dispose();
				_fenetres_sujets.remove(subject);
			}
			
		
		}
		return false;
	
	}
	
	@Override
	public boolean unregister_from_subject( InterfaceSujetDiscussion subject ) {
		
		if ( super.unregister_from_subject(subject) ) {
		
			if ( _fenetres_sujets.containsKey(subject) ) {
				_fenetres_sujets.get(subject).dispose();
				_fenetres_sujets.remove(subject);
			}
			
		}
		
		return false;
		
	}

	@Override
	public void affiche(InterfaceSujetDiscussion sujet, String message) throws RemoteException {
		super.affiche(sujet, message);
		
		if ( _fenetres_sujets.containsKey(sujet) ) {
			_fenetres_sujets.get(sujet).print_message(message);
		}
	}    

	@Override
	public void onSubjectUnavailable(InterfaceSujetDiscussion sujet) {
		super.onSubjectUnavailable(sujet);
		
		if ( _fenetres_sujets.containsKey(sujet) ) {
			_fenetres_sujets.get(sujet).dispose();
			_fenetres_sujets.remove(sujet);
		}
		this.refresh_subject_list();
		
	}

	@Override
	public void notifyUserConnect(InterfaceSujetDiscussion sujet,
			String username) throws RemoteException {
		super.notifyUserConnect(sujet, username);
		refresh_subject_window(sujet);
		
	}

	@Override
	public void notifyUserDisconnect(InterfaceSujetDiscussion sujet,
			String username) throws RemoteException {
		super.notifyUserDisconnect(sujet,username);
		refresh_subject_window(sujet);
		
	}
	
	@Override
	public void notifyUsernameChanged( InterfaceSujetDiscussion sujet, String old_name, InterfaceAffichageClient client)
			throws RemoteException {

		this.affiche(sujet, "* "+old_name+" is now known as "+client.getUsername());
		
		if ( _fenetres_sujets.containsKey(sujet) ) {
			_fenetres_sujets.get(sujet).refresh_user_list();
		}
		
		
	}
	
	public void refresh_subject_window( InterfaceSujetDiscussion subject ) {
		
		if ( _fenetres_sujets.containsKey(subject) ) {
			_fenetres_sujets.get(subject).refresh_user_list();
		}
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6234833162366161500L;


	@Override
	public void mouseClicked(MouseEvent e) {

	   
	   if(e.getClickCount() == 2) {
		     int index = _subjects_list.locationToIndex(e.getPoint());
		     ListModel dlm = _subjects_list.getModel();
		     Object item = dlm.getElementAt(index);;
		     _subjects_list.ensureIndexIsVisible(index);
		     System.out.println("Double clicked on " + item);
		     
		     InterfaceSujetDiscussion suj = null;
			try {
				suj = _serveur.obtientSujet(item.toString());
			} catch (RemoteException e1) {
				System.out.println("Erreur : Sujet non dispo");
				this.refresh_subject_list();
			}
		     
		     if ( suj != null ) {
		    	 if (!this.register_to_subject(suj)) {
					this.refresh_subject_list();
		    	 }
		     }
		     
	   }
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if ( e.getSource().equals(_refresh_button) ) {
			this.refresh_subject_list();
		}
		else if ( e.getSource().equals(_button_change_username) ) {
			this.change_username(_text_username.getText());
		}
		
	}

	
	

}
