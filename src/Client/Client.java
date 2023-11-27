package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    String hostName = "127.0.0.1";
    int portNumber = 12345;

    private Socket addressSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader input;

    public Client(String hostName, int portNumber) throws IOException, NullPointerException {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.addressSocket = new Socket(hostName, portNumber);
        this.out = new ObjectOutputStream(addressSocket.getOutputStream());
        out.flush();
        this.in = new ObjectInputStream(addressSocket.getInputStream());
        this.input = new BufferedReader(new InputStreamReader(addressSocket.getInputStream()));
    }
    public void connectAndSend(Object message) throws IOException {
        out.writeObject(message);
    }
    public Object connectAndReceive() throws IOException, ClassNotFoundException {
        return in.readObject();
    }
    public Object connectSendAndReceive(Object message) throws IOException, ClassNotFoundException, NullPointerException {
        out.writeObject(message);
        return in.readObject();
    }

    public String connectAndReceiveText() throws IOException {
        return input.readLine();
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