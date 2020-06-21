package com.example.word_city;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Database db ;
    Button play, scoreboard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activate_components();
        db = new Database(this);
    }

    private void activate_components()
    {
        play = findViewById(R.id.play_button);
        scoreboard = findViewById(R.id.scoreboard_button);


        play.setOnClickListener(this);
        scoreboard.setOnClickListener(this);
    }



    @Override
    public void onClick(View view)
    {
        if(view.equals(play))
        {
            Intent level_section = new Intent(this, Level_Section.class);
            startActivity(level_section);
        }
        if(view.equals(scoreboard))
        {
            Intent scoreboard_section = new Intent(this,Scoreboard.class);
            String[][] data = db.getData();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data",data);
            scoreboard_section.putExtras(bundle);
            startActivity(scoreboard_section);
        }
    }
}
