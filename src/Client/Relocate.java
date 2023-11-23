package Client;

import POJOs.GameInstance;
import POJOs.Player;

import java.io.IOException;
import java.time.LocalDateTime;

public class Relocate {
    public static void startGame() throws IOException, ClassNotFoundException, InterruptedException { //startas senare av en JButton
        Client client;
        client = new Client("127.0.0.1", 12345);
        LocalDateTime ldt = LocalDateTime.now();
        int getSecNumb = ldt.getSecond();
        Player player = new Player("spelare" + getSecNumb);
        GameInstance gI = new GameInstance(player.getName());



        String recievedName;
        while (true) {
            client.connectAndSend("READY" + player.getName());
            Thread.sleep(100);
            recievedName = (String) client.connectAndReceive();

            if (!player.getName().equals(recievedName)) {
                player.setOpponent(new Player(recievedName));
                gI.addSecondPlayer(recievedName);


                System.out.println("Båda spelare har anslutit.");

                break;

            }

        }
    }
}
