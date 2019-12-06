package Phase3.AP.powerUp;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.mob.Player;

public class IncreaseSpeed extends PowerUp {
    public IncreaseSpeed(int x, int y) {
        super(x, y);
    }

    @Override
    void perform(Player player) {
        player.setPlayerSpeed(player.getPlayerSpeed() + 1);
    }
}
