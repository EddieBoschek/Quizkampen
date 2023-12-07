package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MainMenuGUI {
    JButton startGameButton = new JButton("Starta nytt spel");
    JButton play = new JButton("Spela");
    JLabel gameName = new JLabel("Quizkampen", SwingConstants.CENTER);
    JPanel menuPanelMaster = new JPanel();
    JPanel menuSubPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JFrame frame = new JFrame();
    int numbOfRounds, numbOfQuest, opponentScoreCounter, playerScoreCounter;
    Client client;
    JLabel playerName = new JLabel("Spelare 1");
    JLabel opponentName = new JLabel("Spelare 2");
    JLabel enterName = new JLabel("Skriv in ditt namn: ");
    JTextField enterNameField = new JTextField();
    JLabel currentScore = new JLabel("0-0");
    ArrayList<JLabel> playerScoreArray = new ArrayList<>();
    ArrayList<JLabel> opponentScoreArray = new ArrayList<>();
    ArrayList<JLabel> categoryArray = new ArrayList<>();
    int currentRound = 0;
    int[] properties;
    boolean settingUp = true;
    boolean playersReady = true;
    Object serverMessage;
    boolean firstRound = true;
    boolean startButtonClicked = false;

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
        playersReady = true;

        buttonsPanel.setLayout(new GridLayout(3, 1));
        buttonsPanel.setSize(300, 200);
        enterName.setHorizontalAlignment(SwingConstants.CENTER);
        enterName.setFont(new Font("San Serif", Font.PLAIN, 16));
        buttonsPanel.add(enterName);
        buttonsPanel.add(enterNameField);
        buttonsPanel.add(startGameButton);
        buttonsPanel.setBackground(Color.ORANGE);

        Dimension d = new Dimension(250, 80);

        startGameButton.setPreferredSize(d);
        startGameButton.setFont(new Font("San Serif", Font.PLAIN, 18));

        gameName.setPreferredSize(d);
        gameName.setFont(new Font("Serif", Font.PLAIN, 30));
        gameName.setBackground(Color.BLUE);
        gameName.setForeground(Color.ORANGE);
        gameName.setOpaque(true);

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == startGameButton) {
                    if (!startButtonClicked) {
                        if (!enterNameField.getText().isBlank())
                            playerName.setText(enterNameField.getText());
                        try {
                            {
                                client = new Client("127.0.0.1", 12345);
                            }
                            while (settingUp) {
                                serverMessage = sendAndReceive("SettingUp" + playerName.getText());
                                if (serverMessage instanceof int[]) {
                                    properties = (int[]) serverMessage;
                                    numbOfQuest = properties[0];
                                    numbOfRounds = properties[1];

                                    playerScoreCounter = 0;
                                    opponentScoreCounter = 0;
                                    playersReady = true;
                                    play.setText("Spela");

                                    for (Object l:playerScoreArray) {
                                        ((JLabel) l).setText("□");
                                        ((JLabel) l).setForeground(Color.black);
                                    }
                                    for (Object l:opponentScoreArray) {
                                        ((JLabel) l).setText("□");
                                        ((JLabel) l).setForeground(Color.black);
                                    }
                                }
                                serverMessage = receive();
                                if (serverMessage instanceof String) {
                                    if (serverMessage.equals("Spelare 1")) {
                                        opponentName.setText("Spelare 2");
                                    } else {
                                        opponentName.setText((String) serverMessage);
                                    }
                                }
                                settingUp = false;
                                playersReady = true;
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        frame.getContentPane().removeAll();
                        getGameMenu();
                        startButtonClicked = true;
                    }
                } else if (e.getSource() == play) {
                    if (playersReady) {
                        try {
                            playRound();
                                play.setText("Få poäng");
                        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        playersReady = false;
                        frame.repaint();
                        frame.revalidate();

                    } else {
                        try {
                            updateScoreAll();
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        playersReady = true;
                        frame.repaint();
                        frame.revalidate();
                    }
                }
            }
        };

        startGameButton.addActionListener(buttonListener);
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
            JLabel jlb = new JLabel(String.valueOf(c));
            JLabel jlb2 = new JLabel(String.valueOf(c));
            jlb.setForeground(Color.black);
            jlb2.setForeground(Color.black);
            playerScoreArray.add(jlb);
            playerScoreArray.get(i).setFont(f);
            opponentScoreArray.add(jlb2);
            opponentScoreArray.get(i).setFont(f);
        }
        for (int i = 0; i < (numbOfRounds + 1); i++) {
            categoryArray.add(new JLabel(String.valueOf(i + 1)));
            categoryArray.get(i).setFont(f);
            categoryArray.get(i).setForeground(Color.ORANGE);
        }

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

            if (i % numbOfQuest == 0)
                categoryPanel.add(categoryArray.get(i / numbOfQuest));
        }

        gameMenu.add(northPanel, BorderLayout.NORTH);
        gameMenu.add(centerPanel, BorderLayout.CENTER);
        gameMenu.add(play, BorderLayout.SOUTH);

        northPanel.setLayout(new GridLayout(1, 3));
        northPanel.setPreferredSize(new Dimension(50, 50));
        playerName.setForeground(Color.WHITE);
        playerName.setBackground(Color.ORANGE);
        playerName.setOpaque(true);
        playerName.setHorizontalAlignment(SwingConstants.RIGHT);
        playerName.setFont(new Font("San Serif", Font.PLAIN, 16));
        opponentName.setForeground(Color.WHITE);
        opponentName.setBackground(Color.ORANGE);
        opponentName.setOpaque(true);
        opponentName.setHorizontalAlignment(SwingConstants.LEFT);
        opponentName.setFont(new Font("San Serif", Font.PLAIN, 16));
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
    public void updateScore() throws IOException {

        send("GameUpdateRequest");
        Object input = null;
        int i = 0;
        boolean[][] playerBoolArray = new boolean[numbOfRounds][numbOfQuest];
        boolean[][] opponentBoolArray = new boolean[numbOfRounds][numbOfQuest];

            while (true) {
                input = receive();

                while(input == null) {}

                if (input.equals("END")) {
                    System.out.println("END");
                    break;
                }
                switch (i) {
                    case 0 -> playerBoolArray = (boolean[][]) input;
                    case 1 -> opponentBoolArray = (boolean[][]) input;
                    case 2 -> {
                        if (!firstRound) {
                            for (int j = 0; j < numbOfRounds; j++) {
                                String s = ((String[]) input)[j];
                                if (s != null)
                                    categoryArray.get(j).setText(s);
                            }
                        }
                    }
                }
                i++;
            }

        firstRound = false;

        char f = '\u2612';
        char r = '\u2611';

            for (int k = 0; k < numbOfQuest; k++) {
                if (playerBoolArray[currentRound - 1][k]) {
                    playerScoreArray.get(k + (currentRound - 1) * numbOfQuest).setText(String.valueOf(r));
                    playerScoreArray.get(k + (currentRound - 1) * numbOfQuest).setForeground(Color.GREEN);
                    playerScoreCounter++;
                } else {
                    playerScoreArray.get(k + (currentRound - 1) * numbOfQuest).setText(String.valueOf(f));
                    playerScoreArray.get(k + (currentRound - 1) * numbOfQuest).setForeground(Color.RED);
                } if (opponentBoolArray[currentRound - 1][k]) {
                    opponentScoreArray.get(k + (currentRound - 1) * numbOfQuest).setText(String.valueOf(r));
                    opponentScoreArray.get(k + (currentRound - 1) * numbOfQuest).setForeground(Color.GREEN);
                    opponentScoreCounter++;
                } else {
                    opponentScoreArray.get(k + (currentRound - 1) * numbOfQuest).setText(String.valueOf(f));
                    opponentScoreArray.get(k + (currentRound - 1) * numbOfQuest).setForeground(Color.RED);
                }
            }

        currentScore.setText(playerScoreCounter + " - " + opponentScoreCounter);

        client.flushOutput();
        frame.repaint();
        frame.revalidate();

        if (currentRound >= numbOfRounds) {
            if (opponentScoreCounter > playerScoreCounter) {
                play.setText("Du förlorade. Avsluta spel");
            } else if (opponentScoreCounter == playerScoreCounter) {
                play.setText("Oavgjort. Avsluta spel");
            } else {
                play.setText("Du vann! Avsluta spel");
            }
        } else {
            play.setText("Spela");
        }
    }

    public void playRound() throws IOException, ClassNotFoundException, InterruptedException {
        if (currentRound < numbOfRounds) {
            QuizGUI quizGUI = new QuizGUI(client, currentRound, properties, playerName.getText(), opponentName.getText());
            currentRound++;

        } else {
            System.exit(0);
        }
    }

    public void send(Object message) {
        if (message != null) {
            try {
                client.Send(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Object receive() {
        Object receivedMessage = null;
        try {
            receivedMessage = client.Receive();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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

    public void updateScoreAll() throws IOException, ClassNotFoundException {
        Object input = null;
        send("BothPlayersHaveAnsweredQuestions" + currentRound);

        input = receive();

        if (input.equals("BothPlayersHaveAnsweredQuestions" + currentRound)) {
            updateScore();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainMenuGUI mainMenuGUI = new MainMenuGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}