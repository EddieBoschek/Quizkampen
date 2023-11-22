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
        out.writeObject(message);
    }
    public Object connectAndReceive() throws IOException, ClassNotFoundException {
        return in.readObject();
    }
    public Object connectSendAndReceive(String message) throws IOException, ClassNotFoundException {
        out.writeObject(message);
        return in.readObject();
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