package client;
import java.io.*;
import java.net.*;

import client.Board.Status;

/**
 *the class responsible for server communication
 * @author tomer
 */
public class Communication implements Runnable {
	/**
	 * the socket of the server
	 */
	Socket socket;
	/**
	 * the field for writing to server 
	 */
	BufferedWriter out;
	Thread read;
	Board board;
	/**
	 * the constructor of the Client
	 * initiate all fields and starts communicating with server 
	 * @param board- the main frame and all the graphics
	 */
	public Communication(Board board){
		this.board=board;
		try{ 
			socket=new Socket("192.168.1.115",12345); 
			out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			read=new Thread(this);
			read.start();
		}
		catch(IOException e){
			System.out.println("failed to connect");
			System.exit(1);
		}
	}


	/**
	 * the function that sends the data to server
	 * @param line- the string to send to the server
	 */
	public void send(String line){
		try{
			out.write(line + "\n");
			out.flush();

		}catch (IOException e){
			System.err.println("Couldn't read or write");
			System.exit(1);
		}

	}


	@Override
	public void run() {
		try{
			BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream()));
			String serverString;

			while ((serverString=in.readLine())!=null)
			{
				if(serverString.startsWith("bye")){
					send("bye");
					System.exit(1);
					return;
				}
				if(serverString.startsWith("You")){
					board.frame.setTitle(serverString);
					board.endGame(serverString);
				}
				if(serverString.startsWith("Wait for start")) {
					String mar[]=serverString.split("##");
					board.id=Integer.parseInt(mar[1]);
					board.frame.setTitle("Waiting for start of the game");
				}
				//changes the info bar to show the type pressed
				if(serverString.startsWith("Mark_org")){
					board.info.updateMarkOrg(serverString);
				}
				//analyzes the initial data coming from the server
				if(serverString.startsWith("Player")) {
					board.analizeData(serverString);
					board.status=Status.play;
				}
				if(serverString.startsWith("flag")){
					board.status=Status.flag;
					board.repaint();
				}
				//sends the data about the tool placed- index and location
				if(serverString.startsWith("Org")){
					board.analizeOrg(serverString);
					board.status= Status.org;
				}
				//changes the info bar to show the types of the tools who fought
				if(serverString.startsWith("fight")){
					board.info.updateFight(serverString);
				}
				//marks the tool in the index in the array 
				if(serverString.startsWith("mark")){
					board.main.updateMark(serverString);
				}
				//changes turn
				if(serverString.startsWith("turn")){
					board.updateTurn(serverString);
				}
				//adds to the grave yard one of given type
				if(serverString.startsWith("kill")){
					board.kill(serverString);
				}
				if(serverString.startsWith("move")){
					board.oneMove(serverString);
				}
				if(serverString.startsWith("End")){
					board.endOrg();
				}
				board.repaint();


			}
		}
		catch (IOException e) {
			System.exit(1);
		}
	}

}
