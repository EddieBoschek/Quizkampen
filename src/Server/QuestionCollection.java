package Server;

import java.util.*;

public class QuestionCollection {

    public static Question[] getSubjectQuestion(String chosenSubject) {
        ArrayList<ArrayList<Question>> subjectList = new ArrayList<>();
        ArrayList<Question> math = new ArrayList<>();
        ArrayList<Question> history = new ArrayList<>();
        ArrayList<Question> science = new ArrayList<>();

        subjectList.add(math);
        subjectList.add(history);
        subjectList.add(science);

        Question[] questions = new Question[3];
        Random r = new Random();
        int numberOfSubjects = subjectList.size();
        for (ArrayList<Question> subject : subjectList) {
            if (subject.toString().equals(chosenSubject)) {
                for (int i = 0; i < 3; i++) {
                    int randNumber = r.nextInt(1000);
                    int index = randNumber % numberOfSubjects;
                    Question qRand = subject.get(index);

                    if (questions[0].equals(qRand) || questions[1].equals(qRand) || questions[2].equals(qRand)) {
                        i--;
                    }
                    else{
                        questions[i] = qRand;
                    }
                }
            }
        }


        //Questions

        //Math
        List<String> answerOptions = Arrays.asList("-1", "0", "1", "-3");
        math.add(new Question("Vad är -1^3?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("19/6", "8", "17/2", "11/6");
        math.add(new Question("Vad är 2*4+3/6?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("21378", "21212", "40000", "98765");
        math.add(new Question("Vilket av följande tal är jämnt delbart med 3?", answerOptions, answerOptions.get(0)));

        //History
        answerOptions = Arrays.asList("1609", "1462", "1523", "1789");
        history.add(new Question("Vilket år blev Gustav Vasa kung över Sverige?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Tyskland", "Polen", "Frankrike", "Italien");
        history.add(new Question("I vilket land stormade de allierade stränderna i Normandie under andra världskriget?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("1904", "1908", "1910", "1914");
        history.add(new Question("När startade första världskriget?", answerOptions, answerOptions.get(3)));

        //Science
        answerOptions = Arrays.asList("2000", "2006", "2010", "2013");
        science.add(new Question("Vilket år slutade Pluto att klassificeras som en planet?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("0", "1", "2", "3");
        science.add(new Question("Hur många hjärtan har bläckfiskar?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("-40", "0", "70", "150");
        science.add(new Question("Vid vilket temperatur är Celcius och Farenheit lika?", answerOptions, answerOptions.get(0)));

        return questions;
    }

}
