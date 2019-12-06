package Phase3.AP.gui;

import Phase3.AP.Game;
import Phase3.AP.GameClient;
import Phase3.AP.entities.mob.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePlayListButton extends JButton {
    private Game game;
    private GameClient gameClient;

    public GamePlayListButton(Game game, GameClient gameClient) {
        super("- " + game.toString());
        this.game = game;
        this.gameClient = gameClient;
        this.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.getCapacity() > 0) {
                    String[] options = {"Player", "Viewer"};
                    int x = JOptionPane.showOptionDialog(null, "Getting in as a Player or a Viewer?",
                            "Choose your role",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    switch (x) {
                        case 0:
                            gameClient.setViewer(false);
                            gameClient.setGame(game);
                            break;
                        case 1:
                            gameClient.setViewer(true);
                            gameClient.setGame(game);
                            break;
                    }
                } else {
                    int input = JOptionPane.showConfirmDialog(null,
                            "You're about to join as a viewer .", "No Capacity",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
                    if (input == 0) {
                        gameClient.setViewer(true);
                        gameClient.setGame(game);
                    }
                }
            }
        });
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
    }
}
