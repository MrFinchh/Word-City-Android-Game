package com.example.word_city;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "WordCity.db";
    private static String TABLE_NAME = "Scoreboard";
    private static String GAME_NAME = "GameName";
    private static String GAME_SCORE = "GameScore";

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"(GameName TEXT PRIMARY KEY, GameScore REAL)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    protected void saveData(String gameName, double score)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        String[] projection = {GAME_NAME, GAME_SCORE};
        String selection = GAME_NAME + " = ?";
        String[] selectionArgs = {gameName};
        String order = GAME_NAME + " ASC";
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null,null, order);
        int count = cursor.getCount();
        if(count > 0)
        {
            cursor.moveToNext();
            double current_score = cursor.getDouble(1);
            if(current_score < score)
            {
                updateData(gameName,score);
            }
        }
        else
        {
            addData(gameName,score);
        }
        cursor.close();
        db.close();
    }

    private void addData(String gameName, double score)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(GAME_NAME, gameName);
        values.put(GAME_SCORE, score);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    private void updateData(String gameName, double score)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GAME_SCORE , score);
        String selection = GAME_NAME + " = ?";
        String[] selectionArgs = {gameName};
        db.update(TABLE_NAME,values,selection,selectionArgs);
        db.close();
    }

    private Cursor getCursor()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor result = db.rawQuery(query,null);
        return result;
    }

    protected String[][] getData()
    {
        Cursor result = getCursor();
        int size = result.getCount();

        String[][] data = new String[size][2];
        int row = 0 ;
        while(result.moveToNext())
        {
            data[row][0] = result.getString(0);
            data[row][1] = result.getString(1);
            row++;
        }
        result.close();
        return data;
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

}
