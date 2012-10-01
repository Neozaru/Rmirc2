package rmirc.Interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceServeurForum extends Remote {

	public InterfaceSujetDiscussion obtientSujet( String titre_sujet ) throws RemoteException;
	
}
