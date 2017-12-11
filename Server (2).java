import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.*;
// import javax.swing.JOptionPane;
// import javax.swing.JDialog;
// import javax.swing.UIManager;
import java.awt.Color;
// import com.ibm.sureview.util.SureviewUtilities;

public class Server {
	public static final int PORT = 9003;
	public static boolean debug = false;

	public static void main(String[] args) throws IOException {

		if(args.length > 0){
			parseArguments(args);
		}
		
		ServerSocket server = null;
		UIManager UI = new UIManager();
		UI.put("OptionPane.background", Color.red);
		UI.put("Panel.background", Color.yellow);
		displayDialog("Hi","Customer Assistance Needed",(PrintWriter)null);			  
		// try {
		// 	server = new ServerSocket(PORT);
		// 	System.out.println("Server is online");

		// 	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		// 	while(true) {
		// 		ClientHandler handlerProcess = new ClientHandler(server.accept());
		// 		executor.execute(handlerProcess);
		// 	}
		// } catch (IOException ioe) {
		// 	System.out.println("Error hosting on port " + PORT + " " + ioe.toString());
		// } finally {
		// 	server.close();
		// }

	}

	public static void parseArguments(String array[]){
		if((array[0].equals("-d")) || (array[0].equals("-debug"))){
			debug = true;
		}
	}
	public static void displayDialog(final String text, final String title, final PrintWriter pw){
			// EventQueue.invokeLater(new Runnable() {
			// @Override
			// public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException ex){

				} catch (InstantiationException ex){

				} catch (IllegalAccessException ex){

				} catch (UnsupportedLookAndFeelException ex){

				}   

				final JDialog dialog = new JDialog((Frame)null, title);

				//JOptionPane op = new JOptionPane(text, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
				JLabel label = new JLabel("<html>"+ text +"</html>", JLabel.CENTER);
				Font font = new Font("Arial", Font.BOLD,18);
				label.setFont(font);
				JOptionPane op = new JOptionPane(label);

				op.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String name = evt.getPropertyName();
						if ("value".equals(name)) {
							dialog.dispose();

						}
					}
				});
				
				op.setPreferredSize(new Dimension(1000,1000));
				dialog.setUndecorated(true);
				dialog.setLayout(new BorderLayout());
				//dialog.setSize(550,300);
				//dialog.setPreferredSize(500,500);
				dialog.setAlwaysOnTop(true);
				dialog.add(op);
				dialog.addWindowListener(new WindowListener (){
					@Override
					public void windowClosed(WindowEvent e) {
						if(pw!=null)
				    	    pw.println("OKAY");
				    }
				    public void windowDeactivated(WindowEvent e) {
				        //pw.println("OKAY");
				    }
				    public void windowActivated(WindowEvent e) {
				        //pw.println("OKAY");
				    }
				    public void windowDeiconified(WindowEvent e) {
				        //pw.println("OKAY");
				    }

				    public void windowIconified(WindowEvent e) {
				        //pw.println("OKAY");
				    }

				    public void windowClosing(WindowEvent e) {
				        //pw.println("OKAY");
				    }

				    public void windowOpened(WindowEvent e) {
				        //pw.println("OKAY");
				    }
				});
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			// }
			// });
		}
	private static class ClientHandler implements Runnable {
		private Socket socket;
		private String ip;
		private Date date = new Date();
		private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");

		ClientHandler(Socket socket) {
			this.socket = socket;
			ip = socket.getRemoteSocketAddress().toString();
			if (ip.charAt(0) == '/') {
				ip = ip.substring(1);
			}
			System.out.println("New connection from: " + ip + " at " + dateFormat.format(date));
		}
		
		
		@Override
		public void run() {
			InputStreamReader is = null;
			BufferedReader in = null;
			PrintWriter out = null;
			try 
			{
				is = new InputStreamReader(socket.getInputStream());
				in = new BufferedReader(is);
				out = new PrintWriter(socket.getOutputStream(), true);
				
				String message;
				while((message = in.readLine()) != null){
					if(!message.startsWith("Register")){
						break;
					}
					System.out.println("Received " + message);
					if (!debug){
						displayDialog(message,"Customer Assistance Needed",out);			  
					// DOESN'T WORK
					}
					// JOptionPane.showMessageDialog(null, "Audit needed at Register" + message);
					
				}


			} catch (NullPointerException ex) {
				System.out.println("An error has happened with ip "+ ip + " " + ex.toString());
			}
			catch (IOException e){
				System.out.println("An error has happened with ip "+ ip + " " + e.toString());
			}
			finally{
				try{
					is.close();
					in.close();
				}
				catch(IOException ioe){
					System.out.println("Could not close io readers " +ioe.toString());
				}
				out.close();
			}
		}
	}
}   