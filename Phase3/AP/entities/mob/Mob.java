package Phase3.AP.entities.mob;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.Entity;

public abstract class Mob extends Entity {
    protected Board _board;
    protected Game game;
    protected int _direction = -1;
    protected boolean _alive = true;
    protected boolean _moving = false;
    public int _timeAfter = 80;


    public Mob(int x, int y, Board board, Game game) {
        this.game = game;
        _x = x;
        _y = y;
        this._board = board;
    }

    public abstract void update();

//    @Override
//    public abstract void render(Screen screen);

    protected abstract void calculateMove();

    protected abstract void move();

    public abstract void kill(Player bomber);

    protected abstract boolean canMove(double x, double y);

    public boolean isAlive() {
        return _alive;
    }

    public void reveal(){ _alive =true;}

    public boolean isMoving() {
        return _moving;
    }

    public int getDirection() {
        return _direction;
    }


}
