package com.example.testmaps;

import java.util.ArrayList;

import com.entity.PlaceDetailEntity;
import com.global.FMDBDatabaseAccess;
import com.imageworker.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationDetailsPage extends Activity{
	TextView placeName;
	TextView placeDetails;
	ImageView placeImage;
	Button btnMap;
	Button btnFav;
	PlaceDetailEntity temp;
	ArrayList<PlaceDetailEntity> locationList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.place_details_page);
		initializeView();
		super.onCreate(savedInstanceState);
	}

	void initializeView(){
		placeName=(TextView)findViewById(R.id.locationText);
		placeDetails=(TextView)findViewById(R.id.locationDetailsText);
		placeImage=(ImageView)findViewById(R.id.placeLargeImage);
		btnFav=(Button)findViewById(R.id.btn_Fav);
		btnMap=(Button)findViewById(R.id.btn_Map);
		try{
			temp = (PlaceDetailEntity) getIntent().getSerializableExtra("placeDetails");
			if(temp!=null){
				placeName.setText(temp.name);
				placeDetails.setText(temp.formatted_address);
				ImageLoader imageworker=new ImageLoader(LocationDetailsPage.this);
				String imageUrl="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+temp.referenceImage+"&key="+getResources().getString(R.string.api_key);
				imageworker.DisplayImage(imageUrl,placeImage );
			}
			btnFav.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(temp!=null){
						FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(LocationDetailsPage.this);
						fmdb.insertLocationDetails(temp);
					}
				}
			});
			
			btnMap.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(LocationDetailsPage.this, LocationPage.class);
					i.putExtra("placeDetails", temp);
					startActivity(i);
					overridePendingTransition(R.anim.left,R.anim.stillanim);
				}
			});
		}catch(Exception e){
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch(keyCode){
	    case KeyEvent.KEYCODE_BACK:
	        // do something here 
	    	finish();
	    	overridePendingTransition(R.anim.stillanim,R.anim.righttoleft);
	        
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
