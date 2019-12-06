package Phase3.AP.gui;

import Phase3.AP.GameClient;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class AddressesListButton extends JButton {



    public AddressesListButton(GameClient gameClient, InetAddress inetAddress , int port) {
        super("- IP : " + inetAddress.toString().substring(1) + " , Port : " + port);
        this.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gameClient.setSocket(new Socket(inetAddress , port));
                    System.out.println("Connected to " + inetAddress + ":" + port + " successfully!");

                } catch (IOException e1) {
                    System.err.println("Error : " + e1.getMessage());
                }
            }
        });
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
    }

}
