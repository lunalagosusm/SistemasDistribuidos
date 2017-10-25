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
	public LinkedList<Titan> lista;
	private String NombreSZ;
	private int puertoM;
	private String s;//=null;
	public int id;
	public String nombre;
	public String tipo;
	public Consola(LinkedList<Titan> lst, String addr, int puerto, String NomSZ){
		this.lista=lst;
		this.address=addr;
		this.puertoM=puerto;
		this.NombreSZ=NomSZ;
		}
	
	public void run(){
		id=0;
		System.out.println("[SERVIDOR DISTRITO:"+NombreSZ+"] CONSOLA");
		while(true){
			s = sc.nextLine().trim();
//			switch (s){
//				case "Publicar Titan":
			String[] frac = s.split(" ");
			if (frac.length == 2) {
				switch (frac[0].toString().toUpperCase()){
					case "PUBLICAR":
						switch (frac[1].toString().toUpperCase()){
							case "TITAN":
								System.out.println("[SERVIDOR DISTRITO:"+NombreSZ+"] Introducir Nombre:");
								nombre = sc.nextLine();
								System.out.println("[SERVIDOR DISTRITO:"+NombreSZ+"] Introducir Tipo:");
								System.out.println("1.- Normal");
								System.out.println("2.- Exentrico");
								System.out.println("3.- Cambiante");
								switch (Integer.parseInt(sc.nextLine())){
									case 1:
										tipo = "Normal";
										break;
									case 2:
										tipo = "Exentrico";
										break;
									case 3:
										tipo = "Cambiante";
										break;
									default:
										tipo = "Desconocido";
								}
								Titan tit = new Titan(nombre,id,tipo);
								id = id + 1;
					
								//agregar Titan a lista local
								lista.add(tit);
								//
								//mandar aviso por multicast
								try{
									String inf;
									byte [] dato;
									inf = nombre+" "+tipo+" "+id;
									dato = inf.getBytes("UTF-8");
									DatagramPacket dataG = new DatagramPacket(dato, dato.length, InetAddress.getByName(address), puertoM);
									MulticastSocket enviar = new MulticastSocket();
									enviar.send(dataG);
									enviar.close();
								}
								catch (IOException e){
									System.out.println(e.getMessage());
								}
								System.out.println("[SERVIDOR DISTRITO: "+NombreSZ+"] Se ha publicado a "+nombre);
								System.out.println("**************");
								System.out.println("ID: "+id);
								System.out.println("Nombre: "+nombre);
								System.out.println("Tipo: "+tipo);
								System.out.println("**************");
								//
								break;
							default:
								System.out.println("[SERVIDOR DISTRITO:"+NombreSZ+"] (2) Comando no Reconocido");
						}
						break;
					default:
						System.out.println("[SERVIDOR DISTRITO:"+NombreSZ+"] (1) Comando no Reconocido");
				}
			}
			else{
				System.out.println("[SERVIDOR DISTRITO:"+NombreSZ+"] (0) Comando no Reconocido");				
			}
		}
	}
}
