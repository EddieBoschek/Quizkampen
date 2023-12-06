package Server;
import java.io.*;
import java.net.Socket;
import java.util.Properties;
import POJOs.Category;
import POJOs.Question;
import static POJOs.Category.getShuffledCategoryQuestions;
import static POJOs.Category.shuffleCategories;

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
    private boolean[][] playerScore;
    private boolean roundDone;
    Object o;

    public Player(Socket s, String n, boolean isCurrentPlayer) throws IOException {

        ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(s.getInputStream());

        this.name = n;
        this.socket = s;
        this.isCurrentPlayer = isCurrentPlayer;
        this.output = output;
        this.input = input;
        this.score = 0;
        this.playerScore = null;
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

    public boolean[][] getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(boolean[][] playerScore) {
        this.playerScore = playerScore;
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
        playerScore = new boolean[propArray[1]][propArray[0]];

        Object inputLine;
        try {
            while (true) {
                System.out.println(isCurrentPlayer);
                if (isCurrentPlayer) {
                    if ((inputLine = receive()) != null) {
                        System.out.println("myTurn " + inputLine);
                        if (inputLine instanceof String && ((String) inputLine).startsWith("SettingUp")) {
                                name = ((String) inputLine).substring(9);
                                System.out.println("Name: " + name);
                                send(propArray);
                                System.out.println("MyTurn prop sent");
                                opponent.send(propArray);
                                System.out.println("!MyTurn prop sent");
                                opponent.send(name);
                                System.out.println("name to !MyTurn sent");
                                currentRound = 0;
                                opponent.currentRound = 0;
                                roundDone = false;
                                opponent.roundDone = false;
                                playerShiftHasBeenMade = false;


                        } else if (inputLine instanceof boolean[][]) {
                            setPlayerScore((boolean[][]) inputLine);
                            roundDone = true;
                        } else if (inputLine.equals("BothPlayersDone")) {
                            while (true) {
                                if (roundDone && opponent.roundDone) {
                                    send(true);
                                    break;
                                }
                            }
                        } else if (((String) inputLine).startsWith("Start")) {
                            System.out.println("förbi första if");
                                if (Integer.parseInt(((String) inputLine).substring(5)) == currentRound || !playerShiftHasBeenMade) {
                                    System.out.println("förbi andra if");
                                    try {
                                        currentRound = Integer.parseInt(((String) inputLine).substring(5));
                                        if (currentRound % 2 == 0) {
                                            isCurrentPlayer = true;
                                            opponent.isCurrentPlayer = false;
                                        } else {
                                            isCurrentPlayer = false;
                                            opponent.isCurrentPlayer = true;
                                        }
                                        send(isCurrentPlayer());
                                        opponent.send(opponent.isCurrentPlayer());
                                        categoryOptions = shuffleCategories(dao.getCategories());
                                        send(categoryOptions);
                                        opponent.send(categoryOptions);
                                        playerShiftHasBeenMade = true;
                                        System.out.println(name + " " + isCurrentPlayer);
                                        System.out.println(opponent.name + " " + opponent.isCurrentPlayer);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }

                                }

                        } else if (((String) inputLine).startsWith("GetNameRequest")) {
                            setName(((String) inputLine).substring(14));
                            opponent.send(getName());
                        } else if (((String) inputLine).startsWith("GO")) {
                            opponent.send(inputLine);
                            opponent.send(q);
                            playerShiftHasBeenMade = false;
                            currentRound++;
                            opponent.currentRound++;


                        } else if (((String) inputLine).startsWith("P1")) {
                            cat = ((String) inputLine).substring(2);
                            System.out.println("P1");
                            System.out.println(cat);
                            q = getShuffledCategoryQuestions(cat, dao.getCategories());
                            send(q);
                        } else {
                            o = inputLine;
                            System.out.println("rest" + o);
                        }
                    }
                } else {
                    if((inputLine = receive()) != null) {
                        System.out.println("!myTurn " + inputLine);
                        if (inputLine instanceof boolean[][]) {
                            setPlayerScore((boolean[][]) inputLine);
                            roundDone = true;
                        } else if (((String) inputLine).startsWith("SettingUp")) {
                            name = ((String) inputLine).substring(9);
                            System.out.println("Name: " + name);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            opponent.send(name);
                            System.out.println("name to MyTurn sent");
                        } else if (inputLine.equals("GameUpdateRequest")) {
                            updateCurrentPlayerBoard();
                            updateNonCurrentPlayerBoard();
                        } else if (((String) inputLine).startsWith("GetNameRequest")) {
                            opponent.setName(((String) inputLine).substring(14));
                            send(opponent.getName());
                        } else if (inputLine.equals("BothPlayersDone")) {
                            if (roundDone && opponent.roundDone) {
                                send(true);
                            } else {
                                send(false);
                            }

                        } else if (((String) inputLine).startsWith("P1")) {
                            cat = ((String) inputLine).substring(2);
                            System.out.println("P1");
                            System.out.println(cat);
                            q = getShuffledCategoryQuestions(cat, dao.getCategories());
                            send(q);
                        } else if (inputLine.equals("BothPlayersHaveAnsweredQuestions" + currentRound)) {
                            System.out.println(inputLine);
                            send(inputLine);
                            opponent.send(inputLine);
                        } else {
                            o = inputLine;
                            System.out.println("!rest " + o);
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


        opponent.send(opponent.getPlayerScore());
        opponent.send(getPlayerScore());

//        opponent.send(gameCategories);
        opponent.send("END");

    }

    public void updateCurrentPlayerBoard() throws IOException {
        send(name);
        send(opponent.name);

        send(getPlayerScore());
        send(opponent.getPlayerScore());

//        send(gameCategories);
        send("END");

    }
}
