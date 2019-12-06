package Phase3.AP;


import Phase3.AP.Threads.ServerThread;
import Phase3.AP.entities.mob.enemy.Enemy;
import Phase3.AP.gui.DeleteCheckBox;
import Phase3.AP.gui.GameListButton;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class GameServer implements Serializable {
    private static final long serialVersionUID = 3604960L;
    private transient ServerSocket serverSocket;
    private ArrayList<Game> games;
    private ArrayList<Socket> clients;
    private JFrame jFrame;
    private ArrayList<ServerThread> serverThreads;
    private ArrayList<DeleteCheckBox> gamesListDelete;
    private ArrayList<Class<? extends Enemy>> reflectedEnemyClasses ;
    private SimpleDateFormat sdf;
    private JPanel clientListPanel, gamesListPanel;
    private transient Map<Socket, GameClient> clientMap;

    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        games = new ArrayList<>();
        clients = new ArrayList<>();
        clientMap = new HashMap<>();
        serverThreads = new ArrayList<>();
        gamesListDelete = new ArrayList<>();
        reflectedEnemyClasses = new ArrayList<>();
        sdf = new SimpleDateFormat("HH:mm:ss");
        graphicsWorks();

    }

    private void graphicsWorks() {
        jFrame = new JFrame();
        jFrame.setTitle("Server Control Panel");
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        clientListPanel = new JPanel();
        gamesListPanel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel newGamePanel = new JPanel();
        newGamePanel.setLayout(new BorderLayout());

        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(4, 2));

        JLabel label = new JLabel("Width : ");
        jp.add(label);
        JTextField textField = new JTextField("20");

        jp.add(textField);


        JLabel label2 = new JLabel("Height : ");
        jp.add(label2);
        JTextField textField2 = new JTextField("20");
        jp.add(textField2);

        JLabel label3 = new JLabel("Enemy numbers : ");
        jp.add(label3);
        JTextField textField3 = new JTextField("2");
        jp.add(textField3);

        JLabel label4 = new JLabel("Max player numbers : ");
        jp.add(label4);
        JTextField textField4 = new JTextField("1");
        jp.add(textField4);

        newGamePanel.add(jp, BorderLayout.BEFORE_FIRST_LINE);

        JButton button = new JButton("Add game!!");
        button.addActionListener(e -> {
            String _width = textField.getText();
            boolean isValidWidth = true;
            String _height = textField2.getText();
            boolean isValidHeight = true;
            String enemyNumbers = textField3.getText();
            boolean isValidNumbers = true;
            String maxPlayerNumb = textField4.getText();
            boolean isValidPlayers = true;
            if (enemyNumbers.length() != 0) {
                for (int i = 0; i < enemyNumbers.length() && isValidNumbers; i++) {
                    if (enemyNumbers.charAt(i) < 48 || enemyNumbers.charAt(i) > 57)
                        isValidNumbers = false;
                }
            }
            if (maxPlayerNumb.length() != 0) {
                for (int i = 0; i < maxPlayerNumb.length() && isValidPlayers; i++) {
                    if (maxPlayerNumb.charAt(i) < 48 || maxPlayerNumb.charAt(i) > 57)
                        isValidPlayers = false;
                }
            }
            if (isValidPlayers && (maxPlayerNumb.length() == 0 || (Integer.parseInt(maxPlayerNumb)) < 1))
                isValidPlayers = false;
            if (_width != null && _height != null && _width.length() > 0
                    && _height.length() > 0 && _width.charAt(0) != 48 && _height.charAt(0) != 48) {
                int width, height, enemyNumber = -1, playerNumb;
                for (int i = 0; i < _width.length() && isValidWidth; i++) {
                    if (_width.charAt(i) < 48 || _width.charAt(i) > 57)
                        isValidWidth = false;
                }
                for (int i = 0; i < _height.length() && isValidWidth && isValidHeight; i++) {
                    if (_height.charAt(i) < 48 || _height.charAt(i) > 57)
                        isValidHeight = false;
                }
                if (isValidHeight && isValidWidth && isValidNumbers && isValidPlayers) {
                    width = (Integer.parseInt(_width));
                    height = (Integer.parseInt(_height));
                    playerNumb = (Integer.parseInt(maxPlayerNumb));
                    if (enemyNumbers.length() > 0)
                        enemyNumber = (Integer.parseInt(enemyNumbers));
                    addGame(new Game(width, height, enemyNumber, playerNumb , this));
                    JOptionPane.showMessageDialog(jFrame, "Your game has been added to the server list! \n " +
                            "It'll start as soon as a client gets in.");
                    textField.setText("");
                    textField2.setText("");
                    textField3.setText("");
                    textField4.setText("");

                } else {
                    JOptionPane.showMessageDialog(jFrame, "Please fill the fields using unsigned nonzero numbers!");
                }
            } else {
                JOptionPane.showMessageDialog(jFrame, "Please fill the fields using unsigned nonzero numbers!");

            }
        });
        newGamePanel.add(button, BorderLayout.AFTER_LAST_LINE);


        clientListPanel.setLayout(new BoxLayout(clientListPanel, BoxLayout.PAGE_AXIS));
        clientListPanel.add(new JLabel("Empty!"));
        JScrollPane js = new JScrollPane(clientListPanel);


        gamesListPanel.setLayout(new BoxLayout(gamesListPanel, BoxLayout.PAGE_AXIS));
        gamesListPanel.add(new JLabel("Empty!"));
        JScrollPane js2 = new JScrollPane(gamesListPanel);

        JPanel reflectPanel = new JPanel();
        JButton loadButton = new JButton("Load Enemy Class");
        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("out/production/Bomberman/Phase3/AP/entities/mob/enemy");
            fileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public String getDescription() {
                    return "Class Files (*.class)";
                }

                public boolean accept(File f) {
                    return !f.isDirectory() && (f.getName().toLowerCase().endsWith(".class"));
                }
            });
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChooser.getSelectedFile();
                    URLClassLoader classLoader = new URLClassLoader( new URL[]{selectedFile.getParentFile().toURI().toURL()});
                    reflectedEnemyClasses.add((Class<? extends Enemy>) classLoader.loadClass("Phase3.AP.entities.mob.enemy." +selectedFile.getName().substring(0,selectedFile.getName().indexOf('.'))));
                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        reflectPanel.add(loadButton);


        tabbedPane.addTab("New game declaration", newGamePanel);
        tabbedPane.addTab("Client List", js);
        tabbedPane.addTab("Game List", js2);
        tabbedPane.addTab("Load Enemy", reflectPanel);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        jFrame.add(tabbedPane, BorderLayout.CENTER);


        jFrame.setPreferredSize(new Dimension(500, 200));
        jFrame.pack();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

    }


     void waitForPlayer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket s = null;
                try {
                    s = serverSocket.accept();
                    Object object = null;
                    try {
                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                        object = ois.readObject();
                        ServerThread st =new ServerThread(this, (GameClient) object, s);
                        serverThreads.add(st);
                        st.start();
                        clientMap.put(s, (GameClient) object);
                        clients.add(s);
                        clientListUpdate();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (java.io.EOFException ex) {
                        System.out.println("got a temporary connection!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.removeIf(T -> !T.isConnected());
                for (Socket so : clientMap.keySet()) {
                    if (!clients.contains(so)) {
                        clientMap.remove(so);
                        System.out.println("disconnected socket found : " + so);
                    }
                }
            }
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clientListUpdate() {
        clientListPanel.removeAll();
        if (clients.size() == 0)
            clientListPanel.add(new JLabel("Empty!"));
        for (Socket s : clients) {
            clientListPanel.add(new JLabel(s.toString()));
        }
        clientListPanel.repaint();
    }

    private void gamesListUpdate() {
        gamesListPanel.removeAll();
        for (Game g : games) {
            JPanel z = new JPanel();
            DeleteCheckBox temp = new DeleteCheckBox(g);
            gamesListDelete.add(temp);
            z.add(temp);
            z.add(new GameListButton(g, g.getBoard(), games.indexOf(g) + 1));
            gamesListPanel.add(z);
        }
        JButton deleteButton = new JButton("Delete all marked");
        deleteButton.addActionListener(e -> {
            for (DeleteCheckBox dcb : gamesListDelete) {
                if (dcb.isSelected()) {
                    dcb.getGame().endGame();
                    games.remove(dcb.getGame());
                }
            }
            gamesListDelete.removeIf(AbstractButton::isSelected);
            gamesListUpdate();
        });
        gamesListPanel.add(deleteButton);
        gamesListPanel.repaint();
        gamesListPanel.revalidate();
    }


    public ArrayList<Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
        games.add(game);
        gamesListUpdate();
    }

    public ArrayList<Class<? extends Enemy>> getReflectedEnemyClasses() {
        return reflectedEnemyClasses;
    }

    public synchronized void broadcast(String message) {
        String time = sdf.format(new Date());
        String messageLf = time + " " + message + "\n";
        for(int i = serverThreads.size(); --i >= 0;) {
            ServerThread ct = serverThreads.get(i);
            if(!ct.writeMsg(messageLf)) {
                serverThreads.remove(i);
                System.out.println("Disconnected Client " + ct.getGameClient().getName() + " removed from list.");
            }
        }
    }

     void endGame(Game game) {
        games.remove(game);
        gamesListUpdate();
    }


}

//server chizi nafreste clienta action befrestan
