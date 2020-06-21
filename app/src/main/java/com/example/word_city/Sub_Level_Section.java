package com.example.word_city;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Sub_Level_Section extends AppCompatActivity implements View.OnClickListener
{
    TextView level_display;

    String level;

    Button gm1, gm2, gm3, gm4, gm5, gm6;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //activity_sub__level__section
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__level__section);

        level = getIntent().getStringExtra("level");

        activate_components();

        level_display.append(level);
    }

    private void activate_components()
    {
        gm1 = findViewById(R.id.gm1);
        gm1.setOnClickListener(this);

        gm2 = findViewById(R.id.gm2);
        gm2.setOnClickListener(this);

        gm3 = findViewById(R.id.gm3);
        gm3.setOnClickListener(this);

        gm4 = findViewById(R.id.gm4);
        gm4.setOnClickListener(this);

        gm5 = findViewById(R.id.gm5);
        gm5.setOnClickListener(this);

        gm6 = findViewById(R.id.gm6);
        gm6.setOnClickListener(this);

        level_display = findViewById(R.id.level_display_text);

    }

    @Override
    public void onClick(View view)
    {
        Intent game = new Intent(this, Game.class);

        game.putExtra("level",level);
        if(view.equals(gm1))
        {
            game.putExtra("game", "G1");
        }
        if(view.equals(gm2))
        {
            game.putExtra("game", "G2");
        }
        if(view.equals(gm3))
        {
            game.putExtra("game", "G3");
        }
        if(view.equals(gm4))
        {
            game.putExtra("game", "G4");
        }
        if(view.equals(gm5))
        {
            game.putExtra("game", "G5");
        }
        if(view.equals(gm6))
        {
            game.putExtra("game", "G6");
        }
        startActivity(game);
    }
}
