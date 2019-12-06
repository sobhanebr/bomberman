package Phase3.AP;


import Phase3.AP.Threads.EnemiesThread;
import Phase3.AP.Threads.MainPlayerThread;
import Phase3.AP.Threads.TimerThread;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.gui.GameFrame;
import Phase3.AP.gui.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import Phase3.AP.gui.Sprites;


public class Game implements Serializable {

    private static final long serialVersionUID = 1785613087883604960L;
    private boolean _running = true;
    private boolean _paused = false;
    private int width, height;
    private final Board board;
    private static final int BOMBLIMIT = 1;
    private static final int CELLSCALE = 50;
    private static final int BOMBRADIUS = 1;
    private static final int PLAYERSPEED = 2;
    private transient EnemiesThread enemiesThread;
    static final int TIME = 300;
    private int cellScale = CELLSCALE;
    private int maxPlayerNumbers;
    private int enemyNumber ;
    private final static Sprites sprites = new Sprites();
    private ArrayList<Player> players ;
    private transient GameServer gameServer;


    public Game(int width, int height, int enemyNumber, int maxPlayerNumber , GameServer gameServer) {
        this.width = width;
        this.height = height;
        this.gameServer = gameServer;
        if (enemyNumber != -1)
            this.enemyNumber = enemyNumber;
        else
            this.enemyNumber = Math.min(width, height);
        maxPlayerNumbers = maxPlayerNumber;
        if (enemyNumber == -1) {
            board = new Board(this);
        } else {
            board = new Board(this, enemyNumber);
        }
        System.out.println("new board created!");
        players = board.getPlayers();
    }

    public void startGame() {
        enemiesThread =new EnemiesThread(board.getEnemies(), this);
        enemiesThread.start();
    }


    public BufferedImage getPlayerIm() {
        return sprites.getPlayerIm();
    }

    public BufferedImage getGrassTileIm() {
        return sprites.getGrassTileIm();
    }

    public BufferedImage getWallTileIm() {
        return sprites.getWallTileIm();
    }

    public BufferedImage getPortalTileIm() {
        return sprites.getPortalTileIm();
    }

    public BufferedImage getBrickTileIm() {
        return sprites.getBrickTileIm();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Board getBoard() {
        return board;
    }


    private void setWidth(int width) {
        this.width = width;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public int getCellScale() {
        return cellScale;
    }


    public static BufferedImage getBombIm() {
        return sprites.getBombIm();
    }


    public static void setBombIm(BufferedImage bombIm) {
        sprites.setBombIm(bombIm);
    }

    public boolean is_running() {
        return _running;
    }

    public void setCellScale(int cellScale) {
        this.cellScale = cellScale;
    }


    public int getPlayerSpeedConstant() {
        return PLAYERSPEED;
    }

    public int getPlayerspeedConstant() {
        return PLAYERSPEED;
    }

    public int getBombRadiusConstant() {
        return BOMBRADIUS;
    }

    public int getBombLimitConstant() {
        return BOMBLIMIT;
    }


    public static BufferedImage getControlBombsIm() {
        return sprites.getControlBombsIm();
    }

    public static BufferedImage getDecreaseBombsIm() {
        return sprites.getDecreaseBombsIm();
    }

    public static BufferedImage getDecreasePointsIm() {
        return sprites.getDecreasePointsIm();
    }

    public static BufferedImage getDecreaseRadiusIm() {
        return sprites.getDecreaseRadiusIm();
    }

    public static BufferedImage getDecreaseSpeedIm() {
        return sprites.getDecreaseSpeedIm();
    }

    public static BufferedImage getGhostModeIm() {
        return sprites.getGhostModeIm();
    }

    public static BufferedImage getIncreaseBombsIm() {
        return sprites.getIncreaseBombsIm();
    }

    public static BufferedImage getIncreasePointsIm() {
        return sprites.getIncreasePointsIm();
    }

    public static BufferedImage getIncreaseRadiusIm() {
        return sprites.getIncreaseRadiusIm();
    }

    public static BufferedImage getIncreaseSpeedIm() {
        return sprites.getIncreaseSpeedIm();
    }

    @Override
    public String toString() {
        return width + " * " + height + " game including "
                + enemyNumber + " enemies and " + maxPlayerNumbers + " maximum player number";
    }


    int getMaxPlayerNumbers() {
        return maxPlayerNumbers;
    }

    public Player addPlayer(Socket socket, GameClient gameClient) {
        Player p = board.addPlayer(socket, gameClient);
        if (board.getPlayers().size() == 1) {
            startGame();
        }
        return p;
    }

    public int getCapacity() {
        return board.getCapacity();
    }

     GameServer getGameServer() {
        return gameServer;
    }

    public void endGame() {
        board.endGame();
        gameServer.endGame(this);
    }

     EnemiesThread getEnemiesThread() {
        return enemiesThread;
    }

    //TODO : delete bottom method
    public void nextLevel(){
        board.nextLevel();
    }
}

