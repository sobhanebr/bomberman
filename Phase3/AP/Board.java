package Phase3.AP;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.*;
import java.util.List;

import Phase3.AP.Threads.BombThread;
import Phase3.AP.Threads.ReflectedEnemyThread;
import Phase3.AP.entities.bomb.Bomb;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.entities.mob.enemy.*;
import Phase3.AP.entities.tile.GrassTile;
import Phase3.AP.entities.tile.PortalTile;
import Phase3.AP.entities.tile.Tile;
import Phase3.AP.entities.tile.WallTile;
import Phase3.AP.entities.tile.destroyable.BrickTile;
import Phase3.AP.gui.GameFrame;
import Phase3.AP.level.Level;
import Phase3.AP.powerUp.*;

public class Board implements Serializable {

    private static final long serialVersionUID = 2329005017769276683L;
    private int enemyNumbers;
    private int powerupNumbers;
    private int brickCounter = 0;
    private int _screenToShow = -1; //1:endgame, 2:changelevel, 3:paused
    public Tile[][] tiles;
    private Level _level;
    private int counter = 0;
    private Game _game;
    private int width, height;
    private int time;
    private ArrayList<Enemy> enemies;
    private Map<Socket, Player> socketPlayerMap;
    private ArrayList<Player> players;
    private List<Bomb> _bombs;


    Board(Game game) {
        _game = game;
        _level = new Level(this);
        players = new ArrayList<>();
        initialize();
        changeLevel(1); //start in level 1
        enemyNumbers = Math.min(width, height);
        makeEnemies();
        powerupNumbers = Math.min(2 * enemyNumbers, brickCounter / 2);
        makePowerups();
        socketPlayerMap = new HashMap<>();
    }

    Board(Game game, int enemyNumbers) {
        this._game = game;
        this.enemyNumbers = enemyNumbers;
        _level = new Level(this);
        players = new ArrayList<>();
        initialize();
        changeLevel(1); //start in level 1
        makeEnemies();
        powerupNumbers = Math.min(2 * enemyNumbers, brickCounter / 2);
        makePowerups();
        socketPlayerMap = new HashMap<>();
    }

    public void initialize() {
        time = Game.TIME;
        _bombs = new ArrayList<>();
        enemies = new ArrayList<>();
        width = _game.getWidth();
        height = _game.getHeight();
        Random random = new Random();
        tiles = new Tile[_game.getWidth()][_game.getHeight()];
        double l = 0.25;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i % 2 == 1) && (j % 2 == 1)) {
                    tiles[i][j] = new WallTile(i, j);
                } else {
                    double d = random.nextDouble();
                    if (d < l && !(i <= 2 && j <= 2)) {
                        tiles[i][j] = new BrickTile(i, j);
                        brickCounter++;
                    } else {
                        tiles[i][j] = new GrassTile(i, j);
                    }
                }
            }
        }
        int rx = random.nextInt(getWidth()), ry = random.nextInt(getHeight());
        while (!(tiles[rx][ry] instanceof GrassTile) || ((tiles[rx][ry]).getEntities().size() != 0)) {
            rx = random.nextInt(getWidth());
            ry = random.nextInt(getHeight());
        }
        System.out.println("Portal tile @(" + rx + "," + ry + ")");
        tiles[rx][ry] = new BrickTile(rx, ry);
        brickCounter++;
        (tiles[rx][ry]).addEntity(new PortalTile(rx, ry));


    }

    public void makeEnemies() {
        Random r = new Random();
        int rx, ry;
        for (int i = 0; i < enemyNumbers; i++) {
            rx = r.nextInt(getWidth());
            ry = r.nextInt(getHeight());
            int levelType = 1;
            if (_level.getLevel() > 1)
                levelType = r.nextInt(_level.getLevel() - 1) + 1;
            while (!(tiles[rx][ry] instanceof GrassTile) || (tiles[rx][ry]).getEntities().size() != 0) {
                rx = r.nextInt(getWidth());
                ry = r.nextInt(getHeight());
            }
            Enemy b = null;
            int optionsNumb = getEnemyTypeNumbersOfLevel(levelType);
            int enemyIndex;
            if (optionsNumb >0) {
                enemyIndex =  r.nextInt(optionsNumb);
                switch (enemyIndex) {
                    case 0:
                        if (levelType < 5) {
                            switch (levelType) {
                                case 1:
                                    b = new Balloom_1(rx, ry, this, _game);
                                    break;
                                case 2:
                                    b = new Oneal_2(rx, ry, this, _game);
                                    break;
                                case 3:
                                    b = new Minvo_3(rx, ry, this, _game);
                                    break;
                                case 4:
                                    b = new Kondoria_4(rx, ry, this, _game);
                                    break;
                            }
                        } else {
                            try {
                                Class<? extends Enemy> enemyClass = _game.getGameServer().getReflectedEnemyClasses().get(enemyIndex);
                                Constructor constructor = enemyClass.getDeclaredConstructor(int.class, int.class, Board.class,
                                        Game.class);
                                constructor.setAccessible(true);
                                b = (Enemy) constructor.newInstance(rx, ry, this, _game);
                                new ReflectedEnemyThread(b, _game).start();
                            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;
                    default:
                        try {
                            Class<? extends Enemy> enemyClass = _game.getGameServer().getReflectedEnemyClasses().get(enemyIndex-1);
                            Constructor constructor = enemyClass.getDeclaredConstructor(int.class, int.class, Board.class,
                                    Game.class);
                            constructor.setAccessible(true);
                            b = (Enemy) constructor.newInstance(rx, ry, this, _game);
                            new ReflectedEnemyThread(b, _game).start();
                        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            }
            else {
                int type = r.nextInt(4);
                switch (type) {
                    case 0:
                        b = new Balloom_1(rx, ry, this, _game);
                        break;
                    case 1:
                        b = new Oneal_2(rx, ry, this, _game);
                        break;
                    case 2:
                        b = new Minvo_3(rx, ry, this, _game);
                        break;
                    case 3:
                        b = new Kondoria_4(rx, ry, this, _game);
                        break;
                }
            }
            System.out.println(b);
            enemies.add(b);
            if (_game.getEnemiesThread() != null)
                _game.getEnemiesThread().updateEnemyList();
            (tiles[rx][ry]).addEntity(b);
        }
    }

    public void makePowerups() {
        Random r = new Random();
        int t, rx, ry;
        for (int i = 0; i < powerupNumbers; i++) {
            rx = r.nextInt(getWidth());
            ry = r.nextInt(getHeight());
            while (!(tiles[rx][ry] instanceof GrassTile) || (tiles[rx][ry]).getEntities().size() != 0) {
                rx = r.nextInt(getWidth());
                ry = r.nextInt(getHeight());
            }
            PowerUp powerUp = null;
            t = r.nextInt(10);
            switch (t) {
                case 0:
                    powerUp = new ControlBombs(rx, ry);
                    break;
                case 1:
                    powerUp = new DecreaseBombs(rx, ry);
                    break;
                case 2:
                    powerUp = new DecreasePoints(rx, ry);
                    break;
                case 3:
                    powerUp = new IncreaseSpeed(rx, ry);
                    break;
                case 4:
                    powerUp = new DecreaseRadius(rx, ry);
                    break;
                case 5:
                    powerUp = new DecreaseSpeed(rx, ry);
                    break;
                case 6:
                    powerUp = new GhostMode(rx, ry);
                    break;
                case 7:
                    powerUp = new IncreaseBombs(rx, ry);
                    break;
                case 8:
                    powerUp = new IncreasePoints(rx, ry);
                    break;
                default:
                    powerUp = new IncreaseRadius(rx, ry);
                    break;
            }
            tiles[rx][ry] = new BrickTile(rx, ry);
            tiles[rx][ry].addEntity(powerUp);
            System.out.println("new " + powerUp + " @(" + rx + "," + ry + ")");
        }
    }

    public Game get_game() {
        return _game;
    }

    /*
    |--------------------------------------------------------------------------
    | Render & Update
    |--------------------------------------------------------------------------
     */

    public void update() {

//        updateEntities();
//        updateMobs();
//        updateBombs();detectEndGame();

    }


    public void render(GameFrame screen) {
    }

    /*
    |--------------------------------------------------------------------------
    | ChangeLevel
    |--------------------------------------------------------------------------
     */

    public void newGame() {
        resetProperties();
        changeLevel(1);
    }

    @SuppressWarnings("static-access")
    private void resetProperties() {
        for (Player p : players) {
            p.setPlayerSpeed(1);
            p.setBombLimit(1);
            p.setBombRadius(1);
        }
    }

    public void restartLevel() {
        changeLevel(_level.getLevel());
    }

    public void nextLevel() {
        changeLevel(_level.getLevel() + 1);
        _level.nextLevel();
    }

    public void changeLevel(int level) {
        _screenToShow = 2;
        _bombs.clear();

    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void addBomb(Bomb b, Player bomber) {
        if (b != null && _bombs.size() < bomber.getBombLimit()) {
            _bombs.add(b);
            (tiles[(int) b.getX()][(int) b.getY()]).addEntity(b);
            ((GrassTile) tiles[(int) b.getX()][(int) b.getY()]).setHasBomb(true);
            if (!bomber.hasControlBombsPowerUp())
                new BombThread(new Date(), b, this).start();
        }
    }


    public List<Bomb> getBombs() {
        if (_bombs != null)
            return _bombs;
        else {
            return null;
        }
    }


    public int getTime() {
        return time;
    }

    public int subtractTime() {
//        if(_game.isPaused())
//            return this._time;
//        else
        if (time > 0)
            return this.time--;
        else
            for (Player p : players) {
                if (p.getPoints() >= 100)
                    p.addPoint(-100);
                else
                    p.kill(null);
            }
        return 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void removeBomb(Bomb bomb) {
        _bombs.remove(bomb);
    }


    public int getEnemyNumbers() {
        return this.enemyNumbers;
    }

    public void setEnemyNumbers(int enemyNumbers) {
        this.enemyNumbers = enemyNumbers;
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    Player addPlayer(Socket socket, GameClient gameClient) {
        Random random = new Random();
        int rx = random.nextInt(getWidth()), ry = random.nextInt(getHeight());
        while (!(tiles[rx][ry] instanceof GrassTile) || ((tiles[rx][ry]).getEntities().size() != 0)) {
            rx = random.nextInt(getWidth());
            ry = random.nextInt(getHeight());
        }
        Player p = new Player(rx, ry, this, _game, gameClient);
        socketPlayerMap.put(socket, p);
        players.add(p);
        return p;
    }

    public Level get_level() {
        return _level;
    }

    private int getEnemyTypeNumbersOfLevel(int level) {
        if (level < 1)
            return 0;
        int count = 0;
        for (Class<? extends Enemy> enemyClass : _game.getGameServer().getReflectedEnemyClasses()) {
            try {
                Field levelField = enemyClass.getSuperclass().getDeclaredField("level");
                levelField.setAccessible(true);
                Constructor constructor = enemyClass.getDeclaredConstructor(int.class, int.class, Board.class,
                        Game.class);
                constructor.setAccessible(true);
                Enemy temp = (Enemy) constructor.newInstance(0, 0, this, _game);
                int classLevelValue = (int) levelField.get(temp);
                System.out.println(classLevelValue);
                if (classLevelValue == level) {
                    count++;
                }
            } catch (NoSuchFieldException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (level < 5)
            count++;
        return count;
    }

    int getCapacity() {
        return _game.getMaxPlayerNumbers() - players.size();
    }

    void endGame() {
        for (Player p : players) {
            p.getGameClient().getGameFrame().dispose();
            p.kill(null);
        }

    }

    public void fixPlayersPlace(){
        for (Player player : players)
        {
            Random random = new Random();
            int rx = random.nextInt(getWidth()), ry = random.nextInt(getHeight());
            while (!(tiles[rx][ry] instanceof GrassTile) || ((tiles[rx][ry]).getEntities().size() != 0)) {
                rx = random.nextInt(getWidth());
                ry = random.nextInt(getHeight());
            }
            player.setLocation(rx , ry);
        }
    }

}
