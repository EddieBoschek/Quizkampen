package Server;



import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener {

    public ServerListener() {
        try (ServerSocket ss = new ServerSocket(12345);) {
            while (true) {
                Player player1 = new Player(ss.accept(), "spelare1", true);
                Player player2 = new Player(ss.accept(), "spelare2", false);
                GameInstance game = new GameInstance(player1,player2);
                game.start();
                System.out.println("Thread started");
//                player1.send("START");
//                player2.send("START");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main (String[] args) {
        ServerListener sl = new ServerListener();
    }
}