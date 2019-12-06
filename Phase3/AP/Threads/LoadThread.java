package Phase3.AP.Threads;

import Phase3.AP.Board;
import Phase3.AP.Game;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;

public class LoadThread extends Thread {
    private Game game = null;
    private Board board = null;
    private Game previousGame;

    public LoadThread(Game previousGame){
        this.previousGame = previousGame;
    }
    @Override
    public synchronized void run() {
        super.run();

        JFileChooser fileChooser = new JFileChooser("src\\savedGames");
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "SER Files (*.ser)";
            }

            public boolean accept(File f) {
                return !f.isDirectory() && f.getName().toLowerCase().endsWith(".ser");
            }
        });
        int returnValue = fileChooser.showOpenDialog(null);
        Game game = null;
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            FileInputStream fileIn = null;
            try {
                fileIn = new FileInputStream(selectedFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(fileIn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert in != null;
                game = (Game) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
//                        board = (Board) in.readObject();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        System.out.println("Deserialized GameMenu...");
        System.out.println();
        System.out.println("width: " + game.getWidth());
        System.out.println("height: " + game.getHeight());
        System.out.println("scale: " + game.getCellScale());
        System.out.println("board" + game.getBoard());

//        previousGame.getGameFrame().dispose();
        game.startGame();
    }

}


