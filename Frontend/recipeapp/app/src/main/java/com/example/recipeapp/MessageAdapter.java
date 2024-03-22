package com.example.recipeapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

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
        if(item.getSendingMessage()){
            //message is sent by the user
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
            CardView cardSent = convertView.findViewById(R.id.card_chat_message_sent);
            CardView cardReceived = convertView.findViewById(R.id.card_chat_message_received);

            // Populate the data into the template view using the data object
            //populate sent message & timestamp
            itemDate.setText(item.getDate());
            itemMessageSent.setText(item.getMessage());
            itemTimestampSent.setText(item.getTimestamp());

            //view sent message block
            itemMessageSent.setVisibility(View.VISIBLE);
            itemTimestampSent.setVisibility(View.VISIBLE);
            cardSent.setVisibility(View.VISIBLE);

            //hide received message block
            itemUserReceived.setVisibility(View.INVISIBLE);
            itemMessageReceived.setVisibility(View.INVISIBLE);
            itemTimestampReceived.setVisibility(View.INVISIBLE);
            cardReceived.setVisibility(View.INVISIBLE);


            // Return the completed view to render on screen
            return convertView;

        } else {
            //message is being received by the user
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
            CardView cardSent = convertView.findViewById(R.id.card_chat_message_sent);
            CardView cardReceived = convertView.findViewById(R.id.card_chat_message_received);


            // Populate the data into the template view using the data object
            String tempUser = "user: " + item.getSenderId();
            itemUserReceived.setText(tempUser); //TODO - update to get sender's username

            //populate received user, message, & timestamp
            itemMessageReceived.setText(item.getMessage());
            itemTimestampReceived.setText(item.getTimestamp());
            itemDate.setText(item.getDate());

            //view received message block
            itemUserReceived.setVisibility(View.VISIBLE);
            itemMessageReceived.setVisibility(View.VISIBLE);
            itemTimestampReceived.setVisibility(View.VISIBLE);
            cardReceived.setVisibility(View.VISIBLE);

            //hide sent message block
            itemMessageSent.setVisibility(View.INVISIBLE);
            itemTimestampSent.setVisibility(View.INVISIBLE);
            cardSent.setVisibility(View.INVISIBLE);

            // Return the completed view to render on screen
            return convertView;
        }
    }

//    @Override
//    public ListItemObject getItem(int position){
//        return items.get(position);
//    }
}


