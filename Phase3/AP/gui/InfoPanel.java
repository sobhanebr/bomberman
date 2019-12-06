package Phase3.AP.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Phase3.AP.Game;

public class InfoPanel extends JPanel {
	
	private JLabel timeLabel;
	private JLabel levelLabel;
//	private JLabel livesLabel;
	private int time ;
private Game game;

	 InfoPanel(Game game) {
	 	this.game = game;
		setLayout(new GridLayout());

		time = game.getBoard().getTime();
		timeLabel = new JLabel("Time: " + timeInOrder() );
		timeLabel.setForeground(Color.white);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		
		levelLabel = new JLabel("Level: " + game.getBoard().get_level().getLevel());
		levelLabel.setForeground(Color.white);
		levelLabel.setHorizontalAlignment(JLabel.CENTER);
		
		add(timeLabel);
		add(levelLabel);
//		add(livesLabel);
		
		
		setBackground(Color.black);
		setPreferredSize(new Dimension(0, 40));
	}
	
	public void setTime(int t) {
		time = t ;
		timeLabel.setText("Time: " + timeInOrder());
	}

//	public void setLives(int t) {
//		livesLabel.setText("Lives: " + t);
//
//	}

	private String timeInOrder(){
		int min = time/60 , sec = time % 60;
		String res = "";
		if (min < 10)
			res += 0 + "" + min + ":";
		else
			res +=  time/60 + ":";
		if (sec < 10)
			res += 0 + "" + sec;
		else
			res +=  sec;
		return res;
	}

	public void gotToNextLevel(){
		levelLabel.setText("Level: " + game.getBoard().get_level().getLevel());
	}

	public void setPoints(int t) {
		levelLabel.setText("Points: " + t);
	}
	
}
