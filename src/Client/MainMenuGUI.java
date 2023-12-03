package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MainMenuGUI {
    JButton startGameButton = new JButton("Starta nytt spel");
    JButton settingsButton = new JButton("Inställningar");
    JButton play = new JButton("Spela");
    JLabel gameName = new JLabel("Quizkampen", SwingConstants.CENTER);
    JPanel menuPanelMaster = new JPanel();
    JPanel menuSubPanel = new JPanel();
    JPanel activeGamesPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JFrame frame = new JFrame("Quizkampen");
    int numbOfRounds;
    int numbOfQuest;
    boolean[] playerRound = new boolean[]{true, true, false};    //Tillfällig
    boolean[] opponentRound = new boolean[]{true, false, true};  //Tillfällig
    Client client;
    JLabel playerName = new JLabel("Ditt namn");
    JLabel opponentName = new JLabel("Motståndare");
    JLabel enterName = new JLabel("Skriv in ditt namn: ");
    JTextField enterNameField = new JTextField();
    JLabel currentScore = new JLabel("0-0");
    ArrayList<JLabel> playerScoreArray = new ArrayList<>();
    ArrayList<JLabel> opponentScoreArray = new ArrayList<>();
    ArrayList<JLabel> categoryArray = new ArrayList<>();
    int currentRound = 0;
    int[] properties;
    boolean settingUp = true;
    Object o;

    public MainMenuGUI() {

        getStartMenu();

    }
        public void getStartMenu() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.getContentPane().add(menuPanelMaster);

        menuPanelMaster.setLayout(new GridLayout(2,1));
        menuPanelMaster.add(gameName);
        menuPanelMaster.add(menuSubPanel);

        menuSubPanel.setLayout(new BorderLayout());

        for (String position : new String[]{"West", "East", "South"}) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.BLUE);
            panel.setPreferredSize(new Dimension(60, 60));
            menuSubPanel.add(panel, position);
        }

        menuSubPanel.add(buttonsPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(3, 1));
        buttonsPanel.setSize(300, 200);
        enterName.setHorizontalAlignment(SwingConstants.CENTER);
        enterName.setFont(new Font("Serif", Font.PLAIN, 16));
        buttonsPanel.add(enterName);
        buttonsPanel.add(enterNameField);
        buttonsPanel.add(startGameButton);
        buttonsPanel.setBackground(Color.ORANGE);
        //buttonsPanel.add(settingsButton);

        Dimension d = new Dimension(250, 80);

        startGameButton.setPreferredSize(d);
        startGameButton.setFont(new Font("Serif", Font.PLAIN, 20));
        //settingsButton.setPreferredSize(d);
        //settingsButton.setFont(new Font("Serif", Font.PLAIN, 20));

        gameName.setPreferredSize(d);
        gameName.setFont(new Font("Serif", Font.PLAIN, 30));
        gameName.setBackground(Color.BLUE);
        gameName.setForeground(Color.ORANGE);
        gameName.setOpaque(true);

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == startGameButton) {
                    try {
                        client = new Client("127.0.0.1", 12345);
                        while (settingUp) {
                            o = sendAndReceive("PropertiesRequest");
                            if (o instanceof int[]) {
                                properties = (int[]) o;
                                numbOfQuest = properties[0];
                                numbOfRounds = properties[1];
                                System.out.println("Frågor: " + properties[0]);
                                System.out.println("Rundor: " + properties[1]);
                                settingUp = false;
                            }
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("Startar sökning efter en motståndare -> Startar upp en GameInstance och öppnar upp spelmenyn");
                    if (!enterNameField.getText().isBlank())
                        playerName.setText(enterNameField.getText());
                    frame.getContentPane().removeAll();
                    getGameMenu();
//                    updateScore(); //Ska hämta all info, spelarnamn, boolean-poäng-array(s),

//                    frame.repaint();
//                    frame.revalidate();


                } /*else if (e.getSource() == settingsButton) {
                    System.out.println("Öppnar upp en ny JPanel med \"settingsknappar\" som går att justera. Det ska också finnas en apply-knapp");
                }*/ else if (e.getSource() == play) {
                    try {
                        playRound();
                        if (currentRound >= numbOfRounds) {
                            play.setText("Avsluta spel");
                        }
                    } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };

        startGameButton.addActionListener(buttonListener);
        //settingsButton.addActionListener(buttonListener);
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
                categoryArray.add(new JLabel(s));
                categoryArray.get(j).setFont(f);
                categoryArray.get(j).setForeground(Color.ORANGE);
            }
        }

//        play = new JButton("Spela");
//        play.addActionListener(MainMenuGUI.buttonListener);

        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.ORANGE);
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLUE);
        JPanel playerScorePanel = new JPanel(new GridLayout(numbOfRounds, numbOfRounds));
        playerScorePanel.setBackground(Color.BLUE);
        JPanel opponentScorePanel = new JPanel(new GridLayout(numbOfRounds, numbOfRounds));
        opponentScorePanel.setBackground(Color.BLUE);
        JPanel categoryPanel = new JPanel(new GridLayout(numbOfRounds, 1));
        categoryPanel.setBackground(Color.BLUE);
        play.setBackground(Color.ORANGE);
        play.setPreferredSize(new Dimension(50, 50));
        play.setOpaque(true);

        for (int i = 0; i < numbOfRounds * numbOfQuest; i++) {
            playerScorePanel.add(playerScoreArray.get(i));
            opponentScorePanel.add(opponentScoreArray.get(i));

            if (i % 3 == 0)
                categoryPanel.add(categoryArray.get(i / 3));
        }

        gameMenu.add(northPanel, BorderLayout.NORTH);
        gameMenu.add(centerPanel, BorderLayout.CENTER);
        gameMenu.add(play, BorderLayout.SOUTH);

        northPanel.setLayout(new GridLayout(1, 3));
        northPanel.setPreferredSize(new Dimension(50, 50));
        playerName.setForeground(Color.WHITE);
        playerName.setBackground(Color.ORANGE);
        playerName.setOpaque(true);
        playerName.setHorizontalAlignment(SwingConstants.CENTER);
        opponentName.setForeground(Color.WHITE);
        opponentName.setBackground(Color.ORANGE);
        opponentName.setOpaque(true);
        opponentName.setHorizontalAlignment(SwingConstants.CENTER);
        currentScore.setHorizontalAlignment(SwingConstants.CENTER);
        northPanel.add(playerName);
        northPanel.add(currentScore);
        northPanel.add(opponentName);

        centerPanel.add(playerScorePanel);
        centerPanel.add(categoryPanel);
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
                            categoryArray.get(j).setText(s);
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
        } else {
            frame.getContentPane().removeAll();
            getStartMenu();
            client.close();
            play.setText("Spela");
            playerName.setText("Ditt namn");
            opponentName.setText("Motståndare");
            currentScore.setText("0-0");
            //playerScoreArray = new ArrayList<>();
            //opponentScoreArray = new ArrayList<>();
            //categoryArray = new ArrayList<>();
            currentRound = 0;
            settingUp = true;
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