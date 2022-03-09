// import statements here

import java.util.ArrayList;

/**
 * Word
 * <p>
 * brief description of the program
 * <p>
 * a list of your sources of help (if any)
 *
 * @author Justin L, LC1
 * @version 02/03/2022
 */

public class Word {
    private String[] vowels = {"a","e","i","o","u"};
    private final String word;
    private final int vowelNum;
    private final int consonantNum;
    private final int numUnique;

    public Word(String word, int numUnique, int vowelNum) {
        this.word = word;
        this.numUnique = numUnique;
        this.vowelNum = vowelNum;
        this.consonantNum = 5 - vowelNum;
    }

    public String getWord() {
        return word;
    }

    public int getNumUnique() {
        return numUnique;
    }

    public int getVowelNum() {
        return vowelNum;
    }

    public int getConsonantNum() {
        return consonantNum;
    }
}
