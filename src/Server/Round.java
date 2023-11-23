package Server;

import POJOs.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Round extends JFrame {

    private JLabel questionLabel;
    private JButton option1, option2, option3, option4;

    private JLabel result;
    boolean correctAnswer;

    boolean answerSelected;

    Question[] round = new Question[2];

    int counter;


    public Round() throws InterruptedException {


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(questionLabel);
        add(option1);
        add(option2);
        add(option3);
        add(option4);
        add(result);
        setSize(600, 250);
        setLayout(new FlowLayout());


        counter = 0;

        while(counter < round.length) {

            answerSelected = false;

            List<String> tempList = round[counter].getAnswerOptions();

            questionLabel.setText(round[counter].getQuestion());
            option1.setText(tempList.get(0));
            option2.setText(tempList.get(1));
            option3.setText(tempList.get(2));
            option4.setText(tempList.get(3));
            result = new JLabel("Du svarade rätt!");
            result.setVisible(answerSelected);

            TimeUnit.SECONDS.sleep(1);
            counter++;

        }


    }


    ActionListener commonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == option1) {
                if (round[counter].checkAnswer(option1.getText())) {
                    answerSelected = true;
                } else {
                    result.setText("Du svarade fel.");
                    result.setVisible(true);
                }
            } else if (e.getSource() == option2) {
                if (round[counter].checkAnswer(option2.getText())) {
                    result.setVisible(true);
                } else {
                    result.setText("Du svarade fel.");
                    result.setVisible(true);
                }
            } else if (e.getSource() == option3) {
                if (round[counter].checkAnswer(option3.getText())) {
                    result.setVisible(true);
                } else {
                    result.setText("Du svarade fel.");
                    result.setVisible(true);
                }
            } else if (e.getSource() == option4) {
                if (round[counter].checkAnswer(option4.getText())) {
                    result.setVisible(true);
                } else {
                    result.setText("Du svarade fel.");
                    result.setVisible(true);
                }
            }
        }
    };
















    public static void main(String[] args) throws InterruptedException {Round round = new Round();}



}