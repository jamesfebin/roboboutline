package com.boutline.sports.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boutline.sports.models.Match;
import com.boutline.sports.R;

import java.util.ArrayList;

public class LiveMatchesAdapter extends ArrayAdapter<Match> {
    
	// View lookup cache
    private static class ViewHolder {
        TextView lblMatchName;
        TextView lblMatchStartTime;
        TextView lblMatchVenue;
    }

    public LiveMatchesAdapter(Context context, ArrayList<Match> matches) {
       super(context, R.layout.item_match, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
    	
       Match match = getItem(position);    
       
       // Check if an existing view is being reused, otherwise inflate the view
       
       ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_match, parent, false);
          viewHolder.lblMatchName = (TextView) convertView.findViewById(R.id.lblMatchName);
          viewHolder.lblMatchStartTime = (TextView) convertView.findViewById(R.id.lblMatchStartTime);
          viewHolder.lblMatchVenue = (TextView) convertView.findViewById(R.id.lblMatchVenue);
          convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();
       }
       
       // Populate the data into the template view using the data object
       
       viewHolder.lblMatchName.setText(match.getMatchName());
       viewHolder.lblMatchStartTime.setText(match.getMatchStartTime());
       viewHolder.lblMatchVenue.setText(match.getMatchVenue());
       
       // Return the completed view to render on screen
       
       return convertView;
   }
}