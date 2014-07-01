package com.boutline.sports.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        ImageView imgSport;
        RelativeLayout sportContainer;
    }

    public SportsAdapter(Context context, ArrayList<Sport> sports) {
       super(context, R.layout.item_sport, sports);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  
    	
       
       // Check if an existing view is being reused, otherwise inflate the view
       
       Sport sport = getItem(position);    
       final ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_sport, parent, false);
          viewHolder.lblSportName = (TextView) convertView.findViewById(R.id.lblSportName);
          viewHolder.lblSportDescription = (TextView) convertView.findViewById(R.id.lblSportDescription);
          viewHolder.chkFollowStatus = (CheckBox) convertView.findViewById(R.id.chkFollowStatus);
          viewHolder.imgSport = (ImageView) convertView.findViewById(R.id.imgSport);
          viewHolder.sportContainer = (RelativeLayout) convertView.findViewById(R.id.sportContainer);

          convertView.setTag(viewHolder);
          	
       } 
       else {
           viewHolder = (ViewHolder) convertView.getTag();
       }

        /* Following code is to get image from a url, IMPORTANT: Do this in background thread
            try {
                URL newurl = new URL("http://boutline.com/" + sport.sportName() + ".png");
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                viewHolder.imgSport.setImageBitmap(mIcon_val);
            } catch (IOException e) {
                e.printStackTrace();
            }
        */

        viewHolder.lblSportName.setText(sport.getSportName());
        viewHolder.lblSportDescription.setText(sport.getSportDescription());
        viewHolder.chkFollowStatus.setChecked(sport.getSportFollow());
        //Remove the following
        if(sport.getSportName().equals("Cricket")) {
            viewHolder.imgSport.setImageDrawable(getContext().getResources().getDrawable(R.drawable.sport_cricket));
        }
        else{
            viewHolder.imgSport.setImageDrawable(getContext().getResources().getDrawable(R.drawable.sport_football));
        }

        tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
        btf = Typeface.createFromAsset(getContext().getAssets(), boldFontPath);
        viewHolder.lblSportName.setTypeface(btf);
        viewHolder.lblSportDescription.setTypeface(btf);

        viewHolder.sportContainer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View arg0) {
               viewHolder.chkFollowStatus.setChecked(!(viewHolder.chkFollowStatus.isChecked()));
           }
        });

       // Return the completed view to render on screen
       
        return convertView;
   }
}