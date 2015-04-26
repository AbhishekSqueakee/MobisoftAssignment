package com.entity;

import java.util.ArrayList;

import com.example.testmaps.R;
import com.imageworker.ImageLoader;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterClass extends BaseAdapter{
	ArrayList<PlaceDetailEntity> arrObject;
	Context context;
	ImageLoader imageworker;
	class ViewHolder {
        TextView title;
        TextView description;
        ImageView img;
        
    }
	public AdapterClass(Context c,ArrayList<PlaceDetailEntity> arrTemp){
		context=c;
		arrObject=arrTemp;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrObject.size();
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		 final LayoutInflater inflater = (LayoutInflater) context.getApplicationContext()
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
           convertView = inflater.inflate(R.layout.row_cell_place, null);

           holder = new ViewHolder();
           holder.title = (TextView) convertView.findViewById(R.id.TVPlaceName);
           holder.img = (ImageView) convertView.findViewById(R.id.place_image);
           convertView.setTag(holder);
       } else {
           // view already defined, retrieve view holder
           holder = (ViewHolder) convertView.getTag();
       } 
		ImageLoader imageworker=new ImageLoader(context);
		imageworker.DisplayImage(arrObject.get(position).icon,holder.img );
		holder.title.setText(arrObject.get(position).name);
		return convertView;
	}

	

	

}
