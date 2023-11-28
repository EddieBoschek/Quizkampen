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
    Question[] questions;
    int qcounter;
    boolean[][] gameresults;
    int roundCounter;
    boolean[] roundResults;
    int totalRounds;
    int totalQuestions;

    public GameInstance(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        currentPlayer = p1;
        this.player1.setOpponent(p2);
        this.player2.setOpponent(p1);
        this.gameresults = new boolean[5][3];
        this.roundCounter = 0;
        this.roundResults = new boolean[3];
        this.totalRounds = 5;
        this.totalQuestions = 3;
    }
    private void endGame() {
        try {

            player1.send("GAME OVER");
            player2.send("GAME OVER");


            player1.send(gameresults);
            player2.send(gameresults);

            player1.close();
            player2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void displayQuestion(Question question) {
        try {
            currentPlayer.send("DISPLAY QUESTION");
            currentPlayer.send(question.getQuestion());


            for (String option : question.getAnswerOptions()) {
                currentPlayer.send(option);
            }


            String playerAnswer = (String) currentPlayer.receive();


            boolean isCorrect = question.checkAnswer(playerAnswer);


            currentPlayer.send(isCorrect);


            roundResults[qcounter] = isCorrect;
            qcounter++;

            if (qcounter < totalQuestions) {

                displayQuestion(questions[qcounter]);
            } else {

                gameresults[roundCounter] = roundResults;
                roundCounter++;


                if (roundCounter < totalRounds) {

                    qcounter = 0;
                    playRound();
                    sendScoreToClients();
                } else {

                    endGame();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void playRound() {
        qcounter = 0;
        displayQuestion(questions[qcounter]);
    }
    private void sendScoreToClients() {
        try {
            player1.send("SCORE UPDATE");
            player1.send(player1.getScore());
            player1.send(player1.getRoundsWon());

            player2.send("SCORE UPDATE");
            player2.send(player2.getScore());
            player2.send(player2.getRoundsWon());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            try {
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
                    } else {
                        //Sends questions to currentPlayer, sends the picked subject and qustions to the other player
                        System.out.println("Inte Start");
                        Question[] q = getSubjectQuestion((String) inputLine, categories);
                        currentPlayer.send(q);
                        currentPlayer.getOpponent().send((String) inputLine);
                        currentPlayer.getOpponent().send(q);

                        if (questions[qcounter].checkAnswer((String) inputLine)) {
                            currentPlayer.incrementScore();
                            currentPlayer.send("CORRECT");
                        } else {
                            currentPlayer.send("WRONG");
                        }

                        qcounter++;

                        if (qcounter < questions.length) {

                            displayQuestion(questions[qcounter]);
                        } else {

                            gameresults[roundCounter] = roundResults.clone();
                            roundCounter++;


                            sendScoreToClients();


                            if (roundCounter < totalRounds) {

                                qcounter = 0;
                                playRound();
                            } else {

                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);

            }
        }
    }
}