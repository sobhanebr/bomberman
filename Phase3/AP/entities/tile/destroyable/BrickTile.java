package Phase3.AP.entities.tile.destroyable;

import Phase3.AP.entities.Entity;
import Phase3.AP.entities.mob.enemy.Enemy;

public class BrickTile extends DestroyableTile {
    private final  static boolean hasBomb =false ;
    public BrickTile(int x, int y) {
        super(x, y);
        _belowSprite = null;
    }

    public static boolean HasBomb() {
        return hasBomb;
    }
}
