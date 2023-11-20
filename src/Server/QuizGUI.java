package Server;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizGUI {

    private static void GUI() {
        JFrame frame = new JFrame("Quiz GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(4, 1));

        JLabel categoryLabel = new JLabel("Välj en kategori");
        JButton categoryButton1 = new JButton("Kategori 1");
        JButton categoryButton2 = new JButton("Kategori 2");
        JButton categoryButton3 = new JButton("Kategori 3");

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryButton1);
        categoryPanel.add(categoryButton2);
        categoryPanel.add(categoryButton3);

        frame.getContentPane().add(categoryPanel);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(5, 1));

        JLabel questionLabel = new JLabel("(Fråga)");
        JButton questionButton1 = new JButton("Svar 1");
        JButton questionButton2 = new JButton("Svar 2");
        JButton questionButton3 = new JButton("Svar 3");
        JButton questionButton4 = new JButton("Svar 4");

        questionPanel.add(questionLabel);
        questionPanel.add(questionButton1);
        questionPanel.add(questionButton2);
        questionPanel.add(questionButton3);
        questionPanel.add(questionButton4);

        categoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(questionPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        categoryButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(questionPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        categoryButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(questionPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        frame.setVisible(true);
    }
}
