package com.example.word_city;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class Scoreboard extends AppCompatActivity
{

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        tableLayout = findViewById(R.id.tableLayout);
        String[][] data = (String[][]) getIntent().getExtras().getSerializable("data");
        for (int i = 0; i <data.length ; i++)
        {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item,null,false);
            TextView name  = (TextView) tableRow.findViewById(R.id.name);
            TextView title  = (TextView) tableRow.findViewById(R.id.title);

            name.setText(data[i][0]);
            title.setText(data[i][1]);
            tableLayout.addView(tableRow);
        }


    }
}
