import java.net.*;
import java.io.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;


public class Server {
	public static final int PORT = 23959;
	public static final String HOST = "10.246.206.5";
	public static void main(String[] args) throws IOException {

		ServerSocket server = null;

		try {
			server = new ServerSocket(PORT);
			System.out.println("Server is online");

			ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
			Socket sock = new Socket(HOST,PORT);
			ClientHandler handlerProcess = new ClientHandler(sock);
			executor.execute(handlerProcess);

			// while(true) {
				// ClientHandler handlerProcess = new ClientHandler(server.accept());
				// executor.execute(handlerProcess);
			// }
		} catch (IOException ioe) {
			System.out.println("Error hosting on port " + PORT + " " + ioe.toString());
		} finally {
			server.close();
		}

	}
	private static class ClientHandler implements Runnable {
		private Socket socket;
		private String ip;
		private Date date = new Date();
		InputStream in ;
		// BufferedReader in ;
		PrintWriter out ;
		private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");

		ClientHandler(Socket socket) {
			this.socket = socket;
			ip = socket.getRemoteSocketAddress().toString();
			if (ip.charAt(0) == '/') {
				ip = ip.substring(1);
			}
			System.out.println("New connection from: " + ip + " at " + dateFormat.format(date));
		}
		public void sendPOSConnectedMessage(){
			out.print("a");//messageID
			
			String message = 
				"0" + //image buffer 
				"1" + //LaneID String Length 
				"1" + //LaneID 
				"1" + //Software Version string length
				"1"  //Software version string
				;
			out.print(""+message.length());
			out.print(message);
			out.flush();
		}

		public int pullLength(byte[] array){
			dumpArray(array);
			int temp = 0;
			if(array.length == 4){
				temp = (array[0]<<24)&0xff000000|
						(array[1]<<16)&0x00ff0000|
						(array[2]<< 8)&0x0000ff00|
						(array[3]<< 0)&0x000000ff;
			} 
			else if (array.length == 3){
				temp =  (array[0]<<16)&0x00ff0000|
						(array[1]<< 8)&0x0000ff00|
						(array[2]<< 0)&0x000000ff;
			} 
			else if (array.length == 2){
				temp =  (array[0]<< 8)&0x0000ff00|
						(array[1]<< 0)&0x000000ff;
			} 
			else if (array.length == 2){
				temp = (array[0]<< 0)&0x000000ff;
			}

			System.out.println("byte length: " + array.length);
			System.out.println("decimal value: " + temp);
			return temp;
		}

		public void dumpArray(byte[] array){
			// System.out.print("Dumping array :");
			// for (byte a : array){
			// 	System.out.print(a);
			// }
			// System.out.println();
			System.out.println("Array: " +new String(array)); 
		}

		public void parseA() throws IOException{
			System.out.println("Parsing A");
		}
		public void parseB() throws IOException{
			System.out.println("Parsing B");
		}
		public void parseC() throws IOException{
			System.out.println("Parsing C ");

			byte [] length = new byte[4];
			in.read(length);
			int number = pullLength(length);

			byte[] majorVersion = new byte[1];
			byte[] minorVersion = new byte[1];
			byte[] currentState = new byte[1];
			byte[] connectDataLength = new byte[2];

			in.read(majorVersion);
			in.read(minorVersion);
			in.read(currentState);
			in.read(connectDataLength);

			int major = pullLength(majorVersion);
			int minor = pullLength(minorVersion);
			int state = pullLength(currentState);
			int dataLength = pullLength(connectDataLength);

			byte [] message = new byte[dataLength];
			in.read(message);
			System.out.println("message: " + new String(message));
		}
		public void parseD() throws IOException{
			System.out.println("Parsing D");
		}
		public void parseE() throws IOException{
			System.out.println("Parsing E");	
		}
		public void parseF() throws IOException{
			System.out.println("Parsing F");
		}
		public void parseG() throws IOException{
			System.out.println("Parsing G");
		}
		public void parseX() throws IOException{
			System.out.println("Parsing X");
		}


		@Override
		public void run() {

			try 
			{
				// is = new InputStreamReader(socket.getInputStream());
				// in = new BufferedReader(is);
				in = socket.getInputStream();
				out = new PrintWriter(socket.getOutputStream(), true);

				System.out.println("bound readers");
				sendPOSConnectedMessage();

				while(true){
					byte [] id = new byte[1];
					in.read(id);
					switch(id[0]){
						case 'A':
						parseA();
						break;
						case 'B':
						parseB();
						break;
						case 'C':
						parseC();
						break;
						case 'D':
						parseD();
						break;
						case 'E':
						parseE();
						break;
						case 'F':
						parseF();
						break;
						case 'G':
						parseG();
						break;
						case 'X':
						parseG();
						break;
						default:
						System.out.println("message ID: " + new String(id) + " has not beem implemented");
					}


				}
				// System.out.println("Reading");
				// while(true){
				// 	char [] id = new char[1];
				// 	in.read(id);
				// 	System.out.print(new String(id));
				// }

				// while((message = in.readLine()) != null){
				// 	if(!message.startsWith("Register")){
				// 		break;
				// 	}

				// 	System.out.println("New connection from: " + ip + " at " + dateFormat.format(date));
				// 	System.out.println("Received " + message);
				// 	JOptionPane.showMessageDialog(null, "Alert needed at " + message);
				// 	out.println("OKAY");
				// }


			} catch (NullPointerException ex) {
				System.out.println("An error has happened with ip "+ ip + " " + ex.toString());
			}
			catch (IOException e){
				System.out.println("An error has happened with ip "+ ip + " ");
				e.printStackTrace();
			}
			finally{
				// try{
				// 	// is.close();
				// 	in.close();
				// }
				// catch(IOException ioe){
				// 	System.out.println("Could not close io readers " +ioe.toString());
				// }
				// out.close();
			}
		}
	}
}  