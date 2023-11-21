package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    String hostName = "127.0.0.1";
    int portNumber = 12345;

    private Socket addressSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String hostName, int portNumber) throws IOException {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.addressSocket = new Socket(hostName, portNumber);
        this.out = new ObjectOutputStream(addressSocket.getOutputStream());
        this.in = new ObjectInputStream(addressSocket.getInputStream());
    }

    public void connectAndSend(String message) throws IOException {
        System.out.println("Inside connectSend");
        out.writeObject(message);
    }

    public Object connectAndReceive() throws IOException, ClassNotFoundException {
        System.out.println("Inside connectReceive");
        Object obj = in.readObject();
        System.out.println("Inside connectReceive (2)");
        System.out.println(obj);
        return obj;
    }

    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (addressSocket != null) addressSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
Klient kopplar upp sig mot en server. Klienten laddar sedan ner en fråga
med 4 svarsalternativ från servern. Användaren svarar på frågan i klientapplikationen
och får feedback om svaret är rätt eller fel.
 */