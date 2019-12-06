package Phase3.AP.Threads;

import Phase3.AP.Game;
import Phase3.AP.GameClient;
import Phase3.AP.GameServer;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.gui.ClientGUI;
import Phase3.AP.gui.GameFrame;
import Phase3.AP.gui.GamePanel;
import Phase3.AP.gui.InfoFrame;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private GameServer gameServer;
    private GameClient gameClient;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private BufferedReader reader;

    public ServerThread(GameServer gameServer, GameClient gameClient, Socket clientSocket) throws IOException {
        this.gameServer = gameServer;
        this.gameClient = gameClient;
        this.clientSocket = clientSocket;
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        super.run();
        while (clientSocket.isConnected()) {
            String str = null;
            try {
                str = reader.readLine();
                outputStream.reset();
            } catch (java.net.SocketException ex) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert str != null;
            if (str.contains("getGameList")) {
                try {
                    ArrayList<Game> games = gameServer.getGames();
                    outputStream.writeObject(games);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (str.contains("makeGame:")) {
                int[] semicolons = new int[3];
                int i = 0;
                for (int j = 0; j < str.length(); j++) {
                    if (str.charAt(j) == ';')
                        semicolons[i++] = j;
                }
                int t = str.indexOf(':');
                int width = Integer.parseInt(str.substring(t + 1, semicolons[0]));
                int height = Integer.parseInt(str.substring(semicolons[0] + 1, semicolons[1]));
                int enemyNumber = Integer.parseInt(str.substring(semicolons[1] + 1, semicolons[2]));
                int maxPlayerNumbers = Integer.parseInt(str.substring(semicolons[2] + 1));
                gameServer.addGame(new Game(width, height, enemyNumber, maxPlayerNumbers , gameServer));
            } else if (str.contains("addPlayer:")) {
                int indexOfGame = Integer.parseInt(str.substring(10));
                Game g = gameServer.getGames().get(indexOfGame);
                Player p = initPlayer(g);
                try {
                    outputStream.writeObject(p);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (str.contains("chatroomMsg:")) {
                gameServer.broadcast(str.substring(str.indexOf(':') + 1));
            }
        }
        try {
            clientSocket.close();
        } catch (IOException ignored) {
        }
        System.err.println("connection got lost");
    }

    public boolean writeMsg(String messageLf) {
        try {
            outputStream.writeObject(messageLf);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public GameClient getGameClient() {
        return gameClient;
    }


    private Player initPlayer(Game game) {
        Player p = game.addPlayer(gameClient.getSocket(), gameClient);
        GameFrame gf = new GameFrame(game, game.getBoard());
        gf.addKeyListener(p.get_input());
        GamePanel gamePanel = new GamePanel(game, game.getBoard(), p);
        gf.add(gamePanel, BorderLayout.CENTER);
        gameClient.setGamePanel(gamePanel);
        gameClient.setGameFrame(gf);
        gameClient.setInfoFrame(new InfoFrame(game));
        gameClient.setClientGUI(new ClientGUI(gameClient));
        new MainPlayerThread(p, game).start();
        new TimerThread(game, game.getBoard(), p).start();
        return p;
    }
}
