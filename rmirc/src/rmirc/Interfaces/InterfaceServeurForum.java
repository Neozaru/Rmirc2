package rmirc.Interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface InterfaceServeurForum extends Remote {
	
	public Map<String,InterfaceSujetDiscussion> recupereListeDesSujets() throws RemoteException;

	public InterfaceSujetDiscussion obtientSujet( String titre_sujet ) throws RemoteException;
	
	public boolean enregistreSujet( InterfaceSujetDiscussion sujet ) throws RemoteException;
	
}
