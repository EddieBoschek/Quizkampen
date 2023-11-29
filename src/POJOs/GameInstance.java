package POJOs;

import POJOs.Category;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Properties;

import static POJOs.Category.getSubjectQuestion;
import static POJOs.Category.shuffleCategories;

public class GameInstance extends Thread {
    boolean[][] player1GameScore = new boolean[6][3]; //6an ska sen ersättas med antal ronder
    boolean[][] player2GameScore = new boolean[6][3]; //6an ska sen ersättas med antal ronder
    String[] gameCategories = new String[6]; //Samma här
    Player player1;
    Player player2;
    Player currentPlayer;
    Properties p = new Properties();

    public GameInstance(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        currentPlayer = p1;
        this.player1.setOpponent(p2);
        this.player2.setOpponent(p1);
    }

    public void run() {
        // Skickar förfrågan till currentPlayer och tar emot data som sparas i GameInstance

        ArrayList<Category> categories = new ArrayList<>();
        Category math = new Category("Math", Category.readDataFromFile("src/Server/TextFiles/Math"));
        Category history = new Category("History", Category.readDataFromFile("src/Server/TextFiles/History"));
        Category science = new Category("Science", Category.readDataFromFile("src/Server/TextFiles/Science"));
        Category music = new Category("Music", Category.readDataFromFile("src/Server/TextFiles/Music"));
        Category sports = new Category("Sports", Category.readDataFromFile("src/Server/TextFiles/Sports"));
        Category geography = new Category("Geography", Category.readDataFromFile("src/Server/TextFiles/Geography"));

        categories.add(math);
        categories.add(history);
        categories.add(science);
        categories.add(music);
        categories.add(sports);
        categories.add(geography);

        try {
            p.load(new FileInputStream("src/Server/QuestionsRounds.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int questionsQuantity = Integer.parseInt(p.getProperty("questions", "2"));
        int roundsQuantity = Integer.parseInt(p.getProperty("rounds", "2"));

        Object inputLine;
        System.out.println("GameStart");
        try {

            //Start of game

            while (true) {

                if ((inputLine = currentPlayer.receive()) != null) {

                    if (inputLine.equals("Start")) {
                        System.out.println("Start");
                        try {
                            player1.send(player1.isCurrentPlayer);
                            player2.send(player2.isCurrentPlayer);
                            Category[] sendBack = shuffleCategories(categories);
                            currentPlayer.send(sendBack);
                            currentPlayer.getOpponent().send(sendBack);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                    else if (inputLine == "GameUpdateRequest") {
                        currentPlayer.send(currentPlayer.name);
                        currentPlayer.send(currentPlayer.getOpponent().name);
                        if (currentPlayer == player1) {
                            currentPlayer.send(player1GameScore);
                            currentPlayer.send(player2GameScore);
                        }
                        else if (currentPlayer == player2) {
                            currentPlayer.send(player2GameScore);
                            currentPlayer.send(player1GameScore);
                        }
                        currentPlayer.send(gameCategories);
                        currentPlayer.send("END");

                        //Här borde currentPlayer byta spelare i serverdelen!!!
                    }


                    else { //Sends questions to currentPlayer, sends the picked subject and qustions to the other player
                        System.out.println("Inte Start");
                        Question[] q = getSubjectQuestion((String) inputLine, categories);
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