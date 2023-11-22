package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Thread {
    Socket s;
    Category categories = new Category();
    public Server(Socket s) {
        this.s = s;
    }

    public void run() {

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