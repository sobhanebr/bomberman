package Phase3.AP.powerUp;

import Phase3.AP.Board;
import Phase3.AP.entities.mob.Player;

public class IncreasePoints extends PowerUp {
    public IncreasePoints(int x, int y) {
        super(x, y);
    }

    @Override
    void perform(Player player) {
        player.addPoint(100);
    }
}
