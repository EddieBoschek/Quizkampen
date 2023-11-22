package POJOs;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String question;
    private List<String> answerOptions;
    private String correctAnswer;
    private int questionIndex;

    public Question(String question, List<String> answers, String correctAnswer) {
        this.question = question;
        this.answerOptions = answers;
        this.correctAnswer = correctAnswer;
        this.questionIndex = -1;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerOption(int i) {
        return answerOptions.get(i);
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
    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int index) {
        this.questionIndex = index;
    }
}
