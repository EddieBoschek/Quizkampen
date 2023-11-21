package Server;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizGUI {

    static Question askedQuest = null;

    public static void GUI() {
        JFrame frame = new JFrame("Quiz GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new FlowLayout());

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(4, 1));

        Category category = new Category();
        String[] categories = category.shuffleCategories();

        JLabel categoryLabel = new JLabel("Välj en kategori");
        JButton categoryButton1 = new JButton(categories[0]);
        JButton categoryButton2 = new JButton(categories[1]);
        //JButton categoryButton3 = new JButton("Kategori 3");

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryButton1);
        categoryPanel.add(categoryButton2);
        //categoryPanel.add(categoryButton3);

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

        QuestionCollection qCollection = new QuestionCollection("qCollection");
        //Question[] questions = new Question[3];


        categoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                Question[] questions = qCollection.getSubjectQuestion(categoryButton1.getText());
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

        categoryButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                Question[] questions = qCollection.getSubjectQuestion(categoryButton2.getText());
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
    }
}
