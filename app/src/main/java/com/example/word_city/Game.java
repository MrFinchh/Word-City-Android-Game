package com.example.word_city;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends AppCompatActivity implements View.OnClickListener
{
    SaveSystem saveSystem = new SaveSystem(this);;
    String currentLevel, currentGame ;
    String gameKey ;
    private LinearLayout gameLayout;
    private int letterCount;
    private ArrayList<String> letterList;
    private ArrayList<String> wordList;
    private ArrayList<String> unlockedWordList ;
    private Button shuffle ;
    private DrawLine drawLine ;
    private CrosswordDisplay crosswordDisplay ;
    private GameOrganizer gameOrganizer;
    private Crossword game ;
    private String resultStr = "";
    private Button result;
    private int negativeAttempts = 0 ;
    private int positiveAttempts = 0 ;
    private Database db;
    private long startTime = 0;
    private int lastGameSecond = 0;
    private int second ;
    private Timer timer  = new Timer();

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if(timer != null)
            {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                seconds = seconds % 60;
                second = seconds;
                saveScores();
            }
        }
    };

    public void stopTimer()
    {
        if (timer != null)
        {
            timerHandler.removeCallbacks(timerRunnable);
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void startTimer()
    {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerHandler.post(timerRunnable);
            }
        }, 0, 1000);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(positiveAttempts != wordList.size())
            startTimer();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        stopTimer();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        stopTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        db = new Database(this);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        setContentView(R.layout.activity_game);

        currentLevel = getIntent().getStringExtra("level");
        currentGame  = getIntent().getStringExtra("game");
        gameKey = currentLevel+currentGame ;

        init();
        addPuzzle();
        letterList = gameOrganizer.getLetterList();
        letterCount = gameOrganizer.getLetterCount();
        wordList = gameOrganizer.getWordList();
        addShuffleAndResult();
        addWords();
        getUnlockedWord();
        startTimer();
    }

    private void getUnlockedWord()
    {
        drawLine.setWordGroupListener(new WordGroupListener()
        {
            @Override
            public void onWordReady(String word)
            {

                if(wordList.contains(word) && addToUnlockedWordList(word))
                {
                    Point point  = getPoint(word);
                    String type  = getType(word);

                    saveSystem.saveUnlockedWords(word);
                    crosswordDisplay.getUnlockedWordDetail(word, point, type);
                    positiveAttempts++;
                    isGameOver();
                }
                else
                {
                    System.out.println(word);
                    negativeAttempts++ ;
                }
            }
            @Override
            public void onWordType(String word)
            {
                resultStr = word ;
                resultButtonVisibility();
            }
        });
    }

    private void init()
    {
        gameLayout = findViewById(R.id.main);
        gameLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private void addPuzzle()
    {
        CrosswordDisplay.LayoutParams params4 = new CrosswordDisplay.LayoutParams(1100,1100);
        crosswordDisplay = new CrosswordDisplay(this);
        crosswordDisplay.setLayoutParams(params4);
        crosswordDisplay.setX(0);
        crosswordDisplay.setY(0);
        gameOrganizer = new GameOrganizer(gameKey);
        startGame();

        gameLayout.addView(crosswordDisplay);
    }

    private void addWords()
    {
        DrawLine.LayoutParams params3 = new DrawLine.LayoutParams(700,700);
        drawLine = new DrawLine(this);
        drawLine.init_wordButton(this,letterCount,letterList);
        drawLine.setLayoutParams(params3);
        drawLine.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_view));
        drawLine.setX(320);
        drawLine.setY(0);
        gameLayout.addView(drawLine);
    }

    private void addShuffleAndResult()
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150,150);
        shuffle  = new Button(this);
        shuffle.setOnClickListener(this);
        shuffle.setLayoutParams(params);
        shuffle.setBackgroundResource(R.drawable.shape);
        shuffle.setX(25);
        result = new Button(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(600,150);
        result.setLayoutParams(params1);
        result.setBackgroundResource(R.drawable.result_button);
        result.setX(400);
        resultButtonVisibility();
        gameLayout.addView(result);
        gameLayout.addView(shuffle);
    }

    private void resultButtonVisibility()
    {
        if(resultStr.isEmpty())
        {
            result.setVisibility(View.INVISIBLE);
        }
        else
        {
            result.setVisibility(View.VISIBLE);
            result.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.result_font));
            result.setText(resultStr);
        }
    }

    private boolean addToUnlockedWordList(String word)
    {
        if(!(unlockedWordList.contains(word)))
        {
            unlockedWordList.add(word);
            return true;
        }
        return false;
    }

    private void isGameOver()
    {
        if(positiveAttempts == wordList.size())
        {
            second = second + lastGameSecond;
            final double score = calculateScore(negativeAttempts,second);
            db.saveData(gameKey,score);
            stopTimer();
            saveSystem.deleteSaveFiles();
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Intent gameOver = new Intent(Game.this, GameOver.class);
                gameOver.putExtra("NegativeAttempts",negativeAttempts);
                gameOver.putExtra("Second",second);
                gameOver.putExtra("Score",score);
                gameOver.putExtra("gameKey",gameKey);
                startActivity(gameOver);
            }
        }, 2000);
        }
    }

    private double calculateScore(int negativeAttempts, int time)
    {
        double negative = (double)(negativeAttempts) * 0.5 ;
        double waste = (double)(time) * 0.1 ;
        return 100 - (negative + waste) ;
    }

    private void startGame()
    {
        if(saveSystem.isAnyGameSaved(gameKey))
        {
            String firstWordDirection = saveSystem.getFirstWordDirection();
            firstWordDirection = firstWordDirection.trim();
            unlockedWordList = saveSystem.getUnlockedWords();
            game = gameOrganizer.getGame(firstWordDirection);
            ArrayList<String> scores = saveSystem.getScoreEssentials();
            crosswordDisplay.setCrossword(game,this);
            for (int i = 0; i <unlockedWordList.size() ; i++)
            {
                String unlockedWord = unlockedWordList.get(i);
                Point point = getPoint(unlockedWord);
                String type = getType(unlockedWord);
                crosswordDisplay.getUnlockedWordDetail(unlockedWord,point,type);
            }
            extractScores(scores);
        }
        else
        {
            game = gameOrganizer.getGame();
            saveSystem.saveGameKey(gameKey);
            saveGame(game.firstWordDirection);
            unlockedWordList = new ArrayList<>();
            crosswordDisplay.setCrossword(game,this);
        }
    }

    private void  extractScores(ArrayList<String> scores)
    {
        lastGameSecond = Integer.parseInt(scores.get(0));
        negativeAttempts = Integer.parseInt(scores.get(1));
        positiveAttempts = Integer.parseInt(scores.get(2));
    }
    private void saveScores()
    {
        String result = "";
        result += (String.valueOf(second)) + "\n";
        result += (String.valueOf(negativeAttempts)) + "\n";
        result += (String.valueOf(positiveAttempts));
        saveSystem.saveScoreEssentials(result);
    }

    private String getType(String word)
    {
        if(game.horizontalWords.get(word) != null)
        {
            return  "HORIZONTAL";
        }
        else
        {
            return  "VERTICAL";
        }
    }
    private Point getPoint(String word)
    {
        if(game.horizontalWords.get(word) != null)
        {
            return game.horizontalWords.get(word);
        }
        else
        {
            return game.verticalWords.get(word);
        }
    }

    private void saveGame(String firstWordDirection)
    {
        saveSystem.saveFirstWordDirection(firstWordDirection);
    }

    @Override
    public void onClick(View view)
    {
        if(view.equals(shuffle))
        {
            drawLine.shuffleActivated();
        }
    }

}