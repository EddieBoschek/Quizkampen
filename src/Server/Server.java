package Server;

import POJOs.GameInstance;
import POJOs.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Thread {
    Socket s;
    Category categories = new Category();
    QuestionCollection questColl = new QuestionCollection("questColl");
    boolean gameNotStarted = true;
    GameInstance gI;

    public Server(Socket s) {
        this.s = s;
    }

    public void run() {

        try (
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {
            String inputLine;

            String playerName;
            while ((inputLine = (String) in.readObject()) != null) {
                while (gameNotStarted) {
                    if (inputLine.startsWith("READY")) {
                        playerName = inputLine.substring(5);
                        out.writeObject(playerName);

                    }
                }
                if (inputLine.equals("Start")) {
                    System.out.println("Start");
                    out.writeObject(categories.shuffleCategories());
                    //out.reset();
                } else {
                    System.out.println("Inte Start");
                    out.writeObject(questColl.getSubjectQuestion(inputLine));
                    //out.reset();
                }
            }


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}