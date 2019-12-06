package Phase3.AP.entities.tile;

import Phase3.AP.entities.Entity;


public class GrassTile extends Tile {
    private boolean hasBomb ;
    public GrassTile(int x, int y ) {
        super(x, y);
        _belowSprite = null;
        hasBomb = false;

    }

    @Override
    public boolean collide(Entity e) {
        return true;
    }


    public  void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }

    public  boolean HasBomb() {
        return hasBomb;
    }


    @Override
    public void update() {

    }

}
