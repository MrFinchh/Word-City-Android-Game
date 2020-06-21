package com.example.word_city;


import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

public class CrosswordMaker implements Sign
{
    private Dictionary<String, Point> horizontalWords;
    private Dictionary<String, Point>verticalWords;
    private ArrayList<String> wordList ;
    private Grid puzzle ;
    private Grid direction ;
    private String firstWordDirection ;

    CrosswordMaker(ArrayList<String> wordList, Grid puzzle, Grid direction)
    {
        this.horizontalWords = new Hashtable<>();
        this.verticalWords = new Hashtable<>();
        this.wordList = wordList;
        this.puzzle = puzzle ;
        this.direction = direction;
        this.firstWordDirection = randomDirection();
        putFirstWordToGrid(firstWordDirection);
        putWords();
    }

    CrosswordMaker(ArrayList<String> wordList, Grid puzzle, Grid direction, String firstWordDirection)
    {
        this.horizontalWords = new Hashtable<>();
        this.verticalWords = new Hashtable<>();
        this.wordList = wordList;
        this.puzzle = puzzle ;
        this.direction = direction;
        putFirstWordToGrid(firstWordDirection);
        putWords();
    }

    private void putWords()
    {
        for (int i = 0; i <wordList.size()-1; i++)
        {
            String current_word = wordList.get(i+1);
            boolean placed = searchLetters(current_word);
            if(!placed)
            {
                if( !(Collections.frequency(wordList, current_word) > 1) )
                {
                    wordList.add(current_word);
                }
                else
                {
                    putWordSomewhere(current_word);
                }
            }
        }
    }
    private boolean searchLetters(String word)
    {
        for (int row = 0; row <puzzle.grid_length ; row++)
        {
            //if(word.contains(String.valueOf(puzzle.grid[i]));
            for (int col = 0; col <puzzle.grid_length ; col++)
            {
                char letter = puzzle.grid[row][col];
                if(word.contains(String.valueOf(puzzle.grid[row][col])))
                {
                    if(putWordHorizontal(word,row,col,letter))
                        return true;
                    else if(putWordVertical(word,row,col,letter))
                        return true;
                }
            }
        }
        return false;
    }


    private void putWordSomewhere(String word)
    {
        int before_start = puzzle.getStartPosition();
        if(before_start == -1)
        {
            wordList.remove(word);
            Log.i("remeoved", "putWordSomewhere: "+word);
            System.out.println("Word removed :"+word);
            return;
        }
        String type = puzzle.getWordPlaceType(before_start);
        int start = before_start + 1 ;

        if(type.equals(HORIZONTAL))
        {
            putAsHorizontal(word,start,0,0);
            this.direction.grid[start][0] = HORIZONTAL_PLACED;
            this.puzzle.grid[start][0] = word.charAt(0);
        }
        else
        {
            putAsVertical(word,0,start,0);
            this.direction.grid[0][start] = VERTICAL_PLACED;
            this.puzzle.grid[0][start] = word.charAt(0);
        }
    }

    private boolean putWordHorizontal(String word, int letter_row, int letter_col, char letter)
    {
        int same_word_index = getSameLetterIndex(word,letter);
        int word_length = word.length();
        if(isPlaceable(letter_col, same_word_index, word_length))
        {
            boolean ans = putAsHorizontal(word, letter_row, letter_col, same_word_index);
            return ans;
        }
        return false;
    }

    private boolean putWordVertical(String word, int letter_row, int letter_col, char letter)
    {
        int same_word_index = getSameLetterIndex(word,letter);
        int word_length = word.length();
        if(isPlaceable(letter_row, same_word_index, word_length))
        {
            boolean ans = putAsVertical(word, letter_row, letter_col, same_word_index);
            return ans ;
        }
        return false;
    }

    private int getSameLetterIndex(String word, char letter)
    {
        return word.indexOf(letter);
    }

    private void putFirstWordToGrid(String direction)
    {
        String longest_word = wordList.get(0);
        if(direction.equals("HORIZONTAL"))
        {
            putAsHorizontal(longest_word,0,0,0);
            this.direction.grid[0][0] = HORIZONTAL_PLACED;
            this.horizontalWords.put(longest_word, new Point(0,0));
        }
        else
        {
            putAsVertical(longest_word,0,0,0);
            this.direction.grid[0][0] = VERTICAL_PLACED;
            this.verticalWords.put(longest_word, new Point(0,0));
        }
        puzzle.grid[0][0] = longest_word.charAt(0);
    }



    private void markDead(String word, int row, int col, Grid direction, String type)
    {
        Marker marker = new Marker(word, row, col, direction);
        marker.markDead(word,row,col,type);
    }
    private void markDirectionGrid(String word, int row, int col, Grid direction, String type)
    {
        Marker marker = new Marker(word, row, col, direction);
        marker.mark(type);
    }



    private boolean putAsHorizontal(String word, int row, int col, int same_index)
    {
        int start_col = col - same_index ;
        int start_col_copy = start_col ;
        if(checkHorizontalAvailable(word, row, start_col))
        {
            this.horizontalWords.put(word, new Point(row,start_col));
            for (int i = 0; i <word.length() ; i++)
            {
                if(i != same_index)
                {
                    puzzle.grid[row][start_col] = word.charAt(i);
                    direction.grid[row][start_col] = HORIZONTAL_PLACED;
                }
                start_col++ ;
            }
            markDirectionGrid(word, row, start_col_copy, direction, HORIZONTAL);
            markDead(word,row,start_col_copy,direction,HORIZONTAL);
            return true;
        }
        return false;
    }

    private boolean checkHorizontalAvailable(String word, int row, int col)
    {
        if(deadHorizontalCheck(word,row,col))
        {
            for (int i = 0; i <word.length() ; i++)
            {
                char sign = direction.grid[row][col];
                char letter = puzzle.grid[row][col];
                char current_letter = word.charAt(i);

                if(sign == DEAD || sign == HORIZONTAL_PLACED  || sign == VERTICAL_AVAILABLE || (letter != current_letter && letter != '*'))
                {
                    return false;
                }
                col++;
            }
        }
        else
        {
            return false;
        }

        return true ;
    }

    private boolean deadHorizontalCheck(String word, int row, int col)
    {
        int right = col + word.length() ;
        char right_sign;
        if(right > direction.grid_length)
        {
            return false;
        }
        else if(right == direction.grid_length)
        {
            right_sign = '*';
        }
        else
        {
            right_sign = direction.grid[row][right];
        }


        if(col != 0)
        {
            int left = col - 1 ;
            char left_sign = direction.grid[row][left];
            if(right_sign == HORIZONTAL_PLACED || right_sign == VERTICAL_PLACED ||
                    left_sign == HORIZONTAL_PLACED || left_sign == VERTICAL_PLACED)
            {
                return false;
            }
        }
        else
        {
            if(right_sign == HORIZONTAL_PLACED || right_sign == VERTICAL_PLACED)
            {
                return false;
            }
        }
        return true;
    }



    private boolean putAsVertical(String word, int row, int col, int same_index)
    {
        int start_row = row - same_index ;
        int start_row_copy = start_row ;
        if(checkVerticalAvailable(word,start_row,col))
        {
            this.verticalWords.put(word, new Point(start_row,col));
            for (int i = 0; i <word.length() ; i++)
            {
                if(i != same_index)
                {
                    puzzle.grid[start_row][col] = word.charAt(i);
                    direction.grid[start_row][col] = VERTICAL_PLACED;
                }
                start_row++ ;
            }
            markDirectionGrid(word, start_row_copy, col, direction, VERTICAL);
            markDead(word,start_row_copy,col,direction,VERTICAL);
            return true;
        }
        return false;
    }
    private boolean checkVerticalAvailable(String word, int row, int col)
    {
        if(deadVerticalCheck(word,row,col))
        {
            for (int i = 0; i <word.length() ; i++)
            {
                char sign = direction.grid[row][col];
                char letter = puzzle.grid[row][col];
                char current_letter = word.charAt(i);
                if(sign == DEAD || sign == VERTICAL_PLACED || sign == HORIZONTAL_AVAILABLE || (letter != current_letter && letter != '*'))
                {
                    return  false;
                }
                row++;
            }
        }
        else
        {
            return false;
        }

        return true;
    }
    private boolean deadVerticalCheck(String word, int row, int col)
    {
        int down = row + word.length();
        char down_sign ;
        if(down > direction.grid_length)
        {
            return false;
        }
        else if(down == direction.grid_length)
        {
            down_sign = '*';
        }
        else
        {
            down_sign = direction.grid[down][col];
        }

        if(row != 0)
        {
            int up = row - 1 ;
            char up_sign = direction.grid[up][col];
            if(down_sign == HORIZONTAL_PLACED || down_sign == VERTICAL_PLACED ||
                    up_sign == HORIZONTAL_PLACED || up_sign == VERTICAL_PLACED)
            {
                return false;
            }
        }
        else
        {
            if(down_sign == HORIZONTAL_PLACED || down_sign == VERTICAL_PLACED)
            {
                System.out.println("Vertical dead end: "+down_sign);
                return false;
            }
        }
        return true;
    }



    private void put(String word, int row, int col, String type)
    {
        char sign ;
        if(type.equals(HORIZONTAL))
        {
            sign = HORIZONTAL_PLACED;
        }
        else
        {
            sign = VERTICAL_PLACED;
        }
        for (int i = 0; i <word.length() ; i++)
        {
            puzzle.grid[row][col] = word.charAt(i);
            direction.grid[row][col] = sign;

            if(sign == HORIZONTAL_PLACED)
            {
                row++;
            }
            else
            {
                col++;
            }
        }
    }


    private String randomDirection()
    {
        Random random = new Random();
        int number = random.nextInt(2);
        if(number == 0)
            return "HORIZONTAL";
        else
            return "VERTICAL";
    }

    private boolean isPlaceable(int row_or_col, int same_letter_index, int length)
    {
        return row_or_col - same_letter_index >= 0 && row_or_col + length - 1 <= puzzle.grid_length;
    }

    protected Dictionary<String, Point> getHorizontalWords()
    {
        return horizontalWords;
    }
    protected Dictionary<String, Point> getVerticalWords()
    {
        return verticalWords;
    }
    protected String getFirstWordDirection(){return  firstWordDirection;}
}
