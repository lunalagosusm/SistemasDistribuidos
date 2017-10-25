import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorCentral{

	static final int serverPort = 5000;
	static final int packetSize = 1024;  

    public static void main (String [] args) throws InterruptedException{ 
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

				try {
					// Obtener datos del cliente para realizar eco
					clientAddr = packet.getAddress();
					clientPort = packet.getPort();
					System.out.println(clientPort);
					System.out.println("2.-NO");
					msg = new String(data, 0, packet.getLength(), "UTF-8"); // nombre distrito

					Iterator<Servidor>itr = zonas.iterator();

					Servidor temp;
					boolean state;
					
					while (itr.hasNext()) {
						Servidor elemento = itr.next();
						if (msg.equals(elemento.nombre)) {
							
							System.out.println("[Servidor Central]: Dar autorizacion a "+ clientAddr + " por distrito "+ msg + "?.");
							System.out.println("NOTA: Debes contestar la pregunta anterior, antes de dar autorizacion");
							System.out.println("1.-SI");	
							System.out.println("2.-NO");
							//menu.dormir();
							Scanner input_op = new Scanner(System.in);
							//menu = new ThreadMenu(zonas);
							String reply;
							switch (Integer.parseInt(input_op.nextLine())){
								case 1:
									System.out.println("[Servidor Central]:Haz autorizado a "+clientAddr);
									reply = elemento.ipzona+" "+elemento.ipmult+" "+elemento.ppet+" "+elemento.pmult;
									data = reply.getBytes("UTF-8");
									packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
									socket.send(packet);
									input_op = null;
									break;
									
								case 2:
									System.out.println("[Servidor Central]:No haz autorizado a "+clientAddr);
									reply = "Permiso Denegado";
									data = reply.getBytes("UTF-8");
									packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
									socket.send(packet);
									//menu = new ThreadMenu(zonas);
									input_op = null;
									break;
								default:
									System.out.println("[Servidor Central]:Opcion Invalida");
									break;
							}

							//System.out.println("[Servidor Central]: Nombre: "+elemento.nombre+", IP Peticiones:"+elemento.ipzona+", IP Multicast:"+elemento.ipmult+", Puerto Peticiones:"+elemento.ppet+", Puerto Multicast:"+elemento.pmult);
							//temp = new Servidor(elemento.ipzona,elemento.ipzona,elemento.ipmult,elemento.ppet,elemento.pmult);
							//String reply = elemento.ipzona+" "+elemento.ipmult+" "+elemento.ppet+" "+elemento.pmult;
							//data = reply.getBytes("UTF-8");
							//packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
							//packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
							//socket.send(packet);
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