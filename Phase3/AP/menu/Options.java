package Phase3.AP.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Phase3.AP.Game;
import Phase3.AP.gui.GameFrame;

public class Options extends JMenu implements ChangeListener {

	GameFrame _frame;
	
	public Options(GameFrame frame) {
		super("Options");
		
		_frame = frame;
		
//		JMenuItem pause = new JMenuItem("Pause");
//		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
//		pause.addActionListener(new MenuActionListener(frame));
//		add(pause);
		
//		JMenuItem resume = new JMenuItem("Resume");
//		resume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
//		resume.addActionListener(new MenuActionListener(frame));
//		add(resume);
		
		addSeparator();
		
		add(new JLabel("Size: "));
		
		JSlider sizeRange = new JSlider(JSlider.HORIZONTAL,
                1, 5, 5);
		
		//Turn on labels at major tick marks.
		sizeRange.setMajorTickSpacing(2);
		sizeRange.setMinorTickSpacing(1);
		sizeRange.setPaintTicks(true);
		sizeRange.setPaintLabels(true);
		sizeRange.addChangeListener(this);
		
		add(sizeRange);
		
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        int fps = source.getValue();

//			game.setCellScale(fps*10);
//	        System.out.println( Game.getCellScale());
	        
//	        _frame._gamepane.changeSize();
//			_frame.setSize(_frame.getGame().getWidth() * Game.getCellScale()  + 6
//					, _frame.getGame().getHeight() * Game.getCellScale() + 58);
//			_frame.revalidate();
//			_frame.repaint();

	    }
	}
	
	class MenuActionListener implements ActionListener {
		public GameFrame _frame;
		public MenuActionListener(GameFrame frame) {
			_frame = frame;
		}
		
		  @Override
		public void actionPerformed(ActionEvent e) {
			  
			  if(e.getActionCommand().equals("Pause")) {
//				  _frame.pauseGame();
			  }
				  
			  if(e.getActionCommand().equals("Resume")) {
//				  _frame.resumeGame();
			  }
		  }
	}
}
