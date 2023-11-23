package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    Socket s;
    public Server(Socket s) {
        this.s = s;
    }

    public void run() {

        ArrayList<Category> categories = new ArrayList<>();
        Category math = new Category("Math", Category.readDataFromFile("src/Server/TextFiles/Math"));
        Category history = new Category("History", Category.readDataFromFile("src/Server/TextFiles/History"));
        Category science = new Category("Science", Category.readDataFromFile("src/Server/TextFiles/Science"));
        Category music = new Category("Music", Category.readDataFromFile("src/Server/TextFiles/Music"));
        Category sports = new Category("Sports", Category.readDataFromFile("src/Server/TextFiles/Sports"));
        Category geography = new Category("Geography", Category.readDataFromFile("src/Server/TextFiles/Geography"));

        categories.add(math);
        categories.add(history);
        categories.add(science);
        categories.add(music);
        categories.add(sports);
        categories.add(geography);

        try(
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());)
        {
            String inputLine;
            while ((inputLine = (String)in.readObject()) != null) {
                if (inputLine.equals("Start")) {
                    out.writeObject(Category.shuffleCategories(categories));
                    //out.reset();
                } else {
                    out.writeObject(Category.getSubjectQuestion(inputLine, categories));
                    //out.reset();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}