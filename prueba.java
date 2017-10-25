import java.net.*;
import java.io.*;
import java.util.*;

public static void main (String [] args){

	static final int serverPort = 5000;
	static final int packetSize = 1024; 

	ArrayList<Servidor> lista = new ArrayList<Servidor>();

	for (int i=1; i<=4; i++){
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
   	 	lista.add(server); 
	}

}

