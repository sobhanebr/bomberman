package Phase3.AP.powerUp;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.mob.Player;

public class DecreaseSpeed extends PowerUp {
    public DecreaseSpeed(int x, int y) {
        super(x, y);
    }

    @Override
    void perform(Player player) {
        if (player.getPlayerSpeed() > 1)
            player.setPlayerSpeed(player.getPlayerSpeed()-1);
    }
}
