

public static void main(String[] args) {

		try {
			System.out.println("server creado........");
			// 1. crear el servidor..
			DatagramSocket socket = new DatagramSocket(45000);

			// 2. recibir mensaje desde el cliente...

			// 2.1 crear el paquete donde se recibe el mensaje.
			byte[] buffer = new byte[1024];
			DatagramPacket paqueteCliente = new DatagramPacket(buffer, 1024);
			// 2.2 recibir el paquete. operacion bloqueante.
			System.out.println("socket esperando....");
			socket.receive(paqueteCliente);

			// 2.3 leer el paquete como string...
			String msj = new String(paqueteCliente.getData());

			System.out.println("desde "
					+ paqueteCliente.getAddress().getHostAddress()
					+ " desde el puerto " + paqueteCliente.getPort()
					+ " se recibio:" + msj);

			// 3. enviar respuesta..
			String resp = new Date().toString();// la hora como respuesta.
			// 3.1 crear datagrama de envio.
			// direccion destino..
			InetAddress addr = paqueteCliente.getAddress();// la misma del
												// cliente.
			int port = paqueteCliente.getPort();
			// el datagrama contiene la información del destino.
			DatagramPacket paqueteEnvio = new DatagramPacket(resp.getBytes(),
					resp.length(), addr, port);
			System.out.println("enviando:"+new String(paqueteEnvio.getData()));
			// 3.2 enviar paquete...
			socket.send(paqueteEnvio);
			
			//4. cerrar el socket...
			socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
