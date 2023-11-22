package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {

    public ServerListener() {

        try (ServerSocket ss = new ServerSocket(12345);) {
            while (true) {

                Socket s = ss.accept();
                Server server = new Server(s);
                server.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main (String[] args) {
        ServerListener sl = new ServerListener();
    }
}