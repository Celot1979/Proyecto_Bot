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
		centro.add(Contenido_Servidor);
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
			Socket_Inicio inicio = new Socket_Inicio(IP, 9995,"");
			
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
				
				if(mensaje.length()> 0) {
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
				String Opcion_Usu, Loguear, n_Pedido;
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
					Contenido_Servidor.append("La opcion marcada es: " + Opcion_Usu + "\n" + "El usaurio que se ha logueado es: " + Loguear + "\n" + "Numero de pedido: " + n_Pedido);
					
					miSocket.close();
					if(Opcion_Usu.equals("1")) {
						String mensaje = "\n" + "\n" + "Bien. " + Loguear + " estamos estudiando tu caso";
						String mensaje2 = "\n" + "Le hemos enviado un email con los detalles de la incidencia";
						Socket_Inicio inicio2 = new Socket_Inicio(IP, 9995,mensaje);
						Thread.sleep(2000);
						Socket_Inicio inicio3 = new Socket_Inicio(IP, 9995,mensaje2);
						
						
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

	private JTextArea Contenido_Servidor;
	private JLabel Texto_Servidor,Texto_Servidor1 ;
	private String IP = "192.168.0.6";
	

	
}