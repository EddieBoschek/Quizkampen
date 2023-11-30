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
    Category[] categoryOptions;
    Question[] q;
    String cat;
    public GameInstance(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        currentPlayer = p1;
        this.player1.setOpponent(p2);
        this.player2.setOpponent(p1);
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
                            categoryOptions = shuffleCategories(dao.getCategories());
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
                            currentPlayer.getOpponent().send(currentPlayer.isCurrentPlayer());
                            categoryOptions = shuffleCategories(dao.getCategories());
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

                    } else if (((String) inputLine).startsWith("GO")) {


                        currentPlayer.getOpponent().send(inputLine);

                        currentPlayer.getOpponent().send(q);




                    } else if (((String) inputLine).startsWith("P1")){ //Sends questions to currentPlayer, sends the picked subject and qustions to the other player
                        cat = ((String) inputLine).substring(2);
                        System.out.println("P1");
                        System.out.println(cat);
                        q = getCategoryQuestions(cat, dao.getCategories());
                        currentPlayer.send(q);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}