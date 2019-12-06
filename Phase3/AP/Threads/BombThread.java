package Phase3.AP.Threads;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.bomb.Bomb;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class BombThread extends Thread {
    private Date nextAnimation;
    private int stepDelay = 5000;
    private Bomb bomb;
    private Board board;
    private static ArrayList<BufferedImage> images;
    private static BufferedImage mainImage;

    public BombThread(Date nextAnimation, Bomb bomb, Board board) {
        this.board = board;
        this.nextAnimation = nextAnimation;
        this.nextAnimation.setTime(nextAnimation.getTime() + stepDelay);
        this.bomb = bomb;
        images = new ArrayList<>();
        mainImage = Game.getBombIm();
        try {
            images.add(ImageIO.read(new File("src/images/op/sprites/Bomb_f02.png").toURI().toURL()));
            images.add(ImageIO.read(new File("src/images/op/sprites/Bomb_f03.png").toURI().toURL()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        long z = 0;
//        System.out.println(new Date());
        while (new Date().before(nextAnimation)) {

            long t = nextAnimation.getTime() - new Date().getTime();
            if (t % 1000 == 0) {
                Game.setBombIm(images.get((int) z % 2));
                z++;
            }
        }
//        System.out.println(new Date());
        Game.setBombIm(mainImage);
        new ExplosionThread(board, bomb).start();
        if (!bomb.is_exploded())
            bomb.explode();
    }
}
