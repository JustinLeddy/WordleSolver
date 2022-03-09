// import statements here

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

/**
 * IdealWordSequence
 * <p>
 * brief description of the program
 * <p>
 * a list of your sources of help (if any)
 *
 * @author Justin L, LC1
 * @version 02/03/2022
 */

public class IdealWordSequence {
    private static String[] vowels = {"a", "e", "i", "o", "u"};

    public static void main(String[] args) throws IOException { //TODO: account for words ont in the list that may reduce it's size by a larger amount
        ArrayList<String> confirmedLetters = new ArrayList<>();
        while (true) {
            //prompt user for known letters / positions
            System.out.println("Input known letters / position (_,_,_,_,_):");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            input = input.trim();
            input = input.toLowerCase();
            String[] letters = input.split(",");


            //variables
            ArrayList<Word> words = new ArrayList<>();
            ArrayList<Word> nonFitWords = new ArrayList<>();

            //check if reset or not
            int initialWordNum = 0;
            BufferedReader br = new BufferedReader(new FileReader("possible_words.txt"));
            if (letters.length > 5) {
                br.close();
                confirmedLetters = new ArrayList<>();
                br = new BufferedReader(new FileReader("word_unique_vowel.txt"));
            }


            //loop through file
            String line;
            line = br.readLine();
            while (line != null) {
                initialWordNum++;
                if (line.length() > 0) {
                    String[] content = line.split(" ");
                    //check if word fits input parameters
                    boolean fitsInput = true;
                    boolean badLetter = false;
                    for (int i = 0; i < 5; i++) {

                        //check if we already guessed a letter in that spot
                        if (fitsInput) {
                            if (letters[i].charAt(0) == '!') { //letter in word
                                String letter = letters[i].substring(1, 2);
                                confirmedLetters.add(letter);
                                //check if word contains letter not in this spot.
                                if (!(content[0].contains(letter) && !content[0].substring(i, i + 1).equals(letter))) {
                                    fitsInput = false;
                                }
                            } else if (letters[i].charAt(0) == '^') { //confirmed letter
                                String letter = letters[i].substring(1, 2);
                                confirmedLetters.add(letter);
                                //check if word matches, if not remove
                                if (!(letter.equals(content[0].substring(i, i + 1)))) {
                                    fitsInput = false;
                                }
                            } else { //letter not found in final word
                                //check if word contains this letter
                                if (content[0].contains(letters[i]) && !confirmedLetters.contains(letters[i])) {
                                    fitsInput = false;
                                    badLetter = true;
                                }
                            }
                        }
                    }
                    if (fitsInput) {
                        words.add(new Word(content[0].toLowerCase(), Integer.parseInt(content[1]), Integer.parseInt(content[2])));
                    }
                    if (!badLetter) {
                        //add any word that doesn't have a restricted letter
                        nonFitWords.add(new Word(content[0].toLowerCase(), Integer.parseInt(content[1]), Integer.parseInt(content[2])));
                    }
                }
                line = br.readLine();
            }
            br.close();


            //write new list of words to separate input file
            PrintWriter pw = new PrintWriter("possible_words.txt");
            for (Word w : words) {
                pw.print(w.getWord() + " " + w.getNumUnique() + " " + w.getVowelNum() + "\n");
            }
            pw.flush();
            pw.close();

            //write new list of words to separate input file
            PrintWriter pw2 = new PrintWriter("non_fit_words.txt");
            for (Word w : nonFitWords) {
                pw2.print(w.getWord() + " " + w.getNumUnique() + " " + w.getVowelNum() + "\n");
            }
            pw2.flush();
            pw2.close();



            //all data imported
            ArrayList<Word> idealFirstWords = new ArrayList<>();
            //grab list of words with most vowels

            int bestRemoved = 0;

            for (Word w : nonFitWords) {
                //copy words
                ArrayList<Word> copy = new ArrayList<>(words);

                //get number of possible removed words
                ArrayList<Word> removedWords = removeOtherWords(w, copy,confirmedLetters);

                int numRemoved = words.size() - removedWords.size();

                if (numRemoved > bestRemoved) {
                    //if we find a better solution, empty list of first words, then re-add solution
                    bestRemoved = numRemoved;
                    idealFirstWords.clear();
                    idealFirstWords.add(w);
                } else if (numRemoved == bestRemoved) {
                    //matches best
                    idealFirstWords.add(w);
                }
            }


            //print choices
            Word choice;
            try {
                choice = idealFirstWords.get(0);
            } catch (IndexOutOfBoundsException e) {
                continue;
            }

            for (Word w : idealFirstWords) {
                if (w.getNumUnique() > choice.getNumUnique()) {
                    choice = w;
                }
            }

            System.out.println("A good choice would be \"" + choice.getWord() + "\" which could remove " + bestRemoved + "/" + words.size() + " words." );
            System.out.println("Number of Possible Words: " + words.size() + " Words Eliminated: " + (initialWordNum - words.size()));


        }
    }


    public static ArrayList<Word> removeOtherWords(Word word, ArrayList<Word> words,ArrayList<String> confirmed) {

        ArrayList<Word> removedWords = new ArrayList<>(words);
        //loop through letters
        for (int i = 0; i < word.getWord().length(); i++) {
            String letter = word.getWord().substring(i, i + 1);
            if(confirmed.contains(letter)) {
                continue;
            }
            for (int j = 0; j < words.size(); j++) {
                if (words.get(j).getWord().contains(letter)) {
                    removedWords.remove(words.get(j));
                }
            }
        }

        return removedWords;
    }

}


