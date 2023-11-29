package Server;

import POJOs.Category;
import POJOs.Question;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static POJOs.Category.getCategoryQuestions;
import static POJOs.Category.shuffleCategories;

public class GameInstance extends Thread {
    private boolean[][] gameScore = new boolean[5][3]; //5an ska sen ersättas med antal ronder
    private String[] gameCategories = new String[5]; //Samma här
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Properties p = new Properties();
    private boolean orderCheck;

    private DAO dao = new DAO();


    public GameInstance(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        currentPlayer = p1;
        this.player1.setOpponent(p2);
        this.player2.setOpponent(p1);
    }

    public void run() {
        try {
            p.load(new FileInputStream("src/Server/QuestionsRounds.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int questionsQuantity = Integer.parseInt(p.getProperty("questions", "2"));
        int roundsQuantity = Integer.parseInt(p.getProperty("rounds", "2"));

        Object inputLine;
        System.out.println("GameStart");

        // Skickar förfrågan till currentPlayer och tar emot data som sparas i GameInstance
        try {
            while (true) {
                if ((inputLine = currentPlayer.receive()) != null) {
                    if (inputLine.equals("Start")) {
                        System.out.println("Start");
                        try {
                            player1.send(player1.isCurrentPlayer());
                            player2.send(player2.isCurrentPlayer());
                            Category[] categoryOptions = shuffleCategories(dao.getCategories());
                            currentPlayer.send(categoryOptions);
                            currentPlayer.getOpponent().send(categoryOptions);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else if (inputLine.equals("Ny Runda")) {
                        System.out.println("Ny Runda");
                        try {
                            if (player1.isCurrentPlayer()) {
                                player1.setCurrentPlayer(false);
                                player2.setCurrentPlayer(true);
                                currentPlayer = player2;
                            } else {
                                player1.setCurrentPlayer(true);
                                player2.setCurrentPlayer(false);
                                currentPlayer = player1;
                            }
                            player1.send(player1.isCurrentPlayer());
                            player2.send(player2.isCurrentPlayer());
                            Category[] categoryOptions = shuffleCategories(dao.getCategories());
                            currentPlayer.send(categoryOptions);
                            currentPlayer.getOpponent().send(categoryOptions);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else { //Sends questions to currentPlayer, sends the picked subject and qustions to the other player
                        System.out.println("Inte Start");
                        Question[] q = getCategoryQuestions((String) inputLine, dao.getCategories());
                        currentPlayer.send(q);
                        currentPlayer.getOpponent().send((String) inputLine);
                        currentPlayer.getOpponent().send(q);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}