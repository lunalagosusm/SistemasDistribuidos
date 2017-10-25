import java.net.*;
import java.io.*;
import java.util.*;

public class Cliente{

	///////////
//	public String cons=null;
	public String[] titandato=null;
	public String mensaje = null;
	public int packetSize = 1024;
	public String resp=null;
	public LinkedList<Titan> lista;
	public int id;
	///////////
	public static String consultarZona(String nombre,String direccion, String puerto){

		DatagramSocket socket; // para enviar datos
		DatagramPacket packet; // lo que se envia

		InetAddress address,addr_zone,addr_mult;;

		int p_zona,p_multicast;

		byte[] data = null;

		String msgReturn = null;

		try {
			address = InetAddress.getByName(direccion);
			socket = new DatagramSocket();
			data = nombre.getBytes("UTF-8"); // leer mensaje a enviar desde variable zona
			packet = new DatagramPacket(data, data.length, address, Integer.parseInt(puerto));//, address, ServidorCentral.serverPort);
			socket.send(packet);

			data = new byte[ServidorCentral.packetSize];
			packet = new DatagramPacket(data, data.length);
			socket.receive(packet);

			msgReturn = new String(packet.getData());
			//if (msgReturn=="Permiso Denegado") {
			//	System.out.println("[Cliente]: No haz sido autorizado para explorar esta zona:");
			//}	


		} catch (IOException ex) {
			System.out.println("[Cliente]: No pudo leer mensaje :" + ex.getMessage());
			System.exit(0);
		}
		// crear datagrama con datos y direccion de envio
		
		return msgReturn;
	}
	@SuppressWarnings("resource")
	public static void main(String args[]) throws UnknownHostException, SocketException{
		
		///////////
	//	String cons=null;
		String[] titandato=null;
		String mensaje = null;
		int packetSize = 1024;
		String resp=null;
		LinkedList<Titan> lista = new LinkedList<Titan>();
		int id;
		///////////
		//DatagramSocket socket; // para enviar datos
		//DatagramPacket packet; // lo que se envia

		//MulticastSocket receptor; // receptor de mensajes multicast
		//////////////
		DatagramPacket pack;
		DatagramSocket sock;
//		byte[] datt;
		id = 0;
		//////////////

		InetAddress address,addr_zone,addr_mult; // Hacia donde enviar y recibir
		int p_zona,p_multicast; // puertos de servidor zona y multicast
		String messageReturn,msgmulti; // lo que se obtiene del servidor
		byte[] data = null; // buffer para el mensaje a enviar
		//byte[] datamult; // buffer para mensaje multicast

		Scanner input = new Scanner(System.in); // datos de entrada del cliente

		System.out.println("*****************************************");
		System.out.println("[Cliente]: Ingresar IP Servidor Central:");
		String ip_scentral = input.nextLine();
		System.out.println("[Cliente]: Ingresar Puerto Servidor Central:");
		String port_scentral = input.nextLine();
		System.out.println("[Cliente]: Introducir Nombre de Distrito a investigar, Ej: Trost, Shiganshina:");
		String distrito = input.nextLine(); // string con el nombre de la distrito a explorar

<<<<<<< HEAD
		messageReturn = consultarZona(zona,ip_scentral,port_scentral);

		System.out.println(messageReturn + "primer flag");
=======
		messageReturn = consultarZona(distrito,ip_scentral,port_scentral);
>>>>>>> dddefa6d3aaa065c4203c288bc070f4cb1895239
		String[] div = messageReturn.split(" "); 

		addr_mult = InetAddress.getByName(div[1].trim());
		addr_zone = InetAddress.getByName(div[0].trim());
		p_zona = Integer.parseInt(div[2].trim());
		p_multicast = Integer.parseInt(div[3].trim());

		ThreadMulticast escuchar = new ThreadMulticast(p_multicast,addr_mult); 
		escuchar.start();

		for (; ; ) {
			System.out.println("*****************************************");
			System.out.println("[Cliente] CONSOLA");
			System.out.println("[Cliente] (1) Listar Titanes en Distrito");
			System.out.println("[Cliente] (2) Cambiar distrito");
			System.out.println("[Cliente] (3) Capturar Titan");
			System.out.println("[Cliente] (4) Asesinar Titan");
			System.out.println("[Cliente] (5) Listar Titanes capturados");
			System.out.println("[Cliente] (6) Listar Titanes asesinados");
			mensaje =null;
			switch (Integer.parseInt(input.nextLine())){
				case 1: //LISTAR TITANES DEL DISTRITO

					mensaje = "LISTAR";
					try{
						byte[] datt;
						datt = mensaje.getBytes("UTF-8");
						pack = new DatagramPacket(datt, datt.length, addr_zone, p_zona);
						sock = new DatagramSocket();
						sock.send(pack);
						sock.close();
						//recibir respuesta
						datt = new byte[packetSize];
						pack = new DatagramPacket(datt, datt.length);
						sock.receive(pack);
				
						resp= new String(pack.getData());
						//
						String pr="cadena a comparar";
						char _toCompare='c';
						int rep=0;
						char []caracteres=resp.toCharArray();
						for(int ii=0;ii<caracteres.length;ii++){
							if(':' ==caracteres[ii]){
								rep++;
							}
						//
						}
						if (rep>1){
							String[] titn = resp.split(":");
							System.out.println("[Cliente] Titanes por capturar!");
							for (int t=0; t < titn.length; t++){
								titandato = titn[t].split(" ");
								//imprimir
								System.out.println("*****");
								System.out.println("nombre: "+titandato[0]);
								System.out.println("ID: "+titandato[1]);
								System.out.println("Tipo: "+titandato[2]);
							}
							System.out.println("*****");
						}
						else{
							System.out.println("[Cliente] No hay Titanes por capturar!");
						}
					}
					catch(IOException e){
						System.out.println(e.getMessage());
					}

					break;
				case 2: // CAMBIAR DISTRITO
					System.out.println("*****************************************");
					System.out.println("[Cliente]: Ingresar IP Servidor Central:");
					ip_scentral = input.nextLine();
					System.out.println("[Cliente]: Ingresar Puerto Servidor Central:");
					port_scentral = input.nextLine();
					System.out.println("[Cliente]: Introducir Nombre distrito a investigar, Ej: Trost, Shiganshina:");
					distrito = input.nextLine(); // string con el nombre del distrito a explorar
					messageReturn = consultarZona(distrito,ip_scentral,port_scentral);
					//div = messageReturn.split(" "); 

					System.out.println(messageReturn.length());
					System.out.println(messageReturn);
					System.out.println("Permiso Denegado".length());

					if (messageReturn.equals("Permiso Denegado")) {
						System.out.println("[Cliente]: No haz sido autorizado para explorar este distrito:");
					}
					else{
						div = messageReturn.split(" ");
						addr_mult = InetAddress.getByName(div[1].trim());
						addr_zone = InetAddress.getByName(div[0].trim());
						p_zona = Integer.parseInt(div[2].trim());
						p_multicast = Integer.parseInt(div[3].trim());

						escuchar.stop();
						escuchar = new ThreadMulticast(p_multicast,addr_mult); 
						escuchar.start();
					}

					break;
				case 3:// CAPTURAR TITAN
					System.out.println("*****************************************");
					System.out.println("[Cliente] ID de Titan a capturar:");
					String numero = input.nextLine();
					//mensaje a SZ "CAPTURAR"
					mensaje = "CAPTURAR "+Integer.parseInt(numero);
					try{
						byte[] dattc;
						dattc = mensaje.getBytes("UTF-8");
						pack = new DatagramPacket(dattc, dattc.length, addr_zone, p_zona);
						sock = new DatagramSocket();
						sock.send(pack);
						//sock.close();
						//recibir respuesta
						dattc = new byte[packetSize];
						pack = new DatagramPacket(dattc, dattc.length);
						sock.receive(pack);
						//dependiendo de la respuesta
							//si si, agregar a lista local
							//si no, solo imprimir
						resp= new String(pack.getData());
						if (resp.split(" ")[0].equals("Ups!")){
							System.out.println("[Cliente] "+ resp);
						}
						else
						{
							String[] ttitann = resp.split(" ");
							Titan tan = new Titan( ttitann[0],id,ttitann[1]);
							lista.add(tan);
							System.out.println("[Cliente] Titan capturado!");
							System.out.println("id: "+id);
							System.out.println("Nombre: "+ttitann[0]);
							System.out.println("Tipo: "+ ttitann[1]);
							id=id+1;
						}
					}
					catch(IOException e){
						System.out.println(e.getMessage());
					}
					break;
				case 4:// ASESINAR TITAN
					System.out.println("AUN NO IMPLEMENTADO ASESINAR TITAN");
					break;
				case 5:// LISTAR TITANES CAPTURADOS
					//recorrer lista local e imprimir
					if (lista.size()>0){
						System.out.println("*****************************************");
						System.out.println("[Cliente] Titanes actualmente capturados!");
						for (Iterator<Titan> i = lista.iterator(); i.hasNext();) {
							System.out.println("*****");
							Titan titt = i.next();
							System.out.println("nombre: "+titt.nombre);
							System.out.println("ID: "+titt.id);
							System.out.println("Tipo: "+titt.tipo);
						}
						System.out.println("*****");
					}
					else{
						System.out.println("*****************************************");
						System.out.println("[Cliente] No has capturado ningun Titan!");
					}
					break;
				case 6:// LISTAR TITANES ASESINADOS
					System.out.println("AUN NO IMPLEMENTADO LISTAR TITANES ASESINADOS");
					break;
				default:
					break;
			}

			
		}

	}

}
