import java.io.IOException;
import java.lang.Integer;
import java.lang.String;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.LinkedList;

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
		Peticiones hilo2 = new Peticiones(lista, addrPet, Integer.parseInt(PtoPet), nombre);
		hilo.start();
		hilo2.start();
/*
		System.out.println("nombre "+nombre);
		System.out.println("ipmulti "+addrMulti);
		System.out.println("Ptomulti "+PtoMulti);
		System.out.println("ippet "+addrPet);
		System.out.println("ptopet "+PtoPet);
*/
		}

	}