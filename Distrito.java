import java.net.*;
import java.io.*;
import java.util.*;

//clase que contiene la lógica del servidor de distrito
public class Distrito {
	private static Scanner sc;
	public static void main (String[] args) throws IOException {
		sc = new Scanner (System.in);
		LinkedList<Titan> lista = new LinkedList<Titan>();
		//Datos de conexion a distrito
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
		
		//hilo que ejecuta las opciones de la consola de distrito
		Consola hilo = new Consola(lista, addrMulti, Integer.parseInt(PtoMulti), nombre);
		hilo.start();

		//variables para el socket entre el distrito y cliente
		DatagramPacket packet;
		DatagramSocket socket;
		byte[] data; // Para datos a ser enviados en paquetes
		int clientPort;
		int idc,ida; // id titan capturado y asesinado respectivamente
		int packetSize = 1024;
		int flag=0;
		InetAddress clientAddr;
		String mensaje;
		String s=null;
		String peti = null;
		String aviso=null;
		String estado = null;

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
					System.out.println("[Distrito]: No se pudo recibir:" + ie.getMessage());
					continue;
				}

				try { // codigo para las consultas del cliente al distrito
					aviso = "";
					clientAddr = packet.getAddress();
					clientPort = packet.getPort();
					mensaje = new String(data, 0, packet.getLength(), "UTF-8");
					String[] array_msg = mensaje.split(" ");
					byte[] dataSend;

					switch (array_msg[0]){
						case "LISTAR": //LOGICA PARA LISTAR TITANES DEL DISTRITO

							for (Iterator<Titan> i = lista.iterator(); i.hasNext();) {
								Titan ttitann = i.next();
								aviso = aviso+ttitann.nombre+" "+ttitann.id+" "+ttitann.tipo+":";

							}
							dataSend = aviso.getBytes("UTF-8");
							packet = new DatagramPacket(dataSend, dataSend.length, clientAddr, clientPort);
							socket.send(packet);
							break;
						case "CAPTURAR": //LOGICA PARA CAPTURAR TITANES DEL DISTRITO

						 	idc = Integer.parseInt(array_msg[1].trim()); //id de titan a capturar
						 	Iterator<Titan> i = lista.iterator();

						 	while (i.hasNext()) {
						 		Titan ttitann = i.next();

						 		if (ttitann.id + 1 == idc){
						 			flag=1;
						 			int idtitan=+ttitann.id+1;
						 			estado = "vivo";
						 			aviso = ttitann.nombre+" "+ttitann.tipo+" "+idtitan+" "+estado;
						 			i.remove();
						 			dataSend = aviso.getBytes("UTF-8");
						 			packet = new DatagramPacket(dataSend, dataSend.length, clientAddr, clientPort);
						 			socket.send(packet);
						 			break;
						 		}
						 	}
						 	if (flag==0){
						 		aviso = "Ups! Titan no disponible";
						 		System.out.println("Ups! Titan no disponible");//////////////
						 	}
						 	else{
						 		flag=0;
						 		System.out.println("Han capturado un titan de este distrito!!");//////////////
						 	}

						 	break;
						case "ASESINAR": //LOGICA PARA ASESINAR TITANES DEL DISTRITO
							ida = Integer.parseInt(array_msg[1].trim()); //id de titan a asesinar
						 	i = lista.iterator();

						 	while (i.hasNext()) {
						 		Titan ttitann = i.next();

						 		if (ttitann.id + 1 == ida){
						 			flag=1;
						 			int idtitan=+ttitann.id+1;
						 			estado = "muerto";
						 			aviso = ttitann.nombre+" "+ttitann.tipo+" "+idtitan+" "+estado;
						 			i.remove();
						 			dataSend = aviso.getBytes("UTF-8");
						 			packet = new DatagramPacket(dataSend, dataSend.length, clientAddr, clientPort);
						 			socket.send(packet);
						 			break;
						 		}
						 		else{
						 			System.out.println("NO EXISTE TAL TITAN");
						 		}
						 	}
						 	if (flag==0){
						 		aviso = "Ups! Titan no disponible";
						 		System.out.println("Ups! Titan no disponible");//////////////
						 	}
						 	else{
						 		flag=0;
						 		System.out.println("Han asesinado un titan de este distrito!!");//////////////
						 	}
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