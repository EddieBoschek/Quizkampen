package Server;

import POJOs.Question;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class QuestionCollection {
    private String subjectName;
    private ArrayList<Question> qList;

    public QuestionCollection(String subjectName, ArrayList<Question> qList) {
        this.subjectName = subjectName;
        this.qList = qList;
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
    public static Question[] getSubjectQuestion(String chosenSubject) {
        ArrayList<QuestionCollection> subjectList = new ArrayList<>();
        QuestionCollection math = new QuestionCollection("math", readDataFromFile("src/Server/TextFiles/Math"));
        QuestionCollection history = new QuestionCollection("history", readDataFromFile("src/Server/TextFiles/History"));
        QuestionCollection science = new QuestionCollection("science", readDataFromFile("src/Server/TextFiles/Science"));
        QuestionCollection music = new QuestionCollection("music", readDataFromFile("src/Server/TextFiles/Music"));
        QuestionCollection sports = new QuestionCollection("sports", readDataFromFile("src/Server/TextFiles/Sports"));
        QuestionCollection geography = new QuestionCollection("geography", readDataFromFile("src/Server/TextFiles/Geography"));

        subjectList.add(math);
        subjectList.add(history);
        subjectList.add(science);
        subjectList.add(music);
        subjectList.add(sports);
        subjectList.add(geography);

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
    public static ArrayList<Question> readDataFromFile(String readFromFile){
        Path inFilePath = Paths.get(readFromFile);
        String fileName = inFilePath.getFileName().toString();

        String question = "";
        String correctAnswer = "";
        String controller = "*";
        ArrayList<Question> questions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(readFromFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> answerOptions = new ArrayList<>();
                answerOptions.add(line);
                for (int i = 0; i < 3; i++) {
                    if ((line = br.readLine()) != null) {
                        answerOptions.add(line);
                    }
                }
                if ((line = br.readLine()) != null) {
                    question = line;
                }
                if ((line = br.readLine()) != null) {
                    correctAnswer = line;
                }
                if ((line = br.readLine()) != null && !line.equals(controller)) {
                    throw new IOException("The text formatting in the file " + fileName + " is incorrect.");
                }
                Question q = new Question(question, answerOptions, correctAnswer);
                questions.add(q);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("The file " + fileName + " could not be found.");
            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("Error occurred while reading from file " + fileName + ".");
            e.printStackTrace();
            System.exit(0);
        }
        catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
            System.exit(0);
        }
        return questions;
    }
}