package Phase3.AP.entities.mob.enemy;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.Entity;
import Phase3.AP.entities.mob.Mob;
import Phase3.AP.entities.mob.Player;

import java.util.Random;


public abstract class Enemy extends Mob {
    private int speed;
    private int level;
    private String picturePath;
    protected Player target;
    int vx, vy;
    int dx, dy;
    Random random;

    private Enemy(int x, int y, Board board, Game game, int speed, int level) {
        super(x, y, board, game);
        this.speed = speed;
        this.level = level;
        random = new Random();
    }

    Enemy(int x, int y, Board board, Game game, int speed, int level, String picturePath) {
        this(x, y, board, game, speed, level);
        this.picturePath = picturePath;
    }


    public void kill(Player bomber) {
        if (!_alive) return;
        _alive = false;
        _board.removeEnemy(this);
        _board.getTiles()[(int) _x][(int) _y].removeEntity(this);
        if (bomber!= null)
        bomber.addPoint(20 * level);
    }

    @Override
    public void update() {
        if (_alive)
            calculateMove();
    }

    @Override
    protected void move() {
        if (!_alive) return;
        _board.getTiles()[(int) _x][(int) _y].removeEntity(this);
        _y += vy;
        _x += vx;
        _board.getTiles()[(int) _x][(int) _y].addEntity(this);
        vx = vy = 0;
        for (Entity e : _board.getTiles()[(int) _x][(int) _y].getEntities())
            this.collide(e);
    }

    @Override
    public boolean canMove(double x, double y) {
        if ((_x + x) >= 0
                && (_y + y) >= 0
                && (_x + x) < _board.getWidth()
                && (_y + y) < _board.getHeight()
                && this.collide(_board.getTiles()[(int) (_x + x)][(int) (_y + y)])) {
            for (Entity e : _board.getTiles()[(int) (_x + x)][(int) (_y + y)].getEntities())
                if (!this.collide(e))
                    return false;
            return true;
        }
        return false;

    }

    void findNearestPlayer() {
        int diff = Integer.MAX_VALUE;
        target = null;
        for (Player p : _board.getPlayers()) {
            int z = (int) Math.sqrt((p.getX() - _x) * (p.getX() - _x) + (p.getY() - _y) * (p.getY() - _y));
            if (z < diff) {
                diff = z;
                target = p;
            }
        }
        if (target != null) {
            dx = (int) (target.getX() -_x);
            dy = (int) (target.getY() -_y);
        }
        else {
            System.out.println("found no player");
        }
    }

    @Override
    public abstract boolean collide(Entity e);

    @Override
    protected abstract void calculateMove();

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public int getSpeed() {
        return speed;
    }
}
