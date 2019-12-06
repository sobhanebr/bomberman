package Phase3.AP.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


import Phase3.AP.GameType;
import Phase3.AP.Threads.LoadThread;
import Phase3.AP.Threads.SaveThread;
import Phase3.AP.gui.GameFrame;

public class GameMenu extends JMenu {

	public GameFrame frame;

	public GameMenu(GameFrame frame) {
		super("GameMenu");
		this.frame = frame;

		/*
		 * New GameMenu
		 */
		JMenuItem newgame = new JMenuItem("New GameMenu");
		newgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		newgame.addActionListener(new MenuActionListener(frame));
		add(newgame);

		/*
		 * Scores
		 */
		JMenuItem scores = new JMenuItem("Load GameMenu");
		scores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		scores.addActionListener(new MenuActionListener(frame));
		add(scores);

		/*
		 * Codes
		 */
		JMenuItem codes = new JMenuItem("Save GameMenu");
		codes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		codes.addActionListener(new MenuActionListener(frame));
		add(codes);
	}

	class MenuActionListener implements ActionListener {
		public GameFrame _frame;
		public MenuActionListener(GameFrame frame) {
			_frame = frame;
		}

		  @Override
		public synchronized void actionPerformed(ActionEvent e) {

			  if(e.getActionCommand().equals("New GameMenu")) {
                  frame.dispose();
//                  Phase3.AP.Game game = new Phase3.AP.Game(1);
//                  game.initialize();
			  }

			  else if(e.getActionCommand().equals("Load GameMenu")) {
                  LoadThread loadThread = new LoadThread(frame.getGame());
                  loadThread.start();
			  }
			  
			  else if(e.getActionCommand().equals("Save GameMenu")) {
                  SaveThread saveThread = new SaveThread( frame.getGame() );
                  saveThread.start();
			  }

		  }
		}

}
