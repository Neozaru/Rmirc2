package rmirc.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import rmirc.Interfaces.InterfaceAffichageClient;
import rmirc.Interfaces.InterfaceServeurForum;
import rmirc.Interfaces.InterfaceSujetDiscussion;


public class AffichageClient extends UnicastRemoteObject implements InterfaceAffichageClient {

	public enum UserMode {
		UNKNOWN,
		CLI_MODE,
		GUI_MODE
	};
    
	
	protected InterfaceServeurForum _serveur;
	
	protected Map<String,InterfaceSujetDiscussion> _sujets_disponibles;
	protected Map<String,InterfaceSujetDiscussion> _sujets_suivis;
	protected String _username;
	
	protected UserMode _current_user_mode = UserMode.UNKNOWN;

    /**
	 * 
	 */
	private static final long serialVersionUID = 1582318348755837843L;
	
	public AffichageClient( InterfaceServeurForum srv ) throws RemoteException {
		this(srv,null);
	}

	public AffichageClient( InterfaceServeurForum srv, String username ) throws RemoteException {
		
		_serveur = srv;
		_sujets_disponibles = new HashMap<String,InterfaceSujetDiscussion>();
		_sujets_suivis = new HashMap<String,InterfaceSujetDiscussion>();
		_username = ( username != null ) ? username : "Unamed";
		
		if ( _serveur != null ) {
			//_serveur.recupereListeDesSujets();
		}
    }
	
	protected void pull_subjects_list() {
		
		try {
			_sujets_disponibles = _serveur.recupereListeDesSujets();
		} catch (RemoteException e) {
			System.out.println("Server unreachable. (is it online ?)");
			System.out.println("Exiting ...");
			System.exit(0);
		}
		
	}
	
	private InterfaceSujetDiscussion find_subject( String subject_name ) {
		
		try {
			InterfaceSujetDiscussion sujet =  _serveur.obtientSujet(subject_name);
			return sujet;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public boolean register_to_subject( InterfaceSujetDiscussion subject ) {
		
		try {
			subject.inscription(this);
			_sujets_suivis.put(subject.get_titre(), subject);
			return true;
		} catch (RemoteException e) {
			
		}
		
		return false;
		
	}
	
	public boolean unregister_from_subject( InterfaceSujetDiscussion subject ) {
		
		try {
			subject.desInscription(this);
			_sujets_suivis.remove(subject.get_titre());
			return true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	public boolean send_message_on_subject( InterfaceSujetDiscussion subject, String message ) {
		
		try {
			boolean ret = subject.diffuse(this,message);
			return ret;
		}
		catch ( ConnectException e ) {
			System.out.println("Erreur : Connexion avec le canal perdue. Tentez de rejoindre le canal.");
		} 
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void onStart() {
		this.start_cli();
	}
	
	
	protected void start_cli() {
		_current_user_mode = UserMode.CLI_MODE;

		 String prompt = "> ";
         String cmd = "";
         
         print_help();
         System.out.print(_username+prompt);
         while ( ( cmd = wait_for_cmd() ) != "exit" ) {
         	
         	if ( cmd.equals("/help") || cmd.equals("help") ) {
         		print_help();
         	}
			else if ( cmd.equals("/followed") ) {
				
				System.out.println("Followed subjects :");
				for ( String titre : _sujets_suivis.keySet() ) {
					System.out.println("- "+titre);
				}

			}
         	else {
         		
         		String[] spl = cmd.split(" ");
         		
         		if ( spl.length > 1 ) {
         			
         			InterfaceSujetDiscussion suj = this.find_subject(spl[1]);
         			
         			if ( suj != null ) {
	            			
	            			if ( spl[0].equals("/join") ) {
	            				
	            				//suj.inscription(this);            			
	            				if ( !this.register_to_subject(suj) ) {
	            					System.out.println("Error : Unable to register in Channel '"+spl[1]+"'");
	            				}
	            				
	            			}
	            			else if ( spl[0].equals("/part") ) {
	            				//suj.desInscription(this);
	            				if ( !this.unregister_from_subject(suj) ) {
	            					System.out.println("Error : Unable to unregister from Channel '"+spl[1]+"'");
	            				}

	            			}

	            			else if ( spl[0].equals("/msg") && spl.length > 2 ) {
	            				
	            				if ( !this.send_message_on_subject(suj,spl[2]) ) {
	            					System.out.println("Error : Are you registered channel '"+spl[1]+"' ?");
	            				}
	            	
	            			}
         			}
         			else {
         				System.out.println(spl[1]+" : Subject not found !");
         			}
         			
         			
         		}
         		
         		
         	}
     
             System.out.print(_username+prompt);

         }
	}
	
	/* Begin implemented methods */

	@Override
	public void affiche(InterfaceSujetDiscussion sujet, String message) throws RemoteException {
		
		System.out.println("["+sujet.get_titre()+"] " + message);
		
	}    
	

	@Override
	public String getUsername() throws RemoteException {
		return _username;
	}

	
	@Override
	public void notifyUnavailable(InterfaceSujetDiscussion sujet)
			throws RemoteException {
		_sujets_disponibles.remove(sujet.get_titre());
		_sujets_suivis.remove(sujet.get_titre());
		
	}

	@Override
	public void notifyUserConnect(InterfaceSujetDiscussion sujet,
			String username) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyUserDisconnect(InterfaceSujetDiscussion sujet,
			String username) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	
	public static String wait_for_cmd() {
		
	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	      String cmd = "";

	      try {
	    	  cmd = br.readLine();
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read argument!");
	         System.exit(1);
	      }
	      
	      return cmd;
	      
	}
	
	public static void print_help() {
		System.out.println("Available commands : ");
		System.out.println("	/join <subject>");
		System.out.println("	/part <subject>");
		System.out.println("	/msg <subject> <message>");
	}
	
    public static void main(String args[]) {
    	
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
        	
        	String mode = "cli";
        	
            String name = "TohuBohu";
            Registry registry = LocateRegistry.getRegistry(0);
            
            String nickname = "Unamed";
            if ( args.length > 0 ) {
            	nickname = args[0];
            	
            	if (args.length > 1 ) {
            		
            		mode = args[1];
            		
            	}
            	
            }
            
            InterfaceServeurForum srv = (InterfaceServeurForum) registry.lookup(name);
            
            if ( srv != null ) {
	            AffichageClient ac = null;
	            
	            if ( mode.equals("gui")) {
	            	ac = new AffichageClientGUI(srv,nickname);
	            }
	            else {
	            	ac = new AffichageClient(srv,nickname);
	            }
	            
	            ac.onStart();
            }
            else {
            	System.out.println("Unable to find Server");
            }
            
            
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }


    
}
