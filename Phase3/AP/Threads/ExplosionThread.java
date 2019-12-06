package Phase3.AP.Threads;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.bomb.Bomb;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExplosionThread extends Thread {
    private static ArrayList<BufferedImage> images;
    private static BufferedImage mainImage;
    private Board _board;
    private Bomb bomb;

    public ExplosionThread(Board board, Bomb bomb) {
        this._board = board;
        this.bomb = bomb;
        images = new ArrayList<>();
        mainImage = Game.getBombIm();
        try {
            for (int i = 1; i < 299; i++) {
                images.add(ImageIO.read(new File("src/images/images/g ("+i+").png").toURI().toURL()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        super.run();
        for(BufferedImage bf : images)
        {
            Game.setBombIm(bf);
            try {
                sleep(11);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        _board.removeBomb(bomb);
        _board.getTiles()[(int) bomb.getX()][(int) bomb.getY()].removeEntity(bomb);
        Game.setBombIm(mainImage);
        bomb.getBomber().getGameClient().getGamePanel().repaint();
    }
}
