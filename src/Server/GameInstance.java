package Server;

import POJOs.Category;
import POJOs.Question;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static POJOs.Category.getShuffledCategoryQuestions;
import static POJOs.Category.shuffleCategories;

public class GameInstance extends Thread {
    boolean[][] player1GameScore; //6an ska sen ersättas med antal ronder
    boolean[][] player2GameScore;
    private String[] gameCategories = new String[6]; //Samma här
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    int currentRound;
    private Properties p = new Properties();
    private boolean orderCheck;
    private DAO dao = new DAO();
    Category[] categoryOptions;
    Question[] q;
    String cat;
    boolean playerShiftHasBeenMade = false;
    boolean propSent = false;
    public GameInstance(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        currentPlayer = p1;
        this.player1.setOpponent(p2);
        this.player2.setOpponent(p1);
        System.out.println("New gameInstance");
    }
    public void updateScores(String winner) {
        if (winner.equals(player1.getName())) {
            player1.setScore(player1.getScore() + 1);
        } else if (winner.equals(player2.getName())) {
            player2.setScore(player2.getScore() + 1);
        }
    }

    public int getPlayer1Score() {
        return player1.getScore();
    }

    public int getPlayer2Score() {
        return player2.getScore();
    }

    public void run() {
        try {
            p.load(new FileInputStream("src/Server/QuestionsRounds.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] propArray = new int[2];
        propArray[0] = Integer.parseInt(p.getProperty("questions", "2"));
        propArray[1] = Integer.parseInt(p.getProperty("rounds", "2"));
        player1GameScore = new boolean[propArray[1]][propArray[0]];
        player2GameScore = new boolean[propArray[1]][propArray[0]];


        Object inputLine;


        // Skickar förfrågan till currentPlayer och tar emot data som sparas i GameInstance
        try {
            while (true) {
                if ((inputLine = currentPlayer.receive()) != null) {
                    if (inputLine.equals("PropertiesRequest") && !propSent) {
                        currentPlayer.send(propArray);
                        currentPlayer.getOpponent().send(propArray);
                        propSent = true;
                        System.out.println("Prop send");
                        System.out.println("Shift: " + playerShiftHasBeenMade);

                    } else if (((String) inputLine).startsWith("Start") && !playerShiftHasBeenMade) {
                        System.out.println((String) inputLine);
                        try {
                            int nmbr = Integer.parseInt(((String) inputLine).substring(5));
                            if (nmbr % 2 == 0) {
                                currentPlayer = player1;
                                player1.setCurrentPlayer(true);
                                player2.setCurrentPlayer(false);
                                System.out.println("Current player: 1");
                            } else {
                                player1.setCurrentPlayer(false);
                                player2.setCurrentPlayer(true);
                                currentPlayer = player2;
                                System.out.println("Current player: 2");
                            }
                            System.out.println("GameStart");
                            player1.send(player1.isCurrentPlayer());
                            player2.send(player2.isCurrentPlayer());
                            categoryOptions = shuffleCategories(dao.getCategories());
                            currentPlayer.send(categoryOptions);
                            currentPlayer.getOpponent().send(categoryOptions);
                            playerShiftHasBeenMade = true;
                            System.out.println("Categories sent");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else if (inputLine.equals("GameUpdateRequest")) {
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
                        currentPlayer.send(currentRound);
                        currentPlayer.send("END");

                    } else if (((String) inputLine).startsWith("GO")) {
                        currentPlayer.getOpponent().send(inputLine);
                        currentPlayer.getOpponent().send(q);
                        playerShiftHasBeenMade = false;

                    } else if (((String) inputLine).startsWith("P1")){
                        cat = ((String) inputLine).substring(2);
                        System.out.println("P1");
                        System.out.println(cat);
                        q = getShuffledCategoryQuestions(cat, dao.getCategories());
                        currentPlayer.send(q);
                        System.out.println("q sent");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}