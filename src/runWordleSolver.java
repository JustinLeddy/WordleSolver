// import statements here

import java.io.IOException;
import java.util.ArrayList;

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
    public static void main(String[] args) {
        while (true) {
            WordleSolver solver = new WordleSolver("word_unique_vowel.txt");
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
            }

            for (Word w : guesses) {
                if (w.getNumUnique() > choice.getNumUnique()) {
                    choice = w;
                }
            }
            String output = "A good choice would be \"" + choice.getWord() + "\" which could remove " + solver.getBestRemoved() + "/" + solver.getWordSize() + " words.\n" +
                    "Number of Possible Words: " + solver.getWordSize() + " Words Eliminated: " + (solver.getInitialWordNum() - solver.getWordSize()) + "\n";
            System.out.print(output);
        }
    }
}
