package Phase3.AP;

import java.io.IOException;
import java.util.Scanner;

public class ServerRun {
    public static void main(String[] args)  {
        System.out.print("Enter the port to set the server on : ");
        GameServer server ;
        boolean validPort = false;
        while (!validPort) {
            try {
                server = new GameServer(new Scanner(System.in).nextInt());
                validPort = true;
                server.waitForPlayer();
            } catch (IOException e) {
                System.err.println(e.getMessage() + "\n This port is busy now.\nTry another port!");
            }
        }


//        GameServer server = null;
//        try {
//            server = new GameServer(9090);
//            server.waitForPlayer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
