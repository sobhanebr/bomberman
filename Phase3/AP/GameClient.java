package Phase3.AP;

import Phase3.AP.Threads.ClientThread;
import Phase3.AP.Threads.MainPlayerThread;
import Phase3.AP.Threads.TimerThread;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.gui.*;
import Phase3.AP.input.Keyboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

import static javax.swing.SwingConstants.CENTER;

public class GameClient implements Serializable {
    private static final long serialVersionUID = 1734613937973604960L;
    private transient Socket socket = null;
    private String name;
    private Game theGamePlayingIn = null;
    private JComboBox<String> c1;
    private JComboBox<String> c2;
    private ClientThread clientThread;
    private DefaultComboBoxModel<String> model1, model2;
    private final String[] ip_Range = new String[255];
    private MyJTextField ip1, ip2, ip3, portTextfield;
    private JFrame buttonsFrame, connectFrame, chooseGameFrame;
    private JPanel gamesListPanel;
    private ArrayList<Game> games;
    private GameFrame gameFrame;
    private boolean isViewer;
    private GamePanel gamePanel;
    private ClientGUI clientGUI;
    private Player meAsPlayer;
    private String myPicturePath;
    private InfoFrame infoFrame;

    public GameClient() {
        initialize();
    }

    private void connect() {

        for (int i = 0; i < 255; i++) {
            ip_Range[i] = String.valueOf(i + 1);
        }
        connectFrame = new JFrame();
        JPanel serverFinderPanel = new JPanel();
        JPanel directConnect = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();


        serverFinderPanel.setLayout(new BoxLayout(serverFinderPanel, BoxLayout.PAGE_AXIS));

        JPanel ipPanel = new JPanel();
        ipPanel.setLayout(new BoxLayout(ipPanel, BoxLayout.LINE_AXIS));

        ip1 = new MyJTextField(3);
        ip1.setHorizontalAlignment(CENTER);

        ip2 = new MyJTextField(3);
        ip2.setHorizontalAlignment(CENTER);

        ip3 = new MyJTextField(3);
        ip3.setHorizontalAlignment(CENTER);

        ipPanel.add(new JLabel("IP : "));
        ipPanel.add(ip1);
        ipPanel.add(new JLabel("."));
        ipPanel.add(ip2);
        ipPanel.add(new JLabel("."));
        ipPanel.add(ip3);

        model1 = new DefaultComboBoxModel<>(ip_Range);
        model2 = new DefaultComboBoxModel<>(ip_Range);


        JPanel ipRange = new JPanel();
        c1 = new JComboBox<>(model1);
        c1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int temp = Integer.valueOf((String) Objects.requireNonNull(c1.getSelectedItem()));
                for (int i = 255; i > 0; i--) {
                    if (i < temp)
                        model2.removeElement(String.valueOf(i));
                    else if (model2.getIndexOf(String.valueOf(i)) == -1)
                        model2.addElement(String.valueOf(i));
                }

                c1.transferFocus();
            }
        });
        c2 = new JComboBox<>(model2);
        c2.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int temp = Integer.valueOf((String) Objects.requireNonNull(c2.getSelectedItem()));
                for (int i = 1; i < 256; i++) {
                    if (i > temp)
                        model1.removeElement(String.valueOf(i));
                    else if (model1.getIndexOf(String.valueOf(i)) == -1)
                        model1.addElement(String.valueOf(i));
                }
                c2.transferFocus();
            }
        });

        ipRange.add(new JLabel("IP Range : "));
        ipRange.add(c1);
        ipRange.add(new JLabel("up to"));
        ipRange.add(c2);

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new BoxLayout(portPanel, BoxLayout.X_AXIS));
        portTextfield = new MyJTextField(5);
        portTextfield.setHorizontalAlignment(CENTER);
        portPanel.add(new JLabel("Port : "));
        portPanel.add(portTextfield);


        JButton findButton = new JButton("Find");
        findButton.addActionListener(e -> {
            ArrayList<InetAddress> addresses = new ArrayList<>();
            if (ip1.getText().length() > 0
                    && ip2.getText().length() > 0 && ip3.getText().length() > 0
                    && portTextfield.getText().length() > 0
                    && Integer.parseInt(portTextfield.getText()) > 1023
                    && Integer.parseInt(portTextfield.getText()) < 65536) {
                String subnet = ip1.getText() + "." + ip2.getText() + "." + ip3.getText();
                for (int i = Integer.parseInt((String) Objects.requireNonNull(c1.getSelectedItem())); i <= Integer.parseInt((String) Objects.requireNonNull(c2.getSelectedItem())); i++) {
                    String host = subnet + "." + i;
                    try {
                        InetAddress ip = InetAddress.getByName(host);
                        try (Socket ignored = new Socket(ip, Integer.parseInt(portTextfield.getText()))) {
                            addresses.add(ip);
                        } catch (IOException ignored) {
                        }
                    } catch (UnknownHostException e1) {
                        System.err.println(e1.getMessage());
                    }
                }
                buttonsFrame = new JFrame("Servers List");
                buttonsFrame.setLocationRelativeTo(null);
                buttonsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                buttonsFrame.setPreferredSize(new Dimension(500, 200));
                buttonsFrame.pack();
                JPanel cp = (JPanel) buttonsFrame.getContentPane();
                JPanel temp = new JPanel();
                temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
                if (addresses.size() == 0)
                    temp.add(new JLabel("Empty"));
                else {
                    for (InetAddress address : addresses) {
                        temp.add(new AddressesListButton(this, address, Integer.parseInt(portTextfield.getText())));
                    }
                }
                JScrollPane js = new JScrollPane(temp);
                cp.add(js);
                buttonsFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(connectFrame, "Please fill the fields first!");
            }
        });


        serverFinderPanel.add(ipPanel);
        serverFinderPanel.add(ipRange);
        serverFinderPanel.add(portPanel);
        serverFinderPanel.add(findButton);


        directConnect.setLayout(new BoxLayout(directConnect, BoxLayout.Y_AXIS));
        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(2, 2));
        temp.add(new JLabel("IP : "));
        JFormattedTextField ipTextField = new JFormattedTextField();
        ipTextField.setHorizontalAlignment(CENTER);
        ipTextField.setText("127.0.0.1");
        temp.add(ipTextField);
        temp.add(new JLabel("Port : "));
        MyJTextField portTextField = new MyJTextField(5);
        portTextField.setHorizontalAlignment(CENTER);
        portTextField.setText("9090");
        temp.add(portTextField);
        directConnect.add(temp);

        JButton connectButton = new JButton("connect");
        connectButton.addActionListener(e -> {
            if (ipTextField.getText().length() > 0 && portTextField.getText().length() > 0
                    && Integer.parseInt(portTextField.getText()) > 1023 && Integer.parseInt(portTextField.getText()) < 65536) {
                boolean validation = true;
                String[] ip = new String[4];
                ip[0] = ipTextField.getText().substring(0, ipTextField.getText().indexOf('.'));
                ip[1] = ipTextField.getText().substring(ip[0].length() + 1, ipTextField.getText().substring(ip[0].length() + 1).indexOf('.') + ip[0].length() + 1);
                ip[2] = ipTextField.getText().substring(ip[0].length() + ip[1].length() + 2, ipTextField.getText().substring(ip[0].length() + ip[1].length() + 2).indexOf('.') + ip[0].length() + ip[1].length() + 2);
                ip[3] = ipTextField.getText().substring(ip[0].length() + ip[1].length() + +ip[2].length() + 3);

                for (int i = 0; i < 4 && validation; i++) {
                    for (int j = 0; j < ip[i].length() && validation; j++) {
                        if (ip[i].charAt(j) < 48 || ip[i].charAt(j) > 57)
                            validation = false;
                    }
                }
                if (validation) {
                    try {
                        InetAddress ip2 = InetAddress.getByName(ipTextField.getText());
                        try {
                            setSocket(new Socket(ip2, Integer.parseInt(portTextField.getText())));
                            System.out.println("Connected to " + ip2 + ":" + Integer.parseInt(portTextField.getText()) + " successfully!");
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(connectFrame, "Error : " + e1.getMessage() + "\n"
                                    + "Try connecting through another IP address , please.");
                        }
                    } catch (UnknownHostException e1) {
                        JOptionPane.showMessageDialog(connectFrame, "Error : " + e1.getMessage() + "\n"
                                + "Enter a valid IP address , please.");
                    }
                } else
                    JOptionPane.showMessageDialog(connectFrame, "Please correct the fields first!");
            } else
                JOptionPane.showMessageDialog(connectFrame, "Please correct the fields first!");
        });
        directConnect.add(connectButton);


        tabbedPane.addTab("Server Finder", serverFinderPanel);
        tabbedPane.addTab("Connecting directly", directConnect);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        connectFrame.add(tabbedPane, BorderLayout.CENTER);

        connectFrame.setTitle("Server Connector");
        connectFrame.setResizable(false);
        connectFrame.setLocationRelativeTo(null);
        connectFrame.setPreferredSize(new Dimension(350, 200));
        connectFrame.pack();
        connectFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        connectFrame.setVisible(true);

    }

    private void initialize() {
        JFrame initFrame = new JFrame("Init");
        JPanel cp = (JPanel) initFrame.getContentPane();
        cp.setLayout(new GridLayout(2, 2));

        myPicturePath = ("src\\images\\Creep_F_f00.png");

        cp.add(new JLabel("Name "));
        JTextField clientNameTextField = new JTextField("Sobhan");
        cp.add(clientNameTextField);
        JButton loadPictureButton = new JButton("Load Picture");
        loadPictureButton.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser("src\\images");
            fileChooser.setSelectedFile(new File("src\\images\\Creep_F_f00.png"));
            fileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public String getDescription() {
                    return "Image Files (*.jpg , .png , .jpeg)";
                }

                public boolean accept(File f) {
                    return !f.isDirectory() && (f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpeg"));
                }
            });
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                myPicturePath = selectedFile.getPath();
            }

        });
        cp.add(loadPictureButton);
        JButton readyButton = new JButton("Ready to start");
        readyButton.addActionListener(e -> {
            name = clientNameTextField.getText();
            initFrame.dispose();
            connect();
        });
        cp.add(readyButton);

        initFrame.setPreferredSize(new Dimension(300, 120));
        initFrame.pack();
        initFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initFrame.setLocationRelativeTo(null);
        initFrame.setResizable(false);
        initFrame.setVisible(true);
    }

    public void setSocket(Socket socket) {
        if (this.socket == null) {
            if (buttonsFrame != null && (buttonsFrame.isActive() || buttonsFrame.isShowing()))
                buttonsFrame.dispose();
            if (connectFrame != null && (connectFrame.isActive() || connectFrame.isShowing()))
                connectFrame.dispose();
        }
        this.socket = socket;
        try {
            ObjectOutputStream obj = new ObjectOutputStream(socket.getOutputStream());
            obj.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clientThread = new ClientThread(this, socket);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showConnectedFrame();
    }

    private void showConnectedFrame() {
        chooseGameFrame = new JFrame("Choose Game");
        System.out.println("Getting server information...");
        games = clientThread.askForGameList();
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

        JButton button = new JButton("Request Game!");
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
                    clientThread.newGameRequest(width, height, enemyNumber, playerNumb);
                    System.out.println("Your Game request has been sent.");
                    textField.setText("");
                    textField2.setText("");
                    textField3.setText("");
                    textField4.setText("");

                } else {
                    JOptionPane.showMessageDialog(chooseGameFrame, "Please fill the fields using unsigned nonzero numbers!");
                }
            } else {
                JOptionPane.showMessageDialog(chooseGameFrame, "Please fill the fields using unsigned nonzero numbers!");
            }
        });
        newGamePanel.add(button, BorderLayout.AFTER_LAST_LINE);


        gamesListPanel.setLayout(new BoxLayout(gamesListPanel, BoxLayout.Y_AXIS));

        if (games.size() == 0)
            gamesListPanel.add(new JLabel("Empty!"));
        else {
            for (Game g : games) {
                gamesListPanel.add(new GamePlayListButton(g, this));
            }
        }
        gamesListPanel.add(new RefreshButton());
        JScrollPane js2 = new JScrollPane(gamesListPanel);

        tabbedPane.addTab("New game declaration", newGamePanel);
        tabbedPane.addTab("Game List", js2);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        chooseGameFrame.add(tabbedPane, BorderLayout.CENTER);

        chooseGameFrame.setPreferredSize(new Dimension(350, 170));
        chooseGameFrame.pack();
        chooseGameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chooseGameFrame.setLocationRelativeTo(null);
        chooseGameFrame.setResizable(false);
        chooseGameFrame.setVisible(true);
    }

    public String getName() {
        return name;
    }

    public void setGame(Game theGamePlayingIn) {
        if (!isViewer) {
            if (theGamePlayingIn.getCapacity() > 0) {
                this.theGamePlayingIn = theGamePlayingIn;
                meAsPlayer = clientThread.addPlayer(games.indexOf(theGamePlayingIn));
                chooseGameFrame.dispose();
            } else
                JOptionPane.showMessageDialog(chooseGameFrame, "You can't join this game .\n Please select another game .");
        } else {
            GameViewerPanel gameViewerPanel = new GameViewerPanel(theGamePlayingIn, theGamePlayingIn.getBoard());
            gameFrame = new GameFrame(theGamePlayingIn, theGamePlayingIn.getBoard());
            gameFrame.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                        int t = gameViewerPanel.getbY() - 1;
                        if (t >= 0 && Math.min(t + 20, theGamePlayingIn.getHeight()) - t >= gameViewerPanel.gethLen())
                            gameViewerPanel.setbY(gameViewerPanel.getbY() - 1);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
                        int t = gameViewerPanel.getbY() + 1;
                        if (Math.min(t + 20, theGamePlayingIn.getHeight()) - t >= gameViewerPanel.gethLen())
                            gameViewerPanel.setbY(gameViewerPanel.getbY() + 1);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                        int t = gameViewerPanel.getbX() + 1;
                        if (Math.min(t + 20, theGamePlayingIn.getWidth()) - t >= gameViewerPanel.getwLen())
                            gameViewerPanel.setbX(gameViewerPanel.getbX() + 1);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                        int t = gameViewerPanel.getbX() - 1;
                        if (t >= 0 && Math.min(t + 20, theGamePlayingIn.getWidth()) - t >= gameViewerPanel.getwLen())
                            gameViewerPanel.setbX(gameViewerPanel.getbX() - 1);
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

            gameFrame.add(gameViewerPanel, BorderLayout.CENTER);
        }
    }

    private void refreshGameList() {
        gamesListPanel.removeAll();
        games = clientThread.askForGameList();
        if (games.size() == 0)
            gamesListPanel.add(new JLabel("Empty!"));
        else {
            for (Game g : games)
                gamesListPanel.add(new GamePlayListButton(g, this));
        }
        gamesListPanel.add(new RefreshButton());
        gamesListPanel.repaint();
    }

    public Game getGame() {
        return theGamePlayingIn;
    }

    public ClientThread getClientThread() {
//        System.err.println(clientThread);
        return clientThread;
    }


    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getMyPicturePath() {
        return myPicturePath;
    }

    public void setViewer(boolean viewer) {
        isViewer = viewer;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public ClientGUI getClientGUI() {
        return clientGUI;
    }

    public void setClientGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    public InfoFrame getInfoFrame() {
        return infoFrame;
    }

    public void setInfoFrame(InfoFrame infoFrame) {
        this.infoFrame = infoFrame;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    class MyJTextField extends JTextField {
        private int limit;

        public MyJTextField(int limit) {
            super();
            this.limit = limit;
            setPreferredSize(new Dimension(50, 20));
        }

        @Override
        protected Document createDefaultModel() {
            return new LimitDocument();
        }

        private class LimitDocument extends PlainDocument {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null) return;
                boolean isValid = true;
                for (int i = 0; i < str.length() && isValid; i++) {
                    if (str.charAt(i) < 48 || str.charAt(i) > 57)
                        isValid = false;
                }
                if ((getLength() + str.length()) <= limit && isValid) {
                    super.insertString(offset, str, attr);
                    if (getLength() == limit)
                        transferFocus();
                }
            }
        }
    }

    class RefreshButton extends JButton {
        RefreshButton() {
            super("Refresh");
            setPreferredSize(new Dimension(0, 0));
            addActionListener(e -> refreshGameList());
        }

    }


}
