import java.net.*;
import java.io.*;
import java.util.*;

//hilo para la comunicacion Unicast

public class ThreadDatagramS extends Thread {

    DatagramSocket rcptr; // rcptr de mensajes multicast
    InetAddress addr_pet; // Hacia donde enviar y recibir
    int p_pet; // puertos de servidor distr y multicast
    String msgpet; // lo que se obtiene del servidor
    byte[] datapet; // buffer para mensaje multicast

    public ThreadDatagramS(int puerto, InetAddress address ){
        addr_pet = address;
        p_pet = puerto;
    }

    public void run() {
	    for (; ; ) {
            try {
                rcptr = new DatagramSocket();
                datapet = new byte[1024];
                DatagramPacket pquete = new DatagramPacket(datapet,datapet.length);
                rcptr.receive(pquete);
                addr_pet = pquete.getAddress();
                msgpet = new String (pquete.getData());
                String[] separar = msgpet.split(" "); 
                String peticion = separar[0].trim();
            } catch (IOException iee) {
                System.out.println(iee.getMessage());
                System.exit(0);
            }
	    }
    } 
} 