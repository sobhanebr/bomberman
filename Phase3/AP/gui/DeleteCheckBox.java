package Phase3.AP.gui;

import Phase3.AP.Game;

import javax.swing.*;

public class DeleteCheckBox extends JCheckBox {
    private Game game;

    public DeleteCheckBox(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
