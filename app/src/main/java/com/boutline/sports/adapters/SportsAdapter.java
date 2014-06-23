package com.boutline.sports.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.boutline.sports.models.Sport;
import com.boutline.sports.R;

import java.util.ArrayList;

public class SportsAdapter extends ArrayAdapter<Sport> {
	
	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	
    private static class ViewHolder {
        TextView lblSportName;
        TextView lblSportDescription;
        CheckBox chkFollowStatus;
    }

    public SportsAdapter(Context context, ArrayList<Sport> sports) {
       super(context, R.layout.item_sport, sports);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  
    	
       
       // Check if an existing view is being reused, otherwise inflate the view
       
       Sport sport = getItem(position);    
       ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_sport, parent, false);
          viewHolder.lblSportName = (TextView) convertView.findViewById(R.id.lblSportName);
          viewHolder.lblSportDescription = (TextView) convertView.findViewById(R.id.lblSportDescription);
          viewHolder.chkFollowStatus = (CheckBox) convertView.findViewById(R.id.chkFollowStatus);
          convertView.setTag(viewHolder);
          	
       } 
       else {
           viewHolder = (ViewHolder) convertView.getTag();
       }
       
       // Populate the data into the template view using the data object
       
       viewHolder.lblSportName.setText(sport.getSportName());
       viewHolder.lblSportDescription.setText(sport.getSportDescription());
       viewHolder.chkFollowStatus.setChecked(sport.getSportFollow());
       tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
       btf = Typeface.createFromAsset(getContext().getAssets(), boldFontPath);
       viewHolder.lblSportName.setTypeface(btf);
       viewHolder.lblSportDescription.setTypeface(tf);
       
       // Return the completed view to render on screen
       
       return convertView;
   }
}