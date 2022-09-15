// import statements here

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * main
 * <p>
 * brief description of the program
 * <p>
 * a list of your sources of help (if any)
 *
 * @author Justin L, LC1
 * @version 21/03/2022
 */

public class runWordleSolver {
    static String dictEN = "word_unique_vowel.txt";
    static String dictES = "word_unique_vowel.txt";
    static String dictESTilde = "word_unique_vowel.txt";

    public static void main(String[] args) {

        //catch dictionary language
        Scanner in = new Scanner(System.in);
        System.out.println("1 = English; 2 = Español; 3 = Español con tildes;");
        int input = in.nextInt();

        WordleSolver solver = null;
        try {
            //language select
            switch (input) {
                case 1:
                    solver = new WordleSolver("word_unique_vowel.txt");
                    break;
                case 2:
                    solver = new WordleSolver("word_unique_vowel_es.txt");
                    break;
                case 3:
                    solver = new WordleSolver("word_unique_vowel_es_tilde.txt");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            ArrayList<Word> guesses = null;
            try {
                guesses = solver.run();
            } catch (IOException e) {
                System.out.println("Input file not found.");
            }

            //wrong size input catch, default behaviour is exit
            if ( guesses == null) {
                System.out.print("Exiting...");
                System.exit(1);
            }
            //update next word list in UI
            //print choices
            Word choice = null;
            try {
                choice = guesses.get(0);
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                //TODO add new exception type
                System.out.println("No possible choices, check your inputs or update dictionary.");
                choice = new Word("",0,0);
            }

            for (Word w : guesses) {
                if (w.getNumUnique() > choice.getNumUnique()) {
                    choice = w;
                }
            }
            int prevCount = solver.getWordSize();
            String output = "A good choice would be \"" + choice.getWord() + "\" which could remove " + solver.getBestRemoved() + "/" + prevCount + " words.\n" +
                    "Number of Possible Words: " + solver.getWordSize() + " Words Eliminated: " + (solver.getInitialWordNum() - solver.getWordSize()) + "\n";
            System.out.print(output);
        }
    }
}
