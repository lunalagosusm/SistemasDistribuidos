import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorCentral{

	static final int serverPort = 5000;
	static final int packetSize = 1024;

	public static boolean isNumeric(String str){  
  		try{
    		double d = Double.parseDouble(str);  
  		}
  		catch(NumberFormatException nfe)  
  		{  
    		return false;  
  		}  
  		return true;  
	}

	public static void main (String [] args){

		ArrayList<Servidor> lista = new ArrayList<Servidor>();

		DatagramPacket packet;
		DatagramSocket socket;

		byte[] data; // Para datos a ser enviados en paquetes
		int clientPort;
		InetAddress clientAddr;
		String mensaje;

		Scanner input = new Scanner(System.in);


		System.out.println("[Servidor Central]: Cuantos distritos desea crear?");
		int count_distr;
		String input_count_distr = input.nextLine();

		if(isNumeric(input_count_distr)){
			count_distr = Integer.parseInt(input_count_distr);
			if (count_distr > 0){
				if (count_distr>1){
					System.out.println("[Servidor Central]: Se crearan "+count_distr+" distritos");
				}
				else{
					System.out.println("[Servidor Central]: Se creara "+count_distr+" distrito");
				}
			}
			else{
				System.out.println("[Servidor Central]: No se crearan distritos.\n");
				System.out.println("Servidor Central cerrado.\n");
				System.exit(0);
			}
		}
		else{
			count_distr = 1;
			System.out.println("[Servidor Central]: Entrada no reconocida.");
			System.out.println("[Servidor Central]: Se creara "+count_distr+" distrito");
		}
		

		for (int i=1; i<=count_distr; i++){
			//Scanner input = new Scanner(System.in);
	        System.out.println("*****************************************");
	   		System.out.println("AGREGAR DISTRITO");
	   		System.out.println("[Servidor Central]: Nombre Distrito:");
	   		String ns_distrito = input.nextLine();
	   		System.out.println("[Servidor Central]: IP Multicast:");
	   		String ip_multi = input.nextLine();
	   		System.out.println("[Servidor Central]: IP Peticiones:");
	   		String ip_distrito = input.nextLine();
	   		System.out.println("[Servidor Central]: Puerto Peticiones:");
	   		String port_distrito = input.nextLine();
	   		System.out.println("[Servidor Central]: Puerto Multicast:");
	   		String port_multicast = input.nextLine();

	   		Servidor server = new Servidor(ns_distrito,ip_distrito,ip_multi,port_distrito,port_multicast);
	   	 	lista.add(server); 
		}

		System.out.println("[Servidor Central]: Esperando a recibir conexiones de clientes...\n");

		try {
			
			socket = new DatagramSocket(serverPort);

			for (;;){

				data = new byte[ServidorCentral.packetSize];
				packet = new DatagramPacket(data, packetSize);

				try {
						// esperar indefinidamente la llegada de un paquete
					socket.receive(packet);

				} 
				catch (IOException ie) {
					System.out.println("[Servidor Central]: No se pudo recibir:" + ie.getMessage());
					continue;
				}

				try {
						// Obtener datos del cliente para realizar eco
					clientAddr = packet.getAddress();
					clientPort = packet.getPort();
					mensaje = new String(data, 0, packet.getLength(), "UTF-8"); // nombre distrito

					Iterator<Servidor>itr = lista.iterator();
						
					while (itr.hasNext()) {
						Servidor elemento = itr.next();
						if (mensaje.equals(elemento.nombre)) {
						
							System.out.println("[Servidor Central]: Dar autorizacion a "+ clientAddr + " por Distrito "+ mensaje + "?.");
							System.out.println("1.-SI");	
							System.out.println("2.-NO");
							String opcion = input.nextLine();

							String reply;
							switch (opcion){
								case "1":
									System.out.println("[Servidor Central] Respuesta a "+clientAddr+" por "+mensaje+".");
									reply = elemento.ipzona+" "+elemento.ipmult+" "+elemento.ppet+" "+elemento.pmult;
									System.out.println("[Servidor Central] Nombre: "+mensaje+", IP Multicast: "+elemento.ipmult+", Puerto Multicast: "+elemento.pmult+",IP Peticiones: "+elemento.ipzona+", Puerto Peticiones: "+elemento.ppet);
									data = reply.getBytes("UTF-8");
									packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
									socket.send(packet);
									break;
									
								case "2":
									System.out.println("[Servidor Central]:No haz autorizado a "+clientAddr);
									reply = "Permiso Denegado";
									data = reply.getBytes("UTF-8");
									packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
									socket.send(packet);
									break;
								default:
									System.out.println("[Servidor Central]:Opcion Invalida, se rechazara la conexion.");
									System.out.println("[Servidor Central]:No haz autorizado a "+clientAddr);
									reply = "Permiso Denegado";
									data = reply.getBytes("UTF-8");
									packet = new DatagramPacket(data, data.length, clientAddr, clientPort);
									socket.send(packet);
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

