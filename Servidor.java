import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor { // Clase de tipo servidor en donde se almacenara datos de servidor zona
 
   	public String nombre,ipzona,ipmult,ppet,pmult;
    // Constructor de la clase
	public Servidor(String nombre, String ipzona, String ipmult, String ppet, String pmult) {
        this.nombre = nombre;
        this.ipmult = ipmult;
        this.ipzona = ipzona;
        this.ppet = ppet;
        this.pmult = pmult;
	}
}