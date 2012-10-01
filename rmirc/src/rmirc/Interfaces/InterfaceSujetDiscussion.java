package rmirc.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceSujetDiscussion extends Remote {

	public void inscription( InterfaceAffichageClient iface_client ) throws RemoteException;
	
	public void desInscription( InterfaceAffichageClient iface_client ) throws RemoteException;
	
	public void diffuse( String message ) throws RemoteException;
	
	
}
