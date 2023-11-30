package POJOs;

import java.io.*;
import java.net.Socket;

public class Player {
    private int score;
    private Socket socket;
    protected String name;
    private Player opponent; //Can make array (Player[]) for several games w different players;
    private boolean isCurrentPlayer;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Player(Socket s, String n, boolean isCurrentPlayer) throws IOException {

        ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(s.getInputStream());

        this.name = n;
        this.socket = s;
        this.isCurrentPlayer = isCurrentPlayer;
        this.output = output;
        this.input = input;
        this.score = 0;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
    }
    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
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
    public void setScore(int score) {
        this.score = score;
    }
    public int getScore() {
        return score;
    }
}
