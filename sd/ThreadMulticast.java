import java.net.*;
import java.io.*;
import java.util.*;

public class ThreadMulticast extends Thread {

    MulticastSocket receptor; // receptor de mensajes multicast

    InetAddress addr_mult; // Hacia donde enviar y recibir
    int p_multicast; // puertos de servidor zona y multicast
    String msgmulti; // lo que se obtiene del servidor
    byte[] datamult; // buffer para mensaje multicast

    public ThreadMulticast(int puerto, InetAddress address ){
        addr_mult = address;
        p_multicast = puerto;
    }

    public void run() { 
	    for (;;) {
            try {
                // Recibir paquete del servidor
                receptor = new MulticastSocket(p_multicast);
                receptor.joinGroup(addr_mult);
                datamult = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(datamult,datamult.length);
                receptor.receive(paquete);
                msgmulti = new String (paquete.getData());
                System.out.println("[Cliente]: Aparece nuevo Distribumon!: "+ msgmulti);

            } catch (IOException iee) {
                System.out.println("[Cliente]: Error de direccion :" + iee.getMessage());
                System.exit(0);
            }
	    }
    } 
} 