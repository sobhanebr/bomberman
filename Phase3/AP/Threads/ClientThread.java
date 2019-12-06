package Phase3.AP.Threads;

import Phase3.AP.Game;
import Phase3.AP.GameClient;
import Phase3.AP.entities.mob.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ClientThread extends Thread {
    private GameClient gameClient;
    private Socket serverSocket;
    private ObjectInputStream inputStream;
    private PrintWriter out;
    private String command;
    private final Object monitor, monitor2;
    private Object result;
    private boolean isWaited = true, needToRead, stillReading = true, chattingMode = false;

    public ClientThread(GameClient gameClient, Socket serverSocket) throws IOException {
        this.gameClient = gameClient;
        this.serverSocket = serverSocket;
        inputStream = new ObjectInputStream(this.serverSocket.getInputStream());
        out = new PrintWriter(this.serverSocket.getOutputStream(), true);
        monitor = new Object();
        monitor2 = new Object();
    }

    @Override
    public synchronized void run() {
        super.run();
        while (serverSocket.isConnected()) {
            if (!chattingMode) {
                do {
                    synchronized (monitor) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } while (isWaited);
                out.println(command);
                out.flush();
                synchronized (monitor2) {
                    if (needToRead) {
                        try {
                            result = inputStream.readObject();
                            stillReading = false;
                            monitor2.notify();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                isWaited = true;
            } else {
                try {
                    result = inputStream.readObject();
                    gameClient.getClientGUI().append((String) result);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        System.err.println("Connection got lost");

    }

    public ArrayList<Game> askForGameList() {
        synchronized (monitor) {
            isWaited = false;
            needToRead = true;
            command = "getGameList";
            monitor.notify();
        }
        synchronized (monitor2) {
            do {
                try {
                    monitor2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (stillReading);
            stillReading = true;
            return (ArrayList<Game>) result;
        }
    }

    public void newGameRequest(int width, int height, int enemyNumbers, int maxPlayerNumbers) {
        synchronized (monitor) {
            isWaited = false;
            needToRead = false;
            command = "makeGame:" + width + ";" + height + ";" + enemyNumbers + ";" + maxPlayerNumbers;
            monitor.notify();
        }
    }

    public Player addPlayer(int gameIndex) {
        synchronized (monitor) {
            isWaited = false;
            needToRead = true;
            command = "addPlayer:" + gameIndex;
            monitor.notify();
        }
        synchronized (monitor2) {
            do {
                try {
                    monitor2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (stillReading);
            chattingMode = true;
            stillReading = true;
            return (Player) result;
        }
    }

    public void writeToChatroom(String message) {
        out.println("chatroomMsg:" + message);
        out.flush();
    }
}