package Phase3.AP.powerUp;

import Phase3.AP.Board;
import Phase3.AP.entities.mob.Player;

public class GhostMode extends PowerUp {
    public GhostMode(int x, int y) {
        super(x, y);
    }


    @Override
    void perform(Player player) {
        player.setInGhostMode(true);
    }
}
