package com.boutline.sports.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.R;
import com.boutline.sports.models.Tournament;

import java.util.ArrayList;

public class BanterTopicsAdapter extends ArrayAdapter<Tournament> {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;

    private static class ViewHolder {
        RelativeLayout tourContainer;
        TextView lblTournamentName;
        CheckBox chkFollowStatus;
    }
    public BanterTopicsAdapter(Context context, ArrayList<Tournament> tournaments) {
       super(context, R.layout.item_bantertopic, tournaments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
       // Get the data item for this position
    	
       Tournament tournament = getItem(position);    
       
       // Check if an existing view is being reused, otherwise inflate the view
       
       final ViewHolder viewHolder; // view lookup cache stored in tag
       
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_bantertopic, parent, false);
          viewHolder.tourContainer = (RelativeLayout) convertView.findViewById(R.id.tourContainer);
          viewHolder.lblTournamentName = (TextView) convertView.findViewById(R.id.lblTournamentName);
          viewHolder.chkFollowStatus = (CheckBox) convertView.findViewById(R.id.chkFollowStatus);
          convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();
       }
  
       
       // Set the click listeners for following a tournament
       
       viewHolder.chkFollowStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        	   changeFollowStatusForTournament();
           }
         }
       );  
       
       
       
       // Populate the data into the template view using the data object
       
       viewHolder.lblTournamentName.setText("");
       viewHolder.chkFollowStatus.setChecked(false);
       tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
       btf = Typeface.createFromAsset(getContext().getAssets(), boldFontPath);
       viewHolder.lblTournamentName.setTypeface(btf);

        viewHolder.tourContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                viewHolder.chkFollowStatus.setChecked(!(viewHolder.chkFollowStatus.isChecked()));
            }
        });

        // Return the completed view to render on screen

       return convertView;
   }
    
    // Function to follow tournament - to be called when checkbox is toggled
    
    public void changeFollowStatusForTournament(){

    }
    
}