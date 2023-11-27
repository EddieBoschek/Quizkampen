package Server;

import POJOs.Category;

import java.io.FileInputStream;
import POJOs.GameInstance;
import POJOs.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ArrayList;

public class Server extends Thread {
    Socket s;
    Properties p = new Properties();
    ArrayList<Server> threadList;
    PrintWriter output;
    Category categories = new Category();
    QuestionCollection questColl = new QuestionCollection("questColl");
    boolean gameNotStarted = true;
    GameInstance gI;

    public Server(Socket s) {
        this.s = s;
    }

    public Server(Socket socket, ArrayList<Server> threads) {
        this.s = socket;
        this.threadList = threads;
    }

    public void run() {

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

        try(
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());)
        {
            String inputLine;
            while ((inputLine = (String)in.readObject()) != null) {
        try (

                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {
//            output = new PrintWriter(s.getOutputStream(), true);
            Object inputLine;

            String text;
            while ((inputLine = in.readObject()) != null) {
                while (gameNotStarted) {
//                    if (inputLine.startsWith("READY")) {
//                        playerName = inputLine.substring(5);
                    if (inputLine instanceof String) {
                        text = (String) inputLine;
                        if (text.equals("sendGameInstance"))
                            out.writeObject(gI);
//                        else
//                            sendToBothClients(text);
                    }
                    if (inputLine instanceof GameInstance)
                        gI = (GameInstance) inputLine;

//                    }
//                    else if (inputLine.equals("BOTH_READY"))
//                        System.out.println("Spel startat.");
                    if (inputLine.equals("BOTH_PLAYERS_CONNECTED"))
                        gameNotStarted = false;
                }
                if (inputLine.equals("Start")) {
                    out.writeObject(Category.shuffleCategories(categories));
                    //out.reset();
                } else {
                    out.writeObject(Category.getSubjectQuestion(inputLine, categories));
                    System.out.println("Inte Start");
                    out.writeObject(questColl.getSubjectQuestion((String) inputLine));
                    //out.reset();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private void sendToBothClients(String outMessage) {
//        for (Server ser : threadList) {
//            ser.output.println(outMessage);
}