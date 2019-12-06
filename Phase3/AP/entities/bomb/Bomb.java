package Phase3.AP.entities.bomb;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.Entity;
import Phase3.AP.entities.mob.Mob;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.entities.tile.GrassTile;
import Phase3.AP.entities.tile.WallTile;
import Phase3.AP.entities.tile.destroyable.BrickTile;

public class Bomb extends Entity {

    private Board _board;
    private Player bomber;
    private boolean _allowedToPassThrough = false;
    private boolean _exploded = false;

    @Override
    public void update() {

    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Mob) {
            ((Mob) e).kill(bomber);

        }
        if (e instanceof BrickTile) {
            Entity en;
            if (_board.getTiles()[(int) e.getX()][(int) e.getY()].getEntities().size() > 0)
                en = _board.getTiles()[(int) e.getX()][(int) e.getY()].getEntities().get(0);
            else en = null;
            _board.getTiles()[(int) e.getX()][(int) e.getY()] = new GrassTile((int) e.getX(), (int) e.getY());
            if (en != null)
                _board.getTiles()[(int) e.getX()][(int) e.getY()].addEntity(en);
            bomber.addPoint(10);
        } else if (e instanceof Bomb) {
            ((Bomb) e).explode();
        }
        return _allowedToPassThrough;
    }

    //
    public Bomb(int x, int y, Board board , Player bomber) {
        _x = x;
        _y = y;
        _board = board;
        this.bomber = bomber;
    }

    public void explode() {
        ((GrassTile) _board.getTiles()[(int) this.getX()][(int) this.getY()]).setHasBomb(false);
        _exploded = true;
        boolean[] a = {true, true, true, true};
        for (int r = 1; r <= bomber.getBombRadius(); r++) {
            if (a[0]) {
                if (_x + r < _board.getWidth() && (_board.getTiles()[(int) (_x + r)][(int) _y] instanceof WallTile
                        || _board.getTiles()[(int) (_x + r)][(int) _y] instanceof BrickTile)) {
                    a[0] = false;
                }
                if (_x + r < _board.getWidth() && _board.getTiles()[(int) (_x + r)][(int) _y] instanceof GrassTile) {
                    for (Entity e : _board.getTiles()[(int) (_x + r)][(int) _y].getEntities()) {
                        collide(e);
                    }
                }
                if (_x + r < _board.getWidth())
                    collide(_board.getTiles()[(int) (_x + r)][(int) _y]);
            }
            if (a[1]) {
                if (_y - r >= 0 && (_board.getTiles()[(int) _x][(int) (_y - r)] instanceof WallTile
                        || _board.getTiles()[(int) _x][(int) (_y - r)] instanceof BrickTile)) {
                    a[1] = false;
                }
                if ((_y - r >= 0) && (_board.getTiles()[(int) _x][(int) (_y - r)] instanceof GrassTile)) {
                    for (Entity e : _board.getTiles()[(int) _x][(int) (_y - r)].getEntities()) {
                        collide(e);
                    }
                }
                if (_y - r >= 0)
                    collide(_board.getTiles()[(int) _x][(int) (_y - r)]);
            }
            if (a[2]) {
                if (_x - r >= 0 && (_board.getTiles()[(int) (_x - r)][(int) _y] instanceof WallTile
                        || _board.getTiles()[(int) (_x - r)][(int) _y] instanceof BrickTile)) {
                    {
                        a[2] = false;
                    }
                }
                if (_x - r >= 0 && _board.getTiles()[(int) (_x - r)][(int) _y] instanceof GrassTile) {
                    for (Entity e : _board.getTiles()[(int) (_x - r)][(int) _y].getEntities()) {
                        collide(e);
                    }
                }
                if (_x - r >= 0)
                    collide(_board.getTiles()[(int) (_x - r)][(int) _y]);
            }
            if (a[3]) {
                if (_y + r < _board.getHeight() && (_board.getTiles()[(int) _x][(int) (_y + r)] instanceof WallTile
                        || _board.getTiles()[(int) _x][(int) (_y + r)] instanceof BrickTile)) {
                    a[3] = false;
                }
                if (_y + r < _board.getHeight() && _board.getTiles()[(int) _x][(int) (_y + r)] instanceof GrassTile) {
                    for (Entity e : _board.getTiles()[(int) _x][(int) (_y + r)].getEntities())
                        collide(e);
                }
            }
            if (_y + r < _board.getHeight())
                collide(_board.getTiles()[(int) _x][(int) (_y + r)]);
        }
    }


    public boolean is_exploded() {
        return _exploded;
    }

    public Player getBomber() {
        return bomber;
    }
}
