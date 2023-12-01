package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuGUI {
    JButton startGameButton = new JButton("Starta nytt spel");
    JButton settingsButton = new JButton("Inställningar");
    JButton play = new JButton("Spela");
    JLabel gameName = new JLabel("Quizkampen", SwingConstants.CENTER);
    JPanel menuPanel = new JPanel();
    JPanel activeGamesPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JFrame frame = new JFrame();
    int numbOfRounds;
    int numbOfQuest;
    boolean[] playerRound = new boolean[]{true, true, false};    //Tillfällig
    boolean[] opponentRound = new boolean[]{true, false, true};  //Tillfällig
    Client client;
    JLabel playerName = new JLabel("My name");
    JLabel opponentName = new JLabel("Opponent name");
    JLabel currentScore = new JLabel("0-0");
    ArrayList<JLabel> playerScoreArray = new ArrayList<>();
    ArrayList<JLabel> opponentScoreArray = new ArrayList<>();
    ArrayList<JLabel> subjectArray = new ArrayList<>();
    int currentRound = 0;
    int[] properties;
    boolean settingUp = true;
    Object o;

    public MainMenuGUI() throws IOException {
        client = new Client("127.0.0.1", 12345);
        while (settingUp) {
            o = sendAndReceive("PropertiesRequest");
            System.out.println(o);
            if (o instanceof int[]) {

                properties = (int[]) o;
                numbOfQuest = properties[0];
                numbOfRounds = properties[1];
                settingUp = false;
            }
        }

        client = new Client("127.0.0.1", 12345);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 600);
        frame.getContentPane().add(menuPanel);

        menuPanel.setLayout(new BorderLayout());
        menuPanel.add(gameName, BorderLayout.NORTH);
        menuPanel.add(activeGamesPanel, BorderLayout.CENTER);
        menuPanel.add(buttonsPanel, BorderLayout.SOUTH);

        buttonsPanel.setLayout(new GridLayout(2, 1));
        buttonsPanel.setSize(300, 200);
        buttonsPanel.add(startGameButton);
        buttonsPanel.add(settingsButton);

        Dimension d = new Dimension(250, 80);

        startGameButton.setPreferredSize(d);
        startGameButton.setFont(new Font("Serif", Font.PLAIN, 20));
        settingsButton.setPreferredSize(d);
        settingsButton.setFont(new Font("Serif", Font.PLAIN, 20));

        gameName.setPreferredSize(d);
        gameName.setFont(new Font("Serif", Font.PLAIN, 30));

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == startGameButton) {
                    System.out.println("Startar sökning efter en motståndare -> Startar upp en GameInstance och öppnar upp spelmenyn");

                    frame.getContentPane().removeAll();
                    getGameMenu();
//                    updateScore(); //Ska hämta all info, spelarnamn, boolean-poäng-array(s),

//                    frame.repaint();
//                    frame.revalidate();


                } else if (e.getSource() == settingsButton) {
                    System.out.println("Öppnar upp en ny JPanel med \"settingsknappar\" som går att justera. Det ska också finnas en apply-knapp");
                } else if (e.getSource() == play) {
                    try {
                        playRound();
                    } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };

        startGameButton.addActionListener(buttonListener);
        settingsButton.addActionListener(buttonListener);
        play.addActionListener(buttonListener);


        frame.setVisible(true);
    }

    public void getGameMenu() {
        JPanel gameMenu = new JPanel(new BorderLayout());
        frame.getContentPane().add(gameMenu);

        String s = null;
        char c = '\u25A1';
        Font f = new Font("Serif", Font.PLAIN, 35);

        for (int i = 0; i < numbOfRounds * numbOfQuest; i++) {
            playerScoreArray.add(new JLabel(String.valueOf(c)));
            playerScoreArray.get(i).setFont(f);
            opponentScoreArray.add(new JLabel(String.valueOf(c)));
            opponentScoreArray.get(i).setFont(f);
            if (i % 3 == 0) {
                int j = i / 3;
                s = String.valueOf(j + 1);
                subjectArray.add(new JLabel(s));
                subjectArray.get(j).setFont(f);
            }
        }

//        play = new JButton("Spela");
//        play.addActionListener(MainMenuGUI.buttonListener);

        JPanel northPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel playerScorePanel = new JPanel(new GridLayout(numbOfRounds, numbOfRounds));
        JPanel opponentScorePanel = new JPanel(new GridLayout(numbOfRounds, numbOfRounds));
        JPanel subjectPanel = new JPanel(new GridLayout(numbOfRounds, 1));

        for (int i = 0; i < numbOfRounds * numbOfQuest; i++) {
            playerScorePanel.add(playerScoreArray.get(i));
            opponentScorePanel.add(opponentScoreArray.get(i));

            if (i % 3 == 0)
                subjectPanel.add(subjectArray.get(i / 3));
        }

        gameMenu.add(northPanel, BorderLayout.NORTH);
        gameMenu.add(centerPanel, BorderLayout.CENTER);
        gameMenu.add(play, BorderLayout.SOUTH);

        northPanel.add(playerName);
        northPanel.add(currentScore);
        northPanel.add(opponentName);

        centerPanel.add(playerScorePanel);
        centerPanel.add(subjectPanel);
        centerPanel.add(opponentScorePanel);


        frame.repaint();
        frame.revalidate();
    }

    public void updateScore() {
        send("GameUpdateRequest");
        System.out.println("GUR");
        Object input = null;
        int i = 0;
        boolean[][] playerBoolArray = new boolean[numbOfRounds][numbOfQuest];
        boolean[][] opponentBoolArray = new boolean[numbOfRounds][numbOfQuest];

        while (true) {
            input = receive();
            if (input == "END")
                break;

            switch (i) {
                case 0 -> playerName.setText((String) input);
                case 1 -> opponentName.setText((String) input);
                case 2 -> playerBoolArray = (boolean[][]) input;
                case 3 -> opponentBoolArray = (boolean[][]) input;
                case 4 -> {
                    for (int j = 0; j < numbOfRounds; j++) {
                        String s = ((String[]) input)[j];
                        if (s != null)
                            subjectArray.get(j).setText(s);
                    }
                }

            }
            i++;
        }


        int playerScoreCounter = 0;
        int opponentScoreCounter = 0;
        int loopCounter = 0;
        for (int j = 0; j < currentRound - 1; j++) {
            for (int k = 0; k < numbOfQuest; k++) {
                if (playerBoolArray[j][k]) {
                    playerScoreArray.get(loopCounter).setForeground(Color.GREEN);
                    playerScoreCounter++;
                }
                else
                    playerScoreArray.get(loopCounter).setForeground(Color.RED);

                if (opponentBoolArray[j][k]) {
                    opponentScoreArray.get(loopCounter).setForeground(Color.GREEN);
                    opponentScoreCounter++;
                }
                else
                    opponentScoreArray.get(loopCounter).setForeground(Color.RED);

                loopCounter++;
            }
        }
        currentScore.setText(playerScoreCounter + " - " + opponentScoreCounter);
    }

    public void playRound() throws IOException, ClassNotFoundException, InterruptedException {
        if (currentRound < numbOfRounds) {
            System.out.println("current round: " + currentRound);
            QuizGUI quizGUI = new QuizGUI(client, currentRound, properties);
            currentRound++;
        }
    }

    public void send(Object message) {
        if (message != null) {
            try {
                client.connectAndSend(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Object receive() {
        Object receivedMessage = null;
        try {
            receivedMessage = client.connectAndReceive();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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

}