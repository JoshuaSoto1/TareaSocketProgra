import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TCPCliente extends JFrame {

    private JTextArea areaTexto;
    private JTextField campoTexto;
    private JButton botonEnviar;
    private JButton botonDesconectar;
    private Socket socketCliente;
    private DataOutputStream salidaServidor;
    private BufferedReader entradaServidor;
    
    public TCPCliente() {
        //configuracion basica de la ventana cliente
        setTitle("Cliente TCP");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Configuración de la interfaz (no editable)
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        areaTexto.setBackground(new Color(240, 240, 240));
        areaTexto.setForeground(new Color(0, 0, 0));
        
        //componentes 
        campoTexto = new JTextField(30);
        botonEnviar = new JButton("Enviar");
        botonDesconectar = new JButton("Desconectar");
        
        // aca se personalizan los botones
        botonEnviar.setBackground(new Color(50, 150, 255));
        botonEnviar.setForeground(Color.WHITE);
        botonEnviar.setFocusPainted(false);
        
        botonDesconectar.setBackground(new Color(255, 70, 70));
        botonDesconectar.setForeground(Color.WHITE);
        botonDesconectar.setFocusPainted(false);
        
        // Layout principal de la ventana
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        
        //panel inferior que contiene el campo de texto y los botones
        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new FlowLayout());
        panelEntrada.add(campoTexto);
        panelEntrada.add(botonEnviar);
        panelEntrada.add(botonDesconectar);
        
        panel.add(panelEntrada, BorderLayout.SOUTH); 
        
        add(panel); // agrega todo el panel a la ventana principal
        
        // Acciones de los botones

        //para enviar
        botonEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
        // para desconectar 
        botonDesconectar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                desconectar();
            }
        });
    }
      
    // Metodo que establece la conexion con el servidor.
    private void conectar() throws IOException {
        socketCliente = new Socket("localhost", 6789);
        salidaServidor = new DataOutputStream(socketCliente.getOutputStream());
        entradaServidor = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        
        // Aca se muestra el mensaje de conexión exitosa
        JOptionPane.showMessageDialog(this, "¡Conexion exitosa con el servidor!", "Conexión", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //Metodo que envia un mensaje al servidor.
    private void enviarMensaje() {
        try {
            String mensaje = campoTexto.getText();
            
            if (mensaje.length() > 100) {
                JOptionPane.showMessageDialog(this, "El mensaje no puede superar los 100 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Enviar el mensaje al servidor
            salidaServidor.writeBytes(mensaje + '\n');
            
            // Recibir la respuesta modificada del servidor
            String mensajeModificado = entradaServidor.readLine();
            
            // Mostrar el mensaje modificado en el área de texto
            areaTexto.append("Mensaje convertido proveniente del servidor: " + mensajeModificado + "\n");
            
            campoTexto.setText(""); // limpia el campo de entrada
            
        } catch (IOException e) {
            e.printStackTrace(); //imrrime error en consola si ocurre
        }
    }
    
     //Metodo que solicita al servidor cerrar la conexion
    private void desconectar() {
        try {
            salidaServidor.writeBytes("CERRAR CONEXION\n");  // Mensaje o palabra reservada para cerrar la conexión
            socketCliente.close();
            areaTexto.append("La conexion ha sido cerrada exitosamente por peticion del cliente \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // la clase main con el metodo principal que inicia la interfaz grafica del cliente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TCPCliente cliente = new TCPCliente(); //crea instancia de cliente
                cliente.setVisible(true); //muestra ventana
                
                try {
                    cliente.conectar(); //intenta conectar al servidor
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
