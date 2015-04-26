package com.example.testmaps;

import java.util.ArrayList;

import com.entity.AdapterClass;
import com.entity.PlaceDetailEntity;
import com.global.FMDBDatabaseAccess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class FavPlacePage extends Activity{
	ListView favListView;
	ArrayList<PlaceDetailEntity> locationList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.fav_place_page);
		favListView=(ListView)findViewById(R.id.list_favplace);
		favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i=new Intent(FavPlacePage.this, LocationDetailsPage.class);
				i.putExtra("placeDetails", locationList.get(position));
				startActivity(i);
				overridePendingTransition(R.anim.left,R.anim.stillanim);
			}
		});
		setFavPlaceList();
		super.onCreate(savedInstanceState);
	}

	void setFavPlaceList(){
		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(FavPlacePage.this);
		locationList=fmdb.GetLocationDetails();
		AdapterClass madapter=new AdapterClass(FavPlacePage.this, locationList);
		favListView.setAdapter(madapter);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch(keyCode){
	    case KeyEvent.KEYCODE_BACK:
	        // do something here 
	    	finish();
	    	overridePendingTransition(0,R.anim.push_down);
	        
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
