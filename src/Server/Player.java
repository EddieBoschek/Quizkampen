package Server;

import POJOs.Category;
import POJOs.Question;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

import static POJOs.Category.getShuffledCategoryQuestions;
import static POJOs.Category.shuffleCategories;

public class Player implements Runnable {
    private int score;
    private Socket socket;
    protected String name;
    private Player opponent; //Can make array (Player[]) for several games w different players;
    private boolean isCurrentPlayer;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    boolean[][] gameScore;
    Object inputLine;
    boolean playerShiftHasBeenMade;
    Category[] categoryOptions;
    private DAO dao = new DAO();
    int currentRound = 0;
    boolean startOfGame = true;
    Question[] q;
    String cat;
    private Properties p = new Properties();
    private String[] gameCategories = new String[6];

    public Player(Socket s, String n, boolean isCurrentPlayer) throws IOException {

        ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(s.getInputStream());

        this.name = n;
        this.socket = s;
        this.isCurrentPlayer = isCurrentPlayer;
        this.output = output;
        this.input = input;
        this.score = 0;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void send(Object o) throws IOException {
        output.writeObject(o);
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
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
        gameScore = new boolean[propArray[1]][propArray[0]];


        try {
            while (true) {
                if ((inputLine = receive()) != null) {
                    if (isCurrentPlayer) {
                        if (inputLine.equals("PropertiesRequest")) {
                            send(propArray);
                            opponent.send(propArray);

                        } else if (((String) inputLine).startsWith("Start") && !playerShiftHasBeenMade) {
                            System.out.println((String) inputLine);
                            try {
                                int nmbr = Integer.parseInt(((String) inputLine).substring(5));
                                if (nmbr % 2 == 0) {
                                    //gameInstance.currentPlayer = player1;
                                    isCurrentPlayer = true;
                                    opponent.isCurrentPlayer = false;
                                } else {
                                    //GameInstance.currentPlayer = player2;
                                    isCurrentPlayer = false;
                                    opponent.isCurrentPlayer = true;
                                }
                                send(isCurrentPlayer());
                                opponent.send(opponent.isCurrentPlayer());
                                categoryOptions = shuffleCategories(dao.getCategories());
                                send(categoryOptions);
                                opponent.send(categoryOptions);
                                playerShiftHasBeenMade = true;
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else if (inputLine.equals("GameUpdateRequest" + currentRound)) {
                            if (startOfGame) {
                                updateCurrentPlayerBoard();
                                updateNonCurrentPlayerBoard();
                            }
                            startOfGame = false;

                        } else if (((String) inputLine).startsWith("GetNameRequest")) {
                            setName(((String) inputLine).substring(14));
                            opponent.send(getName());
                        } else if (((String) inputLine).startsWith("GO")) {
                            opponent.send(inputLine);
                            opponent.getOpponent().send(q);
                            playerShiftHasBeenMade = false;
                            currentRound++;


                        } else if (((String) inputLine).startsWith("P1")) {
                            cat = ((String) inputLine).substring(2);
                            System.out.println("P1");
                            System.out.println(cat);
                            q = getShuffledCategoryQuestions(cat, dao.getCategories());
                            send(q);
                        }
//                        else if ((inputLine.equals("BothPlayersHaveAnsweredQuestions" + currentRound)) && startOfGame) { //|| currentRound == 1
//                            System.out.println(inputLine);
//                            send(inputLine);
//                            opponent.send(inputLine);
//                        }
                    } else {

                        if (inputLine.equals("GameUpdateRequest" + currentRound) && !startOfGame) {
                            updateNonCurrentPlayerBoard();
                            updateCurrentPlayerBoard();
                        } else if (((String) inputLine).startsWith("GetNameRequest")) {
                            opponent.setName(((String) inputLine).substring(14));
                            send(opponent.getName());
                        } else if (inputLine.equals("BothPlayersHaveAnsweredQuestions" + currentRound)) {
                            System.out.println(inputLine);
                            send(inputLine);
                            opponent.send(inputLine);
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateNonCurrentPlayerBoard() throws IOException {
        opponent.send(opponent.name);
        opponent.send(name);

        opponent.send(opponent.gameScore);
        opponent.send(gameScore);

        opponent.send(gameCategories);
        opponent.send("END");
    }

    public void updateCurrentPlayerBoard() throws IOException {
        send(name);
        send(opponent.name);

        send(gameScore);
        send(opponent.gameScore);

        send(gameCategories);
        send("END");

    }
}