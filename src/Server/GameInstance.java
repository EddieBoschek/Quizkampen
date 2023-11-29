package Server;

import POJOs.Category;
import POJOs.Question;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static POJOs.Category.getCategoryQuestions;
import static POJOs.Category.shuffleCategories;

public class GameInstance extends Thread {
    boolean[][] player1GameScore = new boolean[6][3]; //6an ska sen ersättas med antal ronder
    boolean[][] player2GameScore = new boolean[6][3];
    private String[] gameCategories = new String[6]; //Samma här
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    int currenRound;
    private Properties p = new Properties();
    private boolean orderCheck;

    private DAO dao = new DAO();
    String roundResult;
    boolean firstPlayerDone;


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
                    } else if (inputLine.equals("NewRound")) {
                        System.out.println("NewRound");
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
                    } else if (inputLine.equals("GameUpdateRequest")) {
                        currentPlayer.send(currentPlayer.name);
                        currentPlayer.send(currentPlayer.getOpponent().name);
                        if (currentPlayer == player1) {
                            currentPlayer.send(player1GameScore);
                            currentPlayer.send(player2GameScore);
                        } else if (currentPlayer == player2) {
                            currentPlayer.send(player2GameScore);
                            currentPlayer.send(player1GameScore);
                        }
                        currentPlayer.send(gameCategories);
                        currentPlayer.send(currenRound);
                        currentPlayer.send("END");
                    } else if (((String) inputLine).startsWith("RoundResult")) {
                        System.out.println(inputLine);
                        roundResult = "WakeUp" + inputLine.toString().substring(10);
                        System.out.println("wakeup");
                        currentPlayer.getOpponent().send(roundResult);
                        System.out.println(roundResult);
                        firstPlayerDone = true;


//                   } else if (inputLine instanceof boolean[][]) {
//
//                        player1GameScore = ((boolean[][]) inputLine);
//
//
                    } else {//Sends questions to currentPlayer, sends the picked subject and qustions to the other player
                        System.out.println("Inte Start:");
                        System.out.println(inputLine);
                        Question[] q = getCategoryQuestions((String) inputLine, dao.getCategories());
                        currentPlayer.send(q);
                        currentPlayer.getOpponent().send((String) inputLine);
                        currentPlayer.getOpponent().send(q);
                    }



                }
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}