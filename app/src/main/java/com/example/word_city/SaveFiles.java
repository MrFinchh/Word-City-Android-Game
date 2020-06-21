package com.example.word_city;

public interface SaveFiles
{
    String GAME_KEY = "gameKey.txt";
    String FIRST_WORD_DIRECTION = "firstWordDir.txt";
    String UNLOCKED_WORDS = "unlockedWords.txt";
    String SCORE = "score.txt";

    String APPEND_MODE = "APPEND";
    String WRITE_MODE = "WRITE";


    String[] FILES = {GAME_KEY,FIRST_WORD_DIRECTION,UNLOCKED_WORDS,SCORE};
}
