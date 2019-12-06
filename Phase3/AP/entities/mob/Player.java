package Phase3.AP.entities.mob;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.GameClient;
import Phase3.AP.entities.Entity;
import Phase3.AP.entities.bomb.Bomb;
import Phase3.AP.entities.tile.GrassTile;
import Phase3.AP.entities.tile.PortalTile;
import Phase3.AP.entities.tile.WallTile;
import Phase3.AP.entities.tile.destroyable.BrickTile;
import Phase3.AP.input.Keyboard;
import Phase3.AP.powerUp.ControlBombs;
import Phase3.AP.powerUp.PowerUp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends Mob implements Serializable {

    private static final long serialVersionUID = 6529008311267757690L;
    private List<Bomb> _bombs;
    private Keyboard _input;
    private int points;
    private int vx = 0, vy = 0;
    private boolean isInGhostMode = false;
    private static List<PowerUp> _powerups;
    //can be modified with bonus
    private int bombLimit;
    private int bombRadius;
    private int playerSpeed;
    private GameClient gameClient;


    public Player(int x, int y, Board board, Game game, GameClient gameClient) {
        super(x, y, board, game);
        this.gameClient = gameClient;
        points = 0;
        _powerups = new ArrayList<>();
        _bombs = _board.getBombs();
        bombLimit = game.getBombLimitConstant();
        bombRadius = game.getBombRadiusConstant();
        playerSpeed = game.getPlayerspeedConstant();
        _input = new Keyboard(game, board, this);
    }


    /*
    |--------------------------------------------------------------------------
    | Update & Render
    |--------------------------------------------------------------------------
     */
    @Override
    public void update() {

//        clearBombs();
//        if (!_alive) {
//            afterKill();
//            return;
//        }

//        animate();

        calculateMove();

//        detectPlaceBomb();
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof PortalTile) {
            if (_board.getEnemies().size() != 0) {
                return false;
            } else {
                addPoint(100);
                _board.nextLevel();
                this.getGameClient().getGamePanel().repaint();
            }
        } else if (e instanceof PowerUp) {
            e.collide(this);
            _powerups.add((PowerUp) e);
            _board.getTiles()[(int) e.getX()][(int) e.getY()].removeEntity(e);
        }
        if (isInGhostMode)
            return !(e instanceof BrickTile) && (
                    (e instanceof WallTile) ||
                            !((e instanceof GrassTile) && ((GrassTile) e).HasBomb()));
        else
            return !(e instanceof BrickTile) &&
                    !(e instanceof WallTile) &&
                    !((e instanceof GrassTile) && ((GrassTile) e).HasBomb());

    }

//    @Override
//    public void render(Screen screen) {
//        calculateXOffset();
//
//        if(_alive)
//            chooseSprite();
//        else
//            _sprite = Sprite.player_dead1;
//
//        screen.renderEntity((int)_x, (int)_y - _sprite.SIZE, this);
//    }


    /*
    |--------------------------------------------------------------------------
    | Mob Unique
    |--------------------------------------------------------------------------
     */
    private void detectPlaceBomb() {
//        if(_input.space && GameMenu.getBombLimit() > 0 && _timeBetweenPutBombs < 0)
        {

//            int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
//            int yt = Coordinates.pixelToTile( (_y + _sprite.getSize() / 2) - _sprite.getSize() ); //subtract half player height and minus 1 y position
//
//            placeBomb(xt,yt);
//            GameMenu.addBombRate(-1);


        }
    }


    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
//                GameMenu.addBombRate(1);
            }
        }

    }

    /*
    |--------------------------------------------------------------------------
    | Mob Colide & Kill
    |--------------------------------------------------------------------------
     */
    @Override
    public void kill(Player bomber) {
        if (!_alive) return;
        _alive = false;
        int counter = 0;
        for (Player pl : game.getBoard().getPlayers()) {
            if (pl.isAlive()) {
                counter++;
                break;
            }
        }
        if (counter == 0)
            game.endGame();
        _powerups.clear();
        bombLimit = game.getBombLimitConstant();
        bombRadius = game.getBombRadiusConstant();
        playerSpeed = game.getPlayerspeedConstant();
        if (bomber != null)
            bomber.addPoint(100);
        System.err.println("you Lost!!");


//        _board.addLives(-1);
//
//        Message msg = new Message("-1 LIVE", getXMessage(), getYMessage(), 2, Color.white, 14);
//        _board.addMessage(msg);
    }


    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            if (_bombs.size() == 0) {

//                if(_board.getLives() == 0)
//                    _board.endGame();
//                else
//                    _board.restartLevel();
            }
        }
    }

    @Override
    protected boolean canMove(double x, double y) {
        return (x == 1 && (this._x + 1) < game.getWidth() && this.collide(_board.tiles[(int) (this._x + 1)][(int) (this._y)])
                || (x == -1 && this._x > 0 && this.collide(_board.tiles[(int) (this._x - 1)][(int) (this._y)])
                || (y == 1 && (this._y + 1) < game.getHeight() && this.collide(_board.tiles[(int) (this._x)][(int) (this._y + 1)])
                || (y == -1 && this._y > 0 && this.collide(_board.tiles[(int) (this._x)][(int) (this._y - 1)])))));
    }

    /*
    |--------------------------------------------------------------------------
    | Mob Movement
    |--------------------------------------------------------------------------
     */
    @Override
    public void calculateMove() {
        if (_input.up || _input.down || _input.left || _input.right) {
            if (_input.up) vy = -1;
            else if (_input.down) vy = 1;
            else if (_input.left) vx = -1;
            else vx = 1;
        } else
            vx = vy = 0;
    }


    public void move() {
        if (vx > 0) _direction = 0;
        else if (vx < 0) _direction = 2;
        if (vy < 0) _direction = 1;
        else if (vy > 0) _direction = 3;
        if (canMove(0, vy)) { //separate the moves for the player can slide when is colliding
            _board.getTiles()[(int) _x][(int) _y].removeEntity(this);
            _y += vy;
            _board.getTiles()[(int) _x][(int) _y].addEntity(this);
        }
        if (canMove(vx, 0)) {
            _board.getTiles()[(int) _x][(int) _y].removeEntity(this);
            _x += vx;
            _board.getTiles()[(int) _x][(int) _y].addEntity(this);
        }
        for (Entity e : _board.getTiles()[(int) _x][(int) _y].getEntities())
            this.collide(e);
        if (getGameClient().getGamePanel() != null)
            this.render(getGameClient().getGamePanel());
    }

    public boolean hasControlBombsPowerUp() {
        for (PowerUp p : _powerups) {
            if (p instanceof ControlBombs)
                return true;
        }
        return false;
    }

    public void addPoint(int p) {
        points += p;
        gameClient.getGameFrame().getInfoPanel().setPoints(points);
    }

    public int getPoints() {
        return points;
    }

    public void addPowerUp(PowerUp powerUp) {
        _powerups.add(powerUp);
    }

    public boolean isInGhostMode() {
        return isInGhostMode;
    }

    public void setInGhostMode(boolean inGhostMode) {
        isInGhostMode = inGhostMode;
    }

    public void setBombLimit(int bombLimit) {
        this.bombLimit = bombLimit;
    }

    public void setBombRadius(int bombRadius) {
        this.bombRadius = bombRadius;
    }

    public void setPlayerSpeed(int playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public int getBombLimit() {
        return bombLimit;
    }

    public int getBombRadius() {
        return bombRadius;
    }

    public int getPlayerSpeed() {
        return playerSpeed;
    }

    public void moved() {
        vx = vy = 0;
    }

    public Keyboard get_input() {
        return _input;
    }

    public GameClient getGameClient() {
        return gameClient;
    }

    public Game getGame() {
        return game;
    }

    public void setLocation(int rx, int ry) {
        _board.getTiles()[(int) _x][(int) _y].removeEntity(this);
        _x = rx;
        _y = ry;
        _board.getTiles()[(int) _x][(int) _y].addEntity(this);
    }
}