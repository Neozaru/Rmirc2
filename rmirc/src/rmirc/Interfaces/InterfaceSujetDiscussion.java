package rmirc.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceSujetDiscussion extends Remote {

	public void inscription( InterfaceAffichageClient iface_client ) throws RemoteException;
	
	public void desInscription( InterfaceAffichageClient iface_client ) throws RemoteException;
	
	public void diffuse( String message ) throws RemoteException;
	
	public boolean diffuse( InterfaceAffichageClient iface_client, String message ) throws RemoteException;
	
	public String get_titre() throws RemoteException;
	
	
}
