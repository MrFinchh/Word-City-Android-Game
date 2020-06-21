package com.example.word_city;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grid
{
    protected int grid_length;

    protected char [][] grid;

    Grid(int grid_length)
    {
        this.grid_length = grid_length ;
        grid = new char[grid_length][grid_length];
        fillGrid(grid);
    }

    private void fillGrid(char [][] grid)
    {
        for (int i = 0; i <grid_length ; i++) {
            for (int j = 0; j <grid_length ; j++) {
                grid[i][j] = '*';
            }
        }
    }


    protected void displayGrid()
    {
        for (char [] row :grid)
        {
            for (char cell: row)
            {
                System.out.print(cell+" ");
            }
            System.out.println();
        }
    }

    private int findEmptyRow()
    {
        Pattern pattern = Pattern.compile("[^*]");
        Matcher matcher ;
        for (int i = 0; i < grid.length ; i++)
        {
            String row = String.valueOf(grid[i]);
            row = row.toLowerCase();
            matcher = pattern.matcher(row);

            if(!matcher.find())
            {
                System.out.println("OK ROW :"+row);
                if(i+1 < grid_length)
                {
                    String next = String.valueOf(grid[i+1]);
                    next = next.toLowerCase();
                    matcher = pattern.matcher(next);
                    if(!matcher.find())
                    {
                        return  i ;
                    }
                }
            }
        }
        return -1;
    }

    private boolean checkColumn(int currentColumn)
    {
        for (int i = 0; i <grid.length ; i++)
        {
            char letter = (grid[i][currentColumn]);

            if(letter != '*')
            {
                return false;
            }
        }
        return true;
    }


    private int findEmptyCol()
    {
        for (int col = 0; col <grid.length ; col++)
        {
            if(checkColumn(col))
            {
                if(col+1 < grid_length && checkColumn(col+1))
                {
                    return col;
                }
            }
        }
        return -1;
    }


    private int compare()
    {
        int row = findEmptyRow();
        int col = findEmptyCol();

        if(row == -1)
        {
            return col;
        }
        else if (col == -1)
        {
            return row ;
        }
        else
        {
            if(row > col)
            {
                return col;
            }
            else
            {
                return row;
            }
        }

    }


    protected int getStartPosition()
    {
        return compare();
    }


    protected String getWordPlaceType(int number)
    {

        if(number == findEmptyCol())
        {
            return "VERTICAL";
        }
        else
        {
            return "HORIZONTAL";
        }
    }



}
