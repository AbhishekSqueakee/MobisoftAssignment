package com.example.testmaps;


import java.util.ArrayList;
import com.entity.AdapterClass;
import com.entity.PlaceDetailEntity;
import com.global.FMDBDatabaseAccess;
import com.global.ParseSyncData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomePage extends Activity{
	ArrayList<String> placeList;
	ListView listViewPlace;
	ListView lvPlaceList;
	ParseSyncData parser;
	Location location;
	FMDBDatabaseAccess fmdb;
	ProgressDialog mDialog;
	LinearLayout defaultLay;
	LinearLayout placeLay;
	ArrayList<PlaceDetailEntity> locationList;
	boolean isListDisplaying=false;
	String catType;
	int radius;
	Button btnFav;
	Display display;
	Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		parser=new ParseSyncData(HomePage.this);
		fmdb=new FMDBDatabaseAccess(HomePage.this);
		mDialog=new ProgressDialog(HomePage.this);
		defaultLay=(LinearLayout)findViewById(R.id.defaultLay);
		placeLay=(LinearLayout)findViewById(R.id.placeListLay);
		placeLay.setVisibility(View.GONE);
		lvPlaceList=(ListView)findViewById(R.id.lv_placeList);
		initializeView();
		GPSTracker gps=new GPSTracker(HomePage.this);
		location=gps.getLocation();
		display=getWindowManager().getDefaultDisplay();
		mHandler=new Handler();
		
	}
	
	private void initializeView(){
		try{
			locationList=new ArrayList<PlaceDetailEntity>();
			placeList=new ArrayList<String>();
			placeList.add("Food");
			placeList.add("Gym");
			placeList.add("School");
			placeList.add("Hospital");
			placeList.add("Spa");
			placeList.add("Resturant");
			listViewPlace = (ListView) findViewById(R.id.listview_place);
			listViewPlace.setAdapter(adapterPlaceType);
			listViewPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(parser.isConnectingToInternet()){
					final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
		                    HomePage.this,android.R.layout.select_dialog_singlechoice);
					catType=placeList.get(position).toLowerCase();
					AlertDialog.Builder builderSingle = new AlertDialog.Builder(HomePage.this);
		            builderSingle.setIcon(R.drawable.ic_launcher);
		            builderSingle.setTitle("Select Distance(in KM) For Displaying Results:-");
		            arrayAdapter.add("1");
		            arrayAdapter.add("2");
		            arrayAdapter.add("4");
		            arrayAdapter.add("5");
		            arrayAdapter.add("10");
		            arrayAdapter.add("15");
		            arrayAdapter.add("20");
		            arrayAdapter.add("25");
		            arrayAdapter.add("30");
		            builderSingle.setNegativeButton("cancel",
		                    new DialogInterface.OnClickListener() {

		                        @Override
		                        public void onClick(DialogInterface dialog, int which) {
		                            dialog.dismiss();
		                        }
		                    });

		            builderSingle.setAdapter(arrayAdapter,
		                    new DialogInterface.OnClickListener() {

		                        @Override
		                        public void onClick(DialogInterface dialog, int rad) {
		                        	radius=Integer.parseInt(arrayAdapter.getItem(rad));
		                        	getData();
		                        }
		                    });
		            builderSingle.show();
				}
					else{
						parser.showAlertDialog("Connection Error", "No Internet Access");
					}
				}
	        });
			
			lvPlaceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent i=new Intent(HomePage.this, LocationDetailsPage.class);
					i.putExtra("placeDetails", locationList.get(position));
					startActivity(i);
					overridePendingTransition(R.anim.left,R.anim.stillanim);
				}
			});
			btnFav=(Button)findViewById(R.id.showFavBtn);
			btnFav.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(HomePage.this, FavPlacePage.class);
					startActivity(i);
					overridePendingTransition(R.anim.pushup,R.anim.stillanim);
				}
			});
		}catch(Exception e){
			Log.d("Error", ""+e.toString());
		}
	}
	
	
	private BaseAdapter adapterPlaceType = new BaseAdapter() {
		class ViewHolder {
	        TextView txt_placename;
	    	
	    }
		ViewHolder holder = null;
		
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return placeList.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				try{
						 final LayoutInflater inflater = (LayoutInflater) HomePage.this.getApplicationContext()
					                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						 if (convertView == null) {
							holder = new ViewHolder();
				            convertView = inflater.inflate(R.layout.row_place, null);
				            holder.txt_placename=(TextView)convertView.findViewById(R.id.txt_placeName); 
				            convertView.setTag(holder);
				        } else {
				            // view already defined, retrieve view holder
				            holder = (ViewHolder) convertView.getTag();
				        } 
						 holder.txt_placename.setText(placeList.get(position));
					 }catch(Exception e)
					 {
						 Log.d("Error", ""+e.toString());
					 }
				
				return convertView;
			}
		};
		
		public void getData(){
			radius=radius*1000;
		 	String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+location.getLatitude()+","+location.getLongitude()+
	    			"&radius="+radius+"&types="+catType+"&key="+getResources().getString(R.string.api_key);
		 	parser.getPlaceList(url);
		}
		
		public void displayLocationList(ArrayList<PlaceDetailEntity> tempList){
			try{
				
				if(!tempList.isEmpty()){
					locationList=tempList;
					placeLay.setVisibility(View.VISIBLE);
					TranslateAnimation animA=new TranslateAnimation(display.getWidth(),0,0,0);
					animA.setDuration(1000);
					placeLay.startAnimation(animA);
					
					if(locationList.size()!=0){
						AdapterClass madapter=new AdapterClass(HomePage.this, locationList);
						lvPlaceList.setAdapter(madapter);
						isListDisplaying=true;
					}
					mHandler.postDelayed(timedtask, 2000);
					
				}
			}catch(Exception e){
				
			}
		}
	
		private Runnable timedtask=new Runnable() {
			@Override
			public void run()
			{
				try{
				// TODO Auto-generated method stub
					defaultLay.setVisibility(View.GONE);
					btnFav.setVisibility(View.INVISIBLE);
				}catch(Exception e){
					Log.d("Exception timedtask=", e.toString());
				}
				}
			
				
		};
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		    switch(keyCode){
		    case KeyEvent.KEYCODE_BACK:
		        // do something here 
		    	if(isListDisplaying){
		    		defaultLay.setVisibility(View.VISIBLE);
					listViewPlace.setVisibility(View.VISIBLE);
					btnFav.setVisibility(View.VISIBLE);
					isListDisplaying=false;
					TranslateAnimation animA=new TranslateAnimation(0,display.getWidth(),0,0);
					animA.setDuration(1000);
					placeLay.startAnimation(animA);
					placeLay.setVisibility(View.GONE);
					return true;
		    	}
		    	finish();
		    	overridePendingTransition(R.anim.stillanim,R.anim.righttoleft);
		    	
		        
		    }
		    return super.onKeyDown(keyCode, event);
		}
}
