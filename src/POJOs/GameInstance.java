package POJOs;

import java.net.ServerSocket;
import java.util.Arrays;

public class GameInstance extends Thread {
    boolean[][] gameScore = new boolean[5][3]; //5an ska sen ersättas med antal ronder
    String[] gameCatergories = new String[5]; //Samma här
    Player[] players = new Player[2];

    public GameInstance(Player player1, Player player2) {
        this.players = new Player[]{player1, player2};
    }

    public GameInstance(String playerName) {
        this.players[0] = new Player(playerName);
    }

    public void addSecondPlayer(String playerName) {
        if (players[0] == null)
            players[0] = new Player(playerName);
        else if (!players[0].getName().equals(playerName))
            players[1] = new Player(playerName);
    }

    public boolean arePlayersConnected() {
        boolean connected = players[0] != null && players[1] != null;
        return connected;
    }
}
