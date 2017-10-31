import java.net.*;
import java.io.*;
import java.util.*;

public class Cliente{

	public String[] titandato=null;
	public String mensaje = null;
	public int packetSize = 1024; //tamaño de paquetes a enviar
	public String resp = null;
	public LinkedList<Titan> lista_capturados; //almacena los titanes capturados
	public LinkedList<Titan> lista_asesinados; //almacena los titanes asesinados
	public int id; // id para titanes
	
	//consultarDistrito sirve para que el cliente se pueda comunicar con el distrito al que esta conectado
	public static String consultarDistrito(String nombre,String direccion, String puerto){

		DatagramSocket socket; // para enviar datos
		DatagramPacket packet; // lo que se envia

		InetAddress address; //dirección ip del socket a utilizar

		//int p_distr,p_multicast;

		byte[] data = null;

		String msgReturn = null;

		try {
			address = InetAddress.getByName(direccion);
			socket = new DatagramSocket();
			data = nombre.getBytes("UTF-8"); // leer mensaje a enviar desde variable distr
			packet = new DatagramPacket(data, data.length, address, Integer.parseInt(puerto));
			socket.send(packet); //se envia la información
			data = new byte[1024];
			packet = new DatagramPacket(data, data.length);
			socket.setSoTimeout(20000);
    			try {
        			socket.receive(packet);
        			socket.close();
					msgReturn = new String(packet.getData());
    			} catch (SocketTimeoutException ste) {
        			System.out.println("No hubo respuesta del servidor, tiempo de conexion mayor a 20 segundos.");
        			//System.exit(0);
        			msgReturn="timeout";
    			}

		} catch (IOException ex) {
			System.out.println("[Cliente]: Error = " + ex.getMessage());
			//System.exit(0);
			msgReturn="error";
		}		
		return msgReturn;
	}

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

	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws UnknownHostException, SocketException{
		

		String[] titandato=null;
		String mensaje = null;
		int packetSize = 1024;
		String resp=null;
		LinkedList<Titan> lista_capturados = new LinkedList<Titan>();
		LinkedList<Titan> lista_asesinados = new LinkedList<Titan>();
		int id;

		DatagramPacket pack;
		DatagramSocket sock;
		id = 0;

		InetAddress address,addr_distrito,addr_mult; // Hacia donde enviar y recibir
		int p_distr,p_multicast; // puertos de servidor distr y multicast
		String messageReturn,msgmulti,messageReturnDistrito; // lo que se obtiene del servidor
		byte[] data = null; // buffer para el mensaje a enviar

		Scanner input = new Scanner(System.in); // datos de entrada del cliente

		System.out.println("*****************************************");
		System.out.println("[Cliente]: Ingresar IP Servidor Central:");
		String ip_scentral = input.nextLine();
		System.out.println("[Cliente]: Ingresar Puerto Servidor Central:");
		String port_scentral = input.nextLine();
		System.out.println("[Cliente]: Introducir Nombre de Distrito a investigar, Ej: Trost, Shiganshina:");
		String distrito = input.nextLine(); // string con el nombre de la distrito a explorar

		if(isNumeric(port_scentral)){
			messageReturn = consultarDistrito(distrito,ip_scentral,port_scentral);
		}
		else{
			messageReturn = "error";
		}

		String mensajedenegado = new String("Permiso Denegado");
		
		if (messageReturn.trim().equals(mensajedenegado)){
			System.out.println("\nSe ha rechazado su conexion con el servidor central");
			System.exit(0);
		}
		if(messageReturn.trim().equals("timeout")){
			System.out.println("\nSe ha rechazado su conexion con el servidor central porque no hubo respuesta. Revise sus datos de acceso y ejecute otra vez el servidor de cliente.");
			System.exit(0);
		}
		if(messageReturn.trim().equals("error")){
			System.out.println("\nDatos de ingreso no validos.");
			System.exit(0);
		}
		else{
			System.out.println("\nConexion autorizada por el servidor central");
		}

		String[] div = messageReturn.split(" "); 

		addr_mult = InetAddress.getByName(div[1].trim());
		addr_distrito = InetAddress.getByName(div[0].trim());
		p_distr = Integer.parseInt(div[2].trim());
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
			
			switch (input.nextLine()){
				case "1": //LISTAR TITANES DEL DISTRITO
					mensaje = "LISTAR";
					ThreadDatagramS escuchar2 = new ThreadDatagramS(p_distr,addr_distrito);
					escuchar2.start();	
					messageReturnDistrito = consultarDistrito(mensaje,div[0].trim(),div[2].trim());
					escuchar2.stop();
					escuchar2 = new ThreadDatagramS(p_distr,addr_distrito); 
					escuchar2.start();
					resp = messageReturnDistrito;
					String[] titn = resp.split(":");
					int largo = titn.length-1;
					if (largo>0){
						System.out.println("*****************************************");
						System.out.println("[Cliente] Cantidad de Titanes por capturar en el distrito "+distrito+": "+largo);
						for (int t=0; t < largo; t++){
							titandato = titn[t].split(" ");
							int idtitan = Integer.parseInt(titandato[1])+1;
							System.out.println("*****");
							System.out.println("Nombre: "+titandato[0]);
							System.out.println("ID: "+idtitan);
							System.out.println("Tipo: "+titandato[2]);
						}
					}
					else{
						System.out.println("[Cliente] No hay Titanes libres en el distrito!");
					}
					break;
				case "2": // CAMBIAR DISTRITO
					System.out.println("*****************************************");
					System.out.println("[Cliente]: Ingresar IP Servidor Central:");
					ip_scentral = input.nextLine();
					System.out.println("[Cliente]: Ingresar Puerto Servidor Central:");
					port_scentral = input.nextLine();
					System.out.println("[Cliente]: Introducir Nombre distrito a investigar, Ej: Trost, Shiganshina:");
					distrito = input.nextLine(); // string con el nombre del distrito a explorar
					//messageReturn = consultarDistrito(distrito,ip_scentral,port_scentral);

					if(isNumeric(port_scentral)){
						messageReturn = consultarDistrito(distrito,ip_scentral,port_scentral);
					}
					else{
						messageReturn = "error";
					}

					if (messageReturn.trim().equals("Permiso Denegado")) {
						System.out.println("[Cliente]: No haz sido autorizado para explorar este distrito.");
						break;
					}
					if (messageReturn.trim().equals("timeout")) {
						System.out.println("[Cliente]: Aun permanece en el mismo distrito.");
						break;
					}
					if (messageReturn.trim().equals("error")) {
						System.out.println("[Cliente]: Datos mal ingresados, aun permanece en el mismo distrito.");
						break;
					}
					else{
						div = messageReturn.split(" ");
						addr_mult = InetAddress.getByName(div[1].trim());
						addr_distrito = InetAddress.getByName(div[0].trim());
						p_distr = Integer.parseInt(div[2].trim());
						p_multicast = Integer.parseInt(div[3].trim());

						escuchar.stop();
						escuchar = new ThreadMulticast(p_multicast,addr_mult); 
						escuchar.start();
					}

					break;
				case "3":// CAPTURAR TITAN

					System.out.println("*****************************************");
					System.out.println("[Cliente] ID de Titan a capturar:");
					String numero = input.nextLine();
					mensaje = "CAPTURAR "+Integer.parseInt(numero);
					try{
						byte[] dattc;
						dattc = mensaje.getBytes("UTF-8");
						pack = new DatagramPacket(dattc, dattc.length, addr_distrito, p_distr);
						sock = new DatagramSocket();
						sock.send(pack);
						dattc = new byte[packetSize];
						pack = new DatagramPacket(dattc, dattc.length);
						sock.setSoTimeout(1000);
    					try {
        					sock.receive(pack);
    					} catch (SocketTimeoutException ste) {
        					System.out.println("ID titan no valido");
        					break;
						}
						resp= new String(pack.getData());

						if (resp.split(" ")[0].equals("Ups!")){
							System.out.println("[Cliente] "+ resp);
						}
						else
						{
							String[] ttitann = resp.split(" ");
							Titan tan_catch = new Titan( ttitann[0],Integer.parseInt(ttitann[2].trim()),ttitann[1],ttitann[3].trim());
							lista_capturados.add(tan_catch);
							System.out.println("[Cliente] Titan capturado!");
							System.out.println("id: "+ttitann[2]);
							System.out.println("Nombre: "+ttitann[0]);
							System.out.println("Tipo: "+ ttitann[1]);
							id=id+1;
						}
					}
					catch(IOException e){
						System.out.println(e.getMessage());
					}
					break;
				case "4":// ASESINAR TITAN
	
					System.out.println("*****************************************");
					System.out.println("[Cliente] ID de Titan a Asesinar:");
					numero = input.nextLine();
					mensaje = "ASESINAR "+Integer.parseInt(numero);
					try{
						byte[] dattc;
						dattc = mensaje.getBytes("UTF-8");
						pack = new DatagramPacket(dattc, dattc.length, addr_distrito, p_distr);
						sock = new DatagramSocket();
						sock.send(pack);
						dattc = new byte[packetSize];
						pack = new DatagramPacket(dattc, dattc.length);
						sock.setSoTimeout(1000);
    					try {
        					sock.receive(pack);
    					} catch (SocketTimeoutException ste) {
        					System.out.println("ID titan no valido");
        					break;
						}
						resp= new String(pack.getData());
						if (resp.split(" ")[0].equals("Ups!")){
							System.out.println("[Cliente] "+ resp);
						}
						else
						{
							String[] ttitann = resp.split(" ");
							Titan tan_kill = new Titan( ttitann[0],Integer.parseInt(ttitann[2].trim()),ttitann[1],ttitann[3].trim());
							lista_asesinados.add(tan_kill);
							System.out.println("[Cliente] Titan asesinado!");
							System.out.println("id: "+ttitann[2]);
							System.out.println("Nombre: "+ttitann[0]);
							System.out.println("Tipo: "+ ttitann[1]);
							id=id+1;
						}
					}
					catch(IOException e){
						System.out.println(e.getMessage());
					}

					break;
				case "5":// LISTAR TITANES CAPTURADOS
					if (lista_capturados.size()>0){
						System.out.println("*****************************************");
						System.out.println("[Cliente] Titanes actualmente capturados!");
						for (Iterator<Titan> i = lista_capturados.iterator(); i.hasNext();) {
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
				case "6":// LISTAR TITANES ASESINADOS
					if (lista_asesinados.size()>0){
						System.out.println("*****************************************");
						System.out.println("[Cliente] Titanes que haz asesinado!");
						for (Iterator<Titan> i = lista_asesinados.iterator(); i.hasNext();) {
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
						System.out.println("[Cliente] No has asesinado a ningun Titan!");
					}
					break;
				default:
					System.out.println("[SERVIDOR CLIENTE] COMANDO NO RECONOCIDO");
					break;
			}
		}
	}
}