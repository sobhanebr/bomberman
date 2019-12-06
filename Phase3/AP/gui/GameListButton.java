package Phase3.AP.gui;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.mob.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameListButton extends JButton {
    private Game game;
    private Board board;
    private int index;

    public GameListButton(Game game ,Board board, int index) {
        super("- " + game.toString());
        this.game = game;
        this.board = board;
        this.index = index;
        this.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makePointFrame();
            }
        });
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
    }

    private void makePointFrame(){
        JFrame jFrame = new JFrame("Game(" + index + ") Points");
        jFrame.setPreferredSize(new Dimension(500,200));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel , BoxLayout.PAGE_AXIS));
        if (board.getPlayers().size() == 0)
            panel.add(new JLabel("No client found inside yet!"));
        for (Player p : board.getPlayers()){
            panel.add(new JLabel(p.toString() + " : " + p.getPoints()));
        }
        jFrame.getContentPane().add(new JScrollPane(panel));
        jFrame.pack();
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }


}
