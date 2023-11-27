package Server;

import POJOs.GameInstance;
import POJOs.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListener {

    public ServerListener() {
        ArrayList<Server> threadList = new ArrayList<>();
        try (ServerSocket ss = new ServerSocket(12345);) {
            while (true) {

                Player player1 = new Player(ss.accept(), "spelare1", true);
                Player player2 = new Player(ss.accept(), "spelare2", false);
                GameInstance game = new GameInstance(player1,player2);
                game.start(); //Startar upp en Gameinstance
                player1.send("START");
                player2.send("START");
//                Socket s = ss.accept();
//                Server server = new Server(s, threadList);
//                threadList.add(server);
//                server.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main (String[] args) {
        ServerListener sl = new ServerListener();
    }
}