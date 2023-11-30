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
    private Question[] questions = new Question[3];
    private boolean[][] gameresults = new boolean[6][3];

    public boolean[] getRoundResults() {
        return roundResults;
    }

    private boolean[] roundResults = new boolean[3];
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
    JPanel scorePanel, questionPanel, answerPanel, continuePanel;
    JButton continueButton, questionButton1, questionButton2, questionButton3, questionButton4;
    String s;
    JButton p1q1, p1q2, p1q3, p2q1, p2q2, p2q3;

    public QuizGUI(Client client, int roundCounter) throws IOException, ClassNotFoundException, NullPointerException, InterruptedException {
        this.client = client;
        this.roundCounter = roundCounter;
        System.out.println("innan loopen");
        while (!Objects.equals(message = (String) receiveMessageFromServer(), "START")) {
//            System.out.println(message);
        }
        if (roundCounter == 0) {
            while (startOfGame) {
                sendMessageToServer("Start");
                oMessage = receiveMessageFromServer();
                if (oMessage instanceof Boolean) {
                    myTurn = (boolean) oMessage;
                    System.out.println("It is my turn: " + myTurn);
                    startOfGame = false;
                }
            }
        } else {
            sendMessageToServer("NewRound");
            oMessage = receiveMessageFromServer();
            if (oMessage instanceof Boolean) {
                myTurn = (boolean) oMessage;
                System.out.println("It is my turn: " + myTurn);
            }
        }
        frame = new JFrame("Quizkampen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 450);
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
        scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setLayout(new GridLayout(2, 0));
        panel2.setLayout(new GridLayout(2, 0));
        JPanel p1Score = new JPanel();
        JLabel p1Name = new JLabel("Jag");
        p1Score.add(p1q1 = new JButton());
        p1Score.add(p1q2 = new JButton());
        p1Score.add(p1q3 = new JButton());
        for (Component c : p1Score.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).setPreferredSize(new Dimension(25, 25));
                ((JButton) c).setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }
        JPanel p2Score = new JPanel();
        JLabel p2Name = new JLabel("Motståndare");
        p2Score.add(p2q1 = new JButton());
        p2Score.add(p2q2 = new JButton());
        p2Score.add(p2q3 = new JButton());
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
            frame.getContentPane().add(categoryPanel);
        }

        if (!myTurn) {
            while (true) {
                s = (String) receiveMessageFromServer();
                System.out.println(s);

                if (s.startsWith("GO")) {
                    s = s.substring(2);
                    System.out.println(s);

                    oMessage = receiveMessageFromServer();
                    System.out.println(oMessage);
                    if (oMessage instanceof Question[] quests) {
                        questions = quests;
                    }
                    break;
                }
            }
            playRound(questions);
        }

        categoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button 1");
                frame.getContentPane().removeAll();

                serverMessage = sendAndReceive("P1" + categoryButton1.getText());

                if (serverMessage instanceof Question[] quests) {
                    questions = quests;
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
                    questions = quests;
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
        Font f = new Font("serif", Font.PLAIN, 24);
        Font f2 = new Font("dialog", Font.PLAIN, 24);
        questionPanel = new JPanel();
        questionLabel = new JLabel("(Fråga)");
        questionLabel.setFont(f);
        questionPanel.add(questionLabel);
        answerPanel = new JPanel();
        answerPanel.setPreferredSize(new Dimension(850, 100));
        answerPanel.setLayout(new GridLayout(2, 2));
        answerPanel.add(questionButton1 = new JButton("Svar 1"));
        answerPanel.add(questionButton2 = new JButton("Svar 2"));
        answerPanel.add(questionButton3 = new JButton("Svar 3"));
        answerPanel.add(questionButton4 = new JButton("Svar 4"));
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
                    if (myTurn) {
                        gameresults[roundCounter] = roundResults;
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
                    if (s.charAt(qCounter) == 't') {
                        p2q1.setBackground(Color.green);
                    } else {
                        p2q1.setBackground(Color.red);
                    }
                    frame.repaint();
                    frame.revalidate();
                }
                case 1 -> {
                    if (s.charAt(qCounter) == 't') {
                        p2q2.setBackground(Color.green);
                    } else {
                        p2q2.setBackground(Color.red);
                    }
                    frame.repaint();
                    frame.revalidate();
                }
                case 2 -> {
                    if (s.charAt(qCounter) == 't') {
                        p2q3.setBackground(Color.green);
                    } else {
                        p2q3.setBackground(Color.red);
                    }
                    frame.repaint();
                    frame.revalidate();
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
