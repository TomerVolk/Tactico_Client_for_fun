package client;

import java.awt.*;
import javax.swing.JPanel;

import client.Board.Status;

/**
 * the bar at the top of the frame that shows what was the last fight
 * @author tomer
 */
public class InfoBar extends JPanel{
	private static final long serialVersionUID = 1L;
	int mark;
	/**
	 * the board of this player
	 */
	Board b;
	/**
	 * the indexes of the tool from this player from last fight
	 */
	int YourFight=-1;
	/**
 	 * the indexes of the tool from the opponent from last fight
	 */
	int OpponentFight=-1;
	/**
	 * the constructor of the Bar, initiates the board and the size
	 * @param board the board of this game
	 */
	public InfoBar(Board board) {
		b=board;
		this.setPreferredSize(new Dimension(b.getWidth(), 50));
	}
	/**
	 * the function that draws everything
	 * @param g the graphics of the draw
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		if(b.status== Status.flag) g.drawString("place your flag", 10, 40);
		if(b.status== Status.org&&mark!=-1){
			if(mark!=0)		g.drawString("organize your tools, you clicked on "+mark, 10, 40);
			else		g.drawString("organize your tools, you clicked on a bomb", 10, 40);
		}
		if(YourFight==-1||OpponentFight==-1) return;
		g.drawString("Your Tool was: "+YourFight, 50, 45);
		g.drawString("Opponent Tool was: "+OpponentFight, 500, 45);
	}
	public void updateFight(String serverString){
		String [] fight= serverString.split("##");
		YourFight= Integer.parseInt(fight[1]);
		OpponentFight= Integer.parseInt(fight[2]);
		YourFight= Board.converter[YourFight];
		OpponentFight= Board.converter[OpponentFight];
		repaint();
	}
	public void updateMarkOrg(String serverString) {
		String[] data= serverString.split("##");
		mark= Integer.parseInt(data[1]);
		this.repaint();
		
	}
		
}