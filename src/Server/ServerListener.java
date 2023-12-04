package Server;



import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener {

    public ServerListener() {
        try (ServerSocket ss = new ServerSocket(12345);) {
            while (true) {
                Player player1 = new Player(ss.accept(), "spelare1", true);
                Player player2 = new Player(ss.accept(), "spelare2", false);

                player1.setOpponent(player2);
                player2.setOpponent(player1);
                GameInstance game = new GameInstance(player1,player2);
//                game.start();
                Thread t1 = new Thread(player1);
                Thread t2 = new Thread(player2);
                t1.start();
                t2.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main (String[] args) {
        ServerListener sl = new ServerListener();
    }
}