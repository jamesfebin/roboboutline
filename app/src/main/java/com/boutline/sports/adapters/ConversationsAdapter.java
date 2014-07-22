package com.boutline.sports.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.boutline.sports.models.Conversation;
import com.boutline.sports.R;

import java.util.ArrayList;

public class ConversationsAdapter extends SimpleCursorAdapter {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;

    Context context;

    public ConversationsAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }
    // View lookup cache

    private static class ViewHolder {
        TextView lblConversationName;
        TextView lblConversationTopic;
        TextView lblLastMessage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare and Assign the fonts

        Cursor c = getCursor();

        if(c.moveToPosition(position)) {
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);

            // Get the data item for this position


            // Check if an existing view is being reused, otherwise inflate the view

            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.item_conversation, parent, false);
                viewHolder.lblConversationName = (TextView) convertView.findViewById(R.id.lblConversationName);
                viewHolder.lblConversationTopic = (TextView) convertView.findViewById(R.id.lblConversationTopic);
                viewHolder.lblLastMessage = (TextView) convertView.findViewById(R.id.lblLastMessage);
                convertView.setTag(viewHolder);

            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Populate the data into the template view using the data object

            viewHolder.lblConversationName.setText(c.getString(c.getColumnIndex(Conversation.COL_NAME)));
            viewHolder.lblConversationTopic.setText(c.getString(c.getColumnIndex(Conversation.COL_TOURNAMENTNAME)));
            viewHolder.lblLastMessage.setText(c.getString(c.getColumnIndex(Conversation.COL_LASTMESSAGE)));

            // Assign the fonts

            viewHolder.lblConversationName.setTypeface(btf);
            viewHolder.lblConversationTopic.setTypeface(tf);
            viewHolder.lblLastMessage.setTypeface(tf);

            // Return the completed view to render on screen
        }
        return convertView;
   }
}