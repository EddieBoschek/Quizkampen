package Client;

import POJOs.GameInstance;
import POJOs.Player;

import java.io.IOException;
import java.time.LocalDateTime;

public class Relocate {
    public static void startGame() throws IOException, ClassNotFoundException { //startas senare av en JButton
        Client client;
        client = new Client("127.0.0.1", 12345);
        LocalDateTime ldt = LocalDateTime.now();
        int getSecNumb = ldt.getSecond();
        Player player = new Player("spelare" + getSecNumb);


        client.connectAndSend("READY" + player.getName());
        Object recievedMessage;
        while (true) {
            recievedMessage = client.connectAndReceive();

            if (recievedMessage instanceof GameInstance)
                player.addGame((GameInstance) recievedMessage);
            else if (recievedMessage instanceof String && !player.getName().equals(recievedMessage))
                player.setOpponent(new Player((String) recievedMessage));

            if (player.getOpponent() != null && player.getGame() != null) {
                System.out.println("Båda spelare har anslutit.");
                break;
            }

        }
    }
}
