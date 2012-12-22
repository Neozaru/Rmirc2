package rmirc.Fournisseur;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmirc.Interfaces.InterfaceServeurForum;
import rmirc.Interfaces.InterfaceSujetDiscussion;

public class FournisseurDeSujet {

	
    public static void main(String[] args) {
    	
    	String[] liste_nouveaux_sujets = null;
    	
    	if ( args.length > 0 ) {
    		liste_nouveaux_sujets = args;
    	}
    	

    	if ( liste_nouveaux_sujets == null ) {
    		System.out.println("Usage : "+ "FounisseurDeSujet" + " <new_channel_name> [<new_channel_name>] ...");
    		System.exit(1);
    	}
    	
    	
        if (System.getSecurityManager() == null) {
  
           System.setSecurityManager(new SecurityManager());
            
        }
        try {
        	
        	
        	String server_name = "TohuBohu";   	
            Registry registry = LocateRegistry.getRegistry(0);

            //String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/"+server_name+"_channel_"+titre;
            InterfaceServeurForum forum = (InterfaceServeurForum) registry.lookup(server_name);
            
            
            for ( int i=0; i<liste_nouveaux_sujets.length; i++ ) {
            	
            	String titre = liste_nouveaux_sujets[i];
	            InterfaceSujetDiscussion channel = new SujetDiscussion( titre );
	            
	            
	            if ( forum.enregistreSujet(channel) ) {
	                System.out.println("Sujet " + titre + " cree");
	            }
	            else {
	                System.out.println("Impossible de creer " + titre + ". Sujet deja existant ?");
	
	            }
	            
            }
            
            
        } catch (Exception e) {
        	
            System.err.println("Server Unreachable (is it online ?)");
            System.exit(0);
            //e.printStackTrace();
          
        }
        
    }


}
