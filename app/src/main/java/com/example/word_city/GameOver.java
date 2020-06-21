package com.example.word_city;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity implements View.OnClickListener
{
    Button next_game, back ;
    TextView score_view, second_view, wrongAttempt_view ;
    String gameKey ;
    double score;
    int second;
    int negativeAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        score = getIntent().getDoubleExtra("Score",0);
        second = getIntent().getIntExtra("Second",0);
        negativeAttempts = getIntent().getIntExtra("NegativeAttempts",0);
        gameKey = getIntent().getStringExtra("gameKey");

        score_view = findViewById(R.id.score_view);
        second_view = findViewById(R.id.second_view);
        wrongAttempt_view = findViewById(R.id.wattemp_view);

        back = findViewById(R.id.back);
        next_game = findViewById(R.id.next_game);
        next_game.setOnClickListener(this);
        back.setOnClickListener(this);

        setDetails();
    }

    private String getSubLevelKey()
    {
        String subLevelKey ;
        char current_game = gameKey.charAt(3);
        int current_game_number = current_game - '0' ;
        if(current_game_number < 6)
        {
            current_game += (char)(1) ;
            String next_game = String.valueOf(current_game);
            subLevelKey = "G" + next_game ;
        }
        else
        {
            next_game.setClickable(false);
            next_game.setEnabled(false);
            return null;
        }
        return subLevelKey;
    }

    private void setDetails()
    {
        String score_text = String.valueOf(score);
        String second_text = String.valueOf(second);
        String negative_attempts = String.valueOf(negativeAttempts);
        score_view.setText(score_text);
        second_view.setText(second_text);
        wrongAttempt_view.setText(negative_attempts);
    }


    @Override
    public void onClick(View view)
    {
        String level = gameKey.substring(0,2);
        if(view.equals(back))
        {
            Intent sub_level_section = new Intent(this, Sub_Level_Section.class);
            sub_level_section.putExtra("level",level);
            startActivity(sub_level_section);
        }
        else
        {
            Intent game = new Intent(this, Game.class);
            game.putExtra("level",level);
            String subLevel = getSubLevelKey();
            if(subLevel != null)
            {
                game.putExtra("game",subLevel);
                startActivity(game);
            }
        }
    }
}
