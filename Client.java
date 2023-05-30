/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import Serveur.Commun;



/**
 *
 * @author AGBAGNO
 */
public class Client extends Thread{
	private Socket client;
	private PrintStream toServeur;
	private BufferedReader fromServeur;
	private Scanner scanner = new Scanner(System.in);


	public Client() {
	try {
	 client =new Socket("localhost",1242);

	 //this.start(); 
	}catch (IOException e) {
		
		e.printStackTrace();
	}}

	@Override 
	public void run(){
	try {
	 
	 toServeur= new PrintStream(client.getOutputStream());
	 fromServeur= new BufferedReader( new InputStreamReader(client.getInputStream()));
	 
	 while(true){
	 
	    //Demande saisie du message client
	    System.out.println("bienvenue sur le serveur de persistence");
	    System.out.println("Choisissez l'opération que vous voulez effectuer");
	    System.out.println(" 1  la moyenne des persistences");
	    System.out.println(" 2  la mediane des persistences");
	    System.out.println(" 3  l'occurence d'une persistence ");
	    System.out.println(" 4  voir les opérations effectuées depuis le lancement du serveur");
	    String str=scanner.nextLine();
	    toServeur.println(str);
	    
	    int choix=Integer.parseUnsignedInt(str);
		if(choix==4)
		{
		Commun.transfert(client.getInputStream(), new FileOutputStream("D:\\affichage.txt"),true);
				
		}else if(choix ==3)
		{
			System.out.println(" saisissez la persistence dont vous voulez");
			toServeur.println(scanner.nextLine());
			String occurence= fromServeur.readLine();
		    System.out.println("l'occurence de votre persistence est  "+ occurence);
			
		}else
		{
			String message= fromServeur.readLine();
		    System.out.println("le résultat de votre demande"+ message);
		}
	       
	      
	} 
	 }catch (IOException e) {
		
		e.printStackTrace();
	}
	try {
		client.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	public static void main(String[] args) {
	 Client client =new Client();
	 client.start();
	 
	 
	 
	}
    
    
}
