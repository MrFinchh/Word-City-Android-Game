package com.example.word_city;



import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;



public class CrosswordDisplay extends ViewGroup
{

    private TextView [][]  viewList;

    private String word;
    private Point point;
    private String type ;


    private Crossword crossword ;

    private int left;
    private int top ;
    private int right ;
    private int bottom ;
    private int k ;

    CrosswordDisplay(Context context)
    {
        super(context);
        this.setWillNotDraw(false);
    }

    private void initSmallSquare()
    {
        left = 0 ; // 50
        top =  0; // 200
        right = 100 ; // 200
        bottom = 100 ; // 350
        k = 100 ;
    }
    private void initBigSquare()
    {
        left = 50; // 50
        top =  50; // 200
        right = 200 ; // 200
        bottom = 200 ; // 350
        k = 150 ;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        int row = crossword.dir_grid.grid_length;
        int col = crossword.dir_grid.grid[0].length;
        for (int j = 0; j <row ; j++)
        {
            for (int l = 0; l <col ; l++)
            {
                char letter = crossword.word_grid.grid[j][l];
                if(letter != '*')
                {
                    TextView cell = viewList[j][l];
                    int[] positions = getPosition(j,l);
                    cell.layout(positions[0],positions[1],positions[2],positions[3]);
                }
            }
        }
    }

    private int [] getPosition(int row, int col )
    {
        int l = left   + ( (col) * k) ;
        int t = top    + ( (row) * k) ;
        int r = right  + ( (col) * k) ;
        int b = bottom + ( (row) * k) ;
        return new int [] {l,t,r,b};
    }

    private void createCell(Context context)
    {
        int letterCount = 0 ;
        int row = crossword.dir_grid.grid_length;
        int col = crossword.dir_grid.grid[0].length;
        viewList = new TextView[row][col];
        for (int i = 0; i < row ; i++)
        {
            for (int j = 0; j <col; j++)
            {
                TextView cell = getCell(context);
                viewList[i][j] = cell ;
                char letter = crossword.word_grid.grid[i][j];
                if(letter != '*')
                {
                    letterCount++ ;
                    this.addView(cell);
                }
            }
        }
        defineSquareSize(letterCount);
    }

    private TextView getCell(Context context)
    {
        TextView cell = new TextView(context);
        cell.setBackground(getContext().getResources().getDrawable(R.drawable.cell));
        return cell;
    }

    private void defineSquareSize(int squareCount)
    {
        if(squareCount < 15)
        {
            initBigSquare();
        }
        else
        {
            initSmallSquare();
        }
    }

    protected void setCrossword(Crossword crossword, Context context)
    {
        this.crossword = crossword ;
        createCell(context);
    }

    protected void getUnlockedWordDetail(String word, Point point, String type)
    {
        this.word = word;
        this.point = point ;
        this.type = type;
        showUnlockedWord(word,point,type);
    }

    private void showUnlockedWord(String word, Point point, String type)
    {
        int row = point.x ;
        int col = point.y ;
        for (int i = 0; i <word.length(); i++)
        {
            TextView cell = viewList[row][col];
            cell.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font2));
            cell.setTextColor(Color.BLACK);
            cell.setBackground(getContext().getResources().getDrawable(R.drawable.unlockedcell));
            cell.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            String letter = String.valueOf(word.charAt(i));
            cell.setText(letter);
            if(type.equals("HORIZONTAL"))
            {
                col +=1 ;
            }
            else
            {
                row += 1 ;
            }
        }
    }

}
