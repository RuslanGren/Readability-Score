package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int[] age = {6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 22, 25};
        String str = readFileAsString(args[0]);
        double sentences = 0;
        double words = 0;
        double syllables = 0;
        double polysyllables = 0;
        for (String sentence : str.split("[?!.]")) {
            sentences++;
            if (sentence.startsWith(" ")) sentence = sentence.substring(1);
            for (String word : sentence.split(" ")) {
                words++;
                int countSyllable = countSyllable(word);
                syllables += countSyllable;
                if (countSyllable > 2) polysyllables++;
            }
        }
        double characters = str.replace(" ", "").length();
        print(words, sentences, characters, syllables, polysyllables);
        System.out.println();
        double scoreARI = 4.71 * (characters/words) + 0.5 * (words/sentences) - 21.43;
        double scoreFK = 0.39 * (words/sentences) + 11.8 * (syllables/words) - 15.59;
        double scoreSMOG = 1.043 * Math.sqrt(polysyllables * (30/sentences)) + 3.1291;
        double scoreCL = 0.0588 * (characters/words*100) - 0.296 * (sentences/words*100) - 15.8;
        String action = scanner.nextLine();
        switch (action) {
            case ("ARI") -> System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n", scoreARI, age[(int) Math.ceil(scoreARI)-1]);
            case ("FK") -> System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n", scoreFK, age[(int) Math.ceil(scoreFK)-1]);
            case ("SMOG") -> System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", scoreSMOG, age[(int) Math.ceil(scoreSMOG)-1]);
            case ("CL") -> System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n", scoreCL, age[(int) Math.ceil(scoreCL)-1]);
            case ("all") -> {
                System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n", scoreARI, age[(int) Math.ceil(scoreARI)-1]);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n", scoreFK, age[(int) Math.ceil(scoreFK)-1]);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", scoreSMOG, age[(int) Math.ceil(scoreSMOG)-1]);
                System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n", scoreCL, age[(int) Math.ceil(scoreCL)-1]);
            }
            default -> System.out.println("unknown command");
        }
    }

    private static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    private static int countSyllable(String word) {
        int syllable_count = 0;
        if (contain(word, 0)) syllable_count++;
        for (int index = 1; index < word.length(); index++) {
            if (contain(word, index) && !contain(word, index - 1)) syllable_count++;
        }
        if (word.endsWith("e")) syllable_count--;
        if (word.endsWith("le") && word.length() > 2 && !contain(word, word.length() - 3)) syllable_count++;
        if (syllable_count == 0) syllable_count++;
        return syllable_count;
    }

    private static boolean contain(String word, int index) {
        char[] vowels = {'a', 'e', 'i', 'o', 'u', 'y'};
        char character = word.charAt(index);
        for (char ch : vowels) {
            if (ch == character) return true;
        }
        return false;
    }

    private static void print(double words, double sentences, double characters, double syllables, double polysyllables) {
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
    }
}
