package com.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.testmaps/databases/";
 
    private static String DB_NAME = "testmaps.db";
 
    private SQLiteDatabase mydb_local; 
 
    private final Context context;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.context = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
    	
    		
    		
    try{
    	
    		
    	
    	if(dbExist){
    		//do nothing - database already exist
    		/*context.deleteDatabase(DB_NAME);
    		this.getReadableDatabase();
    		 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}*/
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
    			copyDataBase1();
    		} catch (IOException e) {
 
        		//throw new Error("Error copying database");
    			Log.d("Error=", ""+e.getMessage());
        	}
    	}
    }
    catch(Exception e)
    {
    	Log.d("Exception",""+e.getMessage());
    }
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	boolean retVal = false;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		File fdb=new File(myPath);
    		if(fdb.exists())
    		{
    			retVal=true;
    		}
    		else
    		{
    			retVal=false;
    		}
    	}catch(Exception e){
    		Log.d("", "");
    		//database does't exist yet.
 
    	}
 
    	//if(checkDB != null){
 //
    		//checkDB.close();
 
    	//}
 
    	return retVal;// != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	try{
    	InputStream myInput = context.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    	}
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
    }
 
    private void copyDataBase1() throws IOException{
    	 
    	//Open your local db as the input stream
    	InputStream myInput = new FileInputStream("/data/data/com.example.testmaps/databases/testmaps.db");
 
    	// Path to the just created empty db
    	String outFileName ="";
    	outFileName=Environment.getExternalStorageDirectory()+"/baidu/mapbackup.db";
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
        mydb_local = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 try{
    	    if(mydb_local != null)
    	    	mydb_local.close();
 }
 catch(Exception e)
 {
 	Log.d("Exception",""+e.getMessage());
 }
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return mydb_iTrain.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
}
