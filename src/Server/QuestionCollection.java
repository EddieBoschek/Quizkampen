package Server;

import java.io.Serializable;
import java.util.*;
import java.io.Serializable;
import java.util.List;

public class QuestionCollection {
    private ArrayList<Question> qList = new ArrayList<>();
    private String subjectName;

    public QuestionCollection(String subjectName) {
        this.subjectName = subjectName;
    }

    public void addToList(Question q) {
        this.qList.add(q);
    }

    public Question getFromQList(int i) {
        return qList.get(i);
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Question[] getSubjectQuestion(String chosenSubject) {
        ArrayList<QuestionCollection> subjectList = new ArrayList<>();
        QuestionCollection math = new QuestionCollection("math");
        QuestionCollection history = new QuestionCollection("history");
        QuestionCollection science = new QuestionCollection("science");




        //Questions

        //Math
        List<String> answerOptions = Arrays.asList("-1", "0", "1", "-3");
        math.addToList(new Question("Vad är -1^3?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("19/6", "8", "17/2", "11/6");
        math.addToList(new Question("Vad är 2*4+3/6?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("21378", "21212", "40000", "98765");
        math.addToList(new Question("Vilket av följande tal är jämnt delbart med 3?", answerOptions, answerOptions.get(0)));

        //History
        answerOptions = Arrays.asList("1609", "1462", "1523", "1789");
        history.addToList(new Question("Vilket år blev Gustav Vasa kung över Sverige?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Tyskland", "Polen", "Frankrike", "Italien");
        history.addToList(new Question("I vilket land stormade de allierade stränderna i Normandie under andra världskriget?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("1904", "1908", "1910", "1914");
        history.addToList(new Question("När startade första världskriget?", answerOptions, answerOptions.get(3)));

        //Science
        answerOptions = Arrays.asList("2000", "2006", "2010", "2013");
        science.addToList(new Question("Vilket år slutade Pluto att klassificeras som en planet?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("0", "1", "2", "3");
        science.addToList(new Question("Hur många hjärtan har bläckfiskar?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("-40", "0", "70", "150");
        science.addToList(new Question("Vid vilket temperatur är Celcius och Farenheit lika?", answerOptions, answerOptions.get(0)));

        subjectList.add(math);
        subjectList.add(history);
        subjectList.add(science);

        Question[] questions = new Question[3];
        Random r = new Random();
        int numberOfSubjects = subjectList.size();
        for (QuestionCollection subject : subjectList) {
            if (subject.getSubjectName().equalsIgnoreCase(chosenSubject)) {
                for (int i = 0; i < 3; i++) {
                    int randNumber = r.nextInt(1000);
                    int index = randNumber % numberOfSubjects;
                    Question qRand = subject.getFromQList(index);

                    if (questions[0] != null && questions[0].equals(qRand) || questions[1] != null &&
                            questions[1].equals(qRand) || questions[2] != null && questions[2].equals(qRand)) {
                        i--;
                    }
                    else{
                        questions[i] = qRand;
                    }
                }
            }
        }
        return questions;
    }
}
