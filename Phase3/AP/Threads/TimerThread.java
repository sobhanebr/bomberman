package Phase3.AP.Threads;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.mob.Player;

public class TimerThread extends Thread {

    private Game game;
    private Board board;
    private Player player;

    public TimerThread(Game game, Board board, Player player) {
        this.game = game;
        this.board = board;
        this.player = player;
    }

    @Override
    public void run() {
        super.run();
        long timer = System.currentTimeMillis();

        while (game.is_running()) {
            if (System.currentTimeMillis() - timer > 1000) { //once per second
                player.getGameClient().getGameFrame().getInfoPanel().setTime(board.subtractTime());
                timer += 1000;
            }
        }
    }
}
