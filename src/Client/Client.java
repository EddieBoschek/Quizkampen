package Client;

import Server.Question;
import java.io.*;
import java.net.Socket;

public class Client {
    String hostName = "127.0.0.1";
    int portNumber = 12345;

    private Socket addressSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String hostName, int portNumber) throws IOException, ClassNotFoundException {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }
    /*public void sendMessage(String message) {
        try(Socket addressSocket = new Socket(hostName, portNumber);
            ObjectOutputStream out = new ObjectOutputStream(addressSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(addressSocket.getInputStream());){

            Object fromServer;
            String fromUser;
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            fromServer = in.readObject();

            if (fromServer instanceof Question question){
                int count = 1;
                for (String s : question.getAnswerOptions()) {
                    System.out.println(count + ": " + s);
                    count++;
                }
            }
            fromUser = stdIn.readLine();
            while (fromServer != null) {
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
    }*/
    public void connect() throws IOException {
        addressSocket = new Socket(hostName, portNumber);
        out = new ObjectOutputStream(addressSocket.getOutputStream());
        in = new ObjectInputStream(addressSocket.getInputStream());
    }

    public void sendMessage(String message) throws IOException {
        out.writeObject(message);
    }

    public Object receiveMessage() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void close() throws IOException {
        addressSocket.close();
    }
}

/*
Klient kopplar upp sig mot en server. Klienten laddar sedan ner en fråga
med 4 svarsalternativ från servern. Användaren svarar på frågan i klientapplikationen
och får feedback om svaret är rätt eller fel.
 */