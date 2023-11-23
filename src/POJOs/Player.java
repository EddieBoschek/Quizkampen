package POJOs;

public class Player {
    String name;
    Player opponent; //Can make array (Player[]) for several games w different players;


    public Player(String name){
        this.name = name;
    }

    public Player(){}

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
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
