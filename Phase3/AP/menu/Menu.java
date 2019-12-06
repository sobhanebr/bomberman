package Phase3.AP.menu;

import Phase3.AP.gui.GameFrame;

import javax.swing.JMenuBar;


public class Menu extends JMenuBar {
	
	public Menu(GameFrame frame) {
		add( new GameMenu(frame) );
		add( new Options(frame) );
	}
	
}
