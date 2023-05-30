package Worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class Worker extends Thread{
	private Socket worker;
	private PrintStream toServeur;
	private BufferedReader fromServeur;
	
	
	
	public Worker() {
	try {
		worker =new Socket("localhost",1242);

		 //this.start(); 
		}catch (IOException e) {
			
			e.printStackTrace();
		}}

		@Override 
		public void run(){
		try {
		 
		 toServeur= new PrintStream(worker.getOutputStream());
		 fromServeur= new BufferedReader( new InputStreamReader(worker.getInputStream()));
		// la charge de travail
		 toServeur.println(20);
		 while(true){
		 
		    
		    String message= fromServeur.readLine();
		    //int nb= Integer.parseInt(message);
		    int nb= calculPersistence(Integer.parseUnsignedInt(message));
		    System.out.println("le chiffre"+message +"a pour persistence"+ nb);
		    toServeur.println(nb);
		    sleep(10000);
		    
		    
		    
		      
		} 
		 }catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
	
	

    public int calculPersistence(int nombre) {
        // Calcule de la persistance
            int persistence = 0;
            
        
            while(nombre > 9) {
                int product = 1;
                while(nombre != 0) {
                    product *= nombre % 10;
                    nombre /= 10;
                }
                nombre = product;
                persistence++;
            }
        
            return persistence;
        }
    
    public static void main(String[] args) {
   	  Worker worker =new Worker();
   	 worker.start();
   	 
   	}

}
