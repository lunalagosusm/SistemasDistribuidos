import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class ZoneServer {
	private static Scanner sc;
	public static void main (String[] args) throws IOException {
		sc = new Scanner (System.in);
		Vector<int> vector = new Vector <int>();
		System.out.println("[SERVIDOR ZONA] Nombre Servidor:");
		String nombre = sc.nextLine();
		System.out.println("[SERVIDOR ZONA:"+nombre+"] IP Multicast:");
		String addrMulti = sc.nextLine();
		System.out.println("[SERVIDOR ZONA:"+nombre+"] Puerto Multicast:");
		String PtoMulti = sc.nextLine();
		System.out.println("[SERVIDOR ZONA:"+nombre+"] IP Peticiones:");
		String addrPet = sc.nextLine();
		System.out.println("[SERVIDOR ZONA:"+nombre+"] Puerto Peticiones:");
		String PtoPet = sc.nextLine();
		
		Consola hilo = new Consola(vector, addrMulti, PtoMulti);
		//EstadoD hilo2 = new EstadoD();
		hilo.start();
//		hilo2.start();
/*
		System.out.println("nombre "+nombre);
		System.out.println("ipmulti "+addrMulti);
		System.out.println("Ptomulti "+PtoMulti);
		System.out.println("ippet "+addrPet);
		System.out.println("ptopet "+PtoPet);
*/
		}

	}
