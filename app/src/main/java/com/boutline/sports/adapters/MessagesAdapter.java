package com.boutline.sports.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.boutline.sports.helpers.FormateTime;
import com.boutline.sports.models.Message;
import com.boutline.sports.R;

import java.util.ArrayList;

public class MessagesAdapter extends SimpleCursorAdapter {
    

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
    SharedPreferences preferences;
    Context context;
    String lastDate = "";
    public MessagesAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView lblMessage;
        TextView lblSenderName;
        TextView lblMessageTime;
        TextView lblConjunction;
        ImageView imgProPic;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	//Set up fonts

        preferences = context.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        String currentUserId = preferences.getString("boutlineUserId",null);

        FormateTime timeformatter = new FormateTime();

        Cursor c = getCursor();

        if(c.moveToPosition(position)) {
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);

            String cursorUnixtime = c.getString(c.getColumnIndex(Message.COL_TIME));
            String cursorDate = timeformatter.formatUnixtime(cursorUnixtime,"dd MMM");

            // Get the data item for this position


           String botId = "bouty";

           // Check if an existing view is being reused, otherwise inflate the view

            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);

                if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(botId)==true) {
                    convertView = inflater.inflate(R.layout.item_botmessage, parent, false);
                }else if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId)==false) {

                    convertView = inflater.inflate(R.layout.item_leftmessage, parent, false);
                    viewHolder.imgProPic = (ImageView) convertView.findViewById(R.id.messageProPicLeft);
                    AQuery aq = new AQuery(context);
                    ImageOptions options = new ImageOptions();
                    String image_url = c.getString(c.getColumnIndex(Message.COL_USERPICURL));
                    options.round = 50;
                    aq.id(viewHolder.imgProPic).image(image_url, options);
                    viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                    viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                    viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                    convertView.setTag(viewHolder);

                    // Assign the fonts

                    viewHolder.lblSenderName.setTypeface(tf);
                    viewHolder.lblMessageTime.setTypeface(tf);
                    viewHolder.lblConjunction.setTypeface(tf);


                    viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
                    viewHolder.lblMessage.setTypeface(btf);

                    if(cursorDate.matches(lastDate)==false)
                    {
                        // Add to the view
                        lastDate = cursorDate;
                    }


                }  else if(c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId)==true){
                    convertView = inflater.inflate(R.layout.item_rightmessage, parent, false);
                    viewHolder.imgProPic = (ImageView) convertView.findViewById(R.id.messageProPicRight);

                    AQuery aq = new AQuery(context);
                    ImageOptions options = new ImageOptions();
                    options.round = 50;
                    String image_url = c.getString(c.getColumnIndex(Message.COL_USERPICURL));
                    aq.id(viewHolder.imgProPic).image(image_url, options);
                    viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                    viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                    viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                    convertView.setTag(viewHolder);

                    // Assign the fonts

                    viewHolder.lblSenderName.setTypeface(tf);
                    viewHolder.lblMessageTime.setTypeface(tf);
                    viewHolder.lblConjunction.setTypeface(tf);
                    viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
                    viewHolder.lblMessage.setTypeface(btf);

                    if(cursorDate.matches(lastDate)==false)
                    {
                        // Add to the view
                        lastDate = cursorDate;
                    }
                }



            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                LayoutInflater inflater = LayoutInflater.from(context);

                if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(botId)==true) {
                    convertView = inflater.inflate(R.layout.item_botmessage, parent, false);
                }else if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId)==false) {

                    convertView = inflater.inflate(R.layout.item_leftmessage, parent, false);
                    viewHolder.imgProPic = (ImageView) convertView.findViewById(R.id.messageProPicLeft);
                    AQuery aq = new AQuery(context);
                    ImageOptions options = new ImageOptions();
                    String image_url = c.getString(c.getColumnIndex(Message.COL_USERPICURL));
                    options.round = 50;
                    aq.id(viewHolder.imgProPic).image(image_url, options);
                    viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                    viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                    viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                    convertView.setTag(viewHolder);

                    // Assign the fonts

                    viewHolder.lblSenderName.setTypeface(tf);
                    viewHolder.lblMessageTime.setTypeface(tf);
                    viewHolder.lblConjunction.setTypeface(tf);


                    viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
                    viewHolder.lblMessage.setTypeface(btf);
                    if(cursorDate.matches(lastDate)==false)
                    {
                        // Add to the view
                        lastDate = cursorDate;
                    }

                }  else if(c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId)==true){
                    convertView = inflater.inflate(R.layout.item_rightmessage, parent, false);
                    viewHolder.imgProPic = (ImageView) convertView.findViewById(R.id.messageProPicRight);

                    AQuery aq = new AQuery(context);
                    ImageOptions options = new ImageOptions();
                    options.round = 50;
                    String image_url = c.getString(c.getColumnIndex(Message.COL_USERPICURL));
                    aq.id(viewHolder.imgProPic).image(image_url, options);
                    viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                    viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                    viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                    convertView.setTag(viewHolder);

                    // Assign the fonts

                    viewHolder.lblSenderName.setTypeface(tf);
                    viewHolder.lblMessageTime.setTypeface(tf);
                    viewHolder.lblConjunction.setTypeface(tf);
                    viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
                    viewHolder.lblMessage.setTypeface(btf);

                    if(cursorDate.matches(lastDate)==false)
                    {
                        // Add to the view
                        lastDate = cursorDate;
                    }
                }

            }

            // Populate the data into the template view using the data object
            if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(botId)==true) {

              }
            else if(c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId)==false)
            {
                viewHolder.lblSenderName.setText(c.getString(c.getColumnIndex(Message.COL_SENDERNAME)));
                viewHolder.lblMessageTime.setText(timeformatter.formatUnixtime(c.getString(c.getColumnIndex(Message.COL_TIME)),"hh:mm a"));
                viewHolder.lblMessage.setText(c.getString(c.getColumnIndex(Message.COL_MESSAGE)));
            }
            else if(c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId)==true)
            {
                viewHolder.lblSenderName.setText(c.getString(c.getColumnIndex(Message.COL_SENDERNAME)));
                viewHolder.lblMessageTime.setText(timeformatter.formatUnixtime(c.getString(c.getColumnIndex(Message.COL_TIME)),"hh:mm a"));
                viewHolder.lblMessage.setText(c.getString(c.getColumnIndex(Message.COL_MESSAGE)));
       }
            // Return the completed view to render on screen
        }
        return convertView;
   }
}