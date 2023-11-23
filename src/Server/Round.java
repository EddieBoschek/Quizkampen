package Server;


import POJOs.GameInstance;
import POJOs.Player;
import POJOs.Question;

import java.util.ArrayList;
import java.util.List;

public class Round {

    int questionsCreated = 0;

    Question tempQ;

    List<Question> roundQuestionsP1 = new ArrayList<>();

    List<Question> roundQuestionsP2 = new ArrayList<>();




    public Round(int qPerR, Player p1, Player p2, Category c) {

        while(questionsCreated < qPerR) {

            //tempQ = getQuestion(c);
            //getQuestion = metod för att hämta frågor, vet inte var den borde ligga


            roundQuestionsP1.add(tempQ);
            roundQuestionsP2.add(tempQ);

            questionsCreated++;
        }

    }

}