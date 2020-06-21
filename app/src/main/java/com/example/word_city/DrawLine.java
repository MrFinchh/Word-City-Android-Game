package com.example.word_city;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DrawLine extends ViewGroup implements View.OnTouchListener
{
    Paint paint;
    private Path latestPath;
    private ArrayList<Path> pathList = new ArrayList<>();
    private ArrayList<Button> buttonList = new ArrayList<>();
    private int button_count ;
    private ArrayList<String> word_list ;
    private ArrayList<Integer> active_buttons_indexes = new ArrayList<>();
    String word = "";
    private WordGroupListener wordGroupListener;

    public void setWordGroupListener(WordGroupListener wordGroupListener)
    {
        this.wordGroupListener = wordGroupListener ;
    }


    public DrawLine(Context context)
    {
        super(context);
        this.setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(30);
        initPath();
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom)
    {
        int y = (bottom - top) / 2;
        int x  = (right- left) / 2;
        int distance = 250;
        int angle = findAngle(this.button_count);
        double radian = Math.toRadians(angle);
        for (int i = 0; i <buttonList.size() ; i++)
        {
            Button button = buttonList.get(i);
            Point point = findNewLocation(x,y,distance,radian,i+1);
            button.layout(point.x-70, point.y-70, point.x+70,point.y+70);
        }
    }

    private void initPath()
    {
        latestPath = getNewPath();
        pathList.add(latestPath);
    }

    private Path getNewPath()
    {
        return new Path();
    }

    private void startPath(float x, float y)
    {
        latestPath.moveTo(x,y);
    }

    private void updatePath(float x, float y)
    {
        latestPath.lineTo(x,y);
    }

    private void clearPaths()
    {
        latestPath.reset();
        pathList.clear();
        initPath();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        for (int i = 0; i < pathList.size() ; i++)
        {
            canvas.drawPath(pathList.get(i), paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        int mx = (int) event.getX();
        int my = (int) event.getY();

        for (int i = 0; i <buttonList.size() ; i++)
        {
            if(view.equals(buttonList.get(i)))
            {
                mx += buttonList.get(i).getX();
                my += buttonList.get(i).getY();
                break;
            }
        }

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startPath(mx,my);
                break;

            case MotionEvent.ACTION_MOVE:
                updatePath(mx,my);
                int x = isButtonActivated(mx , my);
                if(x > -1)
                {
                    word += buttonList.get(x).getText();
                    wordGroupListener.onWordType(word);
                }
                break;

            case MotionEvent.ACTION_UP:
                clearPaths();
                wordGroupListener.onWordReady(word);
                word = "";
                wordGroupListener.onWordType(word);
                active_buttons_indexes.clear();
                break;
        }
        invalidate();
        return true;
    }

    protected void init_wordButton(Context context,int length, ArrayList<String> word_list)
    {
        this.word_list = word_list ;
        this.button_count = length ;
        for (int i = 0; i <length ; i++)
        {
            this.createButton(context, word_list.get(i));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createButton(Context context, String word)
    {
        Button button = new Button(context);
        button.setBackground(getContext().getResources().getDrawable(R.drawable.shape));
        button.setGravity(Gravity.CENTER);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.result_font));
        button.setText(word);
        //DrawLine.LayoutParams params = new DrawLine.LayoutParams(20,20);
        //setLayoutParams(params);
        addView(button);
        button.setOnTouchListener(this);
        buttonList.add(button);
    }

    private int isButtonActivated(int mx, int my)
    {
        for (int i = 0; i <buttonList.size() ; i++)
        {
            int x1 = (int)(buttonList.get(i).getX());
            int x2 = x1 + buttonList.get(i).getWidth() ;

            int y1 = (int)(buttonList.get(i).getY());
            int y2 = y1 + buttonList.get(i).getHeight() ;

            if(mx >= x1 && mx <= x2 && my >= y1 && my <= y2)
            {
                if(! active_buttons_indexes.contains(i))
                {
                    active_buttons_indexes.add(i);
                    return i;
                }
            }
        }
        return -1 ;
    }

    private int findAngle(int button_count)
    {
        return 360 / button_count;
    }

    private Point findNewLocation(int x, int y, int distance, double radian, int i)
    {
        int new_x = (int)( x + distance * Math.cos(radian*i));
        int new_y = (int)( y + distance * Math.sin(radian*i));
        return new Point(new_x, new_y);
    }

    protected void shuffleActivated()
    {
        Collections.shuffle(word_list);

        for (int i = 0; i <buttonList.size() ; i++)
        {
            buttonList.get(i).setText(word_list.get(i));
        }
        invalidate();
    }

}
