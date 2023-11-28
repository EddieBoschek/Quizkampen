package Server;

import POJOs.Category;

import java.io.FileInputStream;
import POJOs.GameInstance;
import POJOs.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import static POJOs.Category.getSubjectQuestion;

public class Server extends Thread {
    Socket s;
    ArrayList<Server> threadList;
    PrintWriter output;
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

        //String inputLine;
        //while ((inputLine = (String)in.readObject()) != null) {
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
                /*if (inputLine.equals("Start")) {
                    out.writeObject(Category.shuffleCategories(categories));
                    //out.reset();
                } else {
                    out.writeObject(getSubjectQuestion((String) inputLine, categories));
                    System.out.println("Inte Start");
                    out.writeObject(getSubjectQuestion((String) inputLine, categories));
                    //out.reset();
                }*/
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private void sendToBothClients(String outMessage) {
//        for (Server ser : threadList) {
//            ser.output.println(outMessage);
}