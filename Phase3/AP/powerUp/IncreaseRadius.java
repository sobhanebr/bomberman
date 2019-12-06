package Phase3.AP.powerUp;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.mob.Player;

public class IncreaseRadius extends PowerUp {
    public IncreaseRadius(int x, int y) {
        super(x, y);
    }

    @Override
    void perform(Player player) {
        player.setBombRadius(player.getBombRadius() +1);
    }
}
