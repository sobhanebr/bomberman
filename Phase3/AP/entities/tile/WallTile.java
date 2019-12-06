package Phase3.AP.entities.tile;

import Phase3.AP.entities.Entity;

public class WallTile extends Tile {
    private static final boolean hasBomb = false;
    public WallTile(int x, int y ) {
        super(x, y);
        _belowSprite = new GrassTile(x,y);
    }

    @Override
    public boolean collide(Entity e) {
        return false;
    }

    @Override
    public void update() {

    }

    public static boolean isHasBomb() {
        return hasBomb;
    }
}
