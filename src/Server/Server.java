package Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class Server extends Thread {
    Socket s;
    Category categories = new Category();
    Properties p = new Properties();
    public Server(Socket s) {
        this.s = s;
    }

    public void run() {

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
                if (inputLine.equals("Start")) {
                    out.writeObject(categories.shuffleCategories());
                    //out.reset();
                } else {
                    out.writeObject(QuestionCollection.getSubjectQuestion(inputLine));
                    //out.reset();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}