import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorCentral{

	static final int serverPort = 32000;
	static final int packetSize = 1024;  

    public static void main (String [] args) { 
    	ArrayList <Servidor> zonas = new ArrayList <Servidor> ();
	    DatagramPacket packet;
		DatagramSocket socket;

		byte[] data; // Para datos a ser enviados en paquetes
		int clientPort;
		InetAddress clientAddr;
		String msg;    	
		ThreadMenu menu = new ThreadMenu(zonas); 
		menu.start(); 

		try {
			
			socket = new DatagramSocket(serverPort);

			for (;;){ 

				data = new byte[ServidorCentral.packetSize];
				// Crear paquete para recibir mensajes
				packet = new DatagramPacket(data, packetSize);

				try {
						// esperar indefinidamente la llegada de un paquete
					socket.receive(packet);

				} catch (IOException ie) {
					System.out.println("[Servidor Central]: No se pudo recibir:" + ie.getMessage());
					continue;
				}

				// imprimir string que fue recibido en consola de servidor
				try {
					// Obtener datos del cliente para realizar eco
					clientAddr = packet.getAddress();
					clientPort = packet.getPort();
					msg = new String(data, 0, packet.getLength(), "UTF-8");
					//msg = msg.trim();
						
					System.out.println("[Servidor Central]: Respuesta "+ clientAddr + " por "+ msg + ".");
					//System.out.println("[Servidor Central]: cantidad de servidores: "+ zonas.size());

					Iterator<Servidor>itr = zonas.iterator();

					Servidor temp;
					boolean state;
					
					while (itr.hasNext()) {
						Servidor elemento = itr.next();
						if (msg.equals(elemento.nombre)) {
							
							System.out.println("[Servidor Central]: Nombre: "+elemento.nombre+", IP Peticiones:"+elemento.ipzona+", IP Multicast:"+elemento.ipmult+", Puerto Peticiones:"+elemento.ppet+", Puerto Multicast:"+elemento.pmult);
							//temp = new Servidor(elemento.ipzona,elemento.ipzona,elemento.ipmult,elemento.ppet,elemento.pmult);
							String reply = elemento.ipzona+" "+elemento.ipmult+" "+elemento.ppet+" "+elemento.pmult;
							data = reply.getBytes("UTF-8");
							packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
							socket.send(packet);
						}

					}

				} catch (UnsupportedEncodingException ex) {
					System.out.println("[Servidor Central]: Codificacion no soportada " + ex.getMessage());
				} catch (IOException ex) {
					System.out.println("[Servidor Central]: No pudo ser enviado " + ex.getMessage());
					System.exit(0);
				}
    		}
			//imprimeArray(zonas);
  		}catch (IOException ie) {
			System.out.println("[Servidor Central]: Servidor no pudo arrancar:" + ie.getMessage());
			System.exit(0);	// Comunicacion con cliente
		}
            
    }       
}