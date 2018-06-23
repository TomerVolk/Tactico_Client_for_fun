package client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * the class who is responsible for the graphics.
 * All the parameters are for the use of all classes
 *@author Tomer Volk
 */
public class Board extends JPanel implements MouseListener,WindowListener{
	static final long serialVersionUID = 1L;
	/**
	 * The place of the click- whether it was at the sides of center panels
	 */
	public enum Place{
		right, left, main;
	}
	public enum Status{
		org, play, youWin, opponentWin, flag
	}
	Status status;
	/**
	 * The main frame of the game
	 */
	JFrame frame;
	/**
	 * The Bar at the left side of the board
	 */
	SideBar leftBar;
	/**
	 * The Bar at the right side of the board
	 */
	SideBar rightBar;
	/**
	 * The Bar at the up side of the board
	 */
	InfoBar info;
	/**
	 * The panel of the board itself
	 */
	MainPanel main;
	/**
	 * The Opponent player
	 */
	Point[] opponent= new Point[40];
	/**
	 * The Current player
	 */
	Point [] me= new Point[40];
	/**
	 * The Class responsible of the connection to the server
	 */
	Communication client;
	/**
	 * The id of the player
	 */
	int id;
	int turn;
	/**
	 * the constructor of the Class, that actually starts the game
	 * gives initial values to all fields and builds initial graphics
	 */
	public Board(){
		frame=new JFrame("Tactico");
		frame.setSize(1000, 950);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout=new BorderLayout();
		this.setLayout(layout);
		main=new MainPanel(this);
		this.add(main, BorderLayout.CENTER);
		if(status!=Status.opponentWin&&status!=Status.youWin) {
			info=new InfoBar(this);
			this.add(info, BorderLayout.NORTH);
			rightBar=new SideBar(this, true);
			this.add(rightBar, BorderLayout.WEST);
			leftBar=new SideBar(this, false);
			this.add(leftBar, BorderLayout.EAST);
		}
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.addWindowListener(this);
		frame.addMouseListener(this);
	}
	/**
	 * gets the initial data from the server
	 * @param turn- who's turn it is according to server
	 * @param serverString- the data from the string
	 */
	public void analizeData(String serverString) {
		// analyzing the data from server at the beginning
		
		turn= 1-turn;
		if(id== turn) frame.setTitle("Your turn");
		else frame.setTitle("Opponent turn");
	}
	public void analizeOrg(String serverString){
		
	}

	/**
	 * automatically activated when the mouse is clicked
	 */
	public void mouseClicked(MouseEvent e) {
		if(!frame.contains(e.getPoint())||e.getY()<50) {
			return;
		}
		if(e.getX()< leftBar.getWidth()){
			client.send("Point##"+e.getX()+"##"+ e.getY()/(frame.getHeight()/11)+"##left##"+e.getButton());
			return;
		}
		if(main.contains(e.getPoint())){
			client.send("Point##"+(e.getX()-50)/(frame.getWidth()/11)+"##"+(e.getY()-50)/(frame.getHeight()/11)+"##center##"+e.getButton());
			return;
		}
	}
	/**
	 * closes the client and the game when X is pressed
	 */
	public void windowClosing(WindowEvent arg0) {
		client.send("bye");
	}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
}
