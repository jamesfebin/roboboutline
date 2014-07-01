package com.boutline.sports.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.models.Tournament;
import com.boutline.sports.R;

import java.util.ArrayList;

public class TournamentsAdapter extends ArrayAdapter<Tournament> {
    
	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	
    private static class ViewHolder {
        TextView lblTournamentName;
        TextView lblTournamentStartTime;
        CheckBox chkFollowStatus;
    }
    public TournamentsAdapter(Context context, ArrayList<Tournament> tournaments) {
       super(context, R.layout.item_tournament, tournaments);
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
          convertView = inflater.inflate(R.layout.item_tournament, parent, false);
          viewHolder.lblTournamentName = (TextView) convertView.findViewById(R.id.lblTournamentName);
          viewHolder.chkFollowStatus = (CheckBox) convertView.findViewById(R.id.chkFollowStatus);
          viewHolder.lblTournamentStartTime = (TextView) convertView.findViewById(R.id.lblTournamentStartTime);
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
       
       viewHolder.lblTournamentName.setText(tournament.getTournamentName());
       viewHolder.lblTournamentStartTime.setText(tournament.getTournamentStarttime() + " to " + tournament.getTournamentEndtime());
       viewHolder.chkFollowStatus.setChecked(tournament.getTournamentFollow());
       tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
       btf = Typeface.createFromAsset(getContext().getAssets(), boldFontPath);
       viewHolder.lblTournamentName.setTypeface(btf);
       viewHolder.lblTournamentStartTime.setTypeface(btf);

       // Return the completed view to render on screen
       
       return convertView;
   }
    
    // Function to follow tournament - to be called when checkbox is toggled
    
    public void changeFollowStatusForTournament(){
    	//TODO 
    	Toast.makeText(getContext(), "You will get notifications for this tournament", Toast.LENGTH_SHORT).show();
    }
    
}