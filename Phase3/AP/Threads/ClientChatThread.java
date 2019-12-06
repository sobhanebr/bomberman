package Phase3.AP.Threads;


import Phase3.AP.gui.ClientGUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientChatThread extends Thread {
    private Socket serverSocket;
    private ObjectInputStream inputStream;
    private PrintWriter out;
    private ClientGUI clientGUI;
    private Object result;

    public ClientChatThread(ClientGUI clientGUI, Socket serverSocket) throws IOException {
        this.clientGUI = clientGUI;
        this.serverSocket = serverSocket;
        inputStream = new ObjectInputStream(this.serverSocket.getInputStream());
        out = new PrintWriter(this.serverSocket.getOutputStream(), true);
        System.err.println("end of creating chatThread");
    }

    @Override
    public synchronized void run() {
        super.run();
        while (serverSocket.isConnected()) {
            try {
                result = inputStream.readObject();
                clientGUI.append((String) result);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.err.println("Connection got lost");
    }

    public void writeToChatroom(String message) {
        out.println("chatroomMsg:" + message);
        out.flush();
    }
}
