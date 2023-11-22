package Client;

import POJOs.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuizGUI {

    private Client client;
    private ScoreCounter scoreCounter;
    private Question askedQuest;
    private Object serverMessage;
    private String[] categories;
    private Question[] questions = new Question[3];
    private JLabel result;
    private JLabel questionLabel;  // Added declaration for 'questionLabel'
    private JButton questionButton1;
    private JButton questionButton2;
    private JButton questionButton3;
    private JButton questionButton4;

    public QuizGUI() throws IOException, ClassNotFoundException {
        JFrame frame = new JFrame("Quiz GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 250);
        frame.setLayout(new FlowLayout());

        client = new Client("127.0.0.1", 12345);
        scoreCounter = new ScoreCounter();

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(4, 1));

        sendMessageToServer("Start");
        serverMessage = receiveMessageFromServer();
        categories = (String[]) serverMessage;

        JLabel categoryLabel = new JLabel("Välj en kategori");
        JButton categoryButton1 = new JButton(categories[0]);
        JButton categoryButton2 = new JButton(categories[1]);

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryButton1);
        categoryPanel.add(categoryButton2);

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
                handleAnswer(((JButton) e.getSource()).getText());
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

        categoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                serverMessage = sendAndReceive(categoryButton1.getText());
                if (serverMessage instanceof Question[] quests) {
                    questions = quests;
                }
                askedQuest = questions[0];
                setNextQuestion();
                frame.getContentPane().add(questionPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        categoryButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                serverMessage = sendAndReceive(categoryButton2.getText());
                if (serverMessage instanceof Question[] quests) {
                    questions = quests;
                }
                askedQuest = questions[0];
                setNextQuestion();
                frame.getContentPane().add(questionPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        frame.setVisible(true);
    }

    private void sendMessageToServer(String message) {
        try {
            client.connectAndSend(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object receiveMessageFromServer() {
        Object receivedMessage = null;
        try {
            receivedMessage = client.connectAndReceive();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return receivedMessage;
    }

    private Object sendAndReceive(String message) {
        Object receivedMessage = null;
        try {
            receivedMessage = client.connectSendAndReceive(message);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return receivedMessage;
    }

    private void handleAnswer(String selectedAnswer) {
        if (askedQuest.checkAnswer(selectedAnswer)) {
            scoreCounter.increaseScore();
            result.setText("Du svarade rätt! Poäng: " + scoreCounter.getScore());
        } else {
            result.setText("Du svarade fel. Poäng: " + scoreCounter.getScore());
        }
        result.setVisible(true);

        // Proceed to the next question or end the quiz as needed.
        setNextQuestion();
    }

    private void setNextQuestion() {
        if (askedQuest != null) {
            int nextIndex = askedQuest.getQuestionIndex() + 1;
            if (nextIndex < questions.length) {
                askedQuest = questions[nextIndex];
                updateQuestionPanel();
            } else {
                // Quiz has ended. You can implement end-of-quiz logic here.
                // For example, display a summary or return to the main menu.
                System.out.println("Quiz ended. Final score: " + scoreCounter.getScore());
            }
        }
    }

    private void updateQuestionPanel() {
        questionLabel.setText(askedQuest.getQuestion());
        questionButton1.setText(askedQuest.getAnswerOption(0));
        questionButton2.setText(askedQuest.getAnswerOption(1));
        questionButton3.setText(askedQuest.getAnswerOption(2));
        questionButton4.setText(askedQuest.getAnswerOption(3));
        result.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    QuizGUI quizGUI = new QuizGUI();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
