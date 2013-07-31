package finalproject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends JFrame {
	
//properties
	//a JPanel for JTextArea, pTop, and one for JTextField, pBot
	JPanel pTop = new JPanel();
	JPanel pBot = new JPanel();
	
	//JTextArea to display text with black border
	JTextArea display = new JTextArea(10, 46);
	Border border = BorderFactory.createLineBorder(Color.gray);
		
	//JTextField to type text
	JTextField jtf = new JTextField(40);
		
	//send button
	JButton bSend = new JButton("Send");
	
	//Socket, PrintStream, BufferedReader for global use
	Socket socket;
	PrintStream ps;
	BufferedReader br;

	//constructor
	public ChatServer(){
		
		//finish defining GUI components
		display.setEditable(false);
		display.setBorder(border);
		jtf.setBorder(border);
				
		//add GUI components to panels
		pTop.add(display);
		pBot.add(jtf);
		pBot.add(bSend);
			
		//add panels to JFrame, position
		add(pTop, BorderLayout.NORTH);
		add(pBot, BorderLayout.SOUTH);
		
		//assign listener to send button
		bSend.addActionListener(new ButtonListener());
			
		//SET UP SERVER SOCKET AND STREAMS
		try{			
			//create ServerSocket
			ServerSocket serverSocket = new ServerSocket(8000);
				
			// Listen for a connection request
			socket = serverSocket.accept();
				
			//create input, output streams
			InputStreamReader input = new InputStreamReader(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
					
			//create BufferedReader for input, PrintStream for output
			br = new BufferedReader(input);
			ps = new PrintStream(output);
			
			//input Thread receives strings and prints to display
			Thread inputThread = new Thread(new DisplayLoop());
			inputThread.start();
									
		}catch(Exception e){System.out.println(e);}

	} //end constructor
	
	
	//listener defines send button actions
	public class ButtonListener implements ActionListener{		
		@Override
		public void actionPerformed(ActionEvent ae){
			ps.println(jtf.getText());
			jtf.setText("");
		}			
	} // end nested class ButtonListener
	
	
	//display loop thread as nested class
	public class DisplayLoop implements Runnable {
		//loop keeps waiting for next line to append to jtf
		@Override
		public void run(){
			while(true){
				try{
					display.append(br.readLine() + "\n");
				}catch(Exception e){System.out.println(e);}
			}
		} //end run()
	} //end DisplayLoop nested class
	
	
	//main method draws JFrame
		public static void main(String[] args) {
			ChatServer frame = new ChatServer();
			frame.pack();
			frame.setTitle("Chat Server");
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setVisible(true);
		}//end main
	
} // end class ChatServer