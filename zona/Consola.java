import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;
import java.util.LinkedList;

public class Consola extends Thread{
	private Scanner sc = new Scanner(System.in);
	private Vector<int> vec;
	private String address;
	private int puertoM;
	private String s=null;
	public int id;
	public Consola(Vector<int> vector, String addr, int puerto){
		this.vec=vector;
		this.address=addr;
		this.puertoM=puerto;
		}
	
	public void run(){
		id=0;
		while(true){
			s = sc.nextLine();
			String[] frac = s.split(" ");
			if (frac.length == 2) {
				switch (frac[0].toUpperCase()){
					case "PUBLICAR":					
						switch (frac[1].toUpperCase()){
							case "DISTRIBUMON":
								System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] Introducir Nombre:");
								nombre = sc.nextLine();
								System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] Introducir Nivel:");
								nivel = sc.nextLine();
								Distribumon = new Distribumon(nombre,id,nivel, "Free");
								id=id+1;
								//agregar distribumon a lista local
								
								//mandar aviso por multicast
								byte[] utf8bytes = nombre.getBytes("UTF-8");
								byte [] dato = new byte[utf8bytes.length];
								dato =nombre.getBytes();
								DatagramPacket dataG = new DatagramPacket(dato, dato.length, InetAddress.getByName(address), puertoM);
								MulticastSocket enviar = new MulticastSocket();
								enviar.send(dataG);
								enviar.close();
								System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] Se ha publicado a "+nombre);
								//
								break;
							default:
								System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] (2) Comando no Reconocido");
							}
						break;
					default:
						System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] (1) Comando no Reconocido");
			}
			else{
				System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] (0) Comando no Reconocido");
								
			}
		}
