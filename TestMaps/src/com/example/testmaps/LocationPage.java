package com.example.testmaps;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;

import com.entity.PlaceDetailEntity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 
public class LocationPage extends FragmentActivity {
 
	SupportMapFragment fm;
    GoogleMap googleMap;
    PlaceDetailEntity temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_location_page);
        temp=new PlaceDetailEntity();
        temp=(PlaceDetailEntity) getIntent().getSerializableExtra("placeDetails");
        
                fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
 
                        // Getting GoogleMap object from the fragment
                        googleMap = fm.getMap();
 
                        // Enabling MyLocation Layer of Google Map
                        googleMap.setMyLocationEnabled(true);

                        // LatLng object to store user input coordinates
                        LatLng point = new LatLng(Double.parseDouble(""+temp.lat), Double.parseDouble(""+temp.lng));
 
                        // Drawing the marker at the coordinates
                        drawMarker(point);
                
                
            }
 
    
 
    private void drawMarker(LatLng point){
    	//googleMap = fm.getMap();
    	if(googleMap!=null){
	        // Clears all the existing coordinates
	        googleMap.clear();
	 
	        // Creating an instance of MarkerOptions
	        MarkerOptions markerOptions = new MarkerOptions();
	 
	        // Setting latitude and longitude for the marker
	        markerOptions.position(point);
	 
	        // Setting title for the InfoWindow
	        markerOptions.title(""+temp.name);
	 
	        // Setting InfoWindow contents
	        markerOptions.snippet("Latitude:"+point.latitude+",Longitude"+point.longitude);
	 
	        // Adding marker on the Google Map
	        googleMap.addMarker(markerOptions);
	 
	        // Moving CameraPosition to the user input coordinates
	        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
    	}
    }
 
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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