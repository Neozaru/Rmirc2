package rmirc.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;


public interface InterfaceServeurForum extends Remote {
	
	public Set<InterfaceSujetDiscussion> recupereListeDesSujets() throws RemoteException;

	public InterfaceSujetDiscussion obtientSujet( String titre_sujet ) throws RemoteException;
	
	public boolean enregistreSujet( InterfaceSujetDiscussion sujet ) throws RemoteException;
	
}
