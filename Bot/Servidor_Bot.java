package Bot;
import java.awt.BorderLayout;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import com.sun.speech.freetts.*;
import javax.swing.*;
import com.sun.speech.freetts.*;
public class Servidor_Bot {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		marco_Servidor m_s = new marco_Servidor();
		m_s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
class marco_Servidor extends JFrame{
	public marco_Servidor() {
		setBounds(400,400,350,350);
		add(new lamina_Servidor());
		setVisible(true);
	}
	
}
class lamina_Servidor extends JPanel implements Runnable{
	public lamina_Servidor() {
		setLayout(new BorderLayout());
		JPanel centro = new JPanel ();
		Contenido_Servidor = new JTextArea(20,20);
		JScrollPane laminaScroll = new JScrollPane(Contenido_Servidor);
		Contenido_Servidor.setLineWrap(true);
		centro.add(laminaScroll );
		Texto_Servidor = new JLabel("Hola! Soy Devil Sheep !!!!  " + "\n" +  "\n" + "En qué podemos ayudarte?" + "\n" +  "\n" + " 1. Problemas con el pedido " +  "\n" + " 2. Información de devoluciones " + "\n" + " 3. Puntos de recogida "  + "\n" + " 4. Ninguna de las opciones ");
		add(centro, BorderLayout.CENTER);
		Thread miHilo = new Thread(this);
	    miHilo.start();	
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			//Inicia un socket para envíar la info incial al cliente.
			Socket_Inicio inicio = new Socket_Inicio(IP, 9998,"");
			//Escucha el servidor al cliente
			Escucha_servidor Recibe = new Escucha_servidor(9993);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Clases construidas  para optimzar el código que no tengamos tantas líneas.
	 */
	class Socket_Inicio{
		public Socket_Inicio(String Ip, int puerto,String mensaje) {
			try {
				Socket socket_envio_Info = new Socket(Ip,puerto);
				
				if(mensaje.length() == 0) {
					DataOutputStream flujoSalida1= new DataOutputStream (socket_envio_Info.getOutputStream());
					flujoSalida1.writeUTF(Texto_Servidor.getText());
					flujoSalida1.close();
				}
				
				if(mensaje.length() > 0) {
					DataOutputStream flujoSalida1= new DataOutputStream (socket_envio_Info.getOutputStream());
					flujoSalida1.writeUTF(mensaje);
					flujoSalida1.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class Escucha_servidor{
		public Escucha_servidor(int puerto) {
			try {
				//Inicia el Server que queda a la espera de la información
				ServerSocket Servidor_Escucha = new ServerSocket(puerto);
				//Variables que guardarán la información que llegue en el paquete mandado por el cliente
				String Opcion_Usu, Loguear, n_Pedido, mensaje_del_cliente;
				//Se instancia un objeto de la clase que empaqueta la info
				Informacion_Cliente datos_paquete;
				while(true){
					// Se crea la aceptación de lo que llegue por el socket que está a la espera
					Socket miSocket = Servidor_Escucha.accept(); 
					ObjectInputStream flujoDatosEntrada = new ObjectInputStream( miSocket.getInputStream());
					datos_paquete = (Informacion_Cliente) flujoDatosEntrada.readObject();
					Opcion_Usu = datos_paquete.getOpcion();
					Loguear = datos_paquete.getUsuario();
					n_Pedido = datos_paquete.getN_pedido();
					mensaje_del_cliente = datos_paquete.getMensaje();
					Contenido_Servidor.append("La opcion marcada es: " + Opcion_Usu + "\n" + "El usaurio que se ha logueado es: " + Loguear + "\n" + "Numero de pedido: " + n_Pedido + "\n" + "Mensaje del cliente : " + mensaje_del_cliente);
					
					miSocket.close();
					if(Opcion_Usu.equals("1")) {
						respuesta_opcion_cliente primera = new respuesta_opcion_cliente(inicio ,Loguear," estamos estudiando su caso", "Le hemos enviado un email con los detalles de la incidencia",datos_paquete);
						
						
					}else if(Opcion_Usu.equals("2")) {
						respuesta_opcion_cliente segunda = new respuesta_opcion_cliente(inicio ,Loguear," .Recogida en casa(Sin coste),180 días para devolución", " Para conocer nuestra politica de devoluciones : http://www.DevilShepp/Devoluciones.com",datos_paquete);
					}else if(Opcion_Usu.equals("3")) {
						respuesta_opcion_cliente tercera = new respuesta_opcion_cliente(inicio ,Loguear," .Tiendas físicas en : Avilés, Gijón, Oviedo ", "Para más información visite nuestra web: http://www.DevilShepp/Contacto.com",datos_paquete);
					}else if(Opcion_Usu.equals("4")) {
						respuesta_opcion_cliente cuarta= new respuesta_opcion_cliente(inicio ,Loguear,"  ", " ",datos_paquete);
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	class respuesta_opcion_cliente{
		public respuesta_opcion_cliente(Socket_Inicio Inicio, String Loguear,String mensaje, String mensaje2,Informacion_Cliente datos_paquete) throws InterruptedException {
			
			mensaje = mensaje;
			mensaje2 = mensaje2;
			datos_paquete = datos_paquete;
			Color Color_Tipo = new Color(255, 175, 175);
			String nombre = Loguear;
			String nom =nombre.toUpperCase();
			String mensaje3 = "\n" + "\n" + "Estimado cliente :  " + nom + mensaje;
			String mensaje4  = "\n" + mensaje2;
			Inicio = new Socket_Inicio(IP, 9998,mensaje3);
			Thread.sleep(2000);
			Inicio = new Socket_Inicio(IP, 9998,mensaje4);
			if(datos_paquete.getOpcion().equals("4")) {
				String mensaje5 = JOptionPane.showInputDialog("Que quieres decir al cliente:");
				Inicio = new Socket_Inicio(IP, 9998,mensaje5);
			}
			
			
		}
	}

	private JTextArea Contenido_Servidor;
	private JLabel Texto_Servidor,Texto_Servidor1 ;
	private String IP = "192.168.0.7";
	private Socket_Inicio inicio;
	//private String mensaje,mensaje2;
}