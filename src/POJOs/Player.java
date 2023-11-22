package POJOs;

public class Player {
    String name;
    Player opponent; //Can make array (Player[]) for several games w different players;
    GameInstance game; //Can make array (GameInstance[]) for several games w different players;

    public Player(String name){
        this.name = name;
    }

    public Player(){}

    public String getName() {
        return name;
    }
}
