import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
  public static void main(String argv[]) throws Exception {
        String mensajeCliente;
        String mensajeRespuesta;

        // Crea un socket de servidor en el puerto 6789, esperando conexiones entrantes.
        ServerSocket socketBienvenida = new ServerSocket(6789);
        
        while(true) {
            Socket socketConexion = socketBienvenida.accept();

            BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socketConexion.getInputStream()));
            DataOutputStream salidaCliente = new DataOutputStream(socketConexion.getOutputStream());
            
            // este ciclo es el que permite que se procesen multiples mensajes del mismo cliente
            while (true) {
                mensajeCliente = entradaCliente.readLine();
                
                if (mensajeCliente.equals("CERRAR CONEXION")) {
                    // si el cliente envia el mensaje reservado se termina la conexion y se muestra el mensaje de abajo
                    salidaCliente.writeBytes("La conexion ha sido cerrada exitosamente por peticion del cliente\n");
                    socketConexion.close();
                    break; //rompe el ciclo y espera una nueva conexion
                }
                
                // aca se invierten minusculas a mayusculas y viceversa
                String mensajeInvertido = invertirMayusMinus(mensajeCliente);
                
                // aca se ordena alfabeticamente el mensaje
                mensajeRespuesta = ordenarAlfabeticamente(mensajeInvertido);

                salidaCliente.writeBytes(mensajeRespuesta + '\n'); //se da la respuesta al cliente
            }
        }
    }

    // se invierten mayusculas y minusculas letra por letra
    private static String invertirMayusMinus(String mensaje) {
    StringBuilder resultado = new StringBuilder();

    for (char c : mensaje.toCharArray()) {
        if (Character.isUpperCase(c)) {
            resultado.append(Character.toLowerCase(c));
        } else if (Character.isLowerCase(c)) {
            resultado.append(Character.toUpperCase(c));
        } else {
            resultado.append(c); // conservar espacios, números, signos, etc.
        }
    }

    return resultado.toString();
}

    private static String ordenarAlfabeticamente(String mensaje) {

        // Dividir el mensaje en palabras y ordenarlas alfabeticamente
        String[] palabras = mensaje.split(" "); //divide el mensaje en palabras
        Arrays.sort(palabras);                        // ordena alfabeticamente las palabras
        return String.join(" ", palabras);  // vuelve a unirlas en un solo string
    }
}

