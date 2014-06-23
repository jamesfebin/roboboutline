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
    }

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
       super(context, R.layout.item_message, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	//Set up fonts		
    	Context context = parent.getContext();
    	tf = Typeface.createFromAsset(context.getAssets(), fontPath);
    	btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);
    			
        // Get the data item for this position
        
    	Message message = getItem(position);    
        
    	// Check if an existing view is being reused, otherwise inflate the view
        
    	ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_message, parent, false);
          viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
          viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
          viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
          convertView.setTag(viewHolder);
          
          // Assign the fonts
          
          viewHolder.lblMessage.setTypeface(btf);
          viewHolder.lblSenderName.setTypeface(tf);
          viewHolder.lblMessageTime.setTypeface(tf);
          
        } else {
           viewHolder = (ViewHolder) convertView.getTag();
        }
        
        // Populate the data into the template view using the data object
       
        viewHolder.lblMessage.setText(message.getMessage());
        viewHolder.lblSenderName.setText(message.getUsername());
        viewHolder.lblMessageTime.setText(message.getUnixtime());
        
        // Return the completed view to render on screen
        
        return convertView;
   }
}