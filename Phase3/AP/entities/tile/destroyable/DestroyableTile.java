package Phase3.AP.entities.tile.destroyable;

import Phase3.AP.entities.Entity;
import Phase3.AP.entities.tile.Tile;

public abstract class DestroyableTile extends Tile {
    protected boolean _destroyed = false;


    public DestroyableTile(int x, int y) {
        super(x, y);
    }

    public boolean isDestroyed() {
        return _destroyed;
    }

    public void destroy() {
        _destroyed = true;
    }


    @Override
    public boolean collide(Entity e) {
        //TODO : explode if e's instance of bomb
        return false;
    }

    @Override
    public void update() {
        if (_destroyed){
         //TODO : animate
         remove();
        }
    }
    public void addBelowSprite(Tile sprite) {
        _belowSprite = sprite;
    }

}
