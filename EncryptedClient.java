import java.awt.Dimension;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JList;



/* Author CGWilliam
 * 1/22/2017
 * 
 * The Client Program for the encrypted client message program
 * 
 */


// The initialization of all variables
public class EncryptedClient extends JFrame implements ActionListener{
	JFrame frame = new JFrame("Encrypted Client");
    JTextField IPinput, Userinput, In_Encrypted, Usergroupinput;
    JTextField chatouttext = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    JButton Submit, Save, Refresh;
    JCheckBox Family, Work, Sports, Friends, GroupOR;

    PrintWriter output;
    BufferedReader input;
    private JLabel lblIpAddress, lblUsername, lblChatArea, lblEncryptionKey, lblUserGroup, lblUserInWorkGroup, lblUserInFamilyGroup, lblUserInSportsGroup, lblUserInFriendsGroup, lblGroup;
    static String IPAdress, Username, Groupin;
    static String Password = null;
    
    public ArrayList<String> groupdata = new ArrayList<String>();
    public ArrayList<String> workgroup = new ArrayList<String>();
    public ArrayList<String> familygroup = new ArrayList<String>();
    public ArrayList<String> sportsgroup = new ArrayList<String>();
    public ArrayList<String> friendsgroup = new ArrayList<String>();
    
	static Cipher cipher;
	static boolean nameset = false;
	static boolean passet = false;
	
	JFileChooser fileChooser = new JFileChooser();
	static FileInputStream fileInputStream=null;
	
	//static String ency = "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";
	
	// The building of the GUI 	
    @SuppressWarnings("unchecked")
	public EncryptedClient(){
    	chatouttext.setEditable(false);
        messageArea.setEditable(false);

        lblChatArea = new JLabel("Chat Area:");
        lblChatArea.setBounds(137, 11, 85, 14);
        getContentPane().add(lblChatArea);

    	// IP address label and entry box 
    	lblIpAddress = new JLabel("IP Address");
    	lblIpAddress.setBounds(20, 153, 75, 14);
    	getContentPane().add(lblIpAddress); 
    		
        getContentPane().setLayout(null);
        IPinput = new JTextField(5);
        IPinput.setBounds(20,180,100,20);
        getContentPane().add(IPinput);
        
        // Username label and entry box   
        lblUsername = new JLabel("Username");
        lblUsername.setBounds(20, 213, 75, 14);
        getContentPane().add(lblUsername);
            
        Userinput = new JTextField(5);
        Userinput.setBounds(20, 240, 100, 20);
        getContentPane().add(Userinput);
        
        // Encryption Text label and entry box
        In_Encrypted = new JTextField(5);
        In_Encrypted.setBounds(20, 120, 100, 20);
        getContentPane().add(In_Encrypted);
        
        lblEncryptionKey = new JLabel("Encryption Key");
        lblEncryptionKey.setBounds(20, 91, 100, 16);
        getContentPane().add(lblEncryptionKey);
        
        // Labels for the user groups
        lblUserGroup = new JLabel("All Users");
        lblUserGroup.setBounds(20, 360, 75, 14);
        getContentPane().add(lblUserGroup);
        
        lblUserInWorkGroup = new JLabel("Work Group");
        lblUserInWorkGroup.setBounds(140, 360, 75, 14);
        getContentPane().add(lblUserInWorkGroup);
        
        lblUserInFamilyGroup = new JLabel("Family Group");
        lblUserInFamilyGroup.setBounds(260, 360, 75, 14);
        getContentPane().add(lblUserInFamilyGroup);
        
        lblUserInSportsGroup = new JLabel("Sports Group");
        lblUserInSportsGroup.setBounds(380, 360, 75, 14);
        getContentPane().add(lblUserInSportsGroup);
        
        lblUserInFriendsGroup = new JLabel("Friends Group");
        lblUserInFriendsGroup.setBounds(500, 360, 100, 14);
        getContentPane().add(lblUserInFriendsGroup);        
        // The data collection action    
        Submit = new JButton("Submit");
        Submit.setBounds(20,273,100,25);
        Submit.addActionListener(this);
        getContentPane().add(Submit);
        
        Save = new JButton("KeyFile");
        Save.setBounds(20,44,100,25);
        Save.addActionListener(this);
        getContentPane().add(Save);
        
        // Group Check boxes
        JLabel lblGroup = new JLabel("Groups to join:");
        lblGroup.setBounds(175, 275, 100, 25);
        getContentPane().add(lblGroup);

        Family = new JCheckBox("Family");
        Family.setBounds(275,300,100,25);
        Family.addActionListener(this);
        getContentPane().add(Family);
        
        Work = new JCheckBox("Work");
        Work.setBounds(275,275,100,25);
        Work.addActionListener(this);
        getContentPane().add(Work);
        
        Sports = new JCheckBox("Sports");
        Sports.setBounds(375,275,100,25);
        Sports.addActionListener(this);
        getContentPane().add(Sports);
        
        Friends = new JCheckBox("Friends");
        Friends.setBounds(375,300,100,25);
        Friends.addActionListener(this);
        getContentPane().add(Friends);
        
        GroupOR = new JCheckBox("GroupOR");
        GroupOR.setBounds(475,320,100,25);
        GroupOR.addActionListener(this);
        getContentPane().add(GroupOR);
        
        chatouttext.setBounds(130, 46, 453, 20);
        getContentPane().add(chatouttext);
        getContentPane().add(new JScrollPane(messageArea)).setBounds(130, 76, 483, 193);   
        chatouttext.addActionListener(this); 
      
    } 
    // The main action listener for the events that are executed in the GUI 
    
    public void actionPerformed(ActionEvent e) { 
    	// Actions to perform when the submit button is pressed
		// System.out.println("Message Construction Start: " + messageconstructionstart);   	
           if(e.getSource()== Submit){
               IPAdress = (IPinput.getText());
               Username = (Userinput.getText());
               if (passet == false){
            	   Password = (In_Encrypted.getText());
            	   if(Password.isEmpty()){
                	   Password = null;
                   }
               }   
               if (nameset == false){
               	output.println("ADDNAME " + Username);
               	nameset = true;
               }   
            }
           
           // Actions to perform when the save button is pressed
           if (e.getSource() == Save){
        	   createFileChooser(frame);
        	   In_Encrypted.setEditable(false);
           }

    	   if (Password == null && (!Work.isSelected() && !Family.isSelected() && !Sports.isSelected() & !Friends.isSelected())){  
        	   output.println(chatouttext.getText());
           }
    	   else if ((Work.isSelected() || Family.isSelected() || Sports.isSelected() || Friends.isSelected()) && Password != null && GroupOR.isSelected()){
    		   String reply = "";     
    		   
    		   if (Work.isSelected()){
        		   reply = "!GWORK";   
        	   }
    		   if (Family.isSelected()){
        		   reply = reply + "!GFAMILY";
        	   }    	   
    		   if (Sports.isSelected()){
        		   reply = reply + "!GSPORTS";
        	   }       	   
    		   if (Friends.isSelected()){
        		   reply = reply + "!GFRIENDS";
        	   }
    		   try {
					byte[] key;
					key = (Password).getBytes("UTF-8");
					MessageDigest sha = MessageDigest.getInstance("SHA-1");
					key = Arrays.copyOf(key,16);		
					SecretKeySpec secretKeySpec = new SecretKeySpec (key, "AES");
					cipher = Cipher.getInstance("AES");
					String encryptedText = encrypt(chatouttext.getText(), secretKeySpec);
					output.println("!OR!ENCRYPT!" + reply + (Username) + ": " + encryptedText);
					//String encryptedText = encrypt(reply + (Username) + ": " + chatouttext.getText(), secretKeySpec);
					//output.println("!OR!ENCRYPT!" + encryptedText);
			} catch (Exception e1) {
			}
    	   }
    	   
           else if ((Work.isSelected() || Family.isSelected() || Sports.isSelected() || Friends.isSelected()) && GroupOR.isSelected()){
    		   String reply = ""; 
    		   
    		   if (Work.isSelected()){
        		   reply = "!GWORK";   
        	   }
    		   if (Family.isSelected()){
        		   reply = reply + "!GFAMILY";
        	   }    	   
    		   if (Sports.isSelected()){
        		   reply = reply + "!GSPORTS";
        	   }       	   
    		   if (Friends.isSelected()){
        		   reply = reply + "!GFRIENDS";
        	   }
    		   output.println("!OR" + reply + Username + ": " + chatouttext.getText() + "\n");
    	   }   	   

           // Encryption of all outgoing messages based on the password entered by the user
           else if (Password != null && (!Work.isSelected() && !Family.isSelected() && !Sports.isSelected() && !Friends.isSelected())){	
        	   long messageconstructionstart = System.nanoTime();
        	   try {
		  					byte[] key;
		  					key = (Password).getBytes("UTF-8");
		  					MessageDigest sha = MessageDigest.getInstance("SHA-1");
		  					key = Arrays.copyOf(key,16);		
		  					SecretKeySpec secretKeySpec = new SecretKeySpec (key, "AES");
		  					cipher = Cipher.getInstance("AES");
		  					String encryptedText = encrypt(chatouttext.getText(), secretKeySpec);
		  					//String encryptedText = encrypt((ency), secretKeySpec);
		  					output.println("!ENCRYPT" + (Username) + ": " + encryptedText);
		  			} catch (Exception e1) {
		  		}
        	   double totalconstruct =(System.nanoTime() - messageconstructionstart);   
               //System.out.println((totalconstruct));
           }	   
           else if ((Work.isSelected() || Family.isSelected() || Sports.isSelected() || Friends.isSelected()) && Password != null){
        	   String reply = "";
    		   if (Work.isSelected()){
        		   reply = "!GWORK";   
        	   }
    		   if (Family.isSelected()){
        		   reply = reply + "!GFAMILY";
        	   }    	   
    		   if (Sports.isSelected()){
        		   reply = reply + "!GSPORTS";
        	   }       	   
    		   if (Friends.isSelected()){
        		   reply = reply + "!GFRIENDS";
        	   }
    		    
    		   //System.out.println(reply);
    		   
    		   try {
					byte[] key;
					key = (Password).getBytes("UTF-8");
					MessageDigest sha = MessageDigest.getInstance("SHA-1");
					key = Arrays.copyOf(key,16);		
					SecretKeySpec secretKeySpec = new SecretKeySpec (key, "AES");
					cipher = Cipher.getInstance("AES");
					String encryptedText = encrypt(chatouttext.getText(), secretKeySpec);
					//String encryptedText = encrypt(ency, secretKeySpec); 
					output.println("!ENCRYPT!" + reply + (Username) + ": " + encryptedText);	
					//String encryptedText = encrypt(reply + (Username) + ": " + chatouttext.getText(), secretKeySpec);
					//output.println("!ENCRYPT!" + encryptedText);
					
			} catch (Exception e1) {
			}
    	   }
    	   
           else if ((Work.isSelected() || Family.isSelected() || Sports.isSelected() || Friends.isSelected())){
    		   String reply = "";
    		   
    		   if (Work.isSelected()){
        		   reply = "!GWORK";   
        	   }
    		   if (Family.isSelected()){
        		   reply = reply + "!GFAMILY";
        	   }    	   
    		   if (Sports.isSelected()){
        		   reply = reply + "!GSPORTS";
        	   }       	   
    		   if (Friends.isSelected()){
        		   reply = reply + "!GFRIENDS";
        	   }
    		   output.println(reply + Username + ": " + chatouttext.getText() + "\n");
    	   }          
           chatouttext.setText(""); 
           }
    
	@SuppressWarnings("resource")
	public void run() throws Exception {
    		// the processing of all incoming messages to the client 
			 Socket socket = new Socket(IPAdress, 5255);
			 input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 output = new PrintWriter(socket.getOutputStream(), true); 
			 int count = 0;
			 Users();
	    	 Refresh.doClick();
			 // process if the message is encrypted or not
			 while (true){
				long messagedecryptionstart = 0;
				String messagein = input.readLine();
				boolean nb = true;	
				//System.out.println(messagein);
		    	//System.out.println("Message Decryption Start: " + messagedecryptionstart);

				if(messagein.endsWith(":") || (messagein.endsWith(": "))){
					if(messagein.startsWith("!GROUP") && nb == true){
						GroupMessage(messagein);
					}
					if(messagein.startsWith("!RGROUP") && nb == true){
						RemoveUser(messagein);
					} 
					nb = false;
				}
				else if (messagein.startsWith("!OR")){
					System.out.println("Went to OR-----" + messagein);
					messagein = messagein.substring(3);
					if (messagein.startsWith("!ENCRYPT") && nb == true){
						ArrayList<String> tempusergroup = new ArrayList<String>();
						//System.out.println(Arrays.toString(workgroup.toArray()));
						if(workgroup.contains(Username))
							tempusergroup.add("WORK");
						if(familygroup.contains(Username))
							tempusergroup.add("FAMILY");
						if(sportsgroup.contains(Username))
							tempusergroup.add("SPORTS");
						if(friendsgroup.contains(Username))
							tempusergroup.add("FRIENDS");
						OREncryptedMessage(messagein, nb, tempusergroup);
						nb = false;
						Users();
				    	Refresh.doClick();
					}
					else{
						ORGroupMessageIn(messagein, workgroup, familygroup, sportsgroup, friendsgroup);
						Users();
						Refresh.doClick();
					}
					
				}
				else if (messagein.startsWith("!ENCRYPT") && nb == true){	
					messagedecryptionstart = System.nanoTime();
					ArrayList<String> tempusergroup = new ArrayList<String>();
					//System.out.println(Arrays.toString(workgroup.toArray()));
					if(workgroup.contains(Username))
						tempusergroup.add("WORK");
					if(familygroup.contains(Username))
						tempusergroup.add("FAMILY");
					if(sportsgroup.contains(Username))
						tempusergroup.add("SPORTS");
					if(friendsgroup.contains(Username))
						tempusergroup.add("FRIENDS");
		
					EncryptedMessage(messagein, nb, tempusergroup);
					nb = false;
					Users();
			    	Refresh.doClick();
				}	
				else if(messagein.startsWith("!ACCEPT") && nb == true){
					chatouttext.setEditable(true);
					Users();
			    	Refresh.doClick();
				}
				
				else if (messagein.startsWith("!USERS") && nb == true){
					if (!groupdata.contains(messagein.substring(6))){
						groupdata.add(messagein.substring(6));
					}
				}
				else if(messagein.startsWith("IN") && nb == true){
					messageArea.append(messagein.substring(3)+ "\n");
					Users();
			    	Refresh.doClick();
				}
				else{				
					GroupMessageIn(messagein, workgroup, familygroup, sportsgroup, friendsgroup);
					Users();
			    	Refresh.doClick();
				}
								
				
				if(messagein.startsWith("!USER") || messagein.startsWith("!GROUP") || messagein.startsWith("!RGROUP") || messagein.startsWith("!ACCEPT") || messagein.endsWith(" ")){
					continue;
			 }
				
				else{
					double elapsedTimeInSec = (System.nanoTime() - messagedecryptionstart) * 1.0e-9;
	            	//count = count + 1;
	            	//System.out.println(elapsedTimeInSec);

				}
			 }	 
		}			
    
    public void GroupMessageIn(String messagein, ArrayList<String> workgroup, ArrayList<String> familygroup, ArrayList<String> sportsgroup, ArrayList<String> friendsgroup){
    	ArrayList<String> intempgroup = new ArrayList<String>();
		ArrayList<String> tempusergroup = new ArrayList<String>();
		
		//System.out.println(Arrays.toString(workgroup.toArray()));
		if(workgroup.contains(Username))
			tempusergroup.add("WORK");
		if(familygroup.contains(Username))
			tempusergroup.add("FAMILY");
		if(sportsgroup.contains(Username))
			tempusergroup.add("SPORTS");
		if(friendsgroup.contains(Username))
			tempusergroup.add("FRIENDS");
		
		////Test Data
		 //int testnumber =50;
		 //for (int i = 0; i < testnumber; i++){
			// tempusergroup.add(generateRandom(7));
		 //}

		
		String[] groups = (messagein.substring(1)).split("!");
		String message= "";
		for (int i=0; i <groups.length; i++){
			String group = groups[i];
			if(group.startsWith("GWORK")){
				message = (message + "WORK-");
				intempgroup.add("WORK");
			}
			if(group.startsWith("GFAMILY")){
				message = (message + "FAMILY-");
				intempgroup.add("FAMILY");
			}
			if(group.startsWith("GSPORTS")){
				message = (message + "SPORTS-");
				intempgroup.add("SPORTS");
			}
			if(group.startsWith("GFRIENDS")){
				message = (message + "FRIENDS-");
				intempgroup.add("FRIENDS");
			}
		}				
		messagein = messagein.replace("!GWORK","").replace("!GFAMILY","").replace("!GSPORTS","").replace("!GFRIENDS","");
		System.out.println("The Current Groups " + Username.toString() + "is in: " + Arrays.toString(tempusergroup.toArray()));
		System.out.println("The Current Groups the incoming message is from: " + Arrays.toString(intempgroup.toArray()));
		if(tempusergroup.containsAll(intempgroup) && intempgroup.containsAll(tempusergroup))
			messageArea.append(message + messagein + "\n");	 
    }
    
    public void ORGroupMessageIn(String messagein, ArrayList<String> workgroup, ArrayList<String> familygroup, ArrayList<String> sportsgroup, ArrayList<String> friendsgroup){
    	ArrayList<String> intempgroup = new ArrayList<String>();
		ArrayList<String> tempusergroup = new ArrayList<String>();
		
		if(workgroup.contains(Username))
			tempusergroup.add("WORK");
		if(familygroup.contains(Username))
			tempusergroup.add("FAMILY");
		if(sportsgroup.contains(Username))
			tempusergroup.add("SPORTS");
		if(friendsgroup.contains(Username))
			tempusergroup.add("FRIENDS");
		
		String[] groups = (messagein.substring(1)).split("!");
		String message= "";
		for (int i=0; i <groups.length; i++){
			String group = groups[i];
			if(group.startsWith("GWORK")){
				message = (message + "WORK-");
				intempgroup.add("WORK");
			}
			if(group.startsWith("GFAMILY")){
				message = (message + "FAMILY-");
				intempgroup.add("FAMILY");
			}
			if(group.startsWith("GSPORTS")){
				message = (message + "SPORTS-");
				intempgroup.add("SPORTS");
			}
			if(group.startsWith("GFRIENDS")){
				message = (message + "FRIENDS-");
				intempgroup.add("FRIENDS");
			}
		}	
		
		System.out.println("The Current Groups " + Username.toString() + "is in: " + Arrays.toString(tempusergroup.toArray()));
		System.out.println("The Current Groups the incoming message is from: " + Arrays.toString(intempgroup.toArray()));
		
		messagein = messagein.replace("!GWORK","").replace("!GFAMILY","").replace("!GSPORTS","").replace("!GFRIENDS","");
		for(String user : tempusergroup){
			for(String group: intempgroup){
				if(user == group){
					messageArea.append("OR-" + message + messagein + "\n");
					break;
				}
				break;
			}
			break;
		}
    }
    
    public void GroupMessage(String messagein){
    	String group = messagein.substring(7);
		if (group.startsWith("Work")){
			String useringroup = group.substring(4,(group.indexOf(":")));
			if (!workgroup.contains(useringroup)) {
				 workgroup.add(useringroup);
			}
		}
		else if (group.startsWith("Family")){
			String useringroup = group.substring(6,(group.indexOf(":")));
			if (!familygroup.contains(useringroup)) {
				 familygroup.add(useringroup);
			}
		}
		else if (group.startsWith("Sports")){
			String useringroup = group.substring(6,(group.indexOf(":")));
			if (!sportsgroup.contains(useringroup)) {
				 sportsgroup.add(useringroup);
			}	
		}
		else if (group.startsWith("Friends")){
			String useringroup = group.substring(7,(group.indexOf(":")));
			if (!friendsgroup.contains(useringroup)) {
				 friendsgroup.add(useringroup);
			}										
	 }
 
    }
    
    public void RemoveUser(String messagein){
    	String group = messagein.substring(8);

		if (group.startsWith("Work")){
			String useringroup = group.replaceFirst("Work", "").replaceFirst(":", "");
			workgroup.remove(useringroup);
		}
		else if (group.startsWith("Family")){
			String useringroup = group.replaceFirst("Family", "").replaceFirst(":", "");
			familygroup.remove(useringroup);				
		}
		else if (group.startsWith("Sports")){
			String useringroup = group.replaceFirst("Sports", "").replaceFirst(":", "");
			sportsgroup.remove(useringroup);			
		}
		else if (group.startsWith("Friends")){
			String useringroup = group.replaceFirst("Friends", "").replaceFirst(":", "");
			friendsgroup.remove(useringroup);				
		}							
    }
    
    public void OREncryptedMessage(String messagein, boolean nb, ArrayList<String> tempusergroup) throws Exception{

    	messagein = messagein.replace("!ENCRYPT!","");
    	messagein = messagein.replace("!ENCRYPT","");
    	System.out.println("The Initial encrypted: " + messagein);
    	boolean answered = false;
    	if((Work.isSelected() || Family.isSelected() || Sports.isSelected() || Friends.isSelected()) && Password != null){	
			ArrayList<String> intempgroup = new ArrayList<String>();
			String[] groups = (messagein.substring(1)).split("!");
			String message= "";
			for (int i=0; i <groups.length; i++){
				String group = groups[i];
				if(group.startsWith("GWORK")){
					message = (message + "WORK-");
					intempgroup.add("WORK");
				}
				if(group.startsWith("GFAMILY")){
					message = (message + "FAMILY-");
					intempgroup.add("FAMILY");
				}
				if(group.startsWith("GSPORTS")){
					message = (message + "SPORTS-");
					intempgroup.add("SPORTS");
				}
				if(group.startsWith("GFRIENDS")){
					message = (message + "FRIENDS-");
					intempgroup.add("FRIENDS");
				}
			}
			
			//System.out.println("The Current Encrypted Groups " + Username.toString() + "is in: " + Arrays.toString(tempusergroup.toArray()));
			//System.out.println("The Current Encrypted Groups the incoming message is from: " + Arrays.toString(intempgroup.toArray()));
	    	
			
			for(String user : tempusergroup){
				for(String group: intempgroup){
					if(user == group){
						try{
							messagein = messagein.replace("!GWORK","").replace("!GFAMILY","").replace("!GSPORTS","").replace("!GFRIENDS","");
							//System.out.println(messagein);
							String messageout[] = messagein.split(":",2);
							String inname = messageout[0];
							//System.out.println(inname);
							KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
							keyGenerator.init(128);
							byte[] key = (Password).getBytes("UTF-8");
							MessageDigest sha = MessageDigest.getInstance("SHA-1");
							key = Arrays.copyOf(key,16);
							SecretKeySpec secretKeySpec = new SecretKeySpec (key, "AES");
							String encrytedText = (messageout[1]).substring(1);
							//System.out.println(encrytedText);
							try{
								String decryptedText = decrypt(encrytedText, secretKeySpec);
								//System.out.println(decryptedText);
								messageArea.append("ENCRYPTED-OR-" + message + inname + ": " +decryptedText + "\n");
							}catch(BadPaddingException e){
								messageArea.append("<Encrypted, Bad Key>" + "\n");
							}
						// IOExcption catch
					}catch(IOException e){
						messageArea.append("<Encrypted, Enter Key>" + "\n");
					}
						break;
					}
					break;
				}
				break;
			}	
    	}
    	else if(Password == null){
			messageArea.append("<Encrypted, Enter Key>");
			System.out.println("Checking for Group Encryption.");
		}		 
    	else if(Password != null && answered == false){
			try{
				String message[] = messagein.split(":",2);
				String inname = message[0];
				//System.out.println(inname);
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(128);
				byte[] key = (Password).getBytes("UTF-8");
				MessageDigest sha = MessageDigest.getInstance("SHA-1");
				key = Arrays.copyOf(key,16);
				SecretKeySpec secretKeySpec = new SecretKeySpec (key, "AES");
				String encrytedText = (message[1]).substring(1);
				//System.out.println(encrytedText);
				try{
					String decryptedText = decrypt(encrytedText, secretKeySpec);
					//System.out.println(decryptedText);
					messageArea.append("ENCRYPTED-" + inname + " : " +decryptedText + "\n");
				}catch(BadPaddingException e){
					messageArea.append(inname + "<Encrypted, Bad Key>" + "\n");
				}
		// IOExcption catch
		}catch(IOException e){
			messageArea.append("<Encrypted, Enter Key>" + "\n");
		 }
		 }
    }
    
    public void EncryptedMessage(String messagein, boolean nb, ArrayList<String> tempusergroup) throws Exception{
    	messagein = messagein.replace("!ENCRYPT!","");
    	messagein = messagein.replace("!ENCRYPT","");
    	System.out.println("The initial encrypted: " + messagein);
    	boolean answered = false;
    	if((Work.isSelected() || Family.isSelected() || Sports.isSelected() || Friends.isSelected()) && Password != null){	
			ArrayList<String> intempgroup = new ArrayList<String>();
			//System.out.println(messagein);
			String[] groups  = (messagein.substring(1)).split("!");
			String message= "";
			for (int i=0; i <groups.length; i++){
				String group = groups[i];
				if(group.startsWith("GWORK")){
					message = (message + "WORK-");
					intempgroup.add("WORK");
				}
				if(group.startsWith("GFAMILY")){
					message = (message + "FAMILY-");
					intempgroup.add("FAMILY");
				}
				if(group.startsWith("GSPORTS")){
					message = (message + "SPORTS-");
					intempgroup.add("SPORTS");
				}
				if(group.startsWith("GFRIENDS")){
					message = (message + "FRIENDS-");
					intempgroup.add("FRIENDS");
				}
				
			//System.out.println("The Current Encrypted Groups " + Username.toString() + "is in: " + Arrays.toString(tempusergroup.toArray()));
			//System.out.println("The Current Encrypted Groups the incoming message is from: " + Arrays.toString(intempgroup.toArray()));
	    	
			if(tempusergroup.containsAll(intempgroup) && intempgroup.containsAll(tempusergroup)){
				try{
					messagein = messagein.replace("!GWORK","").replace("!GFAMILY","").replace("!GSPORTS","").replace("!GFRIENDS","");
					System.out.println(messagein);
					String messageout[] = messagein.split(":",2);
					String inname = messageout[0];
					//System.out.println(inname);
					KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
					keyGenerator.init(128);
					byte[] key = (Password).getBytes("UTF-8");
					MessageDigest sha = MessageDigest.getInstance("SHA-1");
					key = Arrays.copyOf(key,16);
					SecretKeySpec secretKeySpec = new SecretKeySpec (key, "AES");
					String encrytedText = (messageout[1]).substring(1);
					//System.out.println(encrytedText);
					try{
							String decryptedText = decrypt(encrytedText, secretKeySpec);
							System.out.println(decryptedText);
							messageArea.append("ENCRYPTED-" + message + inname + ": " +decryptedText + "\n");
					}catch(BadPaddingException e){
						messageArea.append("<Encrypted, Bad Key>" + "\n");
					}
				// IOExcption catch
			}catch(IOException e){
				messageArea.append("<Encrypted, Enter Key>" + "\n");
			}
				
		}	
			}
    	}
    	
    	else if(Password == null){
			messageArea.append("<Encrypted, Enter Key>");
			System.out.println("Checking for Group Encryption.");
		}		 
    	else if(Password != null && answered == false){
			try{
				String message[] = messagein.split(":",2);
				String inname = message[0];
				//System.out.println(inname);
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(128);
				byte[] key = (Password).getBytes("UTF-8");
				MessageDigest sha = MessageDigest.getInstance("SHA-1");
				key = Arrays.copyOf(key,16);
				SecretKeySpec secretKeySpec = new SecretKeySpec (key, "AES");
				String encrytedText = (message[1]).substring(1);
				//System.out.println(encrytedText);
				try{
					String decryptedText = decrypt(encrytedText, secretKeySpec);
					System.out.println(decryptedText);
					messageArea.append("ENCRYPTED-" + inname + " : " +decryptedText + "\n");
				}catch(BadPaddingException e){
					messageArea.append(inname + "<Encrypted, Bad Key>" + "\n");
				}
		// IOExcption catch
		}catch(IOException e){
			messageArea.append("<Encrypted, Enter Key>" + "\n");
		 }
		 }
    }
    
    public void Users() {  
    	// The refresh button action, for the user groups
		Refresh = new JButton(new AbstractAction("Refresh"){
			public void actionPerformed(ActionEvent e){
					if (Work.isSelected()){
		    		   output.println("!GROUP Work" + (Username) +":");
		    	   }
		    	   if (Family.isSelected()){
		    		   output.println("!GROUP Family" + (Username) +":");
		    	   }    	   
		    	   if (Sports.isSelected()){
		    		   output.println("!GROUP Sports" + (Username) +":");
		    	   }       	   
		    	   if (Friends.isSelected()){
		    		   output.println("!GROUP Friends" + (Username) +":");
		    	   }	   
		    	   if (!Work.isSelected()){
		    		   output.println("!RGROUP Work" + (Username) + ":");
		    	   }
		    	   if (!Family.isSelected()){
		    		   output.println("!RGROUP Family" + (Username) + ":");
		    	   }
		    	   if (!Sports.isSelected()){
		    		   output.println("!RGROUP Sports" + (Username) + ":");
		    	   }
		    	   if (!Friends.isSelected()){
		    		   output.println("!RGROUP Friends" + (Username) + ":");
		    	   }
		    	   
			//Alllist
			JList<String> alllist = new JList<>(groupdata.toArray(new String[0]));
			getContentPane().add(new JScrollPane(alllist)).setBounds(20, 376, 110, 193);
    		
    		//Worklist
    		JList<String> worklist = new JList<>(workgroup.toArray(new String[0]));
			getContentPane().add(new JScrollPane(worklist)).setBounds(140, 376, 110, 193);
    		
    		//Familylist
    		JList<String> familylist = new JList<>(familygroup.toArray(new String[0]));
			getContentPane().add(new JScrollPane(familylist)).setBounds(260, 376, 110, 193);
    		
    		//Sportslist
    		JList<String> sportslist = new JList<>(sportsgroup.toArray(new String[0]));
			getContentPane().add(new JScrollPane(sportslist)).setBounds(380, 376, 110, 193);	
    		
    		//Friendslist
    		JList<String> friendslist = new JList<>(friendsgroup.toArray(new String[0]));
			getContentPane().add(new JScrollPane(friendslist)).setBounds(500, 376, 110, 193);
			
			//System.out.println("Alllist" + (groupdata).toString());
			//System.out.println("Workgroup" + (workgroup).toString());
			//System.out.println("Familygroup" + (familygroup).toString());
			//System.out.println("Sportsgroup" + (sportsgroup).toString());
			//System.out.println("Friendsgroup" + (friendsgroup).toString());
			
			
    		
    		alllist.updateUI();
    		worklist.updateUI();
    		familylist.updateUI();
    		sportslist.updateUI();
    		friendslist.updateUI();
    		
			}
	        });
	    Refresh.setBounds(480, 280, 110, 25);
	    getContentPane().remove(Refresh);
	    getContentPane().add(Refresh);
    }
    
    public static String encrypt(String plainText, SecretKey secretKey)
			throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
		String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}
    
    public static String decrypt(String encryptedText, SecretKey secretKey)
			throws Exception {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedTextByte = decoder.decode(encryptedText);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
    
    private static void createFileChooser(final JFrame frame){
    	String filename = File.separator+"tmp";
    	JFileChooser filechooser = new JFileChooser(new File(filename));
    	filechooser.showOpenDialog(frame);
    	File filein = filechooser.getSelectedFile();
		   byte[] keyfiledata = new byte[(int) filein.length()];
		   try{
			   fileInputStream = new FileInputStream(filein);
			   fileInputStream.read(keyfiledata);
			   fileInputStream.close();
			   
			   String keyfile = new String(keyfiledata, StandardCharsets.UTF_8).substring(0, 100);
			   Password = keyfile;
			   passet = true;
		   }
		   catch(Exception e1){
			   System.out.println("Fatal Error in Keyfile Retreval.");
		   }
    	
    }
  
    public static String generateRandom(int length) {
    	String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new SecureRandom();
        if (length <= 0) {
            throw new IllegalArgumentException("String length must be a positive integer");
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
    
    public static void main (String[] args)throws Exception {
       EncryptedClient Client = new EncryptedClient();
       Client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       Client.setSize(new Dimension(650, 650));
       Client.setVisible(true);
       Client.run();
       Client.Users();
  
  }
}