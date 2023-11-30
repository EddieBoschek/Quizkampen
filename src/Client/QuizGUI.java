package Client;

import POJOs.Category;
import POJOs.Question;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuizGUI {

    private Question askedQuest = null;
    private Client client;
    private Object serverMessage;
    private Category[] categories;
    private Question[] questions = new Question[10];
    private boolean[][] gameresults = new boolean[6][3];
    private boolean[] roundResults = new boolean[3];
    boolean[] opponentRoundResults;
    private boolean myTurn;
    private boolean startOfGame = true;
    private JFrame frame;
    private String message;
    private Object oMessage;
    private JLabel questionLabel;
    private int opponentDoClickValue = -1;
    private int qCounter = 0;
    private int roundCounter = 0;
    JPanel scorePanel, questionPanel, answerPanel, continuePanel;
    JButton continueButton, questionButton1, questionButton2, questionButton3, questionButton4;
    boolean contin;
    JButton p1q1, p1q2, p1q3, p2q1, p2q2, p2q3;
    Font f = new Font("serif", Font.PLAIN, 24);
    Font f2 = new Font("dialog", Font.PLAIN, 24);

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
        frame.setSize(850, 450);
        frame.setLayout(new BorderLayout());

        categories = (Category[]) receiveMessageFromServer();

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

        panel1.setLayout(new GridLayout(2,0));

        panel2.setLayout(new GridLayout(2,0));


        JPanel p1Score = new JPanel();
        JLabel p1Name = new JLabel("Jag");
        p1q1 = new JButton();
        p1q2 = new JButton();
        p1q3 = new JButton();
        p1q1.setPreferredSize(new Dimension(25,25));
        p1q2.setPreferredSize(new Dimension(25,25));
        p1q3.setPreferredSize(new Dimension(25,25));
        p1q1.setBorder(BorderFactory.createLineBorder(Color.black));
        p1q2.setBorder(BorderFactory.createLineBorder(Color.black));
        p1q3.setBorder(BorderFactory.createLineBorder(Color.black));
        p1Score.add(p1q1);
        p1Score.add(p1q2);
        p1Score.add(p1q3);


        JPanel p2Score = new JPanel();
        JLabel p2Name = new JLabel("Motståndare");
        p2q1 = new JButton();
        p2q2 = new JButton();
        p2q3 = new JButton();
        p2q1.setPreferredSize(new Dimension(25,25));
        p2q2.setPreferredSize(new Dimension(25,25));
        p2q3.setPreferredSize(new Dimension(25,25));
        p2q1.setBorder(BorderFactory.createLineBorder(Color.black));
        p2q2.setBorder(BorderFactory.createLineBorder(Color.black));
        p2q3.setBorder(BorderFactory.createLineBorder(Color.black));
        p2Score.add(p2q1);
        p2Score.add(p2q2);
        p2Score.add(p2q3);

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
                if ((oMessage = receiveMessageFromServer()) != null) {
                    if (oMessage.equals(categories[0].getCategoryName())) {
                        opponentDoClickValue = 0;
                    } else if (((String) oMessage).equals(categories[1].getCategoryName())) {
                        opponentDoClickValue = 1;
                    } else if (((String) oMessage).equals(categories[2].getCategoryName())) {
                        opponentDoClickValue = 2;
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

        categoryButton3.addActionListener(new ActionListener() {
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

        frame.setVisible(true);
        if (opponentDoClickValue == 0) {
            categoryButton1.doClick();
        } else if (opponentDoClickValue == 1) {
            categoryButton2.doClick();
        } else if (opponentDoClickValue == 2) {
            categoryButton3.doClick();
        }
    }

    private void displayQuestion(Question question) {

        questionPanel = new JPanel();
        questionLabel = new JLabel("(Fråga)");
        questionLabel.setFont(f);
        questionPanel.add(questionLabel);

        answerPanel = new JPanel();
        answerPanel.setPreferredSize(new Dimension(850, 100));
        answerPanel.setLayout(new GridLayout(2, 2));
        questionButton1 = new JButton("Svar 1");
        questionButton2 = new JButton("Svar 2");
        questionButton3 = new JButton("Svar 3");
        questionButton4 = new JButton("Svar 4");

        answerPanel.add(questionButton1);
        answerPanel.add(questionButton2);
        answerPanel.add(questionButton3);
        answerPanel.add(questionButton4);

        questionButton1.setFont(f2);
        questionButton2.setFont(f2);
        questionButton3.setFont(f2);
        questionButton4.setFont(f2);

        continuePanel = new JPanel();
        continueButton = new JButton("Fortsätt");
        continueButton.setPreferredSize(new Dimension(500, 50));
        continueButton.setFont(f2);
        continuePanel.add(continueButton);


        askedQuest = question;
        questionLabel.setText(askedQuest.getQuestion());
        questionButton1.setText(askedQuest.getAnswerOption(0));
        questionButton2.setText(askedQuest.getAnswerOption(1));
        questionButton3.setText(askedQuest.getAnswerOption(2));
        questionButton4.setText(askedQuest.getAnswerOption(3));


        ActionListener commonActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                handleAnswer((JButton) e.getSource());

                qCounter++;


            }
        };
        ActionListener continueActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (qCounter < questions.length) {
                    displayQuestion(questions[qCounter]);
                } else {
                    gameresults[roundCounter] = roundResults;

                    sendMessageToServer("Ny Runda");
                    oMessage = receiveMessageFromServer();
                    if (oMessage instanceof Boolean) {
                        myTurn = (boolean) oMessage;
                        System.out.println("It is my turn: " + myTurn);
                        roundCounter++;
                        updateScorePanel();
                    }
                }
            }
        };

        questionButton1.addActionListener(commonActionListener);
        questionButton2.addActionListener(commonActionListener);
        questionButton3.addActionListener(commonActionListener);
        questionButton4.addActionListener(commonActionListener);
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
        updateScorePanel();
    }

    private void handleAnswer(JButton jb) {
        if (questions[qCounter].checkAnswer(jb.getText())) {
            jb.setBackground(Color.green);
            jb.repaint();
            jb.revalidate();
            continueButton.setVisible(true);

            roundResults[qCounter] = true;
        } else {
            for (Component c:answerPanel.getComponents()) {
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
        switch (qCounter) {
            case 0 -> {
                p1q1.setBackground(jb.getBackground());
                p1q1.revalidate();
                p1q1.repaint();
            }
            case 1 -> {
                p1q2.setBackground(jb.getBackground());
                frame.repaint();
                frame.revalidate();
            }
            case 2 -> {
                p1q3.setBackground(jb.getBackground());
                frame.repaint();
                frame.revalidate();
            }

        }
        if (!myTurn) {
            switch (qCounter) {
                case 0 -> {
                    if (opponentRoundResults[0]) {
                        p2q1.setBackground(Color.green);
                    } else {
                        p2q1.setBackground(Color.red);
                    }

                }
                case 1 -> {
                    if (opponentRoundResults[1]) {
                        p2q1.setBackground(Color.green);
                    } else {
                        p2q1.setBackground(Color.red);
                    }

                }
                case 2 -> {
                    if (opponentRoundResults[2]) {
                        p2q1.setBackground(Color.green);
                    } else {
                        p2q1.setBackground(Color.red);
                    }

                }

            }
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
    private void updateScorePanel() {
        p1q1.setBackground(gameresults[roundCounter][0] ? Color.green : Color.red);
        p1q2.setBackground(gameresults[roundCounter][1] ? Color.green : Color.red);
        p1q3.setBackground(gameresults[roundCounter][2] ? Color.green : Color.red);

        p2q1.setBackground(opponentRoundResults[0] ? Color.green : Color.red);
        p2q2.setBackground(opponentRoundResults[1] ? Color.green : Color.red);
        p2q3.setBackground(opponentRoundResults[2] ? Color.green : Color.red);
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