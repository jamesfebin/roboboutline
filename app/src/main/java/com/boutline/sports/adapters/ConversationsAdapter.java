package com.boutline.sports.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boutline.sports.models.Conversation;
import com.boutline.sports.R;

import java.util.ArrayList;

public class ConversationsAdapter extends ArrayAdapter<Conversation> {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    
	// View lookup cache

    private static class ViewHolder {
        TextView lblConversationName;
        TextView lblConversationTopic;
        TextView lblLastMessage;
    }

    public ConversationsAdapter(Context context, ArrayList<Conversation> conversations) {
       super(context, R.layout.item_conversation, conversations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare and Assign the fonts

        tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
        btf = Typeface.createFromAsset(getContext().getAssets(), boldFontPath);
    	
       // Get the data item for this position
    	
       Conversation conversation = getItem(position);  
       
       // Check if an existing view is being reused, otherwise inflate the view
       
       ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_conversation, parent, false);
          viewHolder.lblConversationName = (TextView) convertView.findViewById(R.id.lblConversationName);
          viewHolder.lblConversationTopic = (TextView) convertView.findViewById(R.id.lblConversationTopic);
          viewHolder.lblLastMessage = (TextView) convertView.findViewById(R.id.lblLastMessage);
          convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();
       }
       
       // Populate the data into the template view using the data object
       
       viewHolder.lblConversationName.setText(conversation.getConversationName());
       viewHolder.lblConversationTopic.setText(conversation.getTournamentName());
       viewHolder.lblLastMessage.setText(conversation.getLastMessage());

       // Assign the fonts

       viewHolder.lblConversationName.setTypeface(btf);
       viewHolder.lblConversationTopic.setTypeface(tf);
       viewHolder.lblLastMessage.setTypeface(tf);

       // Return the completed view to render on screen

        return convertView;
   }
}