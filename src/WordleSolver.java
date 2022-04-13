// import statements here

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * IdealWordSequence
 * <p>
 * brief description of the program
 * <p>
 * a list of your sources of help (if any)
 *
 * @author Justin L
 * @version 02/03/2022
 */

public class WordleSolver {

    private ArrayList<Word> dict;
    private ArrayList<Word> words;
    private ArrayList<Word> nonFitWords;
    private ArrayList<String> confirmedLetters;
    private int bestRemoved;
    private int initialWordNum;
    private int wordSize;

    public WordleSolver(String dictionary) throws IOException{
        dict = new ArrayList<>(5756);

        //intialize dictionary file
        BufferedReader br = new BufferedReader(new FileReader(dictionary));
        String line = br.readLine();
        while(line != null) {
            if (line.length() > 0) {
                String[] content = line.split(" ");
                dict.add(new Word(content[0].toLowerCase(), Integer.parseInt(content[1]), Integer.parseInt(content[2])));
            }
            line = br.readLine();
        }

        //initialize words list
        words = new ArrayList<>();
        nonFitWords = new ArrayList<>();
        confirmedLetters = new ArrayList<>(5);


    }

    public ArrayList<Word> run() throws IOException {

            //prompt user for known letters / positions
            String[] letters = getCommandLineInput().split(",");
            if (letters.length < 5) {
                return null;
            }
            else if(letters.length > 5) {
                //reset words
                words = new ArrayList<>(dict);
                nonFitWords = new ArrayList<>();
                confirmedLetters.clear();

            }

            //loop through file
            initializePossibleWordsGuesses(letters);

            //set wordSize
            wordSize = words.size();

            //all data imported
            ArrayList<Word> idealGuesses = getIdealGuess();

            return idealGuesses;
    }

    public ArrayList<Word> run(String result) throws IOException {
        confirmedLetters = new ArrayList<>();

        //prompt user for known letters / positions
        String[] letters = result.split(",");
        if (letters.length < 5) {
            return null;
        }

        else if(letters.length > 5) {
            //reset words
            words = new ArrayList<>(dict);
            nonFitWords = new ArrayList<>();
            confirmedLetters.clear();

        }

        //loop through file
        initializePossibleWordsGuesses(letters);

        //set wordSize
        wordSize = words.size();

        //all data imported
        ArrayList<Word> idealGuesses = getIdealGuess();

        return idealGuesses;
    }

    public ArrayList<Word> removeOtherWords(Word word, ArrayList<Word> words, ArrayList<String> confirmed) {

        ArrayList<Word> removedWords = new ArrayList<>(words);
        //loop through letters
        for (int i = 0; i < word.getWord().length(); i++) {
            String letter = word.getWord().substring(i, i + 1);
            if (confirmed.contains(letter)) {
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

    public String getCommandLineInput() {
        //prompt user for known letters / positions
        System.out.println("Input known letters / position (_,_,_,_,_):");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        input = input.trim();
        return (input.toLowerCase());

    }

    public void initializePossibleWordsGuesses(String[] letters) throws IOException{
        //loop through file
        initialWordNum = words.size();
        //copy words and reset
        ArrayList<Word> temp = new ArrayList<>(words);
        words.clear();
        for(Word w : temp) {
                //check if word fits input parameters
                boolean fitsInput = true;
                boolean badLetter = false;
                String currentWord = w.getWord();
                for (int i = 0; i < 5; i++) {

                    //check if we already guessed a letter in that spot
                    if (fitsInput) {
                        if (letters[i].charAt(0) == '!') { //letter in word
                            String letter = letters[i].substring(1, 2);
                            if (!confirmedLetters.contains(letter)) {
                                confirmedLetters.add(letter);
                            }
                            //check if word contains letter not in this spot.
                            if (!(currentWord.contains(letter) && !currentWord.substring(i, i + 1).equals(letter))) {
                                fitsInput = false;
                            }
                        } else if (letters[i].charAt(0) == '^') { //confirmed letter
                            String letter = letters[i].substring(1, 2);
                            if (!confirmedLetters.contains(letter)) {
                                confirmedLetters.add(letter);
                            }
                            //check if word matches, if not remove
                            if (!(letter.equals(currentWord.substring(i, i + 1)))) {
                                fitsInput = false;
                            }
                        } else { //letter not found in final word
                            //check if word contains this letter
                            if (currentWord.contains(letters[i]) && !confirmedLetters.contains(letters[i])) {
                                fitsInput = false;
                                badLetter = true;
                            }
                        }
                    }
                }

            if (fitsInput) {
                words.add(w);
            }
            if (!badLetter) {
                //add any word that doesn't have a restricted letter
                nonFitWords.add(w);
            }
        }

        /*/write new list of words to separate input file
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
        */
    }

    public ArrayList<Word> getIdealGuess() {
        ArrayList<Word> idealFirstWords = new ArrayList<>();
        //grab list of words with most vowels

        bestRemoved = 0;
        if(words.size() <= 2 || confirmedLetters.size() >=4) {
            for (Word w : words) {
                //copy words
                ArrayList<Word> copy = new ArrayList<>(words);

                //get number of possible removed words
                ArrayList<Word> removedWords = removeOtherWords(w, copy, confirmedLetters);

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
        } else {
            for (Word w : nonFitWords) {
                //copy words
                ArrayList<Word> copy = new ArrayList<>(words);

                //get number of possible removed words
                ArrayList<Word> removedWords = removeOtherWords(w, copy, confirmedLetters);

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
        }
        return idealFirstWords;
    }

    //get set methods

    public int getBestRemoved() {
        return bestRemoved;
    }

    public int getInitialWordNum() {
        return initialWordNum;
    }

    public int getWordSize() {
        return wordSize;
    }
}


