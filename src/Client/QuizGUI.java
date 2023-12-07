package Client;

import POJOs.Category;
import POJOs.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuizGUI {
    private Question askedQuest = null;
    private Client client;
    private Object serverMessage;
    private Category[] categories;
    JButton[] categoryButtons = new JButton[3];
    private Question[] questions;
    private boolean[][] gameresults;
    private boolean[] roundResults;
    private boolean myTurn;
    private boolean startOfGame = true;
    private JFrame frame;
    private Object oMessage;
    private static JLabel questionLabel = new JLabel();
    private static JLabel emptyLabel2 = new JLabel();
    private int qCounter = 0;
    private int roundCounter;
    int numbOfRounds;
    int numbOfQuests;
    JPanel scorePanel, questionPanel, answerPanel, continuePanel, p1Score, p2Score;
    JButton continueButton;
    JPanel[] emptyPanels = new JPanel[4];
    String s;
    Font f = new Font("serif", Font.PLAIN, 24);
    Font f2 = new Font("dialog", Font.PLAIN, 24);
    JLabel p1Name = new JLabel("Spelare 1");
    JLabel p2Name = new JLabel("spelare 2");

    public QuizGUI(Client client, int roundCounter, int[] properties, String playerName, String opponentName) throws NullPointerException {
        this.client = client;
        this.roundCounter = roundCounter;
        numbOfQuests = properties[0];
        numbOfRounds = properties[1];
        gameresults = new boolean[numbOfRounds][numbOfQuests];
        roundResults = new boolean[numbOfQuests];
        questions = new Question[numbOfQuests];
        this.p1Name.setText(playerName);
        this.p2Name.setText(opponentName);

        sendMessageToServer("Start" + roundCounter);

        while((oMessage = receiveMessageFromServer()) != null){

            if (oMessage instanceof Boolean) {
                myTurn = (boolean) oMessage;
                break;
            }
        }

        frame = new JFrame("Quizkampen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);
        frame.setLocation(300, 0);
        frame.setLayout(new BorderLayout());

        categories = (Category[]) receiveMessageFromServer();

        for (int i = 0; i < emptyPanels.length; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(Color.BLUE);
            emptyPanels[i] = emptyPanel;
        }
        emptyPanels[0].setPreferredSize(new Dimension(0, 50));
        emptyPanels[1].setPreferredSize(new Dimension(175, 0));
        emptyPanels[2].setPreferredSize(new Dimension(175, 0));
        emptyPanels[3].setPreferredSize(new Dimension(0, 100));

        frame.getContentPane().add(emptyPanels[0], BorderLayout.NORTH);
        frame.getContentPane().add(emptyPanels[1], BorderLayout.WEST);
        frame.getContentPane().add(emptyPanels[2], BorderLayout.EAST);
        frame.getContentPane().add(emptyPanels[3], BorderLayout.SOUTH);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(4, 1));

        JLabel categoryLabel = new JLabel("Välj en kategori");
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Dimension labelSize = new Dimension(200, 50);
        categoryLabel.setPreferredSize(labelSize);
        categoryLabel.setFont(f);
        categoryLabel.setBackground(Color.BLUE);
        categoryLabel.setForeground(Color.ORANGE);
        categoryLabel.setOpaque(true);
        categoryPanel.add(categoryLabel);

        Dimension buttonSize = new Dimension(150, 40);

        for (int i = 0; i < categoryButtons.length; i++) {
            JButton categoryButton = new JButton(categories[i].getCategoryName());
            categoryButton.setPreferredSize(buttonSize);
            categoryButton.setFont(f2);
            categoryButton.setBackground(Color.ORANGE);
            categoryButton.setOpaque(true);
            categoryButtons[i] = categoryButton;
            categoryPanel.add(categoryButton);
        }
        frame.getContentPane().add(categoryPanel, BorderLayout.CENTER);

        scorePanel = new JPanel();
        scorePanel.setLayout(new BorderLayout());
        scorePanel.setBackground(Color.BLUE);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.BLUE);
        JPanel player1Board = new JPanel();

        player1Board.setLayout(new GridLayout(numbOfQuests, 0));
        player1Board.setBackground(Color.BLUE);
        JPanel player2Board = new JPanel();

        player2Board.setLayout(new GridLayout(numbOfQuests, 0));
        player2Board.setBackground(Color.BLUE);
        p1Score = new JPanel();
        p1Score.setBackground(Color.BLUE);
        p1Name.setHorizontalAlignment(SwingConstants.CENTER);
        p1Name.setBackground(Color.BLUE);
        p1Name.setForeground(Color.ORANGE);
        for (int i = 0; i < numbOfQuests; i++) {
            p1Score.add(new JButton());

        }
        for (Component c : p1Score.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).setPreferredSize(new Dimension(25, 25));
                ((JButton) c).setBorder(BorderFactory.createLineBorder(Color.BLACK));
                ((JButton) c).setOpaque(true);
            }
        }
        p2Score = new JPanel();
        p2Score.setBackground(Color.BLUE);
        p2Name.setHorizontalAlignment(SwingConstants.CENTER);
        p2Name.setBackground(Color.BLUE);
        p2Name.setForeground(Color.ORANGE);
        for (int i = 0; i < numbOfQuests; i++) {
            p2Score.add(new JButton());
        }
        for (Component c : p2Score.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).setPreferredSize(new Dimension(25, 25));
                ((JButton) c).setBorder(BorderFactory.createLineBorder(Color.BLACK));
                ((JButton) c).setOpaque(true);
            }
        }
        player1Board.add(p1Name);
        player1Board.add(p1Score);
        player2Board.add(p2Name);
        player2Board.add(p2Score);
        scorePanel.add(player1Board, BorderLayout.WEST);
        scorePanel.add(emptyPanel, BorderLayout.CENTER);
        scorePanel.add(player2Board, BorderLayout.EAST);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(15, 200, 0, 200));

        if (myTurn) {
            frame.setVisible(true);
        }
        if (!myTurn) {
            while (true) {
                s = (String) receiveMessageFromServer();

                if (s.startsWith("GO")) {
                    s = s.substring(2);

                    oMessage = receiveMessageFromServer();
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
            playRound(questions);
        }

        categoryButtons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();

                serverMessage = sendAndReceive("P1" + categoryButtons[0].getText());
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
        categoryButtons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                if (myTurn)
                    serverMessage = sendAndReceive("P1" + categoryButtons[1].getText());
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
        categoryButtons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                if (myTurn)
                    serverMessage = sendAndReceive("P1" + categoryButtons[2].getText());
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
        questionPanel.setLayout(new GridLayout(4, 1));
        questionPanel.setBackground(Color.BLUE);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel emptyLabel1 = new JLabel();
        JLabel emptyLabel3 = new JLabel();
        questionLabel.setBackground(Color.BLUE);
        questionLabel.setForeground(Color.ORANGE);
        questionLabel.setFont(f);
        emptyLabel2.setBackground(Color.BLUE);
        emptyLabel2.setForeground(Color.ORANGE);
        emptyLabel2.setFont(f);
        emptyLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel2.setText("");
        questionPanel.add(emptyLabel1);
        questionPanel.add(questionLabel);
        questionPanel.add(emptyLabel2);
        questionPanel.add(emptyLabel3);
        answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2, 2));
        answerPanel.setPreferredSize(new Dimension(700, 100));
        answerPanel.setBackground(Color.ORANGE);

        for (int i = 0; i < 4; i++) {
            answerPanel.add(new JButton());
        }

        continuePanel = new JPanel();
        continuePanel.setBorder(BorderFactory.createEmptyBorder(22, 0, 0, 0));
        continuePanel.setBackground(Color.BLUE);
        continueButton = new JButton("Fortsätt");
        continueButton.setPreferredSize(new Dimension(200, 50));
        continueButton.setFont(f2);
        continuePanel.add(continueButton);

        askedQuest = question;
        formatQuestion(askedQuest.getQuestion());

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

                        StringBuilder s = new StringBuilder();
                        for (boolean b : roundResults) {
                            if (b) {
                                s.append('t');
                            } else {
                                s.append('f');
                            }
                        }
                    if (myTurn) {
                        sendMessageToServer("GO" + s);
                    }
                    sendMessageToServer(gameresults);
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
            jb.setOpaque(true);
            jb.repaint();
            jb.revalidate();
            continueButton.setVisible(true);
            roundResults[qCounter] = true;
        } else {
            for (Component c : answerPanel.getComponents()) {
                if (c instanceof JButton) {
                    if (askedQuest.checkAnswer(((JButton) c).getText())) {
                        c.setBackground(Color.green);
                        ((JButton) c).setOpaque(true);
                    }
                }
            }
            jb.setBackground(Color.red);
            jb.setOpaque(true);
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

    public static void formatQuestion(String question) {
        if (question.length() <= 65) {
            questionLabel.setText(question);
        } else {
            int lastSpaceIndex = question.lastIndexOf(" ", 65);

            if (lastSpaceIndex == -1) {
                lastSpaceIndex = 65;
            }
            questionLabel.setText(question.substring(0, lastSpaceIndex));
            emptyLabel2.setText(question.substring(lastSpaceIndex + 1));
        }
    }

    private void sendMessageToServer(Object message) {
        try {
            client.Send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object receiveMessageFromServer() {
        Object receivedMessage = null;
        try {
            receivedMessage = client.Receive();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return receivedMessage;
    }

    private Object sendAndReceive(String message) {
        Object receivedMessage = null;
        try {
            receivedMessage = client.SendAndReceive(message);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return receivedMessage;
    }
}