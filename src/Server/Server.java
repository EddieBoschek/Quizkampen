package Server;

import POJOs.GameInstance;
import POJOs.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    Socket s;
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
                    System.out.println("Start");
                    out.writeObject(categories.shuffleCategories());
                    //out.reset();
                } else {
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



