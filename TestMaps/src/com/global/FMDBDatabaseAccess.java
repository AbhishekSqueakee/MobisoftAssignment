package com.global;

import java.util.ArrayList;

import com.entity.PlaceDetailEntity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.JetPlayer.OnJetEventListener;
import android.util.Log;
import android.widget.Toast;

public class FMDBDatabaseAccess extends Activity
{
	SQLiteDatabase mydb;
	Context context;
	String LoginUserID;
	private static String DBNAME = "testmaps.db";

	
	public FMDBDatabaseAccess(Context con)
	{
		context=con;
		DBNAME = "testmaps.db";
		SharedPreferences SP = con.getSharedPreferences("AppDelegate",MODE_PRIVATE);
		
	}
	
	
	
	  public ArrayList<PlaceDetailEntity> GetLocationDetails()
		 {
		  	 ArrayList<PlaceDetailEntity> placeList =new ArrayList<PlaceDetailEntity>();
		     try {
		    	 mydb= context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
		         String strQuery;
		         strQuery="select * from FavPlaces";
		         
		         Cursor cursor=mydb.rawQuery(strQuery, null);
		         PlaceDetailEntity temp;
		         if(cursor.moveToFirst())
		         while(cursor.moveToNext()){
		        	 temp=new PlaceDetailEntity();
		        	 temp.formatted_address=cursor.getString(cursor.getColumnIndex("formatted_address"));
		        	 temp.geometry=cursor.getString(cursor.getColumnIndex("geometry"));
		        	 temp.lat=cursor.getDouble(cursor.getColumnIndex("lat"));
		        	 temp.lng=cursor.getDouble(cursor.getColumnIndex("lng"));
		        	 temp.icon=cursor.getString(cursor.getColumnIndex("icon"));
		        	 temp.id=cursor.getString(cursor.getColumnIndex("id"));
		        	 temp.name=cursor.getString(cursor.getColumnIndex("name"));
		        	 temp.referenceImage=cursor.getString(cursor.getColumnIndex("referenceImage"));
		        	 placeList.add(temp);
		         }
		         cursor.close();
		        
		         
		         return placeList;
		         
		     }
		     catch (Exception e) {
		         return null;
		     }
		     finally{
		    	 mydb.close();
		     }
		     
		 }
	  
	 

	  public boolean  insertLocationDetails(PlaceDetailEntity temp)
	  {
	      try {
	    	  mydb= context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
	    	  String strQuery="select * from FavPlaces where id='"+temp.id+"'";
	    	  Cursor cursor=mydb.rawQuery(strQuery, null);
	    	  if(cursor.moveToFirst()){
	    		  Toast.makeText(context, "Location Already Saved", Toast.LENGTH_SHORT).show();
	    		  return true;
	    	  }
	    	  else{
		    	 
		          String strq="insert into FavPlaces(formatted_address,geometry,lat,lng,icon,id,name,referenceImage) values('"+temp.formatted_address+"','"+temp.geometry+"','"+temp.lat+"','"+temp.lng+"','"+temp.icon+"','"+temp.id+"','"+temp.name+"','"+temp.referenceImage+"');";
		          mydb.execSQL(strq);
		         
		          Toast.makeText(context, "Location Saved Succesfully", Toast.LENGTH_SHORT).show();
		          return true;
	    	  }
	          
	      }
	      catch (Exception e) {
	    	  Log.d("Error =", ""+e.getMessage());
	    	  return false;
	      }
	      finally{
	    	  mydb.close();
	      }
	      
	  }
	  
	  

}
