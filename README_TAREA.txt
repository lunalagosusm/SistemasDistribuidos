Tarea n°1 - Sistemas Distribuidos  *
Grupo: 19                          *
Integrantes: Pedro Luna  pluna     *
			 Alex Peña   apena     *
************************************

- Se trabajo con la version 1.8 de java para el desarrollo de la tarea, pero se usaron elementos que aun son compatibles con la version 1.7 de las maquinas habilitadas.
- Los codigos se almacenaron y distribuyeron de la siguiente manera:

	10.10.2.216 -> ServidorCentral 
                   -> ServidorCentral.java
								   -> Servidor.java

    10.10.2.92  -> Cliente         
                    -> Cliente.java
    				-> Titan.java
    				-> ThreadMulticast.java
    			    -> ThreadDatagramS.java

    10.10.2.232 -> Distrito        
                     -> Distrito.java
    				 -> Titan.java
    				 -> Consola.java

- La clave para acceder a todas las maquinas es "alexpedro"
- Para compilar los codigos basta colocar en cada maquina el comando "make"
- Para ejecutar los archivos de cada servidor, en cada maquina se debe ejecutar el comando "make run".
- Para borrar los .class basta colocar "make clean" en cada máquina.
- El cliente se debe conectar al servidor central unicamente por el puerto 5000.

Explicacion General

- El servidorCentral actua como una "agenda" que almacena la informacion de los servidores de distrito, y además autoriza la conexion a los clientes. Este servidor debe crear primero los distritos y despues contesta las autorizaciones. Al comienzo pregunta cuantos distritos desea crear, por comodidad en el ingreso de datos, se recomienda colocar un numero pequeño como 2 o 3, para no perder tiempo en ese proceso de prueba al ingresar datos.
- El Cliente almacena en dos arrylist los titanes que captura y asesina para mayor facilidad de listar ambos titanes.
- El cliente espera durante 20 segundos la respuesta del ServidorCentral, un a vez pasado los 20 segundos, se queda en el mismo distrito o se sale el programa si es que el cliente se le rechaza la conexion en el primer intento.
- En distrito, se manejan los servidores de distrito que crea el Servidor Central, crea un arraylist que almacena todos los titanes que se publica por distrito, y a la vez difunde el mensaje a los clientes que se encuentran conectados a éste.