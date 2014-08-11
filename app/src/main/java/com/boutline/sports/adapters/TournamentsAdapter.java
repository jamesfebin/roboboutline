package com.boutline.sports.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.boutline.sports.activities.ChooseMatchActivity;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.helpers.FormateTime;
import com.boutline.sports.jobs.SendTournamentPreferences;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.R;
import com.path.android.jobqueue.JobManager;

public class TournamentsAdapter extends SimpleCursorAdapter {


    public String biSharpFontPath = "fonts/sharpbolditalic.ttf";
    public Typeface bitf;
    public String fontPath = "fonts/sharp.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/sharpbold.ttf";
    public Typeface btf;
    public String proxiFontPath = "fonts/proxinova.ttf";
    public Typeface ptf;
    JobManager jobManager;


    private Context context;
    private int layout;
    BoutDBHelper dbHelper;

    private static class ViewHolder {
        TextView lblTournamentName;
        TextView lblTournamentStartTime;
        CheckBox chkFollowStatus;
        RelativeLayout matchContainer;
        ImageView imgTournament;
    }

    public TournamentsAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Cursor c = getCursor();
        if(c.moveToPosition(position)) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.item_tournament, parent, false);
                viewHolder.lblTournamentName = (TextView) convertView.findViewById(R.id.lblTournamentName);
                viewHolder.chkFollowStatus = (CheckBox) convertView.findViewById(R.id.chkFollowStatus);
                viewHolder.lblTournamentStartTime = (TextView) convertView.findViewById(R.id.lblTournamentStartTime);
                viewHolder.matchContainer = (RelativeLayout) convertView.findViewById(R.id.matchContainer);
                viewHolder.imgTournament = (ImageView) convertView.findViewById(R.id.imgTournament);
                convertView.setTag(viewHolder);
            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }

            final String tournamentId = c.getString(c.getColumnIndex("_id"));
            final String tournamentName = c.getString(c.getColumnIndex("name"));
            final String hashtag = c.getString(c.getColumnIndex("hashtag"));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChooseMatchActivity.class);
                    intent.putExtra("tournamentId", tournamentId);
                    intent.putExtra("tournamentName",tournamentName);
                    intent.putExtra("hashtag",hashtag);
                    Activity activity = (Activity) context;
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                }
            });
            viewHolder.chkFollowStatus.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    changeFollowStatusForTournament(viewHolder.chkFollowStatus.isChecked());
                    jobManager = MyApplication.getInstance().getJobManager();
                    jobManager.addJobInBackground(new SendTournamentPreferences(tournamentId));
                    dbHelper.getInstance(context).updateFollowTorunament(viewHolder.chkFollowStatus.isChecked(), tournamentId);
                }
            });
            FormateTime timeformatter = new FormateTime();

            // Populate the data into the template view using the data object
            viewHolder.lblTournamentName.setText(c.getString(c.getColumnIndex("name")));
            String matchStartTime = timeformatter.formatUnixtime(c.getString(c.getColumnIndex(Tournament.COL_UnixTimeStart)),"dd MMM yyyy");
            String matchEndTime = timeformatter.formatUnixtime(c.getString(c.getColumnIndex(Tournament.COL_UnixTimeEnd)),"dd MMM yyyy");
            viewHolder.lblTournamentStartTime.setText( matchStartTime+ " till "+ matchEndTime);

            int follow;
            Boolean follow_status;
            follow = c.getInt(c.getColumnIndex("followed"));

            if (follow == 1) {
                follow_status = true;
            } else {
                follow_status = false;
            }

            AQuery aq = new AQuery(context);
            ImageOptions options = new ImageOptions();
            options.animation = AQuery.FADE_IN;
            String image_url = "https://boutstorage.blob.core.windows.net/tournaments/" + c.getString(c.getColumnIndex(Tournament.COL_Hashtag)) + ".png";
            Log.d("picture is", image_url);
            aq.id(viewHolder.imgTournament).image(image_url, options);

            viewHolder.chkFollowStatus.setChecked(follow_status);
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            ptf = Typeface.createFromAsset(context.getAssets(), proxiFontPath);
            btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);
            bitf = Typeface.createFromAsset(context.getAssets(), biSharpFontPath);
            viewHolder.lblTournamentName.setTypeface(bitf);
            viewHolder.lblTournamentStartTime.setTypeface(ptf);

            // Return the completed view to render on screen
        }
       return convertView;
   }
    
    // Function to follow tournament - to be called when checkbox is toggled
    
    public void changeFollowStatusForTournament(Boolean follow_status){
        if(follow_status) {
            Toast.makeText(context, "You will get notifications for this tournament", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "You will not get notifications for this tournament", Toast.LENGTH_SHORT).show();
        }
    }
    
}