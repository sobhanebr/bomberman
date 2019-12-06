package Phase3.AP.entities.tile;

import Phase3.AP.entities.Entity;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Tile extends Entity implements Serializable {
    private static final long serialVersionUID = 6529689011557757690L;
    protected Tile _belowSprite;
    private ArrayList<Entity> entities ;
    public Tile(int x, int y) {
        _x = x;
        _y = y;
        entities = new ArrayList<>();
    }


    @Override
    public abstract boolean collide(Entity e);


    public void addEntity(Entity e){
        entities.add(e);
    }

    public void removeEntity(Entity e){
        entities.remove(e);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }


    @Override
    public abstract void update();
}
