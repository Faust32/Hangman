import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

class Hangman{

    public static void main(String[] args) throws IOException {
        System.out.println("hello, world!");
        if (welcome() == 1) startGame();
        else System.exit(0);
    }
    public static Scanner scanner = new Scanner(System.in);

    public static String makeArt(int a) {
        String[] art = {
                "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
        };
        return art[a];
    }

    public static int welcome(){
        System.out.println("Привет! Добро пожаловать в \"Виселицу\". Правила игры просты: тебе дается некоторое слово" +
                ", которое тебе предстоит угадать. \nТы можешь совершить 6 ошибок, последняя из которых станет финальной" +
                ". Чтобы начать игру, введи в консоль 1. Если хочешь выйти, введи 0. ");
        return scanner.nextInt();
    }

    public static void startGame() throws IOException {
        String originalWord = wordGenerator();
        String blankTest = String.valueOf('*').repeat(originalWord.length());
        String[] blank = blankTest.split("");
        for (String i : blank){
            System.out.printf("%s", i);
        }
        System.out.println("\n");
        int mistakes_count = 0;
        while (mistakes_count<6) {
            if (scanner.hasNextLine()) {
                String inputSymbol = scanner.next();
                if (checkIfCharInBlank(blank, inputSymbol.toLowerCase())) {
                    System.out.println("Эта буква уже использовалось, попробуй другую.");
                    continue;
                }
                else mistakes_count = mistakeHandler(blank, originalWord, inputSymbol.toLowerCase(), mistakes_count);
            }
            System.out.printf("%s", makeArt(mistakes_count));
            System.out.println("\n");
            for (String i : blank){
                System.out.printf("%s", i);
            }
            System.out.println("\n");
            checkIfVictory(blank, originalWord);
        }
        if (mistakes_count==6) endGame(false, originalWord);
    }

    public static String wordGenerator() throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get("src/dictionary.txt")));
        String[] dictionary = fileContent.split(" ");
        Random randomIndex = new Random();
        int wordIndex = randomIndex.nextInt(dictionary.length);
        return dictionary[wordIndex];
    }


    public static int mistakeHandler(String[] blank, String originalWord, String symbol, int mistakesCount){
        if (!originalWord.toLowerCase().contains(symbol)) {
            mistakesCount++;
        }
        else {
            putCharInBlank(blank, originalWord, symbol, mistakesCount);
        }
        return mistakesCount;
    }
    public static void putCharInBlank(String[] blank, String originalWord, String symbol, int mistakesCount){
        for (int i = 0; i < blank.length; i++) {
                if (originalWord.charAt(i) == symbol.charAt(0)) blank[i] = (symbol);
        }

    }
    public static boolean checkIfCharInBlank(String[] blank, String inputSymbol) {
        for (String c : blank) {
            if (Objects.equals(c, inputSymbol)) {
                return true;
            }
        }
        return false;
    }
    public static void checkIfVictory(String[] blank, String originalWord) throws IOException {
        String givenWord = String.join(",", blank).replaceAll(",", "");
        String cleanedStr = originalWord.replaceAll("[\\[\\],\\s]", "");
        if (Objects.equals(givenWord, cleanedStr)) {
            endGame(true, originalWord);
        }
    }
    public static void endGame(boolean end, String correctWord) throws IOException {
        if (end) {
            System.out.println("Поздравляю с победой! Не хочешь сыграть еще раз? 1 - да, 0 - нет.\n");
            int input = scanner.nextInt();
            if (input != 1) System.exit(0);
            else startGame();
        }
        else {
            System.out.printf("Мда, не повезло. Вот загаданное слово: %s. Не хочешь сыграть еще раз? 1 - да, 0 - нет.\n", correctWord);
            int input = scanner.nextInt();
            if (input != 1) System.exit(0);
            else startGame();
        }
    }

}