package client;
import java.io.*;
import java.net.*;

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
			socket=new Socket("",12345); 
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
				if(serverString.startsWith("Wait for start")) {
					String mar[]=serverString.split("##");
					board.id=Integer.parseInt(mar[1]);
					board.frame.setTitle("Waiting for start of the game");
				}
				if(serverString.startsWith("Player")) {
					System.out.println("id=  "+board.id+" server sent "+serverString);
					board.analizeData(serverString);
				}
				if(serverString.startsWith("Org")){
					System.out.println("id=  "+board.id+" server sent "+serverString);
					board.analizeOrg(serverString);
				}
				if(serverString.startsWith("fight")){
					board.info.updateFight(serverString);
				}


			}
		}
		catch (IOException e) {
			System.exit(1);
		}
	}

}
