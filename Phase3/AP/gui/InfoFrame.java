package Phase3.AP.gui;

import Phase3.AP.Game;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.entities.mob.enemy.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class InfoFrame extends JFrame {
    private Game game;
    private JPanel temp;
    private JScrollPane js;
    private JButton ref;


    public InfoFrame(Game game) throws HeadlessException {
        super("Info");
        this.game = game;
        temp = new JPanel();
        ref = new JButton("Refresh");
        ref.addActionListener(e -> update());
        temp.setLayout(new GridLayout(game.getBoard().getPlayers().size() + game.getBoard().getEnemies().size() + 1, 3));
        for (Player player : game.getBoard().getPlayers()) {
            try {
                temp.add(new JLabel(new ImageIcon(ImageIO.read(new File(player.getGameClient().getMyPicturePath())))));
                temp.add(new JLabel(player.getGameClient().getName() + " : " + player.getPoints()));
                temp.add(new JLabel("@(" + (int) player.getX() + "," + (int) player.getY() + ")"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Enemy enemy : game.getBoard().getEnemies()) {
            try {
                temp.add(new JLabel(new ImageIcon(ImageIO.read(new File(enemy.getPicturePath())))));
                temp.add(new JLabel(enemy.getClass().getSimpleName()));
            } catch (IOException e) {
                System.err.println("Couldn't find enemy pic . \n" + e.getMessage());
            }
            temp.add(new JLabel("@(" + (int) enemy.getX() + "," + (int) enemy.getY() + ")"));
        }
        temp.add(ref);
        js = new JScrollPane(temp);
        this.add(js);
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }


    public void update() {
        temp.removeAll();
        for (Player player : game.getBoard().getPlayers()) {
            try {
                temp.add(new JLabel(new ImageIcon(ImageIO.read(new File(player.getGameClient().getMyPicturePath())))));
                temp.add(new JLabel(player.getGameClient().getName() + " : " + player.getPoints()));
                temp.add(new JLabel("@(" + (int) player.getX() + "," + (int) player.getY() + ")"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Enemy enemy : game.getBoard().getEnemies()) {
            try {
                temp.add(new JLabel(new ImageIcon(ImageIO.read(new File(enemy.getPicturePath())))));
                temp.add(new JLabel(enemy.getClass().getSimpleName()));
            } catch (IOException e) {
                System.err.println("Couldn't find enemy pic . \n" + e.getMessage());
            }
            temp.add(new JLabel("@(" + (int) enemy.getX() + "," + (int) enemy.getY() + ")"));
        }
        temp.add(ref);
        repaint();
    }
}
