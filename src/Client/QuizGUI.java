package Client;

import POJOs.Category;
import POJOs.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class QuizGUI {

    private Question askedQuest = null;
    private Client client;
    private Object serverMessage;
    private Category[] categories;
    private Question[] questions = new Question[3];
    private boolean[][] gameresults = new boolean[4][3]; //4 och 3 ersätts med värden från properties-filen
    private boolean[] roundResults = new boolean[3]; // samma här
    private boolean myTurn;
    private boolean startOfGame = true;
    private JFrame frame;
    private String message;
    private Object oMessage;
    private JLabel result = new JLabel();
    private int opponentDoClickValue = -1;
    private int qCounter = 0;
    private int roundCounter = 0;

    public QuizGUI() throws IOException, ClassNotFoundException, NullPointerException, InterruptedException {

        client = new Client("127.0.0.1", 12345);

        System.out.println("innan loopen");

        while (!Objects.equals(message = (String) receiveMessageFromServer(), "START")) {
//            System.out.println(message);
        }
        while (startOfGame) {
            sendMessageToServer("Start");
            oMessage = receiveMessageFromServer();
            if (oMessage instanceof Boolean) {
                myTurn = (boolean) oMessage;
                System.out.println("It is my turn: " + myTurn);
                startOfGame = false;
            }
        }

        frame = new JFrame("Quizkampen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 250);
        frame.setLayout(new FlowLayout());

        categories = (Category[]) receiveMessageFromServer();

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(4, 1));

        JLabel categoryLabel = new JLabel("Välj en kategori");
        JButton categoryButton1 = new JButton(categories[0].getSubjectName());
        JButton categoryButton2 = new JButton(categories[1].getSubjectName());
        //JButton categoryButton3 = new JButton(categories[2].getSubjectName());

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryButton1);
        categoryPanel.add(categoryButton2);
        //categoryPanel.add(categoryButton3);

        if (myTurn) {
            frame.getContentPane().add(categoryPanel);
        }
        if (!myTurn) {
            while (true) {
                if ((oMessage = receiveMessageFromServer()) != null) {
                    if (oMessage.equals(categories[0].getSubjectName())) {
                        opponentDoClickValue = 0;
                    } else if (((String) oMessage).equals(categories[1].getSubjectName())) {
                        opponentDoClickValue = 1;
                    }
                    break;
                }
            }
        }
        categoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button 1");
                frame.getContentPane().removeAll();
                if (myTurn)
                    serverMessage = sendAndReceive(categoryButton1.getText());
                else {
                    serverMessage = receiveMessageFromServer();
                    System.out.println(serverMessage);
                }
                if (serverMessage instanceof Question[] quests) {
                    questions = quests;
                }
                playRound(questions);
            }
        });


        categoryButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                if (myTurn)
                    serverMessage = sendAndReceive(categoryButton2.getText());
                else {
                    serverMessage = receiveMessageFromServer();
                    System.out.println(serverMessage);
                }
                if (serverMessage instanceof Question[] quests) {questions = quests;
                    }
                playRound(questions);
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
        if (opponentDoClickValue == 0) {
            categoryButton1.doClick();
        } else if (opponentDoClickValue == 1) {
            categoryButton2.doClick();
        }
    }

    private void displayQuestion(Question question) {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(6, 1));

        JLabel questionLabel = new JLabel("(Fråga)");
        JButton questionButton1 = new JButton("Svar 1");
        JButton questionButton2 = new JButton("Svar 2");
        JButton questionButton3 = new JButton("Svar 3");
        JButton questionButton4 = new JButton("Svar 4");

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

                qCounter++;
                if (qCounter < questions.length) {
                    displayQuestion(questions[qCounter]);
                } else {
                    gameresults[roundCounter] = roundResults;

                    sendMessageToServer("Ny Runda");
                    oMessage = receiveMessageFromServer();
                    if (oMessage instanceof Boolean) {
                        myTurn = (boolean) oMessage;
                        System.out.println("It is my turn: " + myTurn);
                        //qCounter = 0;
                        roundCounter++;
                    }
                    //behöver skapa en loop eller göra om kategorivalet till en funktion
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
            displayQuestion(questions[qCounter]);
    }

    private void handleAnswer(String answer) {
        if (questions[qCounter].checkAnswer(answer)) {

            result.setText("Du svarade rätt.");
            result.setVisible(true);

            roundResults[qCounter] = true;
        } else {

            result.setText("Du svarade fel.");
            result.setVisible(true);

            roundResults[qCounter] = false;
        }
    }

    private void sendMessageToServer(Object message) {
        try {
            client.connectAndSend(message);
            //client.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Object receiveMessageFromServer() {
        Object receivedMessage = null;
        try {
            receivedMessage = client.connectAndReceive();
            //receivedMessage = client.receiveMessage();
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Innan JFrame");
                    QuizGUI quizGUI = new QuizGUI();
                    System.out.println("JFrame borde starta");

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}