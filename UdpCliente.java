import java.net.*;
import java.io.*;

public static void main(String[] args) {
		try {

			// 1. crear el socket por donde se enviara la peticion y se recibira
			// la respuesta..
			DatagramSocket socket = new DatagramSocket(32000);

			// 2. crear datagrama para enviar la info. el datagrama contiene
			// toda la info necesaria para que llegue el msj
			String msj = "Hola Server....."; // msj a enviar.
			String ip = "127.0.0.1";
			int port = 45000;
			// 2.1 crear datagrama
			DatagramPacket paqueteEnvio = new DatagramPacket(msj.getBytes(),
					msj.length(), InetAddress.getByName(ip), port);
			// 2.2 enviar paquete.
			socket.send(paqueteEnvio);

			// 3. recibir respuesta...

			// 3.1 crear datagrama de recepcion.
			byte[] resp = new byte[1024];
			DatagramPacket paqueteRecibido = new DatagramPacket(resp,
					resp.length);

			// 3.2 recibir paquete.
			socket.receive(paqueteRecibido);

			// 4. mostrar info...
			System.out.println("Server respondio desde "
					+ paqueteRecibido.getAddress().getHostAddress()
					+ " por el puerto " + paqueteRecibido.getPort()
					+ " se recibio:" + new String(paqueteRecibido.getData()));

			// 5. cerrar
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
