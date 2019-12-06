package Phase3.AP.gui;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.input.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

import Phase3.AP.menu.Menu;

public class GameFrame extends JFrame {

    private Game game;
    private Board board;
    private Menu menu;
    private InfoPanel infoPanel;


    public GameFrame(Game game , Board board) throws HeadlessException {
        this.game = game;
        this.board = board;
        this.setLayout(new BorderLayout());
        this.setTitle("Bomberman");
        menu = new Menu( this );
        infoPanel = new InfoPanel(game);
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout());
        innerPanel.add(menu );
        innerPanel.add(infoPanel);
        add(innerPanel , BorderLayout.NORTH) ;
        this.setSize(Math.min(game.getWidth(),20) * game.getCellScale()  + 6
                , Math.min(game.getHeight(),20) * game.getCellScale() + 75);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public Game getGame() {
        return game;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }



}
