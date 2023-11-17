package Server;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String question;
    private List<String> answerOptions;
    private String correctAnswer;

    public Question(String question, List<String> answers, String correctAnswer) {
        this.question = question;
        this.answerOptions = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }

    public String getAnswer(int number) {
        return answerOptions.get(number);
    }

    public boolean checkAnswer(String answer) {
        return this.correctAnswer.equals(answer);
    }
}
