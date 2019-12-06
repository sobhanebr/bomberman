package Phase3.AP.gui;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.Entity;
import Phase3.AP.entities.bomb.Bomb;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.entities.mob.enemy.*;
import Phase3.AP.entities.tile.GrassTile;
import Phase3.AP.entities.tile.PortalTile;
import Phase3.AP.entities.tile.Tile;
import Phase3.AP.entities.tile.WallTile;
import Phase3.AP.entities.tile.destroyable.BrickTile;
import Phase3.AP.powerUp.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class GamePanel extends JPanel implements Serializable {
    private static final long serialVersionUID = 6584685011267757690L;
    private Board board;
    private Game game;
    private Player player;
    private Tile[][] tiles;
    private ArrayList<Bomb> bombs;
    private transient BufferedImage playerIm, grassTileIm, wallTileIm, portalTileIm, brickTileIm, bombIm;

    public GamePanel(Game game, Board board, Player player) {
        this.game = game;
        this.board = board;
        this.player = player;
        bombs = (ArrayList<Bomb>) board.getBombs();
        try {
            playerIm = ImageIO.read(new File(player.getGameClient().getMyPicturePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        grassTileIm = game.getGrassTileIm();
        wallTileIm = game.getWallTileIm();
        portalTileIm = game.getPortalTileIm();
        brickTileIm = game.getBrickTileIm();
        bombIm = Game.getBombIm();

    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tiles = board.getTiles();
        int wLen = Math.min(20, game.getWidth()), hLen = Math.min(20, game.getHeight());
        int minI = (int) Math.max(0, player.getX() - 9), minJ = (int) Math.max(0, player.getY() - 11);
        if ((int) Math.min(minI + 20, game.getWidth()) - minI < wLen)
            minI = game.getWidth() - wLen;
        if ((int) Math.min(minJ + 20, game.getHeight()) - minJ < hLen)
            minJ = game.getHeight() - hLen;
        for (int i = minI; i < (int) Math.min(minI + 20, game.getWidth()); i++) {
            for (int j = minJ; j < (int) Math.min(minJ + 20, game.getHeight()); j++) {
                if (tiles[i][j] instanceof WallTile) {
                    g2.drawImage(wallTileIm.getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                            new Color(0, 0, 0, 0), null);

                } else if (tiles[i][j] instanceof BrickTile) {
                    g2.drawImage(brickTileIm.getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                            new Color(0, 0, 0, 0), null);

                } else if (tiles[i][j] instanceof GrassTile) {
                    g2.drawImage(grassTileIm.getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                            new Color(0, 0, 0, 0), null);
                    synchronized (this) {
                        try {
                            for (Entity e : tiles[i][j].getEntities()) {
                                if (e instanceof PortalTile)
                                    g2.drawImage(portalTileIm.getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof Enemy){
                                    BufferedImage pic = ImageIO.read(new File(((Enemy)e).getPicturePath()));
                                    g2.drawImage(pic.getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                }
                                else if (e instanceof ControlBombs)
                                    g2.drawImage(game.getControlBombsIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof DecreaseBombs)
                                    g2.drawImage(game.getDecreaseBombsIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof DecreasePoints)
                                    g2.drawImage(game.getDecreasePointsIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof DecreaseRadius)
                                    g2.drawImage(game.getDecreaseRadiusIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof DecreaseSpeed)
                                    g2.drawImage(game.getDecreaseSpeedIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof IncreaseBombs)
                                    g2.drawImage(game.getIncreaseBombsIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof IncreasePoints)
                                    g2.drawImage(game.getIncreasePointsIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof IncreaseRadius)
                                    g2.drawImage(game.getIncreaseRadiusIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof IncreaseSpeed)
                                    g2.drawImage(game.getIncreaseSpeedIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                else if (e instanceof GhostMode)
                                    g2.drawImage(game.getGhostModeIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                if (e instanceof Bomb) {
                                    g2.drawImage(game.getBombIm().getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                            (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                            new Color(0, 0, 0, 0), null);
                                }
                                if (e instanceof Player && !e.equals(player) && e != player) {
                                    try {
                                        BufferedImage buff = ImageIO.read(new File(((Player) e).getGameClient().getMyPicturePath()));
                                        g2.drawImage(buff.getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH),
                                                (int) (tiles[i][j].getX() - minI) * game.getCellScale(), (int) (tiles[i][j].getY() - minJ) * game.getCellScale(),
                                                new Color(0, 0, 0, 0), null);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                            if (player.isAlive())
                                g2.drawImage((playerIm.getScaledInstance(game.getCellScale(), game.getCellScale(), Image.SCALE_SMOOTH)),
                                        (int) (player.getX() - minI) * game.getCellScale(), (int) (player.getY() - minJ) * game.getCellScale(),
                                        new Color(0, 0, 0, 0), null);

                        } catch (java.util.ConcurrentModificationException e) {
                            //TODO
                        } catch (IOException e) {
                            System.err.println("Couldn't find enemy pic . \n" + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public void update() {
        bombIm = Game.getBombIm();
        repaint();
    }

    @Override
    public void repaint() {
        super.repaint();
    }


}
