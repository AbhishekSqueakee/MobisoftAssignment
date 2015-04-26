
package com.example.testmaps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.global.DataBaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class StartPage extends Activity 
{
	private static String DB_PATH = "/data/data/com.example.testmaps/databases/";
	 
    private static String DB_NAME = "testmaps.db";
    @Override
	 public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start_page);
        DataBaseHelper  myDbHelper = new DataBaseHelper(this);
      try{
        boolean dbexists1=CheckIfDBExists();
        	if(!dbexists1){
        		myDbHelper.createDataBase();
        		
        		
        	}

 
        } 
        catch (IOException ioe) 
        {
 
 		//throw new Error("Unable to create database");
        	Log.d("Error Main_Start_Page onCreate IOException=", ""+ioe.getMessage());
        	
        }
        try 
        {
 
        	myDbHelper.openDataBase();
        	myDbHelper.close();
        	
        	Intent i=new Intent(StartPage.this, HomePage.class);
        	startActivity(i);
        	finish();
        }
        catch(SQLException sqle){
 
 		//throw sqle;
        	Log.d("Error Main_Start_Page onCreate SQLException=", ""+sqle.getMessage());
 
        }
            catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
    }


	
		
	 private boolean checkDataBase(){
		 
	    	//SQLiteDatabase checkDB = null;
	    	boolean retVal=false;
	    	try{
	    		String myPath = DB_PATH + DB_NAME;
	    		File fdb=new File(myPath);
	    		if(fdb.exists())
	    		{
	    			retVal=true;
	    		}
	    		else
	    		{
	    			retVal=false;
	    		}
	    		//checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    	}catch(SQLiteException e){
	 
	    		//database does't exist yet.
	 
	    	}
			return retVal;
	 }

	 public boolean CheckIfDBExists() throws IOException{
	    	boolean dbExist=false;
	    	dbExist = checkDataBase();
	    	return dbExist;
	    }

	}
