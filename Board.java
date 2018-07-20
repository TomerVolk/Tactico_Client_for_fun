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
	static int [] converter= new int[40];
	/**
	 * The Class responsible of the connection to the server
	 */
	Communication client;
	/**
	 * The id of the player
	 */
	int id;
	int turn;
	static ImageIcon tool[]= new ImageIcon[12];
	static ImageIcon toolRed[]= new ImageIcon[12];
	/**
	 * the constructor of the Class, that actually starts the game
	 * gives initial values to all fields and builds initial graphics
	 */
	public Board(){
		frame=new JFrame("Tactico");
		frame.setSize(1000, 950);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startConverter();
		for(int i=0;i<40;i++){
			me[i]=new Point(-1, -1);
			opponent[i]=new Point(-1, -1);
		}
		BorderLayout layout=new BorderLayout();
		client= new Communication(this);
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
		for(int i=0;i<12;i++){
			tool[i]=new ImageIcon("pic/"+i+".PNG");
			toolRed[i]=new ImageIcon("pic/"+i+"_1.PNG");
		}
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.addWindowListener(this);
		frame.addMouseListener(this);
		int auto=JOptionPane.showConfirmDialog(frame, "would you like to use the auto organizer?","Auto Organizer", JOptionPane.YES_NO_OPTION);
		if(auto== JOptionPane.YES_OPTION){
			client.send("auto");
		}
	}
	public void startConverter(){
		converter[0]=  (11); //creates the flag
		for(int i=1;i<7;i++){		//creates the bombs
			converter[i]= (0);
		}
		converter[7]=  (1);	//creates the spy
		for(int i=8;i<16;i++){	//creates the level 2
			converter[i]= (2);	
		}
		for(int i=16;i<21;i++){	//creates the level 3
			converter[i]= (3);	
		}
		for(int i=21;i<25;i++){//create level 4
			converter[i]= (4);	
		}
		for(int i=25;i<29;i++){//creates level 5
			converter[i]= (5);	
		}
		for(int i=29;i<33;i++){//creates level 6
			converter[i]= (6);	
		}
		for(int i=33;i<36;i++){//creates level 7
			converter[i]= (7);	
		}
		for(int i=36;i<38;i++){//creates level 8
			converter[i]= (8);	
		}
		converter[38]=  (9); //creates level 9
		converter[39]= (10); //creates level 10
	}
	void analizeData(String serverString) {
		String players[]= serverString.split("Player");
		if(id==0){
			updatePlayer(players[1], me);
			updatePlayer(players[2], opponent);
		}
		else{
			updatePlayer(players[2], me);
			updatePlayer(players[1], opponent);
		}
		repaint();
	}
	private void updatePlayer(String player, Point[] p){
		String[] one_player= player.split("Tool");
		for(int j=1;j<one_player.length&&j<41;j++){
			System.out.println(one_player[j]);
			String tool[]= one_player[j].split("##");
			int x= Integer.parseInt(tool[0]), y= Integer.parseInt(tool[1]);
			if(id==1)	p[j-1]=new Point(x,9- y);
			else p[j-1]= new Point(x, y);
		}
	}
	public void updateTurn(String serverString){
		String [] data= serverString.split("##");
		turn= Integer.parseInt(data[1]);
		if(turn==id){
			frame.setTitle("Your Turn");
		}
		else{
			frame.setTitle("Opponent Turn");
		}
		repaint();

	}
	public void analizeOrg(String serverString){
		String[]  one_player= serverString.split("Tool");
		for(int j=1;j<one_player.length&&j<41;j++){
			String tool[]= one_player[j].split("##");
			int x= Integer.parseInt(tool[0]), y= Integer.parseInt(tool[1]);
			me[j-1]=new Point(x, y);
		}
		repaint();
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
			if(id==0)	client.send("Point##"+(e.getX()-50)/(frame.getWidth()/11)+"##"+(e.getY()-50)/(frame.getHeight()/11)+"##center##"+e.getButton());
			if(id==1)	client.send("Point##"+(e.getX()-50)/(frame.getWidth()/11)+"##"+(9-(e.getY()-50)/(frame.getHeight()/11))+"##center##"+e.getButton());
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
