package Client;

public class ScoreCounter {
    private int score;

    public ScoreCounter() {
        this.score = 0;
    }

    public void increaseScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }
}

