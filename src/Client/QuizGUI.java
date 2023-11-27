package Client;

import POJOs.Category;
import POJOs.GameInstance;
import POJOs.Player;
import POJOs.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class QuizGUI {

    Question askedQuest = null;
    private Client client;
    Object serverMessage;
    Category[] categories;
    Question[] questions = new Question[3];
    boolean myTurn;
    boolean startOfGame = true;
    JButton categoryButton1;
    JButton categoryButton2;
    String message;
    Object oMessage;

    int opponentDoClickValue = -1;


    public QuizGUI() throws IOException, ClassNotFoundException, NullPointerException, InterruptedException {

//        Thread.sleep(1000);

        client = new Client("127.0.0.1", 12345);

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

//        while(true) {
//            if (receiveMessageFromServer() == "GameStart")
//                break;
//        }

        JFrame frame = new JFrame("Quiz GUI");
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
        categories = (String[]) receiveMessageFromServer();

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
        if (!myTurn){
            while(true) {
                if((oMessage = receiveMessageFromServer()) != null) {
                    System.out.println("innan if");
                    if (oMessage.equals(categories[0])) {
                        System.out.println(oMessage.equals(categories[0]));
                        opponentDoClickValue = 0;
                    } else if (((String) oMessage).equals(categories[1])) {
                        opponentDoClickValue = 1;
                    }
                    System.out.println("inte myTurn");
                    break;
                }
            }
        }

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(6, 1));

        JLabel questionLabel = new JLabel("(Fråga)");
        JButton questionButton1 = new JButton("Svar 1");
        JButton questionButton2 = new JButton("Svar 2");
        JButton questionButton3 = new JButton("Svar 3");
        JButton questionButton4 = new JButton("Svar 4");
        JLabel result = new JLabel("Du svarade rätt!");
        result.setVisible(false);

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
                System.out.println();
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

        if (myTurn) {
            categoryButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
        }

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

        //QuestionCollection qCollection = new QuestionCollection("qCollection");
        //Question[] questions = new Question[3];

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
        System.out.println(opponentDoClickValue);
        if (opponentDoClickValue == 0) {
            categoryButton1.doClick();
        } else if (opponentDoClickValue == 1) {
            categoryButton2.doClick();
        }
    }

    private void sendMessageToServer(Object message) {
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


    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        QuizGUI quizGUI = new QuizGUI();
//        while(true) {
//            if (quizGUI.receiveMessageFromServer() == "GameStart")
//                break;
//        }
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
                //QuizGUI.setVisible(true);
            }
        });
    }
}