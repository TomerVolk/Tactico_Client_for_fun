package client;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import client.Board.Status;
/**
 * the panel where the board is, and where the tools are
 * @author tomer
 */
public class MainPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	/**
	 * the frame of the board
	 */
	private Board board;
	int mark;
	/**
	 * the constructor, initiates the board field and the size and background
	 * @param b
	 */
	public MainPanel(Board b){
		this.board=b;
		this.setPreferredSize(new Dimension(b.frame.getWidth()-100, b.frame.getHeight()-50));
		this.setBackground(new Color(0, 153, 0));
	}
	/**
	 * the function that draws the panel, including all the tools
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(board.status==Status.opponentWin) {
			g.setFont(new Font(Font.BOLD+"", Font.BOLD, 200));
			g.drawString("You \n Lost", 500, 500);
			return;
		}
		if(board.status==Status.youWin) {
			g.setFont(new Font(Font.BOLD+"", Font.BOLD, 200));
			g.drawString("You \n Won", 500, 500);
			return;
		}
		g.drawImage(new ImageIcon("pic/board.png").getImage(), 0, 0, this.getWidth(), this.getHeight(),null);
		for(int i=0;i<40;i++){
			Point[] OTools=board.opponent;
			if (!OTools[i].equals(new Point(-1,-1))) {
				Point p= OTools[i];
				g.setColor(Color.red);
				g.fillRect(this.getWidth()/10*(int) p.getX(), this.getHeight()/10*(int)p.getY(),  this.getWidth()/10, this.getHeight()/10);
				g.setColor(Color.BLACK);
			}
		}
		
		g.setColor(Color.black);
		for(int i=1;i<10;i++){
			g.drawLine(this.getWidth()/10*i, 0, this.getWidth()/10*i, this.getHeight());
			g.drawLine(0, this.getHeight()/10*i, this.getWidth(), this.getHeight()*i/10);
		}
		for(int i=0;i<40;i++){
			Point[] myTools=board.me;
			Point p= myTools[i];
			if (!myTools[i].equals(new Point(-1,-1))&& i!=mark) {
				g.drawImage(Board.tool[Board.converter[i]].getImage(), this.getWidth()/10*(int) p.getX(), this.getHeight()/10*(int)p.getY(),  this.getWidth()/10, this.getHeight()/10, null);
			}
			if (!myTools[i].equals(new Point(-1,-1))&& i==mark) {
				g.drawImage(Board.toolRed[Board.converter[i]].getImage(), this.getWidth()/10*(int) p.getX(), this.getHeight()/10*(int)p.getY(),  this.getWidth()/10, this.getHeight()/10, null);
			}
		}
		
	}
	public void updateMark(String serverString){
		String [] data= serverString.split("##");
		int marker= Integer.parseInt(data[1]);
		if(board.status==Board.Status.org) board.info.mark= marker;
		else{
			this.mark=marker;
		}
		System.out.println("marking "+marker);
		this.repaint();
	}
}