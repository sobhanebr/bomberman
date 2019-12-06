package Phase3.AP.powerUp;


import Phase3.AP.Game;
import Phase3.AP.entities.mob.Player;

public class DecreaseRadius extends PowerUp {
    public DecreaseRadius(int x, int y) {
        super(x, y);
    }

    @Override
    void perform(Player player) {
        if (player.getBombRadius() > 1)
            player.setBombRadius(player.getBombRadius() -1);
    }
}
