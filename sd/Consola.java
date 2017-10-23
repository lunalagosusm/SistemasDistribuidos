import java.io.IOException;
import java.lang.Integer;
import java.lang.String;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;
import java.util.LinkedList;

public class Consola extends Thread{
	private Scanner sc = new Scanner(System.in);
	//private Vector<int> vec;
	private String address;
	public LinkedList<Distribumon> lista;
	private String NombreSZ;
	private int puertoM;
	private String s;//=null;
	public int id;
	public String nombre;
	public int nivel;
	public Consola(LinkedList<Distribumon> lst, String addr, int puerto, String NomSZ){
		this.lista=lst;
		this.address=addr;
		this.puertoM=puerto;
		this.NombreSZ=NomSZ;
		}
	
	public void run(){
		id=0;
		System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] CONSOLA");
		while(true){
			s = sc.nextLine().trim();
//			switch (s){
//				case "Publicar Distribumon":
			String[] frac = s.split(" ");
			if (frac.length == 2) {
				switch (frac[0].toString().toUpperCase()){
					case "PUBLICAR":
						switch (frac[1].toString().toUpperCase()){
							case "DISTRIBUMON":
					System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] Introducir Nombre:");
					nombre = sc.nextLine();
					System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] Introducir Nivel:");
					nivel = Integer.parseInt(sc.nextLine());
					Distribumon poke = new Distribumon(nombre,id,nivel);
					id=id+1;
					
					//agregar distribumon a lista local
					lista.add(poke);
					//
					//mandar aviso por multicast
					try{
						byte[] utf8bytes = nombre.getBytes("UTF-8");
						byte [] dato = new byte[utf8bytes.length];
						dato =nombre.getBytes();
						DatagramPacket dataG = new DatagramPacket(dato, dato.length, InetAddress.getByName(address), puertoM);
						MulticastSocket enviar = new MulticastSocket();
						enviar.send(dataG);
						enviar.close();
					}
					catch (IOException e){
						System.out.println(e.getMessage());
					}
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
			}
			else{
				System.out.println("[SERVIDOR ZONA:"+NombreSZ+"] (0) Comando no Reconocido");
								
			}
		}
	}
}
