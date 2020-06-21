package com.example.word_city;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;

public class Crossword
{
    private ArrayList<String> word_list ;
    private int grid_length ;

    protected Grid word_grid;
    protected Grid dir_grid;

    protected Dictionary<String, Point>horizontalWords;
    protected Dictionary<String, Point>verticalWords;
    protected String firstWordDirection ;
    private CrosswordMaker maker ;

    Crossword(ArrayList<String> word_list)
    {
        init(word_list);
        maker = new CrosswordMaker(word_list, word_grid, dir_grid);
        setDirections();
        //displayBothGrid();
    }
    Crossword(ArrayList<String> word_list, String firstWordDirection)
    {
        init(word_list);
        maker = new CrosswordMaker(word_list, word_grid, dir_grid, firstWordDirection);

        setDirections();
    }

    private void init(ArrayList<String> word_list)
    {
        horizontalWords = new Hashtable<>();
        verticalWords = new Hashtable<>();
        this.word_list = word_list;
        sortList();
        grid_length = scaleGrid();
        word_grid = new Grid(grid_length);
        dir_grid = new Grid(grid_length);
    }
    private void setDirections()
    {
        horizontalWords = maker.getHorizontalWords();
        verticalWords = maker.getVerticalWords();
        firstWordDirection = maker.getFirstWordDirection();
    }

    private int scaleGrid()
    {
        int max = largestWord();
        return max * 2 ;
    }

    private void displayBothGrid()
    {
        word_grid.displayGrid();
        System.out.println("---------------------------");
        dir_grid.displayGrid();
    }

    private int largestWord()
    {
        int max = 0 ;
        for (int i = 0; i <word_list.size() ; i++)
        {
            if(word_list.get(i).length() > max)
                max = word_list.get(i).length();
        }
        return max;
    }

    private void sortList()
    {
        Collections.sort(word_list, new Comparator<String>()
        {
            public int compare(String s1,String s2)
            {
                return s2.length() - s1.length();
            }
        });
    }

}