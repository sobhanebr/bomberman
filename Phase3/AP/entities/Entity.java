package Phase3.AP.entities;

import Phase3.AP.gui.GamePanel;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 6529685011211037690L;
    protected double _x, _y;
    protected boolean _removed = false;

    public abstract void update();

    public void remove() {
        _removed = true;
    }

    public boolean isRemoved() {
        return _removed;
    }

    public abstract boolean collide(Entity e);

    /*
    public boolean collide(ArrayList<Entity> arrayList) {
        boolean res ;
        for (Entity e : arrayList){
            res =  collide(e);
            if (!res) return false;
        }
        return true;
    }
    */

    public double getX() {
        return _x;
    }

    public double getY() { return _y; }
    public void render(GamePanel gamePanel){
//        gamePanel.revalidate();
        gamePanel.repaint();
    }

}


