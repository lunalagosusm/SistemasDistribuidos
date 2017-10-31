import java.net.*;
import java.io.*;
import java.util.*;

//clase que contiene la logica del servidor de Cliente
public class Cliente{

	public String[] titandato;	//varible para operar con los elementos de las listas de titanes
	public String mensaje;	//variable para la comunicacion por soket
	public int packetSize;	//tamano de paquetes a enviar
	public String resp;	//variable para la respuesta por socket
	public LinkedList<Titan> lista_capturados; //almacena los titanes capturados
	public LinkedList<Titan> lista_asesinados; //almacena los titanes asesinados
	public int id; // id para titanes
	
	//consultarDistrito sirve para que el cliente se pueda comunicar con el distrito al que esta conectado
	public static String consultarDistrito(String nombre,String direccion, String puerto){

		DatagramSocket socket; // para enviar datos
		DatagramPacket packet; // lo que se envia
		InetAddress address; //direccion ip del socket a utilizar
		byte[] data = null; //variable a enviar en la comunicacion por socket
		String msgReturn = null; //variable para almacenar la respuesta del socket

		//El siguiente codigo realiza la comunicacion
		try {
			address = InetAddress.getByName(direccion);
			socket = new DatagramSocket();
			data = nombre.getBytes("UTF-8"); // leer mensaje a enviar desde variable distr
			packet = new DatagramPacket(data, data.length, address, Integer.parseInt(puerto));
			socket.send(packet); //se envia la informacion
			data = new byte[1024];
			packet = new DatagramPacket(data, data.length);
			socket.setSoTimeout(20000);	//si no hay respuesta del socket consultado, a los 20 segundos se retorna un mensaje con "timeout"
    			try {
        			socket.receive(packet);
        			socket.close();
					msgReturn = new String(packet.getData());
    			} catch (SocketTimeoutException ste) {
        			System.out.println("No hubo respuesta del servidor, tiempo de conexion mayor a 20 segundos.");
        			msgReturn="timeout";
    			}

		} catch (IOException ex) {
			System.out.println("[Cliente]: Error = " + ex.getMessage());
			//System.exit(0);
			msgReturn="error";
		}		
		return msgReturn;
	}

	//funcion para mejorar la robustes al ingresar puertos y algunas consultas del codigo
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
		
		//se inicializan variables declaradas mas arriba
		String[] titandato=null;
		String mensaje = null;
		int packetSize = 1024;
		String resp=null;
		LinkedList<Titan> lista_capturados = new LinkedList<Titan>();
		LinkedList<Titan> lista_asesinados = new LinkedList<Titan>();
		int id = 0;

		DatagramPacket pack;
		DatagramSocket sock;

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

		//se comprueba si hay error al ingresar el puerto
		if(isNumeric(port_scentral)){
			messageReturn = consultarDistrito(distrito,ip_scentral,port_scentral);
		}
		else{
			messageReturn = "error";
		}

		//Si se niego la conexion desde el servidor central
		String mensajedenegado = new String("Permiso Denegado");
		
		if (messageReturn.trim().equals(mensajedenegado)){
			System.out.println("\nSe ha rechazado su conexion con el servidor central");
			System.exit(0);
		}

		//si no hay respuesta desde el socket
		else if(messageReturn.trim().equals("timeout")){
			System.out.println("\nSe ha rechazado su conexion con el servidor central porque no hubo respuesta. Revise sus datos de acceso y ejecute otra vez el servidor de cliente.");
			System.exit(0);
		}

		//si hay un error al ingresar los datos de conexion
		else if(messageReturn.trim().equals("error")){
			System.out.println("\nDatos de ingreso no validos.");
			System.exit(0);
		}

		//si la conecxion se realiza correctamente y sin errores
		else{
			System.out.println("\nConexion autorizada por el servidor central");
		}

		//se leen los datos de la respuesta del servidor
		String[] div = messageReturn.split(" "); 

		addr_mult = InetAddress.getByName(div[1].trim());
		addr_distrito = InetAddress.getByName(div[0].trim());
		p_distr = Integer.parseInt(div[2].trim());
		p_multicast = Integer.parseInt(div[3].trim());

		//hilo para escuchar permanentemete si el cliente recibe un mensaje por multicast
		ThreadMulticast escuchar = new ThreadMulticast(p_multicast,addr_mult); 
		escuchar.start();

		for (; ; ) {

			//opciones cliente
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
					//se abre un socket a treaves del siguiente hilo para la comunicacion entre cilente y distrito
					ThreadDatagramS escuchar2 = new ThreadDatagramS(p_distr,addr_distrito);
					escuchar2.start();
					messageReturnDistrito = consultarDistrito(mensaje,div[0].trim(),div[2].trim());
					escuchar2.stop();
					escuchar2 = new ThreadDatagramS(p_distr,addr_distrito); 
					escuchar2.start();
					//se recupera la respuesta del distrito y se imprimen los titanes disponibles
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
					//se ingresan los datos del distrito a conectarse
					System.out.println("*****************************************");
					System.out.println("[Cliente]: Ingresar IP Servidor Central:");
					ip_scentral = input.nextLine(); // string con la direccion del servidor central
					System.out.println("[Cliente]: Ingresar Puerto Servidor Central:");
					port_scentral = input.nextLine(); /// string con el puerto del servidor central
					System.out.println("[Cliente]: Introducir Nombre distrito a investigar, Ej: Trost, Shiganshina:");
					distrito = input.nextLine(); // string con el nombre del distrito a explorar

					//verificacion de puerto
					if(isNumeric(port_scentral)){
						messageReturn = consultarDistrito(distrito,ip_scentral,port_scentral);
					}
					else{
						messageReturn = "error";
					}

					//si se rechaza la conexion
					if (messageReturn.trim().equals("Permiso Denegado")) {
						System.out.println("[Cliente]: No haz sido autorizado para explorar este distrito.");
						break;
					}
					//si no hay respuesta del servidor central
					else if (messageReturn.trim().equals("timeout")) {
						System.out.println("[Cliente]: Aun permanece en el mismo distrito.");
						break;
					}
					//si se produce un error al ingresar los datos
					else if (messageReturn.trim().equals("error")) {
						System.out.println("[Cliente]: Datos mal ingresados, aun permanece en el mismo distrito.");
						break;
					}
					//en caso de ningun error o conexion rechazada
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
					//se recibe el ID del titan a capturar
					System.out.println("*****************************************");
					System.out.println("[Cliente] ID de Titan a capturar:");
					String numero = input.nextLine();

					//se verifica que el id ingresado sea un numero
					if(isNumeric(numero)){
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
        						sock.receive(pack); //si no hay problemas con id de titan
    						} catch (SocketTimeoutException ste) {
        						System.out.println("ID titan no valido");//si el id no corresponde a un titan libre
        						break;
							}
							resp= new String(pack.getData());

							if (resp.split(" ")[0].equals("Ups!")){
								System.out.println("[Cliente] "+ resp);
							}
							else //se imprimen datos del titan capturado
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
					}
					else{
						System.out.println("ID titan no valido");
						break;
					}
					break;
				case "4":// ASESINAR TITAN
					//se recibe el ID del titan a capturar
					System.out.println("*****************************************");
					System.out.println("[Cliente] ID de Titan a Asesinar:");
					numero = input.nextLine();
					//se verifica que el id ingresado sea un numero
					if(isNumeric(numero)){
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
        						sock.receive(pack);//si no hay problemas con id de titan
    						} catch (SocketTimeoutException ste) {
        						System.out.println("ID titan no valido");//si el id no corresponde a un titan libre
        						break;
							}
							resp= new String(pack.getData());
							if (resp.split(" ")[0].equals("Ups!")){
								System.out.println("[Cliente] "+ resp);
							}
							else //se imprimen datos del titan capturado
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
					}
					else{
						System.out.println("ID titan no valido");
						break;
					}
					break;
				case "5":// LISTAR TITANES CAPTURADOS
					//en caso de haber algun titan capturado
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
					else{ //en caso de no haber un titan caputado
						System.out.println("*****************************************");
						System.out.println("[Cliente] No has capturado ningun Titan!");
					}
					break;
				case "6":// LISTAR TITANES ASESINADOS
					//en caso de haber algun titan asesinado
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
					else{ //en caso de no haber algun titan capturado

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