package Server;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Category {
    private List<String> categories = Arrays.asList("Math", "History", "Science", "Music", "Sports", "Geography");

    public String[] shuffleCategories() {
        String[] returnList = new String[5];
        Random random = new Random();
        int randomNumber;
        boolean inArray;

        for (int i = 0; i < returnList.length; i++) {
            do {
                randomNumber = random.nextInt(categories.size());
                inArray = false;
                for (String s : returnList) {
                    if (categories.get(randomNumber).equals(s)) {
                        inArray = true;
                        break;
                    }
                }
            } while (inArray);
            returnList[i] = categories.get(randomNumber);
        }
        return returnList;
    }
}