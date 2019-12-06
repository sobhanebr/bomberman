package Phase3.AP.powerUp;


import Phase3.AP.entities.mob.Player;

public class DecreasePoints extends PowerUp {


    public DecreasePoints(int x, int y) {
        super(x, y);
    }

    @Override
    void perform(Player player) {
        player.addPoint(-100);
    }
}
