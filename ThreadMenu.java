import java.net.*;
import java.io.*;
import java.util.*;

public class ThreadMenu extends Thread {

  public ArrayList <Servidor> array ;
  public boolean continuar = true;

  public ThreadMenu(ArrayList lista){
      array = lista;
  }

  public void dormir()throws InterruptedException{
      sleep(1000);
  }


	public void run() { 

		while (continuar) {

			Scanner input = new Scanner(System.in);
            System.out.println("*****************************************");
   			System.out.println("AGREGAR DISTRITO");
   			System.out.println("[Servidor Central]: Nombre Distrito:");
   			String ns_zone = input.nextLine();
   			System.out.println("[Servidor Central]: IP Multicast:");
   			String ip_multi = input.nextLine();
   			System.out.println("[Servidor Central]: IP Peticiones:");
   			String ip_zone = input.nextLine();
   			System.out.println("[Servidor Central]: Puerto Peticiones:");
   			String port_zone = input.nextLine();
   			System.out.println("[Servidor Central]: Puerto Multicast:");
   			String port_multicast = input.nextLine();

   			Servidor server = new Servidor(ns_zone,ip_zone,ip_multi,port_zone,port_multicast);
   	 		array.add(server);

		}
	} 
} 