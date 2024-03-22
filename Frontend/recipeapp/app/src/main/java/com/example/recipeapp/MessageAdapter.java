package com.example.recipeapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * @author Ryan McFadden
 */
public class MessageAdapter extends ArrayAdapter<MessageItemObject> {

    public MessageAdapter(Context context, List<MessageItemObject> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MessageItemObject item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
        }

        // Lookup view for data population
        TextView itemDate = convertView.findViewById(R.id.text_chat_date);
        TextView itemMessageSent = convertView.findViewById(R.id.text_chat_message_sent);
        TextView itemTimestampSent = convertView.findViewById(R.id.text_chat_timestamp_sent);
        TextView itemUserReceived = convertView.findViewById(R.id.text_chat_user_received);
        TextView itemMessageReceived = convertView.findViewById(R.id.text_chat_message_received);
        TextView itemTimestampReceived = convertView.findViewById(R.id.text_chat_timestamp_received);

        // Populate the data into the template view using the data object
        itemDate.setText(item.getDate());
        itemMessageSent.setText(item.getMessage());
        itemTimestampSent.setText(item.getTimestamp());
        itemUserReceived.setText(item.getOtherUser());
        //populate sent and received message & timestamp with the same data, hide one later
        itemMessageReceived.setText(item.getMessage());
        itemTimestampReceived.setText(item.getTimestamp());

        // Return the completed view to render on screen
        return convertView;
    }

//    @Override
//    public ListItemObject getItem(int position){
//        return items.get(position);
//    }
}


