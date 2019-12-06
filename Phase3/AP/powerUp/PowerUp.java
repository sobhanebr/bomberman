package Phase3.AP.powerUp;

import Phase3.AP.entities.Entity;
import Phase3.AP.entities.mob.Player;

public abstract class PowerUp extends Entity {
    private static final long serialVersionUID = 2768453063269796683L;


    public PowerUp(int x , int y ) {
        this._x = x;
        this._y = y;
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Player)
            this.perform((Player) e);
        return true;
    }

     abstract void perform(Player player);

    @Override
    public void update() { }
}
