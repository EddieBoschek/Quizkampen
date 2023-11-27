package POJOs;

import java.io.*;
import java.net.Socket;

public class Player {
    Socket socket;
    String name;
    Player opponent; //Can make array (Player[]) for several games w different players;
    boolean isCurrentPlayer;
    ObjectInputStream input;
    ObjectOutputStream output;

    public Player(Socket s, String n, boolean isCurrentPlayer) throws IOException {

        ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(s.getInputStream());

        this.name = n;
        this.socket = s;
        this.isCurrentPlayer = isCurrentPlayer;
        this.output = output;
        this.input = input;
    }



    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }


    public void send(Object o) throws IOException {
        output.writeObject(o);
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getOpponent() {
        return opponent;
    }
}
