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

    public Server(Socket s) {
        this.s = s;
    }

    public void run() {

        try (
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {
            String inputLine;
            Player player1 = new Player();
            Player player2 = new Player();
            String newPlayer;
            while ((inputLine = (String) in.readObject()) != null) {
                if (inputLine.startsWith("READY")) {
                    newPlayer = inputLine.substring(5);
                    if (player1.getName() == null)
                        player1 = new Player(newPlayer);
//                    else if (!player1.getName().equals(newPlayer)) {      //Fungerar bara om spelarna har olika namn
//                        player2 = new Player(newPlayer);
//                    }
                    else
                        player2 = new Player(newPlayer);
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

            if (player1.getName() != null && player2.getName() != null){
                GameInstance gI = new GameInstance(player1,player2);

                out.writeObject(gI); //sending gameInstance
                out.writeObject(player1.getName());
                out.writeObject(player2.getName());

            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}