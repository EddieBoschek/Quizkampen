package POJOs;

import POJOs.Category;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Properties;

import static POJOs.Category.getSubjectQuestion;
import static POJOs.Category.shuffleCategories;

public class GameInstance extends Thread {
    boolean[][] gameScore = new boolean[5][3]; //5an ska sen ersättas med antal ronder
    String[] gameCategories = new String[5]; //Samma här
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
//                (
//                ObjectOutputStream outp1 = new ObjectOutputStream(player1.socket.getOutputStream());
////                ObjectInputStream inp1 = new ObjectInputStream(player1.socket.getInputStream());
//                ObjectOutputStream outp2 = new ObjectOutputStream(player2.socket.getOutputStream());
////                ObjectInputStream inp2 = new ObjectInputStream(player2.socket.getInputStream());
//                ObjectOutputStream outCurr = new ObjectOutputStream(currentPlayer.socket.getOutputStream());
//                ObjectInputStream inCurr = new ObjectInputStream(currentPlayer.socket.getInputStream());) {

            //Start of game
//                while ((inputLine = inp1.readObject()) != null || (inputLine = inp2.readObject()) != null) {
            while (true) {
//                            if ((inputLine = player1.receive()) != null || (inputLine = player2.receive()) != null);
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

                    } else { //Sends questions to currentPlayer, sends the picked subject and qustions to the other player
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