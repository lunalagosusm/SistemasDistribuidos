import java.io.IOException;
import java.lang.Integer;
import java.lang.String;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Iterator;

public class Peticiones extends Thread{
	private static Scanner sc;
	private String address;
	public LinkedList<Distribumon> lista;
	private String NombreSZ;
	private int puertoP;
	private String s=null;
	private String peti = null;
	public int idc;
	public InetAddress clientAddr;
	public int clientPort;
	public String aviso;
	private int flag=0;
/*	public String nombre;
	public int nivel;*/
	
	public int packetSize = 1024;
	
	////

	////
	
	public Peticiones(LinkedList<Distribumon> lst, String addr, int puerto, String NomSZ){
		this.lista=lst;
		this.address=addr;
		this.puertoP=puerto;
		this.NombreSZ=NomSZ;
	}
	@SuppressWarnings("resource")
	public void run(){
		DatagramPacket packet;
		DatagramSocket socket;
		byte[] data;
		
		
		for(;;){

//			System.out.println("UdpServer: Esperando recibir paquetes ....");

			try {
				data = new byte[packetSize];
				socket = new DatagramSocket(clientPort);
				packet = new DatagramPacket(data, packetSize);
				
				// esperar indefinidamente la llegada de un paquete
				aviso = null;
				System.out.println("esperando la wea");
				socket.receive(packet);
				System.out.println("recibi la wea");
				clientAddr = packet.getAddress();
				clientPort = packet.getPort();
				
				peti= new String(packet.getData());
				String[] s = peti.split(" ");
				switch (s[0].toUpperCase()){
					case "CAPTURAR":
						if (s.length>1){
							int idc = Integer.parseInt(s[1]);
							//buscar en lista
							for (Iterator<Distribumon> i = lista.iterator(); i.hasNext();) {
								Distribumon pokemon = i.next();
								if (pokemon.id == idc){
									flag=1;
									aviso = pokemon.nombre+" "+pokemon.nivel;
									//eliminar
									i.remove();
									//mensaje al cliente q
									break;
								}
							}
							//si no esta, informar al cliente q
							if (flag==0){
								aviso = "Ups! Distribumon no disponible";
								System.out.println("Ups! Distribumon no disponible");//////////////
							}
							else{
								flag=0;
								System.out.println("ok");//////////////
							}
						}
						else{
							//error, avisarle al cliente q
							aviso = "Ups! Distribumon no identificable";
							System.out.println("Ups! Distribumon no identificable");///////////////
						}
						break;
					case "LISTAR":
						//crear superstring iterando sobre lista
						for (Iterator<Distribumon> i = lista.iterator(); i.hasNext();) {
							Distribumon pokemon = i.next();
							aviso = aviso+":"+pokemon.nombre+" "+pokemon.id+" "+pokemon.nivel;
						}	//enviar a cliente
						break;
					default:
						aviso = "Ups! no hubo procesamiento de solicitud";
						System.out.println("Ups! no hubo procesamiento de solicitud");////////////
						break;
				}
				byte[] dataSend;
				dataSend = aviso.getBytes("UTF-8");
				packet = new DatagramPacket(dataSend, dataSend.length, clientAddr, clientPort);
				socket.send(packet);
				//socket.close();
			} catch (IOException ie) {
				System.out.println("UdpServer: No se pudo recibir:" + ie.getMessage());
				continue;
			}
			
		}
	}
}
