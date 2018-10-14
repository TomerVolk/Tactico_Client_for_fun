package client;

import java.awt.*;
import javax.swing.*;
/**
 * The side panels of the game- each player's grave yard
 * @author tomer
 */
public class SideBar extends JPanel{
	private static final long serialVersionUID = 1L;
	private int[] dead;
	/**
	 * Determines whether it's this player grave- yard or the other
	 */
	private boolean isMe;
	/**
	 * Default constructor, initiate the fields and background color
	 * @param b the board of the game
	 * @param isMe determines whether it's this player's or opponent's grave- yard
	 */
	public SideBar(Board b, boolean isMe){
		this.setPreferredSize(new Dimension(50,b.frame.getHeight()-50));
		this.isMe=isMe;
		if(isMe)	this.setBackground(Color.BLUE);
		else	this.setBackground(Color.RED);
		dead=new int[11];
		for(int i=0;i<11;i++){
			dead[i]=0;
		}
	}
	/**
	 * the function that draws the Bar
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(isMe) {
			g.setColor(Color.blue);
		}
		else {
			g.setColor(Color.red);
		}
		g.setFont(new Font(null, 0, 25));
		for(int i=0;i<11;i++){
			g.drawImage(Board.tool[i].getImage(), 0, this.getHeight()/11*i,  this.getWidth(), this.getHeight()/11, null);
			g.drawString(dead[i]+"", 30, this.getHeight()/11*(i+1)-15);
			g.drawLine(0, this.getHeight()/11*(i+1), this.getWidth(),  this.getHeight()/11*(i+1));
		}
	}
	public void updateDead(int type){
		if(type>10) return;
		dead[type]++;
		repaint();
	}
	public void unkill(int type){
		if(type>10) return;
		dead[type]--;
		repaint();
	}
}