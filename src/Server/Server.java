package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Server extends Thread {

    protected String question = "Vilket år blev Gustav Vasa kung över Sverige?";
    protected List<String> answers = Arrays.asList("1609", "1462", "1523", "1789");
    protected String correctAnswer = "1523";

    Socket s;
    public Server(Socket s) {
        this.s = s;
    }

    public void run() {

        Question question1 = new Question(question, answers, correctAnswer);

        try(
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());)
        {

            String inputLine;
            boolean result;
            out.writeObject(question1);

            while ((inputLine = (String)in.readObject()) != null) {
                int number = (Integer.parseInt(inputLine)) - 1;
                result = question1.checkAnswer(question1.getAnswer(number));
                out.writeObject(result);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}