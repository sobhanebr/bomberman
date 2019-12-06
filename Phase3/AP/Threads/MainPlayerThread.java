package Phase3.AP.Threads;

import Phase3.AP.Game;
import Phase3.AP.GameClient;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.gui.GamePanel;

public class MainPlayerThread extends Thread {
    private int delay;
    private Player player;
    private Game game;


    public MainPlayerThread(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    @Override
    public void run() {
        super.run();
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (true) {
            delay = 400 / (player.getPlayerSpeed());
            player.move(); //move  on the inputHandler
            player.moved();
            //
            GamePanel gp = player.getGameClient().getGamePanel();
            gp.repaint();
            //
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = delay - timeDiff;
            if (sleep < 0)
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Animation interrupted!");
            }
            beforeTime = System.currentTimeMillis();
        }
    }
}
