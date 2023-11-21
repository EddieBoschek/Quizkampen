package Server;
import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuizGUI {

    static Question askedQuest = null;
    private Client client;
    Object serverMessage;
    String[] categories;
    Question[] questions;

    public QuizGUI() throws IOException, ClassNotFoundException {
        JFrame frame = new JFrame("Quiz GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new FlowLayout());

        client = new Client("127.0.0.1", 12345);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(4, 1));

        serverMessage = client.receiveMessage();
        if (serverMessage instanceof String[] strings) {
            categories = strings;
        }

        JLabel categoryLabel = new JLabel("Välj en kategori");
        JButton categoryButton1 = new JButton(categories[0]);
        JButton categoryButton2 = new JButton(categories[1]);
        //JButton categoryButton3 = new JButton("Kategori 3");

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryButton1);
        categoryPanel.add(categoryButton2);
        //categoryPanel.add(categoryButton3);

        frame.getContentPane().add(categoryPanel);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(6, 1));

        JLabel questionLabel = new JLabel("(Fråga)");
        JButton questionButton1 = new JButton("Svar 1");
        JButton questionButton2 = new JButton("Svar 2");
        JButton questionButton3 = new JButton("Svar 3");
        JButton questionButton4 = new JButton("Svar 4");
        JLabel result = new JLabel("Du svarade rätt!");
        result.setVisible(false);

        ActionListener commonActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == questionButton1) {
                    if (askedQuest.checkAnswer(questionButton1.getText())) {
                        result.setVisible(true);
                    } else {
                        result.setText("Du svarade fel.");
                        result.setVisible(true);
                    }
                } else if (e.getSource() == questionButton2) {
                    if (askedQuest.checkAnswer(questionButton2.getText())) {
                        result.setVisible(true);
                    } else {
                        result.setText("Du svarade fel.");
                        result.setVisible(true);
                    }
                } else if (e.getSource() == questionButton3) {
                    if (askedQuest.checkAnswer(questionButton3.getText())) {
                        result.setVisible(true);
                    } else {
                        result.setText("Du svarade fel.");
                        result.setVisible(true);
                    }
                } else if (e.getSource() == questionButton4) {
                    if (askedQuest.checkAnswer(questionButton4.getText())) {
                        result.setVisible(true);
                    } else {
                        result.setText("Du svarade fel.");
                        result.setVisible(true);
                    }
                }
            }
        };

        questionButton1.addActionListener(commonActionListener);
        questionButton2.addActionListener(commonActionListener);
        questionButton3.addActionListener(commonActionListener);
        questionButton4.addActionListener(commonActionListener);

        questionPanel.add(questionLabel);
        questionPanel.add(questionButton1);
        questionPanel.add(questionButton2);
        questionPanel.add(questionButton3);
        questionPanel.add(questionButton4);
        questionPanel.add(result);

        QuestionCollection qCollection = new QuestionCollection("qCollection");
        //Question[] questions = new Question[3];


        categoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                try {
                    client.sendMessage(categoryButton1.getText());
                    serverMessage = client.receiveMessage();
                    if (serverMessage instanceof Question[] quests) {
                        questions = quests;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                askedQuest = questions[0];
                questionLabel.setText(questions[0].getQuestion());
                questionButton1.setText(questions[0].getAnswerOption(0));
                questionButton2.setText(questions[0].getAnswerOption(1));
                questionButton3.setText(questions[0].getAnswerOption(2));
                questionButton4.setText(questions[0].getAnswerOption(3));
                frame.getContentPane().add(questionPanel);
                frame.revalidate();
                frame.repaint();
                }
            });

        categoryButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                Question[] questions = qCollection.getSubjectQuestion(categoryButton2.getText());
                askedQuest = questions[0];
                questionLabel.setText(questions[0].getQuestion());
                questionButton1.setText(questions[0].getAnswerOption(0));
                questionButton2.setText(questions[0].getAnswerOption(1));
                questionButton3.setText(questions[0].getAnswerOption(2));
                questionButton4.setText(questions[0].getAnswerOption(3));
                frame.getContentPane().add(questionPanel);
                frame.revalidate();
                frame.repaint();
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
    private void sendMessageToServer(String message) {
        try {
            client.connect();
            client.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void receiveAndDisplayMessageFromServer() {
        try {
            client.connect();
            Object receivedMessage = client.receiveMessage();
            textArea.append("Servern svarade: " + receivedMessage + "\n");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                QuizGUI quizGUI = new QuizGUI();
                QuizGUI.setVisible(true);
            }
        });
    }
}
