package rmirc.Serveur;

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
		client.affiche("Vous etes inscrit au sujet "+_titre+" ! :)");
		
	}

	@Override
	public synchronized void desInscription(InterfaceAffichageClient client)
			throws RemoteException {

		if ( _clients.contains(client) ) {
			_clients.remove(client);
			client.affiche("Vous etes maintenant desinscrit du sujet "+_titre);
		}
		else {
			client.affiche("[WTF] Vous n'etes pas inscrit au sujet "+_titre);
		}
		
	}

	@Override
	public synchronized void diffuse(String message) throws RemoteException {

		Iterator<InterfaceAffichageClient> iterator = _clients.iterator();
		
		while ( iterator.hasNext() ) {
			iterator.next().affiche(message);
		}
		
	}

	@Override
	public String get_titre() throws RemoteException {

		return _titre;
	}

	
	
}
