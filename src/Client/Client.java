package Client;

import Server.Question;
import Server.QuizGUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    Client() {
        String hostName = "127.0.0.1";
        int portNumber = 12345;

        try(Socket addressSocket = new Socket(hostName, portNumber);
            ObjectOutputStream out = new ObjectOutputStream(addressSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(addressSocket.getInputStream());){

            Object fromServer;
            String fromUser;
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            QuizGUI.GUI();

            fromServer = in.readObject();

            if (fromServer instanceof Question question){
                System.out.println(question.getQuestion());
                System.out.println();
                int count = 1;
                for (String s : question.getAnswerOptions()) {
                    System.out.println(count + ": " + s);
                    count++;
                }
                System.out.println();
                System.out.println("Ange 1, 2, 3 eller 4: ");
            }
            fromUser = stdIn.readLine();
            while (fromServer != null) {
                System.out.println("Du väljer svarsalternativ " + fromUser + ".");
                out.writeObject(fromUser);
                fromServer = in.readObject();
                if (fromServer instanceof Boolean bool) {
                    if (bool) {
                        System.out.println("Du svarade rätt!");
                        break;
                    } else {
                        System.out.println("Du svarade fel, testa igen: ");
                        fromUser = stdIn.readLine();
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        Client c = new Client();
    }

}

/*
Klient kopplar upp sig mot en server. Klienten laddar sedan ner en fråga
med 4 svarsalternativ från servern. Användaren svarar på frågan i klientapplikationen
och får feedback om svaret är rätt eller fel.
 */