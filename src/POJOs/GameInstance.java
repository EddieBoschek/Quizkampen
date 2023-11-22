package POJOs;

import java.net.ServerSocket;
import java.util.Arrays;

public class GameInstance extends Thread{
    boolean[][] gameScore = new boolean[5][3]; //5an ska sen ersättas med antal ronder
    String[] gameCatergories = new String[5]; //Samma här
    Player[] players;

    public GameInstance(Player player1, Player player2){
        this.players = new Player[]{player1, player2};
    }
}
