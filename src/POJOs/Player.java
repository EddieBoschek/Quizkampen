package POJOs;

import java.io.*;
import java.net.Socket;

public class Player {
    Socket socket;
    String name;
    Player opponent;
    boolean isCurrentPlayer;
    ObjectInputStream input;
    ObjectOutputStream output;
    int score;
    int roundsWon;

    public Player(Socket s, String n, boolean isCurrentPlayer) throws IOException {

        ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(s.getInputStream());

        this.name = n;
        this.socket = s;
        this.isCurrentPlayer = isCurrentPlayer;
        this.output = output;
        this.input = input;
        this.score = 0;
        this.roundsWon = 0;
    }
    public void close() {
        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void incrementScore() {
        this.score++;
    }

    public void incrementRoundsWon() {
        this.roundsWon++;
    }

    public int getScore() {
        return score;
    }

    public int getRoundsWon() {
        return roundsWon;
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
