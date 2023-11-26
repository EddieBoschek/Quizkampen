package Client;

import POJOs.Category;
import POJOs.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class QuizGUI {

    Question askedQuest = null;
    private Client client;
    Object serverMessage;
    Category[] categories;
    Question[] questions = new Question[3];
    JLabel questionLabel, result;
    JButton questionButton1, questionButton2, questionButton3,questionButton4;
    JPanel questionPanel;
    JFrame frame;
    int counter;



    public boolean[] roundResults = new boolean[3];

    public QuizGUI() throws IOException, ClassNotFoundException {
        frame = new JFrame("Quiz GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 250);
        frame.setLayout(new FlowLayout());

        client = new Client("127.0.0.1", 12345);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(4, 1));

        sendMessageToServer("Start");
        serverMessage = receiveMessageFromServer();
        if (serverMessage instanceof Category[] c) {
            categories = c;
        }

        JLabel categoryLabel = new JLabel("Välj en kategori");
        JButton categoryButton1 = new JButton(categories[0].getSubjectName());
        JButton categoryButton2 = new JButton(categories[1].getSubjectName());
        //JButton categoryButton3 = new JButton(categories[2].getSubjectName());

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryButton1);
        categoryPanel.add(categoryButton2);
        //categoryPanel.add(categoryButton3);

        frame.getContentPane().add(categoryPanel);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(6, 1));

        questionLabel = new JLabel("(Fråga)");
        questionButton1 = new JButton("Svar 1");
        questionButton2 = new JButton("Svar 2");
        questionButton3 = new JButton("Svar 3");
        questionButton4 = new JButton("Svar 4");
        result = new JLabel("Du svarade rätt!");
        result.setVisible(false);






        categoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                serverMessage = sendAndReceive(categoryButton1.getText());
                if (serverMessage instanceof Question[] quests) {
                    questions = quests;
                    playRound(questions);
                }

            }
        });

        categoryButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                serverMessage = sendAndReceive(categoryButton2.getText());
                if (serverMessage instanceof Question[] quests) {
                    questions = quests;
                    playRound(questions);
                }

            }
        });

        /*categoryButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(questionPanel);
                frame.revalidate();
                frame.repaint();
            }
        });*/

        frame.setVisible(true);


    }

    public void chooseCategory() {

    }
    private void displayQuestion(Question question) {

        askedQuest = question;
        questionLabel.setText(askedQuest.getQuestion());
        questionButton1.setText(askedQuest.getAnswerOption(0));
        questionButton2.setText(askedQuest.getAnswerOption(1));
        questionButton3.setText(askedQuest.getAnswerOption(2));
        questionButton4.setText(askedQuest.getAnswerOption(3));

        for (ActionListener al : questionButton1.getActionListeners()) {
            questionButton1.removeActionListener(al);
        }
        for (ActionListener al : questionButton2.getActionListeners()) {
            questionButton2.removeActionListener(al);
        }
        for (ActionListener al : questionButton3.getActionListeners()) {
            questionButton3.removeActionListener(al);
        }
        for (ActionListener al : questionButton4.getActionListeners()) {
            questionButton4.removeActionListener(al);
        }

        ActionListener commonActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAnswer(((JButton) e.getSource()).getText());

                counter++;
                if (counter < questions.length) {
                    displayQuestion(questions[counter]);
                } else {
                    // Quiz round finished, perform necessary actions
                }
            }
        };

        questionButton1.addActionListener(commonActionListener);
        questionButton2.addActionListener(commonActionListener);
        questionButton3.addActionListener(commonActionListener);
        questionButton4.addActionListener(commonActionListener);

        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new GridLayout(6, 1));
        frame.getContentPane().add(questionLabel);
        frame.getContentPane().add(questionButton1);
        frame.getContentPane().add(questionButton2);
        frame.getContentPane().add(questionButton3);
        frame.getContentPane().add(questionButton4);
        frame.getContentPane().add(result);

        frame.revalidate();
        frame.repaint();

    }
    public void playRound(Question[] questions) {
        counter = 0;
        displayQuestion(questions[counter]);

    }
    private void handleAnswer(String answer) {
        if (questions[counter].checkAnswer(answer)) {

            result.setText("Du svarade rätt.");
            result.setVisible(true);

            roundResults[counter] = true;

        } else {

            result.setText("Du svarade fel.");
            result.setVisible(true);

            roundResults[counter] = false;

        }
    }

    public boolean[] getRoundResults() {
        return roundResults;
    }

    private void sendMessageToServer(String message) {
        try {
            client.connectAndSend(message);
            //client.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }
    private Object receiveMessageFromServer() {
        Object receivedMessage = null;
        try {
            receivedMessage = client.connectAndReceive();
            //receivedMessage = client.receiveMessage();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } /*finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return receivedMessage;
    }
    private Object sendAndReceive(String message) {
        Object receivedMessage = null;
        try {
            receivedMessage = client.connectSendAndReceive(message);
            //receivedMessage = client.receiveMessage();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } /*finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return receivedMessage;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    QuizGUI quizGUI = new QuizGUI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                //QuizGUI.setVisible(true);
            }
        });
    }
}