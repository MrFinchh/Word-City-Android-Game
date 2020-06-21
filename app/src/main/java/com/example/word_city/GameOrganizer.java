package com.example.word_city;


import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameOrganizer implements  CrosswordWords
{
    private  String gameKey ;
    private  ArrayList<String> wordList ;
    private  Dictionary<String, Integer> occurrences;
    private  ArrayList<String> letterList ;
    private int letterCount ;

    GameOrganizer(String gameKey)
    {
        occurrences = new Hashtable<>();
        this.gameKey = gameKey ;
        letterList = new ArrayList<>();
        wordList = new ArrayList<>();
        createWordList();
        extractLetters();
        createLetterList();
        countLetterCount();
    }

    private int [] getValueIndex()
    {

        for (int keys = 0; keys <GAME_KEYS.length ; keys++)
        {
            for (int index = 0; index < GAME_KEYS[keys].length; index++)
            {
                if(gameKey.equals(GAME_KEYS[keys][index]))
                {
                    return new int[]{keys,index};
                }
            }
        }
        return null;
    }

    private String getWords()
    {
        int [] gameValues = getValueIndex();
        try
        {
            int key = gameValues[0];
            int index = gameValues[1];
            return GAME_VALUES[key][index];
        }
        catch (NullPointerException ex)
        {
            Log.i("", "There is no game.");
        }
        return null;
    }

    private void createWordList()
    {
        String word = getWords();
        word = word.toUpperCase();
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(word);
        while(matcher.find())
        {
            wordList.add(matcher.group());
        }
    }

    protected Crossword getGame()
    {
        return new Crossword(wordList);
    }
    protected Crossword getGame(String firsWordDirection)
    {
        return new Crossword(wordList, firsWordDirection);
    }

    private void extractLetters()
    {
        for (int i = 0; i <wordList.size() ; i++)
        {
            String word = wordList.get(i);
            for (int j = 0; j <word.length() ; j++)
            {
                String letter = String.valueOf(word.charAt(j));
                int count = word.length() - word.replace(letter, "").length();
                addToOccurrences(letter,count);
            }
        }
    }

    private void addToOccurrences(String letter, int count)
    {
        try
        {
            int number = occurrences.get(letter);

            if(count > number )
            {
                occurrences.put(letter,count);
            }
        }
        catch (NullPointerException ex)
        {
            occurrences.put(letter,count);
        }
    }

    private void createLetterList()
    {
        for (Enumeration i = occurrences.keys(); i.hasMoreElements();)
        {
            String key = i.nextElement().toString();
            int value = occurrences.get(key);
            for (int j = 0; j <value; j++)
            {
                letterList.add(key);
            }
        }
    }

    private void countLetterCount()
    {
        for (Enumeration i = occurrences.elements(); i.hasMoreElements();)
        {
            this.letterCount += (int)i.nextElement();
        }
    }

    protected ArrayList<String> getLetterList()
    {
        return this.letterList;
    }
    protected int getLetterCount()
    {
        return this.letterCount;
    }
    protected ArrayList<String> getWordList()
    {
        removeDuplicatesFromWordList();
        return this.wordList;
    }
    private void displayWordList()
    {
        for (int i = 0; i <wordList.size() ; i++)
        {
            Log.i("WordList", "Element :"+wordList.get(i));
        }
    }
    private void displayLetterList()
    {
        for (int i = 0; i <letterList.size() ; i++)
        {
            Log.i("LetterList", "Element :"+letterList.get(i));
        }
        Log.i("Count", "LetterCount: :"+letterCount);
    }

    private void removeDuplicatesFromWordList()
    {
        Set<String> set = new HashSet<>(wordList);
        wordList.clear();
        wordList.addAll(set);
    }

}
