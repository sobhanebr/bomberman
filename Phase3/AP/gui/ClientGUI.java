package Phase3.AP.gui;

import Phase3.AP.GameClient;
import javax.swing.*;
import java.awt.*;



public class ClientGUI extends JFrame  {

    private static final long serialVersionUID = 1L;
    private JLabel label;
    private JTextArea ta;
    private JTextField tf;
    private GameClient client;


    public ClientGUI(GameClient gameClient) {

        super("Chat Client");
        this.client = gameClient;
        JPanel northPanel = new JPanel(new GridLayout(1, 3));


        label = new JLabel("Enter your message", SwingConstants.CENTER);
        northPanel.add(label);
        tf = new JTextField();
        tf.setBackground(Color.WHITE);
        northPanel.add(tf);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> send(tf.getText()));
        northPanel.add(sendButton);
        add(northPanel, BorderLayout.NORTH);


        // The CenterPanel which is the chat room
        ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        centerPanel.add(new JScrollPane(ta));
        ta.setEditable(false);
        add(centerPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);

    }

    private void send(String msg){
        System.out.println("sending msg : " + msg);
        client.getClientThread().writeToChatroom(msg);
        tf.setText("");
    }

    public void append(String str) {
        ta.append(str);
        ta.setCaretPosition(ta.getText().length() - 1);
    }



}

