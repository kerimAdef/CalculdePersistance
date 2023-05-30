package Serveur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;




public class Serveurs extends Thread{
    
   
	private ServerSocket serveur;
	static int[][] tableau;
    static Hashtable<Boolean,Hashtable<String,Integer>> tableH; 
	static FileWriter fw; 
	private Scanner scanner = new Scanner(System.in);
	

	public Serveurs()  {
	
	try {
		this.serveur =new ServerSocket(1242);
		Serveurs.tableH = new Hashtable<Boolean,Hashtable<String,Integer>>();
		Serveurs.tableau= new int[2][100];
		Serveurs.fw = new FileWriter(new File("FichierPersistence"));
		
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	
	System.out.println("Serveur démarre, en attente de la connexion...");
	
	}
	
	@Override 
	public void run(){
	try {int nbrWorker=0;
		System.out.println("Saisissez le nombre de Worker ");
		nbrWorker=scanner.nextInt();
		
			 
	 while(true){
		 
		 
			 
		Socket socket =serveur.accept();
		 if(nbrWorker==0)
		 {new ConnexionClient(socket).start();}
		 else {
			 new ConnexionWorker(nbrWorker,socket).start();
			 nbrWorker--;}
	 }
	    
	      
	 }catch (IOException e) {
		e.printStackTrace();
	}
	 
	} 
	
	//Pour gérer la connexion avec le worker 
	
	class ConnexionWorker extends Thread{
		//attribut
		private int id;
		private Socket socket;
		
		//constructeur
		
		public ConnexionWorker(int id,Socket s) {
			this.id=id;
			this.socket=s;
		}
		@Override 
		public void run(){
			try {
				InputStream recu=socket.getInputStream();
				BufferedReader afRecu= new BufferedReader( new InputStreamReader(recu));
				
				OutputStream envoi=socket.getOutputStream();
				PrintWriter pw=new PrintWriter(envoi,true);
				System.out.println("Connexion du Worker"+id);
				String req=afRecu.readLine();
				System.out.println("nbre de charge"+ req);
				Hashtable<String, Integer> worker= new Hashtable<String, Integer>();
				worker.put("Worker"+id, Integer.parseUnsignedInt(req));
				Serveurs.tableH.put(true,worker);
				while(true){
					  
					
					int i=(int) (2000*Math.random());
					pw.println(i); 
					String nbr=afRecu.readLine();
					System.out.println("le chiffre "+i+" a pour persistence"+ nbr);
					
				    Serveurs.tableau[0][tableau.length]=i;
				    Serveurs.tableau[1][tableau.length]=Integer.parseUnsignedInt(nbr);
			        Serveurs.fw.write("le chiffre "+i+" a pour persistence"+ nbr+ "\n");
			        Serveurs.fw.flush();
			        
					
				}
				 
			} catch (IOException e) {
				
				e.printStackTrace();}
		}
	}
	
	public static void main(String[] args) {
		Serveurs  serveur=new Serveurs();
		serveur.start();
	}
	
	//Pour gerer la connexion avec le client
	
	class ConnexionClient extends Thread{
		//attribut
		private Socket socket;
		
		
		
		public ConnexionClient(Socket s) {
			this.socket=s;
		}
		@Override 
		public void run(){
			try {
                            try {
                                InputStream recu=socket.getInputStream();
                                BufferedReader afRecu= new BufferedReader( new InputStreamReader(recu));
                                
                                OutputStream envoi=socket.getOutputStream();
                                PrintWriter pw=new PrintWriter(envoi,true);
                                System.out.println("Connexion d'un client");
                                //pw.println("bienvenue vous etes le client");
                                
                                while(true){
                                    
                                    String req=afRecu.readLine();
                                    int choix=Integer.parseUnsignedInt(req);
                                    if(choix==4)
                                    {
                                        Commun.transfert(new FileInputStream("D:\\FichierPersistence.txt"), envoi,true);
                                        
                                    }else if(choix ==3)
                                    {
                                        String chiffre=afRecu.readLine();
                                        int ch=Integer.parseUnsignedInt(chiffre);
                                        pw.println(nbrOccurences(ch));
                                    }else if (choix ==2)
                                    {
                                        pw.println(mediane(1));
                                    }else {
                                        pw.println(moyenne());
                                    }
                                    
                                    
                                    
                                }
                                
                            } catch (IOException e) {
                                
                                e.printStackTrace();}
                            serveur.close();
                        } catch (IOException ex) {
				
				Logger.getLogger(Serveurs.class.getName()).log(Level.SEVERE, null, ex);}
		}
	}
	
	public int mediane(int []nums) {
        // recherche de la médiane
            array.sort(nums);
           int[] table=Serveurs.tableau[1];
            int median=mediane(table[table.length]);
     
            return median;
        }
	public int moyenne() {
        // Calcule de la moyenne
           int[] table=Serveurs.tableau[1];
           int somme=0;
           for (int j = 0;j < table.length;j++) {
               somme=somme+table[j] ;
               
            }
           int moyenne=somme/table.length;

     
            return moyenne;
        }
	
	public int nbrOccurences(int nbr) {
		//recherche d'occurence
		int occu=0;
		int nombre=nbr;
		int[] table=Serveurs.tableau[1];
		for(int i=0;i<table.length;i++)
		{
			if (nombre==table[i])
					occu++;
		}
		
		return occu;
		
		 
	}
	
   

}
