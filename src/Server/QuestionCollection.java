package Server;

import POJOs.Question;

import java.util.*;
import java.util.List;

public class QuestionCollection {
    private ArrayList<Question> qList = new ArrayList<>();
    private String subjectName;

    public static Question[] getSubjectQuestion(String chosenSubject) {
        ArrayList<ArrayList<Question>> subjectList = new ArrayList<>();
        ArrayList<Question> math = new ArrayList<>();
        ArrayList<Question> history = new ArrayList<>();
        ArrayList<Question> science = new ArrayList<>();
        ArrayList<Question> music = new ArrayList<>();
        ArrayList<Question> sports = new ArrayList<>();
        ArrayList<Question> geography = new ArrayList<>();

    public QuestionCollection(String subjectName) {
        this.subjectName = subjectName;
    }
        subjectList.add(math);
        subjectList.add(history);
        subjectList.add(science);
        subjectList.add(music);
        subjectList.add(sports);
        subjectList.add(geography);


    public void addToList(Question q) {
        this.qList.add(q);
    }

    public Question getFromQList(int i) {
        return qList.get(i);
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Question[] getSubjectQuestion(String chosenSubject) {
        ArrayList<QuestionCollection> subjectList = new ArrayList<>();
        QuestionCollection math = new QuestionCollection("math");
        QuestionCollection history = new QuestionCollection("history");
        QuestionCollection science = new QuestionCollection("science");




        //Questions

        //Math
        List<String> answerOptions = Arrays.asList("-1", "0", "1", "-3");
        math.addToList(new Question("Vad är -1^3?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("19/6", "8", "17/2", "11/6");
        math.addToList(new Question("Vad är 2*4+3/6?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("21378", "21212", "40000", "98765");
        math.addToList(new Question("Vilket av följande tal är jämnt delbart med 3?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("2", "0", "156", "10");
        math.add(new Question("Vilket är det enda jämna primtalet?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("15", "14", "16", "13");
        math.add(new Question("Vilket tal kommer näst i denna serie: 53, 53, 40, 40, 27, 27...?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("86 400", "90 000", "82 700", "62 700");
        math.add(new Question("Hur många sekunder är det på en dag?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("Aristoteles", "Platon", "Arkimedes", "Socrates");
        math.add(new Question("Vem är matematikens fader?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Romber", "Kvadrater", "Cirklar", "Trianglar");
        math.add(new Question("Pythagoras utvecklade en teori om vilka former?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("Prim-", "Hel-", "Grund-", "Grå-");
        math.add(new Question("Alla naturliga tal och 0 kallas för ...-talen.", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("155 + 802", "718 + 239", "649 + 308", "509 + 458");
        math.add(new Question("Vilket av följande ger inte svaret: 957", answerOptions, answerOptions.get(3)));


        //History
        answerOptions = Arrays.asList("1609", "1462", "1523", "1789");
        history.addToList(new Question("Vilket år blev Gustav Vasa kung över Sverige?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Tyskland", "Polen", "Frankrike", "Italien");
        history.addToList(new Question("I vilket land stormade de allierade stränderna i Normandie under andra världskriget?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("1904", "1908", "1910", "1914");
        history.addToList(new Question("När startade första världskriget?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("1803", "1789", "1689", "1723");
        history.add(new Question("Vilket år började den franska revolutionen?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("19", "49", "29", "39");
        history.add(new Question("Hur gammal var kung Tutankhamon när han dog?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("3", "5", "7", "9");
        history.add(new Question("Hur många kullar byggdes antikens rom på?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("1989", "1978", "2003", "1994");
        history.add(new Question("Vilket år revs Berlinmuren?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("USA", "Indien", "Irland", "Sydafrika");
        history.add(new Question("Vilket land separerades från storbrittanien 1921", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Picasso", "Michelangelo", "Da Vinci", "Pinturicchio");
        history.add(new Question("Vem målade taket i det sixtinska kapellet?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Kambodia", "Kina", "Filippinerna", "Vietnam");
        history.add(new Question("De Röda Khmererna var en regim som styrde vilket land?", answerOptions, answerOptions.get(0)));


        //Science
        answerOptions = Arrays.asList("2000", "2006", "2010", "2013");
        science.addToList(new Question("Vilket år slutade Pluto att klassificeras som en planet?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("0", "1", "2", "3");
        science.addToList(new Question("Hur många hjärtan har bläckfiskar?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("-40", "0", "70", "150");
        science.addToList(new Question("Vid vilket temperatur är Celcius och Farenheit lika?", answerOptions, answerOptions.get(0)));

        subjectList.add(math);
        subjectList.add(history);
        subjectList.add(science);

        answerOptions = Arrays.asList("156", "306", "106", "206");
        science.add(new Question("Hur många ben finns det i människokroppen", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("Hjärtat", "Huden", "Hjärnan", "Magsäcken");
        science.add(new Question("Vilket är kroppens största organ?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Penicillin", "Svampologi", "Mykologi", "Fungologi");
        science.add(new Question("Vad kallas läran om svampar?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("157", "87", "135", "118");
        science.add(new Question("Hur många ämnen är listade på det periodiska systemet?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("122", "0", "202", "228");
        science.add(new Question("Hur många ben har en haj i kroppen?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("AB-", "A-", "B-", "O");
        science.add(new Question("Vilken blodgrupp är mest ovanlig?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("67%", "77%", "87%", "97%");
        science.add(new Question("Hur många procent av Jordens vatten är saltvatten?", answerOptions, answerOptions.get(3)));


        //Music
        answerOptions = Arrays.asList("Tina Turner", "Madonna", "Cyndi Lauper", "Whitney Houston");
        music.add(new Question("Vilken 80-talsstjärna erkänns av Guinness World Records som den bästsäljande kvinnliga inspelningsartisten genom tiderna?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("England", "Sverige", "Irland", "Norge");
        music.add(new Question("Vilket är det enda landet som har vunnit 3 Eurovision Song Contests i rad (1992, 1993 och 1994)?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Gnarls Barkley", "Eminem", "Britney Spears", "Nelly Furtado");
        music.add(new Question("Rolling Stone Magazine nummer 1 på 2000-talet var 'Crazy', av vem?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("Metallica", "The Killers", "Judas Priest", "Bon Jovi");
        music.add(new Question("\"Livin' on a Prayer\" är en klassisk låt av vilket rockband?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("Gitarr", "Munspel", "Trummor", "Piano");
        music.add(new Question("Huey, från Huey Lewis and the News, spelade vilket instrument?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Beethoven", "Mozart", "Brahms", "Stravinsky");
        music.add(new Question("Vilken av dessa klassiska kompositörer var döv?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("Kings of Leon", "30 Seconds to Mars", "Coldplay", "Imagine Dragons");
        music.add(new Question("Jared Leto är sångare för vilket band?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Eazy-E", "Snoop Dogg", "Ice Cube", "Dr. Dre");
        music.add(new Question("Vilken av dessa artister var inte med i gruppen N.W.A?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Jenny", "Janice", "Jessica", "Jane");
        music.add(new Question("Vad är Mary J. Bliges mellannamn?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("1992", "1982", "1987", "1997");
        music.add(new Question("Vilket år vann Lotta Engberg Melodifestivalen?", answerOptions, answerOptions.get(2)));


        //Sports
        answerOptions = Arrays.asList("Tyskland", "Spanien", "Brasilien", "Argentina");
        sports.add(new Question("Vilket land vann herrarnas Fotbolls-EM 2012?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Lacrosse", "Ishockey", "Fotboll", "Baseball");
        sports.add(new Question("Vad är nationalsporten i Kanada?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("Polo", "Tennis", "Golf", "Ishockey");
        sports.add(new Question("Vilken av följande sporter använder inte en boll??", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("Freestyle", "Fjäril", "Hundsim", "Ryggsim");
        sports.add(new Question("Vilken simstil är inte tillåten i OS?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Tyskland", "Japan", "USA", "Singapore");
        sports.add(new Question("Var ligger det största bowlingcentret?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Varpa", "Pärk", "Rövkrok", "Säckhoppning");
        sports.add(new Question("Vilken av dessa är inte en klassisk gutnisk lek?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("Rött & Blått", "Svart % Gult", "Grönt & svart", "Vitt & gult");
        sports.add(new Question("Vilka färger är typiska för fotbollslaget Helsingsborgs IF?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("Cykling", "Simning", "Skidor", "Löpning");
        sports.add(new Question("Vilken av dessa grenar ingår inte i Iron Man?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Danmark", "Sverige", "Norge", "Finland");
        sports.add(new Question("Vilket land kommer att spela fotbolls EM 2024?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("Häcken", "Hammarby", "Umeå", "Rosengård");
        sports.add(new Question("Vilket lag vann Damallsvenskan 2023?", answerOptions, answerOptions.get(1)));


        //Geography
        answerOptions = Arrays.asList("Lima", "Asunción", "Montevideo", "Rio de Janeiro");
        geography.add(new Question("Vilken av dessa Sydamerikanska städer är inte en huvudstad?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("Vatikanstaten", "Lichtenstein", "San Marino", "Monaco");
        geography.add(new Question("Vilket är världens minsta land?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("Nairobi", "Thies", "Dakar", "Abidjan");
        geography.add(new Question("Vad heter Senagals huvudstad?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("1", "4", "9", "6");
        geography.add(new Question("Hur många stjärnor har Australiens flagga?", answerOptions, answerOptions.get(3)));

        answerOptions = Arrays.asList("Neapel", "Venedig", "Milano", "Rom");
        geography.add(new Question("Vilken stad i Italien är känd för sina kanaler?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Brasilia", "Rio de Janeiro", "Amazonas", "Rio de la Plata");
        geography.add(new Question("Vad heter floden som rinner genom Brasiliens regnskog?", answerOptions, answerOptions.get(2)));

        answerOptions = Arrays.asList("Italien", "Hawaii", "Island", "Indonesien");
        geography.add(new Question("Var finns den största vulkanen på jorden?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Ecuador", "Bolivia", "Chile", "Paraguay");
        geography.add(new Question("I vilket land är Quito huvudstad?", answerOptions, answerOptions.get(0)));

        answerOptions = Arrays.asList("Jamaica", "Cypern", "Filippinerna", "Indonesien");
        geography.add(new Question("I vilket land är Nicosia huvudstad?", answerOptions, answerOptions.get(1)));

        answerOptions = Arrays.asList("Rwanda", "Mali", "Uganda", "Somalia");
        geography.add(new Question("I vilket land är Mogadishu huvudstad?", answerOptions, answerOptions.get(3)));


        Question[] questions = new Question[3];
        Random r = new Random();
        int numberOfSubjects = subjectList.size();
        for (QuestionCollection subject : subjectList) {
            if (subject.getSubjectName().equalsIgnoreCase(chosenSubject)) {
                for (int i = 0; i < 3; i++) {
                    int randNumber = r.nextInt(1000);
                    int index = randNumber % numberOfSubjects;
                    Question qRand = subject.getFromQList(index);

                    if (questions[0] != null && questions[0].equals(qRand) || questions[1] != null &&
                            questions[1].equals(qRand) || questions[2] != null && questions[2].equals(qRand)) {
                        i--;
                    }
                    else{
                        questions[i] = qRand;
                    }
                }
            }
        }
        return questions;
    }

}
