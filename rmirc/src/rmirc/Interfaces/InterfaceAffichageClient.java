package rmirc.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceAffichageClient extends Remote {

	public void affiche( InterfaceSujetDiscussion sujet, String message ) throws RemoteException;
	
	public String getUsername() throws RemoteException; 

	/* More */
	//public void notifyUnavailable( InterfaceSujetDiscussion sujet ) throws RemoteException;
	
	public void notifyUserConnect( InterfaceSujetDiscussion sujet, String username ) throws RemoteException; 

	public void notifyUserDisconnect( InterfaceSujetDiscussion sujet, String username ) throws RemoteException; 

	public void notifyUsernameChanged( InterfaceSujetDiscussion sujetDiscussion, String old_name, InterfaceAffichageClient client ) throws RemoteException;
}
