package Phase3.AP.entities.tile;

import Phase3.AP.entities.Entity;

public class PortalTile extends WallTile {
    public PortalTile(int x, int y) {
        super(x, y);
        _belowSprite = null;
    }

    @Override
    public boolean collide(Entity e) {
        return true;
    }

    @Override
    public void update() {

    }

}
