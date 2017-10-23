import java.net.*;
import java.io.*;
import java.util.*;

public class Cliente{

	///////////
//	public String cons=null;
	public String[] pokedato=null;
	public String mensaje = null;
	public int packetSize = 1024;
	public String resp=null;
	public LinkedList<Distribumon> lista;
	public int id;
	///////////
	public static String consultarZona(String nombre,String direccion){

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
			packet = new DatagramPacket(data, data.length, address, ServidorCentral.serverPort);
			socket.send(packet);

			data = new byte[ServidorCentral.packetSize];
			packet = new DatagramPacket(data, data.length);
			socket.receive(packet);

			msgReturn = new String(packet.getData());
			

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
		String[] pokedato=null;
		String mensaje = null;
		int packetSize = 1024;
		String resp=null;
		LinkedList<Distribumon> lista = new LinkedList<Distribumon>();
		int id;
		///////////
		//DatagramSocket socket; // para enviar datos
		//DatagramPacket packet; // lo que se envia

		//MulticastSocket receptor; // receptor de mensajes multicast
		//////////////
		DatagramPacket pack;
		DatagramSocket sock;
//		byte[] datt;
		id =0;
		//////////////

		InetAddress address,addr_zone,addr_mult; // Hacia donde enviar y recibir
		int p_zona,p_multicast; // puertos de servidor zona y multicast
		String messageReturn,msgmulti; // lo que se obtiene del servidor
		byte[] data = null; // buffer para el mensaje a enviar
		//byte[] datamult; // buffer para mensaje multicast

		Scanner input = new Scanner(System.in); // datos de entrada del cliente

		System.out.println("[Cliente]: Ingresar IP Servidor Central:");
		String ip_scentral = input.nextLine();
		System.out.println("[Cliente]: Introducir Nombre de Zona a explorar, Ej: Casa Central, San Joaquin:");
		String zona = input.nextLine(); // string con el nombre de la zona a explorar

		messageReturn = consultarZona(zona,ip_scentral);
		String[] div = messageReturn.split(" "); 

		addr_mult = InetAddress.getByName(div[1].trim());
		addr_zone = InetAddress.getByName(div[0].trim());
		p_zona = Integer.parseInt(div[2].trim());
		p_multicast = Integer.parseInt(div[3].trim());

		//System.out.println(addr_mult);

		ThreadMulticast escuchar = new ThreadMulticast(p_multicast,addr_mult); 
		escuchar.start();

		for (; ; ) {
			System.out.println("[Cliente] CONSOLA");
			System.out.println("[Cliente] (1) Listar distribumones en Zona");
			System.out.println("[Cliente] (2) Cambiar Zona");
			System.out.println("[Cliente] (3) Capturar Distribumon");
			System.out.println("[Cliente] (4) Listar Distribumones capturados");
			mensaje =null;
			switch (Integer.parseInt(input.nextLine())){
				case 1:
					//mensaje a SZ "LISTAR"
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
							String[] pkmn = resp.split(":");
							System.out.println("[Cliente] Distribumones por capturar!");
							for (int t=0; t < pkmn.length; t++){
								pokedato = pkmn[t].split(" ");
								//imprimir
								System.out.println("*****");
								System.out.println("nombre: "+pokedato[0]);
								System.out.println("ID: "+pokedato[1]);
								System.out.println("Nivel: "+pokedato[2]);
							}
							System.out.println("*****");
						}
						else{
							System.out.println("[Cliente] No hay Distribumones por capturar!");
						}
					}
					catch(IOException e){
						System.out.println(e.getMessage());
					}

					break;
				case 2:
					System.out.println("[Cliente]: Ingresar IP Servidor Central:");
					ip_scentral = input.nextLine();
					System.out.println("[Cliente]: Introducir Nombre de Zona a explorar, Ej: Casa Central, San Joaquin:");
					zona = input.nextLine(); // string con el nombre de la zona a explorar
					messageReturn = consultarZona(zona,ip_scentral);
					div = messageReturn.split(" "); 

					addr_mult = InetAddress.getByName(div[1].trim());
					addr_zone = InetAddress.getByName(div[0].trim());
					p_zona = Integer.parseInt(div[2].trim());
					p_multicast = Integer.parseInt(div[3].trim());

					//System.out.println(addr_mult);
					
					escuchar.stop();
					escuchar = new ThreadMulticast(p_multicast,addr_mult); 
					escuchar.start();

					break;
				case 3:
					System.out.println("[Cliente] ID de Distribumon a capturar:");
					String numero = input.nextLine();
					//mensaje a SZ "CAPTURAR"
					mensaje = "CAPTURAR "+Integer.parseInt(numero);
					try{
						byte[] dattc;
						dattc = mensaje.getBytes("UTF-8");
						pack = new DatagramPacket(dattc, dattc.length, addr_zone, p_zona);
						sock = new DatagramSocket();
						sock.send(pack);
						sock.close();
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
							String[] pokemon = resp.split(" ");
							Distribumon poke = new Distribumon( pokemon[0],id, Integer.parseInt(pokemon[1]));
							lista.add(poke);
							System.out.println("[Cliente] Distribumon capturado!");
							System.out.println("id: "+id);
							System.out.println("Nombre: "+pokemon[0]);
							System.out.println("Nivel: "+ pokemon[1]);
							id=id+1;
						}
					}
					catch(IOException e){
						System.out.println(e.getMessage());
					}
					break;
				case 4:
					//recorrer lista local e imprimir
					if (lista.size()>0){
						System.out.println("[Cliente] Distribumones actualmente capturados!");
						for (Iterator<Distribumon> i = lista.iterator(); i.hasNext();) {
							System.out.println("*****");
							Distribumon pokk = i.next();
							System.out.println("nombre: "+pokk.nombre);
							System.out.println("ID: "+pokk.id);
							System.out.println("Nivel: "+pokk.nivel);
						}
						System.out.println("*****");
					}
					else{
						System.out.println("[Cliente] No has capturado ningun Distribumon!");
					}
					break;
				default:
					break;
			}

			
		}

	}

}
