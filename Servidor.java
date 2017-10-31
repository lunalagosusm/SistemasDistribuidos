import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor { // Clase de tipo servidor en donde se almacenara datos de servidor distrito
 
   	public String nombre,ipdistr,ipmult,ppet,pmult;
    // Constructor de la clase
	public Servidor(String nombre, String ipdistr, String ipmult, String ppet, String pmult) {
        this.nombre = nombre;
        this.ipmult = ipmult;
        this.ipdistr = ipdistr;
        this.ppet = ppet;
        this.pmult = pmult;
	}
}