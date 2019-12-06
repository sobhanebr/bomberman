package Phase3.AP;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientRun2 {
    public static void main(String[] args) {
        try {
            System.out.println("LocalHost IP Address : " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        new GameClient();
    }
}