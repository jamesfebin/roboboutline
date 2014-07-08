package com.boutline.sports.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boutline.sports.models.Message;
import com.boutline.sports.R;

import java.util.ArrayList;

public class MessagesAdapter extends ArrayAdapter<Message> {
    

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	
	// View lookup cache
    private static class ViewHolder {
        TextView lblMessage;
        TextView lblSenderName;
        TextView lblMessageTime;
        TextView lblConjunction;
    }

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
       super(context, R.layout.item_leftmessage, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	//Set up fonts		
    	Context context = parent.getContext();
    	tf = Typeface.createFromAsset(context.getAssets(), fontPath);
    	btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);
    			
        // Get the data item for this position
        
    	Message message = getItem(position);
        String botId = "125";
        String currUserId ="123";
    	// Check if an existing view is being reused, otherwise inflate the view
        
    	ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {

          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());

            if(message.getSenderId()==currUserId) {
                convertView = inflater.inflate(R.layout.item_leftmessage, parent, false);
            }
            else if(message.getSenderId()== botId){
                convertView = inflater.inflate(R.layout.item_botmessage, parent, false);
            }
            else{
                convertView = inflater.inflate(R.layout.item_rightmessage, parent, false);
            }

            if(message.getSenderId()!=botId) {

                viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                convertView.setTag(viewHolder);

                // Assign the fonts

                viewHolder.lblSenderName.setTypeface(tf);
                viewHolder.lblMessageTime.setTypeface(tf);
                viewHolder.lblConjunction.setTypeface(tf);
            }
            viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
            viewHolder.lblMessage.setTypeface(btf);

        } else {
           viewHolder = (ViewHolder) convertView.getTag();
        }
        
        // Populate the data into the template view using the data object
        if(message.getSenderId()!=botId) {
            viewHolder.lblSenderName.setText(message.getUsername());
            viewHolder.lblMessageTime.setText(message.getUnixtime());
        }
        viewHolder.lblMessage.setText(message.getMessage());
        // Return the completed view to render on screen
        
        return convertView;
   }
}