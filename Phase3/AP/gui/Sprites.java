package Phase3.AP.gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprites {
    private  BufferedImage playerIm, grassTileIm, wallTileIm, portalTileIm, brickTileIm, bombIm, controlBombsIm, decreaseBombsIm, decreasePointsIm, decreaseRadiusIm, decreaseSpeedIm, ghostModeIm, increaseBombsIm, increasePointsIm, increaseRadiusIm, increaseSpeedIm;

    public Sprites() {
        try {
            playerIm = ImageIO.read(new File("src/images/op/sprites/Bomberman/SideR/Bman_F_f00.png").toURI().toURL());
            grassTileIm = ImageIO.read(new File("src/images/grass.png").toURI().toURL());
            wallTileIm = ImageIO.read(new File("src/images/wall.png").toURI().toURL());
            brickTileIm = ImageIO.read(new File("src/images/brick.png").toURI().toURL());
            bombIm = ImageIO.read(new File("src/images/op/sprites/Bomb_f01.png").toURI().toURL());
            portalTileIm = ImageIO.read(new File("src/images/Portal.png").toURI().toURL());
            //Enemies


            //powerups
            increaseBombsIm = ImageIO.read(new File("src/images/Powerups/BombPowerup.png").toURI().toURL());
            increaseSpeedIm = ImageIO.read(new File("src/images/Powerups/SpeedPowerup.png").toURI().toURL());
            controlBombsIm = ImageIO.read(new File("src/images/classic.png"))
                    .getSubimage(0, 10 * 16, 16, 16);
            ghostModeIm = ImageIO.read(new File("src/images/classic.png"))
                    .getSubimage(16, 10 * 16, 16, 16);
            increasePointsIm = ImageIO.read(new File("src/images/plus.jpg").toURI().toURL());
            decreasePointsIm = ImageIO.read(new File("src/images/minus.jpg").toURI().toURL());
            increaseRadiusIm = ImageIO.read(new File("src/images/Powerups/FlamePowerup.png").toURI().toURL());
            decreaseRadiusIm = ImageIO.read(new File("src/images/classic.png"))
                    .getSubimage(5 * 16, 10 * 16, 16, 16);
            decreaseSpeedIm = ImageIO.read(new File("src/images/classic.png"))
                    .getSubimage(6 * 16, 10 * 16, 16, 16);
            decreaseBombsIm = ImageIO.read(new File("src/images/classic.png"))
                    .getSubimage(4 * 16, 10 * 16, 16, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void setBombIm(BufferedImage bombIm) {
        this.bombIm = bombIm;
    }

    public  BufferedImage getPlayerIm() {
        return playerIm;
    }

    public  BufferedImage getGrassTileIm() {
        return grassTileIm;
    }

    public  BufferedImage getWallTileIm() {
        return wallTileIm;
    }

    public  BufferedImage getPortalTileIm() {
        return portalTileIm;
    }

    public  BufferedImage getBrickTileIm() {
        return brickTileIm;
    }

    public  BufferedImage getBombIm() {
        return bombIm;
    }

    public  BufferedImage getControlBombsIm() {
        return controlBombsIm;
    }

    public  BufferedImage getDecreaseBombsIm() {
        return decreaseBombsIm;
    }

    public  BufferedImage getDecreasePointsIm() {
        return decreasePointsIm;
    }

    public  BufferedImage getDecreaseRadiusIm() {
        return decreaseRadiusIm;
    }

    public  BufferedImage getDecreaseSpeedIm() {
        return decreaseSpeedIm;
    }

    public  BufferedImage getGhostModeIm() {
        return ghostModeIm;
    }

    public  BufferedImage getIncreaseBombsIm() {
        return increaseBombsIm;
    }

    public  BufferedImage getIncreasePointsIm() {
        return increasePointsIm;
    }

    public  BufferedImage getIncreaseRadiusIm() {
        return increaseRadiusIm;
    }

    public  BufferedImage getIncreaseSpeedIm() {
        return increaseSpeedIm;
    }
}
