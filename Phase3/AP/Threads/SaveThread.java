package Phase3.AP.Threads;

import Phase3.AP.Game;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveThread extends Thread {

    private Game game;

    public SaveThread(Game game) {
        this.game = game;
        System.out.println(game);
        System.out.println(game.getBoard());
        System.out.println(game.getWidth() + "," + game.getHeight());
    }

    @Override
    public synchronized void run()
    {
        super.run();
        try {
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
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println(selectedFile.getAbsolutePath());
                FileOutputStream fileOut = new FileOutputStream(selectedFile.getParentFile().getAbsolutePath()
                        +"\\" +selectedFile.getName() +".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeUnshared(game);
//                out.writeUnshared(game.getBoard());
//                out.writeUnshared(game.getWidth());
//                out.writeUnshared(game.getHeight());
//                out.writeUnshared(game.getBoard());
                out.close();
                fileOut.close();
                System.out.println("Serialized data is saved in " + selectedFile.getAbsolutePath());
            }

        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
