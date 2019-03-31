/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    /*
        - wordList: create a list of all words from the dictionary.

        - wordSet: create a list of unique words to identify if the
          word is valid

        - lettersToWord: key is the sorted letters and value is the
          list of anagram strings of the given word
     */
    private static ArrayList<String> wordList = new ArrayList<>();
    private static HashSet<String> wordSet = new HashSet<>();
    private static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> sizeToWords  = new HashMap<>();
    private int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordLength = DEFAULT_WORD_LENGTH;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            // if no key found by length then create one and add
            if(sizeToWords.get(word.length()) == null){
                sizeToWords.put(word.length(), new ArrayList<String>());
            }
            sizeToWords.get(word.length()).add(word);

            // If no key found create a value for it in the hashMap
            if(lettersToWord.get(sortLetters(word)) == null){
                lettersToWord.put(sortLetters(word), new ArrayList<String>());
            }
            lettersToWord.get(sortLetters(word)).add(word);
        }
    }

    /*
        Finds whether given word is in the dictionary and is
        not formed by adding a letter to the start or the end
        of the base word
    */
    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word);
    }

    /*
        helper function to sort word letters alphabetically.
    */
    public String sortLetters(String word){
        char w[] = word.toCharArray();
        Arrays.sort(w);

        return new String(w);
    }

    /*
        Create a list of all possible anagrams for the word
        by comparing @targetWord and wordlist strings.
     */
    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(String val : wordList){
            if(sortLetters(val).equals(sortLetters(targetWord))){
                result.add(val);
            }
        }
        return result;
    }

    /*
        List of all possible anagrams that can be created
        by adding a letter to the given word
    */
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = (int)'a'; i <= (int)'z'; i++){
            String str = word + (char) i;

            if(lettersToWord.get((sortLetters(str))) != null){
                result.addAll(lettersToWord.get(sortLetters(str)));
            }
        }
        return result;
    }

    /*
        Pick a word with the desired number of anagrams
    */
    public String pickGoodStarterWord() {
        ArrayList<String> arr = sizeToWords.get(wordLength);
        String word =  arr.get(random.nextInt(arr.size()));

        while(getAnagramsWithOneMoreLetter(word).size() < MIN_NUM_ANAGRAMS){
            word =  arr.get(random.nextInt(arr.size()));
        }
        return word;
    }
}
