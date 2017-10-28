import java.net.*;
import java.io.*;
import java.util.*;

public class Distrito {
	private static Scanner sc;
	public static void main (String[] args) throws IOException {
		sc = new Scanner (System.in);
		LinkedList<Titan> lista = new LinkedList<Titan>();
		System.out.println("[SERVIDOR Distrito] Nombre Servidor:");
		String nombre = sc.nextLine();
		System.out.println("[SERVIDOR DISTRITO:"+nombre+"] IP Multicast:");
		String addrMulti = sc.nextLine();
		System.out.println("[SERVIDOR DISTRITO:"+nombre+"] Puerto Multicast:");
		String PtoMulti = sc.nextLine();
		System.out.println("[SERVIDOR DISTRITO:"+nombre+"] IP Peticiones:");
		String addrPet = sc.nextLine();
		System.out.println("[SERVIDOR DISTRITO:"+nombre+"] Puerto Peticiones:");
		String PtoPet = sc.nextLine();
		
		Consola hilo = new Consola(lista, addrMulti, Integer.parseInt(PtoMulti), nombre);
		hilo.start();

		DatagramPacket packet;
		DatagramSocket socket;
		byte[] data; // Para datos a ser enviados en paquetes
		int clientPort;
		int idc;
		int packetSize = 1024;
		int flag=0;
		InetAddress clientAddr;
		String mensaje;
		String s=null;
		String peti = null;
		String aviso=null;

		try {
			socket = new DatagramSocket(Integer.parseInt(PtoPet));
			for (;;){

				data = new byte[1024];
				// Crear paquete para recibir mensajes
				packet = new DatagramPacket(data, packetSize);

				try {
					// esperar la llegada de un paquete
					socket.receive(packet);
				} 
				catch (IOException ie) {
					System.out.println("[Distrito]: No se pudo recibir (primer try):" + ie.getMessage());
					continue;
				}

				try {
					aviso = "";
					// Obtener datos del cliente para realizar eco
					clientAddr = packet.getAddress();
					clientPort = packet.getPort();
					mensaje = new String(data, 0, packet.getLength(), "UTF-8");
					//System.out.println(mensaje);
					//System.out.println(mensaje[1]);
					String[] array_msg = mensaje.split(" ");

					//System.out.println(array_msg[0]);
					//System.out.println(array_msg[1]);
					byte[] dataSend;

					switch (array_msg[0]){
						case "LISTAR": //LISTAR TITANES DEL DISTRITO

							//String mensajeesperado = new String("LISTAR");
						//if(mensaje.equals(mensajeesperado)){
							for (Iterator<Titan> i = lista.iterator(); i.hasNext();) {
								Titan ttitann = i.next();
								aviso = aviso+ttitann.nombre+" "+ttitann.id+" "+ttitann.tipo+":";

							}
							//byte[] dataSend;
							//System.out.println(aviso);
							dataSend = aviso.getBytes("UTF-8");
							packet = new DatagramPacket(dataSend, dataSend.length, clientAddr, clientPort);
							socket.send(packet);
						/*}else{
						System.out.println("(distrito) entro al else");
						}*/
							break;
						case "CAPTURAR":

						 	idc = Integer.parseInt(array_msg[1].trim());
						 	Iterator<Titan> i = lista.iterator();

						 	while (i.hasNext()) {
						 		Titan ttitann = i.next();
						 		//System.out.println("for capturar");
						 		//System.out.println(idc);
						 		//System.out.println(ttitann.id);
						 		if (ttitann.id + 1 == idc){
						 			flag=1;
						 			int idtitan=+ttitann.id+1;
						 			//System.out.println(idtitan);
						 			aviso = ttitann.nombre+" "+ttitann.tipo+" "+idtitan;
						 			//eliminar
						 			//System.out.println(aviso);
						 			i.remove();
						 			//byte[] dataSend;
						 			dataSend = aviso.getBytes("UTF-8");
						 			packet = new DatagramPacket(dataSend, dataSend.length, clientAddr, clientPort);
						 			socket.send(packet);
						 			//mensaje al cliente 
						 			break;
						 		}
						 	}
						 	if (flag==0){
						 		aviso = "Ups! Titan no disponible";
						 		System.out.println("Ups! Titan no disponible");//////////////
						 	}
						 	else{
						 		flag=0;
						 		System.out.println("Han capturado un titan de esta zona!!");//////////////
						 	}

						 	break;
						case "ASESINAR":
							break;
						default:
							break;
					}
							

				}catch (UnsupportedEncodingException ex) {
					System.out.println("[Servidor Distrito]: Error en distrito: " + ex.getMessage());
				}
			}
		}catch (IOException ie) {
			System.out.println("[Distrito]: Error con el tercer try:" + ie.getMessage());
			System.exit(0);	// Comunicacion con cliente
		}
}
}