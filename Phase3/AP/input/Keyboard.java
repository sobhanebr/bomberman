package Phase3.AP.input;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.Threads.ExplosionThread;
import Phase3.AP.Threads.LoadThread;
import Phase3.AP.Threads.SaveThread;
import Phase3.AP.entities.bomb.Bomb;
import Phase3.AP.entities.mob.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Keyboard implements KeyListener, Serializable {

    private static final long serialVersionUID = 6529497011267657690L;
    private boolean[] keys = new boolean[550]; //120 is enough to this game :|
    private Game game;
    public boolean up, down, left, right, b, explosion , nextlevel;
    private Player player;
    private Board board;
    private boolean isViewer;

    public Keyboard(Game game, Board board, Player player) {
        this.game = game;
        this.board = board;
        if (player != null) {
            this.player = player;
            isViewer = false;
        } else {
            isViewer = true;
        }
    }

    //    synchronized
    public synchronized void update() {
        synchronized (this) {
            up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
            down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
            left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
            right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
            b = keys[KeyEvent.VK_B];
            nextlevel = keys[KeyEvent.VK_N];
            explosion = keys[KeyEvent.VK_SPACE];
            if (!isViewer){
                if (b) {
                    board.addBomb(new Bomb((int) ((player.getX())), (int) (player.getY()), board, player), player);
                } else if (explosion) {
                    if (player.hasControlBombsPowerUp() && board.getBombs().size() > 0) {
                        new ExplosionThread(board, board.getBombs().get(0)).start();
                        if (!board.getBombs().get(0).is_exploded())
                            board.getBombs().get(0).explode();
                    }
                }
                else if (nextlevel){
                    game.nextLevel();
                }
                else
                    player.calculateMove();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        update();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        update();
    }


}
