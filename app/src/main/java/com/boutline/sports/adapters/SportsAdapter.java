package com.boutline.sports.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.jobs.SendSportPreferences;
import com.boutline.sports.models.Sport;
import com.boutline.sports.R;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;

public class SportsAdapter extends SimpleCursorAdapter{

    public String biSharpFontPath = "fonts/sharpbolditalic.ttf";
    public Typeface bitf;
	public String fontPath = "fonts/sharp.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/sharpbold.ttf";
	public Typeface btf;
    JobManager jobManager;

    private Context context;
    private int layout;
    BoutDBHelper dbHelper;

    private static class ViewHolder {
        TextView lblSportName;
        TextView lblManiac;
        TextView lblSportDescription;
        CheckBox chkFollowStatus;
        ImageView imgSport;
        RelativeLayout sportContainer;
    }



    public SportsAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  

        Cursor c = getCursor();

        if(c.moveToPosition(position)) {
            // Check if an existing view is being reused, otherwise inflate the view
            final ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.item_sport, parent, false);
                viewHolder.lblSportName = (TextView) convertView.findViewById(R.id.lblSportName);
                viewHolder.lblSportDescription = (TextView) convertView.findViewById(R.id.lblSportDescription);
                viewHolder.chkFollowStatus = (CheckBox) convertView.findViewById(R.id.chkFollowStatus);
                viewHolder.imgSport = (ImageView) convertView.findViewById(R.id.imgSport);
                viewHolder.lblManiac = (TextView) convertView.findViewById(R.id.lblManiac);
                viewHolder.sportContainer = (RelativeLayout) convertView.findViewById(R.id.sportContainer);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.lblSportName.setText(c.getString(c.getColumnIndex("name")));
            viewHolder.lblSportDescription.setText("10391 FANS");

            if (c.getInt(c.getColumnIndex("followed")) == 1)
                viewHolder.chkFollowStatus.setChecked(true);
            else
                viewHolder.chkFollowStatus.setChecked(false);

            //Remove the following


            AQuery aq = new AQuery(context);
            ImageOptions options = new ImageOptions();

            String image_url = "https://boutstorage.blob.core.windows.net/bout/sport-" + c.getString(c.getColumnIndex("icon"));
            Log.d("picture is",image_url);
            aq.id(viewHolder.imgSport).image(image_url, options);

            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);
            bitf = Typeface.createFromAsset(context.getAssets(), biSharpFontPath);
            viewHolder.lblSportName.setTypeface(bitf);
            viewHolder.lblSportDescription.setTypeface(btf);
            viewHolder.lblManiac.setTypeface(tf);

            final String sportId = c.getString(c.getColumnIndex("_id"));
            viewHolder.sportContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    viewHolder.chkFollowStatus.setChecked(!(viewHolder.chkFollowStatus.isChecked()));
                    Log.i("Choose to follow/unfollow", sportId);
                    jobManager = MyApplication.getInstance().getJobManager();
                    jobManager.addJobInBackground(new SendSportPreferences(sportId));
                    dbHelper.getInstance(context).updateFollowSport(viewHolder.chkFollowStatus.isChecked(), sportId);
                }
            });

            viewHolder.chkFollowStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Log.i("Choose to follow/unfollow", sportId);
                    jobManager = MyApplication.getInstance().getJobManager();
                    jobManager.addJobInBackground(new SendSportPreferences(sportId));
                    dbHelper.getInstance(context).updateFollowSport(viewHolder.chkFollowStatus.isChecked(), sportId);

                }
            });

            // Return the completed view to render on screen
        }
        return convertView;

   }
}