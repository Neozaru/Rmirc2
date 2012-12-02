package rmirc.Client;

import java.rmi.RemoteException;

import rmirc.Client.AffichageClient.UserMode;
import rmirc.Interfaces.InterfaceServeurForum;
import rmirc.Interfaces.InterfaceSujetDiscussion;

public class AffichageClientGUI extends AffichageClient {

	public AffichageClientGUI(InterfaceServeurForum srv) throws RemoteException {
		super(srv);
		// TODO Auto-generated constructor stub
	}
	
	public AffichageClientGUI(InterfaceServeurForum srv, String nickname) throws RemoteException {
		super(srv,nickname);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onStart() {
		this.start_gui();
	}
	
	private void start_gui() {
		_current_user_mode = UserMode.GUI_MODE;
	}

	@Override
	public void affiche(InterfaceSujetDiscussion sujet, String message) throws RemoteException {
		
		System.out.println("["+sujet.get_titre()+"] " + message);
		
	}    

	@Override
	public void notifyUnavailable(InterfaceSujetDiscussion sujet)
			throws RemoteException {
		super.notifyUnavailable(sujet);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyUserConnect(InterfaceSujetDiscussion sujet,
			String username) throws RemoteException {
		super.notifyUserConnect(sujet, username);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyUserDisconnect(InterfaceSujetDiscussion sujet,
			String username) throws RemoteException {
		super.notifyUserDisconnect(sujet,username);
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -6234833162366161500L;
	
	

}
