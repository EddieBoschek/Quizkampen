package POJOs;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Category implements Serializable {
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
    public static Category[] shuffleCategories(ArrayList<Category> categories) {
        Category[] returnList = new Category[categories.size()];
        Random random = new Random();
        int randomNumber;
        boolean inArray;

        for (int i = 0; i < returnList.length; i++) {
            do {
                randomNumber = random.nextInt(categories.size());
                inArray = false;
                for (Category c : returnList) {
                    if (categories.get(randomNumber) == c) {
                        inArray = true;
                        break;
                    }
                }
            } while (inArray);
            returnList[i] = categories.get(randomNumber);
        }
        return returnList;
    }
    public static Question[] getCategoryQuestions(String chosenCategory, ArrayList<Category> categories) {
        Question[] questions = new Question[3];
        Random r = new Random();
        int numberOfSubjects = categories.size();
        for (Category subject : categories) {
            if (subject.getSubjectName().equalsIgnoreCase(chosenCategory)) {
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