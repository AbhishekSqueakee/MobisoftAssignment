package com.global;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import com.entity.PlaceDetailEntity;
import com.example.testmaps.HomePage;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class ParseSyncData {
	Context cntx;
	ProgressDialog mDialog;
	public ParseSyncData(Context cnt){
		cntx=cnt;
		mDialog=new ProgressDialog(cntx);
	}


	public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) cntx.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
  
          }
          return false;
    }
	
	 @SuppressWarnings("deprecation")
		public void showAlertDialog(String title, String message) {
	        AlertDialog alertDialog = new AlertDialog.Builder(cntx).create();
	        alertDialog.setTitle(title);
	        alertDialog.setMessage(message);
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
	        alertDialog.show();
	    }
	 
	 
	 public void getPlaceList(String url){
		 try{
			 getPlaceTask task=new getPlaceTask();
			 task.URL=url;
			 task.execute();
		 }catch(Exception e){
			 Log.d("Error", ""+e.toString());
		 }
	 }
	 
	 private class getPlaceTask extends AsyncTask<Void, Void, Void> {
		 String URL;
		 String result;
		 ArrayList<PlaceDetailEntity> locationList;
		 @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			 if(!mDialog.isShowing()){
					mDialog.setMessage("Loading...");
					mDialog.setCancelable(false);
					mDialog.show();
				}
			 locationList=new ArrayList<PlaceDetailEntity>();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			InputStream is = null;
			 try {
				
				   String url=URL;
				   HttpClient httpclient = new DefaultHttpClient();
				   HttpPost httppost = new HttpPost(url );
				   HttpResponse response = httpclient.execute(httppost);
				   HttpEntity entity = response.getEntity();
				   is = entity.getContent();
				   try {
					   BufferedReader reader = new BufferedReader(new InputStreamReader(
					     is, "iso-8859-1"), 8);
					   StringBuilder sb = new StringBuilder();
					   String line = null;
					   while ((line = reader.readLine()) != null) {
					    sb.append(line + "\n");
					   }
					   is.close();
					   result = sb.toString().trim();
					   JSONObject jObj = new JSONObject(result);
					   if(jObj.getString("results").contains("geometry")){
						   locationList = fetchData(jObj);
					   }
					   } catch (Exception e) {
					   Log.e("log_tag", "Error converting result " + e.toString());
					  }
				  } catch (Exception e) {
				   Log.e("log_tag", "Error in http connection " + e.toString());
				  }
			
			return null;
		} 
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if(mDialog.isShowing())
				mDialog.dismiss();
			if ( cntx instanceof HomePage ) {
				// do something
				((HomePage) cntx).displayLocationList(locationList);
			}
			super.onPostExecute(result);
		}
		
	 }
	 
	 public static HttpResponse hitUrl(String url) {
		  try {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    return response;
		  } catch (Exception e) {
		   
		    return null;
		  }
		}
	
	 public ArrayList<PlaceDetailEntity> fetchData(JSONObject response){
		 ArrayList<PlaceDetailEntity> data=new ArrayList<PlaceDetailEntity>();
		 try{
			 
			 JSONArray array = response.getJSONArray("results");
			 if (array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject sojsonObject = array.getJSONObject(i);
						PlaceDetailEntity temp=new PlaceDetailEntity();
						temp.id=sojsonObject.getString("id");
						temp.icon=sojsonObject.getString("icon");
						temp.geometry=sojsonObject.getString("geometry");
						temp.name=sojsonObject.getString("name");
						JSONObject jObj = new JSONObject(temp.geometry);
						String location = jObj.getString("location");
						JSONObject sojson;
						temp.formatted_address=sojsonObject.getString("vicinity");
						try{
							JSONArray photos=sojsonObject.getJSONArray("photos");
							sojson = photos.getJSONObject(0);
							temp.referenceImage=sojson.getString("photo_reference");
						}catch(Exception e){}
						sojson = new JSONObject(location);
						temp.lat=sojson.getDouble("lat");
						temp.lng=sojson.getDouble("lng");
						data.add(temp);
					}
			 }
			 
		 }catch(Exception e){
			 Log.d("", "");
		 }
		 return data;
	 }


	 
}