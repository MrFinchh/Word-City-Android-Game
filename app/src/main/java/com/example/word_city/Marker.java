package com.example.word_city;

public class Marker implements Sign
{
    private int row;
    private int col;
    private Grid direction;
    private String word;

    Marker(String word, int row, int col, Grid direction)
    {
        this.word = word;
        this.row  = row;
        this.col  = col;
        this.direction = direction;
    }

    protected void mark(String recently_placed_word_direction)
    {
        if(recently_placed_word_direction.equals(HORIZONTAL))
        {
            markVerticalAvailable(word,row,col);
        }
        else
        {
            markHorizontalAvailable(word,row,col);
        }
    }

    /**********************************************************************************/

    // Use for when word placed as Vertical
    private void markHorizontalAvailable(String word, int row, int col)
    {
        if(col != 0)
        {
            for (int i = 0; i <word.length(); i++)
            {
                leftHorizontal(row,col);
                rightHorizontal(row,col);
                row++;
            }
        }
        else
        {
            for (int i = 0; i <word.length() ; i++)
            {
                rightHorizontal(row,col);
                row++;
            }
        }
    }

    private void leftHorizontal(int row, int col)
    {
        col -=1;
        char sign = direction.grid[row][col];
        horizontalSignRule(sign, row, col);
    }

    private void rightHorizontal(int row, int col)
    {
        col +=1;
        char sign = direction.grid[row][col];
        horizontalSignRule(sign, row, col);
    }

    private void horizontalSignRule(char sign, int row, int col)
    {
        if(sign == VERTICAL_AVAILABLE)
        {
            direction.grid[row][col] = DEAD;
        }
        else if(sign == '*')
        {
            direction.grid[row][col] = HORIZONTAL_AVAILABLE;
        }
    }

    /**********************************************************************************/

    // Use for when word placed as Horizontal
    private void markVerticalAvailable(String word, int row, int col)
    {
        if(row != 0)
        {
            for (int i = 0; i <word.length() ; i++)
            {
                downVertical(row,col);
                upVertical(row,col);
                col++;
            }
        }
        else
        {
            for (int i = 0; i <word.length() ; i++)
            {
                downVertical(row,col);
                col++;
            }
        }

    }

    private void downVertical(int row, int col)
    {
        row += 1;
        char sign = direction.grid[row][col];
        verticalSignRule(sign, row, col);
    }

    private void upVertical(int row, int col)
    {
        row -= 1;
        char sign = direction.grid[row][col];
        verticalSignRule(sign, row, col);
    }

    private void verticalSignRule(char sign, int row, int col)
    {
        if(sign == HORIZONTAL_AVAILABLE)
        {
            direction.grid[row][col] = DEAD;
        }
        else if(sign == '*')
        {
            direction.grid[row][col] = VERTICAL_AVAILABLE;
        }
    }

    /**********************************************************************************/
    //Dead Marking

    protected void markDead(String word, int row, int col, String recently_placed_word_direction)
    {
        if(recently_placed_word_direction.equals(HORIZONTAL))
        {
            markHorizontalDead(word, row, col);
        }
        else
        {
            markVerticalDead(word, row, col);
        }
    }


    private void markHorizontalDead(String word, int row, int col)
    {
        int right = col + word.length();
        if(right < direction.grid_length)
        {
            char right_sign = direction.grid[row][right];
            deadSignRule(right_sign,row,right);
        }

        if(col != 0)
        {
            int left = col - 1 ;
            char left_sign = direction.grid[row][left];
            deadSignRule(left_sign,row,left);
        }

    }

    private void markVerticalDead(String word, int row, int col)
    {
        int down = row + word.length();
        if(down < direction.grid_length)
        {
            char down_sign = direction.grid[down][col];
            deadSignRule(down_sign, down, col);
        }


        if(row != 0)
        {
            int up = row -1 ;
            char up_sign = direction.grid[up][col];
            deadSignRule(up_sign,up,col);
        }
    }

    private void deadSignRule(char sign, int row, int col)
    {
        if(!(sign == HORIZONTAL_PLACED || sign == VERTICAL_PLACED))
        {
            direction.grid[row][col] = DEAD;
        }
    }
}
