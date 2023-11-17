package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Server {

    protected String question = "Vilket år blev Gustav Vasa kung över Sverige?";
    protected List<String> answers = Arrays.asList("1609", "1462", "1523", "1789");
    protected String correctAnswer = "1523";

    public Server() {

        Question question1 = new Question(question, answers, correctAnswer);

        try(ServerSocket ss = new ServerSocket(12345);
            Socket s = ss.accept();
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());)
        {
            String inputLine;
            boolean result;
            out.writeObject(question1);

            //servar frågeloopen
            while ((inputLine = (String)in.readObject()) != null) {
                int number = (Integer.parseInt(inputLine)) - 1;
                result = question1.checkAnswer(question1.getAnswer(number));
                out.writeObject(result);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server serv = new Server();
    }
}