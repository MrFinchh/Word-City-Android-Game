package com.example.word_city;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Level_Section extends AppCompatActivity implements View.OnClickListener
{
    Button lvl1, lvl2, lvl3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level__section);
        activate_components();
    }

    private void activate_components()
    {
        lvl1 = findViewById(R.id.lvl1_button);
        lvl2 = findViewById(R.id.lvl2_button);
        lvl3 = findViewById(R.id.lvl3_button);

        lvl1.setOnClickListener(this);
        lvl2.setOnClickListener(this);
        lvl3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Intent sub_level_section = new Intent(this, Sub_Level_Section.class);
        if(view.equals(lvl1))
        {
            sub_level_section.putExtra("level","L1");
        }
        if(view.equals(lvl2))
        {
            sub_level_section.putExtra("level","L2");
        }
        if(view.equals(lvl3))
        {
            sub_level_section.putExtra("level","L3");
        }
        startActivity(sub_level_section);
    }
}
