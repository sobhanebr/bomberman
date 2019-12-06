package Phase3.AP.Threads;

import Phase3.AP.Game;
import Phase3.AP.entities.mob.enemy.*;


public class ReflectedEnemyThread extends Thread {
    private final int period;
    private final Enemy enemy;


    public ReflectedEnemyThread(Enemy enemy, Game game) {
        this.enemy = enemy;
        period = 400 / enemy.getSpeed() * (game.getPlayerspeedConstant());
    }

    @Override
    public void run() {
        super.run();
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (true) {
            enemy.update();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = period - timeDiff;
            if (sleep < 0)
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.err.println("Animation interrupted!");
            }
            beforeTime = System.currentTimeMillis();
        }
    }

}
