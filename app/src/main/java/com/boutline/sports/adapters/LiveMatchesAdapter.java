package com.boutline.sports.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.boutline.sports.activities.BoardActivity;
import com.boutline.sports.helpers.FormateTime;
import com.boutline.sports.models.Match;
import com.boutline.sports.R;

import java.util.ArrayList;

public class LiveMatchesAdapter extends SimpleCursorAdapter {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;

    Context context;
    private int layout;



    // View lookup cache
    private static class ViewHolder {
        TextView lblMatchName;
        TextView lblMatchStartTime;
        TextView lblMatchVenue;
    }


    public LiveMatchesAdapter(Context context, int layout, Cursor c,
                         String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

        this.context = context;
        this.layout = layout;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
    	
        Cursor c = getCursor();
       // Check if an existing view is being reused, otherwise inflate the view

        if(c.moveToPosition(position)) {
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.item_match, parent, false);
                viewHolder.lblMatchName = (TextView) convertView.findViewById(R.id.lblMatchName);
                viewHolder.lblMatchStartTime = (TextView) convertView.findViewById(R.id.lblMatchStartTime);
                viewHolder.lblMatchVenue = (TextView) convertView.findViewById(R.id.lblMatchVenue);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Populate the data into the template view using the data object

            FormateTime formatit = new FormateTime();
            String formattedTime =  formatit.formatUnixtime(c.getString(c.getColumnIndex("unixtime")),"dd MMM yyyy, hh:mm a");

            viewHolder.lblMatchName.setText(c.getString(c.getColumnIndex("matchshortname")));
            viewHolder.lblMatchStartTime.setText(formattedTime);
            viewHolder.lblMatchVenue.setText(c.getString(c.getColumnIndex("matchvenue")));

            //Set up fonts
            final Context context = parent.getContext();


            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);


            // Assign the font types

            viewHolder.lblMatchName.setTypeface(btf);
            viewHolder.lblMatchStartTime.setTypeface(btf);
            viewHolder.lblMatchVenue.setTypeface(btf);

            final String matchId = c.getString(c.getColumnIndex("_id"));
            final String type = "match";

             String hashtags = c.getString(c.getColumnIndex("hashtags"));


            String hashtagArray[] = hashtags.split(",");
            final String hashtag = hashtagArray[0].replace("[","");

convertView.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {


        Intent intent = new Intent(context, BoardActivity.class);
        intent.putExtra("mtId",matchId);
        intent.putExtra("type",type);
        intent.putExtra("hashtag",hashtag);
        Activity activity = (Activity) context;
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);



    }

});
            // Return the completed view to render on screen
        }
       return convertView;
   }
}