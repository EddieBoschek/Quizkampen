package Client;

import POJOs.Category;
import POJOs.Question;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuizGUI {
    private Question askedQuest = null;
    private Client client;
    private Object serverMessage;
    private Category[] categories;
    private Question[] questions;
    private boolean[][] gameresults;
    private boolean[] roundResults;
    boolean[] opponentRoundResults = new boolean[3];
    private boolean myTurn;
    private boolean startOfGame = true;
    private JFrame frame;
    private String message;
    private Object oMessage;
    private JLabel questionLabel;
    private int opponentDoClickValue = -1;
    private int qCounter = 0;
    private int roundCounter;
    int numbOfRounds;
    int numbOfQuests;
    JPanel scorePanel, questionPanel, answerPanel, continuePanel, p1Score, p2Score;
    JButton continueButton;
    String s;
    Font f = new Font("serif", Font.PLAIN, 24);
    Font f2 = new Font("dialog", Font.PLAIN, 24);

    public QuizGUI(Client client, int roundCounter, int[] properties) throws IOException, ClassNotFoundException, NullPointerException, InterruptedException {
        this.client = client;
        this.roundCounter = roundCounter;
        numbOfQuests = properties[0];
        numbOfRounds = properties[1];
        questions = new Question[numbOfQuests];
        gameresults = new boolean[numbOfRounds][numbOfQuests];
        roundResults = new boolean[numbOfQuests];

        System.out.println("innan loopen");
//        while (!Objects.equals(message = (String) receiveMessageFromServer(), "START")) {
//        }

        while (startOfGame) {
            sendMessageToServer("Start" + roundCounter);
            oMessage = receiveMessageFromServer();
//            client.flushOutput();
            if (oMessage instanceof Boolean) {
                myTurn = (boolean) oMessage;
                System.out.println("It is my turn: " + myTurn);
                startOfGame = false;
            }
        }


        frame = new JFrame("Quizkampen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 450);
        frame.setLayout(new BorderLayout());

        categories = (Category[]) receiveMessageFromServer();



//if (roundCounter != 0) {
//    Object trashcan;
//    while ((trashcan = receiveMessageFromServer()) != null) {
//        System.out.println(trashcan);
//    }
//}



        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(4, 1));
        JPanel emptyPanelWest = new JPanel();
        JPanel emptyPanelEast = new JPanel();
        JPanel emptyPanelNorth = new JPanel();
        JPanel emptyPanelSouth = new JPanel();
        emptyPanelWest.setPreferredSize(new Dimension(250, 0));
        emptyPanelEast.setPreferredSize(new Dimension(250, 0));
        emptyPanelNorth.setPreferredSize(new Dimension(0, 50));
        emptyPanelSouth.setPreferredSize(new Dimension(0, 75));

        JLabel categoryLabel = new JLabel("Välj en kategori");
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton categoryButton1 = new JButton(categories[0].getCategoryName());
        JButton categoryButton2 = new JButton(categories[1].getCategoryName());
        JButton categoryButton3 = new JButton(categories[2].getCategoryName());

        Dimension labelSize = new Dimension(200, 50);
        categoryLabel.setPreferredSize(labelSize);
        categoryLabel.setFont(f);

        Dimension buttonSize = new Dimension(150, 40);
        categoryButton1.setPreferredSize(buttonSize);
        categoryButton2.setPreferredSize(buttonSize);
        categoryButton3.setPreferredSize(buttonSize);
        categoryButton1.setFont(f2);
        categoryButton2.setFont(f2);
        categoryButton3.setFont(f2);

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryButton1);
        categoryPanel.add(categoryButton2);
        categoryPanel.add(categoryButton3);

        scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setLayout(new GridLayout(2, 0));
        panel2.setLayout(new GridLayout(2, 0));
        p1Score = new JPanel();
        JLabel p1Name = new JLabel("Jag");
        for (int i = 0; i < numbOfQuests; i++) {
            p1Score.add(new JButton());

        }
        for (Component c : p1Score.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).setPreferredSize(new Dimension(25, 25));
                ((JButton) c).setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }
        p2Score = new JPanel();
        JLabel p2Name = new JLabel("Motståndare");
        for (int i = 0; i < numbOfQuests; i++) {
            p2Score.add(new JButton());

        }
        for (Component c : p2Score.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).setPreferredSize(new Dimension(25, 25));
                ((JButton) c).setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }
        panel1.add(p1Name);
        panel1.add(p1Score);
        panel2.add(p2Name);
        panel2.add(p2Score);
        scorePanel.add(panel1);
        scorePanel.add(panel2);
        if (myTurn) {
            frame.getContentPane().add(emptyPanelNorth, BorderLayout.NORTH);
            frame.getContentPane().add(categoryPanel, BorderLayout.CENTER);
            frame.getContentPane().add(emptyPanelWest, BorderLayout.WEST);
            frame.getContentPane().add(emptyPanelEast, BorderLayout.EAST);
            frame.getContentPane().add(emptyPanelSouth, BorderLayout.SOUTH);
            frame.revalidate();
            frame.repaint();
        }

        if (!myTurn) {
            while (true) {

                s = (String) receiveMessageFromServer();
                // s är false;




                System.out.println(s);

                if (s.startsWith("GO")) {
                    s = s.substring(2);
                    System.out.println(s);

                    oMessage = receiveMessageFromServer();
                    System.out.println(oMessage);
                    if (oMessage instanceof Question[] quests) {
                        int i = 0;
                        while (i < numbOfQuests) {
                            questions[i] = quests[i];
                            i++;
                        }
                    }
                    break;
                }
            }
            System.out.println(questions.length);
            playRound(questions);
        }

        categoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button 1");
                frame.getContentPane().removeAll();

                serverMessage = sendAndReceive("P1" + categoryButton1.getText());

                if (serverMessage instanceof Question[] quests) {
                    int i = 0;
                    while (i < numbOfQuests) {

                        questions[i] = quests[i];
                        i++;
                    }
                }
                playRound(questions);
            }
        });
        categoryButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button 2");
                frame.getContentPane().removeAll();
                if (myTurn)
                    serverMessage = sendAndReceive("P1" + categoryButton2.getText());

                if (serverMessage instanceof Question[] quests) {
                    int i = 0;
                    while (i < numbOfQuests) {
                        questions[i] = quests[i];
                        i++;
                    }
                }
                playRound(questions);
            }
        });
        categoryButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                if (myTurn)
                    serverMessage = sendAndReceive("P1" + categoryButton3.getText());

                if (serverMessage instanceof Question[] quests) {
                    int i = 0;
                    while (i < numbOfQuests) {
                        questions[i] = quests[i];
                        i++;
                    }
                }
                playRound(questions);
            }
        });

        frame.setVisible(true);
    }

    private void displayQuestion(Question question) {

        Font f = new Font("serif", Font.PLAIN, 24);
        Font f2 = new Font("dialog", Font.PLAIN, 24);
        questionPanel = new JPanel();
        questionLabel = new JLabel("(Fråga)");
        questionLabel.setFont(f);
        questionPanel.add(questionLabel);
        answerPanel = new JPanel();
        answerPanel.setPreferredSize(new Dimension(850, 100));
        answerPanel.setLayout(new GridLayout(2, 2));

        for (int i = 0; i < 4; i++) {
            answerPanel.add(new JButton());
        }

        continuePanel = new JPanel();
        continueButton = new JButton("Fortsätt");
        continueButton.setPreferredSize(new Dimension(500, 50));
        continueButton.setFont(f2);
        continuePanel.add(continueButton);

        askedQuest = question;
        questionLabel.setText(askedQuest.getQuestion());

        ActionListener commonActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component c:answerPanel.getComponents()) {
                    if (c instanceof JButton) {
                        for (ActionListener al : ((JButton) c).getActionListeners()) {
                            ((JButton) c).removeActionListener(al);
                        }
                    }
                }
                handleAnswer((JButton) e.getSource());
                qCounter++;
            }
        };
        ActionListener continueActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (qCounter < numbOfQuests) {
                    displayQuestion(questions[qCounter]);
                } else {
                    gameresults[roundCounter] = roundResults;
                    if (myTurn) {

                        StringBuilder s = new StringBuilder();
                        for (boolean b : roundResults) {
                            if (b) {
                                s.append('t');
                            } else {
                                s.append('f');
                            }
                        }
                        sendMessageToServer("GO" + s);
                        System.out.println(s);
                    }

                    frame.dispose();
                }

            }
        };
        int i = 0;
        for (Component c : answerPanel.getComponents()) {
            if (c instanceof JButton) {
                c.setFont(f2);
                ((JButton) c).setText(askedQuest.getAnswerOption(i));
                ((JButton) c).addActionListener(commonActionListener);
                i++;
            }

        }

        continueButton.addActionListener(continueActionListener);
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new GridLayout(4, 1));
        frame.getContentPane().add(scorePanel);
        frame.getContentPane().add(questionPanel);
        frame.getContentPane().add(answerPanel);
        frame.getContentPane().add(continuePanel);
        continueButton.setVisible(false);
        frame.revalidate();
        frame.repaint();
    }


    public void playRound(Question[] questions) {
        displayQuestion(questions[qCounter]);
    }

    private void handleAnswer(JButton jb) {
        if (questions[qCounter].checkAnswer(jb.getText())) {
            jb.setBackground(Color.green);
            jb.repaint();
            jb.revalidate();
            continueButton.setVisible(true);
            roundResults[qCounter] = true;
        } else {
            for (Component c : answerPanel.getComponents()) {
                if (c instanceof JButton) {
                    if (askedQuest.checkAnswer(((JButton) c).getText())) {
                        c.setBackground(Color.green);
                    }
                }
            }
            jb.setBackground(Color.red);
            jb.repaint();
            jb.revalidate();
            continueButton.setVisible(true);
            roundResults[qCounter] = false;
        }
        p1Score.getComponent(qCounter).setBackground(jb.getBackground());
        frame.repaint();
        frame.revalidate();

        if (!myTurn) {


            if (s.charAt(qCounter) == 't') {
                p2Score.getComponent(qCounter).setBackground(Color.green);
            } else {
                p2Score.getComponent(qCounter).setBackground(Color.red);
            }
            frame.repaint();
            frame.revalidate();

        }
    }

    private void sendMessageToServer(Object message) {
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



    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainMenuGUI mainMenuGUI = new MainMenuGUI();
                } catch (Exception e) {
                }
            }
        });
    }
}
