import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorCentral{

	static final int serverPort = 5000;
	static final int packetSize = 1024; 

	public static void main (String [] args){

		ArrayList<Servidor> lista = new ArrayList<Servidor>();

		DatagramPacket packet;
		DatagramSocket socket;

		byte[] data; // Para datos a ser enviados en paquetes
		int clientPort;
		InetAddress clientAddr;
		String mensaje;

		Scanner input = new Scanner(System.in);

		for (int i=1; i<=2; i++){
			//Scanner input = new Scanner(System.in);
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

		// Iterator<Servidor> itr = lista.iterator(); // imprimir nombres de arreglo de servidores
		
		// while(itr.hasNext()){
		// 	Servidor elemento = itr.next();
		// 	System.out.print(elemento.nombre+" / ");
		// }

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
					//System.out.println("2.-NO");
					mensaje = new String(data, 0, packet.getLength(), "UTF-8"); // nombre distrito

					Iterator<Servidor>itr = lista.iterator();
						
					while (itr.hasNext()) {
						Servidor elemento = itr.next();
						if (mensaje.equals(elemento.nombre)) {
						
							System.out.println("[Servidor Central]: Dar autorizacion a "+ clientAddr + " por distrito "+ mensaje + "?.");
							//System.out.println("NOTA: Debes contestar la pregunta anterior, antes de dar autorizacion");
							System.out.println("1.-SI");	
							System.out.println("2.-NO");
							String opcion = input.nextLine();

							String reply;
							switch (Integer.parseInt(opcion)){
								case 1:
									System.out.println("[Servidor Central]:Haz autorizado a "+clientAddr);
									reply = elemento.ipzona+" "+elemento.ipmult+" "+elemento.ppet+" "+elemento.pmult;
									//System.out.println(reply);
									//System.out.println(clientPort);
									data = reply.getBytes("UTF-8");
									packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
									socket.send(packet);
									//input_op = null;
									break;
									
								case 2:
									System.out.println("[Servidor Central]:No haz autorizado a "+clientAddr);
									reply = "Permiso Denegado";
									data = reply.getBytes("UTF-8");
									packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
									socket.send(packet);
									//menu = new ThreadMenu(zonas);
									//input_op = null;
									break;
								default:
									System.out.println("[Servidor Central]:Opcion Invalida");
									break;
							}
						}
					}
				}catch (UnsupportedEncodingException ex) {
					System.out.println("[Servidor Central]: Codificacion no soportada " + ex.getMessage());
				} catch (IOException ex) {
					System.out.println("[Servidor Central]: No pudo ser enviado " + ex.getMessage());
					System.exit(0);
				}
			}
		}catch (IOException ie) {
			System.out.println("[Servidor Central]: Servidor no pudo arrancar:" + ie.getMessage());
			System.exit(0);	// Comunicacion con cliente
		}
	}
}

