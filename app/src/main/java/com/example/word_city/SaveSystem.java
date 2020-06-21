package com.example.word_city;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SaveSystem implements SaveFiles
{
    Context context ;
    SaveSystem(Context context)
    {
        this.context = context ;
    }

    private void writeToFile(String data, String fileName, String mode)
    {
        try
        {
            OutputStreamWriter outputStreamWriter = getStreamWriter(fileName, mode);
            outputStreamWriter.write(data);
            outputStreamWriter.write('\n');
            outputStreamWriter.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private ArrayList<String> readFromFile(String fileName)
    {
        ArrayList<String> lines = new ArrayList<>();
        try
        {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null )
                {
                    receiveString = receiveString.trim();
                    lines.add(receiveString);
                }
                inputStream.close();
            }
        }
        catch (FileNotFoundException e)
        {
            Log.e("login activity", "File not found: " + e.toString());
        }
        catch (IOException e)
        {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return lines;
    }

    protected void deleteSaveFiles()
    {
        deleteAllFiles();
    }

    protected String  getFirstWordDirection()
    {
        return  readFromFile(FIRST_WORD_DIRECTION).get(0);
    }

    protected void saveFirstWordDirection(String firstWordDirection)
    {
        writeToFile(firstWordDirection, FIRST_WORD_DIRECTION,WRITE_MODE);
    }

    protected ArrayList<String> getUnlockedWords()
    {
        return readFromFile(UNLOCKED_WORDS);
    }

    protected  void saveUnlockedWords(String word)
    {
        writeToFile(word, UNLOCKED_WORDS,APPEND_MODE);
    }

    protected boolean isAnyGameSaved(String gameKey)
    {
        String savedGameKey ;
        try
        {
            savedGameKey = getGameKey();
        }
        catch (IndexOutOfBoundsException ex)
        {
            return false;
        }

        File file = new File(context.getFilesDir(),FIRST_WORD_DIRECTION);
        if(file.exists() && savedGameKey.equals(gameKey))
        {
            return true;
        }
        else
        {
            deleteSaveFiles();
            return false;
        }
    }

    protected void saveGameKey(String gameKey)
    {
        writeToFile(gameKey, GAME_KEY, WRITE_MODE);
    }

    protected String getGameKey()
    {
        return readFromFile(GAME_KEY).get(0);
    }

    private void deleteAllFiles()
    {
        for (int i = 0; i <FILES.length ; i++)
        {
            String path = FILES[i];
            File dir = context.getFilesDir();
            File file = new File(dir, path);
            file.delete();
        }
    }

    protected void saveScoreEssentials(String data)
    {
        writeToFile(data, SCORE, WRITE_MODE);
    }

    protected ArrayList<String> getScoreEssentials()
    {
        return readFromFile(SCORE);
    }


    private OutputStreamWriter getStreamWriter(String fileName,String mode)
{
    try
    {
        if(mode.equals(WRITE_MODE))
        {
            return  new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
        }
        else
        {
            return  new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND));
        }
    }
    catch (FileNotFoundException e)
    {
        e.printStackTrace();
    }
    return null ;
}

}
