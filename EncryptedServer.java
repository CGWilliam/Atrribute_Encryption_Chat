import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;



/* Author CGWilliam
 * 1/22/17
 * 
 * The Server Program for the encrypted client message program
 * This portion of the program must be active to allow all Clients within
 * a local network to connect to one another. 
 * 
 */



// The initialization of the variables
public class EncryptedServer{
	private static final int Port = 5255;
	private static HashSet<String> users = new HashSet<String>();
	private static HashSet<PrintWriter> broadcasts = new HashSet<PrintWriter>();

	// Main method, and the 
	public static void main(String[] args) throws Exception {
		System.out.println("Server Started");
		ServerSocket messagein = new ServerSocket(Port);

		try {
			while (true){
				new Handler(messagein.accept()).start();
			}
		} finally {
			messagein.close();
		}
	}
	
	// The method which spawns off threads for each user
	public static class Handler extends Thread {
		private String user;
		private Socket socket;
		private BufferedReader input; 
		private PrintWriter output;
		
		public Handler(Socket socket) {
            this.socket = socket;
        }
		
		// The running method that handles all user messages and input
		public void run() {
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);				
				
				while (true){
					String messageinput = input.readLine();
					if (messageinput == null){
						return;
					}
					System.out.println("Server Message In: " + messageinput);
					// The method that handles the input of user names and ensures no duplicate names
					if (messageinput.startsWith("ADDNAME ")){
						user = messageinput.substring(8);
						  if (!users.contains(user)) {
	                            users.add(user);
	                            break;
	                        }
					}
					
				}
				output.println("!ACCEPT");
				broadcasts.add(output);
								
				// Further handling of all of the users input 
				while(true){
					String messageinput = input.readLine();
					System.out.println(messageinput);
					if(messageinput == null)
						return;
					
					for (PrintWriter broadcast : broadcasts){
						// Handling encrypted messages
						if(messageinput.startsWith("!GWORK")){
							broadcast.println(messageinput);
						}
						if(messageinput.startsWith("!GFAMILY")){
							broadcast.println(messageinput);
						}
						if(messageinput.startsWith("!GSPORTS")){
							broadcast.println(messageinput);
						}
						if(messageinput.startsWith("!GFRIENDS")){
							broadcast.println(messageinput);
						}
						if(messageinput.startsWith("!ENCRYPT")){
							broadcast.println(messageinput);
						}
						if(messageinput.startsWith("!OR")){
							broadcast.println(messageinput);
						}
						
						// Handle group messages,
						else if(messageinput.startsWith("!GROUP ")){
							broadcast.println(messageinput);
						}
						
						else if(messageinput.startsWith("!RGROUP ")){
							broadcast.println(messageinput);
							
						}						
						else if(messageinput.startsWith("!ENCRYPT !GROUP")){
							broadcast.println(messageinput);
						}
						
						else if(messageinput.startsWith(" ")){
							System.out.println("Blank Message");
						}
						// Handling unencrypted messages 
						else if(!messageinput.startsWith("!")){
							broadcast.println("IN " + user + ": " + messageinput);
						}
						
						for (String user : users){
							broadcast.println("!USERS" + user);
						}
					}			
			}
				
				
			// IOException catch method 
			} catch (IOException e){
				System.out.println(e);
				
			// removing of the names should a user disconnect from the server
			} finally {

				if (user != null){
					users.remove(user);
					for(PrintWriter broadcast : broadcasts){
						broadcast.println("!RUSERS" + user);
					}
				}
				if (output != null){
					broadcasts.remove(output);
				}
				try {
					socket.close();
				} catch (IOException e){
				}
			}		
		}	
		
	}	
}