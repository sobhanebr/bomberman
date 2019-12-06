package Phase3.AP.powerUp;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.mob.Player;

public class DecreaseBombs extends PowerUp {
    public DecreaseBombs(int x, int y) {
        super(x, y);
    }

    @Override
    void perform(Player player) {
        if (player.getBombLimit() > 1)
            player.setBombLimit(player.getBombLimit()-1);
    }
}
