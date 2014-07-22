package com.boutline.sports.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.activities.BoardActivity;
import com.boutline.sports.helpers.FormateTime;
import com.boutline.sports.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpcomingMatchesAdapter extends SimpleCursorAdapter {

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


    public UpcomingMatchesAdapter(Context context, int layout, Cursor c,
                              String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

        this.context = context;
        this.layout = layout;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position


        Cursor c = getCursor();

        if(c.moveToPosition(position)) {
            // Check if an existing view is being reused, otherwise inflate the view

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

            viewHolder.lblMatchName.setText(c.getString(c.getColumnIndex("matchshortname")));


            FormateTime formatit = new FormateTime();
            String formatted =  formatit.formatUnixtime(c.getString(c.getColumnIndex("unixtime")),"dd MMM yyyy, hh:mm a");
            viewHolder.lblMatchStartTime.setText(formatted);
            viewHolder.lblMatchVenue.setText(c.getString(c.getColumnIndex("matchvenue")));

            //Set up fonts
            final Context context = parent.getContext();


            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);


            // Assign the font types

            viewHolder.lblMatchName.setTypeface(btf);
            viewHolder.lblMatchStartTime.setTypeface(btf);
            viewHolder.lblMatchVenue.setTypeface(btf);

            // Return the completed view to render on screen

            final String matchId = c.getString(c.getColumnIndex("_id"));
            final String type = "match";


            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    Toast.makeText(context,"Please wait till the match becomes live.",Toast.LENGTH_SHORT).show();
                    /*
                    Intent intent = new Intent(context, BoardActivity.class);
                    intent.putExtra("mtId",matchId);
                    intent.putExtra("type",type);
                    Activity activity = (Activity) context;
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
*/


                }

            });
        }
        return convertView;

    }
}