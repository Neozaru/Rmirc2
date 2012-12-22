package rmirc.Fournisseur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import rmirc.Interfaces.InterfaceAffichageClient;
import rmirc.Interfaces.InterfaceSujetDiscussion;

public class SujetDiscussion extends UnicastRemoteObject implements InterfaceSujetDiscussion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6673626884454566833L;
	
	
	private final String _titre;
	private Set<InterfaceAffichageClient> _clients;
	
	public SujetDiscussion( String titre_sujet ) throws RemoteException {
		_titre = titre_sujet;
		_clients = new HashSet<InterfaceAffichageClient>();
	}
	
	@Override
	public synchronized void inscription(InterfaceAffichageClient client)
			throws RemoteException {

		_clients.add(client);

		for (InterfaceAffichageClient a_client : _clients) {
			a_client.notifyUserConnect(this, client.getUsername());
		}
		
	}

	@Override
	public synchronized void desInscription(InterfaceAffichageClient client)
			throws RemoteException {

		if ( client != null && _clients.contains(client) ) {
			client.affiche(this, "Vous etes maintenant desinscrit du sujet "+_titre);
			_clients.remove(client);
		}
		else {
			client.affiche(this, "[WTF] Vous n'etes pas inscrit au sujet "+_titre);
		}
		
		for (InterfaceAffichageClient a_client : _clients) {
			a_client.notifyUserDisconnect(this, client.getUsername());
		}
		
	}

	@Override
	public synchronized void diffuse(String message) throws RemoteException {

		Iterator<InterfaceAffichageClient> iterator = _clients.iterator();
		
		while ( iterator.hasNext() ) {
			iterator.next().affiche(this,message);
		}
		
	}
	
	@Override
	public boolean diffuse(InterfaceAffichageClient client, String message)
			throws RemoteException {
		
		if ( client != null && _clients.contains(client) ) {
			this.diffuse("["+client.getUsername() + "] : " + message);
			return true;
		}
		
		return false;
		
	}

	@Override
	public String get_titre() throws RemoteException {

		return _titre;
	}

	@Override
	public Set<InterfaceAffichageClient> recupererListeUtilisateurs()
			throws RemoteException {
		
		
		return _clients;
	}

	@Override
	public void notifyUsernameChanged(String old_username,
			InterfaceAffichageClient iface_client) throws RemoteException {

		for (InterfaceAffichageClient a_client : _clients) {
			a_client.notifyUsernameChanged(this, old_username, iface_client);
		}
		
	}



	
	
}
