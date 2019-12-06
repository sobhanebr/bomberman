package Phase3.AP.Threads;

import Phase3.AP.Game;
import Phase3.AP.entities.mob.enemy.*;

import java.util.ArrayList;

public class EnemiesThread extends Thread {
    private ArrayList<Balloom_1> l1 ;
    private ArrayList<Oneal_2> l2 ;
    private ArrayList<Minvo_3> l3 ;
    private ArrayList<Kondoria_4> l4 ;

    private final int  period ;
    private Game game;


    public EnemiesThread(ArrayList<Enemy> enemies , Game game) {
        this.game = game;
        l1 = new ArrayList<>();
        l2 = new ArrayList<>();
        l3 = new ArrayList<>();
        l4 = new ArrayList<>();
        for(Enemy e : enemies){
            if (e instanceof Balloom_1)
                l1.add((Balloom_1) e);
            else if (e instanceof  Oneal_2)
                l2.add((Oneal_2) e);
            else if (e instanceof Minvo_3)
                l3.add((Minvo_3) e);
            else if (e instanceof Kondoria_4)
                l4.add((Kondoria_4) e);
        }
        period = 400/(game.getPlayerspeedConstant());
    }

    @Override
    public void run() {
        super.run();
        long beforeTime, timeDiff, sleep;
        short evenCounter = 0 ;
        beforeTime = System.currentTimeMillis();
        while (true) {
            calculate(l3);
            calculate(l4);
            if (evenCounter == 0)
            {
                calculate(l1);
                calculate(l2);
            }
            evenCounter++;
            if (evenCounter>1)
                evenCounter = 0;
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

    public void updateEnemyList(){
        for(Enemy e : game.getBoard().getEnemies() ){
            if (e instanceof Balloom_1)
                l1.add((Balloom_1) e);
            else if (e instanceof  Oneal_2)
                l2.add((Oneal_2) e);
            else if (e instanceof Minvo_3)
                l3.add((Minvo_3) e);
            else if (e instanceof Kondoria_4)
                l4.add((Kondoria_4) e);
        }
    }

    private void calculate(ArrayList<? extends Enemy> enemies){
        for (Enemy e : enemies){
            e.update();
        }
    }
}
