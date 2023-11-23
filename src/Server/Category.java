package Server;

import POJOs.Question;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Category {
    private String subjectName;
    private ArrayList<Question> qList;

    public Category(String subjectName, ArrayList<Question> qList) {
        this.subjectName = subjectName;
        this.qList = qList;
    }
    public Question getFromQList(int i) {
        return qList.get(i);
    }
    public String getSubjectName() {
        return subjectName;
    }
    public static String[] shuffleCategories(ArrayList<Category> categories) {
        //ArrayList<String> categories = new ArrayList<>(Arrays.asList("Math", "History", "Science", "Music", "Sports", "Geography"));
        String[] returnList = new String[categories.size()];
        Random random = new Random();
        int randomNumber;
        boolean inArray;

        for (int i = 0; i < returnList.length; i++) {
            do {
                randomNumber = random.nextInt(categories.size());
                inArray = false;
                for (String s : returnList) {
                    if (categories.get(randomNumber).getSubjectName().equals(s)) {
                        inArray = true;
                        break;
                    }
                }
            } while (inArray);
            returnList[i] = categories.get(randomNumber).getSubjectName();
        }
        return returnList;
    }
    public static Question[] getSubjectQuestion(String chosenSubject, ArrayList<Category> categories) {
        /*ArrayList<Category> categories = new ArrayList<>();
        Category math = new Category("Math", readDataFromFile("src/Server/TextFiles/Math"));
        Category history = new Category("History", readDataFromFile("src/Server/TextFiles/History"));
        Category science = new Category("Science", readDataFromFile("src/Server/TextFiles/Science"));
        Category music = new Category("Music", readDataFromFile("src/Server/TextFiles/Music"));
        Category sports = new Category("Sports", readDataFromFile("src/Server/TextFiles/Sports"));
        Category geography = new Category("Geography", readDataFromFile("src/Server/TextFiles/Geography"));

        subjectList.add(math);
        subjectList.add(history);
        subjectList.add(science);
        subjectList.add(music);
        subjectList.add(sports);
        subjectList.add(geography);*/

        Question[] questions = new Question[3];
        Random r = new Random();
        int numberOfSubjects = categories.size();
        for (Category subject : categories) {
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